package bgu.spl181.net.impl;

import bgu.spl181.net.srv.ConnectionHandler;

import java.util.concurrent.atomic.AtomicBoolean;

public class ClientForConnections {

    private final int id;
    private AtomicBoolean isLoggedIn;
    private final ConnectionHandler handler;


    public ClientForConnections(int id, ConnectionHandler handler){
        this.id=id;
        this.isLoggedIn = new AtomicBoolean(false);
        this.handler=handler;
    }
    public int getId() {
        return id;
    }

    public ConnectionHandler getHandler() {
        return handler;
    }
    public boolean isLogedin(){
        return isLoggedIn.get();
    }
    public void setIsLoggedIn(boolean value){
        isLoggedIn.set(value);
    }

}
