package ru.tsc.kafkaproducer.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import reactor.core.publisher.Mono;
import ru.tsc.kafkaproducer.dto.Message;

public interface MessageService {

    Mono<Message> handle(Message message);

    void generate() throws JsonProcessingException, InterruptedException;
}
