package com.example.fsd.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

public class MentorDto {

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class MentorRequest{
        private String name;
        private String email;
        private String contact;
        private String gender;
        private String batchName;
        private String department;
        private String specialization;
    }
}
