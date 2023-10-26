package com.yupi.springbootinit.config;

import lombok.Data;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;

/**
 * Redis 配置
 *
 */
@Configuration
@ConfigurationProperties(prefix = "spring.redis")
@Data
public class RedissonConfig {

    private Integer database;

    private String host;

    private String port;

    private String password;

    @Bean
    public RedissonClient getRedissonClient() throws IOException {
        // 1. Create config object
        Config config = new Config();
        config.useSingleServer()
                //.setAddress("redis://" + host + ":" + port)
                .setAddress("redis://123.207.214.122:6379")
                .setPassword(password)
                .setDatabase(database);
        RedissonClient redisson = Redisson.create(config);
        return redisson;
    }
}
