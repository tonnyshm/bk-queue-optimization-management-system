package com.finalYearProject.queueManagement.model;



import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Data;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Data
@Entity
public class BranchServiceOffered {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private boolean enabled = true; // All services are enabled by default



    @ManyToMany(mappedBy = "branchServices")
    @JsonBackReference
    private Set<Branch> branches = new HashSet<>();


    // Implementing equals() and hashCode() without circular references
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BranchServiceOffered)) return false;
        BranchServiceOffered that = (BranchServiceOffered) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }


}
