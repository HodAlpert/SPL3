package bgu.spl181.net.impl.encoder;

import bgu.spl181.net.api.MessageEncoderDecoder;
import bgu.spl181.net.impl.messages.Message;
import bgu.spl181.net.impl.messages.Register;
import bgu.spl181.net.impl.messages.Request;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public class LineMessageEncoderDecoder implements MessageEncoderDecoder<Message> {

    private byte[] bytes = new byte[1 << 10]; //start with 1k
    private int len = 0;

    @Override
    public Message decodeNextByte(byte nextByte) {
        //notice that the top 128 ascii characters have the same representation as their utf-8 counterparts
        //this allow us to do the following comparison
        if (nextByte == '\n') {
            return popString();
        }

        pushByte(nextByte);
        return null; //not a line yet
    }

    @Override
    public byte[] encode(Message message) {
        return (message.toString() + "\n").getBytes(); //uses utf8 by default
    }

    private void pushByte(byte nextByte) {
        if (len >= bytes.length) {
            bytes = Arrays.copyOf(bytes, len * 2);
        }

        bytes[len++] = nextByte;
    }

    private Message popString() {
        //notice that we explicitly requesting that the string will be decoded from UTF-8
        //this is not actually required as it is the default encoding in java.
        String message = new String(bytes, 0, len, StandardCharsets.UTF_8);
        len = 0;
        Message newMessage = new Message(message);
        Message result = newMessage.unpackMessage();
        if(result instanceof Request || result instanceof Register)
            result = result.unpackMessage();
        return result;
    }
}
