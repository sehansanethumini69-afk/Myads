package com.myads.dto;

import com.myads.model.AdStatus;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class AdResponse {
    private Long id;
    private String title;
    private String description;
    private String category;
    private String imageUrl;
    private String whatsappNumber;
    private AdStatus status;
    private Integer views;
    private String userName;
    private LocalDateTime createdAt;
}