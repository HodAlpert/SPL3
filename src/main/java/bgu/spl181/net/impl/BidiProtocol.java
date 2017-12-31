package bgu.spl181.net.impl;

import bgu.spl181.net.api.bidi.BidiMessagingProtocol;
import bgu.spl181.net.api.bidi.Connections;

import java.util.concurrent.atomic.AtomicBoolean;

public class BidiProtocol<T> implements BidiMessagingProtocol<T>{
    private Connections connections;
    private int connectionId;
    private AtomicBoolean terminate;

    public BidiProtocol() {
        this.terminate.set(false);
    }

    @Override
    public void start(int connectionId, Connections connections) {
        this.connectionId = connectionId;
        this.connections = connections;
    }

    @Override
    public void process(T message) {
        connections.send(this.connectionId,message);
    }

    @Override
    public boolean shouldTerminate() {
        return terminate.get();
    }
}
