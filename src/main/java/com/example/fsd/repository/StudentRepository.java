package com.example.fsd.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.fsd.entity.Group;
import com.example.fsd.entity.Student;

@Repository
public interface StudentRepository extends JpaRepository<Student, String> {
    // Custom query methods can be defined here if needed
    // For example:

    List<Student> findByBatch_BatchName(String batchName);
    // 1. Find students by batch name and group IS NULL
    @Query("SELECT s FROM Student s WHERE s.batch.batchName = :batchName AND s.group IS NULL")
    List<Student> findByBatchNameAndGroupIsNull(@Param("batchName") String batchName);

    // 2. Find students by batch name and group IS NOT NULL
    @Query("SELECT s FROM Student s WHERE s.batch.batchName = :batchName AND s.group IS NOT NULL")
    List<Student> findByBatchNameAndGroupIsNotNull(@Param("batchName") String batchName);

    // 3. Find students by batch name and exact group ID
    @Query("SELECT s FROM Student s WHERE s.batch.batchName = :batchName AND s.group.groupId = :groupId")
    List<Student> findByBatchNameAndGroupId(@Param("batchName") String batchName, @Param("groupId") String groupId);

    // 4. Find students by batch name where group is either NULL or matches a specific group ID
    @Query("SELECT s FROM Student s WHERE s.batch.batchName = :batchName AND (s.group IS NULL OR s.group.groupId = :groupId)")
    List<Student> findByBatchNameAndGroupIsNullOrGroupId(@Param("batchName") String batchName, @Param("groupId") String groupId);

    // 5. Find students by batch name and group ID
    Iterable<Student> findByBatch_BatchNameAndGroupIsNull(String batchName);
    List<Student> findByGroup(Group group);
}
