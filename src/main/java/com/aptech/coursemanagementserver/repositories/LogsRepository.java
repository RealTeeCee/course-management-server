package com.aptech.coursemanagementserver.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.aptech.coursemanagementserver.models.Logs;

public interface LogsRepository extends JpaRepository<Logs, Long> {

}
