package com.skillbox.socialnetwork.main.service.impl;

import com.skillbox.socialnetwork.main.model.Person;
import com.skillbox.socialnetwork.main.model.Role;
import com.skillbox.socialnetwork.main.repository.PersonRepository;
import com.skillbox.socialnetwork.main.repository.RoleRepository;
import com.skillbox.socialnetwork.main.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class UserServiceImpl implements UserService {

    private final PersonRepository personRepository;
    private final RoleRepository roleRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(PersonRepository personRepository, RoleRepository roleRepository, BCryptPasswordEncoder passwordEncoder) {
        this.personRepository = personRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Person register(Person person) {
        Role personRole = roleRepository.findByName("USER");
        List<Role> personRoles = new ArrayList<>();
        personRoles.add(personRole);

        person.setPassword(passwordEncoder.encode(person.getPassword()));
        person.setRoles(personRoles);

        Person registeredPerson = personRepository.save(person);

        log.info("IN register - user: {} successfully registered", registeredPerson);

        return registeredPerson;
    }

    @Override
    public List<Person> getAll() {
        List<Person> personList = personRepository.findAll();
        log.info("IN getAll - {} users found", personList.size());
        return personList;
    }

    @Override
    public Person findByEmail(String email) {
        Person person = personRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("User Not Found with email: " + email));
        log.info("IN findByUsername - user: {} found by email: {}", person.getFirstName(), email);
        return person;
    }

    @Override
    public Person findById(int id) {
        Person person = personRepository.findById(id).orElseThrow(() -> new UsernameNotFoundException("User Not Found with id: " + id));
        log.info("IN findById - user: {} found by id: {}", person, id);
        return person;
    }

    @Override
    public void delete(int id) {
        personRepository.deleteById(id);
        log.info("IN delete - user with id: {} successfully deleted", id);
    }
}
