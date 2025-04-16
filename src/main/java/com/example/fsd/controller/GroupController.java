package com.example.fsd.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.fsd.entity.Group;
import com.example.fsd.entity.Mentor;
import com.example.fsd.entity.Student;
import com.example.fsd.repository.BatchRepository;
import com.example.fsd.repository.GroupRepository;
import com.example.fsd.repository.MentorRepository;
import com.example.fsd.repository.StudentRepository;
import com.example.fsd.response.ResponseBean;

import io.jsonwebtoken.Claims;

import com.example.fsd.response.ErrorResponse;
import com.example.fsd.response.GroupDto.GroupRequest;
import com.example.fsd.response.GroupDto.GroupResponse;
import com.example.fsd.response.GroupDto.GroupResponseStudent;
import com.example.fsd.response.JwtUtil;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api")
public class GroupController {
    @Autowired
    public BatchRepository batchRepository;
    @Autowired
    public StudentRepository studentRepository;
    @Autowired 
    public GroupRepository groupRepository;
    @Autowired
    public MentorRepository mentorRepository;
    @Autowired
    public JwtUtil jwtUtil;
    // Get all Groups
    @GetMapping(path="/groups", produces = "application/json")
    public ResponseEntity<?> getAllGroups(@RequestHeader("Authorization") String token) {
        try {
            System.out.println(token);
            Claims claims = jwtUtil.validateRole(token, List.of("admin", "mentor","student"));
            System.out.println(claims);
            ResponseBean responseBean = new ResponseBean();
            responseBean.setStatus(true);
            responseBean.setData(groupRepository.findAll());
            return ResponseEntity.ok(responseBean);
        }catch (JwtUtil.JwtException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ErrorResponse("JWT "+e.getMessage(), 401));
        } catch (Exception e) {
            ResponseBean responseBean = new ResponseBean();
            responseBean.setStatus(false);
            responseBean.setMessage(e.getMessage());
            return ResponseEntity.status(500).body(responseBean);
        } 
    }

    @GetMapping(path="/mentor/groups/{mentorId}", produces = "application/json")
    public ResponseEntity<?> getMentorGroups(@RequestHeader("Authorization") String token,@PathVariable String mentorId) {
        ResponseBean responseBean = new ResponseBean();
        try {
            System.out.println(token);
            Claims claims = jwtUtil.validateRole(token, List.of("admin", "mentor","student"));
            System.out.println(claims);
            Mentor mentor = mentorRepository.findById(mentorId).orElse(null);
            if(mentor == null){
                responseBean.setMessage("Mentor not found");
                responseBean.setStatus(false);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseBean);
            }
            responseBean.setStatus(true);
            responseBean.setData(groupRepository.findByBatch_BatchName(mentor.getBatch().getBatchName()));
            return ResponseEntity.ok(responseBean);
        }catch (JwtUtil.JwtException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ErrorResponse("JWT "+e.getMessage(), 401));
        } catch (Exception e) {
           
            responseBean.setStatus(false);
            responseBean.setMessage(e.getMessage());
            return ResponseEntity.status(500).body(responseBean);
        } 
    }

    @GetMapping(path="/student/groups/{uid}", produces = "application/json")
    public ResponseEntity<?> getGroupForStudent(@RequestHeader("Authorization") String token,@PathVariable String uid) {
        ResponseBean responseBean = new ResponseBean();
        try {
            System.out.println(token);
            Claims claims = jwtUtil.validateRole(token, List.of("admin", "mentor","student"));
            System.out.println(claims);
            Student student = studentRepository.findById(uid).orElse(null);
            if (student == null) {
                responseBean.setStatus(false);
                responseBean.setMessage("Student not found");
                return ResponseEntity.status(404).body(responseBean);
            }else if(student.getGroup()==null){
                responseBean.setStatus(false);
                responseBean.setMessage("Student does not exist in any group");
                return ResponseEntity.status(404).body(responseBean);
            }
            Group group = groupRepository.findById(student.getGroup().getGroupId()).orElse(null);
            if (group == null) {
                responseBean.setStatus(false);
                responseBean.setMessage("Group not found");
                return ResponseEntity.status(404).body(responseBean);
            }
            List<Student> students = studentRepository.findByGroup(group);
            List<Mentor> mentors = mentorRepository.findByBatch_BatchName(group.getBatch().getBatchName());

            GroupResponseStudent groupResonse = new GroupResponseStudent();
            groupResonse.setGroupId(group.getGroupId());
            groupResonse.setProjectTitle(group.getProjectTitle());
            groupResonse.setProjectDescription(group.getProjectDescription());
            groupResonse.setBatchName(group.getBatch().getBatchName());
            groupResonse.setStudents(students);
            groupResonse.setMentors(mentors);
            responseBean.setData(groupResonse);
            responseBean.setMessage("Group found successfully");
            responseBean.setStatus(true);

            return ResponseEntity.ok(responseBean);

        }catch (JwtUtil.JwtException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ErrorResponse("JWT "+e.getMessage(), 401));
        }catch(Exception e){
            responseBean.setStatus(false);
            responseBean.setMessage(e.getMessage());
            return ResponseEntity.status(500).body(responseBean);
        }

    }

    // Get Particular Group
    @GetMapping(path="/groups/{groupId}", produces = "application/json")
    public ResponseEntity<?> getGroup(@RequestHeader("Authorization") String token,@PathVariable String groupId) {
        ResponseBean responseBean = new ResponseBean();
        try{
            System.out.println(token);
            Claims claims = jwtUtil.validateRole(token, List.of("admin", "mentor","student"));
            System.out.println(claims);
            Group group = groupRepository.findById(groupId).orElse(null);
            if (group == null) {
                responseBean.setStatus(false);
                responseBean.setMessage("Group not found");
                return ResponseEntity.status(404).body(responseBean);
            }
            List<Student> students = studentRepository.findByGroup(group);
            GroupResponse groupResponse = new GroupResponse();
            groupResponse.setGroupId(group.getGroupId());
            groupResponse.setProjectTitle(group.getProjectTitle());
            groupResponse.setProjectDescription(group.getProjectDescription());
            groupResponse.setBatchName(group.getBatch().getBatchName());
            groupResponse.setStudents(students);
            responseBean.setData(groupResponse);
            responseBean.setMessage("Group found successfully");
            responseBean.setStatus(true);
            responseBean.setData(groupResponse);
            return ResponseEntity.ok(responseBean);

        }catch (JwtUtil.JwtException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ErrorResponse("JWT "+e.getMessage(), 401));
        }catch(Exception e){
            responseBean.setStatus(false);
            responseBean.setMessage(e.getMessage());
            return ResponseEntity.status(500).body(responseBean);
        }
    
    }

    //Create a group
    @PostMapping(path="/admin/group", produces = "application/json", consumes = "application/json")
    public ResponseEntity<?> createGroup(@RequestHeader("Authorization") String token,@RequestBody GroupRequest groupreq ){
        System.out.println(token);
            Claims claims = jwtUtil.validateRole(token, List.of("admin", "mentor","student"));
            System.out.println(claims);
        ResponseBean responseBean = new ResponseBean();
        try {
            String gid = generateUniqueUid();
            Group group = new Group();
            group.setGroupId(gid);
            group.setProjectTitle(groupreq.getProjectTitle());
            group.setProjectDescription(groupreq.getProjectDescription());
            group.setBatch(batchRepository.findById(groupreq.getBatchName()).orElseThrow(() -> new RuntimeException("Batch not found")));
            Group savedGroup = groupRepository.save(group);

            Iterable<Student> students = studentRepository.findAllById(groupreq.getStudentUid());
            for (Student student : students) {
                student.setGroup(group);
            }
            studentRepository.saveAll(students);
            responseBean.setStatus(true);
            responseBean.setMessage("Group created successfully");
            responseBean.setData(savedGroup);
            return ResponseEntity.ok(responseBean);
        }catch (JwtUtil.JwtException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ErrorResponse("JWT "+e.getMessage(), 401));
        } catch (Exception e) {
            responseBean.setStatus(false);
            responseBean.setMessage(e.getMessage());
            return ResponseEntity.status(500).body(responseBean);
        }
    }

    @PutMapping(path="/admin/group/{groupId}", produces = "application/json", consumes = "application/json")
    public ResponseEntity<?> editGroup(@RequestHeader("Authorization") String token,@PathVariable String groupId ,@RequestBody GroupRequest editGroup){
        ResponseBean responseBean = new ResponseBean();
        try {
            System.out.println(token);
            Claims claims = jwtUtil.validateRole(token, List.of("admin", "mentor","student"));
            System.out.println(claims);
            Group group = groupRepository.findById(groupId).orElseThrow(() -> new RuntimeException("Group not found"));
            group.setProjectTitle(editGroup.getProjectTitle());
            group.setProjectDescription(editGroup.getProjectDescription());
            group.setBatch(batchRepository.findById(editGroup.getBatchName()).orElseThrow(() -> new RuntimeException("Batch not found")));
            Group savedGroup = groupRepository.save(group);

            Iterable<Student> prevStudents = studentRepository.findByGroup(group);
            for (Student student : prevStudents) {
                student.setGroup(null);
            } 
            studentRepository.saveAll(prevStudents);
            Iterable<Student> students = studentRepository.findAllById(editGroup.getStudentUid());
            for (Student student : students) {
                student.setGroup(group);
            }
            studentRepository.saveAll(students);
            responseBean.setStatus(true);
            responseBean.setMessage("Group : " + groupId + " updated successfully");
            responseBean.setData(savedGroup);
            return ResponseEntity.ok(responseBean);

        }catch (JwtUtil.JwtException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ErrorResponse("JWT "+e.getMessage(), 401));
        } catch (Exception e) {
            responseBean.setStatus(false);
            responseBean.setMessage(e.getMessage());
            return ResponseEntity.status(500).body(responseBean);
        }
    }


    @DeleteMapping(path="/admin/group/{groupId}", produces = "application/json")
    public ResponseEntity<?> deleteGroup(@RequestHeader("Authorization") String token,@PathVariable String groupId){
        ResponseBean responseBean = new ResponseBean();
        try {
            System.out.println(token);
            Claims claims = jwtUtil.validateRole(token, List.of("admin", "mentor","student"));
            System.out.println(claims);
            Group group = groupRepository.findById(groupId).orElseThrow(() -> new RuntimeException("Group not found"));
            Iterable<Student> students = studentRepository.findByGroup(group);
            for (Student student : students) {
                student.setGroup(null);
            }
            studentRepository.saveAll(students);
            groupRepository.delete(group);
            responseBean.setStatus(true);
            responseBean.setMessage("Group deleted successfully");
            return ResponseEntity.ok(responseBean);
        }catch (JwtUtil.JwtException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ErrorResponse("JWT "+e.getMessage(), 401));
        } catch (Exception e) {
            responseBean.setStatus(false);
            responseBean.setMessage(e.getMessage());
            return ResponseEntity.status(500).body(responseBean);
        }
    }



    private String generateUniqueUid(){
        String uid;
        do {
            uid = "G" + (int)(Math.random() * 90000 + 1000000);
        } while (groupRepository.existsById(uid));
        return uid;
    }
}
