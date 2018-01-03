package bgu.spl181.net.impl;

import bgu.spl181.net.api.DataHandler;
import com.google.gson.Gson;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MovieRentalService implements DataHandler {
    private HashMap<String,User> users;
    private HashMap<String,Movie> movies;
    private UsersList userslist;
    private MoviesList movielist;
    private int highestId;
    private List<String> loggedInUsers;

    private MovieRentalService() {
        users = new HashMap<>();
        movies  = new HashMap<>();
        Gson UsersGson = new Gson();
        Gson MoviesGson = new Gson();
        movielist= new MoviesList();
        userslist= new UsersList();
        highestId=0;
        loggedInUsers = new ArrayList<String>();
        try {
            userslist = UsersGson.fromJson(new FileReader("Database/Users.json"), UsersList.class);
            movielist = MoviesGson.fromJson(new FileReader("Database/Movies.json"), MoviesList.class);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        for(User user: userslist.getUsers()){
            users.put(user.getUsername(), user);
        }
        for(Movie movie: movielist.getMovies()){
            movies.put(movie.getName(), movie);
            if (highestId<Integer.decode(movie.getId()))
                highestId=Integer.decode(movie.getId());
        }
        System.out.println(this);
    }

    /**
     * @param name     of user
     * @param password of user
     * @param country  of user
     *                 adds user to MovieRentalService to the Users.json file
     * @return true if user was registered, false if user was registered before
     */
    @Override
    public boolean registerUser(String name, String password, String country) {
        synchronized (users) {
            if (users.containsKey(name))
                return false;
            User user = new User(name, password, country);
            users.put(name, user);
            userslist.addUser(user);
            refreshUsers();
            return true;

        }
    }

    /**
     * @param name     of user to validate
     * @param password of user to validate
     *                 adds user to loggedInUsers
     * @return false if Username and Password combination does not fit any user in the system, else: true
     */
    @Override
    public boolean loginValidation(String name, String password) {
        synchronized (userslist){
            if ((!users.containsKey(name))|!users.get(name).getPassword().equals(password))
                return false;
            else
                loggedInUsers.add(name);
            return true;
        }
    }

    /**
     * @param userName to check if logged in
     * @return true if user is in loggedInList
     * else return false
     */
    @Override
    public boolean isLoggedIn(String userName) {
        synchronized (userslist){
            return loggedInUsers.contains(userName);
        }
    }

    /**
     * @param username of user to return balance-info of
     * @return userBalance
     */
    public Integer userBalanceInfo(String username) {
        synchronized (userslist){
            return users.get(username).getBalance();
        }
    }

    /**
     * @param userName to add amount to
     * @param amount to be added to the user
     *               adds the amount to user balance
     */
    public void userAddBalance(String userName, int amount) {
        synchronized (userslist){
            users.get(userName).setBalance(users.get(userName).getBalance()+amount);
            refreshUsers();
        }
    }

    /**
     * @param movieName to return info of, if null- returns a list of all movies names
     * @return if (movieName==null)
     * if (movie is in movie's list) return:
     * <”movie name”> <No. copies left> <price> <”banned country”,…>
     * else
     * return null
     * else
     * return a list of all  movies names
     */
    public String movieInfo(String movieName) {
        synchronized (movielist){
            StringBuilder answer = new StringBuilder();
            if (movieName==null){
                for(Movie movie:movies.values()){
                    if (!answer.toString().equals(""))
                        answer.append('"').append(movie.getName()).append('"');
                    else
                        answer.append(" ").append('"').append(movie.getName()).append('"');
                }
                return String.valueOf(answer);
            }
            else{
                answer.append(movies.get(movieName).getName()).append(" ")
                        .append(movies.get(movieName).getAvailableAmount()).append(" ")
                        .append(movies.get(movieName).getPrice()).append(" ")
                        .append(movies.get(movieName).getBannedCountries());//TODO- set format
                return String.valueOf(answer);
            }
        }
    }

    /**
     * @param user  who wants to rent the movie
     * @param movie to be rented
     *              Server tries to add the movie to the user rented movie list, remove the cost from the
     *              user’s balance and reduce the amount available for rent by 1
     * @return reasons for false:
     * 1. The user does not have enough money in their balance
     * 2. The movie does not exist in the system
     * 3. There are no more copies of the movie that are available for rental
     * 4. The movie is banned in the user’s country
     * 5. The user is already renting the movie
     * 6. user not exist in the system
     * else:
     * true
     */
    public boolean rentMovie(String user, String movie) {
        return false;
    }

    /**
     * @param user  who wants to rent the movie
     * @param movie to be rented
     *              Server tries to remove the movie from the user rented movie list and increase the amount
     *              of available copies of the movies by 1
     * @return reasons for false:
     * 1. The user is currently not renting the movie
     * 2. The movie does not exist
     */
    public boolean returnMovie(String user, String movie) {
        return false;
    }

    /**
     * @param movieName       name of movie
     * @param totalAmount     total amount of movie
     * @param price           of movie
     * @param bannedCountries of movie
     *                        - The server adds a new movie to the system with the given information. The new movie
     *                        ID will be the highest ID in the system + 1
     * @return reasons for false:
     * 1. User is not an administrator
     * 2. Movie name already exists in the system
     * 3. Price or Amount are smaller than or equal to 0 (there are no free movies)
     */
    public boolean addMovie(String movieName, int totalAmount, int price, List<String> bannedCountries) {
        return false;
    }

    /**
     * @param movieName to remove
     *                  - Server removes a movie by the given name from the system
     * @return reasons for false:
     * 1. User is not an administrator
     * 2. Movie does not exist in the system
     * 3. There is (at least one) a copy of the movie that is currently rented by a user
     */
    public boolean removeMovie(String movieName) {
        return false;
    }

    /**
     * @param movieName to change price of
     * @param price     new price of movie
     *                  - Server changes the price of a movie by the given name
     * @return reasons for false:
     * 1. User is not an administrator
     * 2. Movie does not exist in the system
     * 3. Price is smaller than or equal to 0
     */
    public boolean ChangeMoviePrice(String movieName, int price) {
        return false;
    }

    private static class MovieRentalServiceHolder {
        private static MovieRentalService instance = new MovieRentalService();
    }
    public static MovieRentalService getInstance(){
        return MovieRentalServiceHolder.instance;
    }



    @Override
    public String toString() {
        return
                "users: \n" + users +'\n'+'\n'+
                "movies: \n" + movies +
                '}';
    }
    private void refreshUsers(){
        Gson gson = new Gson();
        String json =gson.toJson(userslist);
        try {
            PrintWriter out = new PrintWriter("Database/Users.json");
            out.print(json);
            out.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void refreshMovies(){
        Gson gson = new Gson();
        String json =gson.toJson(movielist);
        try {
            PrintWriter out = new PrintWriter("Database/Movies.json");
            out.print(json);
            out.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }
}
