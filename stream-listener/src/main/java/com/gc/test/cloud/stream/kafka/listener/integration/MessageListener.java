package com.gc.test.cloud.stream.kafka.listener.integration;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.Input;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.cloud.stream.messaging.Source;
import reactor.core.publisher.Flux;

@Slf4j
@EnableBinding({MessageStream.class, Source.class})
public class MessageListener {

//    @StreamListener
//    @Output(Processor.OUTPUT)
//    public Flux<Message> onNextMessage(@Input(MessageStream.MESSAGE_IN) Flux<Message> messageFlux) {
//        return messageFlux.doOnNext(message -> log.info("Got new message {}", message));
//    }

    @StreamListener
    public void onNextMessage(@Input(MessageStream.MESSAGE_IN) Flux<Message> messageFlux) {
        messageFlux
            //.flatMap(e -> Mono.error(new RuntimeException("444")))
            .doOnError(e -> log.error("Error during processing: ", e))
            .retry()
            .subscribe(
                message -> log.info("Got new message {}", message),
                e -> log.error("Error consumer", e),
                () -> log.warn("Complete consumer")
            );
    }

}
