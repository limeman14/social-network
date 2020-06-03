package com.skillbox.socialnetwork.main.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.skillbox.socialnetwork.main.dto.auth.response.CityDto;
import com.skillbox.socialnetwork.main.dto.auth.response.CountryDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PersonDto implements ResponseDto {
    protected int id;
    @JsonProperty("first_name")
    protected String firstName;
    @JsonProperty("last_name")
    protected String lastName;
    @JsonProperty("reg_date")
    protected Long regDate;
    @JsonProperty("birth_date")
    protected Long birthDate;
    protected String email;
    protected String phone;
    protected String photo;
    protected String about;
    protected String city;
    protected String country;
    @JsonProperty("is_blocked")
    protected Boolean isBlocked;
    @JsonProperty("last_online_time")
    protected Long lastOnlineTime;
    @JsonProperty("messages_permission")
    protected String messagesPermission;
}
