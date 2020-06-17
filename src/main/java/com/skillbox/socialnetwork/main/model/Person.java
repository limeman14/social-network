package com.skillbox.socialnetwork.main.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.skillbox.socialnetwork.main.model.enumerated.Permission;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.util.Date;
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
    private Date regDate;

    @Column(name = "birth_date")
    private Date birthDate;

    @Column(name = "e_mail", unique = true)
    private String email;

    private String phone;

    @JsonIgnore
    private String password;

    private String photo;

    private String about;

    private String city;

    private String country;

    private String confirmationCode;

    private Boolean isApproved;

    @Enumerated(EnumType.STRING)
    private Permission messagesPermission;

    private Date lastOnlineTime;

    private Boolean isBlocked;

    @ManyToMany(fetch = FetchType.EAGER)
    @JsonIgnore
    @JoinTable(name = "user_roles",
            joinColumns = {@JoinColumn(name = "user_id", referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(name = "role_id", referencedColumnName = "id")})
    private List<Role> roles;

    @OneToMany(mappedBy = "person")
    @JsonIgnore
    private List<BlockHistory> blockHistories;

    @OneToMany(mappedBy = "srcPerson")
    @JsonIgnore
    private List<Friendship> friendshipsSrc;

    @OneToMany(mappedBy = "dstPerson")
    @JsonIgnore
    private List<Friendship> friendshipsDst;

    @OneToMany(mappedBy = "author")
    @JsonIgnore
    private List<Message> sentMessages;

    @OneToMany(mappedBy = "recipient")
    @JsonIgnore
    private List<Message> recipientMessages;

    @OneToMany(mappedBy = "author", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Post> posts;

    @OneToMany(mappedBy = "person")
    @JsonIgnore
    private List<PostLike> likes;

    @OneToMany(mappedBy = "author")
    @JsonIgnore
    private List<PostComment> comments;

    @OneToMany(mappedBy = "person")
    @JsonIgnore
    private List<Notification> notifications;

    @OneToMany(mappedBy = "person")
    @JsonIgnore
    private List<DialogToPerson> dialogToPeople;

    @Override
    public String toString() {
        return id + " " + firstName + " " + lastName;
    }
}