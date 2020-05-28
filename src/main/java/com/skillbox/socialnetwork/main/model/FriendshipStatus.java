package com.skillbox.socialnetwork.main.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Entity
@Data
@Table(name = "friendship_status")
public class FriendshipStatus {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotNull
    //@TODO: При сериализации возвращать long
    private LocalDateTime time;

    //Не используется
    private String name;

    @NotNull
    @Enumerated(value = EnumType.STRING)
    private FriendshipCode code;

    @OneToMany(mappedBy = "status")
    private List<Friendship> friendships;
}
