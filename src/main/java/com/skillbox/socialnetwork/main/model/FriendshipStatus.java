package com.skillbox.socialnetwork.main.model;

import com.skillbox.socialnetwork.main.model.enumerated.FriendshipCode;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

@Entity
@Data
@Table(name = "friendship_status")
public class FriendshipStatus {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotNull
    //@TODO: При сериализации возвращать long
    private Date time;

    //Не используется
    private String name;

    @NotNull
    @Enumerated(value = EnumType.STRING)
    private FriendshipCode code;

    @OneToMany(mappedBy = "status")
    private List<Friendship> friendships;
}
