package com.learnkafka.consumer.config;

import java.util.HashMap;
import java.util.Map;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.kafka.listener.KafkaMessageListenerContainer;

import com.learnkafka.properties.KafkaProperties;
import com.learnkafka.validations.KafkaPropertiesValidation;

@ConditionalOnProperty(value = "learn.kafka.kafka-consumer-config.enabled", havingValue = "true", matchIfMissing = false)
@Configuration
@EnableConfigurationProperties(value = { KafkaProperties.class })
public class KafkaConsumerAutoConfiguration extends KafkaPropertiesValidation {

	@Autowired
	private KafkaProperties kafkaProperties;

	@ConditionalOnMissingBean
	@Bean
	public DefaultKafkaConsumerFactory<?, ?> defaultKafkaConsumerFactory() {
		super.checkProperties(kafkaProperties, false, true, false);
		final Map<String, Object> configs = new HashMap<String, Object>();
		configs.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaProperties.getBootstrapServers());
		configs.put(ConsumerConfig.GROUP_ID_CONFIG, kafkaProperties.getKafkaConsumerConfig().getGroupId());
		configs.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG,
				kafkaProperties.getKafkaConsumerConfig().getKeyDeserializer());
		configs.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG,
				kafkaProperties.getKafkaConsumerConfig().getValueDeserializer());
		final DefaultKafkaConsumerFactory<Object, Object> defaultKafkaConsumerFactory = new DefaultKafkaConsumerFactory<>(
				configs);
		return defaultKafkaConsumerFactory;
	}

	@ConditionalOnProperty(value = "learn.kafka.kafka-consumer-config.concurrent-listener-enabled", havingValue = "false", matchIfMissing = true)
	@Bean
	public KafkaMessageListenerContainer<?, ?> kafkaMessageListenerContainer(
			final DefaultKafkaConsumerFactory<Object, Object> defaultKafkaConsumerFactory) {
		final ContainerProperties containerProperties = new ContainerProperties(
				kafkaProperties.getKafkaConsumerConfig().getTopics().toArray(new String[] {}));
		containerProperties.setGroupId(kafkaProperties.getKafkaConsumerConfig().getGroupId());
		final KafkaMessageListenerContainer<Object, Object> kafkaMessageListenerContainer = new KafkaMessageListenerContainer<>(
				defaultKafkaConsumerFactory, containerProperties);
		return kafkaMessageListenerContainer;
	}

	@ConditionalOnProperty(value = "learn.kafka.kafka-consumer-config.concurrent-listener-enabled", havingValue = "true", matchIfMissing = true)
	@Bean
	public ConcurrentKafkaListenerContainerFactory<?, ?> concurrentKafkaListenerContainerFactory(
			final DefaultKafkaConsumerFactory<Object, Object> defaultKafkaConsumerFactory) {
		final ConcurrentKafkaListenerContainerFactory<Object, Object> concurrentKafkaListenerContainerFactory = new ConcurrentKafkaListenerContainerFactory<>();
		concurrentKafkaListenerContainerFactory
				.setAutoStartup(kafkaProperties.getKafkaConsumerConfig().isAutoStartupEnabled());
		concurrentKafkaListenerContainerFactory
				.setConcurrency(kafkaProperties.getKafkaConsumerConfig().getConcurrency());
		concurrentKafkaListenerContainerFactory.setConsumerFactory(defaultKafkaConsumerFactory);
		return concurrentKafkaListenerContainerFactory;
	}

}
