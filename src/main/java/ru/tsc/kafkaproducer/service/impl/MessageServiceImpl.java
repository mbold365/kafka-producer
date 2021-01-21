package ru.tsc.kafkaproducer.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.hazelcast.map.IMap;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.msgpack.core.MessageBufferPacker;
import org.msgpack.core.MessagePack;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import reactor.core.publisher.Mono;
import ru.tsc.kafkaproducer.dto.Message;
import ru.tsc.kafkaproducer.producer.KafkaProducer;
import ru.tsc.kafkaproducer.service.MessageService;

import java.util.Collections;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class MessageServiceImpl implements MessageService {

    private final KafkaProducer producer;
    private final RestTemplate restTemplate;
    private final ObjectWriter objectWriter;
    private final IMap<Long, byte[]> messageMap;

    @Override
    public Mono<Message> handle(Message message) {
        return Mono.just(message)
                .doOnNext(this::process)
                .onErrorResume(ex -> Mono.error(new IllegalArgumentException()));
    }

    @Override
    @Async
    public void generate() throws JsonProcessingException, InterruptedException {
        for (long i = 1; i <= 1000; i++) {
            String messageBody = UUID.randomUUID().toString();
            generatePostRequests(new Message(i, messageBody));
            Thread.sleep(5000);
        }
    }

    private void process(Message message) {
        log.info("Handle message: {}", message);
        MessageBufferPacker packer = MessagePack.newDefaultBufferPacker();
        message.writeData(packer);
        byte[] body = packer.toByteArray();
        messageMap.put(message.getId(), body);
        producer.send(message, body);
    }

    private void generatePostRequests(Message message) throws JsonProcessingException {
        final String url = "http://localhost:8080/kafka/v1/producer/messages";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        String json = objectWriter.writeValueAsString(message);
        HttpEntity<String> entity = new HttpEntity<>(json, headers);
        restTemplate.postForObject(url, entity, String.class);
    }
}
