package com.example.fsd.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Mentor {
    @Id
    private String mentorId;
    @Column(nullable = false)
    private String name;
    @Column(nullable = false, unique = true)
    private String email;
    @Column(nullable = false, unique = true)
    private String contact;
    private String gender;
    @ManyToOne
    @JoinColumn(name="batch_id", referencedColumnName = "batchName")
    private Batch batch;
    @Column(nullable = false)
    private String department;
    private String specialization;
}
