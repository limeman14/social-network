package com.skillbox.socialnetwork.main.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AbstractResponseList {
    private List<ResponseDto> data;
}
