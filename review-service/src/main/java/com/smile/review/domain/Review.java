package com.smile.review.domain;

import jakarta.persistence.*;

import java.sql.ConnectionBuilder;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@Entity
@Table(name = "reviews")
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long reviewId;

//    @ManyToOne(fetch = FetchType.LAZY,optional = false)
//    @JoinColumn(name ="user_id")
//    private User user;
//
//    @ManyToOne(fetch = FetchType.LAZY,optional = false)
//    @JoinColumn(name = "film_id")
//    private Film film;

    @Column(columnDefinition = "Text", nullable = false)
    private String content;

    @Column(nullable = false)
    private int rating; //1-5

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    private LocalDateTime updateAt;

    @OneToMany(mappedBy = "review", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<Comment> comments = new ArrayList<>();


    @OneToMany(mappedBy = "review", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<ReviewLike> likes = new ArrayList<>();




    @PrePersist
    public void prePersist(){
        createdAt = LocalDateTime.now();
    }

    @PreUpdate
    public void preUpdate(){
        updateAt = LocalDateTime.now();
    }



}
