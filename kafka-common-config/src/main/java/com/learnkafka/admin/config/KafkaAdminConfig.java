package com.learnkafka.admin.config;

import java.util.List;

import com.learnkafka.model.Topic;

import lombok.Data;

@Data
public class KafkaAdminConfig {

	private boolean enabled;
	private List<Topic> topics;

}
