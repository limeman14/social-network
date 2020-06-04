package com.skillbox.socialnetwork.main.dto.universal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@Data
public class BaseResponseDto extends ErrorResponseDto {

    private Long timestamp;
    private ResponseDto data;

    public BaseResponseDto(ResponseDto data) {
        super("string");
        timestamp = new Date().getTime();
        this.data = data;
    }
}
