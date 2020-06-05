package com.skillbox.socialnetwork.main.dto.profile;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.skillbox.socialnetwork.main.dto.AbstractResponse;
import com.skillbox.socialnetwork.main.dto.PersonDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WallData {
    private PersonDto author;
    private String title;
    @JsonProperty("post_text")
    private String text;
    @JsonProperty("is_blocked")
    private Boolean blocked;
    private Integer likes;
    private List comments;
    private String type;
}
