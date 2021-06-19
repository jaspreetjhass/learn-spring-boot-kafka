package com.learnkafka.validations;

import java.util.Objects;

import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import com.learnkafka.admin.config.KafkaAdminConfig;
import com.learnkafka.consumer.config.KafkaConsumerConfig;
import com.learnkafka.exception.KafkaPropertiesNotFoundException;
import com.learnkafka.producer.config.KafkaProducerConfig;
import com.learnkafka.properties.KafkaProperties;

public class KafkaPropertiesValidation {

	protected void checkProperties(final KafkaProperties kafkaProperties, boolean checkAdminProperties,
			boolean checkConsumerProperties, boolean checkProducerProperties) {
		if (CollectionUtils.isEmpty(kafkaProperties.getBootstrapServers()))
			throw new KafkaPropertiesNotFoundException("kafka bootstrap properties are not set");

		if (checkProducerProperties) {
			if (Objects.isNull(kafkaProperties.getKafkaProducerConfig()))
				throw new KafkaPropertiesNotFoundException("kafka producer properties are not set");
			else {
				final KafkaProducerConfig kafkaProducerConfig = kafkaProperties.getKafkaProducerConfig();
				if (StringUtils.isEmpty(kafkaProducerConfig.getKeySerializer())
						|| StringUtils.isEmpty(kafkaProducerConfig.getValueSerializer())
						|| Objects.isNull(kafkaProducerConfig.getBatchSize())
						|| Objects.isNull(kafkaProducerConfig.getLingerMs()))
					throw new KafkaPropertiesNotFoundException("kafka producer properties are not set");
			}
		}

		if (checkConsumerProperties) {
			if (Objects.isNull(kafkaProperties.getKafkaConsumerConfig()))
				throw new KafkaPropertiesNotFoundException("kafka consumer properties are not set");
			else {
				final KafkaConsumerConfig kafkaConsumerConfig = kafkaProperties.getKafkaConsumerConfig();
				if (StringUtils.isEmpty(kafkaConsumerConfig.getKeyDeserializer())
						|| StringUtils.isEmpty(kafkaConsumerConfig.getValueDeserializer())
						|| Objects.isNull(kafkaConsumerConfig.getGroupId()))
					throw new KafkaPropertiesNotFoundException("kafka consumer properties are not set");
			}
		}

		if (checkAdminProperties) {
			if (Objects.isNull(kafkaProperties.getKafkaAdminConfig()))
				throw new KafkaPropertiesNotFoundException("kafka admin properties are not set");
			else {
				final KafkaAdminConfig kafkaConsumerConfig = kafkaProperties.getKafkaAdminConfig();
				if (CollectionUtils.isEmpty(kafkaConsumerConfig.getTopics()))
					throw new KafkaPropertiesNotFoundException("kafka admin properties are not set");
			}
		}

	}

}
