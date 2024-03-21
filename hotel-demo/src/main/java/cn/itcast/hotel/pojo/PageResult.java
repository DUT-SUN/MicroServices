package cn.itcast.hotel.pojo;

import lombok.Data;

import java.util.List;

/**
 * 功能描述
 * <p>
 * 成略在胸，良计速出
 *
 * @author SUN
 * @date 2024/03/19  23:02
 */
@Data
public class PageResult {
    private Long total;
    private List<HotelDoc>hotels;

    public PageResult(Long total, List<HotelDoc> hotels) {
        this.total = total;
        this.hotels = hotels;
    }
}
