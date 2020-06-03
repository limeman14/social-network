package com.skillbox.socialnetwork.main.controller;

import com.skillbox.socialnetwork.main.dto.AbstractResponseList;
import com.skillbox.socialnetwork.main.dto.post.PostResponseFactory;
import com.skillbox.socialnetwork.main.dto.post.PostsListResponseFactory;
import com.skillbox.socialnetwork.main.model.Post;
import com.skillbox.socialnetwork.main.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class PostRestController {
    private final PostService postService;

    @Autowired
    public PostRestController(PostService postService) {
        this.postService = postService;
    }

    @GetMapping("/api/v1/post")
    public ResponseEntity<AbstractResponseList> getPosts(
//            @PathVariable String text,
//            @PathVariable Long date_from,
//            @PathVariable Long date_to,
//            @PathVariable Integer offset,
//            @PathVariable Integer itemPerPage
    ) {

        //@TODO: Реализовать работу поиска, отступов и лимита на страницу и лимита по дате

        List<Post> posts = postService.getAll();
        return ResponseEntity.status(HttpStatus.OK).body(PostsListResponseFactory.getPosts(posts));
    }

    @GetMapping("/api/v1/post/{id}")
    public ResponseEntity getPost(@PathVariable int id) {
        Post post = postService.findById(id);
        return ResponseEntity.status(HttpStatus.OK).body(PostResponseFactory.getPost(post));
    }


}

