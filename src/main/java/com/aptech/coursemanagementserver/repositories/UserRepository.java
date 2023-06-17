package com.aptech.coursemanagementserver.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.aptech.coursemanagementserver.models.User;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);// SELECT username FROM users WHERE email = email

    Optional<User> findById(long id);
}
