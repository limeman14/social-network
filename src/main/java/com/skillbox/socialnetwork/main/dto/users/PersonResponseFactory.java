package com.skillbox.socialnetwork.main.dto.users;

import com.skillbox.socialnetwork.main.dto.AbstractResponse;
import com.skillbox.socialnetwork.main.dto.PersonDto;
import com.skillbox.socialnetwork.main.dto.auth.response.CityDto;
import com.skillbox.socialnetwork.main.dto.auth.response.CountryDto;
import com.skillbox.socialnetwork.main.model.Person;

import java.time.ZoneId;

public class PersonResponseFactory {
    public static AbstractResponse getPerson(Person person){
        return new AbstractResponse(
                new PersonDto(
                    person.getId(),
                    person.getFirstName(),
                    person.getLastName(),
                    person.getRegDate().getTime(),
                    person.getBirthDate().atStartOfDay(ZoneId.systemDefault()).toEpochSecond(),
                    person.getEmail(),
                    person.getPhone(),
                    person.getPhoto(),
                    person.getAbout(),
                    new CityDto(1, "Москва"),
                    new CountryDto(1, "Россия"),
                    person.getBlocked(),
                    person.getLastOnline().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli(),
                    person.getMessagesPermission().toString()
            )
        );
    }
}
