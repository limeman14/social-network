package com.skillbox.socialnetwork.main.repo;

import com.skillbox.socialnetwork.main.model.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PersonRepository extends JpaRepository<Person, Integer> {

    Optional<Person> findByEmail(String email);

    Boolean existsByEmail(String email);
}
