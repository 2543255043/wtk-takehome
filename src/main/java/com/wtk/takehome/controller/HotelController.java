package com.wtk.takehome.controller;

import com.wtk.takehome.entity.Hotel;
import com.wtk.takehome.service.HotelService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/hotels") // 酒店接口统一前缀
@RequiredArgsConstructor
public class HotelController {

    private final HotelService hotelService;

    // 1. 查询所有酒店：GET http://localhost:8080/api/hotels
    @GetMapping
    public ResponseEntity<List<Hotel>> getAllHotels() {
        List<Hotel> hotels = hotelService.getAllHotels();
        return ResponseEntity.ok(hotels);
    }

    // 2. 根据ID查询单个酒店：GET http://localhost:8080/api/hotels/1
    @GetMapping("/{id}")
    public ResponseEntity<Hotel> getHotelById(@PathVariable Long id) {
        return hotelService.getHotelById(id)
                .map(ResponseEntity::ok) // 找到酒店，返回200+数据
                .orElse(ResponseEntity.notFound().build()); // 没找到，返回404
    }

    // 3. 按城市查询酒店：GET http://localhost:8080/api/hotels/city/Bangkok
    @GetMapping("/city/{city}")
    public ResponseEntity<List<Hotel>> getHotelsByCity(@PathVariable String city) {
        List<Hotel> hotels = hotelService.getHotelsByCity(city);
        return ResponseEntity.ok(hotels);
    }

    // 4. 按星级查询酒店：GET http://localhost:8080/api/hotels/stars/5
    @GetMapping("/stars/{stars}")
    public ResponseEntity<List<Hotel>> getHotelsByStars(@PathVariable Integer stars) {
        List<Hotel> hotels = hotelService.getHotelsByStars(stars);
        return ResponseEntity.ok(hotels);
    }

    // 5. 分页查询酒店：GET http://localhost:8080/api/hotels/page?pageNum=0&pageSize=5
    @GetMapping("/page")
    public ResponseEntity<Page<Hotel>> getHotelsWithPage(
            @RequestParam(required = false) Integer pageNum,
            @RequestParam(required = false) Integer pageSize) {
        Page<Hotel> hotels = hotelService.getHotelsWithPage(pageNum, pageSize);
        return ResponseEntity.ok(hotels);
    }
}