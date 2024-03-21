package cn.itcast.hotel.config;

import cn.itcast.hotel.constants.MqConstant;
import com.rabbitmq.client.AMQP;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 功能描述
 * <p>
 * 成略在胸，良计速出
 *
 * @author SUN
 * @date 2024/03/20  23:24
 */
@Configuration
public class HotelMQConfig {
    @Bean
    public TopicExchange exchange(){
        return new TopicExchange(MqConstant.HOTEL_EXCHANGE,true,false);
    }

    @Bean
    public Queue InsertQueue(){
        return new Queue(MqConstant.HOTEL_INSERT_QUEUE,true);
    }
    @Bean
    public Queue DeleteQueue(){
        return new Queue(MqConstant.HOTEL_DELETE_QUEUE,true);
    }

    @Bean
    public Binding insertQueueBinding(){
        return  BindingBuilder.bind(InsertQueue()).to(exchange()).with(MqConstant.HOTEL_INSERT_KEY);
    }
    @Bean
    public Binding deleteQueueBinding(){
        return  BindingBuilder.bind(DeleteQueue()).to(exchange()).with(MqConstant.HOTEL_DELETE_KEY);
    }
}
