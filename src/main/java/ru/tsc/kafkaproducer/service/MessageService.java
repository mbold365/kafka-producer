package ru.tsc.kafkaproducer.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import reactor.core.publisher.Mono;
import ru.tsc.kafkaproducer.dto.Message;

public interface MessageService {

    Mono<Message> handle(byte[] body);

    Mono<Message> handle(Message message);

    Mono<Message> generate() throws JsonProcessingException, InterruptedException;
}
