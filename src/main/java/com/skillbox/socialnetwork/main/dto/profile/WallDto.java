package com.skillbox.socialnetwork.main.dto.profile;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WallDto {
    private Integer total;
    private Integer offset;
    private Integer perPage;
    private List<WallData> data;
}
