package com.example.fsd.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.fsd.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    void deleteByUsername(String username);

    User findByUsername(String studentId);   
}
