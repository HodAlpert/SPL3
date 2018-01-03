package bgu.spl181.net.impl;

import bgu.spl181.net.api.bidi.Connections;
import bgu.spl181.net.srv.ConnectionHandler;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class ServerConnections<T> implements Connections<T> {
    private ConcurrentHashMap<Integer,ClientForConnections> map;
    private List<User> connectedUsers;

    public ServerConnections() {
        this.map = new ConcurrentHashMap<>();
        this.connectedUsers = new LinkedList<>();
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
        for(ClientForConnections client : map.values()){
            client.getHandler().send(msg);
        }
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

    @Override
    public List<User> getConnectedUsers() {
        return this.connectedUsers;
    }

    @Override
    public User getConnectedUser(String userName) {
        for(int i=0;i<this.connectedUsers.size();i++)
            if(this.connectedUsers.get(i).getUsername()==userName)
                return this.connectedUsers.get(i);
        return null;
    }


    public void activate(int connectionId,ConnectionHandler handler){
        ClientForConnections client = new ClientForConnections(connectionId,handler);
        map.putIfAbsent(connectionId,client);
    }
    public void connect(int connectionId){
        map.get(connectionId).setIsLoggedIn(true);
    }

    @Override
    public boolean islogedIn(int connectionId) {//checks if user is connected and logged in
        return (map.containsKey(connectionId)&&map.get(connectionId).isLogedin());
    }
}
