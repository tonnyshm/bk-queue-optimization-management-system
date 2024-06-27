package com.finalYearProject.queueManagement.model;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
public class AdminLogin {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;

    private String password;

    @Enumerated(EnumType.STRING) // Assuming role is stored as a string
    private UserRole role; // Enum representing user roles: ADMIN, BANK_MANAGER, etc.


    // Additional fields for admin profile

}
