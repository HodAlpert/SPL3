package bgu.spl181.net.impl.protocol;

import bgu.spl181.net.impl.DataHandling.MovieRentalService;
import bgu.spl181.net.impl.messages.Message;

public class MovieRentalProtocol extends BidiProtocol<Message>{

    public MovieRentalProtocol(MovieRentalService service) {
        super(service);
    }

    @Override
    public void process(Message message) {
        String newUserName="";

        if(message.getName().equals("LOGIN"))
            newUserName = message.getBody().split(" ")[0];
        Message[] responses = ((MovieRentalService)service).handle(message,this.userName);
        for(Message response : responses){
            if(response.getName().equals("ACK") || response.getName().equals("ERROR")) {
                if(response.toString().equals("ACK login succeeded"))
                    this.userName=newUserName;
                if(response.toString().equals("ACK signout succeeded"))
                    this.userName=null;
                connections.send(this.connectionId, response);
            }

            if(response.getName().equals("BROADCAST"))
                for(Object client :((ServerConnections)connections).getMap().values())
                    ((ClientForConnections)client).getHandler().send(response);
        }
    }

}
