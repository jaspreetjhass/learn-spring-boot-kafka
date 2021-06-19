package com.learnkafka;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;
import java.util.HashMap;

import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.serialization.IntegerDeserializer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.test.EmbeddedKafkaBroker;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.kafka.test.utils.KafkaTestUtils;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.learnkafka.models.Book;
import com.learnkafka.models.LibraryEvent;
import com.learnkafka.producer.LibraryEventProducer;
import com.learnkafka.starter.LibraryEventProducerApplication;

@ContextConfiguration(classes = LibraryEventProducerApplication.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@EmbeddedKafka(topics = { "library-events" }, partitions = 1)
@TestPropertySource(properties = { "eureka.client.enabled=false",
		"learn.kafka.bootstrap-servers=${spring.embedded.kafka.brokers}" })
class LibraryEventProducerApplicationTests {

	@Autowired
	private EmbeddedKafkaBroker embeddedKafkaBroker;
	@Autowired
	private LibraryEventProducer libraryEventProducer;
	@Autowired
	private ObjectMapper objectMapper;
	private Consumer<Integer, String> consumer;

	@BeforeEach
	public void setup() {
		final HashMap<String, Object> configs = new HashMap<>(
				KafkaTestUtils.consumerProps("group-1", "true", embeddedKafkaBroker));
		consumer = new DefaultKafkaConsumerFactory<Integer, String>(configs, new IntegerDeserializer(),
				new StringDeserializer()).createConsumer();
		embeddedKafkaBroker.consumeFromAllEmbeddedTopics(consumer);
	}

	@Test
	@Timeout(10)
	void sendLibraryEventAsync() throws IOException {
		final LibraryEvent libraryEvent = LibraryEvent.builder()
				.book(Book.builder().bookId(1).bookAuthor("Balasubramaniam").bookName("C++").build()).build();
		libraryEventProducer.sendLibraryEventAsync(libraryEvent);
		final ConsumerRecord<Integer, String> consumerRecord = KafkaTestUtils.getSingleRecord(consumer,
				"library-events");
		final String value = consumerRecord.value();
		final LibraryEvent output = objectMapper.readValue(value.getBytes(), LibraryEvent.class);

		assertThat(output).extracting(d -> d.getBook().getBookAuthor()).isEqualTo("Balasubramaniam");
	}

	@AfterEach
	public void tearDown() {
		consumer.close();
	}

}
