package com.skillbox.socialnetwork.main.service;

import com.skillbox.socialnetwork.main.dto.universal.ResponseDto;
import com.skillbox.socialnetwork.main.dto.auth.request.RegisterRequestDto;
import com.skillbox.socialnetwork.main.model.Person;

import java.util.List;

public interface PersonService {

    List<Person> getAll();

    Person findByUsername(String username);

    Person findByEmail(String email);

    Person findById(Integer id);

    void delete(Integer id);

    ResponseDto registration(RegisterRequestDto dto);

    void logout(Person person);
}