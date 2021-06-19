package com.learnkafka.admin.config;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.admin.KafkaAdminClient;
import org.apache.kafka.clients.admin.ListTopicsResult;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

import com.learnkafka.model.Topic;
import com.learnkafka.properties.KafkaProperties;
import com.learnkafka.validations.KafkaPropertiesValidation;

@ConditionalOnProperty(value = "learn.kafka.kafka-admin-config.enabled", havingValue = "true", matchIfMissing = false)
@Configuration
public class KafkaAdminAutoConfiguration extends KafkaPropertiesValidation {

	@Autowired
	private KafkaProperties kafkaProperties;

	@Bean
	public AdminClient kafkaAdminClient() {
		super.checkProperties(kafkaProperties, true, false, false);
		final Map<String, Object> configs = new HashMap<String, Object>();
		configs.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaProperties.getBootstrapServers());
		return KafkaAdminClient.create(configs);
	}

	@PostConstruct
	public void initialKafkaBroker() throws InterruptedException, ExecutionException {
		final AdminClient kafkaAdminClient = kafkaAdminClient();
		final ListTopicsResult listTopics = kafkaAdminClient.listTopics();
		final Set<String> existingTopicList = listTopics.names().get();

		//topicName -> topic 
		final Map<String, Topic> createTopicMap = kafkaProperties.getKafkaAdminConfig().getTopics().stream()
				.collect(Collectors.toMap(Topic::getName, Function.identity()));

		//topicNameKeySet
		final Set<String> createTopicNameSet = createTopicMap.keySet();

		final List<NewTopic> newTopicList = new ArrayList<NewTopic>();

		createTopicNameSet.forEach(topic -> {
			if (!existingTopicList.contains(topic)) {
				final NewTopic newTopic = TopicBuilder.name(topic).partitions(createTopicMap.get(topic).getPartitions())
						.replicas(createTopicMap.get(topic).getReplicas()).build();
				newTopicList.add(newTopic);
			}
		});

		kafkaAdminClient.createTopics(newTopicList).all().get();
		
	}

	//@PreDestroy
	public void clearKafkaBroker() {
		final AdminClient kafkaAdminClient = kafkaAdminClient();
		kafkaAdminClient.deleteTopics(kafkaProperties.getKafkaAdminConfig().getTopics().stream().map(Topic::getName)
				.collect(Collectors.toList()));
	}

}
