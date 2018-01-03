package bgu.spl181.net.api;

import bgu.spl181.net.api.bidi.Connections;
import bgu.spl181.net.impl.User;

public interface DataHandler {
    /**
     * @param name of user
     * @param password of user
     * @param dataBlock of user
     *adds user to MovieRentalService to the Users.json file
     * @return true if user was registered, false if user was registered before
     */
    boolean registerUser(String name, String password, String dataBlock);

    /**
     * @param name of user to validate
     * @param password of user to validate
     * @return false if Username and Password combination does not fit any user in the system, else: true
     */
    boolean loginValidation(String name, String password);

    /**
     *
     * @param name user's name
     * @return the user if exists, else return null
     */
    User getUser(String name);

    /**
     * performs the request if possible
     * if possible, sends ack message according to the specific implementation
     * @param request
     * @param user the one who made the request
     * @param connections to send ack message if needed
     * @param connectionId to send ack message if needed
     * @return true if the request was successful, else return false
     */
    boolean proccessRequest(String[] request, User user, Connections connections, int connectionId);

}
