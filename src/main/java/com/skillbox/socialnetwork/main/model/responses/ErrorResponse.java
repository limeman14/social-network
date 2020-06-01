package com.skillbox.socialnetwork.main.model.responses;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class ErrorResponse implements ResponseData{
    private String error;

    @JsonProperty("error_description")
    private String errorDescription;

    public ErrorResponse(String error, String errorDescription) {
        this.error = error;
        this.errorDescription = errorDescription;
    }
}
