package ru.tsc.kafkaproducer.dto;

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
@Slf4j
public class OkResponse implements MsgPackSerializable {

    private String status = "RECEIVED";

    @Override
    public void writeData(MessageBufferPacker packer) {
        try {
            packer.packString(this.status);
        } catch (IOException ex) {
            log.info("Error while writing OK response to msgpack:\n{}", ex.getMessage());
        }
    }

    @Override
    public void readData(MessageUnpacker unpacker) {
        try {
            this.status = unpacker.unpackString();
        } catch (IOException ex) {
            log.info("Error while reading OK response to msgpack:\n{}", ex.getMessage());
        }
    }

    public byte[] toMsgPack() {
        MessageBufferPacker packer = MessagePack.newDefaultBufferPacker();
        this.writeData(packer);
        return packer.toByteArray();
    }
}
