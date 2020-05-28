package com.skillbox.socialnetwork.main.model;

import com.skillbox.socialnetwork.main.model.enumerated.NotificationCode;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;

@Entity
@Data
@Table(name = "notification_types")
public class NotificationType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotNull
    @Enumerated(value = EnumType.STRING)
    private NotificationCode code;

    private String name;

    @OneToMany(mappedBy = "type")
    private List<Notification> notifications;
}
