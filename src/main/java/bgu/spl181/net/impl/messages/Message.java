package bgu.spl181.net.impl.messages;

public class Message {
    private String originalMessage;
    private String name;
    private String body;
    public Message(String message){
        this.originalMessage=message;
        String[] s = message.split(" ", 2);
        this.name=s[0];
        if(s.length==2)
            this.body=s[1];
        else
            this.body=null;
    }

    public String getOriginalMessage() {
        return originalMessage;
    }
    public String getName(){ return  name;}
    public String getBody(){ return body;}

    public Message unpackMessage(){
        if(name.equals("REGISTER"))
           return new Register(originalMessage);
        else if(name.equals("LOGIN"))
            return new Login(originalMessage);
        else if(name.equals("REQUEST"))
            return new Request(originalMessage);
       else if(name.equals("SIGNOUT"))
            return new Signout(originalMessage);
       else if(name.equals("ACK") ||name.equals("ERROR") ||name.equals("BROADCAST"))
           return this;
       return null; // if message not legal
    }

    public String  toString(){
        if(this.body==null)
            return this.name;
        return this.name+" "+this.body;
    }
}
