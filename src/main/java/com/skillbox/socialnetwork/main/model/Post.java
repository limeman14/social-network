package com.skillbox.socialnetwork.main.model;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "post")
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    //@TODO: При сериализации возвращать long
    @Column(nullable = false)
    private LocalDateTime time;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id", nullable = false)
    private Person author;

    @Column(nullable = false)
    private String title;

    //@TODO: У HTML-текста тип String?
    @Column(name = "post_text", nullable = false)
    private String postText;

    //@TODO: Посмотреть, в каком виде Api возвращает это значение
    @Column(name = "is_blocked")
    private Boolean isBlocked = false;
}
