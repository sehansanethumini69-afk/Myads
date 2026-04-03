package com.myads.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class AdRequest {
    
    @NotBlank(message = "Title is required")
    private String title;
    
    @NotBlank(message = "Description is required")
    private String description;
    
    @NotBlank(message = "Category is required")
    @Pattern(regexp = "PERSONAL|PRODUCT", message = "Category must be PERSONAL or PRODUCT")
    private String category;
    
    @NotBlank(message = "WhatsApp number is required")
    private String whatsappNumber;
    
    private MultipartFile image;
}