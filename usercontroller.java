package com.myads.controller;

import com.myads.model.User;
import com.myads.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class UserController {
    
    private final UserService userService;
    
    @GetMapping("/profile")
    public ResponseEntity<User> getProfile() {
        return ResponseEntity.ok(userService.getCurrentUser());
    }
    
    @PutMapping("/profile")
    public ResponseEntity<?> updateProfile(
            @RequestParam String name,
            @RequestParam String phoneNumber) {
        User updatedUser = userService.updateProfile(name, phoneNumber);
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Profile updated successfully");
        response.put("user", updatedUser);
        return ResponseEntity.ok(response);
    }
}