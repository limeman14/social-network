package com.skillbox.socialnetwork.main.service.impl;

import com.skillbox.socialnetwork.main.dto.like.response.LikeResponseDto;
import com.skillbox.socialnetwork.main.dto.universal.Dto;
import com.skillbox.socialnetwork.main.model.Person;
import com.skillbox.socialnetwork.main.model.Post;
import com.skillbox.socialnetwork.main.model.PostLike;
import com.skillbox.socialnetwork.main.model.enumerated.NotificationCode;
import com.skillbox.socialnetwork.main.repository.LikeRepository;
import com.skillbox.socialnetwork.main.repository.PostRepository;
import com.skillbox.socialnetwork.main.service.LikeService;
import com.skillbox.socialnetwork.main.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.stream.Collectors;

@Service
public class LikeServiceImpl implements LikeService {
    private final LikeRepository likeRepository;
    private final PostRepository postRepository;
    private final NotificationService notificationService;

    @Autowired
    public LikeServiceImpl(LikeRepository likeRepository, PostRepository postRepository, NotificationService notificationService) {
        this.likeRepository = likeRepository;
        this.postRepository = postRepository;
        this.notificationService = notificationService;
    }

    @Override
    public Dto save(PostLike like) {
        if (!isLiked(like.getPost(), like.getPerson())) {
            likeRepository.save(like);
        }
        notificationService.addNotification(like.getPerson(), like.getPost().getAuthor(), NotificationCode.LIKE, "Пользователь "+like.getPerson() + " оценил вашу запись.");

        return new LikeResponseDto(like.getPost().getLikes().size(), like.getPost().getLikes().stream()
                .map(PostLike::getPerson).map(Person::getId).collect(Collectors.toList()));
    }

    @Override
    public Boolean isLiked(Post post, Person person) {
        return post.getLikes().stream().map(PostLike::getPerson).collect(Collectors.toList()).contains(person);
    }

    @Override
    public PostLike getLike(Integer id) {
        return likeRepository.findPostLikeById(id);
    }

    @Override
    public Dto delete(Integer id, Person person) {
        Post post = postRepository.findPostById(id);
        PostLike like = likeRepository.findPostLikeByPostAndPerson(post, person);
        likeRepository.delete(like);
        return new LikeResponseDto(post.getLikes().size(), new ArrayList<>());
    }
}
