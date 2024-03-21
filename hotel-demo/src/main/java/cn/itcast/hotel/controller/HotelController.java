package cn.itcast.hotel.controller;

import cn.itcast.hotel.pojo.PageResult;
import cn.itcast.hotel.pojo.RequestParams;
import cn.itcast.hotel.service.IHotelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * 功能描述
 * <p>
 * 成略在胸，良计速出
 *
 * @author SUN
 * @date 2024/03/19  23:05
 */
@RestController
@RequestMapping("/hotel")
public class HotelController {
    @Autowired
    private IHotelService hotelService;
    @PostMapping("/list")
    public PageResult search(@RequestBody RequestParams params) throws IOException {
        System.out.println(1);
        return hotelService.search(params);
    }
    @PostMapping("/filters")
    public Map<String, List<String>> dataFilter(@RequestBody RequestParams params) throws IOException {
        return hotelService.filters(params);
    }
    @GetMapping("/suggestion")
    public List<String>getSuggestions(@RequestParam("key")String prefix){
        return hotelService.getSuggestions(prefix);
    };
}
