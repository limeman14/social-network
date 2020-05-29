package com.skillbox.socialnetwork.main.model;

import lombok.Data;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

@Entity
@Data
@Table(name = "post_comments")
public class PostComment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    //@TODO: При сериализации возвращать long
    @NotNull
    private Date time;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "post_id")
    private Post post;

    @ManyToOne
    //может быть null, так как нет родительского коммента
    @JoinColumn(name = "parent_id")
    private PostComment parentComment;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "author_id")
    private Person author;

    @NotNull
    @Type(type = "text")
    private String commentText;

    @NotNull
    private Boolean isBlocked;

    @OneToMany(mappedBy = "parentComment")
    private List<PostComment> childComments;

    @OneToMany(mappedBy = "comment")
    private List<BlockHistory> blockHistories;
}
