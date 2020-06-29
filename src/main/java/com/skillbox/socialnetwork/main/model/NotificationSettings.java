package com.skillbox.socialnetwork.main.model;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
@Table(name = "notification_settings")
public class NotificationSettings {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @OneToOne
    @JoinColumn(name = "person_id", referencedColumnName = "id")
    private Person person;

    @Column(name = "POST")
    private boolean postNotification;

    @Column(name = "POST_COMMENT")
    private boolean postCommentNotification;

    @Column(name = "COMMENT_COMMENT")
    private boolean commentCommentNotification;

    @Column(name = "FRIEND_REQUEST")
    private boolean friendRequestNotification;

    @Column(name = "FRIEND_BIRTHDAY")
    private boolean friendBirthdayNotification;

    @Column(name = "MESSAGE")
    private boolean messageNotification;
}
