package com.skillbox.socialnetwork.main.service.impl;

import com.skillbox.socialnetwork.main.dto.like.request.LikeRequest;
import com.skillbox.socialnetwork.main.dto.like.response.LikeResponseDto;
import com.skillbox.socialnetwork.main.dto.universal.BaseResponse;
import com.skillbox.socialnetwork.main.dto.universal.Dto;
import com.skillbox.socialnetwork.main.dto.universal.ResponseFactory;
import com.skillbox.socialnetwork.main.model.CommentLike;
import com.skillbox.socialnetwork.main.model.Person;
import com.skillbox.socialnetwork.main.model.Post;
import com.skillbox.socialnetwork.main.model.PostLike;
import com.skillbox.socialnetwork.main.model.enumerated.NotificationCode;
import com.skillbox.socialnetwork.main.repository.CommentLikeRepository;
import com.skillbox.socialnetwork.main.repository.CommentRepository;
import com.skillbox.socialnetwork.main.repository.LikeRepository;
import com.skillbox.socialnetwork.main.repository.PostRepository;
import com.skillbox.socialnetwork.main.service.LikeService;
import com.skillbox.socialnetwork.main.service.NotificationService;
import com.skillbox.socialnetwork.main.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.stream.Collectors;

@Service
public class LikeServiceImpl implements LikeService {
    private final LikeRepository postLikeRepository;
    private final CommentLikeRepository commentLikeRepository;
    private final PostRepository postRepository;
    private final NotificationService notificationService;
    private final PostService postService;
    private final CommentRepository commentRepository;

    @Autowired
    public LikeServiceImpl(LikeRepository likeRepository, CommentLikeRepository commentLikeRepository, PostRepository postRepository, NotificationService notificationService, PostService postService, CommentRepository commentRepository)
    {
        this.postLikeRepository = likeRepository;
        this.commentLikeRepository = commentLikeRepository;
        this.postRepository = postRepository;
        this.notificationService = notificationService;
        this.postService = postService;
        this.commentRepository = commentRepository;
    }

    @Override
    public Dto save(PostLike like)
    {
        if (!isLiked(like.getPost(), like.getPerson()))
        {
            postLikeRepository.save(like);
        }
        notificationService.addNotification(like.getPerson(), like.getPost()
                .getAuthor(), NotificationCode.LIKE, "Пользователь " + like.getPerson() + " оценил вашу запись.");

        return new LikeResponseDto(like.getPost().getLikes().size(), like.getPost().getLikes().stream()
                .map(PostLike::getPerson).map(Person::getId).collect(Collectors.toList()));
    }

    @Override
    public Boolean isLiked(Post post, Person person)
    {
        return post.getLikes().stream().map(PostLike::getPerson).collect(Collectors.toList()).contains(person);
    }

    @Override
    public PostLike getLike(Integer id)
    {
        return postLikeRepository.findPostLikeById(id);
    }

    @Override
    public Dto delete(Integer id, Person person)
    {
        Post post = postRepository.findPostById(id);
        PostLike like = postLikeRepository.findPostLikeByPostAndPerson(post, person);
        postLikeRepository.delete(like);
        return new LikeResponseDto(post.getLikes().size(), new ArrayList<>());
    }

    @Override
    public BaseResponse putLike(LikeRequest request, Person authorizedUser)
    {
        switch (request.getType())
        {
            case "Post":
                postLikeRepository.save(
                        PostLike.builder()
                                .person(authorizedUser)
                                .post(postService.findById(request.getId()))
                                .time(new Date())
                                .build());
                return ResponseFactory.responseOk();
            case "Comment":
                commentLikeRepository.save(
                        CommentLike.builder()
                                .person(authorizedUser)
                                .comment(commentRepository.findById(request.getId()).get())
                                .time(new Date())
                                .build());
                return ResponseFactory.responseOk();
        }
        return null;
    }
}
