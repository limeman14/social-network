package com.skillbox.socialnetwork.main.service.impl;

import com.skillbox.socialnetwork.main.dto.auth.request.AuthenticationRequestDto;
import com.skillbox.socialnetwork.main.dto.auth.request.RegisterRequestDto;
import com.skillbox.socialnetwork.main.dto.auth.response.AuthResponseFactory;
import com.skillbox.socialnetwork.main.dto.profile.request.PasswordSetRequestDto;
import com.skillbox.socialnetwork.main.dto.universal.*;
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
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

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


    @Autowired
    public AuthServiceImpl(AuthenticationManager authenticationManager, JwtTokenProvider jwtTokenProvider, PersonService personService, EmailService emailService, BCryptPasswordEncoder passwordEncoder) {
        this.authenticationManager = authenticationManager;
        this.jwtTokenProvider = jwtTokenProvider;
        this.personService = personService;
        this.emailService = emailService;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @MethodLogWithTime
    public BaseResponse login(AuthenticationRequestDto request) {
        String email = request.getEmail();
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, request.getPassword()));
            Person user = personService.findByEmail(email);
            String token = jwtTokenProvider.createToken(email);
            return AuthResponseFactory.getAuthResponse(user, token);
        } catch (AuthenticationException e) {
            throw new BadCredentialsException("Invalid username or password for user " + email);
        }
    }

    @Override
    @MethodLogWithTime
    public Response register(RegisterRequestDto request, GeoIP location) throws GeoIp2Exception, IOException {
        Response registration = personService.registration(request,location);
        emailService.sendSimpleMessageUsingTemplate(request.getEmail(), request.getFirstName(), "Рады приветствовать Вас на нашем ресурсе!");
        return registration;
    }

    @Override
    public void logout(String token) {
        try {
            String email = jwtTokenProvider.getUsername(token);
            personService.logout(personService.findByEmail(email));
        }catch (Exception ignored){
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
            if(strings.length == 2){
                Person person = personService.findByEmail(strings[0]);
                if(person.getConfirmationCode().equals(strings[1])){
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
