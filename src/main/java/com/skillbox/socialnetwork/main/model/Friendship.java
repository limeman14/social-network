package com.skillbox.socialnetwork.main.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@NoArgsConstructor
@AllArgsConstructor
@Entity
@Data
@Table(name = "friendships")
public class Friendship {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotNull
    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "status_id")
    private FriendshipStatus status;

    @NotNull
    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "src_person_id")
    private Person srcPerson;

    @NotNull
    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "dst_person_id")
    private Person dstPerson;
}
