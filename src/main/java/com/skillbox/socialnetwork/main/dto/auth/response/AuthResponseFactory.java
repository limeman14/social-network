package com.skillbox.socialnetwork.main.dto.auth.response;

import com.skillbox.socialnetwork.main.dto.AbstractResponse;
import com.skillbox.socialnetwork.main.model.Person;

import java.time.ZoneId;
import java.util.Date;

public class AuthResponseFactory {

    public static AbstractResponse getAuthResponse(Person person, String token){
        return new AbstractResponse(
                new AuthPersonDto(
                    person.getId(),
                    person.getFirstName(),
                    person.getLastName(),

                    person.getRegDate().getTime(),
                    person.getBirthDate() != null ?
                                person.getBirthDate().atStartOfDay(ZoneId.systemDefault()).toEpochSecond():null,
                    person.getEmail(),
                    person.getPhone(),
                    person.getPhoto(),
                    person.getAbout(),
                    new CityDto(1, "Москва"),
                    new CountryDto(1, "Россия"),
                    person.getBlocked(),
                    person.getLastOnline().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli(),
                    person.getMessagesPermission().toString(),
                    token
                )
        );
    }


}
