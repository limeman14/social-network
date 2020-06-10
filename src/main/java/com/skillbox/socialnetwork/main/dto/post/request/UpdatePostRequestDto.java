package com.skillbox.socialnetwork.main.dto.post.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdatePostRequestDto {
    private String title;

    @JsonProperty("post_text")
    private String postText;
}
