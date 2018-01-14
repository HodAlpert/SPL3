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
        if (nextByte == '\n') {
            return popMessage();
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

    private Message popMessage() {

        String message = new String(bytes, 0, len, StandardCharsets.UTF_8);
        len = 0;
        Message newMessage = new Message(message);
        //transform newMessage into it's final message form
        Message result = newMessage.unpackMessage();
        if(result instanceof Request || result instanceof Register)
            result = result.unpackMessage();
        return result;
    }
}
