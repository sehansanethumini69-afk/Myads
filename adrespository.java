package com.myads.repository;

import com.myads.model.Ad;
import com.myads.model.AdStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AdRepository extends JpaRepository<Ad, Long> {
    
    List<Ad> findByStatusOrderByCreatedAtDesc(AdStatus status);
    
    List<Ad> findByUserIdOrderByCreatedAtDesc(Long userId);
    
    @Query("SELECT a FROM Ad a WHERE a.status = 'APPROVED' AND " +
           "(LOWER(a.title) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
           "LOWER(a.description) LIKE LOWER(CONCAT('%', :search, '%'))) " +
           "ORDER BY a.createdAt DESC")
    List<Ad> searchApprovedAds(@Param("search") String search);
    
    List<Ad> findByStatusAndCategoryOrderByCreatedAtDesc(AdStatus status, String category);
}