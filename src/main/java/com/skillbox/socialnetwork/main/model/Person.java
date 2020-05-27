package com.skillbox.socialnetwork.main.model;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "person")
public class Person {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "last_name", nullable = false)
    private String lastName;

    //@TODO: При сериализации возвращать long
    @Column(name = "reg_date", nullable = false)
    private final LocalDateTime regDate = LocalDateTime.now();

    //@TODO: При сериализации возвращать long
    @Column(name = "birth_date", nullable = false)
    private LocalDate birthDate;

    @Column(name = "e_mail")
    private String email;

    private String phone;

    private String password;

    private String photo;

    @Column(columnDefinition = "TEXT")
    private String about;

    //@TODO: Подумать, как лучше реализовать Town
    private String town;

    @Column(name = "confirmation_code")
    private String confirmationCode;

    //@TODO: Посмотреть, в каком виде Api возвращает это значение
    @Column(name = "is_approved")
    private Boolean approved = false;

    @Enumerated(EnumType.STRING)
    @Column(name = "messages_permission", columnDefinition = "enum")
    private Permission messagesPermission;

    @Column(name = "last_online_time")
    private LocalDateTime lastOnline;

    //@TODO: Посмотреть, в каком виде Api возвращает это значение
    @Column(name = "is_blocked")
    private Boolean blocked;
}