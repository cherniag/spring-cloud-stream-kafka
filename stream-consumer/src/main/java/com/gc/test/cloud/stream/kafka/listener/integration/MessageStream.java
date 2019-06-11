package com.gc.test.cloud.stream.kafka.listener.integration;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.messaging.SubscribableChannel;

public interface MessageStream {
    String MESSAGE_IN = "messages_in";

    @Input(MessageStream.MESSAGE_IN)
    SubscribableChannel messages();
}
