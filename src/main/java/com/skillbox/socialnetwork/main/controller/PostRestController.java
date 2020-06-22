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
    private final PostService postService;

    @Autowired
    public PostRestController(PostService postService) {
        this.postService = postService;
    }

    @GetMapping("/api/v1/post")
    public ResponseEntity<?> getPosts(
            @RequestParam(defaultValue = "") String text,
            @RequestParam(name = "date_from", required = false, defaultValue = "946684800000") Long dateFrom,       //1 января 2000 года, если не указано иное
            @RequestParam(name = "date_to", required = false, defaultValue = "4102444800000") Long dateTo,          //1 января 2100 года, если не указано иное
            @RequestParam(required = false, defaultValue = "") String author,
            @RequestParam(required = false, defaultValue = "0") Integer offset,
            @RequestParam(required = false, defaultValue = "20") Integer itemPerPage
    ) {
        return ResponseEntity.ok(postService.searchPosts(text, dateFrom, dateTo, author, offset, itemPerPage));
    }

    @GetMapping("/api/v1/post/{id}")
    public ResponseEntity<?> getPost(@PathVariable int id) {
        return ResponseEntity.ok(postService.getPost(id));
    }

    @PutMapping("api/v1/post/{id}")
    public ResponseEntity<?> updatePost(
            @PathVariable int id,
            @RequestParam(name = "publish_date", required = false) Long publishDate,
            @RequestBody UpdatePostRequestDto request
    ) {
        return ResponseEntity.ok(postService.editPost(id, publishDate, request));
    }

    @DeleteMapping("api/v1/post/{id}")
    public ResponseEntity<?> deletePost(@PathVariable int id) {
        return ResponseEntity.ok(postService.deletePost(id));
    }
}

