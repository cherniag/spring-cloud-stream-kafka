package com.gc.test.kafka.producer.context;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "kafka.producer")
@Data
public class KafkaConfig {
    private String bootstrapServers;
    private String acks;
    private String valueSerializer;
    private String keySerializer;
    private String transactionalId;
}
