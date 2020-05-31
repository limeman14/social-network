package com.skillbox.socialnetwork.main.model.responses;

import com.skillbox.socialnetwork.main.model.City;
import com.skillbox.socialnetwork.main.model.Country;
import com.skillbox.socialnetwork.main.model.enumerated.Permission;
import lombok.Data;

@Data
public class MyInfoResponse implements Response{
    private int id;
    private String firstName;
    private String lastName;

    private long reg_date;

    private long birth_date;

    private String email;
    private String phone;
    private String photo;
    private String about;
    private City city;
    private Country country;
    private Permission messages_permission;


    private long last_online_time;

    private boolean isBlocked;
}
