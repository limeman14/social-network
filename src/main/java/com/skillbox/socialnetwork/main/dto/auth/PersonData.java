package com.skillbox.socialnetwork.main.dto.auth;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.skillbox.socialnetwork.main.model.Person;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class PersonData {

    private Integer id;

    @JsonProperty("first_name")
    private String firstName;

    @JsonProperty("last_name")
    private String lastName;

    private String email;

    private String phone;

    private String photo;

    private String about;

    private String city;

    private String country;

    public PersonData(Person person){
        id = person.getId();
        firstName = person.getFirstName();
        email = person.getEmail();
        phone = person.getPhone();
        photo = person.getPhoto();
        about = person.getAbout();
        city = "{\"id\" : \"1\", \"title\" : \"Москва\"}";
        country = "{\"id\" : \"1\", \"title\" : \"Россия\"}";
    }

}
