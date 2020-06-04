package com.skillbox.socialnetwork.main.dto.post.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.skillbox.socialnetwork.main.dto.universal.ResponseDto;
import com.skillbox.socialnetwork.main.dto.comment.response.CommentDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PostResponseDto implements ResponseDto {
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
