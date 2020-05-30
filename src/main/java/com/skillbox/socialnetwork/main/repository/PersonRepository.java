package com.skillbox.socialnetwork.main.repository;

import com.skillbox.socialnetwork.main.model.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PersonRepository extends JpaRepository<Person, Integer> {

    void deleteById(int id);

    Optional<Person> findByEmail(String email);
}
