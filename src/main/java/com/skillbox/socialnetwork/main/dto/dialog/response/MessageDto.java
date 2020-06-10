package com.skillbox.socialnetwork.main.dto.dialog.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.skillbox.socialnetwork.main.dto.universal.Dto;
import com.skillbox.socialnetwork.main.model.enumerated.ReadStatus;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MessageDto implements Dto {
    private int id;
    private long time;
    @JsonProperty("author_id")
    private int authorId;
    @JsonProperty("recipient_id")
    private int recipientId;
    @JsonProperty("message_text")
    private String text;
    @JsonProperty("read_status")
    private ReadStatus status;
}
