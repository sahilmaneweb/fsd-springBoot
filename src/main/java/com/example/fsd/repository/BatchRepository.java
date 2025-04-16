package com.example.fsd.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.fsd.entity.Batch;

@Repository
public interface BatchRepository extends JpaRepository<Batch, String> {
    
}
