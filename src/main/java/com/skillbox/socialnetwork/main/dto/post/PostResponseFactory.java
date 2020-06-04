package com.skillbox.socialnetwork.main.dto.post;

import com.skillbox.socialnetwork.main.dto.AbstractResponse;
import com.skillbox.socialnetwork.main.dto.users.PersonResponseFactory;
import com.skillbox.socialnetwork.main.model.Post;

import java.util.ArrayList;

public class PostResponseFactory {
    public static AbstractResponse getPost(Post post) {
        return new AbstractResponse(
                new PostDto(
                        post.getId(),
                        post.getTime().getTime(),
                        PersonResponseFactory.getPerson(post.getAuthor()).getData(),
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
