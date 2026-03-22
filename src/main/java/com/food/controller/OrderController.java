package com.food.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Autowired;

import com.food.entity.Order;
import com.food.repository.OrderRepository;

@RestController
@RequestMapping("/api/orders")
public class OrderController {
	 private final OrderRepository orderRepository;

	    @Autowired
    public OrderController(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

	    @PostMapping
	    public Order placeOrder(@RequestBody Order order) {
	        order.setStatus("PENDING");
	        return orderRepository.save(order);
	    }

	    @GetMapping("/admin")
	    public List<Order> getAllOrders() {
	        return orderRepository.findAll();
	    }

}
