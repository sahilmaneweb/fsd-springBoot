package com.example.fsd.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "groups")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Group {
    @Id
    private String groupId;
    @Column(nullable = false)
    private String projectTitle;
    @Column(nullable = true)
    private String projectDescription;
    @ManyToOne
    @JoinColumn(name="batch_id", referencedColumnName = "batchName")
    private Batch batch;
}
