package com.skillbox.socialnetwork.main.dto.comment.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.skillbox.socialnetwork.main.dto.universal.Dto;
import lombok.Data;

@Data
public class CommentDto implements Dto {
    @JsonProperty("parent_id")
    private int parentId;
    @JsonProperty("comment_text")
    private String text;
    private int id;
    @JsonProperty("post_id")
    private String postId;
    private long time;
    @JsonProperty("author_id")
    private int authorId;
    @JsonProperty("is_blocked")
    private boolean blocked;
}
