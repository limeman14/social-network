package com.skillbox.socialnetwork.main.dto.universal;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class ErrorResponseDto implements ResponseDto{
    private String error;

    @JsonProperty("error_description")
    private String errorDescription;

    public ErrorResponseDto(String error, String errorDescription){
        this.error = error;
        this.errorDescription = errorDescription;
    }
}
