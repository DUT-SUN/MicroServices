package cn.itcast.hotel.constants;

/**
 * 功能描述
 * <p>
 * 成略在胸，良计速出
 *
 * @author SUN
 * @date 2024/03/20  23:04
 */
public class MqConstant {
    //交换级
    public static final  String HOTEL_EXCHANGE="hotel.topic";
    //队列
    public static final String HOTEL_INSERT_QUEUE="hotel.insert.queue";
    public static final String HOTEL_DELETE_QUEUE="hotel.delete.queue";
    //routing key
    public static final String HOTEL_INSERT_KEY="hotel.insert";
    public static final String HOTEL_DELETE_KEY="hotel.delete";
}
