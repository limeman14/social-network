package com.skillbox.socialnetwork.main.model;

import com.skillbox.socialnetwork.main.model.enumerated.FriendshipCode;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Calendar;
import java.util.List;

@Entity
@Data
@Table(name = "friendship_statuses")
public class FriendshipStatus {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private Date time;

    private String name;

    @Enumerated(value = EnumType.STRING)
    private FriendshipCode code;

    @OneToMany(mappedBy = "status")
    private List<Friendship> friendships;
}
