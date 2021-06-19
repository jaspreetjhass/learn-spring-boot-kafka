package com.learnkafka.exception;

public class KafkaPropertiesNotFoundException extends RuntimeException {

	private static final long serialVersionUID = -3059429547298222080L;

	public KafkaPropertiesNotFoundException() {
	}

	public KafkaPropertiesNotFoundException(String cause) {
		super(cause);
	}

	public KafkaPropertiesNotFoundException(String cause, Throwable throwable) {
		super(cause, throwable);
	}

}
