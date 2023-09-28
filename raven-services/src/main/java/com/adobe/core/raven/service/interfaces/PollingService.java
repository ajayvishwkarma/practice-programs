package com.adobe.core.raven.service.interfaces;


import com.adobe.core.raven.dto.QAResponse;
import com.adobe.core.raven.dto.cgen.Cgen;
import com.adobe.core.raven.dto.message.JobRequest;

import java.util.ArrayList;

public interface PollingService {

   QAResponse pollRaven(String jobId, String stepId);
   ArrayList<QAResponse> rePollRaven(String jobId, JobRequest jobRequest );

   }
