package ru.tsc.kafkaproducer.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.msgpack.core.MessageBufferPacker;
import org.msgpack.core.MessagePack;
import org.msgpack.core.MessageUnpacker;
import ru.tsc.kafkaproducer.msgpack.MsgPackSerializable;

import java.io.IOException;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Slf4j
public class Message implements MsgPackSerializable {

    private Long id;

    private String message;

    public Message(byte[] body) {
        this.readData(MessagePack.newDefaultUnpacker(body));
    }

    @Override
    public void writeData(MessageBufferPacker packer) {
        try {
            packer.packString(id.toString());
            packer.packString(message);
        } catch (IOException ex) {
            log.info("Error in write data method:\n{}", ex.getMessage());
        }
    }

    @Override
    public void readData(MessageUnpacker unpacker) {
        try {
            this.id = unpacker.unpackLong();
//            this.id = 1L;
            this.message = unpacker.unpackString();
        } catch (IOException ex) {
            log.info("Error in read data method:\n{}", ex.getMessage());
        }
    }
}
