package com.skillbox.socialnetwork.main.model;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@Table(name = "roles")
public class PersonRole {
    //@TODO: Доделать сущность по мере необходимости

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotNull
    private String role;

    @OneToMany(mappedBy = "role")
    private List<Person> personList;
}
