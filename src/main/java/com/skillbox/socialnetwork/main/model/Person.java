package com.skillbox.socialnetwork.main.model;

import com.skillbox.socialnetwork.main.model.enumerated.Permission;
import lombok.Data;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

@Entity
@Data
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
    @NotNull
    private Date regDate;

    //@TODO: При сериализации возвращать long
    private Date birthDate;

    @NotNull
    @Column(unique = true)
    private String eMail;

    private String phone;

    @NotNull
    private String password;

    @Type(type = "text")
    private String photo;

    @Type(type = "text")
    private String about;

    @ManyToOne
    @JoinColumn(name = "town_id")
    private City town;

    @NotNull
    private String confirmationCode;

    private Boolean isApproved;

    @NotNull
    @Enumerated(EnumType.STRING)
    private Permission messagesPermission;

    private Date lastOnlineTime;

    private Boolean isBlocked;

    @ManyToOne
    private PersonRole role;

    @OneToMany(mappedBy = "srcPerson")
    private List<Friendship> srcFriendships;

    @OneToMany(mappedBy = "dstPerson")
    private List<Friendship> dstFriendships;

    @OneToMany(mappedBy = "author")
    private List<Message> sentMessages;

    @OneToMany(mappedBy = "recipient")
    private List<Message> receivedMessages;

    @OneToMany(mappedBy = "author")
    private List<Post> posts;

    @OneToMany(mappedBy = "person")
    private List<PostLike> likes;

    @OneToMany(mappedBy = "author")
    private List<PostComment> comments;

    @OneToMany(mappedBy = "person")
    private List<Notification> notifications;

    @OneToMany(mappedBy = "person")
    private List<BlockHistory> blockHistories;
}