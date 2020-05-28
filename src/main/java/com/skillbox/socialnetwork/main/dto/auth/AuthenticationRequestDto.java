package com.skillbox.socialnetwork.main.dto.auth;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO class for authentication (login) request.
 *
 * @author Alchin Yuriy
 * @version 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuthenticationRequestDto {
    private String email;
    private String password;
}
