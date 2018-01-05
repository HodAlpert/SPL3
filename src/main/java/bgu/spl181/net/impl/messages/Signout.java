package bgu.spl181.net.impl.messages;

public class Signout extends  Message{


    public Signout(String message) {
        super(message);
    }

    @Override
    public Message unpackMessage() {
        return null;
    }
}
