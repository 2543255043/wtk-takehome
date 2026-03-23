package com.wtk.takehome.controller;

import com.wtk.takehome.entity.Review;
import com.wtk.takehome.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/reviews")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    // 🔍 评论查询接口（GET）
    // 地址：http://localhost:8080/api/reviews/hotel/1
    @GetMapping("/hotel/{hotelId}")
    public ResponseEntity<List<Review>> getReviewsByHotelId(@PathVariable Long hotelId) {
        List<Review> reviews = reviewService.getReviewsByHotelId(hotelId);
        return ResponseEntity.ok(reviews);
    }

    // ✍️ 评论新增接口（POST）
    // 地址：http://localhost:8080/api/reviews
    @PostMapping("/addReview")
    public ResponseEntity<Review> addReview(@RequestBody Review review) {
        Review savedReview = reviewService.addReview(review);
        return ResponseEntity.ok(savedReview);
    }


    // AI摘要接口：GET /api/reviews/hotel/{hotelId}/summary
    @GetMapping("/hotel/{hotelId}/summary")
    public ResponseEntity<String> getHotelReviewSummary(@PathVariable Long hotelId) {
        String summary = reviewService.getHotelReviewSummary(hotelId);
        return ResponseEntity.ok(summary);
    }
}