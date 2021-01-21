package ru.tsc.kafkaproducer.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.msgpack.core.MessageBufferPacker;
import org.msgpack.core.MessagePack;
import org.msgpack.core.MessageUnpacker;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.Persistable;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;
import ru.tsc.kafkaproducer.msgpack.MsgPackSerializable;

import java.io.IOException;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Slf4j
@Table("messages")
public class Message implements MsgPackSerializable, Persistable<Long> {

    @Id
    private Long id;

    @Column
    private String message;

    public Message(byte[] body) {
        this.readData(MessagePack.newDefaultUnpacker(body));
    }

    @Override
    public void writeData(MessageBufferPacker packer) {
        try {
            packer.packLong(id);
            packer.packString(message);
            log.info("Successfully serialized message with id: {}", this.id);
        } catch (IOException ex) {
            log.info("Error while writing data to msgpack:\n{}", ex.getMessage());
        }
    }

    @Override
    public void readData(MessageUnpacker unpacker) {
        try {
            this.id = unpacker.unpackLong();
            this.message = unpacker.unpackString();
            log.info("Successfully deserialized message with id: {} and body: {}", this.id, this.message);
        } catch (IOException ex) {
            log.info("Error while reading data to msgpack:\n{}", ex.getMessage());
        }
    }

    @Override
    @Transient
    public boolean isNew() {
        return id == null;
    }
}
