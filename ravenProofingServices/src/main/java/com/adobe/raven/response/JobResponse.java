package com.adobe.raven.response;

import com.adobe.raven.dto.ResponseError;
import com.adobe.raven.dto.job.MasterJob;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public @Data
class JobResponse {

    ResponseError error;
    private ArrayList<ResponseError> errors;
    private ArrayList<ResponseError> warnings;

    private Boolean jobCreated;
    private Boolean jobUpdated;
    private Boolean validData;
    private String jobId;
    private List<MasterJob> result;

    // for job details
    private MasterJob jobResult;
}
