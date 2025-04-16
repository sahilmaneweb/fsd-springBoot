package com.example.fsd.response;

import java.beans.JavaBean;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@JavaBean
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Component
@JsonInclude(JsonInclude.Include.NON_NULL) // Exclude null fields from JSON serialization
public class ResponseBean {
    private String message;
    private boolean status;
    private Object data;
}
