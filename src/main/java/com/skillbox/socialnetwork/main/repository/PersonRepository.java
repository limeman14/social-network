package com.skillbox.socialnetwork.main.repository;

import com.skillbox.socialnetwork.main.model.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;

public interface PersonRepository extends JpaRepository<Person, Integer> {
    Person findByEmail(String email);

    Person findPersonById(Integer id);

    @Query("select p from Person p where " +
            "(:firstName is null or p.firstName = :firstName) and " +
            "(:lastName is null or p.lastName = :lastName) and " +
            "(p.birthDate between :dateFrom and :dateTo)")
    List<Person> search(@Param("firstName")String name, @Param("lastName")String surname,
                        @Param("dateFrom")Date dateFrom, @Param("dateTo")Date dateTo);

}
