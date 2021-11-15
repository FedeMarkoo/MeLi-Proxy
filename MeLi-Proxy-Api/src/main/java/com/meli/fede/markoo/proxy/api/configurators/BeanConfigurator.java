package com.meli.fede.markoo.proxy.api.configurators;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;

@Configuration
public class BeanConfigurator {

    @Value("${spring.redis.host}")
    private String redisServer;
    @Value("${spring.redis.port}")
    private Integer redisPort;

    @Bean
    public RedisConnectionFactory redisConnectionFactoryStream() {
        final RedisStandaloneConfiguration standaloneConfig = new RedisStandaloneConfiguration(this.redisServer, this.redisPort);
        return new LettuceConnectionFactory(standaloneConfig);
    }

    @Bean
    RedisTemplate<String, Integer> redisTemplate(final RedisConnectionFactory redisConnectionFactory) {
        final RedisTemplate<String, Integer> template = new RedisTemplate<>();
        template.setConnectionFactory(redisConnectionFactory);
        return template;
    }

}
