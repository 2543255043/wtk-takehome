package com.wtk.takehome.repository;

import com.wtk.takehome.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    // 核心查询：根据酒店ID查评论
    List<Review> findByHotelId(Long hotelId);


}