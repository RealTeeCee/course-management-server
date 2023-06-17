package com.aptech.coursemanagementserver.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.aptech.coursemanagementserver.models.Notification;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {

    Optional<Notification> findById(long id);

    @Query(value = """
            SELECT n.* FROM notifications n
            WHERE n.user_to_id = :id
            """, nativeQuery = true)
    List<Notification> findByUserToId(long id);

    @Query(value = """
            SELECT n.* FROM notifications n
            WHERE n.user_to_id = :id
            AND is_delivered = 0
            """, nativeQuery = true)
    List<Notification> findByUserToIdAndDeliveredFalse(long id);

}