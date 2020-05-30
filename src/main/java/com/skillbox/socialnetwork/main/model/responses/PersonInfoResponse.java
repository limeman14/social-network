package com.skillbox.socialnetwork.main.model.responses;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.skillbox.socialnetwork.main.model.City;
import com.skillbox.socialnetwork.main.model.Country;
import lombok.Data;

@Data
public class PersonInfoResponse implements ResponseData{
    private Integer id;

    @JsonProperty("first_name")
    private String firstName;

    @JsonProperty("last_name")
    private String lastName;

    @JsonProperty("reg_date")
    private Long regDate;

    @JsonProperty("birth_date")
    private Long birthDate;

    private String email;

    private String phone;

    private String photo;

    private String about;

    private City city;

    private Country country;

    @JsonProperty("is_blocked")
    private Boolean isBlocked;

    @JsonProperty("last_online_time")
    private Long lastOnlineTime;

    @JsonProperty("messages_permission")
    private String messagesPermission;

    private String token;

}
