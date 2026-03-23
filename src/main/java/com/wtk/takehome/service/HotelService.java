package com.wtk.takehome.service;

import com.wtk.takehome.entity.Hotel;
import com.wtk.takehome.repository.HotelRepository;
import io.micrometer.common.util.StringUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor // 替代@Autowired，更优雅
public class HotelService {

    private final HotelRepository hotelRepository;

    // 1. 查询所有酒店（基础列表）
    public List<Hotel> getAllHotels() {
        return hotelRepository.findAll();
    }

    // 2. 根据ID查询单个酒店（最常用）
    public Optional<Hotel> getHotelById(Long id) {
        // 参数校验：ID不能为空且大于0
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("酒店ID必须大于0");
        }
        return hotelRepository.findById(id);
    }

    // 3. 按城市查询酒店（比如查询曼谷的所有酒店）
    public List<Hotel> getHotelsByCity(String city) {
        if (city == null || StringUtils.isBlank(city)) {
            throw new IllegalArgumentException("城市名称不能为空");
        }
        return hotelRepository.findByCity(city);
    }

    // 4. 按星级查询酒店（比如查询5星酒店）
    public List<Hotel> getHotelsByStars(Integer stars) {
        if (stars == null || stars < 1 || stars > 5) {
            throw new IllegalArgumentException("星级必须是1-5之间的整数");
        }
        return hotelRepository.findByStars(stars);
    }

    // 5. 分页查询酒店（适合酒店数量多的场景）
    public Page<Hotel> getHotelsWithPage(Integer pageNum, Integer pageSize) {
        // 默认值：页码从0开始，每页10条
        pageNum = (pageNum == null || pageNum < 0) ? 0 : pageNum;
        pageSize = (pageSize == null || pageSize <= 0) ? 10 : pageSize;
        Pageable pageable = PageRequest.of(pageNum, pageSize);
        return hotelRepository.findAll(pageable);
    }
}