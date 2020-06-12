package com.skillbox.socialnetwork.main.dto.dialog;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DialogAddRequest {
    @JsonProperty("user_ids")
    private List<Integer> userIds;
}
