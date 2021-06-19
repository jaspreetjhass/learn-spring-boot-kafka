package com.learnkafka.producer.config;

import java.util.HashMap;
import java.util.Map;

import org.apache.kafka.clients.producer.ProducerConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;

import com.learnkafka.properties.KafkaProperties;
import com.learnkafka.validations.KafkaPropertiesValidation;

@ConditionalOnProperty(value = "learn.kafka.kafka-producer-config.enabled", havingValue = "true", matchIfMissing = false)
@Configuration
@EnableConfigurationProperties(value = { KafkaProperties.class })
public class KafkaProducerAutoConfiguration extends KafkaPropertiesValidation {

	@Autowired
	private KafkaProperties kafkaProperties;

	@ConditionalOnMissingBean
	@Bean
	public DefaultKafkaProducerFactory<Object, Object> producer() {
		super.checkProperties(kafkaProperties, false, false, true);
		final Map<String, Object> configs = new HashMap<String, Object>();
		configs.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaProperties.getBootstrapServers());
		configs.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG,
				kafkaProperties.getKafkaProducerConfig().getKeySerializer());
		configs.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,
				kafkaProperties.getKafkaProducerConfig().getValueSerializer());
		configs.put(ProducerConfig.LINGER_MS_CONFIG, kafkaProperties.getKafkaProducerConfig().getLingerMs());
		configs.put(ProducerConfig.BATCH_SIZE_CONFIG, kafkaProperties.getKafkaProducerConfig().getBatchSize());
		final DefaultKafkaProducerFactory<Object, Object> defaultKafkaProducerFactory = new DefaultKafkaProducerFactory<>(
				configs);
		return defaultKafkaProducerFactory;
	}

	@ConditionalOnMissingBean
	@Bean
	public KafkaTemplate<?, ?> kafkaTemplate(
			final DefaultKafkaProducerFactory<Object, Object> defaultKafkaProducerFactory) {
		final KafkaTemplate<Object, Object> kafkaTemplate = new KafkaTemplate<Object, Object>(defaultKafkaProducerFactory);
		kafkaTemplate.setDefaultTopic(kafkaProperties.getKafkaProducerConfig().getDefaultTopic());
		return kafkaTemplate;
	}

}
