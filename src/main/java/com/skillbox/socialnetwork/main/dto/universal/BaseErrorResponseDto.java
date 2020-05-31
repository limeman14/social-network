package com.skillbox.socialnetwork.main.dto.universal;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class BaseErrorResponseDto extends ErrorResponseDto{

    @JsonProperty("error_description")
    private String errorDescription;

    public BaseErrorResponseDto(String error, String errorDescription){
        super(error);
        this.errorDescription = errorDescription;
    }
}
