package com.learnkafka.rest;

import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.support.SendResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.learnkafka.models.LibraryEvent;
import com.learnkafka.producer.LibraryEventProducer;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/v1/libraryevents")
public class LibraryEventController {

	@Autowired
	private LibraryEventProducer libraryEventProducer;

	@PostMapping("scanLibraryEventAsync")
	public ResponseEntity<LibraryEvent> scanLibraryEventAsync(@RequestBody final LibraryEvent libraryEvent)
			throws JsonProcessingException {
		log.info("calling event producer");
		libraryEventProducer.sendLibraryEventAsync(libraryEvent);
		log.info("event is sent to kafka broker");
		return new ResponseEntity<LibraryEvent>(libraryEvent, HttpStatus.CREATED);
	}

	@PostMapping("scanLibraryEventSync")
	public ResponseEntity<LibraryEvent> scanLibraryEventSync(@RequestBody final LibraryEvent libraryEvent)
			throws JsonProcessingException {
		log.info("calling event producer");
		final SendResult<Integer, String> sendResult = libraryEventProducer.sendLibraryEventSync(libraryEvent);
		final ProducerRecord<Integer, String> producerRecord = sendResult.getProducerRecord();
		final RecordMetadata recordMetadata = sendResult.getRecordMetadata();
		log.info("event is sent to kafka broker with key : {}, value : {} & partition : {}.", producerRecord.key(),
				producerRecord.value(), recordMetadata.partition());
		return new ResponseEntity<LibraryEvent>(libraryEvent, HttpStatus.CREATED);
	}

	@PostMapping("scanLibraryEventAsyncWithProducerRecord")
	public ResponseEntity<LibraryEvent> scanLibraryEventAsyncWithProducerRecord(
			@RequestBody final LibraryEvent libraryEvent) throws JsonProcessingException {
		log.info("calling event producer");
		libraryEventProducer.sendLibraryEventAsyncWithProducerRecord(libraryEvent);
		log.info("event is sent to kafka broker.");
		return new ResponseEntity<LibraryEvent>(libraryEvent, HttpStatus.CREATED);
	}
	
}
