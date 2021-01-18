package ru.tsc.kafkaproducer.service;

import reactor.core.publisher.Mono;
import ru.tsc.kafkaproducer.dto.Message;

public interface MessageService {

    Mono<Message> handle(byte[] body);

    void handle(Message message);
}
