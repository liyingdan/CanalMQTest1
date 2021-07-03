package com.janet.mq;

import com.rabbitmq.client.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @Description rabbitMQ 普通消费者
 * @Date 2021/6/30
 * @Author Janet
 */
@Component
@Order(1)
public class Consumer implements CommandLineRunner {
    public static final String EXCHANGE_NAME = "materialActivityTopicExchange";  //交换机名称

    public static final String QUEUE_NAME = "materialActivityTopicQueue";  //队列名称

    public static final String DLX_EXCHANGE_NAME = "deadMaterialActivityTopicExchange"; //死信交换机

    public static final String DEX_QUEUE_NAME = "deadMaterialActivityTopicQueue"; //死信队列

    public static final String BINDING_KEY = "material.confirmed.activity";

    public static final String DEX__BINDING_KEY = "material.confirmed.activity.dead"; //死信队列绑定路由键

    @Override
    public void run(String... args) throws Exception {
        Connection connection = ConnectionUtils.getConnection();
        final Channel channel = connection.createChannel();

        // 声明死信交换机(direct 类型)以及死信队列
        channel.exchangeDeclare(DLX_EXCHANGE_NAME, "direct", true, false, null); //声明死信交换机
        channel.queueDeclare(DEX_QUEUE_NAME, true, false, false, null); //声明死信队列
        channel.queueBind(DEX_QUEUE_NAME, DLX_EXCHANGE_NAME, DEX__BINDING_KEY); //死信队列和死信交换机绑定


        //声明队列
        Map<String, Object> arguments = new HashMap<>();
        arguments.put("x-dead-letter-exchange", DLX_EXCHANGE_NAME);//设置DLX
        arguments.put("x-dead-letter-routing-key", DEX__BINDING_KEY);
        arguments.put("x-message-ttl", 60000);//设置过期时间

        // 声明普通队列 并添加 DLX
        channel.queueDeclare(QUEUE_NAME, true, false, false, arguments);

        channel.queueBind(QUEUE_NAME, EXCHANGE_NAME, BINDING_KEY); //普通队列绑定生产者的交换机

        channel.basicQos(1); //每个消费者发送确认消息之前，消息队列不发送下一个消息到消费者，一次只能处理一个消息

        boolean autoAck = false; //自动应答 false
        //定义一个消费者
        channel.basicConsume(QUEUE_NAME, autoAck, new DefaultConsumer(channel) {
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {

                String msg = new String(body, "utf-8");
                System.out.println("消费者接收到消息: " + msg );

                channel.basicAck(envelope.getDeliveryTag(), false);

            }
        });
    }
}
