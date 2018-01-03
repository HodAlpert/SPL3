package bgu.spl181.net.api;

public interface DataHandler {
    /**
     * @param name of user
     * @param password of user
     * @param dataBlock of user
     *adds user to MovieRentalService to the Users.json file
     * @return true if user was registered, false if user was registered before
     */
    public boolean registerUser(String name, String password, String dataBlock);

    /**
     * @param name of user to validate
     * @param password of user to validate
     * @return false if Username and Password combination does not fit any user in the system, else: true
     */
    public boolean loginValidation(String name, String password);
}
