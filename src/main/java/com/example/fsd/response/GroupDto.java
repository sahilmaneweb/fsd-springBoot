package com.example.fsd.response;

import java.util.List;

import com.example.fsd.entity.Mentor;
import com.example.fsd.entity.Student;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

public class GroupDto {
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class GroupRequest{
        private String projectTitle;
        private String projectDescription;
        private String batchName;
        private List<String> studentUid;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class GroupResponse{
        private String groupId;
        private String projectTitle;
        private String projectDescription;
        private String batchName;
        private List<Student> students;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class GroupResponseStudent{
        private String groupId;
        private String projectTitle;
        private String projectDescription;
        private String batchName;
        private List<Student> students;
        private List<Mentor> mentors;
    }
}
