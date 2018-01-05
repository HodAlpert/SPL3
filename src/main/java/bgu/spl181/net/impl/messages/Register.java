package bgu.spl181.net.impl.messages;

public class Register extends Message{

    private String userName;
    private String password;
    private String dataBlocks;

    public Register(String message) {
        super(message);
        String[] s=getBody().split(" ",3);
        this.userName=s[0];
        this.password=s[1];
        if(s.length==3)
            this.dataBlocks=s[2];
        else
            this.dataBlocks="";
    }

    public String getUserName() {
        return userName;
    }

    public String getPassword() {
        return password;
    }

    public String getDataBlocks() {
        return dataBlocks;
    }

    @Override
    public Message unpackMessage() {
        if(dataBlocks.split("=")[0].equals("country"))
            return new RegisterMRS(getOriginalMessage());
        else
            return this;
    }
}
