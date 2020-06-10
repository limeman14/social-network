package com.skillbox.socialnetwork.main.controller;

import com.skillbox.socialnetwork.main.dto.post.request.UpdatePostRequestDto;
import com.skillbox.socialnetwork.main.dto.post.response.PostResponseFactory;
import com.skillbox.socialnetwork.main.dto.universal.BaseResponseList;
import com.skillbox.socialnetwork.main.model.Post;
import com.skillbox.socialnetwork.main.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
public class PostRestController {
    private final PostService postService;

    @Autowired
    public PostRestController(PostService postService) {
        this.postService = postService;
    }

    @GetMapping("/api/v1/post")
    public ResponseEntity<BaseResponseList> getPosts(
//            @RequestParam String text,
//            @RequestParam(name = "date_from", required = false) Long dateFrom,
//            @RequestParam(name = "date_to", required = false) Long dateTo,
            @RequestParam(required = false) Integer offset,
            @RequestParam(required = false, defaultValue = "20") Integer itemPerPage
    ) {

        //@TODO: Реализовать работу поиска, отступов и лимита на страницу и лимита по дате

        List<Post> posts = postService.getAll();
        return ResponseEntity.status(HttpStatus.OK).body(PostResponseFactory.getPosts(posts, offset, itemPerPage));
    }

    @GetMapping("/api/v1/post/{id}")
    public ResponseEntity<?> getPost(@PathVariable int id) {
        Post post = postService.findById(id);
        return ResponseEntity.status(HttpStatus.OK).body(PostResponseFactory.getPost(post));
    }

    @PutMapping("api/v1/post/{id}")
    public ResponseEntity<?> updatePost(@PathVariable int id, @RequestParam(name = "publish_date", required = false) Long publishDate, @RequestBody UpdatePostRequestDto postDto) {
        Post post = postService.findById(id);
        post.setPostText(postDto.getPostText());
        post.setTitle(postDto.getTitle());

        //получение текущей даты и проверка на то, указана ли дата отложенного поста в запросе
        Date publishDateNow = new Date();
        post.setTime(publishDate == null ? publishDateNow : new Date(publishDate));

        return ResponseEntity.status(HttpStatus.OK).body(PostResponseFactory.getPost(postService.save(post)));
    }
}

