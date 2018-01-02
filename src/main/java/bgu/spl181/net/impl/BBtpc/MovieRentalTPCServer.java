package bgu.spl181.net.impl.BBtpc;

import bgu.spl181.net.api.MessageEncoderDecoder;
import bgu.spl181.net.api.bidi.BidiMessagingProtocol;
import bgu.spl181.net.impl.MovieRentalService;
import bgu.spl181.net.srv.BaseServer;
import bgu.spl181.net.srv.BlockingConnectionHandler;

import java.util.function.Supplier;

public class MovieRentalTPCServer<T> extends BaseServer<T> {
    private MovieRentalService service;

    public MovieRentalTPCServer(int port, Supplier<BidiMessagingProtocol<T>> protocolFactory, Supplier<MessageEncoderDecoder<T>> encdecFactory) {
        super(port, protocolFactory, encdecFactory);
        service = new MovieRentalService();
    }

    @Override
    protected void execute(BlockingConnectionHandler handler) {
        new Thread(handler).start();
    }
}

