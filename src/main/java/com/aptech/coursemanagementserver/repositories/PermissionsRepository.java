package com.aptech.coursemanagementserver.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.aptech.coursemanagementserver.models.Permissions;

public interface PermissionsRepository extends JpaRepository<Permissions, Long> {

}
