package com.example.bankappapplication.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Entity
@Data @NoArgsConstructor @AllArgsConstructor
public class Account {
    @Id
    private String id = UUID.randomUUID().toString();
    private double balance;
    private double overDraft;
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_at", nullable = false, updatable = false)
    private Date createdAt = new Date();
    @ManyToOne @JsonIgnore
    private Customer customer;
    @OneToMany(mappedBy = "account", fetch = FetchType.LAZY)
    private List<Operation> operations;

}