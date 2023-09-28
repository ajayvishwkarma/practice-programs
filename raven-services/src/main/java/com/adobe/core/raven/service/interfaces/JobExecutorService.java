package com.adobe.core.raven.service.interfaces;


import com.adobe.core.raven.dto.message.JobMessageRequest;

import java.util.List;

public interface JobExecutorService {

   void executeQAJob(String jobId, String stepId);
   List<String> executeRetestQA(String jobId, List<JobMessageRequest> htmls);

   }
