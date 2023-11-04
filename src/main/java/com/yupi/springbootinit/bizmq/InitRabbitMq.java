package com.yupi.springbootinit.bizmq;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

/**
 * 用于初始化rabbitmq的队列、交换机、绑定关系
 * 只需执行一次
 */
public class InitRabbitMq {
    public static void main(String[] args) {
        try {
            // 创建连接工厂
            ConnectionFactory factory = new ConnectionFactory();
            factory.setHost("123.207.214.122");
            factory.setPort(5672);
            factory.setUsername("admin");
            factory.setPassword("123456");
            // 创建连接
            Connection connection = null;

            connection = factory.newConnection();
            // 创建通道
            Channel channel = connection.createChannel();
            // 定义交换机的名称
            String exchangeName = BiMqConstant.MY_DIRECT_EXCHANGE;
            channel.exchangeDeclare(exchangeName, "direct");
            // 创建队列名称
            String queueName = BiMqConstant.MY_DIRECT_QUEUE;
            // 创建队列
            channel.queueDeclare(queueName, true, false, false, null);
            // 绑定队列和交换机
            channel.queueBind(queueName, exchangeName, BiMqConstant.MY_DIRECT_ROUTINGKEY);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
