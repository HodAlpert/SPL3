package bgu.spl181.net.impl.protocol;

import bgu.spl181.net.srv.ConnectionHandler;

import java.util.concurrent.atomic.AtomicBoolean;

public class ClientForConnections<T> {

    private final int id;
    private AtomicBoolean isLoggedIn;
    private final ConnectionHandler<T> handler;


    public ClientForConnections(int id, ConnectionHandler<T> handler){
        this.id=id;
        isLoggedIn = new AtomicBoolean(false);
        this.handler=handler;
    }
    public int getId() {
        return id;
    }

    public ConnectionHandler<T> getHandler() {
        return handler;
    }
    public boolean isLoggedin(){
        return isLoggedIn.get();
    }
    public void setIsLoggedIn(boolean value){
        isLoggedIn.set(value);
    }

}
