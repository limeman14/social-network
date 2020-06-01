package com.skillbox.socialnetwork.main.model.responses;

import lombok.Data;

import java.util.Date;

@Data
public class GeneralResponse {

    private String error;

    private long timestamp;

    private ResponseData data;

    public GeneralResponse(String error, ResponseData data) {
        this.error = error;
        this.timestamp = new Date().getTime();
        this.data = data;
    }
}
