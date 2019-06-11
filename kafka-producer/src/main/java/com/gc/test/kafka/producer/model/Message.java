package com.gc.test.kafka.producer.model;

import lombok.Value;

@Value
public class Message {
    long id;
    String content;
}
