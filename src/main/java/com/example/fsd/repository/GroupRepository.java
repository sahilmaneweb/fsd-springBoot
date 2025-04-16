package com.example.fsd.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.fsd.entity.Group;

@Repository
public interface GroupRepository extends JpaRepository<Group, String> {
    // Custom query methods can be defined here if needed
    // For example:
    // List<Group> findByName(String name);
    List<Group> findByBatch_BatchName(String batchName);
}
