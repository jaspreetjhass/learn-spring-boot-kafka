package com.learnkafka.model;

import lombok.Data;

@Data
public class Topic {

	private String name;
	private Integer partitions;
	private Integer replicas;
	
}
