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

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
public class Student {
    @Id
    private String uid;
    @Column(nullable = false)
    private String name;
    @Column(nullable = false, unique = true)
    private String email;
    @Column(nullable = false, unique = true)
    private String phone;
    private String address;
    private String gender;
    @ManyToOne
    @JoinColumn(name="batch_id", referencedColumnName = "batchName", nullable = true)
    private Batch batch;
    @ManyToOne
    @JoinColumn(name = "group_id", referencedColumnName = "groupId", nullable = true)
    private Group group;

}

