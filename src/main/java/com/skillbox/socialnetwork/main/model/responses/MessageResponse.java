package com.skillbox.socialnetwork.main.model.responses;

import lombok.Data;

@Data
public class MessageResponse implements ResponseData {

    private String message;

    public MessageResponse(String message) {
        this.message = message;
    }
}
