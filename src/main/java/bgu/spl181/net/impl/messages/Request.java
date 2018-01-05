package bgu.spl181.net.impl.messages;

public class Request extends Message{
    private String requestName;
    private String requestBody;

    public Request(String message) {
        super(message);
        String[] s=getBody().split(" ",2);
        this.requestName=s[0];
        if(s.length==2)
            this.requestBody=s[1];
        else
            this.requestBody="";
    }

    public String getRequestName() {
        return requestName;
    }

    public String getRequestBody() {
        return requestBody;
    }

    @Override
    public Message unpackMessage() {
        if(requestName.equals("balance")){
            String[] s=requestBody.split(" ");
            if(s[0].equals("info"))
                return new RequestBalanceInfo(getOriginalMessage());
            else
                return new RequestBalanceAdd(getOriginalMessage());
        }
        else if(requestName.equals("info"))
            return new RequestInfo(getOriginalMessage());
        else if(requestName.equals("rent"))
            return new RequestRent(getOriginalMessage());
        else if(requestName.equals("return"))
            return new RequestReturn(getOriginalMessage());
        else if(requestName.equals("addmovie"))
            return new RequestAddMovie(getOriginalMessage());
        else if(requestName.equals("remmovie"))
            return new RequestRemMovie(getOriginalMessage());
        else if(requestName.equals("changeprice"))
            return new RequestChangePrice(getOriginalMessage());
        else
            return null;
    }
}
