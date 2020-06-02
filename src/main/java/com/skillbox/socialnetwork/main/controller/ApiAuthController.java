package com.skillbox.socialnetwork.main.controller;

import com.fasterxml.jackson.annotation.JsonView;
import com.mysql.cj.xdevapi.Collection;
import com.skillbox.socialnetwork.main.model.Person;
import com.skillbox.socialnetwork.main.model.Role;
import com.skillbox.socialnetwork.main.model.enumerated.ERole;
import com.skillbox.socialnetwork.main.model.requests.LoginRequest;
import com.skillbox.socialnetwork.main.model.requests.SignupRequest;
import com.skillbox.socialnetwork.main.model.responses.*;
import com.skillbox.socialnetwork.main.repo.PersonRepository;

import com.skillbox.socialnetwork.main.security.UserDetailsImpl;
import com.skillbox.socialnetwork.main.security.jwt.JwtUtils;
import com.skillbox.socialnetwork.main.security.service.UserService;
import com.skillbox.socialnetwork.main.security.service.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class ApiAuthController {
    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    PersonRepository personRepository;


    @Autowired
    PasswordEncoder encoder;

    @Autowired
    UserServiceImpl userService;

    @Autowired
    JwtUtils jwtUtils;

    @JsonView(View.LoginResponse.class)
    @PostMapping("/auth/login")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));

        Person person = userService.findByEmail(loginRequest.getEmail());
        String token = jwtUtils.createToken(loginRequest.getEmail(), person.getRoles());

        JwtResponse data = personToJwt(person);
        data.setToken(token);

        GeneralResponse model = new GeneralResponse(null, data);

        return ResponseEntity.ok(model);
    }

    @PostMapping("/auth/logout")
    public ResponseEntity<?> logoutUser(@RequestHeader(name = "Authorization") String token){
        String email = jwtUtils.getUsername(token);
        userService.logout(userService.findByEmail(email));
        /*Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null) {
            System.out.println(auth.getPrincipal());
            Person person = personRepository.findByEmail(auth.getName()).orElseThrow(() -> new UsernameNotFoundException("User Not Found "));
            System.out.println(person.getEmail());
            person.setLastOnline(Calendar.getInstance());
            new SecurityContextLogoutHandler().logout(request, response, auth);
        }*/
        MessageResponse data = new MessageResponse("ok");
        return ResponseEntity.ok(new GeneralResponse(null, data));
    }

    @PostMapping("/account/register")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signUpRequest) {

        if (personRepository.existsByEmail(signUpRequest.getEmail())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Email is already in use!"));
        }

        // Create new user's account
        Person person = new Person();
        person.setFirstName(signUpRequest.getFirstName());
        person.setLastName(signUpRequest.getLastName());
        person.setEmail(signUpRequest.getEmail());
        person.setPassword(signUpRequest.getPasswd1());
        userService.register(person);

        return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
    }

    @JsonView(View.MyInfoResponse.class)
    @GetMapping("/users/me")
    public ResponseEntity<?> getMyInfo(@RequestHeader(name = "Authorization") String token){
        String email = jwtUtils.getUsername(token);
        System.out.println(personRepository.findByEmail(email).get().getEmail());
        Person person = personRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("User Not Found "));
        System.out.println(person.getEmail());
        JwtResponse response = personToJwt(person);

        GeneralResponse model = new GeneralResponse(null, response);

        return ResponseEntity.ok(model);
    }

    private JwtResponse personToJwt(Person person){
        JwtResponse jwtResponse = new JwtResponse();
        jwtResponse.setId(person.getId());
        jwtResponse.setFirstName(person.getFirstName());
        jwtResponse.setLastName(person.getLastName());
        jwtResponse.setRegDate(person.getRegDate().getTimeInMillis());
        jwtResponse.setEmail(person.getEmail());
        jwtResponse.setPhone(person.getPhone());
        jwtResponse.setPhoto(person.getPhoto());
        jwtResponse.setAbout(person.getAbout());
        jwtResponse.setCity(person.getTown() != null ? person.getTown().getCity() : null);
        jwtResponse.setCountry(person.getTown() != null ? person.getTown().getCountry() : null);
        jwtResponse.setMessagesPermission(person.getMessagesPermission());
        jwtResponse.setLastOnlineTime(person.getLastOnline() == null ? Calendar.getInstance().getTimeInMillis() : person.getLastOnline().getTimeInMillis());
        jwtResponse.setBlocked(person.getBlocked());
        return jwtResponse;
    }
}
