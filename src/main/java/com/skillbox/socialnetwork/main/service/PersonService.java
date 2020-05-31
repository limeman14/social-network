package com.skillbox.socialnetwork.main.service;

import com.skillbox.socialnetwork.main.dto.ResponseDto;
import com.skillbox.socialnetwork.main.dto.request.RegisterRequestDto;
import com.skillbox.socialnetwork.main.model.Person;

import java.util.List;

/**
 * Service interface for class {@link Person}.
 *
 * @author Eugene Suleimanov
 * @version 1.0
 */

public interface PersonService {

    List<Person> getAll();

    Person findByUsername(String username);

    Person findByEmail(String email);

    Person findById(Integer id);

    void delete(Integer id);

    ResponseDto registration(RegisterRequestDto dto);
}