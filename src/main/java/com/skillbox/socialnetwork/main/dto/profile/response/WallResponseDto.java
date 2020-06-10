package com.skillbox.socialnetwork.main.dto.profile.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.skillbox.socialnetwork.main.dto.person.response.PersonResponseDto;
import com.skillbox.socialnetwork.main.dto.universal.Dto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WallResponseDto implements Dto {
    private int id;
    private long time;
    private PersonResponseDto author;
    private String title;
    @JsonProperty("post_text")
    private String text;
    @JsonProperty("is_blocked")
    private Boolean blocked;
    private Integer likes;
    private List comments;
    private String type;
}
