package bgu.spl181.net.impl;

import bgu.spl181.net.api.bidi.BidiMessagingProtocol;
import bgu.spl181.net.api.bidi.Connections;
import bgu.spl181.net.srv.ConnectionHandler;

import java.util.concurrent.atomic.AtomicBoolean;

public class BidiProtocol<T> implements BidiMessagingProtocol<T>{
    private Connections connections;
    private int connectionId;
    private AtomicBoolean terminate = new AtomicBoolean(false);

    public BidiProtocol() {
    }

    @Override
    public void start(int connectionId, Connections<T> connections, ConnectionHandler<T> handler) {
        this.connectionId = connectionId;
        this.connections = connections;
        connections.activate(connectionId,handler);
        System.out.println("Client "+ connectionId+ ": connected");


    }

    @Override
    public void process(T message) {
        System.out.println(message);
        connections.send(this.connectionId,message);
        if (message.equals("bye")){
            connections.disconnect(connectionId);
            System.out.println("Client "+ connectionId+ ": disconnected");

        }
    }

    @Override
    public boolean shouldTerminate() {
        return terminate.get();
    }

}
