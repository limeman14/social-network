package com.skillbox.socialnetwork.main.repository;

import com.skillbox.socialnetwork.main.model.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;
import java.util.List;

public interface PersonRepository extends JpaRepository<Person, Integer> {
    Person findByEmail(String email);

    Person findPersonById(Integer id);

    @Query("select p from Person p where " +
            "p.firstName like %?1% and " +
            "p.lastName like %?2% and " +
            "p.birthDate between ?3 and ?4 and " +
            "p.town.country.title like %?5% and " +
            "p.town.city.title like %?6%")
    List<Person> search(String name, String surname, Date dateFrom, Date dateTo, String countryName, String cityName);

}
