package org.example.controllers;

import io.micrometer.core.annotation.Counted;
import io.micrometer.core.annotation.Timed;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.KafkaException;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.Iterator;
import java.util.Random;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * The KafkaController class is a REST controller that manages Kafka requests for sending and receiving messages.
 * It provides methods for sending messages to Kafka and retrieving random messages from Kafka.
 */
@Tag(name = "Kafka", description = "Tutorial management Kafka requests")
@RestController
public class KafkaController {

    /**
     * The name of the Kafka topic used in the application.
     */
    @Value("${spring.kafka.template.default-topic}")
    private String topicName;

    /**
     * This variable represents a concurrent linked queue that stores messages.
     */
    private final ConcurrentLinkedQueue<String> messages = new ConcurrentLinkedQueue<>();
    /**
     * Represents a Kafka template used for sending messages to Kafka topics.
     */
    private final KafkaTemplate<Long, String> kafkaTemplate;

    /**
     * This class represents a Kafka controller that allows sending and receiving messages from Kafka.
     */
    public KafkaController(KafkaTemplate<Long, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    /**
     * Send a message to Kafka.
     *
     * @param message The message to send.
     * @return A ResponseEntity with the result of the operation.
     */
    @Timed("sendMessage")
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = {@Content(schema = @Schema(), mediaType = "application/json")})})
    @GetMapping("/api/sendMessage/{message}")
    public ResponseEntity<String> sendMessage(@PathVariable("message") String message) {
        if (message == null || message.isEmpty()) {
            return new ResponseEntity<>("Неверное сообщение", HttpStatus.BAD_REQUEST);
        }
        try {
            kafkaTemplate.send(topicName, message);
            return new ResponseEntity<>(message + " было добавлено", HttpStatus.ACCEPTED);
        } catch (KafkaException e) {
            return new ResponseEntity<>("Сообщение не может быть отправлено " + e.getMessage(),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Listens for messages from Kafka and adds them to the messages list.
     *
     * @param message The message received from Kafka.
     */
    @KafkaListener(topics = "${spring.kafka.template.default-topic}", groupId = "${spring.kafka.consumer.group-id}")
    @Counted(value = "kafka.messages.count", description = "Total number of processed Kafka messages")
    public void listen(String message) {
        messages.add(message);
    }

    /**
     * Retrieves a random message that has been sent to Kafka.
     *
     * @return A ResponseEntity with the random message as the body if messages exist in Kafka, otherwise a ResponseEntity
     * with a "No messages in Kafka" response and a HTTP status code of 404 (Not Found).
     */
    @Timed("getMessage")
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = {@Content(schema = @Schema())})})
    @GetMapping("/api/getMessage")
    public ResponseEntity<String> getRandomMessage() {
        if (messages.isEmpty()) {
            return new ResponseEntity<>("Нет сообщений в Kafka", HttpStatus.NOT_FOUND);
        }


        // Обновление до потокобезопасного кода с использованием итератора
        Iterator<String> iterator = messages.iterator();
        String randomMessage = null;
        for (int i = 0; iterator.hasNext(); i++) {
            String message = iterator.next();
            if (new Random().nextInt(i + 1) == i) randomMessage = message;
        }

        return new ResponseEntity<>(randomMessage, HttpStatus.OK);
    }
}
