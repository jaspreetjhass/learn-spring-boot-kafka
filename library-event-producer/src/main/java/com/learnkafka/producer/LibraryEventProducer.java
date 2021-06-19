package com.learnkafka.producer;

import java.util.concurrent.ExecutionException;

import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.learnkafka.models.LibraryEvent;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class LibraryEventProducer {

	@Autowired
	private KafkaTemplate<Integer, String> kafkaTemplate;
	@Autowired
	private ObjectMapper objectMapper;

	public void sendLibraryEventAsync(final LibraryEvent libraryEvent) throws JsonProcessingException {

		final Integer key = libraryEvent.getLibraryId();
		final String libraryEventStr = objectMapper.writeValueAsString(libraryEvent);

		final ListenableFuture<SendResult<Integer, String>> listenableFuture = kafkaTemplate.sendDefault(key,
				libraryEventStr);

		final class ListenableFutureCallbackImpl implements ListenableFutureCallback<SendResult<Integer, String>> {

			@Override
			public void onFailure(final Throwable ex) {
				log.error(String.format("message is not sent to kafka broker with reason : %s",
						ex.getLocalizedMessage()));
			}

			@Override
			public void onSuccess(final SendResult<Integer, String> result) {
				log.info("message sent succcessfully with key : {}, value : {} & partition : {}", key, libraryEventStr,
						result.getRecordMetadata().partition());
			};
		}

		listenableFuture.addCallback(new ListenableFutureCallbackImpl());
	}

	public SendResult<Integer, String> sendLibraryEventSync(final LibraryEvent libraryEvent)
			throws JsonProcessingException {

		final Integer key = libraryEvent.getLibraryId();
		final String libraryEventStr = objectMapper.writeValueAsString(libraryEvent);

		SendResult<Integer, String> sendResult = null;
		try {
			sendResult = kafkaTemplate.sendDefault(key, libraryEventStr).get();
		} catch (InterruptedException | ExecutionException e) {
			log.error(String.format("message is not sent to kafka broker with reason : %s", e.getLocalizedMessage()));
		}
		return sendResult;
	}

	public void sendLibraryEventAsyncWithProducerRecord(final LibraryEvent libraryEvent)
			throws JsonProcessingException {

		final Integer key = libraryEvent.getLibraryId();
		final String libraryEventStr = objectMapper.writeValueAsString(libraryEvent);
		final String topic = "library-events";

		final ProducerRecord<Integer, String> producerRecord = buildProducerRecord(topic, key, libraryEventStr);

		final ListenableFuture<SendResult<Integer, String>> listenableFuture = kafkaTemplate.send(producerRecord);

		listenableFuture.addCallback(new ListenableFutureCallback<SendResult<Integer, String>>() {
			@Override
			public void onFailure(final Throwable ex) {
				log.error(String.format("message is not sent to kafka broker with reason : %s",
						ex.getLocalizedMessage()));
			}

			@Override
			public void onSuccess(final SendResult<Integer, String> result) {
				log.info("message sent succcessfully with key : {}, value : {} & partition : {}", key, libraryEventStr,
						result.getRecordMetadata().partition());
			};
		});
	}

	private ProducerRecord<Integer, String> buildProducerRecord(final String topic, final Integer key,
			final String libraryEventStr) {
		return new ProducerRecord<Integer, String>(topic, key, libraryEventStr);
	}

}
