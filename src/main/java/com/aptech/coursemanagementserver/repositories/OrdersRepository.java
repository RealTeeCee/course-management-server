package com.aptech.coursemanagementserver.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.aptech.coursemanagementserver.dtos.OrderDto;
import com.aptech.coursemanagementserver.models.Orders;

public interface OrdersRepository extends JpaRepository<Orders, Long> {
    @Query(value = """
            SELECT TOP o.* FROM orders o
            WHERE o.user_id = :userId
            AND o.course_id = :courseId
            AND o.status = 'COMPLETED'
            ORDER BY created_at DESC
                    """, nativeQuery = true)
    OrderDto findCompletedOrderByUserIdAndCourseId(long userId, long courseId);
}
