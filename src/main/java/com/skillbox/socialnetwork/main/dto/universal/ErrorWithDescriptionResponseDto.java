package com.skillbox.socialnetwork.main.dto.universal;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class ErrorWithDescriptionResponseDto extends ErrorResponseDto{

    @JsonProperty("error_description")
    private String errorDescription;

    public ErrorWithDescriptionResponseDto(String error, String errorDescription){
        super(error);
        this.errorDescription = errorDescription;
    }
}
