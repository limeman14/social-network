package com.skillbox.socialnetwork.main.dto.post;

import com.skillbox.socialnetwork.main.dto.person.response.PersonResponseFactory;
import com.skillbox.socialnetwork.main.dto.universal.BaseResponseDto;
import com.skillbox.socialnetwork.main.dto.universal.ResponseDto;
import com.skillbox.socialnetwork.main.model.Post;

import java.util.ArrayList;

public class PostResponseFactory {
    public static ResponseDto getPost(Post post) {
        return new BaseResponseDto(
                new PostDto(
                        post.getId(),
                        post.getTime().getTime(),
                        PersonResponseFactory.getPerson(post.getAuthor()),
                        post.getTitle(),
                        post.getPostText(),
                        post.getIsBlocked(),
                        post.getLikes().size(),
                        //@TODO: Возвращать тут комментарии
                        new ArrayList<>()
                )
        );
    }
}
