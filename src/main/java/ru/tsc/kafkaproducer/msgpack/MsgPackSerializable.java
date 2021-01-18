package ru.tsc.kafkaproducer.msgpack;

import org.msgpack.core.MessageBufferPacker;
import org.msgpack.core.MessageUnpacker;

import java.io.IOException;

public interface MsgPackSerializable {

    void writeData(MessageBufferPacker packer) throws IOException;

    void readData(MessageUnpacker unpacker) throws IOException;
}
