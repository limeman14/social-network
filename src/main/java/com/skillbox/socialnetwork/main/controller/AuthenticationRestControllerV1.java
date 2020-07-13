package com.skillbox.socialnetwork.main.controller;

import com.maxmind.geoip2.exception.GeoIp2Exception;
import com.skillbox.socialnetwork.main.dto.GeoIP.GeoIP;
import com.skillbox.socialnetwork.main.dto.auth.request.AuthenticationRequestDto;
import com.skillbox.socialnetwork.main.dto.auth.request.RegisterRequestDto;
import com.skillbox.socialnetwork.main.dto.notifications.request.NotificationSettingRequestDto;
import com.skillbox.socialnetwork.main.dto.profile.request.EmailRequestDto;
import com.skillbox.socialnetwork.main.dto.profile.request.PasswordSetRequestDto;
import com.skillbox.socialnetwork.main.dto.universal.Response;
import com.skillbox.socialnetwork.main.dto.universal.ResponseFactory;
import com.skillbox.socialnetwork.main.security.jwt.JwtUser;
import com.skillbox.socialnetwork.main.service.AuthService;
import com.skillbox.socialnetwork.main.service.GeoIPLocationService;
import com.skillbox.socialnetwork.main.service.NotificationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@Slf4j
@RestController
public class AuthenticationRestControllerV1 {

    private final AuthService authService;
    private final GeoIPLocationService geoService;
    private final NotificationService notificationService;

    @Autowired
    public AuthenticationRestControllerV1(AuthService authService,
            GeoIPLocationService geoService,
            NotificationService notificationService)
    {
        this.authService = authService;
        this.geoService = geoService;
        this.notificationService = notificationService;
    }

    @PostMapping("/api/v1/auth/login")
    public ResponseEntity<?> login(@RequestBody AuthenticationRequestDto requestDto)
    {
        return ResponseEntity.ok(authService.login(requestDto));
    }

    @PostMapping("/api/v1/account/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequestDto requestDto, HttpServletRequest request) throws Exception
    {
        GeoIP location;
        try
        {
            location = getLocation(null, request);
        } catch (GeoIp2Exception e)
        {
            log.warn("Registration from localhost", e);
            location = new GeoIP(null, "localhost", "localhost", "0.00", "0.00");
        }
        Response result = authService.register(requestDto, location);
        return ResponseEntity.ok(result);
    }


    @PostMapping("/api/v1/auth/logout")
    public ResponseEntity<?> logout(@RequestHeader(name = "Authorization") String token)
    {
        authService.logout(token);
        return ResponseEntity.ok(ResponseFactory.responseOk());
    }

    @PutMapping("/api/v1/account/password/recovery")
    public ResponseEntity<?> passwordRecovery(HttpServletRequest request, @RequestBody EmailRequestDto dto)
    {
        Response response = authService.passwordRecovery(dto.getEmail(),
                request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort(), "change-password");
        return ResponseEntity.ok(response);
    }

    @PutMapping("/api/v1/account/password/set")
    public ResponseEntity<?> passwordRecovery(
            @RequestHeader(name = "Referer") String referer,
            @RequestBody PasswordSetRequestDto dto
                                             )
    {
        return ResponseEntity.ok(authService.passwordSet(dto, referer));
    }

    @PutMapping("/api/v1/account/password/change")
    public ResponseEntity<?> passwordChange(HttpServletRequest request, @RequestHeader(name = "Authorization") String token)
    {
        return ResponseEntity.ok(authService.passwordRecovery(authService.getAuthorizedUser(token).getEmail(),
                request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort(), "shift-password"));
    }

    @PutMapping("/api/v1/account/email/change")
    public ResponseEntity<?> sendEmailChangeLetter(HttpServletRequest request,
            @RequestHeader(name = "Authorization") String token)
    {
        return ResponseEntity.ok(authService.sendEmailChangeLetter(request, token));
    }


    @PutMapping("/api/v1/account/email")
    public ResponseEntity<?> changeEmail(@RequestHeader(name = "Authorization") String token, @RequestBody EmailRequestDto request,
            @RequestHeader(name = "Referer") String referer)
    {
        return ResponseEntity.ok(authService.changeEmail(token, request, referer));
    }

    @GetMapping("/api/v1/account/notifications")
    public ResponseEntity<?> getNotificationSettings(@AuthenticationPrincipal JwtUser user)
    {
        return ResponseEntity.ok(notificationService.getNotificationSettings(user.getId()));
    }

    @PutMapping("/api/v1/account/notifications")
    public ResponseEntity<?> changeNotificationSetting(
            @AuthenticationPrincipal JwtUser user,
            @RequestBody NotificationSettingRequestDto dto
                                                      )
    {
        return ResponseEntity.ok(notificationService.changeNotificationSetting(user.getId(), dto));
    }


    @GetMapping("/GeoIPTest")
    public GeoIP getLocation(
            @RequestParam(value = "ipAddress", required = false) String ipAddress,
            HttpServletRequest request
                            ) throws IOException, GeoIp2Exception
    {
        String remoteAddress = "";
        if (request != null)
        {
            remoteAddress = request.getHeader("X-FORWARDED-FOR");
            if (remoteAddress == null || "".equals(remoteAddress))
            {
                remoteAddress = request.getRemoteAddr();
            }
        }
        return geoService.getLocation(ipAddress != null ? ipAddress : remoteAddress);
    }
}
