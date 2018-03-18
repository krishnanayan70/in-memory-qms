package com.phonepe.assignment.config;

import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;

import com.phonepe.assignment.data.Message;

public class RedisConfig {

	@Bean
	JedisConnectionFactory jedisConnectionFactory() {
	    return new JedisConnectionFactory();
	}
	 
	@Bean
	public RedisTemplate<String, Message> redisTemplate() {
	    RedisTemplate<String, Message> template = new RedisTemplate<>();
	    template.setConnectionFactory(jedisConnectionFactory());
	    return template;
	}
	
//	@Bean
//	MessageListenerAdapter messageListener() { 
//	    return new MessageListenerAdapter(new RedisMessageSubscriber());
//	}
	
//	@Bean
//	RedisMessageListenerContainer redisContainer() {
//	    RedisMessageListenerContainer container = new RedisMessageListenerContainer(); 
//	    container.setConnectionFactory(jedisConnectionFactory()); 
//	    container.addMessageListener(messageListener(), topic()); 
//	    return container; 
//	}
	
//	@Bean
//	MessagePublisher redisPublisher() { 
//	    return new RedisMessagePublisher(redisTemplate(), topic());
//	}
	
//	@Bean
//	ChannelTopic topic() {
//	    return new ChannelTopic("messageQueue");
//	}

}