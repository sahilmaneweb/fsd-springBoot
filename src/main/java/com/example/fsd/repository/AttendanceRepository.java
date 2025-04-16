package com.example.fsd.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.fsd.entity.Attendance;
import com.example.fsd.entity.Attendance.AttendanceStatus;


import jakarta.transaction.Transactional;

@Repository
public interface AttendanceRepository extends JpaRepository<Attendance, Long> {
    List<Attendance> findByStudent_UidAndDateBetween(String uid, LocalDate startDate, LocalDate endDate);
    Optional<Attendance> findByStudent_UidAndDate(String uid, LocalDate date);
    List<Attendance> findByStudent_UidAndDateAndStatus(String uid, LocalDate date, Attendance.AttendanceStatus status);
    

    // Removed duplicate method definition to resolve the error.

    @Modifying
    @Transactional
    @Query("DELETE FROM Attendance a WHERE a.student.id = :studentId")
    void deleteAllByStudentId(@Param("studentId") String studentId);

    List<Attendance> findByStudent_UidAndStatus(String uid, AttendanceStatus status);


    List<Attendance> findByDateBetweenAndStudent_Batch_BatchName(LocalDate start, LocalDate end, String batchName);


    @Query("SELECT DISTINCT a.date FROM Attendance a")
    List<LocalDate> findAllDistinctDates();

    // Total distinct dates where any student was marked PRESENT
@Query("SELECT DISTINCT a.date FROM Attendance a WHERE a.status = 'PRESENT'")
List<LocalDate> findDistinctDatesWhereAnyStudentPresent();

// Distinct dates where a specific student was marked PRESENT
@Query("SELECT DISTINCT a.date FROM Attendance a WHERE a.student.uid = :uid AND a.status = 'PRESENT'")
List<LocalDate> findDistinctDatesByStudentUidAndStatus(@Param("uid") String uid, @Param("status") Attendance.AttendanceStatus status);

}
