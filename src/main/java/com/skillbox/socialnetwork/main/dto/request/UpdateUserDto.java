package com.skillbox.socialnetwork.main.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.skillbox.socialnetwork.main.model.Permission;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateUserDto {
    @JsonProperty("last_name")
    private String firstName;
    @JsonProperty("birth_date")
    private String lastName;
    @JsonProperty("birth_date")
    private Date birthDate;
    private String phone;
    @JsonProperty("photo_id")
    private String photoId;
    private String about;
    @JsonProperty("town_id")
    private Integer townId;
    @JsonProperty("country_id")
    private Integer countryId;
    @JsonProperty("messages_permission")
    private Permission messagePermission;

}
