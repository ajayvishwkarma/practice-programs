package com.adobe.core.raven.service.implementation;

import com.adobe.core.raven.CommonUtils;
import com.adobe.core.raven.constants.JobConstant;
import com.adobe.core.raven.constants.QAConstant;
import com.adobe.core.raven.dto.cgen.Cgen;
import com.adobe.core.raven.dto.cgen.CgenRepository;
import com.adobe.core.raven.dto.job.JobStep;
import com.adobe.core.raven.dto.job.MasterJob;
import com.adobe.core.raven.dto.message.*;
import com.adobe.core.raven.dto.qa.HtmlInfo;
import com.adobe.core.raven.dto.qa.ReshareQaRepository;
import com.adobe.core.raven.repository.CgenInfoRepository;
import com.adobe.core.raven.repository.MasterJobInfoRepository;
import com.adobe.core.raven.service.interfaces.DataService;
import com.adobe.core.raven.service.interfaces.JobExecutorService;
import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.*;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;

public class JobExecutorServiceImpl implements JobExecutorService {
    private static Logger logger = LoggerFactory.getLogger(JobExecutorServiceImpl.class);

    @Autowired
    DataService dataService;

    @Autowired
    @Qualifier(value = "qaJob")
    Job job;

    @Autowired
    @Qualifier(value = "qaJobReshare")
    Job jobReshare;

    @Autowired
    JobLauncher jobLauncher;

    @Autowired
    MasterJobInfoRepository masterJobInfoRepository;

    @Autowired
    CgenInfoRepository cgenInfoRepository;

    public void executeQAJob(String jobId, String stepId){
        HashMap<String,String> htmlBodyMap = dataService.fetchHtmlBody(jobId,stepId);
        String workfrontId = dataService.fetchWorkfrontId(jobId);
        //ArrayList<Cgen> cgenArrayList = dataService.fetchCgen(jobId,stepId);
        ApplicationContext context =
                new ClassPathXmlApplicationContext("beans.xml");
        ThreadPoolTaskExecutor executor = (ThreadPoolTaskExecutor) context.getBean("taskExecutor");

        JobStep jobStep = dataService.fetchJobStep(jobId, stepId);
        masterJobInfoRepository.updateStatus(jobId, stepId, JobConstant.IN_PROGRESS);
        masterJobInfoRepository.updateJobState(jobId, jobStep.getName() + " " + JobConstant.IN_PROGRESS);


        for(Map.Entry<String,String> htmlBody: htmlBodyMap.entrySet()){
            JobParameters qaJobParameters = new JobParametersBuilder().addString(JobConstant.WORKFRONT_ID, workfrontId)
                    .addString(JobConstant.MESSAGE_ID, htmlBody.getKey())
                    .addString(JobConstant.STEP_ID, stepId)
                    .addString(JobConstant.JOB_ID, jobId)
                    .addString(JobConstant.TIMESTAMP, String.valueOf(System.currentTimeMillis()))
                    .addString(JobConstant.HTML_BODY, htmlBody.getValue()).toJobParameters();
           executor.execute(getTask(qaJobParameters));
        }
        executor.shutdown();

    }

    @Override
    public List<String> executeRetestQA(String jobId, List<JobMessageRequest> htmls) {

        // make md5
        List<String> responseList = new ArrayList<String>();
        HashMap<String,String> htmlBodyMap = new HashMap<String,String>();
        String id = "";
        for (JobMessageRequest encodedMsg :  htmls) {
            String md5 = null;
            try {
                md5 = CommonUtils.convertToMD5(encodedMsg.getBase64());
            } catch (NoSuchAlgorithmException e) {
                logger.error(e.getMessage(),e);
            }
            CgenRepository insertedCgenRepository = cgenInfoRepository.getByHash(md5);
            // check md5 in db
            MessageRepository messageRepository = dataService.fetchMessageByMd5(md5);
            //not present parse .msg file
            if (messageRepository == null) {
                //  call parse api
                messageRepository = parseMessageRepository(encodedMsg.getBase64());
                messageRepository = dataService.insertMessage(messageRepository);
                id = createReshareQa(messageRepository, insertedCgenRepository);
                responseList.add(id);
                htmlBodyMap.put(messageRepository.getId(), messageRepository.getHtml().getBody());
            }else{
                ReshareQaRepository reshareQaRepository =  dataService.getReshareQARepository(messageRepository.getId());
                if(reshareQaRepository == null){
                    createReshareQa(messageRepository, insertedCgenRepository);
                    id = messageRepository.getId();
                    htmlBodyMap.put(messageRepository.getId(), messageRepository.getHtml().getBody());
                }
            }
            responseList.add(id);
            // responseList.add(createReshareQa(messageRepository, insertedCgenRepository));
        }

        //}
        //}
        // QA starts from here
        ApplicationContext context =
                new ClassPathXmlApplicationContext("beans.xml");
        ThreadPoolTaskExecutor executor = (ThreadPoolTaskExecutor) context.getBean("taskExecutorReshare");
        String workfrontId = dataService.fetchWorkfrontId(jobId);
        String stepId = getSelfQAStepID(jobId);



        for(Map.Entry<String,String> htmlBody: htmlBodyMap.entrySet()){
            JobParameters qaJobParameters = new JobParametersBuilder().addString(JobConstant.WORKFRONT_ID, workfrontId)
                    .addString(JobConstant.MESSAGE_ID, htmlBody.getKey())
                    .addString(JobConstant.STEP_ID,stepId )
                    .addString(JobConstant.JOB_ID, jobId)
                    .addString(JobConstant.HTML_BODY, htmlBody.getValue()).toJobParameters();
            executor.execute(getTaskReshare(qaJobParameters));
        }
        executor.shutdown();
        return responseList;
    }

    private String getSelfQAStepID(String jobId) {

        MasterJob masterJob = masterJobInfoRepository.get(jobId);
        String stepid = "";
        ArrayList<JobStep> jobSteps=masterJob.getSteps();

        for(JobStep jobStep: jobSteps){

            if(jobStep.getName().equals("Self QA"))
            {
                return jobStep.getId();
            }

        }
        return "";
    }

    /**
     * New method to create and insert Reshare repository object
     * @param messageRepository
     * @param insertedCgenRepository
     * @return
     */
    private String createReshareQa(MessageRepository messageRepository, CgenRepository insertedCgenRepository) {
        ReshareQaRepository reshareQaRepository = new ReshareQaRepository();
        reshareQaRepository.setId(messageRepository.getId());
        reshareQaRepository.setStatus(JobConstant.QAItemStatus);

        String messageActivityId = messageRepository.getActivityId();
        if(messageActivityId != null && !messageActivityId.isEmpty() && insertedCgenRepository != null ) {
            ArrayList<Cgen> content = insertedCgenRepository.getContent();
            Cgen foundCgenContent = content.stream().filter(cgenContent ->
                            cgenContent.getActivityId().equals(messageActivityId))
                    .findAny().orElse(null);
            if (foundCgenContent != null) {
                String segmentName = foundCgenContent.getCreativeFileName();
                reshareQaRepository.setSegment(segmentName);
            }
        }
        dataService.insertMessage(reshareQaRepository);
        return reshareQaRepository.getId();
    }

    public static MessageRepository parseMessageRepository(String base64Str) {

        MessageRepository messageRepository = new MessageRepository();
        MessageContent content = new MessageContent();
        content.setType(QAConstant.MessageFileType);
        content.setBase64(base64Str);
        //messageRepository.setMetadata(metadata);

        messageRepository.setContent(content);

        ParseRequest parseRequest = new ParseRequest();
        parseRequest.setMsgFiles(base64Str);
        Gson gson = new Gson();
        String request = gson.toJson(parseRequest);

        String response = null;
        try {
            response = CommonUtils.callPostUrl(QAConstant.MessageParserUrl, request, null);
        } catch (IOException e) {
            e.printStackTrace();
        }

        if(response != null) {

            ParseResponse parseResponse = gson.fromJson(response, ParseResponse.class);
            String body = parseResponse.getHtmlBody();
            String subject = parseResponse.getSubject();
            String senderName = parseResponse.getSenderName();
            String senderAddress = parseResponse.getSenderAddress();

            String md5 = null;
            try {
                md5 = CommonUtils.convertToMD5(base64Str);
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }
            messageRepository.setMd5(md5);

            messageRepository.setSubject(subject);
            messageRepository.setSenderName(senderName);
            messageRepository.setSenderAddress(senderAddress);

            HtmlInfo htmlInfo = new HtmlInfo();
            htmlInfo.setBody(body);
            messageRepository.setHtml(htmlInfo);

            String[] subjectArray = subject.split("\\[")[1].split(" ");
            String activityId = subjectArray[0];
            String langLocal = subjectArray[1];
            String language = langLocal.split("_")[0];

            messageRepository.setActivityId(activityId);
            messageRepository.setLocale(langLocal);
            messageRepository.setLanguage(language);

        }
        return messageRepository;
    }

    private Runnable getTask(JobParameters qaJobParameters) {
        return () -> {
            try {
                JobExecution execution = jobLauncher.run(job, qaJobParameters);
                System.out.println("STATUS of " + qaJobParameters.getString(JobConstant.MESSAGE_ID) + "-" +execution.getStatus());
            } catch (JobExecutionAlreadyRunningException e) {
                e.printStackTrace();
            } catch (JobRestartException e) {
                e.printStackTrace();
            } catch (JobInstanceAlreadyCompleteException e) {
                e.printStackTrace();
            } catch (JobParametersInvalidException e) {
                e.printStackTrace();
            }
        };
    }
    private Runnable getTaskReshare(JobParameters qaJobParameters) {
        return () -> {
            try {
                JobExecution execution = jobLauncher.run(jobReshare, qaJobParameters);
                System.out.println("STATUS of " + qaJobParameters.getString(JobConstant.MESSAGE_ID) + "-" +execution.getStatus());
            } catch (JobExecutionAlreadyRunningException e) {
                e.printStackTrace();
            } catch (JobRestartException e) {
                e.printStackTrace();
            } catch (JobInstanceAlreadyCompleteException e) {
                e.printStackTrace();
            } catch (JobParametersInvalidException e) {
                e.printStackTrace();
            }
        };
    }

}
