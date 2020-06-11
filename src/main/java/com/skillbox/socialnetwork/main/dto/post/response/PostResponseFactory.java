package com.skillbox.socialnetwork.main.dto.post.response;

import com.skillbox.socialnetwork.main.dto.person.response.PersonResponseFactory;
import com.skillbox.socialnetwork.main.dto.universal.BaseResponse;
import com.skillbox.socialnetwork.main.dto.universal.BaseResponseList;
import com.skillbox.socialnetwork.main.dto.universal.ResponseFactory;
import com.skillbox.socialnetwork.main.model.Post;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class PostResponseFactory {
    public static BaseResponse getSinglePost(Post post) {
        return ResponseFactory.getBaseResponse(postToDto(post));
    }

    public static BaseResponseList getPostsList(List<Post> posts, int offset, int limit) {
        return ResponseFactory.getBaseResponseList(
                posts.stream()
                        .filter(post -> post.getTime().before(new Date()))
                        .map(PostResponseFactory::postToDto)
                        .collect(Collectors.toList()),
                offset, limit);
    }

    public static BaseResponseList getPostsList(List<Post> posts) {
        return new BaseResponseList(
                posts.stream()
                        .filter(post -> post.getTime().before(new Date()))
                        .map(PostResponseFactory::postToDto)
                        .collect(Collectors.toList()));
    }

    private static PostResponseDto postToDto(Post post) {
        return new PostResponseDto(
                post.getId(),
                post.getTime().getTime(),
                PersonResponseFactory.getPersonDto(post.getAuthor()),
                post.getTitle(),
                post.getPostText(),
                post.getIsBlocked(),
                post.getLikes().size(),
                //@TODO: Возвращать тут комментарии
                new ArrayList<>(),
                "POSTED", //@TODO ENUM postType
                post.getTags() != null
                        ? post.getTags()
                            .stream()
                            .map(p -> p.getTag().getTag())
                            .collect(Collectors.toList())
                        : new ArrayList<>()
        );
    }
}
