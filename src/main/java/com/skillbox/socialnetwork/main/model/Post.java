package com.skillbox.socialnetwork.main.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.validation.constraints.NotNull;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "posts")
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    //@TODO: При сериализации возвращать long
    @NotNull
    private LocalDateTime time;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @NotNull
    @JoinColumn(name = "author_id")
    private Person author;

    @NotNull
    private String title;

    @NotNull
    @Type(type = "text")
    private String postText;

    @Column(name = "is_blocked")
    private Boolean blocked;

    @OneToMany(mappedBy = "post")
    private List<BlockHistory> blockHistories;

    @OneToMany(mappedBy = "post")
    private List<Post2tag> post2tagList;

    @OneToMany(mappedBy = "post")
    private List<PostLike> likes;

    @OneToMany(mappedBy = "post")
    private List<PostFile> files;

    @OneToMany(mappedBy = "post")
    private List<PostComment> comments;
}