package com.aptech.coursemanagementserver.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.aptech.coursemanagementserver.models.Notification;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {

    Optional<Notification> findById(long id);

    // List<Notification> findByUserToId(long id);

    // List<Notification> findByUserToIdAndDeliveredFalse(long id);

}