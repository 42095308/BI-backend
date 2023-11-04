package com.yupi.springbootinit.bizmq;

import org.junit.jupiter.api.Test;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;


@SpringBootTest
@Component
class MyMessageProducerTest {
    @Resource
    private MyMessageProducer myMessageProducer;

    @Test
    public void sendMsg() {
        myMessageProducer.sendMsg("hello world");
    }
}