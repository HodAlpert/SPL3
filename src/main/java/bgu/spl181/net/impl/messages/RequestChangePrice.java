package bgu.spl181.net.impl.messages;

public class RequestChangePrice extends Request{
    private String movieName;
    private int price;

    public RequestChangePrice(String message) {
        super(message);
        String[] s = getRequestBody().split("\"");
        this.movieName = s[1];
        this.price = Integer.parseInt(s[2].substring(1));
    }

    public String getMovieName() {
        return movieName;
    }

    public int getPrice() {
        return price;
    }

    @Override
    public Message unpackMessage() {
        return null;
    }
}
