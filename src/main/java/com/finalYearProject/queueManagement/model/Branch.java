package com.finalYearProject.queueManagement.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Data;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;


@Data
@Entity
public class Branch {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String bkBranchName;
    @Enumerated(EnumType.STRING)
    private LocationProvince locationProvince;

    private double latitude;
    private double longitude;
    private String address;
    private String imageUrl;

    @ManyToMany
    @JoinTable(
            name = "branch_service_mapping",
            joinColumns = @JoinColumn(name = "branch_id"),
            inverseJoinColumns = @JoinColumn(name = "service_id")
    )
    @JsonBackReference
    private Set<BranchServiceOffered> branchServices = new HashSet<>();

    // Other attributes such as address, phone number, etc.


    // Implementing equals() and hashCode() without circular references
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Branch)) return false;
        Branch branch = (Branch) o;
        return Objects.equals(id, branch.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
