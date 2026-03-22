package com.food.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.food.entity.MenuItem;

public interface MenuItemRepository extends JpaRepository<MenuItem, Long>
{
	 List<MenuItem> findByCategoryId(Long categoryId);
	    List<MenuItem> findByNameContainingIgnoreCase(String name);
	    List<MenuItem> findByAvailableTrue();
}
