package bgu.spl181.net.impl.messages;

public class RequestBalanceInfo extends Request{

    public RequestBalanceInfo(String message) {
        super(message);
    }

    @Override
    public Message unpackMessage() {
        return null;
    }
}
