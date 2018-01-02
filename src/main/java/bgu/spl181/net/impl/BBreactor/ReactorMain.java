package bgu.spl181.net.impl.BBreactor;

import bgu.spl181.net.impl.BidiProtocol;
import bgu.spl181.net.impl.echo.LineMessageEncoderDecoder;
import bgu.spl181.net.srv.Reactor;

public class ReactorMain {
    public static void main(String[] args) {
               Reactor<String> server = new Reactor<>(
                10,//num of threads
                       Integer.parseInt(args[0]),//the port
                BidiProtocol<String>::new, //protocol factory
                LineMessageEncoderDecoder::new); // encdec factory
        server.serve();
    }
}


