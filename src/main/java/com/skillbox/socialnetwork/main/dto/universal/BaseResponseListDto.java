package com.skillbox.socialnetwork.main.dto.universal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
public class BaseResponseListDto extends ErrorResponseDto{
    private Long timestamp;
    private List<ResponseDto> data;

    public BaseResponseListDto(List<ResponseDto> data) {
        super("string");
        timestamp = new Date().getTime();
        this.data = data;
    }
}
