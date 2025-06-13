package com.smile.review.repository.user;

import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository {
    Optional<Object> findByUsername(String username);
}
