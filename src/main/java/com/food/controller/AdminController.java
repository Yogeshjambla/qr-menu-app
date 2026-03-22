package com.food.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.food.entity.Admin;
import com.food.entity.Category;
import com.food.entity.MenuItem;
import com.food.repository.AdminRepository;
import com.food.repository.CategoryRepository;
import com.food.repository.MenuItemRepository;
import com.food.service.AuthService;

@RestController
@RequestMapping("/api/admin")
public class AdminController {
	private final AdminRepository adminRepository;
    private final CategoryRepository categoryRepository;
    private final MenuItemRepository menuItemRepository;
    private final AuthService authService;

    @Autowired
    public AdminController(AdminRepository adminRepository, CategoryRepository categoryRepository, 
                          MenuItemRepository menuItemRepository, AuthService authService) {
        this.adminRepository = adminRepository;
        this.categoryRepository = categoryRepository;
        this.menuItemRepository = menuItemRepository;
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> credentials) {
        try {
            String username = credentials.get("username");
            String password = credentials.get("password");
            
            Map<String, Object> response = authService.authenticate(username, password);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody Map<String, String> adminData) {
        try {
            String username = adminData.get("username");
            String password = adminData.get("password");
            
            Admin admin = authService.createAdmin(username, password);
            return ResponseEntity.ok(Map.of("message", "Admin created successfully", "adminId", admin.getId()));
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }

    @GetMapping("/dashboard")
    public Map<String, Object> dashboard() {
        Map<String, Object> analytics = new HashMap<>();
        analytics.put("categoryCount", categoryRepository.count());
        analytics.put("menuItemCount", menuItemRepository.count());
        analytics.put("orderCount", adminRepository.count());
        return analytics;
    }

}
