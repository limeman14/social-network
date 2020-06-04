package com.skillbox.socialnetwork.main.dto.universal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ErrorResponseDto implements ResponseDto {
    private String error;
}
