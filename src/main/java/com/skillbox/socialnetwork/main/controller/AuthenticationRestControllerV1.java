package com.skillbox.socialnetwork.main.controller;


import com.skillbox.socialnetwork.main.dto.auth.request.AuthenticationRequestDto;
import com.skillbox.socialnetwork.main.dto.auth.request.RegisterRequestDto;
import com.skillbox.socialnetwork.main.dto.profile.request.EmailRequestDto;
import com.skillbox.socialnetwork.main.dto.profile.request.PasswordSetRequestDto;
import com.skillbox.socialnetwork.main.dto.universal.*;
import com.skillbox.socialnetwork.main.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
public class AuthenticationRestControllerV1 {

    private final AuthService authService;

    @Autowired
    public AuthenticationRestControllerV1(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/api/v1/auth/login")
    public ResponseEntity<?> login(@RequestBody AuthenticationRequestDto requestDto){
        return ResponseEntity.ok(authService.login(requestDto));
    }

    @PostMapping("/api/v1/account/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequestDto requestDto){
        Response result = authService.register(requestDto);
        return ResponseEntity.ok(result);
    }


    @PostMapping("/api/v1/auth/logout")
    public ResponseEntity<?> logout(@RequestHeader(name = "Authorization") String token) {
        authService.logout(token);
        return ResponseEntity.ok(ResponseFactory.responseOk());
    }

    @PutMapping("/api/v1/account/password/recovery")
    public ResponseEntity<?> passwordRecovery(HttpServletRequest request, @RequestBody EmailRequestDto dto){
        Response response = authService.passwordRecovery(dto.getEmail(),
                request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort());
        return ResponseEntity.ok(response);
    }

    @PutMapping("/api/v1/account/password/set")
    public ResponseEntity<?> passwordRecovery(
            @RequestHeader(name = "Referer") String referer,
            @RequestBody PasswordSetRequestDto dto
    ){
        return ResponseEntity.status(HttpStatus.OK).body(authService.passwordSet(dto, referer));
    }

}
