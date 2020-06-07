package com.skillbox.socialnetwork.main.dto.profile;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PasswordSetRequestDto {
    private String password;
    private String token;
}
