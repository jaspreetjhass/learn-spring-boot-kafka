package com.learnkafka.producer.config;

import lombok.Data;

@Data
public class KafkaProducerConfig {

	private boolean enabled;
	private String keySerializer;
	private String valueSerializer;
	private Integer batchSize;
	private Long lingerMs;
	private String defaultTopic;
	
}
