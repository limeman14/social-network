package com.skillbox.socialnetwork.main.dto.post.response;

import com.skillbox.socialnetwork.main.dto.person.response.PersonResponseFactory;
import com.skillbox.socialnetwork.main.dto.universal.BaseResponse;
import com.skillbox.socialnetwork.main.dto.universal.BaseResponseList;
import com.skillbox.socialnetwork.main.dto.universal.ResponseFactory;
import com.skillbox.socialnetwork.main.model.Post;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class PostResponseFactory {
    public static BaseResponse getPost(Post post) {
        return ResponseFactory.getBaseResponse(getPostDto(post));
    }

    public static BaseResponseList getPosts(List<Post> posts, int offset, int limit) {
        return ResponseFactory.getBaseResponseList(posts.stream()
                        .map(PostResponseFactory::getPostDto)
                        .collect(Collectors.toList()),
                offset, limit);
    }

    private static PostResponseDto getPostDto(Post post) {
        return new PostResponseDto(
                post.getId(),
                post.getTime().getTime(),
                PersonResponseFactory.getPerson(post.getAuthor()).getData(),
                post.getTitle(),
                post.getPostText(),
                post.getIsBlocked(),
                post.getLikes().size(),
                //@TODO: Возвращать тут комментарии
                new ArrayList<>()
        );
    }
}
