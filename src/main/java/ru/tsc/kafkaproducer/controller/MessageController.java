package ru.tsc.kafkaproducer.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
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

    @PostMapping(path = "/messages",
            consumes = MediaType.APPLICATION_OCTET_STREAM_VALUE,
            produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public Mono<ResponseEntity<byte[]>> handleMessage(@RequestBody byte[] body) {
        return messageService.handle(body)
                .onErrorMap(ex -> new IllegalArgumentException(ex.getMessage()))
                .map(response -> ResponseEntity.ok(okResponse.toMsgPack()))
                .subscribeOn(Schedulers.boundedElastic());
    }

    @PostMapping(path = "test")
    public Mono<ResponseEntity<Message>> handleMessage(@RequestBody Message message) {
        messageService.handle(message);
        return Mono.just(new ResponseEntity<>(HttpStatus.OK));
    }
}
