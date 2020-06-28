package com.skillbox.socialnetwork.main.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.skillbox.socialnetwork.main.model.enumerated.NotificationCode;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;

@Entity
@Data
@Table(name = "notification_types")
@NoArgsConstructor
public class NotificationType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotNull
    @Enumerated(value = EnumType.STRING)
    private NotificationCode code;

    @JsonIgnore
    @ToString.Exclude
    @OneToMany(mappedBy = "type")
    private List<Notification> notifications;

    public NotificationType(int id) {
        this.id = id;
    }
}
