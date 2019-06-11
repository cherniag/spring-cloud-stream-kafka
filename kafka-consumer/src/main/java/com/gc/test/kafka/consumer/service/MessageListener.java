package com.gc.test.kafka.consumer.service;

import java.io.IOException;
import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.Collection;
import java.util.Collections;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gc.test.kafka.consumer.model.Message;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRebalanceListener;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.TopicPartition;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class MessageListener {
    private final KafkaConsumer<String, String> kafkaConsumer;
    private final ObjectMapper objectMapper = new ObjectMapper();
    @Value("${service.message.topic.name}")
    private String topicName;
    private final ScheduledThreadPoolExecutor scheduledThreadPoolExecutor = new ScheduledThreadPoolExecutor(1);

    @PostConstruct
    void init() {
        kafkaConsumer.subscribe(Collections.singletonList(topicName), new DefaultConsumerRebalanceListener());
        scheduledThreadPoolExecutor.scheduleAtFixedRate(this::readMessages, 10000, 10000, TimeUnit.MILLISECONDS);
    }

    @PreDestroy
    void destroy() {
        scheduledThreadPoolExecutor.shutdownNow();
    }

    private void readMessages() {
        try {
            ConsumerRecords<String, String> consumerRecords = kafkaConsumer.poll(Duration.of(1, ChronoUnit.SECONDS));
            Iterable<ConsumerRecord<String, String>> records = consumerRecords.records(topicName);
            for (ConsumerRecord<String, String> record : records) {
                log.info("Record key: {}, offset: {}, partition: {}, timestamp: {}", record.key(), record.offset(), record.partition(), record.timestamp());
                Message message = objectMapper.readValue(record.value(), Message.class);
                log.info("Got message: {}", message);
            }
            //kafkaConsumer.commitSync();
        } catch (IOException e) {
            log.error("Could not read message", e);
        }
    }

    private static class DefaultConsumerRebalanceListener implements ConsumerRebalanceListener {
        @Override
        public void onPartitionsRevoked(Collection<TopicPartition> partitions) {
            log.info("onPartitionsRevoked: {}", partitions);
        }

        @Override
        public void onPartitionsAssigned(Collection<TopicPartition> partitions) {
            log.info("onPartitionsAssigned: {}", partitions);
        }
    }


}
