package com.aptech.coursemanagementserver.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.aptech.coursemanagementserver.models.Part;

public interface PartRepository extends JpaRepository<Part, Long> {

}
