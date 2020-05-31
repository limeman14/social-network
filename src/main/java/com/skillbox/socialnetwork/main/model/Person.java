package com.skillbox.socialnetwork.main.model;

import com.skillbox.socialnetwork.main.model.enumerated.Permission;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.Calendar;
import java.util.List;

@Entity
@Data
@Table(name = "persons")
public class Person {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String firstName;

    private String lastName;

    @CreationTimestamp
    private Calendar regDate;

    private Calendar birthDate;

    @Column(name = "e_mail", unique = true)
    private String email;

    private String phone;

    private String password;

    @Type(type = "text")
    private String photo;

    @Type(type = "text")
    private String about;

    @ManyToOne
    private Town town;

    private String confirmationCode;

    private Boolean isApproved;

    @Enumerated(EnumType.STRING)
    private Permission messagesPermission;

    @Column(name = "last_online_time")
    private Calendar lastOnline;

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

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "user_roles",
            joinColumns = {@JoinColumn(name = "user_id", referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(name = "role_id", referencedColumnName = "id")})
    private List<Role> roles;

}