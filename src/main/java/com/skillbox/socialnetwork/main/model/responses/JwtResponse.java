package com.skillbox.socialnetwork.main.model.responses;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonView;
import com.skillbox.socialnetwork.main.model.City;
import com.skillbox.socialnetwork.main.model.Country;
import com.skillbox.socialnetwork.main.model.enumerated.Permission;
import lombok.Data;

@Data
public class JwtResponse implements Response{

    @JsonView(View.MyInfoResponse.class)
    private int id;

    @JsonView(View.MyInfoResponse.class)
    @JsonProperty("first_name")

    private String firstName;

    @JsonView(View.MyInfoResponse.class)
    @JsonProperty("last_name")
    private String lastName;

    @JsonView(View.MyInfoResponse.class)
    @JsonProperty("reg_date")
    private long regDate;

    @JsonView(View.MyInfoResponse.class)
    @JsonProperty("birth_date")
    private long birthDate;
    @JsonView(View.MyInfoResponse.class)
    private String email;
    @JsonView(View.MyInfoResponse.class)
    private String phone;
    @JsonView(View.MyInfoResponse.class)
    private String photo;
    @JsonView(View.MyInfoResponse.class)
    private String about;
    @JsonView(View.MyInfoResponse.class)
    private City city;
    @JsonView(View.MyInfoResponse.class)
    private Country country;

    @JsonView(View.MyInfoResponse.class)
    @JsonProperty("messages_permission")
    private Permission messagesPermission;

    @JsonView(View.MyInfoResponse.class)
    @JsonProperty("last_online_time")
    private long lastOnlineTime;

    @JsonView(View.MyInfoResponse.class)
    @JsonProperty("is_blocked")
    private boolean isBlocked;


    @JsonView(View.LoginResponse.class)
    private String token;
}
