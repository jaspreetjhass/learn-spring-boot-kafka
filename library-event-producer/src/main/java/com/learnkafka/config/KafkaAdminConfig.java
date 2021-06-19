/*
 * package com.learnkafka.config;
 * 
 * import org.apache.kafka.clients.admin.NewTopic; import
 * org.springframework.beans.factory.annotation.Value; import
 * org.springframework.context.annotation.Bean; import
 * org.springframework.context.annotation.Configuration; import
 * org.springframework.kafka.config.TopicBuilder;
 * 
 * import com.learnkafka.utilities.KafkaConstant;
 * 
 * @Configuration public class KafkaAdminConfig {
 * 
 * @Value("${spring.kafka.template.default-topic}") private String topicName;
 * 
 * @Bean public NewTopic newTopic() { return
 * TopicBuilder.name(topicName).partitions(KafkaConstant.DEFAULT_PARTITION)
 * .replicas(KafkaConstant.DEFAULT_REPLICAS).build(); }
 * 
 * }
 */