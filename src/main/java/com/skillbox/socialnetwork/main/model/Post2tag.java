package com.skillbox.socialnetwork.main.model;

import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Entity
@Data
@Table(name = "post2tag")
public class Post2tag {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;

    @OneToMany(cascade = CascadeType.ALL)
    @Column(name = "tag_id", nullable = false)
    private List<Tag> tags;
}
