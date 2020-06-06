package com.skillbox.socialnetwork.main.controller;


import com.skillbox.socialnetwork.main.dto.auth.request.AuthenticationRequestDto;
import com.skillbox.socialnetwork.main.dto.auth.request.RegisterRequestDto;
import com.skillbox.socialnetwork.main.dto.universal.BaseResponseDto;
import com.skillbox.socialnetwork.main.dto.universal.ErrorResponseDto;
import com.skillbox.socialnetwork.main.dto.universal.MessageResponseDto;
import com.skillbox.socialnetwork.main.dto.universal.ResponseDto;
import com.skillbox.socialnetwork.main.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

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
        ResponseDto result = authService.register(requestDto);

        return ResponseEntity
                .status(
                        ((ErrorResponseDto)result).getError().equals("invalid_request") ?
                                HttpStatus.BAD_REQUEST : HttpStatus.OK)
                .body(result);
    }


    @PostMapping("/api/v1/auth/logout")
    public ResponseEntity<?> logout(@RequestHeader(name = "Authorization") String token) {
        authService.logout(token);
        return ResponseEntity.ok(new BaseResponseDto(new MessageResponseDto("ok")));
    }

}
