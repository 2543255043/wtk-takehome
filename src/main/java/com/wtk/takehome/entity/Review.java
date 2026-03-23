package com.wtk.takehome.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "review")
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 自增ID（如果data.sql里有id就保留）
    private Long id;

    @Column(name = "hotel_id")
    private Long hotelId;

    private String reviewer; // 必须加这个字段，对应data.sql里的reviewer列

    private Integer rating;

    private String content;

    @Column(name = "created_at")
    private LocalDateTime createdAt;
}
