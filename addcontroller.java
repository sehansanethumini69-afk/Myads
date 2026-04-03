package com.myads.controller;

import com.myads.dto.AdRequest;
import com.myads.dto.AdResponse;
import com.myads.service.AdService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/ads")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class AdController {
    
    private final AdService adService;
    
    @PostMapping
    public ResponseEntity<AdResponse> createAd(
            @RequestParam("title") String title,
            @RequestParam("description") String description,
            @RequestParam("category") String category,
            @RequestParam("whatsappNumber") String whatsappNumber,
            @RequestParam(value = "image", required = false) MultipartFile image) {
        
        AdRequest request = new AdRequest();
        request.setTitle(title);
        request.setDescription(description);
        request.setCategory(category);
        request.setWhatsappNumber(whatsappNumber);
        request.setImage(image);
        
        return ResponseEntity.ok(adService.createAd(request));
    }
    
    @GetMapping
    public ResponseEntity<List<AdResponse>> getApprovedAds() {
        return ResponseEntity.ok(adService.getApprovedAds());
    }
    
    @GetMapping("/search")
    public ResponseEntity<List<AdResponse>> searchAds(@RequestParam String query) {
        return ResponseEntity.ok(adService.searchAds(query));
    }
    
    @GetMapping("/my-ads")
    public ResponseEntity<List<AdResponse>> getMyAds() {
        return ResponseEntity.ok(adService.getMyAds());
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<AdResponse> getAdById(@PathVariable Long id) {
        return ResponseEntity.ok(adService.getAdById(id));
    }
}