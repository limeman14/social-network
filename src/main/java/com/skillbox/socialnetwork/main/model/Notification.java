package com.skillbox.socialnetwork.main.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Entity
@Data
@Table(name = "notifications")
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotNull
    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "type_id")
    private NotificationType type;

    //@TODO: При сериализации возвращать long
    @NotNull
    private LocalDateTime sentTime;

    @NotNull
    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "person_id")
    private Person person;

    //@TODO: добавить entity_id!

    @NotNull
    private String contact;
}
