package com.skillbox.socialnetwork.main.controller;

import com.skillbox.socialnetwork.main.dto.request.UpdateUserDto;
import com.skillbox.socialnetwork.main.dto.users.PersonResponseFactory;
import com.skillbox.socialnetwork.main.model.Person;
import com.skillbox.socialnetwork.main.security.jwt.JwtTokenProvider;
import com.skillbox.socialnetwork.main.service.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
public class ProfileController {
    private final JwtTokenProvider jwtTokenProvider;
    private final PersonService personService;

    @Autowired
    public ProfileController(JwtTokenProvider jwtTokenProvider, PersonService personService) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.personService = personService;
    }

    @GetMapping("/api/v1/users/me")
    public ResponseEntity<?> getMeProfile(HttpServletRequest request){
        try {
            String email = jwtTokenProvider.getUsername(request.getHeader("Authorization"));
            Person person = personService.findByEmail(email);

            return ResponseEntity.status(HttpStatus.OK).body(PersonResponseFactory.getPerson(person));
        }catch (Exception ex){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @PutMapping("/api/v1/users/me")
    public ResponseEntity<?> updateMeProfile(HttpServletRequest request, @RequestBody UpdateUserDto dto){
        try {
            String email = jwtTokenProvider.getUsername(request.getHeader("Authorization"));
            Person person = personService.findByEmail(email);

            return ResponseEntity.status(HttpStatus.OK).body(PersonResponseFactory.getPerson(person));
        }catch (Exception ex){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }
}
