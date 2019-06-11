package com.gc.test.kafka.consumer.context;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "kafka.consumer")
@Data
public class KafkaConfig {
    private String bootstrapServers;
    private String valueDeserializer;
    private String keyDeserializer;
    private String groupId;
    private boolean enableAutoCommit;
    private long autoCommitIntervalMs;
    private long sessionTimeoutMs;
}
