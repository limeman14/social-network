package com.skillbox.socialnetwork.main.model;

import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Data
@Entity
public class Town {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @OneToOne
    private City city;

    @OneToOne
    private Country country;

    @OneToMany(mappedBy = "town")
    private List<Person> townPersons;
}
