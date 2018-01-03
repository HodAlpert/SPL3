package bgu.spl181.net.impl.BBtpc;

import bgu.spl181.net.api.MessageEncoderDecoder;
import bgu.spl181.net.api.bidi.BidiMessagingProtocol;
import bgu.spl181.net.impl.MovieRentalService;
import bgu.spl181.net.srv.BaseServer;
import bgu.spl181.net.srv.BlockingConnectionHandler;

import java.util.function.Supplier;

public class MovieRentalTPCServer extends BaseServer<String> {
    private MovieRentalService service;

    public MovieRentalTPCServer(int port, Supplier<BidiMessagingProtocol<String>> protocolFactory, Supplier<MessageEncoderDecoder<String>> encdecFactory) {
        super(port, protocolFactory, encdecFactory);
        service = MovieRentalService.getInstance();
    }

    @Override
    protected void execute(BlockingConnectionHandler handler) {
        new Thread(handler).start();
    }
}

