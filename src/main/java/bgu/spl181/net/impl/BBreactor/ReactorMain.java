package bgu.spl181.net.impl.BBreactor;

import bgu.spl181.net.api.DataHandler;
import bgu.spl181.net.impl.messages.Message;
import bgu.spl181.net.impl.protocol.MovieRentalProtocol;
import bgu.spl181.net.impl.DataHandling.MovieRentalService;
import bgu.spl181.net.impl.encoder.LineMessageEncoderDecoder;
import bgu.spl181.net.srv.Reactor;

public class ReactorMain {

    public static void main(String[] args) {
        System.out.println("reactor");
        DataHandler<Message> service= new MovieRentalService();
               Reactor<Message> server = new Reactor<>(
                10,//num of threads
                       Integer.parseInt(args[0]),//the port
                       ()->new MovieRentalProtocol((MovieRentalService) service), //protocol factory
                LineMessageEncoderDecoder::new);
        server.serve();
    }
}


