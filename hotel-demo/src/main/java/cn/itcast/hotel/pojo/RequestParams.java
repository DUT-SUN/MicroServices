package cn.itcast.hotel.pojo;

import lombok.Data;

/**
 * 功能描述
 * <p>
 * 成略在胸，良计速出
 *
 * @author SUN
 * @date 2024/03/19  22:59
 */
@Data
public class RequestParams {
    private String key;
    private Integer page;
    private Integer size;
    private String sortBy;
    private String brand;
    private String starName;
    private String city;
    private Integer minPrice;
    private Integer maxPrice;
    private String location;
}
