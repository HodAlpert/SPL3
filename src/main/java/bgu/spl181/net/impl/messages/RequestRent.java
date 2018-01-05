package bgu.spl181.net.impl.messages;

public class RequestRent extends Request{
    private String movieName;

    public RequestRent(String message) {
        super(message);
        String[] s = getRequestBody().split("\"");
        this.movieName = s[1];
    }

    public String getMovieName() {
        return movieName;
    }
    @Override
    public Message unpackMessage() {
        return null;
    }
}
