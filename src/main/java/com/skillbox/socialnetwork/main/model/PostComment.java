package com.skillbox.socialnetwork.main.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Entity
@Data
@Table(name = "post_comments")
public class PostComment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    //@TODO: При сериализации возвращать long
    @NotNull
    private LocalDateTime time;

    @NotNull
    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    //может быть null, так как нет родительского коммента
    @JoinColumn(name = "parent_id")
    private PostComment parentComment;

    @NotNull
    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
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
