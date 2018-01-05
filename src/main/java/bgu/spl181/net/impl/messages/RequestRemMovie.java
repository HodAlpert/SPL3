package bgu.spl181.net.impl.messages;

public class RequestRemMovie extends  Request{
    private String movieName;

    public RequestRemMovie(String message) {
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
