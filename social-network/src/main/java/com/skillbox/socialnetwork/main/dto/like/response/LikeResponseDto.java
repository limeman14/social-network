package com.skillbox.socialnetwork.main.dto.like.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.skillbox.socialnetwork.main.dto.universal.Dto;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class LikeResponseDto implements Dto {
    int likes; //liki count
    List<Integer> users;
}
