package com.skillbox.socialnetwork.main.security.service;

import com.skillbox.socialnetwork.main.model.Person;
import com.skillbox.socialnetwork.main.model.Role;
import com.skillbox.socialnetwork.main.repo.PersonRepository;
import com.skillbox.socialnetwork.main.repo.RoleRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
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
    public Person register(Person user) {
        Role roleUser = roleRepository.findByName("ROLE_USER");
        List<Role> userRoles = new ArrayList<>();
        userRoles.add(roleUser);

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRoles(userRoles);

        Person registeredUser = personRepository.save(user);

        log.info("IN register - user: {} successfully registered", registeredUser);

        return registeredUser;
    }

    @Override
    public List<Person> getAll() {
        List<Person> result = personRepository.findAll();
        log.info("IN getAll - {} users found", result.size());
        return result;
    }

    @Override
    public Person findByEmail(String email) {
        Person result = personRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("User Not Found with email: " + email));
        log.info("IN findByUsername - user: {} found by email: {}", result, email);
        return result;
    }

    @Override
    public Person findById(int id) {
        Person result = personRepository.findById(id).orElse(null);

        if (result == null) {
            log.warn("IN findById - no user found by id: {}", id);
            return null;
        }

        log.info("IN findById - user: {} found by id: {}", result, id);
        return result;
    }

    @Override
    public void delete(int id) {
        personRepository.deleteById(id);
        log.info("IN delete - user with id: {} successfully deleted", id);
    }
}
