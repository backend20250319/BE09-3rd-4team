package com.smile.review.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "review_like",
        uniqueConstraints = @UniqueConstraint(columnNames = {"review_id", "user_id"}))
public class ReviewLike {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Review 엔티티와 다대일(ManyToOne) 매핑
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "review_id", nullable = false)
    private Review review;

/*
    // User 엔티티와 매핑: Principal#getName()으로 가져오는 userName에 해당하는 User 엔티티가 있어야 함.
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
*/

    private LocalDateTime createdAt;

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
    }


}
