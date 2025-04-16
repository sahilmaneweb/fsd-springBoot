package com.example.fsd.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

public class UserDto {

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class UserLogin{
        private String username;
        private String password;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class UpdateUser{
        private String username;
        private String password;
        private String newPassword;
    }
}
