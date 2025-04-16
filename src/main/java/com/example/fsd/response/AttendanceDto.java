package com.example.fsd.response;

import java.util.List;

import com.example.fsd.entity.Attendance.AttendanceStatus;
import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

public class AttendanceDto {
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class AttendanceDTO {
        private String uid;
        private String date; // yyyy-MM-dd
        private AttendanceStatus status;
}
    @Data
    public static class AttendanceListDTO {
        @JsonFormat(with = JsonFormat.Feature.ACCEPT_SINGLE_VALUE_AS_ARRAY)
        private List<AttendanceDTO> attendanceList;

        public List<AttendanceDTO> getAttendanceList() {
            return attendanceList;
        }
    
        public void setAttendanceList(List<AttendanceDTO> attendanceList) {
            this.attendanceList = attendanceList;
        }

        
}
}
