package com.wtk.takehome.service;

import com.wtk.takehome.entity.Review;
import com.wtk.takehome.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;

    private final com.wtk.takehome.config.AiSummaryUtil aiSummaryUtil; // 注入AI工具类


    // 评论查询：根据酒店ID查所有评论
    public List<Review> getReviewsByHotelId(Long hotelId) {
        if (hotelId == null || hotelId <= 0) {
            throw new IllegalArgumentException("酒店ID必须大于0");
        }
        return reviewRepository.findByHotelId(hotelId);
    }

    // 评论新增：提交新评论
    public Review addReview(Review review) {
        // 参数校验
        if (review.getHotelId() == null || review.getHotelId() <= 0) {
            throw new IllegalArgumentException("关联酒店ID不能为空且大于0");
        }
        if (review.getRating() == null || review.getRating() < 1 || review.getRating() > 5) {
            throw new IllegalArgumentException("评分必须是1-5之间的整数");
        }
        if (review.getContent() == null || StringUtils.isEmpty(review.getContent())) {
            throw new IllegalArgumentException("评论内容不能为空");
        }
        // 自动填充评论时间（前端不用传）
        review.setCreatedAt(LocalDateTime.now());
        // 保存到数据库
        return reviewRepository.save(review);
    }


    // 新增：生成酒店评论AI摘要
    public String getHotelReviewSummary(Long hotelId) {
        List<Review> reviews = getReviewsByHotelId(hotelId);
        return aiSummaryUtil.generateHotelReviewSummary(hotelId, reviews);
    }

}