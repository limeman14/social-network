package com.skillbox.socialnetwork.main.dto.universal;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;

@AllArgsConstructor
@Data
public class BaseResponseDto implements ResponseDto {
    private String error;
    private Long timestamp;
    private ResponseDto data;

    public BaseResponseDto(ResponseDto data) {
        error = "string";
        timestamp = new Date().getTime();
        this.data = data;
    }
}
