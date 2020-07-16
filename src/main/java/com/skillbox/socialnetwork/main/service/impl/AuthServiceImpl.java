package com.skillbox.socialnetwork.main.service.impl;

import com.maxmind.geoip2.exception.GeoIp2Exception;
import com.skillbox.socialnetwork.main.aspect.MethodLogWithTime;
import com.skillbox.socialnetwork.main.dto.GeoIP.GeoIP;
import com.skillbox.socialnetwork.main.dto.auth.request.AuthenticationRequestDto;
import com.skillbox.socialnetwork.main.dto.auth.request.RegisterRequestDto;
import com.skillbox.socialnetwork.main.dto.auth.response.AuthResponseFactory;
import com.skillbox.socialnetwork.main.dto.profile.request.EmailRequestDto;
import com.skillbox.socialnetwork.main.dto.profile.request.PasswordSetRequestDto;
import com.skillbox.socialnetwork.main.dto.universal.BaseResponse;
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
import com.skillbox.socialnetwork.main.util.LastOnlineTimeAdjuster;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
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
            LastOnlineTimeAdjuster.refreshLastOnlineTime(user);
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
    public Response passwordRecovery(String email, String url, String viewName) {
        Person person = personService.findByEmail(email);
        person.setConfirmationCode(CodeGenerator.codeGenerator());
        personService.save(person);
        String token = jwtTokenProvider.createToken(person.getEmail() + ":" + person.getConfirmationCode());
        emailService.sendPasswordRecovery(email, person.getFirstName(), url + "/"+viewName+"?token=" + token);
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

    @Override
    public Response sendEmailChangeLetter(HttpServletRequest request, String token)
    {
        String email = jwtTokenProvider.getUsername(token);
        String url =
                request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort();
        Person person = personService.findByEmail(email);
        person.setConfirmationCode(CodeGenerator.codeGenerator());
        personService.save(person);
        String secret = jwtTokenProvider.createToken(person.getEmail() + ":" + person.getConfirmationCode());
        emailService.sendSimpleMessage(email, person.getFirstName()+", Вот ссылка для изменения почты вашего аккаунта : "+ url + "/shift-email?token=" + secret);
        log.info("User {} requested email change, confirmation email was sent", email);
        return ResponseFactory.responseOk();
    }

    @Override
    public Response changeEmail(String token, EmailRequestDto request, String referer)
    {
        try {
            URL ub = new URL(referer);
            String[] strings = jwtTokenProvider.getUsername(ub.getQuery().replaceAll("token=", "")).split(":");
            if(strings.length == 2){
                Person person = personService.findByEmail(strings[0]);
                if(person.getConfirmationCode().equals(strings[1])){
                    person.setEmail(request.getEmail());
                    person.setConfirmationCode("");
                    personService.save(person);
                    emailService.sendSimpleMessage(request.getEmail(), "Отныне для входа в Ваш аккаунт соцсети будет использоваться данная почта");
                    log.info("New email set for user {}", person.getEmail());
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
