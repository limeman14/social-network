package com.skillbox.socialnetwork.main.dto.person.response;

import com.skillbox.socialnetwork.main.dto.profile.SearchPersonDto;
import com.skillbox.socialnetwork.main.dto.universal.BaseResponseDto;
import com.skillbox.socialnetwork.main.model.Person;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class PersonResponseFactory {
    public static BaseResponseDto getPerson(Person person){
        return new BaseResponseDto(getPersonDto(person));
    }

    public static SearchPersonDto formatPeopleSearchResultSet(List<Person> people, int offset, int limit){
        return new SearchPersonDto(
                people.size(),
                offset,
                limit,
                getElementsInRange(people.stream().map(PersonResponseFactory::getPersonDto).collect(Collectors.toList()), offset, limit)
        );
    }

    //Режет список по входящим параметрам. Выдает limit количество постов, начиная с offset индекса
    private static List<PersonDto> getElementsInRange(List<PersonDto> list, int offset, int limit) {
        int lastElementIndex = offset + limit;
        int lastPostIndex = list.size();
        if (lastPostIndex >= offset) {//если есть элементы входящие в нужный диапазон
            if (lastElementIndex <= lastPostIndex) {//если все элементы с нужными индексами есть в листе
                return list.subList(offset, lastElementIndex);
            } else {//если не хватает элементов, то в посты записываем остаток, считая от offset
                return list.subList(offset, lastPostIndex);
            }
        } else {
            return new ArrayList<>();
        }
    }

    public static PersonDto getPersonDto(Person person){
        return new PersonDto(
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
        );
    }

}
