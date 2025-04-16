package com.example.fsd.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

public class StudentDto {

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class StudentRequest {
        private String name;
        private String email;
        private String phone;
        private String gender;
        private String address;
        private String batchName;
    }
}
