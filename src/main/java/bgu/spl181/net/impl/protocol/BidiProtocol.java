package bgu.spl181.net.impl.protocol;

import bgu.spl181.net.api.DataHandler;
import bgu.spl181.net.api.bidi.BidiMessagingProtocol;
import bgu.spl181.net.api.bidi.Connections;
import bgu.spl181.net.srv.ConnectionHandler;

import java.util.concurrent.atomic.AtomicBoolean;

public abstract class BidiProtocol<T> implements BidiMessagingProtocol<T>{
    protected Connections connections;
    protected int connectionId;
    protected String userName;
    protected AtomicBoolean terminate;
    protected DataHandler service;

    public BidiProtocol(DataHandler service) {
        this.service=service;
        this.terminate = new AtomicBoolean(false);
    }

    @Override
    public void start(int connectionId, Connections<T> connections, ConnectionHandler<T> handler) {
        this.connectionId = connectionId;
        this.connections = connections;
        connections.activate(connectionId,handler);
        System.out.println("Client "+ connectionId+ ": connected");


    }

    @Override
    public abstract void process(T message);

    @Override
    public boolean shouldTerminate() {
        return terminate.get();
    }

}
