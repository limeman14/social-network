package com.skillbox.socialnetwork.main.model.requests;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class AuthRequest {
    @NotBlank
    private String email;

    @NotBlank
    private String password;
}
