package com.food.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.food.entity.MenuItem;
import com.food.repository.MenuItemRepository;
import com.food.service.FileUploadService;

@RestController
@RequestMapping("/api/menu")
public class MenuController {
	private final MenuItemRepository menuItemRepository;
	private final FileUploadService fileUploadService;

	@Autowired
    public MenuController(MenuItemRepository menuItemRepository, FileUploadService fileUploadService) {
        this.menuItemRepository = menuItemRepository;
        this.fileUploadService = fileUploadService;
    }

    @GetMapping
    public List<MenuItem> getMenu() {
        return menuItemRepository.findAll();
    }

    @GetMapping("/available")
    public List<MenuItem> getAvailableMenuItems() {
        return menuItemRepository.findByAvailableTrue();
    }

    @GetMapping("/category/{categoryId}")
    public List<MenuItem> getMenuByCategory(@PathVariable Long categoryId) {
        return menuItemRepository.findByCategoryId(categoryId);
    }

    @GetMapping("/search")
    public List<MenuItem> searchMenu(@RequestParam String name) {
        return menuItemRepository.findByNameContainingIgnoreCase(name);
    }

    @PostMapping("/admin")
    public ResponseEntity<MenuItem> addMenuItem(@RequestBody MenuItem item) {
        MenuItem savedItem = menuItemRepository.save(item);
        return ResponseEntity.ok(savedItem);
    }

    @PutMapping("/admin/{id}")
    public ResponseEntity<MenuItem> updateMenuItem(@PathVariable Long id, @RequestBody MenuItem item) {
        item.setId(id);
        MenuItem updatedItem = menuItemRepository.save(item);
        return ResponseEntity.ok(updatedItem);
    }

    @PatchMapping("/admin/{id}/availability")
    public ResponseEntity<MenuItem> toggleAvailability(@PathVariable Long id, @RequestParam boolean status) {
        MenuItem item = menuItemRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Menu item not found"));
        item.setAvailable(status);
        MenuItem updatedItem = menuItemRepository.save(item);
        return ResponseEntity.ok(updatedItem);
    }

    @PostMapping("/admin/{id}/image")
    public ResponseEntity<Map<String, String>> uploadImage(@PathVariable Long id, @RequestParam("file") MultipartFile file) {
        try {
            MenuItem item = menuItemRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Menu item not found"));
            
            String imageUrl = fileUploadService.uploadFile(file, "menu-items");
            item.setImageUrl(imageUrl);
            menuItemRepository.save(item);
            
            Map<String, String> response = new HashMap<>();
            response.put("imageUrl", imageUrl);
            response.put("message", "Image uploaded successfully");
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }

    @DeleteMapping("/admin/{id}")
    public ResponseEntity<Void> deleteMenuItem(@PathVariable Long id) {
        menuItemRepository.deleteById(id);
        return ResponseEntity.ok().build();
    }
}
