package com.skillbox.socialnetwork.main.model;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "message")
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    //@TODO: При сериализации возвращать long
    @Column(nullable = false)
    private LocalDateTime time;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @Column(name = "author_id", nullable = false)
    private Person author;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @Column(name = "recipient_id", nullable = false)
    private Person recipient;

    @Column(name = "message_text", nullable = false)
    private String messageText;

    @Enumerated(value = EnumType.STRING)
    @Column(name = "read_status", nullable = false, columnDefinition = "enum")
    private ReadStatus readStatus;
}
