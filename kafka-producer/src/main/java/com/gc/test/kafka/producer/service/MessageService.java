package com.gc.test.kafka.producer.service;

import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicLong;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gc.test.kafka.producer.model.Message;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class MessageService {
    private final KafkaProducer<String, String> kafkaProducer;
    private final AtomicLong idSupplier = new AtomicLong();
    private final ObjectMapper objectMapper = new ObjectMapper();
    @Value("${service.message.topic.name}")
    private String topicName;

    @PostConstruct
    void init() {
//        kafkaProducer.initTransactions();
    }

    @PreDestroy
    void destroy() {
        kafkaProducer.close();
    }

    @Scheduled(fixedRate = 10000)
    @SneakyThrows
    public void sendMessage() {
        long nextId = idSupplier.getAndIncrement();
        Message message = new Message(nextId, "Content " + nextId);
        String value = objectMapper.writeValueAsString(message);
        ProducerRecord<String, String> record = new ProducerRecord<>(topicName, value);
        log.info("Send record: {}", record);
//        kafkaProducer.beginTransaction();
        Future<RecordMetadata> future = kafkaProducer.send(record);
//        kafkaProducer.commitTransaction();

        RecordMetadata metadata = future.get();
        log.info("Record metadata partition: {}, offset: {}, timestamp: {}, serializedKeySize: {}, serializedValueSize: {}",
            metadata.partition(), metadata.offset(), metadata.timestamp(), metadata.serializedKeySize(), metadata.serializedValueSize());

        logMetrics();
    }

    private void logMetrics() {
        if (idSupplier.get() % 5 == 0) {
            kafkaProducer.metrics().forEach((metricName, metricValue) -> log.info("Metric {}={}", metricName.name(), metricValue.metricValue()));
        }
    }


}
