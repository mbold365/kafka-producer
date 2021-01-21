package ru.tsc.kafkaproducer.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
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
    @Operation(summary = "Handle message: pack it to the msgpack and send to kafka consumer")
    public Mono<ResponseEntity<byte[]>> handleMessage(@RequestBody Message message) {
        return messageService.handle(message)
                .onErrorMap(ex -> new IllegalArgumentException(ex.getMessage()))
                .map(response -> ResponseEntity.ok(okResponse.toMsgPack()))
                .subscribeOn(Schedulers.boundedElastic());
    }

    @GetMapping("/messages/generators")
    @Operation(summary = "Generate messages and POST requests to /messages endpoint")
    public Mono<ResponseEntity<Message>> generateMessages() throws JsonProcessingException, InterruptedException {
        messageService.generate();
        return Mono.just(new ResponseEntity<>(HttpStatus.OK));
    }
}
