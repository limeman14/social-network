package com.skillbox.socialnetwork.main.model;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
@Table(name = "tag")
public class Tag {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String tag;
}
