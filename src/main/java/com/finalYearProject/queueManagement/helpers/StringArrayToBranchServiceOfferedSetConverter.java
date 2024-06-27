//package com.finalYearProject.queueManagement.helpers;
//
//import com.finalYearProject.queueManagement.model.BranchServiceOffered;
//import com.finalYearProject.queueManagement.repository.BranchServiceOfferedRepo;
//import org.springframework.core.convert.converter.Converter;
//import org.springframework.stereotype.Component;
//import java.util.HashSet;
//import java.util.Set;
//
//@Component
//public class StringArrayToBranchServiceOfferedSetConverter implements Converter<String[], Set<BranchServiceOffered>> {
//
//    private final BranchServiceOfferedRepo branchServiceOfferedRepo;
//
//    public StringArrayToBranchServiceOfferedSetConverter(BranchServiceOfferedRepo branchServiceOfferedRepo) {
//        this.branchServiceOfferedRepo = branchServiceOfferedRepo;
//    }
//
//    @Override
//    public Set<BranchServiceOffered> convert(String[] source) {
//        Set<BranchServiceOffered> result = new HashSet<>();
//        for (String id : source) {
//            branchServiceOfferedRepo.findById(Long.parseLong(id))
//                    .ifPresent(result::add);
//        }
//        return result;
//    }
//}
//
