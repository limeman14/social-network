package com.skillbox.socialnetwork.main.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.validation.constraints.NotNull;

import javax.persistence.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "friendships")
public class Friendship {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @NotNull
    @JoinColumn(name = "status_id", nullable = false)
    private FriendshipStatus friendshipStatus;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @NotNull
    @JoinColumn(name = "src_person_id", nullable = false)
    private Person srcPerson;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @NotNull
    @JoinColumn(name = "dst_person_id")
    private Person dstPerson;
}
