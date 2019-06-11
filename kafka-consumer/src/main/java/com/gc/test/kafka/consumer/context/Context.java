package com.gc.test.kafka.consumer.context;

import java.util.Properties;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

@Configuration
@EnableConfigurationProperties(KafkaConfig.class)
@EnableScheduling
public class Context {

    @Bean
    public KafkaConsumer kafkaConsumer(KafkaConfig kafkaConfig) {
        Properties properties = new Properties();
        properties.setProperty(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaConfig.getBootstrapServers());
        properties.setProperty(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, kafkaConfig.getKeyDeserializer());
        properties.setProperty(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, kafkaConfig.getValueDeserializer());
        properties.setProperty(ConsumerConfig.GROUP_ID_CONFIG, kafkaConfig.getGroupId());
        properties.setProperty(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, String.valueOf(kafkaConfig.isEnableAutoCommit()));
        properties.setProperty(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG, String.valueOf(kafkaConfig.getAutoCommitIntervalMs()));
        properties.setProperty(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG, String.valueOf(kafkaConfig.getSessionTimeoutMs()));
        return new KafkaConsumer(properties);
    }
}
