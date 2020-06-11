package com.skillbox.socialnetwork.main.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PasswordSetDto {
    private String password;
    private String token;
}
