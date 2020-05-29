package com.skillbox.socialnetwork.main.model.responses;

import com.fasterxml.jackson.annotation.JsonView;
import lombok.Data;

import java.util.Calendar;
import java.util.List;

@Data
public class GeneralListResponse {

    @JsonView(View.GeneralView.class)
    private String error;

    @JsonView(View.GeneralView.class)
    private long timestamp;

    @JsonView(View.GeneralView.class)
    private List<Response> data;

    public GeneralListResponse(String error, List<Response> data) {
        this.error = error;
        this.timestamp = Calendar.getInstance().getTimeInMillis();
        this.data = data;
    }
}
