package com.skillbox.socialnetwork.main.model;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Entity
@Data
@Table(name = "notifications")
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "type_id")
    private NotificationType type;

    @NotNull
    private Date sentTime;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "person_id")
    private Person person;

    //@TODO: добавить entity_id!

    //@NotNull
    //private MainEntity entityId;

    @NotNull
    private String contact;
}
