package bgu.spl181.net.impl.BBtpc;

import bgu.spl181.net.api.DataHandler;
import bgu.spl181.net.impl.protocol.MovieRentalProtocol;
import bgu.spl181.net.impl.MovieRentalService;
import bgu.spl181.net.impl.echo.LineMessageEncoderDecoder;

public class TPCMain {
    public static void main(String[] args) {
        DataHandler service= new MovieRentalService();

        MovieRentalTPCServer server = new MovieRentalTPCServer(
                Integer.parseInt(args[0]),
                ()->new MovieRentalProtocol(service),
                LineMessageEncoderDecoder::new);
        server.serve();
    }
}


