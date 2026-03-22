package com.food.controller;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Autowired;

import com.food.entity.Category;
import com.food.repository.CategoryRepository;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {
	 private final CategoryRepository categoryRepository;

	    @Autowired
    public CategoryController(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

	    @GetMapping
	    public List<Category> getCategories() {
	        return categoryRepository.findAll();
	    }

	    @PostMapping("/admin")
	    public Category addCategory(@RequestBody Category category) {
	        return categoryRepository.save(category);
	    }

	    @PutMapping("/admin/{id}")
	    public Category updateCategory(@PathVariable Long id, @RequestBody Category category) {
	        category.setId(id);
	        return categoryRepository.save(category);
	    }

	    @DeleteMapping("/admin/{id}")
	    public void deleteCategory(@PathVariable Long id) {
	        categoryRepository.deleteById(id);
	    }

}
