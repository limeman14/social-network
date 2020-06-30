package com.skillbox.socialnetwork.main.controller;


import com.skillbox.socialnetwork.main.aspect.MethodLogWithTime;
import com.skillbox.socialnetwork.main.dto.auth.request.AuthenticationRequestDto;
import com.skillbox.socialnetwork.main.dto.auth.request.RegisterRequestDto;
import com.skillbox.socialnetwork.main.dto.profile.request.EmailRequestDto;
import com.skillbox.socialnetwork.main.dto.profile.request.PasswordSetRequestDto;
import com.skillbox.socialnetwork.main.dto.universal.Response;
import com.skillbox.socialnetwork.main.dto.universal.ResponseFactory;
import com.skillbox.socialnetwork.main.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
public class AuthenticationRestControllerV1 {

    private final AuthService authService;
//    private final GeoIPLocationService geoService;

    @Autowired
    public AuthenticationRestControllerV1(AuthService authService
//                                          ,GeoIPLocationService geoService
    ) {
        this.authService = authService;
//        this.geoService = geoService;
    }

    @PostMapping("/api/v1/auth/login")
    public ResponseEntity<?> login(@RequestBody AuthenticationRequestDto requestDto) {
        return ResponseEntity.ok(authService.login(requestDto));
    }

    @PostMapping("/api/v1/account/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequestDto requestDto) {
        Response result = authService.register(requestDto);
        return ResponseEntity.ok(result);
    }

    @PostMapping("/api/v1/auth/logout")
    public ResponseEntity<?> logout(@RequestHeader(name = "Authorization") String token) {
        authService.logout(token);
        return ResponseEntity.ok(ResponseFactory.responseOk());
    }

    @PutMapping("/api/v1/account/password/recovery")
    public ResponseEntity<?> passwordRecovery(HttpServletRequest request, @RequestBody EmailRequestDto dto) {
        Response response = authService.passwordRecovery(dto.getEmail(),
                request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort());
        return ResponseEntity.ok(response);
    }

    @PutMapping("/api/v1/account/password/set")
    public ResponseEntity<?> passwordRecovery(
            @RequestHeader(name = "Referer") String referer,
            @RequestBody PasswordSetRequestDto dto
    ) {
        return ResponseEntity.ok(authService.passwordSet(dto, referer));
    }


//    @GetMapping("/GeoIPTest")
//    public GeoIP getLocation(
//            @RequestParam(value = "ipAddress", required = false) String ipAddress,
//            HttpServletRequest request
//    ) throws Exception {
//        String remoteAddress = "";
//        if (request != null) {
//            remoteAddress = request.getHeader("X-FORWARDED-FOR");
//            if (remoteAddress == null || "".equals(remoteAddress)) {
//                remoteAddress = request.getRemoteAddr();
//            }
//        }
//        return geoService.getLocation(ipAddress != null ? ipAddress : remoteAddress);
//    }
}
