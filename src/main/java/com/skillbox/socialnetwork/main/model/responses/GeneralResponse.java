package com.skillbox.socialnetwork.main.model.responses;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.Data;

import java.util.Calendar;

@Data
public class GeneralResponse {

    @JsonView(View.GeneralView.class)
    private String error;

    @JsonView(View.GeneralView.class)
    private long timestamp;

    @JsonView(View.GeneralView.class)
    private Response data;

    public GeneralResponse(String error, Response data) {
        this.error = error;
        this.timestamp = Calendar.getInstance().getTimeInMillis();
        this.data = data;
    }
}
