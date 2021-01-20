package ru.tsc.kafkaproducer.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import ru.tsc.kafkaproducer.dto.Message;
import ru.tsc.kafkaproducer.dto.OkResponse;
import ru.tsc.kafkaproducer.service.MessageService;

@RestController
@RequestMapping("kafka/v1/producer")
@RequiredArgsConstructor
@Slf4j
public class MessageController {

    private final MessageService messageService;
    private final OkResponse okResponse = new OkResponse();

    @PostMapping("/messages")
    public Mono<ResponseEntity<byte[]>> handleMessage(@RequestBody Message message) {
        return messageService.handle(message)
                .onErrorMap(ex -> new IllegalArgumentException(ex.getMessage()))
                .map(response -> ResponseEntity.ok(okResponse.toMsgPack()))
                .subscribeOn(Schedulers.boundedElastic());
    }

    @PostMapping(path = "/messages/msgpack",
            consumes = MediaType.APPLICATION_OCTET_STREAM_VALUE,
            produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public Mono<ResponseEntity<byte[]>> handleMessage(@RequestBody byte[] body) {
        return messageService.handle(body)
                .onErrorMap(ex -> new IllegalArgumentException(ex.getMessage()))
                .map(response -> ResponseEntity.ok(okResponse.toMsgPack()))
                .subscribeOn(Schedulers.boundedElastic());
    }

    @GetMapping("/messages/generators")
    public Mono<ResponseEntity<Message>> generateMessages() throws JsonProcessingException, InterruptedException {
        messageService.generate();
        return Mono.just(new ResponseEntity<>(HttpStatus.OK));
    }
}
