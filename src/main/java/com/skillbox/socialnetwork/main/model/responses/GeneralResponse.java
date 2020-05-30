package com.skillbox.socialnetwork.main.model.responses;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;

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
