package com.skillbox.socialnetwork.main.model;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "friendship_status")
public class FriendshipStatus {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    //@TODO: При сериализации возвращать long
    @Column(nullable = false)
    private LocalDateTime time;

    //Что здесь хранится?
    private String name;

    @Enumerated(value = EnumType.STRING)
    @Column(columnDefinition = "enum", nullable = false)
    private FriendshipCode code;
}
