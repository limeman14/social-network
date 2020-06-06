package com.skillbox.socialnetwork.main.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.skillbox.socialnetwork.main.model.enumerated.Permission;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class UpdateUserDto {
    @JsonProperty("first_name")
    private String firstName;
    @JsonProperty("last_name")
    private String lastName;
    @JsonProperty("birth_date")
    private Date birthDate;
    private String phone;
    @JsonProperty("photo_id")
    private String photoId;
    private String about;
    private String city;
    private String country;
}
