package com.skillbox.socialnetwork.main.dto.dialog.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.skillbox.socialnetwork.main.dto.universal.Dto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
public class MessageIdDto implements Dto {
    @JsonProperty("message_id")
    private int messageId;
}
