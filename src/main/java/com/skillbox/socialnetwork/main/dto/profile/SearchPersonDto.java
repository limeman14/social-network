package com.skillbox.socialnetwork.main.dto.profile;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.skillbox.socialnetwork.main.dto.person.response.PersonDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SearchPersonDto {
    private Integer total;
    private Integer offset;
    @JsonProperty("perPage")
    private Integer limit;
    private List<PersonDto> data;
}
