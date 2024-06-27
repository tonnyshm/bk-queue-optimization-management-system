package com.finalYearProject.queueManagement.model;


import lombok.Data;

import javax.persistence.*;
import java.util.HashMap;
import java.util.Map;

@Data
@Entity
public class BankManagerLogin {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String username;
    private String password;

    @ManyToOne
    @JoinColumn(name = "branch_id", referencedColumnName = "id")
    private Branch branch;

    @Enumerated(EnumType.STRING) // Assuming role is stored as a string
    private UserRole role; // Enum representing user roles: ADMIN, BANK_MANAGER, etc.

    @ElementCollection
    @CollectionTable(name = "manager_service_status", joinColumns = @JoinColumn(name = "manager_id"))
    @MapKeyJoinColumn(name = "service_id")
    @Column(name = "enabled")
    private Map<BranchServiceOffered, Boolean> serviceStatus = new HashMap<>(); //entity to indicate whether a particular service is disabled for a specific branch by that manager, Add a map to store the service status for each branch in the BankManagerLogin entity.

}
