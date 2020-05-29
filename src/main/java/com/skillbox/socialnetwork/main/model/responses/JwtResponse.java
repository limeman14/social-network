package com.skillbox.socialnetwork.main.model.responses;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonView;
import com.skillbox.socialnetwork.main.model.City;
import com.skillbox.socialnetwork.main.model.Country;
import com.skillbox.socialnetwork.main.model.Permission;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.List;

@Data
public class JwtResponse implements Response{
    //private String type = "Bearer";
    @JsonView(View.MyInfoResponse.class)
    private int id;
    @JsonView(View.MyInfoResponse.class)
    private String firstName;
    @JsonView(View.MyInfoResponse.class)
    private String lastName;
    @JsonView(View.MyInfoResponse.class)
    private long reg_date;
    @JsonView(View.MyInfoResponse.class)
    private long birth_date;
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
    private Permission messages_permission;
    @JsonView(View.MyInfoResponse.class)
    private long last_online_time;
    @JsonView(View.MyInfoResponse.class)
    private boolean isBlocked;

    @JsonView(View.LoginResponse.class)
    private String token;
}
