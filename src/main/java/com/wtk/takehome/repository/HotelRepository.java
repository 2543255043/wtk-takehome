package com.wtk.takehome.repository;

import com.wtk.takehome.entity.Hotel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

// JpaRepository<实体类, 主键类型>
public interface HotelRepository extends JpaRepository<Hotel, Long> {

    // 1. 按城市查询酒店
    List<Hotel> findByCity(String city);

    // 2. 按星级查询酒店
    List<Hotel> findByStars(Integer stars);

    // 3. 按国家+城市查询酒店
    List<Hotel> findByCountryAndCity(String country, String city);

    // 4. 分页查询所有酒店
    Page<Hotel> findAll(Pageable pageable);

    // 5. 按星级区间查询（比如3-5星）
    List<Hotel> findByStarsBetween(Integer minStars, Integer maxStars);
}