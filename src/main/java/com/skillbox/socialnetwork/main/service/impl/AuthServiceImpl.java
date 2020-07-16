package com.skillbox.socialnetwork.main.service.impl;

import com.maxmind.geoip2.exception.GeoIp2Exception;
import com.skillbox.socialnetwork.main.aspect.MethodLogWithTime;
import com.skillbox.socialnetwork.main.dto.GeoIP.GeoIP;
import com.skillbox.socialnetwork.main.dto.auth.request.AuthenticationRequestDto;
import com.skillbox.socialnetwork.main.dto.auth.request.RegisterRequestDto;
import com.skillbox.socialnetwork.main.dto.auth.response.AuthResponseFactory;
import com.skillbox.socialnetwork.main.dto.profile.request.PasswordSetRequestDto;
import com.skillbox.socialnetwork.main.dto.universal.ErrorResponse;
import com.skillbox.socialnetwork.main.dto.universal.Response;
import com.skillbox.socialnetwork.main.dto.universal.ResponseFactory;
import com.skillbox.socialnetwork.main.exception.InvalidRequestException;
import com.skillbox.socialnetwork.main.model.Person;
import com.skillbox.socialnetwork.main.security.jwt.JwtAuthenticationException;
import com.skillbox.socialnetwork.main.security.jwt.JwtTokenProvider;
import com.skillbox.socialnetwork.main.service.AuthService;
import com.skillbox.socialnetwork.main.service.EmailService;
import com.skillbox.socialnetwork.main.service.PersonService;
import com.skillbox.socialnetwork.main.util.CodeGenerator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

@Service
@Slf4j
public class AuthServiceImpl implements AuthService {
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final PersonService personService;
    private final EmailService emailService;
    private final BCryptPasswordEncoder passwordEncoder;
    private final NotificationServiceImpl notificationService;


    @Autowired
    public AuthServiceImpl(AuthenticationManager authenticationManager, JwtTokenProvider jwtTokenProvider, PersonService personService, EmailService emailService, BCryptPasswordEncoder passwordEncoder, NotificationServiceImpl notificationService) {
        this.authenticationManager = authenticationManager;
        this.jwtTokenProvider = jwtTokenProvider;
        this.personService = personService;
        this.emailService = emailService;
        this.passwordEncoder = passwordEncoder;
        this.notificationService = notificationService;
    }

    @Override
    public boolean sendActivationEmail(HttpServletRequest request, String email) {
        return false;
    }

    @Override
    @MethodLogWithTime
    public Response login(AuthenticationRequestDto request, HttpServletRequest httpServletRequest, String referer) {
        String email = request.getEmail();
        try {
            if (referer.contains("?")) {
                URL ub = new URL(referer);
                String token = ub.getQuery().replaceAll("token=", "");
                String[] strings = jwtTokenProvider.getUsername(token).split(":");
                if (strings.length == 2) {
                    Person person = personService.findByEmail(strings[0]);
                    if (person.getConfirmationCode().equals(strings[1])) {
                        person.setPassword(passwordEncoder.encode(request.getPassword()));
                        person.setConfirmationCode("");
                        person.setIsApproved(true);
                        personService.save(person);
                        log.info("Account {} is activated", person.getEmail());
                    }
                }
            }
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, request.getPassword()));
            Person user = personService.findByEmail(email);
            String token = jwtTokenProvider.createToken(email);
            return AuthResponseFactory.getAuthResponse(user, token);
        } catch (DisabledException e) {
            return new ErrorResponse("invalid_request", "activation required");
        } catch (AuthenticationException e) {
            throw new BadCredentialsException("Invalid username or password for user " + email);
        } catch (MalformedURLException e) {
            throw new RuntimeException("Invalid activation url");
        }
    }

    @Override
    @MethodLogWithTime
    public Response register(RegisterRequestDto requestDto, GeoIP location, HttpServletRequest request) throws GeoIp2Exception, IOException {
        Response registration = personService.registration(requestDto, location);
        String email = requestDto.getEmail();
        String url = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort();
        Person person = personService.findByEmail(email);
        person.setConfirmationCode(CodeGenerator.codeGenerator());
        personService.save(person);
        String token = jwtTokenProvider.createToken(person.getEmail() + ":" + person.getConfirmationCode());
        emailService.sendActivationLink(email, person.getFirstName(), url + "/login?token=" + token);
        return registration;
    }

    @Override
    public void logout(String token) {
        try {
            String email = jwtTokenProvider.getUsername(token);
            personService.logout(personService.findByEmail(email));
        } catch (Exception ignored) {
            log.warn("User not found.");
        }
    }

    @Override
    public Person getAuthorizedUser(String token) throws JwtAuthenticationException {
        return personService.findByEmail(jwtTokenProvider.getUsername(token));
    }

    @Override
    public boolean isAuthorized(String token) {
        jwtTokenProvider.getUsername(token);
        return true;
    }

    @Override
    public Response passwordRecovery(String email, String url) {
        Person person = personService.findByEmail(email);
        person.setConfirmationCode(CodeGenerator.codeGenerator());
        personService.save(person);
        String token = jwtTokenProvider.createToken(person.getEmail() + ":" + person.getConfirmationCode());
        emailService.sendPasswordRecovery(email, person.getFirstName(), url + "/change-password?token=" + token);
        log.info("User {} requested password recovery, confirmation email was sent", email);
        return ResponseFactory.responseOk();
    }

    @Override
    public Response passwordSet(PasswordSetRequestDto dto, String referer) {
        try {
            URL ub = new URL(referer);
            dto.setToken(ub.getQuery());
            String token = dto.getToken().replaceAll("token=", "");
            String[] strings = jwtTokenProvider.getUsername(token).split(":");
            if (strings.length == 2) {
                Person person = personService.findByEmail(strings[0]);
                if (person.getConfirmationCode().equals(strings[1])) {
                    person.setPassword(passwordEncoder.encode(dto.getPassword()));
                    person.setConfirmationCode("");
                    personService.save(person);
                    log.info("New password set for user {}", person.getEmail());
                    return ResponseFactory.responseOk();
                }
            }
            log.error("Password change failed: token error");
            throw new InvalidRequestException("token_error");
        } catch (MalformedURLException e) {
            log.error("Password change failed: token not found");
            throw new InvalidRequestException("token not found");
        }
    }


}
