package com.skillbox.socialnetwork.main.model;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
@Table(name = "friendship")
public class Friendship {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @Column(name = "status_id", nullable = false)
    private FriendshipStatus friendshipStatus;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @Column(name = "src_person_id", nullable = false)
    private Person srcPerson;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @Column(name = "dst_person_id", nullable = false)
    private Person dstPerson;
}
