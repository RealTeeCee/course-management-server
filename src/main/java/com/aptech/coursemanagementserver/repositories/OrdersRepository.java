package com.aptech.coursemanagementserver.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.aptech.coursemanagementserver.models.Orders;

public interface OrdersRepository extends JpaRepository<Orders, Long> {

    List<Orders> findByUserId(long userId);

    Optional<Orders> findByTransactionId(String transactionId);

}
