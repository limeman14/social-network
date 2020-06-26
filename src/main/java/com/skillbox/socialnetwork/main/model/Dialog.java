package com.skillbox.socialnetwork.main.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "dialogs")
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Dialog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @OneToMany(mappedBy = "dialog")
    @ToString.Exclude
    private List<DialogToPerson> dialogToPersonList;
    @OneToMany(mappedBy = "dialog")
    @ToString.Exclude
    private List<Message> messages;
    @CreationTimestamp
    private Date creationDate;
}
