package bgu.spl181.net.impl.messages;

public class RequestInfo extends Request{
    private String movieName;

    public RequestInfo(String message) {
        super(message);
        if(getRequestBody().equals(""))
            this.movieName=null;
        else
            this.movieName = getRequestBody().split("\"")[1];
    }

    public String getMovieName() {
        return movieName;
    }

    @Override
    public Message unpackMessage() {
        return null;
    }
}
