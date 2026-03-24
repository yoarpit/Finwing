package com.finwing.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import java.time.LocalDateTime;

@Entity
@Table(name = "users")
@Data
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userid;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;
    @Column(nullable =false, unique = true)
    private String username;
    
    @Column(nullable = false)
    private String mobileNo; 
    
    private String gender;
    @Column(nullable = false)
    private String currency;
    @Column(nullable = false)
    private String fullName;
    
    @Column(columnDefinition = "boolean default true")
    private Boolean isActive = true;

    @org.hibernate.annotations.CreationTimestamp
    @Column(updatable = false)
    private java.time.LocalDateTime createdAt;

    @org.hibernate.annotations.UpdateTimestamp
    private java.time.LocalDateTime updatedAt;
}