package com.skillbox.socialnetwork.main.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.validation.constraints.NotNull;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "post_likes")
public class PostLike {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    //@TODO: При сериализации возвращать long
    @NotNull
    private LocalDateTime time;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @NotNull
    @JoinColumn(name = "person_id")
    private Person person;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @NotNull
    @JoinColumn(name = "post_id")
    private Post post;
}
