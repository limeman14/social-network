package com.skillbox.socialnetwork.main.dto.post.response;

import com.skillbox.socialnetwork.main.dto.universal.BaseResponseDto;
import com.skillbox.socialnetwork.main.dto.universal.BaseResponseListDto;
import com.skillbox.socialnetwork.main.dto.universal.ResponseDto;
import com.skillbox.socialnetwork.main.dto.person.response.PersonResponseFactory;
import com.skillbox.socialnetwork.main.model.Post;

import java.util.ArrayList;
import java.util.List;

public class PostResponseFactory {
    public static BaseResponseDto getPost(Post post) {
        return new BaseResponseDto(createPostResponse(post));
    }

    public static BaseResponseListDto getPosts(List<Post> posts) {
        return new BaseResponseListDto(createPostsListResponse(posts));
    }

    private static PostResponseDto createPostResponse(Post post) {
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

    private static List<ResponseDto> createPostsListResponse(List<Post> postList) {
        List<ResponseDto> postDtoList = new ArrayList<>();
        postList.forEach(p ->
                postDtoList.add(createPostResponse(p))
        );
        return postDtoList;
    }
}
