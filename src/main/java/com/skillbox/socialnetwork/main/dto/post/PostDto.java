package com.skillbox.socialnetwork.main.dto.post;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.skillbox.socialnetwork.main.dto.comment.CommentDto;
import com.skillbox.socialnetwork.main.dto.universal.ResponseDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PostDto implements ResponseDto {
    private int id;
    private long time;
    private ResponseDto author;
    private String title;
    @JsonProperty("post_text")
    private String text;
    @JsonProperty("is_blocked")
    private boolean blocked;
    private int likes;
    private List<CommentDto> comments;
}
