package ru.tsc.kafkaproducer.producer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.msgpack.core.MessageBufferPacker;
import org.msgpack.core.MessagePack;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.reactive.ReactiveKafkaProducerTemplate;
import org.springframework.stereotype.Service;
import ru.tsc.kafkaproducer.dto.Message;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class KafkaProducerImpl implements KafkaProducer {

    @Value("${spring.kafka.topic}")
    private String topic;
    private final ReactiveKafkaProducerTemplate<Long, byte[]> kafkaProducerTemplate;

    @Override
    public void send(Message message) {
        MessageBufferPacker packer = MessagePack.newDefaultBufferPacker();
        message.writeData(packer);
        kafkaProducerTemplate.send(topic, packer.toByteArray())
        .subscribe(senderResult -> {
            RecordMetadata metadata =senderResult.recordMetadata();
            log.info("Send message {} with id {} to topic {}, partition {}, offset {}, timestamp {}",
                    packer.toByteArray(), message.getId(), metadata.topic(), metadata.partition(), metadata.offset(), LocalDateTime.now());
        });
    }
}
