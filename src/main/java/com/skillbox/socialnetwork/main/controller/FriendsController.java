package com.skillbox.socialnetwork.main.controller;

import com.skillbox.socialnetwork.main.dto.person.response.PersonResponseFactory;
import com.skillbox.socialnetwork.main.model.Person;
import com.skillbox.socialnetwork.main.security.jwt.JwtUser;
import com.skillbox.socialnetwork.main.service.FriendsService;
import com.skillbox.socialnetwork.main.service.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/friends")
public class FriendsController {
    private final FriendsService friendsService;
    private final PersonService personService;

    @Autowired
    public FriendsController(FriendsService friendsService, PersonService personService) {
        this.friendsService = friendsService;
        this.personService = personService;
    }

    @GetMapping
    public ResponseEntity<?> getFriends(
            @AuthenticationPrincipal JwtUser user,
            @RequestParam(required = false) String name,
            @RequestParam(required = false, defaultValue = "0") Integer offset,
            @RequestParam(required = false, defaultValue = "20") Integer itemPerPage
    ) {
        List<Person> friends = friendsService
                .getFriends(personService.findById(user.getId()), name);
        return ResponseEntity.ok().body(PersonResponseFactory.getPersons(friends, offset, itemPerPage));
    }
}
