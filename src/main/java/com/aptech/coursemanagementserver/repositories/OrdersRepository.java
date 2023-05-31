package com.aptech.coursemanagementserver.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.aptech.coursemanagementserver.models.Orders;

public interface OrdersRepository extends JpaRepository<Orders, Long> {

}
