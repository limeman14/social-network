package com.skillbox.socialnetwork.main.security.service;

import com.skillbox.socialnetwork.main.model.Person;

import java.util.List;

public interface UserService {

    Person register(Person user);

    List<Person> getAll();

    Person findByEmail(String email);

    Person findById(int id);

    void delete(int id);

    void logout(Person user);
}
