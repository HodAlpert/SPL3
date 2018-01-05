package bgu.spl181.net.impl.messages;

public class Login extends Message{

    private String userName;
    private String password;

    public Login(String message) {
        super(message);
        String[] s=getBody().split(" ");
        this.userName=s[0];
        this.password=s[1];
    }

    public String getUserName() {
        return userName;
    }

    public String getPassword() {
        return password;
    }

    @Override
    public Message unpackMessage() {
        return null;
    }
}
