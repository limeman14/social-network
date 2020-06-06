package com.skillbox.socialnetwork.main.service;

import com.skillbox.socialnetwork.main.dto.auth.request.AuthenticationRequestDto;
import com.skillbox.socialnetwork.main.dto.auth.request.RegisterRequestDto;
import com.skillbox.socialnetwork.main.dto.universal.ResponseDto;
import com.skillbox.socialnetwork.main.model.Person;

public interface AuthService {
    ResponseDto login(AuthenticationRequestDto request);
    ResponseDto register(RegisterRequestDto request);
    void logout(String token);
    Person getAuthorizedUser(String token);
    boolean isAuthorized(String token);
}
