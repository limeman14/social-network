package com.skillbox.socialnetwork.main.service.impl;

import com.skillbox.socialnetwork.main.model.Person;
import com.skillbox.socialnetwork.main.repository.PersonRepository;
import com.skillbox.socialnetwork.main.service.PersonService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class PersonServiceImpl implements PersonService {

    @Autowired
    private PersonRepository repository;

    @Override
    public List<Person> getAll() {
        return repository.findAll();
    }

    @Override
    public Person findByUsername(String username) {
        return null;
    }

    @Override
    public Person findByEmail(String email) {
        return repository.findByEmail(email);
    }

    @Override
    public Person findById(Integer id) {
        return repository.findPersonById(id);
    }

    @Override
    public void delete(Integer id) {

    }
}
