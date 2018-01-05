package bgu.spl181.net.api;

public interface DataHandler<T> {


    /**
     * handle the message
     * @param message
     * @return responses to the client/s
     */
    T[] Handle(T message, String clientName);

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
     * @param name to check if logged in
     * @return true if user is in loggedInList
     * else return false
     */
    boolean isLoggedIn(String name);

    /**
     * @param userName to sign out
     * @return true if user was in and removes him
     * else: false
     */
    boolean signOut(String userName);
}
