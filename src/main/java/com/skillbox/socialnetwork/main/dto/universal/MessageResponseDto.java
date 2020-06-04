package com.skillbox.socialnetwork.main.dto.universal;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class MessageResponseDto implements ResponseDto {

    private String message;

}
