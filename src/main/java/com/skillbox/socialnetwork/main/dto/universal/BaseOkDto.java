package com.skillbox.socialnetwork.main.dto.universal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@Data
public class BaseOkDto extends ErrorResponseDto {
    private Long timestamp;
    private DataDto data;

    public BaseOkDto(){
        super("string");
        timestamp = new Date().getTime();
        data = new DataDto("ok");
    }
}
