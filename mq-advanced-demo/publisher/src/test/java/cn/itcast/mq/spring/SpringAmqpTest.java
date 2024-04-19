package cn.itcast.mq.spring;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageBuilder;
import org.springframework.amqp.core.MessageDeliveryMode;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.nio.charset.StandardCharsets;
import java.util.UUID;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class SpringAmqpTest {
    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Test
    public void testSendMessage2SimpleQueue() throws InterruptedException {

        String message = "hello, spring amqp666!";
         Message message1= MessageBuilder.withBody(message.getBytes(StandardCharsets.UTF_8)).setDeliveryMode(MessageDeliveryMode.PERSISTENT).build();
        CorrelationData correlationData=new CorrelationData(UUID.randomUUID().toString());
        //准备ConfirmCallback
         correlationData.getFuture().addCallback(result -> {
             if(result.isAck()){
                 log.debug("消息成功投递到交换机！，消息ID为{}",correlationData.getId());
             }else{
                 System.out.println(result.isAck()+result.toString());
                 log.error("消息投递到交换机失败！消息ID为{}",correlationData.getId());
             }
         },ex -> {
             log.error("消息发送失败！",ex);
         });
        rabbitTemplate.convertAndSend("camq.topic","simple.test", message1,correlationData);
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
    @Test
    public void testTTLMessage(){
        String message = "hello, ttl!";
        Message message1= MessageBuilder.withBody(message.getBytes(StandardCharsets.UTF_8)).setDeliveryMode(MessageDeliveryMode.PERSISTENT).build();
        rabbitTemplate.convertAndSend("ttl.direct","ttl",message1);
        log.info("消息被成功发送!");

    }
}
