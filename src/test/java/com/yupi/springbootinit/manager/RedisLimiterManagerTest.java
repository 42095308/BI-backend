package com.yupi.springbootinit.manager;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

@SpringBootTest
class RedisLimiterManagerTest {

    @Resource
    private RedisLimiterManager redisLimiterManager;

    @Test
    void name() {
        String userID = "123";
        for (int i = 0; i < 5; i++) {
            redisLimiterManager.doRateLimiter(userID);
            System.out.println("第" + i + "次访问");
        }
    }
}