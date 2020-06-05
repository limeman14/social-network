package com.skillbox.socialnetwork.main.service.impl;

import com.skillbox.socialnetwork.main.dto.AbstractResponse;
import com.skillbox.socialnetwork.main.dto.ResponseDto;
import com.skillbox.socialnetwork.main.dto.auth.response.AuthResponseFactory;
import com.skillbox.socialnetwork.main.dto.request.AuthenticationRequestDto;
import com.skillbox.socialnetwork.main.dto.request.RegisterRequestDto;
import com.skillbox.socialnetwork.main.model.Person;
import com.skillbox.socialnetwork.main.security.jwt.JwtAuthenticationException;
import com.skillbox.socialnetwork.main.security.jwt.JwtTokenProvider;
import com.skillbox.socialnetwork.main.service.AuthService;
import com.skillbox.socialnetwork.main.service.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final PersonService personService;

    @Autowired
    public AuthServiceImpl(AuthenticationManager authenticationManager, JwtTokenProvider jwtTokenProvider, PersonService personService) {
        this.authenticationManager = authenticationManager;
        this.jwtTokenProvider = jwtTokenProvider;
        this.personService = personService;
    }

    @Override
    public AbstractResponse login(AuthenticationRequestDto request) {
        try {
            String email = request.getEmail();
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, request.getPassword()));
            Person user = personService.findByEmail(email);

            if (user == null) {
                throw new UsernameNotFoundException("User with username: " + email + " not found");
            }

            String token = jwtTokenProvider.createToken(email);
            return AuthResponseFactory.getAuthResponse(user, token);
        } catch (AuthenticationException e) {
            throw new BadCredentialsException("Invalid username or password");
        }
    }

    @Override
    public ResponseDto register(RegisterRequestDto request) {
        ResponseDto responseDto = personService.registration(request);
        return responseDto;
    }

    @Override
    public void logout(String token) {
        String email = jwtTokenProvider.getUsername(token);
        personService.logout(personService.findByEmail(email));
    }

    @Override
    public Person getAuthorizedUser(String token) throws JwtAuthenticationException {
        return personService.findByEmail(jwtTokenProvider.getUsername(token));
    }

    @Override
    public boolean isAuthorized(String token) {
        try {
            jwtTokenProvider.getUsername(token);
            return true;
        } catch (JwtAuthenticationException e){
            return false;
        }
    }
}
