package com.skillbox.socialnetwork.main.controller;

import com.skillbox.socialnetwork.main.model.Person;
import com.skillbox.socialnetwork.main.model.requests.AuthRequest;
import com.skillbox.socialnetwork.main.model.responses.GeneralResponse;
import com.skillbox.socialnetwork.main.model.responses.MessageResponse;
import com.skillbox.socialnetwork.main.model.responses.PersonInfoResponse;
import com.skillbox.socialnetwork.main.security.jwt.JwtTokenProvider;
import com.skillbox.socialnetwork.main.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.Date;

@RestController
@RequestMapping(value = "/api/v1/auth/")
@Slf4j
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private JwtTokenProvider jwtTokenProvider;
    @Autowired
    private UserService userService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody @Valid AuthRequest authRequest) {
        try {
            String login = authRequest.getEmail();
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(login, authRequest.getPassword()));
            Person person = userService.findByEmail(login);

            if (person == null) {
                throw new UsernameNotFoundException("User with username " + login + " not found");
            }

            String token = jwtTokenProvider.createToken(login, person.getRoles());
            PersonInfoResponse data = personToResponse(person);
            data.setToken(token);
            return ResponseEntity.ok(new GeneralResponse("string", data));
        }
        catch (AuthenticationException e) {
            throw new BadCredentialsException("Invalid username or password");
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletRequest request, HttpServletResponse response) {
        String email = jwtTokenProvider.getUsername(jwtTokenProvider.resolveToken(request));
        userService.findByEmail(email).setLastOnlineTime(new Date());

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null) {
            new SecurityContextLogoutHandler().logout(request, response, auth);
        }

        MessageResponse data = new MessageResponse("ok");
        return ResponseEntity.ok(new GeneralResponse("string", data));
    }

    private PersonInfoResponse personToResponse(Person person) {
        PersonInfoResponse response = new PersonInfoResponse();
        response.setId(person.getId());
        response.setFirstName(person.getFirstName());
        response.setLastName(person.getLastName());
        response.setRegDate(person.getRegDate().getTime());
        response.setEmail(person.getEmail());
        response.setPhone(person.getPhone());
        response.setPhoto(person.getPhoto());
        response.setAbout(person.getAbout());
        response.setCity(person.getTown() != null ? person.getTown().getCity() : null);
        response.setCountry(person.getTown() != null ? person.getTown().getCountry() : null);
        response.setMessagesPermission(person.getMessagesPermission().name());
        response.setLastOnlineTime(person.getLastOnlineTime() == null ? new Date().getTime() : person.getLastOnlineTime().getTime());
        response.setIsBlocked(person.getIsBlocked());
        return response;
    }
}
