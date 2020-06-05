package com.skillbox.socialnetwork.main.dto.universal;

import com.skillbox.socialnetwork.main.dto.ResponseDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class DataDto implements ResponseDto {
    private String message;
}
