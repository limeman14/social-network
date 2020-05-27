package com.skillbox.socialnetwork.main.model;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
@Table(name = "post2tag")
public class Post2tag {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @Column(name = "post_id", nullable = false)
    private Post post;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @Column(name = "tag_i", nullable = false)
    private Tag tag;
}
