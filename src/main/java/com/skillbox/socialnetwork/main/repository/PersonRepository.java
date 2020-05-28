package com.skillbox.socialnetwork.main.repository;

import com.skillbox.socialnetwork.main.model.Person;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository interface that extends {@link JpaRepository} for class {@link Person}.
 *
 * @author Yuriy Alchin
 * @version 1.0
 */
public interface PersonRepository extends JpaRepository<Person, Integer> {
    Person findByEmail(String email);

    Person findPersonById(Integer id);
}
