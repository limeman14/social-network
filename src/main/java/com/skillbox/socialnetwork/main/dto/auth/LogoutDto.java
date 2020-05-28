package com.skillbox.socialnetwork.main.dto.auth;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LogoutDto {
    private String error = "string";
    private Long timestamp;
    private Message data;
}
