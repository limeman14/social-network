package com.skillbox.socialnetwork.main.model;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Data
@Table(name = "friendships")
public class Friendship {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "status_id")
    private FriendshipStatus status;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "src_person_id")
    private Person srcPerson;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "dst_person_id")
    private Person dstPerson;
}
