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
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "post_comments")
public class PostComment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    //@TODO: При сериализации возвращать long
    private LocalDateTime time;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @NotNull
    @JoinColumn(name = "post_id")
    private Post post;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private PostComment parentComment;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @NotNull
    @JoinColumn(name = "author_id")
    private Person author;

    @Type(type = "text")
    @Column(name = "comment_text")
    private String comment;

    @NotNull
    @Column(name = "is_blocked")
    private Boolean blocked;

    @OneToMany(mappedBy = "comment")
    private List<BlockHistory> blockHistories;

    @OneToMany(mappedBy = "parentComment") //Список комментов к этому комменту
    private List<PostComment> childrenComments;
}
