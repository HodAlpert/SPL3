package bgu.spl181.net.impl.messages;

public class RequestBalanceAdd extends Request{

    private int amount;

    public RequestBalanceAdd(String message) {
        super(message);
        String[] s = getRequestBody().split(" ");
        this.amount=Integer.parseInt(s[1]);
    }

    public int getAmount() {
        return amount;
    }

    @Override
    public Message unpackMessage() {
        return null;
    }
}
