package com.smile.review.domain;

import jakarta.persistence.*;

@Entity
@Table(name = "review_likes")
public class ReviewLike {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


}
