package com.skillbox.socialnetwork.main.dto.notifications.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.skillbox.socialnetwork.main.dto.universal.Dto;
import com.skillbox.socialnetwork.main.dto.universal.Response;
import com.skillbox.socialnetwork.main.model.NotificationType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NotificationResponseDto implements Dto {

    private int id;

    @JsonProperty("sent_time")
    private long sentTime;

    @JsonProperty("type_id")
    private NotificationType type;

    @JsonProperty("entity_id")
    private int entityId;

    private String info;
}
