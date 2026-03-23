package com.wtk.takehome.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "hotel")
public class Hotel {
    @Id
    private Long id;
    private String name;
    private String city;
    private String country;
    private Integer stars; // 对应 stars 列
    @Column(name = "created_at") // 映射数据库的 created_at 列
    private LocalDateTime createdAt;
}
