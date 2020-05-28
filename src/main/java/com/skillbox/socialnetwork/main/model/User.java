package com.skillbox.socialnetwork.main.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@NoArgsConstructor
@AllArgsConstructor
@Entity
@Data
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotNull
    private String name;

    @NotNull
    private String eMail;

    @NotNull
    private String password;

    @NotNull
    @Enumerated(value = EnumType.STRING)
    private UserType type;

//    public User(Integer id, @NotNull String name, @NotNull String eMail, @NotNull String password, @NotNull UserType type) {
//        this.id = id;
//        this.name = name;
//        this.eMail = eMail;
//        this.password = password;
//        this.type = type;
//    }
}
