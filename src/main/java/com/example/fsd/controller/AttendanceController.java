package com.example.fsd.controller;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

import com.example.fsd.entity.Attendance;
import com.example.fsd.entity.Batch;
import com.example.fsd.entity.Student;
import com.example.fsd.entity.Attendance.AttendanceStatus;
import com.example.fsd.repository.AttendanceRepository;
import com.example.fsd.repository.BatchRepository;
import com.example.fsd.repository.StudentRepository;
import com.example.fsd.response.JwtUtil;
import com.example.fsd.response.ResponseBean;

import io.jsonwebtoken.Claims;

import com.example.fsd.response.AttendanceDto;
import com.example.fsd.response.AttendanceDto.AttendanceDTO;
import com.example.fsd.response.ErrorResponse;

import org.springframework.web.bind.annotation.CrossOrigin;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/attendance")
public class AttendanceController {

    @Autowired 
    public AttendanceRepository attendanceRepo;
    @Autowired
    public StudentRepository studentRepo;
    @Autowired
    public BatchRepository batchRepo;
    @Autowired
    public JwtUtil jwtUtil;
    // Get all attendance records
    @GetMapping("/")
    public ResponseEntity<?> getAttendance(@RequestHeader("Authorization") String token) {
        ResponseBean response = new ResponseBean();
        try{
            System.out.println(token);
            Claims claims = jwtUtil.validateRole(token, List.of("admin", "mentor","student"));
            System.out.println(claims);
            List<Attendance> all = attendanceRepo.findAll();

        if (all.isEmpty()) {
            response.setStatus(false);
            response.setMessage("No attendance records found.");
            return ResponseEntity.ok(response);
        }

        response.setStatus(true);
        response.setMessage("Attendance records fetched successfully.");
        response.setData(all);
        return ResponseEntity.ok(response);
        }catch (JwtUtil.JwtException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ErrorResponse("JWT "+e.getMessage(), 401));
        } catch(Exception e){
            response.setStatus(false);
            response.setMessage("Error fetching attendance records: " + e.getMessage());
            return ResponseEntity.status(500).body(response);
        }
    }

    // Get attendance by month and batch
    @GetMapping(path = "/{batchName}/{month}", produces = "application/json")
public ResponseEntity<?> getAttendanceByMonthAndBatch(
        @RequestHeader("Authorization") String token,
        @PathVariable String batchName,
        @PathVariable String month) {
    
    ResponseBean response = new ResponseBean();
    try {
        System.out.println(token);
        Claims claims = jwtUtil.validateRole(token, List.of("admin", "mentor", "student"));
        System.out.println(claims);

        // Parse month string like "2025-04" to YearMonth
        YearMonth yearMonth = YearMonth.parse(month); // Format must be yyyy-MM
        LocalDate startDate = yearMonth.atDay(1);
        LocalDate endDate = yearMonth.atEndOfMonth();
        System.out.println("YearMonth : "+yearMonth+" StartDate : "+startDate+" endDate : "+endDate);

        Optional<Batch> batchObj = batchRepo.findById(batchName);
        if (batchObj.isEmpty()) {
            response.setStatus(false);
            response.setMessage("Batch not found.");
            return ResponseEntity.ok(response);
        }

        List<Attendance> attendanceList = attendanceRepo.findByDateBetweenAndStudent_Batch_BatchName(
                startDate, endDate, batchName);

        if (attendanceList.isEmpty()) {
            response.setStatus(false);
            response.setMessage("No attendance records found for the specified month and batch.");
            return ResponseEntity.ok(response);
        }

        Map<String, List<Attendance>> attendanceMap = new HashMap<>();
        for (Attendance attendance : attendanceList) {
            String uid = attendance.getStudent().getUid();
            attendanceMap.computeIfAbsent(uid, k -> new java.util.ArrayList<>()).add(attendance);
        }

        response.setStatus(true);
        response.setMessage("Attendance records fetched successfully.");
        response.setData(attendanceMap);
        return ResponseEntity.ok(response);

    } catch (JwtUtil.JwtException e) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(new ErrorResponse("JWT " + e.getMessage(), 401));
    } catch (Exception e) {
        response.setStatus(false);
        response.setMessage("Error fetching attendance records: " + e.getMessage());
        return ResponseEntity.status(500).body(response);
    }
}


    // Mark attendance for a list of students
    @PostMapping(path = "/mark", consumes = "application/json", produces = "application/json")
    public ResponseEntity<?> markAttendance(@RequestHeader("Authorization") String token,@RequestBody List<AttendanceDTO> attendanceDTOs) {
        System.out.println("Received AttendanceDTO List: " + attendanceDTOs); // Debug

        ResponseBean response = new ResponseBean();

        if (attendanceDTOs == null || attendanceDTOs.isEmpty()) {
            response.setStatus(false);
            response.setMessage("Invalid attendance data provided.");
            return ResponseEntity.badRequest().body(response);
        }

        try {
            System.out.println(token);
            Claims claims = jwtUtil.validateRole(token, List.of("admin", "mentor","student"));
            System.out.println(claims);
            for (AttendanceDTO attendanceDTO : attendanceDTOs) {
                String uid = attendanceDTO.getUid();
                LocalDate date = LocalDate.parse(attendanceDTO.getDate()); // Ensure the date is parsed correctly
                AttendanceStatus status = attendanceDTO.getStatus();

                Optional<Attendance> existingAttendanceOpt = attendanceRepo.findByStudent_UidAndDate(uid, date);
                if (existingAttendanceOpt.isPresent()) {
                    Attendance existingAttendance = existingAttendanceOpt.get();
                    existingAttendance.setStatus(status);  // Update status
                    attendanceRepo.save(existingAttendance);
                    continue; // Skip to the next record if already exists
                }

                Optional<Student> studentOpt = studentRepo.findById(uid);
                if (studentOpt.isPresent()) {
                    Student student = studentOpt.get();
                    Attendance attendance = new Attendance(null, student, date, status);
                    attendanceRepo.save(attendance);
                } else {
                    // Student not found, continue with next record
                    System.out.println("Student not found: " + uid);
                    continue;
                }
            }
            response.setStatus(true);
            response.setMessage("Attendance marked successfully.");
            return ResponseEntity.ok(response);

        }catch (JwtUtil.JwtException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ErrorResponse("JWT "+e.getMessage(), 401));
        } catch (Exception e) {
            response.setStatus(false);
            response.setMessage("Error marking attendance: " + e.getMessage());
            return ResponseEntity.status(500).body(response);
        }
    }

    @GetMapping("/student/dashboard")
public ResponseEntity<?> getStudentAttendanceDashboard(@RequestHeader("Authorization") String token) {
    ResponseBean response = new ResponseBean();
    try {
        Claims claims = jwtUtil.validateRole(token, List.of("student", "mentor", "admin"));
        String studentId = claims.getSubject(); // UID from JWT

        // ✅ Total distinct dates where any student was PRESENT
        List<LocalDate> allPresentDates = attendanceRepo.findDistinctDatesWhereAnyStudentPresent();

        // ✅ Distinct dates where this student was marked PRESENT
        List<LocalDate> studentPresentDates = attendanceRepo.findDistinctDatesByStudentUidAndStatus(studentId, AttendanceStatus.PRESENT);

        int totalLectures = allPresentDates.size();
        int presentLectures = studentPresentDates.size();
        float percentage = (totalLectures == 0) ? 0 : (presentLectures * 100f / totalLectures);

        AttendanceDto.attendanceDashboard dashboard = new AttendanceDto.attendanceDashboard(
            presentLectures,
            totalLectures,
            percentage
        );

        response.setStatus(true);
        response.setMessage("Student attendance dashboard calculated.");
        response.setData(dashboard);
        return ResponseEntity.ok(response);

    } catch (JwtUtil.JwtException e) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(new ErrorResponse("JWT " + e.getMessage(), 401));
    } catch (Exception e) {
        response.setStatus(false);
        response.setMessage("Error while fetching dashboard: " + e.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }
}

}