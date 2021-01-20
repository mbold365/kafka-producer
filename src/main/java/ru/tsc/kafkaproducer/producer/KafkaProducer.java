package ru.tsc.kafkaproducer.producer;

import ru.tsc.kafkaproducer.dto.Message;

public interface KafkaProducer {

    void send(Message message, byte[] body);
}
