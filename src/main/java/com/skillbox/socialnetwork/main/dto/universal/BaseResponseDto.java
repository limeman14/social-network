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
    private DataDto data;

    public BaseResponseDto(){
        super("");
        timestamp = new Date().getTime();
        data = new DataDto("ok");
    }
}
