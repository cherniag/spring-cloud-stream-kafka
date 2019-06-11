package com.gc.test.kafka.producer.context;

import java.util.Properties;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

@Configuration
@EnableConfigurationProperties(KafkaConfig.class)
@EnableScheduling
public class Context {

    @Bean
    public KafkaProducer kafkaProducer(KafkaConfig kafkaConfig) {
        Properties properties = new Properties();
        properties.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaConfig.getBootstrapServers());
        properties.setProperty(ProducerConfig.ACKS_CONFIG, kafkaConfig.getAcks());
        properties.setProperty(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, kafkaConfig.getKeySerializer());
        properties.setProperty(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, kafkaConfig.getValueSerializer());
//        properties.setProperty(ProducerConfig.TRANSACTIONAL_ID_CONFIG, kafkaConfig.getTransactionalId());
        return new KafkaProducer(properties);
    }
}
