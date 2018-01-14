package bgu.spl181.net.impl.protocol;

import bgu.spl181.net.api.bidi.Connections;
import bgu.spl181.net.srv.ConnectionHandler;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class ServerConnections<T> implements Connections<T> {
    private ConcurrentHashMap<Integer,ClientForConnections> map;
    private ReentrantReadWriteLock lock;
    public ServerConnections() {

        this.map = new ConcurrentHashMap<>();
        this.lock = new ReentrantReadWriteLock(true);
    }

    @Override
    public boolean send(int connectionId, T msg) {
        lock.readLock().lock();
        try {
            ClientForConnections client = map.get(connectionId);
            boolean answer = (client != null);
            if (answer) {
                ConnectionHandler handler = map.get(connectionId).getHandler();
                handler.send(msg);
            }
            return answer;
        }
        finally {
            lock.readLock().unlock();
        }
    }

    public ConcurrentHashMap<Integer, ClientForConnections> getMap() {
        return map;
    }

    @Override
    public void broadcast(T msg) {
        lock.readLock().lock();
        try {
            for (ClientForConnections client : map.values())
                client.getHandler().send(msg);
        }
        finally {
            lock.readLock().unlock();
        }
    }

    @Override
    public void disconnect(int connectionId) {
        lock.writeLock().lock();
        try {
            map.remove(connectionId);
        }
        finally {
            lock.writeLock().unlock();
        }
    }

    public void activate(int connectionId,ConnectionHandler handler){
        lock.writeLock().lock();
        try {
            ClientForConnections client = new ClientForConnections(connectionId, handler);
            map.putIfAbsent(connectionId, client);
        }
        finally {
            lock.writeLock().unlock();
        }
    }
    public void connect(int connectionId){
        lock.writeLock().lock();
        try {
            map.get(connectionId).setIsLoggedIn(true);
        }
        finally {
            lock.writeLock().unlock();
        }
    }

    @Override
    public boolean isloggedIn(int connectionId) {//checks if user is connected and logged in
        lock.readLock().lock();
        try {
            return (map.containsKey(connectionId) && map.get(connectionId).isLoggedin());
        }
        finally {
            lock.readLock().unlock();
        }
    }
}
