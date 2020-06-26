package com.skillbox.socialnetwork.main.dto.universal;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class ErrorResponse implements Response{
    private String error;

    @JsonProperty("error_description")
    private String errorDescription;

    public ErrorResponse(String error, String errorDescription){
        this.error = error;
        this.errorDescription = errorDescription;
    }
}
