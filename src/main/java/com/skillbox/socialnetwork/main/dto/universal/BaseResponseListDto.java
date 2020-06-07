package com.skillbox.socialnetwork.main.dto.universal;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
public class BaseResponseListDto implements ResponseDto{
    private String error;
    private long timestamp;
    private Integer total;
    private Integer offset;
    private Integer perPage;
    private List<ResponseDto> data;

    public BaseResponseListDto(List<ResponseDto> data) {
        error = "string";
        timestamp = new Date().getTime();
        this.data = data;
    }

    public BaseResponseListDto(Integer total, Integer offset, Integer perPage, List<ResponseDto> data) {
        error = "string";
        timestamp = new Date().getTime();
        this.total = total;
        this.offset = offset;
        this.perPage = perPage;
        this.data = data;
    }
}
