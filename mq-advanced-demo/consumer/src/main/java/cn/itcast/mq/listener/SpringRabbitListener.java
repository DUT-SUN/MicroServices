package cn.itcast.mq.listener;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class SpringRabbitListener {

    @RabbitListener(queues = "simple.queue")
    public void listenSimpleQueue(String msg) {
        System.out.println("消费者接收到simple.queue的消息：【" + msg + "】");
//        System.out.println(1/0);
        log.info("消费者处理消息成功");
    }
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(name="dl.direct",durable = "true"),
            exchange = @Exchange("dl.exchange"),
            key = "dl"
    ))
    public void listenDLQueue(String msg){
        log.info("接收到dl.queue的延迟消息：{}",msg);
    }
}
