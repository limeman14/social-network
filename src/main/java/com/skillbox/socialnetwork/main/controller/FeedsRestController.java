package com.skillbox.socialnetwork.main.controller;

import com.skillbox.socialnetwork.main.security.jwt.JwtTokenProvider;
import com.skillbox.socialnetwork.main.service.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
public class FeedsRestController {

    private final JwtTokenProvider jwtTokenProvider;

    private final PersonService personService;

    @Autowired
    public FeedsRestController(AuthenticationManager authenticationManager, JwtTokenProvider jwtTokenProvider, PersonService personService) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.personService = personService;
    }

    @GetMapping("/api/v1/feeds")
    public ResponseEntity getFeeds(HttpServletRequest request){
        return ResponseEntity.status(HttpStatus.OK).build();
    }

}
