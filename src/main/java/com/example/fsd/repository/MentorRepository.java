package com.example.fsd.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.fsd.entity.Mentor;

@Repository
public interface MentorRepository extends JpaRepository<Mentor, String> {
    // Custom query methods can be defined here if needed
    // For example:
    // List<Mentor> findByLastName(String lastName);
    List<Mentor> findByBatch_BatchName(String batchName);
}
