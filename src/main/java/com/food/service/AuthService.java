package com.food.service;

import com.food.entity.Admin;
import com.food.repository.AdminRepository;
import com.food.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class AuthService {

    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    public Map<String, Object> authenticate(String username, String password) {
        Optional<Admin> adminOpt = adminRepository.findByUsername(username);
        
        if (adminOpt.isEmpty()) {
            throw new RuntimeException("Admin not found");
        }

        Admin admin = adminOpt.get();
        
        if (!passwordEncoder.matches(password, admin.getPassword())) {
            throw new RuntimeException("Invalid credentials");
        }

        String token = jwtUtil.generateToken(username);
        
        Map<String, Object> response = new HashMap<>();
        response.put("token", token);
        response.put("type", "Bearer");
        response.put("admin", Map.of(
            "id", admin.getId(),
            "username", admin.getUsername()
        ));
        
        return response;
    }

    public Admin createAdmin(String username, String password) {
        if (adminRepository.findByUsername(username).isPresent()) {
            throw new RuntimeException("Username already exists");
        }

        Admin admin = new Admin();
        admin.setUsername(username);
        admin.setPassword(passwordEncoder.encode(password));
        
        return adminRepository.save(admin);
    }
}
