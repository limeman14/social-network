package com.skillbox.socialnetwork.main.model;

import lombok.Data;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

@Entity
@Data
@Table(name = "posts")
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    //@TODO: При сериализации возвращать long
    @NotNull
    private Date time;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "author_id")
    private Person author;

    @NotNull
    private String title;

    @NotNull
    @Type(type = "text")
    private String postText;

    @NotNull
    private Boolean isBlocked;

    @OneToMany(mappedBy = "tag")
    private List<Post2tag> tags;

    @OneToMany(mappedBy = "post")
    private List<PostLike> likes;

    @OneToMany(mappedBy = "post")
    private List<PostFile> files;

    @OneToMany(mappedBy = "post")
    private List<PostComment> comments;

    @OneToMany(mappedBy = "post")
    private List<BlockHistory> blockHistories;
}
