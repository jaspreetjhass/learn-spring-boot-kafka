package com.learnkafka.properties;

import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;

import com.learnkafka.admin.config.KafkaAdminConfig;
import com.learnkafka.consumer.config.KafkaConsumerConfig;
import com.learnkafka.producer.config.KafkaProducerConfig;

import lombok.Data;

@Data
@ConfigurationProperties(prefix = "learn.kafka")
public class KafkaProperties {

	private List<String> bootstrapServers;
	private KafkaAdminConfig kafkaAdminConfig;
	private KafkaProducerConfig kafkaProducerConfig;
	private KafkaConsumerConfig kafkaConsumerConfig;
	
}
