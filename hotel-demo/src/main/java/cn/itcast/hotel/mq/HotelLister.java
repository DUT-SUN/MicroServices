package cn.itcast.hotel.mq;

import cn.itcast.hotel.constants.MqConstant;
import cn.itcast.hotel.service.IHotelService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 功能描述
 * <p>
 * 成略在胸，良计速出
 *
 * @author SUN
 * @date 2024/03/20  23:50
 */
@Component
public class HotelLister {
    @Autowired
    private IHotelService hotelService;
    @RabbitListener(queues = MqConstant.HOTEL_INSERT_QUEUE)
    public void InsertorUpdate(Long id){
        hotelService.insertById(id);
    }

    @RabbitListener(queues = MqConstant.HOTEL_DELETE_QUEUE)
    public void Delete(Long id){
        hotelService.deleteById(id);
    }
}
