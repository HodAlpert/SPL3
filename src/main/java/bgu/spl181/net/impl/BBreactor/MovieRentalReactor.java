package bgu.spl181.net.impl.BBreactor;

import bgu.spl181.net.api.MessageEncoderDecoder;
import bgu.spl181.net.api.bidi.BidiMessagingProtocol;
import bgu.spl181.net.srv.Reactor;

import java.util.function.Supplier;

public class MovieRentalReactor extends Reactor<String> {
    public MovieRentalReactor(int numThreads, int port, Supplier<BidiMessagingProtocol<String>> protocolFactory, Supplier<MessageEncoderDecoder<String>> readerFactory) {
        super(numThreads, port, protocolFactory, readerFactory);
    }


}
