package com.janet.mq;

import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

public class ConnectionUtils {
    /*
     * 获取 MQ 的连接
     * */
    public static Connection getConnection() throws Exception {
        //1. 定义一个连接工厂
        ConnectionFactory factory = new ConnectionFactory();

        //2. 获取服务地址
        factory.setHost("127.0.0.1");

        //3. AMQP 5672
        factory.setPort(5672);

        //4. vhost
        factory.setVirtualHost("/vhost_lyd");

        //5. 用户名
        factory.setUsername("user_lyd");

        //6. 密码
        factory.setPassword("1234");

        return factory.newConnection();
    }
}
