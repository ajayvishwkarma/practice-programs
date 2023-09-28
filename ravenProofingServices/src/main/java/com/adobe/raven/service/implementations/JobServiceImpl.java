package com.adobe.raven.service.implementations;

import com.adobe.raven.Constants;
import com.adobe.raven.MailUtils;
import com.adobe.raven.db.queries.*;
import com.adobe.raven.dto.LanguageCodeRepository;
import com.adobe.raven.dto.geoReviewer.GeoReviewer;
import com.adobe.raven.dto.job.JobStep;
import com.adobe.raven.dto.job.MasterJob;
import com.adobe.raven.dto.message.MessageRepository;
import com.adobe.raven.dto.proof.MailChains;
import com.adobe.raven.dto.qa.QaItem;
import com.adobe.raven.dto.qa.QaRepository;
import com.adobe.raven.dto.qa.ReshareQaRepository;
import com.adobe.raven.dto.user.UserInfo;
import com.adobe.raven.dto.workfront.WorkfrontRepository;
import com.adobe.raven.request.ProofRequest;
import com.adobe.raven.response.ProofResponse;
import com.adobe.raven.service.interfaces.JobService;
import com.mongodb.client.FindIterable;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.mail.Session;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class JobServiceImpl implements JobService {
    @Autowired
    MasterJobQueries masterJobQueries;
    @Autowired
    ProofRepositoryQueries proofRepositoryQueries;
    @Autowired
    UserInfoQueries userInfoQueries;
    @Autowired
    WorkfrontQueries workfrontQueries;
    @Autowired
    GeoReviewerQueries geoReviewerQueries;
    @Autowired
    MessageQueries messageQueries;
    @Autowired
    LanguageRepositoryQueries languageRepositoryQueries;
    @Autowired
    QaRepositoryQueries qaRepositoryQueries;
    @Autowired
    ReshareQAInfoRepository reshareQAInfoRepository;

    @Override
    public  ProofResponse sendQaRetestMails(String jobId, ProofRequest proofRequest) {

        ProofResponse proofResponse = new ProofResponse();
        MasterJob masterJob = masterJobQueries.get(jobId);

        String guIdID = masterJob.getMasterJobMetadata().getLastModifiedById();
        UserInfo createdUserInfo = userInfoQueries.getByGuidID(guIdID);
        WorkfrontRepository workfrontRepository = workfrontQueries.get(masterJob.getWorkfrontId());

        ArrayList<JobStep> steps = masterJob.getSteps();

        JobStep jobStep = steps.stream().filter(step -> step
                        .getName().equalsIgnoreCase(Constants.QALevel_Self))
                .findAny().orElse(null);

        QaRepository qaRepository = qaRepositoryQueries.get(jobStep.getId());
        ArrayList<QaItem> qaItems = qaRepository.getItems();
        List<String> qaItemsId = qaItems.stream().map(x -> x.getId()).collect(Collectors.toList());

        for (String msgId : proofRequest.getMsgIds()) {

            if(!qaItemsId.contains(msgId)){
                ReshareQaRepository reshareQaRepository = reshareQAInfoRepository.get(msgId);
                QaItem qaItem = new QaItem();
                qaItem.setId(reshareQaRepository.getId());
                qaItem.setStatus(reshareQaRepository.getStatus());
                qaItem.setSegment(qaItems.get(0).getSegment());
                qaItem.setBu(qaItems.get(0).getBu());
                qaItem.setCheckList(reshareQaRepository.getCheckList());
                Boolean result = qaRepositoryQueries.insertIteam(jobStep.getId(), qaItem);
            }

            //           }
        }
        ArrayList<MessageRepository> messages = new ArrayList<>();
        QaRepository qaRepositorys = qaRepositoryQueries.get(jobStep.getId());
        ArrayList<QaItem> qaItemss = qaRepositorys.getItems();
        List<String> qaItemsIds = qaItems.stream().map(x -> x.getId()).collect(Collectors.toList());

        for (String qaItemId : qaItemsIds) {

            MessageRepository messageRepository = messageQueries.get(qaItemId);
            messages.add(messageRepository);
        }

       ArrayList<String> toList= proofRequest.getTo();
        GeoReviewer GeoReviewer=geoReviewerQueries.getByEmail(toList.get(0));
        LanguageCodeRepository languageCodeRepository=languageRepositoryQueries.get(GeoReviewer.getLanguage());

        ArrayList<String> mailIds=new ArrayList<>();
         mailIds.addAll(proofRequest.getTo());
        mailIds.addAll(proofRequest.getCc());
//        List<GeoReviewer> geoReviewers=geoReviewerQueries.getByEmail(mailIds);

        String name= mailIds.stream().collect(Collectors.joining("/ "));

        ArrayList<String> ccLists=new ArrayList<String>();
        ccLists.addAll(proofRequest.getCc());

        ccLists.add("proofs@adobe.com");
        if (createdUserInfo != null) {
            ccLists.add(createdUserInfo.getEmailId());
        }
        MailChains mailChains = new MailChains();
        mailChains.setAttachments(messages);


        mailChains.setToList(MailUtils.changeListToString(proofRequest.getTo()));


        mailChains.setCcList(MailUtils.changeListToString(ccLists));

        mailChains.setSubject(proofRequest.getSubject());
//          GeoReviewer geoReviewer = geoReviewerQueries.getByEmail(reviewer.getSendList().get(0));

        String body = Constants.MailBody
                .replaceAll("\\{\\{ReviewerName\\}\\}", name)
                .replaceAll("\\{\\{campaignName\\}\\}", masterJob.getName())
                .replaceAll("\\{\\{deploymentDate\\}\\}", workfrontRepository.getDeploymentDate())
                .replaceAll("\\{\\{languageName\\}\\}",languageCodeRepository.getLanguageName());


        mailChains.setBody(body);
        Session session = MailUtils.outlookAuthenticate();

        boolean isSent = MailUtils.sendLanguageMail(mailChains, session);
        proofResponse.setProofSent(isSent);

        return proofResponse;
    }
}
