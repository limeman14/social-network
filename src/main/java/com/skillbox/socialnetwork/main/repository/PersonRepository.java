package com.skillbox.socialnetwork.main.repository;

import com.skillbox.socialnetwork.main.model.Person;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PersonRepository extends JpaRepository<Person, Integer> {
    Person findByEmail(String email);

    Person findPersonById(Integer id);

}
