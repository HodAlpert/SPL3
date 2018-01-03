package bgu.spl181.net.impl;

import bgu.spl181.net.api.MovieRenatlDataHandler;
import com.google.gson.Gson;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

public class MovieRentalService implements MovieRenatlDataHandler {
    private HashMap<String,User> users;
    private HashMap<String,Movie> movies;
    private UsersList userslist;
    private MoviesList movielist;
    private FileReader moviesReader;
    private FileReader usersReader;
    int highestId;
    private MovieRentalService() {
        users = new HashMap<>();
        movies  = new HashMap<>();
        Gson UsersGson = new Gson();
        Gson MoviesGson = new Gson();
        movielist= new MoviesList();
        userslist= new UsersList();
        highestId=0;
        try {
            usersReader = new FileReader("Database/Users.json");
            moviesReader=new FileReader("Database/Movies.json");
            userslist = UsersGson.fromJson(usersReader, UsersList.class);
            movielist = MoviesGson.fromJson(moviesReader, MoviesList.class);
            usersReader.close();
            moviesReader.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
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
            Gson gson = new Gson();
            try {
                usersReader.reset();
                gson.toJson(userslist, new FileWriter("Database/Users.json"));
                usersReader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return true;

        }
    }

    /**
     * @param name     of user to validate
     * @param password of user to validate
     * @return false if Username and Password combination does not fit any user in the system, else: true
     */
    @Override
    public boolean loginValidation(String name, String password) {
        return false;
    }

    /**
     * @param username of user to return balance-info of
     * @return userBalance
     */
    @Override
    public int userBalanceInfo(String username) {
        return 0;
    }

    /**
     * @param amount to be added to the user
     *               adds the amount to user balance
     */
    @Override
    public void userAddBalance(int amount) {

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
    @Override
    public String movieInfo(String movieName) {
        return null;
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
    @Override
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
    @Override
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
    @Override
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
    @Override
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
    @Override
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
}
