package com.skillbox.socialnetwork.main.controller;

import com.fasterxml.jackson.annotation.JsonView;
import com.skillbox.socialnetwork.main.model.Person;
import com.skillbox.socialnetwork.main.model.requests.LoginRequest;
import com.skillbox.socialnetwork.main.model.requests.SignupRequest;
import com.skillbox.socialnetwork.main.model.responses.*;
import com.skillbox.socialnetwork.main.repo.PersonRepository;

import com.skillbox.socialnetwork.main.security.UserDetailsImpl;
import com.skillbox.socialnetwork.main.security.jwt.JwtUtils;
import com.skillbox.socialnetwork.main.security.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Calendar;
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
    UserService userService;

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
    public ResponseEntity<?> logoutUser(HttpServletRequest request, HttpServletResponse response){

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null) {
            new SecurityContextLogoutHandler().logout(request, response, auth);
        }
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
        person.setPassword(encoder.encode(signUpRequest.getPasswd1()));
        person.setBlocked(false);

        personRepository.save(person);

        return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
    }

    @JsonView(View.MyInfoResponse.class)
    @GetMapping("/users/me")
    public ResponseEntity<?> getMyInfo(@AuthenticationPrincipal UserDetailsImpl userDetails){

        Person person = personRepository.findByEmail(userDetails.getEmail()).orElseThrow(() -> new UsernameNotFoundException("User Not Found "));
        JwtResponse response = personToJwt(person);

        GeneralResponse model = new GeneralResponse(null, response);

        return ResponseEntity.ok(model);
    }

    private JwtResponse personToJwt(Person person){
        JwtResponse jwtResponse = new JwtResponse();
        jwtResponse.setId(person.getId());
        jwtResponse.setFirstName(person.getFirstName());
        jwtResponse.setLastName(person.getLastName());
        jwtResponse.setReg_date(person.getRegDate().getTimeInMillis());
        jwtResponse.setEmail(person.getEmail());
        jwtResponse.setPhone(person.getPhone());
        jwtResponse.setPhoto(person.getPhoto());
        jwtResponse.setAbout(person.getAbout());
        jwtResponse.setCity(person.getTown() != null ? person.getTown().getCity() : null);
        jwtResponse.setCountry(person.getTown() != null ? person.getTown().getCountry() : null);
        jwtResponse.setMessages_permission(person.getMessagesPermission());
        jwtResponse.setLast_online_time(person.getLastOnline() == null ? Calendar.getInstance().getTimeInMillis() : person.getLastOnline().getTimeInMillis());
        jwtResponse.setBlocked(person.getBlocked());
        return jwtResponse;
    }
}
