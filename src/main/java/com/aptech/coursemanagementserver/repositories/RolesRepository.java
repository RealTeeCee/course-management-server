package com.aptech.coursemanagementserver.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.aptech.coursemanagementserver.models.Roles;

public interface RolesRepository extends JpaRepository<Roles, Long> {

}
