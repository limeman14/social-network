package com.skillbox.socialnetwork.main.dto.profile.response;

import com.skillbox.socialnetwork.main.dto.person.response.PersonResponseFactory;
import com.skillbox.socialnetwork.main.dto.universal.BaseResponseList;
import com.skillbox.socialnetwork.main.dto.universal.Dto;
import com.skillbox.socialnetwork.main.dto.universal.ResponseFactory;
import com.skillbox.socialnetwork.main.model.Post;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class WallResponseFactory {

    public static BaseResponseList getWall(List<Post> posts, int offset, int limit) {
        return ResponseFactory.getBaseResponseList(getListDto(posts), offset, limit);
    }

    private static List<Dto> getListDto(List<Post> posts) {
        List<Dto> data = new ArrayList<>();

        posts.stream()
                .filter(post -> post.getTime().before(new Date()))//только текущие посты
                .forEach(post -> data.add(
                        new WallResponseDto(
                                post.getId(),
                                post.getTime().getTime(),
                                PersonResponseFactory.getPersonDto(post.getAuthor()),
                                post.getTitle(),
                                post.getPostText(),
                                post.getIsBlocked(),
                                post.getLikes().size(),
                                new ArrayList<>(), //@TODO Comments
                                "POSTED"//@TODO ENUM postType
                        )
                ));
        return data;
    }
}
