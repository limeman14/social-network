package com.skillbox.socialnetwork.main.dto.post;


import com.skillbox.socialnetwork.main.dto.person.response.PersonResponseFactory;
import com.skillbox.socialnetwork.main.dto.universal.BaseResponseListDto;

import com.skillbox.socialnetwork.main.dto.universal.ResponseDto;
import com.skillbox.socialnetwork.main.model.Post;

import java.util.ArrayList;
import java.util.List;

public class PostsListResponseFactory {
    public static BaseResponseListDto getPosts(List<Post> posts) {
        return new BaseResponseListDto(getAbstractResponseList(posts));
    }

    private static List<ResponseDto> getAbstractResponseList(List<Post> postList) {
        List<ResponseDto> postDtoList = new ArrayList<>();
        postList.forEach(p ->
                postDtoList.add(
                        new PostDto(
                                p.getId(),
                                p.getTime().getTime(),
                                PersonResponseFactory.getPerson(p.getAuthor()),
                                p.getTitle(),
                                p.getPostText(),
                                p.getIsBlocked(),
                                p.getLikes().size(),
                                //@TODO: Возвращать тут комментарии
                                new ArrayList<>()
                        )
                )
        );
        return postDtoList;
    }
}
