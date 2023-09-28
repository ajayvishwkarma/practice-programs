/*************************************************************************
 * ADOBE CONFIDENTIAL ___________________
 * <p/>
 * Copyright 2022 Adobe Systems Incorporated All Rights Reserved.
 * <p/>
 * NOTICE: All information contained herein is, and remains the property of Adobe Systems
 * Incorporated and its suppliers, if any. The intellectual and technical concepts contained herein
 * are proprietary to Adobe Systems Incorporated and its suppliers and are protected by all
 * applicable intellectual property laws, including trade secret and copyright laws. Dissemination
 * of this information or reproduction of this material is strictly forbidden unless prior written
 * permission is obtained from Adobe Systems Incorporated.
 **************************************************************************/

package com.adobe.core.raven.resource;

import com.adobe.asr.connector.ims.IMSConnector;
import com.adobe.asr.exception.dto.ErrorResponse;
import com.adobe.core.raven.config.MyFirstApiResourceProperties;
import com.adobe.core.raven.dto.InputContentModel;
import com.adobe.core.raven.dto.QAResponse;
import com.adobe.core.raven.dto.message.JobRequest;
import com.adobe.core.raven.dto.repo.Mirror;
import com.adobe.core.raven.dto.response.JobResponse;
import com.adobe.core.raven.repository.MirrorInfoRepository;
import com.adobe.core.raven.service.interfaces.*;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


@RestController
@RequestMapping("/ravenservices")
public class RavenApiResource {

      private static Logger logger = LoggerFactory.getLogger(RavenApiResource.class);

      @Autowired
      MyFirstApiResourceProperties props;

      @Autowired
      IMSConnector imsConnector;

      @Autowired
      MsgParserService msgParserService;

      @Autowired
      QAService qaService;

      @Autowired
      JobExecutorService jobExecutorService;

      @Autowired
      PollingService pollingService;

      @Autowired
      DataService dataService;

      @Autowired
      MirrorInfoRepository mirrorInfoRepository;

      @CrossOrigin
      @RequestMapping(value = "/generateQA",method = RequestMethod.POST)
      @ApiResponses(value = { @ApiResponse(code = 500, message = "Something went wrong", response=ErrorResponse.class) })
      public void generateQA(@RequestBody InputContentModel content) {

          qaService.generateQA(content);
    }

    @CrossOrigin
    @RequestMapping(value="/qaRetestJobLauncher",method = RequestMethod.POST)
    public JobResponse  qaRetesetJobLauncher (@RequestParam(required = false) String jobId, String stepId,
                                              @RequestBody(required = true) JobRequest jobRequest) throws Exception{


        List <String> list =  jobExecutorService.executeRetestQA(jobId, jobRequest.getMessages());

        JobResponse jobResponse = new JobResponse();

        jobResponse.setMsgIds(list);

        return jobResponse;
    }

      @CrossOrigin
      @RequestMapping(value="/qaJobLauncher",method = RequestMethod.POST)
      public void qaJobLauncher(@RequestParam(required = false) String jobId, String stepId) throws Exception{
          jobExecutorService.executeQAJob(jobId,stepId);
      }

    @CrossOrigin
    @GetMapping("/pollRaven")
    public QAResponse pollRaven(@RequestParam(required = false) String jobId, String stepId) throws IOException {
        logger.info("Polling Ingestion");
        dataService.mapData();
        return pollingService.pollRaven(jobId, stepId);
    }

    @CrossOrigin
    @PostMapping("/insertMirror")
    public void insertLocale(@RequestBody List<Mirror> mirrors) {

        mirrorInfoRepository.insertAll(mirrors);
    }


    @CrossOrigin
    @GetMapping("/rePollRaven")
    public ArrayList<QAResponse> rePollRaven(@RequestParam(required = false) String jobId, @RequestBody(required = true) JobRequest jobRequest) throws IOException {
        logger.info("Polling Ingestion");


        return pollingService.rePollRaven(jobId, jobRequest);
    }

}