package com.skillbox.socialnetwork.main.controller;

import com.skillbox.socialnetwork.main.dto.post.request.UpdatePostRequestDto;
import com.skillbox.socialnetwork.main.dto.universal.ResponseFactory;
import com.skillbox.socialnetwork.main.service.AuthService;
import com.skillbox.socialnetwork.main.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class PostRestController {
    private final AuthService authService;
    private final PostService postService;

    @Autowired
    public PostRestController(AuthService authService, PostService postService) {
        this.authService = authService;
        this.postService = postService;
    }

    @GetMapping("/api/v1/post")
    public ResponseEntity<?> getPosts(
            @RequestHeader(name = "Authorization") String token,
//            @RequestParam(required = false) String text,
//            @RequestParam(name = "date_from", required = false) Long dateFrom,
//            @RequestParam(name = "date_to", required = false) Long dateTo,
            @RequestParam(required = false) Integer offset,
            @RequestParam(required = false, defaultValue = "20") Integer itemPerPage
    ) {

        //@TODO: Реализовать работу поиска, лимита по дате
        return authService.isAuthorized(token)
                ? ResponseEntity.ok(postService.feeds(offset, itemPerPage))
                : ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(ResponseFactory.getErrorResponse("invalid request", "unauthorized"));
    }

    @GetMapping("/api/v1/post/{id}")
    public ResponseEntity<?> getPost(
            @RequestHeader(name = "Authorization") String token,
            @PathVariable int id
    ) {
        if (authService.isAuthorized(token)) {
            return ResponseEntity.status(HttpStatus.OK).body(postService.getPost(id));
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(ResponseFactory.getErrorResponse("invalid request", "unauthorized"));
    }

    @PutMapping("api/v1/post/{id}")
    public ResponseEntity<?> updatePost(
            @RequestHeader(name = "Authorization") String token,
            @PathVariable int id,
            @RequestParam(name = "publish_date", required = false) Long publishDate,
            @RequestBody UpdatePostRequestDto request
    ) {
        if (authService.isAuthorized(token)) {
            return ResponseEntity.status(HttpStatus.OK).body(postService.editPost(id, publishDate, request));
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(ResponseFactory.getErrorResponse("invalid request", "unauthorized"));
    }

    @DeleteMapping("api/v1/post/{id}")
    public ResponseEntity<?> deletePost(
            @RequestHeader(name = "Authorization") String token,
            @PathVariable int id
    ) {
        return authService.isAuthorized(token)
                ? ResponseEntity.ok(postService.deletePost(id))
                : ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(ResponseFactory.getErrorResponse("invalid request", "unauthorized"));
    }
}

