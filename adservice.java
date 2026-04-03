package com.myads.service;

import com.myads.dto.AdRequest;
import com.myads.dto.AdResponse;
import com.myads.model.Ad;
import com.myads.model.AdStatus;
import com.myads.model.User;
import com.myads.repository.AdRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdService {
    
    private final AdRepository adRepository;
    private final UserService userService;
    private final FileStorageService fileStorageService;
    
    @Transactional
    public AdResponse createAd(AdRequest request) {
        User currentUser = userService.getCurrentUser();
        
        Ad ad = new Ad();
        ad.setTitle(request.getTitle());
        ad.setDescription(request.getDescription());
        ad.setCategory(request.getCategory());
        ad.setWhatsappNumber(request.getWhatsappNumber());
        ad.setUser(currentUser);
        ad.setStatus(AdStatus.PENDING);
        ad.setViews(0);
        
        if (request.getImage() != null && !request.getImage().isEmpty()) {
            String imageUrl = fileStorageService.storeFile(request.getImage());
            ad.setImageUrl(imageUrl);
        }
        
        Ad savedAd = adRepository.save(ad);
        return mapToResponse(savedAd);
    }
    
    public List<AdResponse> getApprovedAds() {
        return adRepository.findByStatusOrderByCreatedAtDesc(AdStatus.APPROVED)
            .stream()
            .map(this::mapToResponse)
            .collect(Collectors.toList());
    }
    
    public List<AdResponse> searchAds(String query) {
        if (query == null || query.trim().isEmpty()) {
            return getApprovedAds();
        }
        return adRepository.searchApprovedAds(query)
            .stream()
            .map(this::mapToResponse)
            .collect(Collectors.toList());
    }
    
    public List<AdResponse> getMyAds() {
        User currentUser = userService.getCurrentUser();
        return adRepository.findByUserIdOrderByCreatedAtDesc(currentUser.getId())
            .stream()
            .map(this::mapToResponse)
            .collect(Collectors.toList());
    }
    
    public List<AdResponse> getPendingAds() {
        return adRepository.findByStatusOrderByCreatedAtDesc(AdStatus.PENDING)
            .stream()
            .map(this::mapToResponse)
            .collect(Collectors.toList());
    }
    
    @Transactional
    public void approveAd(Long adId) {
        Ad ad = adRepository.findById(adId)
            .orElseThrow(() -> new RuntimeException("Ad not found"));
        ad.setStatus(AdStatus.APPROVED);
        adRepository.save(ad);
    }
    
    @Transactional
    public void rejectAd(Long adId) {
        Ad ad = adRepository.findById(adId)
            .orElseThrow(() -> new RuntimeException("Ad not found"));
        ad.setStatus(AdStatus.REJECTED);
        adRepository.save(ad);
    }
    
    @Transactional
    public AdResponse getAdById(Long id) {
        Ad ad = adRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Ad not found"));
        
        if (ad.getStatus() == AdStatus.APPROVED) {
            ad.setViews(ad.getViews() + 1);
            adRepository.save(ad);
        }
        
        return mapToResponse(ad);
    }
    
    private AdResponse mapToResponse(Ad ad) {
        return AdResponse.builder()
            .id(ad.getId())
            .title(ad.getTitle())
            .description(ad.getDescription())
            .category(ad.getCategory())
            .imageUrl(ad.getImageUrl())
            .whatsappNumber(ad.getWhatsappNumber())
            .status(ad.getStatus())
            .views(ad.getViews())
            .userName(ad.getUser().getName())
            .createdAt(ad.getCreatedAt())
            .build();
    }
}