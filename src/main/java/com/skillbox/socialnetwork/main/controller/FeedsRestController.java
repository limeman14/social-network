package com.skillbox.socialnetwork.main.controller;

import com.skillbox.socialnetwork.main.dto.post.response.PostResponseFactory;
import com.skillbox.socialnetwork.main.model.Post;
import com.skillbox.socialnetwork.main.security.jwt.JwtTokenProvider;
import com.skillbox.socialnetwork.main.service.PersonService;
import com.skillbox.socialnetwork.main.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
public class FeedsRestController {

    private final JwtTokenProvider jwtTokenProvider;

    private final PersonService personService;

    private final PostService postService;

    @Autowired
    public FeedsRestController(JwtTokenProvider jwtTokenProvider, PersonService personService, PostService postService) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.personService = personService;
        this.postService = postService;
    }

    @GetMapping("/api/v1/feeds")
    public ResponseEntity getFeeds(
            HttpServletRequest request
//            @PathVariable String query,
//            @PathVariable Integer offset,
//            @PathVariable Integer itemPerPage
    ) {

        //@TODO: Реализовать работу поиска, отступов и лимита на страницу
        List<Post> posts = postService.getAll();
        return ResponseEntity.status(HttpStatus.OK).body(PostResponseFactory.getPosts(posts));
    }

}
