package com.skillbox.socialnetwork.main.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "persons")
public class Person {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotNull
    private String firstName;

    @NotNull
    private String lastName;

    //@TODO: При сериализации возвращать long
    private LocalDateTime regDate;

    //@TODO: При сериализации возвращать long
    private LocalDate birthDate;

    @NotNull
    @Column(name = "e_mail", unique = true)
    private String email;

    private String phone;

    @NotNull
    private String password;

    @Type(type = "text")
    private String photo;

    @Type(type = "text")
    private String about;

    //@TODO: Подумать, как лучше реализовать Town
    private String town;

    private String confirmationCode;

    @Column(name = "is_approved")
    private Boolean approved;

    @Enumerated(EnumType.STRING)
    private Permission messagesPermission;

    @Column(name = "last_online_time")
    private LocalDateTime lastOnline;

    @Column(name = "is_blocked")
    private Boolean blocked;

    @OneToMany(mappedBy = "person")
    private List<BlockHistory> blockHistories;

    @OneToMany(mappedBy = "srcPerson")
    private List<Friendship> friendshipsSrc;

    @OneToMany(mappedBy = "dstPerson")
    private List<Friendship> friendshipsDst;

    @OneToMany(mappedBy = "author")
    private List<Message> sentMessages;

    @OneToMany(mappedBy = "recipient")
    private List<Message> recipientMessages;

    @OneToMany(mappedBy = "author")
    private List<Post> posts;

    @OneToMany(mappedBy = "person")
    private List<PostLike> likes;

    @OneToMany(mappedBy = "author")
    private List<PostComment> comments;

    @OneToMany(mappedBy = "person")
    private List<Notification> notifications;
}