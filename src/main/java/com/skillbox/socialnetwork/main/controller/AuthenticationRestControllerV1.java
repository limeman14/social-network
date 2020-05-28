package com.skillbox.socialnetwork.main.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.skillbox.socialnetwork.main.dto.auth.*;
import com.skillbox.socialnetwork.main.model.Person;
import com.skillbox.socialnetwork.main.security.jwt.JwtTokenProvider;
import com.skillbox.socialnetwork.main.service.PersonService;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;

@RestController
@RequestMapping("/api/v1/")
public class AuthenticationRestControllerV1 {
    private final AuthenticationManager authenticationManager;

    private final JwtTokenProvider jwtTokenProvider;

    private final PersonService personService;

    @Autowired
    public AuthenticationRestControllerV1(AuthenticationManager authenticationManager, JwtTokenProvider jwtTokenProvider, PersonService personService) {
        this.authenticationManager = authenticationManager;
        this.jwtTokenProvider = jwtTokenProvider;
        this.personService = personService;
    }

    @PostMapping("auth/login")
    public ResponseEntity<AuthResponseDto> login(@RequestBody AuthenticationRequestDto requestDto) {
        try {
            String username = requestDto.getEmail();
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, requestDto.getPassword()));
            Person person = personService.findByEmail(username);

            if (person == null) {
                throw new UsernameNotFoundException("User with username: " + username + " not found");
            }

            String token = jwtTokenProvider.createToken(username);

            AuthResponseDto responseDto = new AuthResponseDto();

            responseDto.setIsBlocked(person.getBlocked());
            responseDto.setLastOnlineTime(new Date().getTime());
            responseDto.setMessagesPermission(person.getMessagesPermission());
            responseDto.setTimestamp(new Date().getTime());
            responseDto.setData(new PersonData(person));
            responseDto.setToken(token);

            return ResponseEntity.status(HttpStatus.OK).body(responseDto);
        } catch (AuthenticationException e) {
            throw new BadCredentialsException("Invalid username or password");
        }
    }

    @PostMapping("auth/logout")
    public ResponseEntity<?> logout() {
        LogoutDto responseDto = new LogoutDto();
        responseDto.setTimestamp(new Date().getTime());
        responseDto.setData(new Message());
        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }
}
