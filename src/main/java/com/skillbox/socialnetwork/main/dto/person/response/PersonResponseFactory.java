package com.skillbox.socialnetwork.main.dto.person.response;

import com.skillbox.socialnetwork.main.dto.universal.BaseResponseDto;
import com.skillbox.socialnetwork.main.model.Person;

public class PersonResponseFactory {
    public static BaseResponseDto getPerson(Person person){
        return new BaseResponseDto(
                new PersonDto(
                    person.getId(),
                    person.getFirstName(),
                    person.getLastName(),
                    person.getRegDate().getTime(),
                    person.getBirthDate() != null ? person.getBirthDate().getTime() : null,
                    person.getEmail(),
                    person.getPhone(),
                    person.getPhoto(),
                    person.getAbout(),
                    person.getTown() != null ? person.getTown().getCity().getTitle() : null,
                    person.getTown() != null ? person.getTown().getCountry().getTitle() : null,
                    person.getIsBlocked(),
                    person.getLastOnlineTime().getTime(),
                    person.getMessagesPermission().toString()
            )
        );
    }
}
