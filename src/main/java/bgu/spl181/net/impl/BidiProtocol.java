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

    }

    @Override
    public void process(T message) {
        System.out.println(message);
        connections.send(this.connectionId,message + "hahaha");
    }

    @Override
    public boolean shouldTerminate() {
        return terminate.get();
    }

}
