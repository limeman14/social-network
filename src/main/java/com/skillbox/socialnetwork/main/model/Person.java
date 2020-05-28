package com.skillbox.socialnetwork.main.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Entity
@Data
@Table(name = "person")
@NoArgsConstructor
public class Person {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    @Column(nullable = false)
    private Date regDate;

    @Column(nullable = false)
    private Date birthDate;

    @Column(name = "e_mail")
    private String email;

    private String phone;

    private String password;

    private String photo;

    private String about;

    private String town;

    private String confirmationCode;

    @Column(name = "is_approved")
    private Byte approved;

    @Enumerated(EnumType.STRING)
    @Column
    private Permission messagesPermission;

    @Column(name = "last_online_time")
    private Date lastOnline;


    @Column(name = "is_blocked")
    private Boolean blocked;
}