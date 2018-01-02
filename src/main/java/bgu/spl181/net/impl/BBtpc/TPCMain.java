package bgu.spl181.net.impl.BBtpc;

import bgu.spl181.net.impl.BidiProtocol;
import bgu.spl181.net.impl.echo.LineMessageEncoderDecoder;

public class TPCMain {
    public static void main(String[] args) {
        MovieRentalTPCServer<String> server = new MovieRentalTPCServer<>(
                Integer.parseInt(args[0]),
                BidiProtocol::new,
                LineMessageEncoderDecoder::new);
        server.serve();
    }
}


