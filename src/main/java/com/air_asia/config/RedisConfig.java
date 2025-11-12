package com.air_asia.config;


import com.air_asia.schema.Cart;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * Created by Rama Gopal
 * Project Name - cart-microservice
 */

@Configuration
@EnableCaching
public class RedisConfig {

  /*  @Bean
    public RedisConnectionFactory redisConnectionFactory(){
        return new LettuceConnectionFactory();
    }*/

    //Establish the connection cart-micro to redis server
    @Bean
    public RedisTemplate<String, Cart> redisTemplate(RedisConnectionFactory connectionFactory) {
        RedisTemplate<String, Cart> template = new RedisTemplate<>();
        template.setConnectionFactory(connectionFactory);

        // Key serializer
        template.setKeySerializer(new StringRedisSerializer());

        // Value serializer (serialize Cart as JSON)
        template.setValueSerializer(new GenericJackson2JsonRedisSerializer());

        // Optional - also set hash serializers
        template.setHashKeySerializer(new StringRedisSerializer());
        template.setHashValueSerializer(new GenericJackson2JsonRedisSerializer());

        template.afterPropertiesSet();
        return template;
    }
}

