package com.skillbox.socialnetwork.main.service;

import com.skillbox.socialnetwork.main.dto.auth.request.AuthenticationRequestDto;
import com.skillbox.socialnetwork.main.dto.auth.request.RegisterRequestDto;
import com.skillbox.socialnetwork.main.dto.profile.EmailRequestDto;
import com.skillbox.socialnetwork.main.dto.profile.PasswordSetRequestDto;
import com.skillbox.socialnetwork.main.dto.universal.ResponseDto;
import com.skillbox.socialnetwork.main.model.Person;

import javax.servlet.http.HttpServletRequest;

public interface AuthService {
    ResponseDto login(AuthenticationRequestDto request);
    ResponseDto register(RegisterRequestDto request);
    void logout(String token);
    Person getAuthorizedUser(String token);
    boolean isAuthorized(String token);
    ResponseDto passwordRecovery(String email, String url);
    ResponseDto passwordSet(PasswordSetRequestDto dto, String referer);
}
