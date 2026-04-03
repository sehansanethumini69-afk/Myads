package com.myads.controller;

import com.myads.dto.AdResponse;
import com.myads.service.AdService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {
    
    private final AdService adService;
    
    @GetMapping("/pending")
    public ResponseEntity<List<AdResponse>> getPendingAds() {
        return ResponseEntity.ok(adService.getPendingAds());
    }
    
    @PostMapping("/approve/{adId}")
    public ResponseEntity<?> approveAd(@PathVariable Long adId) {
        adService.approveAd(adId);
        Map<String, String> response = new HashMap<>();
        response.put("message", "Ad approved successfully");
        return ResponseEntity.ok(response);
    }
    
    @PostMapping("/reject/{adId}")
    public ResponseEntity<?> rejectAd(@PathVariable Long adId) {
        adService.rejectAd(adId);
        Map<String, String> response = new HashMap<>();
        response.put("message", "Ad rejected successfully");
        return ResponseEntity.ok(response);
    }
}