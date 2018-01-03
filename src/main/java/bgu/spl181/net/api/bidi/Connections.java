package bgu.spl181.net.api.bidi;

import bgu.spl181.net.impl.User;
import bgu.spl181.net.srv.ConnectionHandler;

import java.io.IOException;
import java.util.List;

public interface Connections<T> {

    boolean send(int connectionId, T msg);

    void broadcast(T msg);

    void disconnect(int connectionId);

    List<User> getConnectedUsers();

    User getConnectedUser(String userName);

    /**
     * @param connectionId num of user
     * @param handler connection handler of the user
     * adds user to the map
     */
    void activate(int connectionId,ConnectionHandler handler);

    /**
     * @param connectionId num of user
     * log's connected user in
     */
    void connect(int connectionId);

    /**
     * @param connectionId num of user
     * @return true if user is connected AND loggedIn
     */
    boolean islogedIn(int connectionId);
}