package com.smile.review.domain;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "review_comments")
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "review_id")
    private Review review;

//    @ManyToOne(fetch = FetchType.LAZY, optional = false)
//    @JoinColumn(name = "user_id")
//    private User user;

    @Column(columnDefinition = "TEXT", nullable = false, length = 2000)
    private String content; // 리뷰 텍스트

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    public  void prePersist(){
        createdAt = LocalDateTime.now();
    }
}
