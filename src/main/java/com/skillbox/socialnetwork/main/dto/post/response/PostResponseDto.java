package com.skillbox.socialnetwork.main.dto.post.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.skillbox.socialnetwork.main.dto.comment.response.CommentDto;
import com.skillbox.socialnetwork.main.dto.person.response.PersonResponseDto;
import com.skillbox.socialnetwork.main.dto.universal.Dto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor

//ToDo: дублирует содержание WallResponseDto

public class PostResponseDto implements Dto {
    private int id;
    private long time;
    private Dto author;
    private String title;
    @JsonProperty("post_text")
    private String text;
    @JsonProperty("is_blocked")
    private boolean blocked;
    private int likes;
    private List comments;
    private String type;
    private List tags;
}
