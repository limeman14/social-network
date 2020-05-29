package com.skillbox.socialnetwork.main.model;

import com.skillbox.socialnetwork.main.model.enumerated.ReadStatus;
import lombok.Data;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Entity
@Data
@Table(name = "messages")
public class Message {
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
    @ManyToOne
    @JoinColumn(name = "recipient_id")
    private Person recipient;

    @NotNull
    @Type(type = "text")
    private String messageText;

    @NotNull
    @Enumerated(value = EnumType.STRING)
    private ReadStatus readStatus;
}
