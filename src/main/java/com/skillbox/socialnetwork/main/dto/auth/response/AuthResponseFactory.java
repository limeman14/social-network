package com.skillbox.socialnetwork.main.dto.auth.response;

import com.skillbox.socialnetwork.main.dto.universal.BaseResponseDto;
import com.skillbox.socialnetwork.main.model.Person;

public class AuthResponseFactory {

    public static BaseResponseDto getAuthResponse(Person person, String token){
        return new BaseResponseDto(
                new AuthPersonResponseDto(
                    person.getId(),
                    person.getFirstName(),
                    person.getLastName(),

                    person.getRegDate().getTime(),
                    person.getBirthDate() != null ?
                                person.getBirthDate().getTime() : null,
                    person.getEmail(),
                    person.getPhone(),
                    person.getPhoto(),
                    person.getAbout(),
                    person.getTown() != null ?
                                person.getTown().getCity().getTitle() : null,
                    person.getTown() != null ?
                                person.getTown().getCountry().getTitle() : null,
                    person.getIsBlocked(),
                    person.getLastOnlineTime().getTime(),
                    person.getMessagesPermission().toString(),
                    token
                )
        );
    }


}
