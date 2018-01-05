package bgu.spl181.net.impl.protocol;

import bgu.spl181.net.api.bidi.Connections;
import bgu.spl181.net.srv.ConnectionHandler;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;

public class ServerConnections<T> implements Connections<T> {
    private ConcurrentHashMap<Integer,ClientForConnections> map;

    public ServerConnections() {
        this.map = new ConcurrentHashMap<>();
    }

    @Override
    public boolean send(int connectionId, T msg) {
        ClientForConnections client = map.get(connectionId);
        boolean answer =(client!=null);
        if (answer){
            ConnectionHandler handler= map.get(connectionId).getHandler();
            handler.send(msg);
        }
        return answer;
    }

    @Override
    public void broadcast(T msg) {
        for(ClientForConnections client : map.values())
            client.getHandler().send(msg);
    }

    @Override
    public void disconnect(int connectionId) {
        try {
            map.get(connectionId).getHandler().close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        map.remove(connectionId);//TODO make sure connection handler closes itself
    }

    public void activate(int connectionId,ConnectionHandler handler){
        ClientForConnections client = new ClientForConnections(connectionId,handler);
        map.putIfAbsent(connectionId,client);
    }
    public void connect(int connectionId){
        map.get(connectionId).setIsLoggedIn(true);
    }

    @Override
    public boolean isloggedIn(int connectionId) {//checks if user is connected and logged in
        return (map.containsKey(connectionId)&&map.get(connectionId).isLoggedin());
    }
}
