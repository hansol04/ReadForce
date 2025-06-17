package com.readforce.config;

import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.BasicPolymorphicTypeValidator;
import com.fasterxml.jackson.databind.jsontype.PolymorphicTypeValidator;

@Configuration
public class CacheConfig {

	@Bean
	public CacheManager cacheManager(RedisConnectionFactory connection_factory) {
		
		// ObjectMapper 설정 추가
		PolymorphicTypeValidator validator = BasicPolymorphicTypeValidator.builder()
				.allowIfBaseType(Object.class)
				.build();
		
		ObjectMapper object_mapper = new ObjectMapper();
		
		object_mapper.activateDefaultTyping(validator, ObjectMapper.DefaultTyping.NON_FINAL);
		
		// JSON 직렬화 설정
		RedisCacheConfiguration redis_cache_configuration = RedisCacheConfiguration.defaultCacheConfig()
				.serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(new StringRedisSerializer()))
				.serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(new GenericJackson2JsonRedisSerializer(object_mapper)));
		
		return RedisCacheManager.RedisCacheManagerBuilder
				.fromConnectionFactory(connection_factory)
				.cacheDefaults(redis_cache_configuration)
				.build();
		
	}
	
}
