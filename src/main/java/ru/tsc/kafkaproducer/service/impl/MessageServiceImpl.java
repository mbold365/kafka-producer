package ru.tsc.kafkaproducer.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.msgpack.core.MessagePackException;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import ru.tsc.kafkaproducer.dto.Message;
import ru.tsc.kafkaproducer.producer.KafkaProducer;
import ru.tsc.kafkaproducer.service.MessageService;

@Service
@RequiredArgsConstructor
@Slf4j
public class MessageServiceImpl implements MessageService {

    private final KafkaProducer producer;

    @Override
    public Mono<Message> handle(byte[] body) {
        return Mono.just(extractMessage(body))
                .doOnNext(this::process)
                .onErrorResume(ex -> Mono.error(new IllegalArgumentException()));
    }

    @Override
    public void handle(Message message) {
        process(message);
    }

    private Message extractMessage(byte[] body) {
        try {
            return new Message(body);
        } catch (MessagePackException ex) {
            log.info("Error in extract message method:\n{}", ex.getMessage());
            throw new RuntimeException();
        }
    }

    private void process(Message message) {
        log.info("Handle message: {}", message);
        producer.send(message);
    }
}
