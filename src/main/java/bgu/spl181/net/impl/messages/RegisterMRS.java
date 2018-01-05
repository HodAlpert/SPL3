package bgu.spl181.net.impl.messages;

public class RegisterMRS extends  Register{
    private String country;

    public RegisterMRS(String message) {
        super(message);
        String[] s=getDataBlocks().split("\"");
        this.country=s[1];
    }

    public String getCountry() {
        return country;
    }

    @Override
    public Message unpackMessage() {
        return null;
    }
}
