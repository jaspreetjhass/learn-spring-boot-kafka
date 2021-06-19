package com.learnkafka.consumer.config;

import java.util.List;

import lombok.Data;

@Data
public class KafkaConsumerConfig {

	private boolean enabled;
	private String keyDeserializer;
	private String valueDeserializer;
	private String groupId;
	private List<String> topics;
	private boolean concurrentListenerEnabled;
	private boolean autoStartupEnabled;
	private Integer concurrency;
	
}
