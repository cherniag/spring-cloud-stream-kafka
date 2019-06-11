package com.gc.test.cloud.stream.kafka.listener.integration;

import lombok.Data;

@Data
public class Message {
    long id;
    String content;
}
