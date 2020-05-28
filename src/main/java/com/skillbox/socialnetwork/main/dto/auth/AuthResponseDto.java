package com.skillbox.socialnetwork.main.dto.auth;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.skillbox.socialnetwork.main.model.Permission;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuthResponseDto {
    private String error = "string";
    private Long timestamp;

    private PersonData data;

    @JsonProperty(value = "messages_permission")
    private Permission messagesPermission;

    @JsonProperty(value = "last_online_time")
    private Long lastOnlineTime;

    @JsonProperty(value = "is_blocked")
    private Boolean isBlocked;

    private String token;
}
