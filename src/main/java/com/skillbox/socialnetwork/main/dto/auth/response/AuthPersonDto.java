package com.skillbox.socialnetwork.main.dto.auth.response;

import com.skillbox.socialnetwork.main.dto.PersonDto;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class AuthPersonDto extends PersonDto {
    private String token;

    public AuthPersonDto(
            int id,
            String firstName,
            String lastName,
            Long regDate,
            Long birthDate,
            String email,
            String phone,
            String photo,
            String about,
            String city,
            String country,
            Boolean isBlocked,
            Long lastOnlineTime,
            String messagesPermission,
            String token
    ) {
        super(id, firstName, lastName, regDate, birthDate, email, phone, photo, about, city, country, isBlocked, lastOnlineTime, messagesPermission);
        this.token = token;
    }

}
