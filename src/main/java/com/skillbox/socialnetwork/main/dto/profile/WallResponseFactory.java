package com.skillbox.socialnetwork.main.dto.profile;

import com.skillbox.socialnetwork.main.dto.person.response.PersonResponseFactory;
import com.skillbox.socialnetwork.main.model.Post;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class WallResponseFactory {

    public static WallDto getWall(List<Post> posts, int offset, int limit){
        List<Post> limitedPostList = getElementsInRange(posts, offset, limit);
        List<WallData> data = getData(limitedPostList);
        return new WallDto(
                data.size(),
                offset,
                limit,
                data
        );
    }


    private static List<WallData> getData(List<Post> posts){
        List<WallData> data = new ArrayList<>();

        posts.stream()
                .filter(post -> post.getTime().before(new Date()))//только текущие посты
                .forEach(post -> data.add(
                new WallData(
                        PersonResponseFactory.getPersonDto(post.getAuthor()),
                        post.getTitle(),
                        post.getPostText(),
                        post.getIsBlocked(),
                        post.getLikes().size(),
                        new ArrayList(), //@TODO Comments
                        "POSTED"//@TODO ENUM postType
                )
        ));
        return data;
    }

    //Режет список по входящим параметрам. Выдает limit количество постов, начиная с offset индекса
    private static List<Post> getElementsInRange(List<Post> list, int offset, int limit) {
        int lastElementIndex = offset + limit;
        int lastPostIndex = list.size();
        if (lastPostIndex >= offset) {//если есть элементы входящие в нужный диапазон
            if (lastElementIndex <= lastPostIndex) {//если все элементы с нужными индексами есть в листе
                return list.subList(offset, lastElementIndex);
            } else {//если не хватает элементов, то в посты записываем остаток, считая от offset
                return list.subList(offset, lastPostIndex);
            }
        } else {
            return new ArrayList<>();
        }
    }

}
