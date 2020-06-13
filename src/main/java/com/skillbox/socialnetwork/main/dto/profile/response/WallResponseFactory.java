package com.skillbox.socialnetwork.main.dto.profile.response;

import com.skillbox.socialnetwork.main.dto.person.response.PersonResponseFactory;
import com.skillbox.socialnetwork.main.dto.universal.BaseResponseList;
import com.skillbox.socialnetwork.main.dto.universal.Dto;
import com.skillbox.socialnetwork.main.dto.universal.ResponseFactory;
import com.skillbox.socialnetwork.main.model.Post;
import com.skillbox.socialnetwork.main.model.Tag;
import com.skillbox.socialnetwork.main.model.enumerated.PostType;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class WallResponseFactory {

    public static BaseResponseList getWall(List<Post> posts, int offset, int limit) {
        return ResponseFactory.getBaseResponseListWithLimit(getListDto(posts), offset, limit);
    }

    private static List<Dto> getListDto(List<Post> posts) {
        List<Dto> data = new ArrayList<>();

        posts.forEach(post -> data.add(
                        new WallResponseDto(
                                post.getId(),
                                post.getTime().getTime(),
                                PersonResponseFactory.getPersonDto(post.getAuthor()),
                                post.getTitle(),
                                post.getPostText(),
                                post.getIsBlocked(),
                                post.getLikes().size(),
                                new ArrayList<>(), //@TODO Comments
                                post.getTime().before(new Date()) ? PostType.POSTED : PostType.QUEUED,           //фильтрация между опубликованными и отложенными постами
                                post.getTags() != null
                                        ? post.getTags()
                                        .stream()
                                        .map(Tag::getTag)
                                        .collect(Collectors.toList())
                                        : new ArrayList<>()
                        )
                ));
        return data;
    }
}
