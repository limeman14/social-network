package com.skillbox.socialnetwork.main.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;


@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "people")
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

    private LocalDate birthDate;

    private String email;

    private String phone;

    @JsonIgnore
    private String password;

    private String photo;

    @Column
    private String about;

    private String town;

    @Column(name = "confirmation_code")
    private String confirmationCode;

    private Boolean approved;

    @Enumerated(EnumType.STRING)
    @Column(name = "messages_permission")
    private Permission messagesPermission;

    private LocalDateTime lastOnline;

    private Boolean blocked;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "user_roles",
            joinColumns = {@JoinColumn(name = "person_id", referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(name = "role_id", referencedColumnName = "id")})
    private List<Role> roles;
}
