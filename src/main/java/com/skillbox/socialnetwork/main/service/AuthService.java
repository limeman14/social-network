package com.skillbox.socialnetwork.main.service;

import com.maxmind.geoip2.exception.GeoIp2Exception;
import com.skillbox.socialnetwork.main.dto.GeoIP.GeoIP;
import com.skillbox.socialnetwork.main.dto.auth.request.AuthenticationRequestDto;
import com.skillbox.socialnetwork.main.dto.auth.request.RegisterRequestDto;
import com.skillbox.socialnetwork.main.dto.profile.request.PasswordSetRequestDto;
import com.skillbox.socialnetwork.main.dto.universal.Response;
import com.skillbox.socialnetwork.main.model.Person;

import java.io.IOException;

public interface AuthService {
    Response login(AuthenticationRequestDto request);

    Response register(RegisterRequestDto request, GeoIP location) throws IOException, GeoIp2Exception;

    void logout(String token);

    Person getAuthorizedUser(String token);

    boolean isAuthorized(String token);

    Response passwordRecovery(String email, String url);

    Response passwordSet(PasswordSetRequestDto dto, String referer);
}
