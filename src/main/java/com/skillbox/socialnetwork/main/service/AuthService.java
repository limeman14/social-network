package com.skillbox.socialnetwork.main.service;

import com.skillbox.socialnetwork.main.dto.AbstractResponse;
import com.skillbox.socialnetwork.main.dto.ResponseDto;
import com.skillbox.socialnetwork.main.dto.request.AuthenticationRequestDto;
import com.skillbox.socialnetwork.main.dto.request.RegisterRequestDto;
import com.skillbox.socialnetwork.main.model.Person;

public interface AuthService {
    AbstractResponse login(AuthenticationRequestDto request);
    ResponseDto register(RegisterRequestDto request);
    void logout(String token);
    Person getAuthorizedUser(String token);
    boolean isAuthorized(String token);
}
