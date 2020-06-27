package com.skillbox.socialnetwork.main.service;

import com.skillbox.socialnetwork.main.dto.auth.request.RegisterRequestDto;
import com.skillbox.socialnetwork.main.dto.universal.Response;
import com.skillbox.socialnetwork.main.model.Person;

import java.util.Date;
import java.util.List;

public interface PersonService {

    List<Person> getAll();

    Person findByEmail(String email);

    Person findById(Integer id);

    List<Person> search(String name, String surname,
                        Date dateFrom, Date dateTo,
                        String cityName, String countryName);

    void delete(Integer id);

    Response registration(RegisterRequestDto dto);

    void logout(Person person);

    Person save(Person person);

    void delete(Person person);
}