package com.janet.mq;

import com.rabbitmq.client.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * @Description 死信队列消费者
 * @Date 2021/7/1
 * @Author Janet
 */
@Component
@Order(2)
public class DexConsumer implements CommandLineRunner {
    public static final String DEX_QUEUE_NAME = "deadMaterialActivityTopicQueue"; //死信队列

    @Override
    public void run(String... args) throws Exception {
        Connection connection = ConnectionUtils.getConnection();
        final Channel channel = connection.createChannel();

        channel.basicQos(1);

        boolean autoAck = false; //自动应答 false
        //定义一个消费者
        channel.basicConsume(DEX_QUEUE_NAME, autoAck, new DefaultConsumer(channel){
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                String msg = new String(body, "utf-8");
                System.out.println("监听到死信队列消息: " + msg);

                channel.basicAck(envelope.getDeliveryTag(), false);

                System.out.println("消息处理完成");

            }
        });

    }
}
