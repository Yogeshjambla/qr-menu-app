package com.food.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.food.entity.Order;

public interface OrderRepository extends JpaRepository<Order, Long>
{

}
