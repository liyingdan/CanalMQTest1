package com.janet.mq;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import org.apache.commons.lang.SerializationUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

/**
 * @Description rabbitMQ 生产者
 * @Date 2021/5/26
 * @Author Janet
 */
@Component
public class Producer {
    private static final String EXCHANGE_NAME = "materialActivityTopicExchange";

    public static Boolean sendMessage(MQMessageActivityModel message, String routingKey) throws Exception {

        Connection connection = ConnectionUtils.getConnection();

        Channel channel = connection.createChannel();

        //定义交换机
        channel.exchangeDeclare(EXCHANGE_NAME, "topic", false);

        // 发送消息
        byte[] bytes = SerializationUtils.serialize(message);
        channel.basicPublish(EXCHANGE_NAME, routingKey, true, false, null, bytes);

        System.out.println("生产者发送消息:" + message);

        channel.close();
        connection.close();

        return true;
    }
}
