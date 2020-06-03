package com.skillbox.socialnetwork.main.controller;

import com.skillbox.socialnetwork.main.dto.ResponseDto;
import com.skillbox.socialnetwork.main.dto.request.AuthenticationRequestDto;
import com.skillbox.socialnetwork.main.dto.auth.response.AuthResponseFactory;
import com.skillbox.socialnetwork.main.dto.request.RegisterRequestDto;
import com.skillbox.socialnetwork.main.dto.universal.BaseResponseDto;
import com.skillbox.socialnetwork.main.dto.universal.ErrorResponseDto;
import com.skillbox.socialnetwork.main.model.Person;
import com.skillbox.socialnetwork.main.security.jwt.JwtTokenProvider;
import com.skillbox.socialnetwork.main.service.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@RestController
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

    @PostMapping("/api/v1/auth/login")
    public ResponseEntity<?> login(@RequestBody AuthenticationRequestDto requestDto){
        try {
            String email = requestDto.getEmail();
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, requestDto.getPassword()));
            Person user = personService.findByEmail(email);

            if (user == null) {
                throw new UsernameNotFoundException("User with username: " + email + " not found");
            }

            String token = jwtTokenProvider.createToken(email);
            return ResponseEntity.ok(AuthResponseFactory.getAuthResponse(user, token));
        } catch (AuthenticationException e) {
            throw new BadCredentialsException("Invalid username or password");
        }
    }

    @PostMapping("/api/v1/account/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequestDto requestDto){
        ResponseDto responseDto = personService.registration(requestDto);

        return ResponseEntity
                .status(
                        ((ErrorResponseDto)responseDto).getError().equals("invalid_request") ?
                                HttpStatus.BAD_REQUEST : HttpStatus.OK)
                .body(responseDto);
    }


    @PostMapping("/api/v1/auth/logout")
    public ResponseEntity<?> logout(@RequestHeader(name = "Authorization") String token) {
        String email = jwtTokenProvider.getUsername(token);
        personService.logout(personService.findByEmail(email));
        return ResponseEntity.status(HttpStatus.OK).body(new BaseResponseDto());
    }

}
