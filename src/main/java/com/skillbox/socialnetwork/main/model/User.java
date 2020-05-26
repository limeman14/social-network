package com.skillbox.socialnetwork.main.model;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
@Table(name = "user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;

    @Column(name = "e_mail")
    private String email;

    private String password;

    @Enumerated(value = EnumType.STRING)
    private UserType type;
}
