package com.skillbox.socialnetwork.main.model;

import com.skillbox.socialnetwork.main.model.enumerated.Permission;
import lombok.Data;
import org.hibernate.annotations.Type;
import org.springframework.data.annotation.CreatedDate;

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

    @CreatedDate
    private Date regDate;

    private Date birthDate;

    @NotNull
    @Column(unique = true)
    private String email;

    private String phone;

    @NotNull
    private String password;

    @Type(type = "text")
    private String photo;

    @Type(type = "text")
    private String about;

    @ManyToOne
    @JoinColumn(name = "town_id")
    private Town town;

    @NotNull
    private String confirmationCode;

    private Boolean isApproved;

    @NotNull
    @Enumerated(EnumType.STRING)
    private Permission messagesPermission;

    private Date lastOnlineTime;

    private Boolean isBlocked;

    @OneToMany(mappedBy = "person", cascade = CascadeType.ALL)
    private List<BlockHistory> blockHistories;

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

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "user_roles",
            joinColumns = {@JoinColumn(name = "user_id", referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(name = "role_id", referencedColumnName = "id")})
    private List<Role> roles;

}