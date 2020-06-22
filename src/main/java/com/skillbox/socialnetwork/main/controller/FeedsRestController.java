package com.skillbox.socialnetwork.main.controller;

import com.skillbox.socialnetwork.main.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FeedsRestController {

    private final PostService postService;

    @Autowired
    public FeedsRestController(PostService postService) {
        this.postService = postService;
    }

    @GetMapping("/api/v1/feeds")
    public ResponseEntity<?> getFeeds(
            @RequestParam(name = "query", required = false, defaultValue = "") String query,
            @RequestParam(name = "offset", required = false, defaultValue = "0") Integer offset,
            @RequestParam(name = "itemPerPage", required = false, defaultValue = "20") Integer limit

    ) {
        return ResponseEntity.ok(postService.feeds(offset, limit));
    }

}
