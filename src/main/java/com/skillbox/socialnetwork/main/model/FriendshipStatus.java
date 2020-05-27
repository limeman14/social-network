package com.skillbox.socialnetwork.main.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.validation.constraints.NotNull;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "friendship_statuses")
public class FriendshipStatus {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    //@TODO: При сериализации возвращать long
    @NotNull
    private LocalDateTime time;

    //Что тут хранить? Удаляем?
    private String name;

    @Enumerated(value = EnumType.STRING)
    @NotNull
    private FriendshipCode code;
}
