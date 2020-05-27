package com.skillbox.socialnetwork.main.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "notifications")
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @NonNull
    @JoinColumn(name = "type_id")
    private NotificationType type;

    //@TODO: При сериализации возвращать long
    private LocalDateTime sentTime;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @NonNull
    @JoinColumn(name = "person_id")
    private Person person;

    //@TODO: добавить entity_id!

    @NonNull
    private String contact;
}
