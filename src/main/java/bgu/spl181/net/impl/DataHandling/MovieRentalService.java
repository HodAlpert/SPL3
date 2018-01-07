package bgu.spl181.net.impl.DataHandling;

import bgu.spl181.net.api.DataHandler;
import bgu.spl181.net.impl.DataHandling.movies.Movie;
import bgu.spl181.net.impl.DataHandling.movies.MoviesList;
import bgu.spl181.net.impl.DataHandling.users.MovieInUser;
import bgu.spl181.net.impl.DataHandling.users.User;
import bgu.spl181.net.impl.DataHandling.users.UsersList;
import bgu.spl181.net.impl.messages.Message;
import com.google.gson.Gson;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.PrintWriter;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class MovieRentalService implements DataHandler<Message> {
    private ConcurrentHashMap<String,User> users;
    private ConcurrentHashMap<String,Movie> movies;
    private UsersList userslist;
    private MoviesList movielist;
    private final ReentrantReadWriteLock usersLock = new ReentrantReadWriteLock(true);
    private final ReentrantReadWriteLock movieLock = new ReentrantReadWriteLock(true);
    private AtomicInteger highestId;
    private CopyOnWriteArrayList<String> loggedInUsers;

    public MovieRentalService() {
        users = new ConcurrentHashMap<>();
        movies  = new ConcurrentHashMap<>();
        Gson UsersGson = new Gson();
        Gson MoviesGson = new Gson();
        movielist= new MoviesList();
        userslist= new UsersList();
        highestId=new AtomicInteger(0);
        loggedInUsers = new CopyOnWriteArrayList<>();
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
            if (highestId.get()<Integer.decode(movie.getId()))
                highestId.set(Integer.decode(movie.getId()));
            highestId.getAndIncrement();
        }
        //System.out.println(this);
    }

    /**
     * @param name     of user
     * @param password of user
     * @param country  of user
     *                 adds user to MovieRentalService to the Users.json file
     * @return true if user was registered, false if user was registered before
     * TODO make sure client performing is not logged in
     */
    @Override
    public boolean registerUser(String name, String password, String country) {
        usersLock.writeLock().lock();
        try {
            if (isUserExist(name))
                return false;
            User user = new User(name, password, country);
            users.put(name, user);
            userslist.addUser(user);
        }
        finally {
            usersLock.writeLock().unlock();
        }
            refreshUsers();
            return true;

    }

    /**
     * @param name     of user to validate
     * @param password of user to validate
     *                 adds user to loggedInUsers
     * @return false if Username and Password combination does not fit any user in the system, else: true
     */
    @Override
    public boolean loginValidation(String name, String password) {
        usersLock.readLock().lock();
        try {
            if ((!users.containsKey(name)) || !users.get(name).getPassword().equals(password))
                return false;
            else
                loggedInUsers.addIfAbsent(name);
            return true;
        }
        finally {
            usersLock.readLock().unlock();

        }
    }

    /**
     * @param userName to check if logged in
     * @return true if user is in loggedInList
     * else return false
     */
    @Override
    public boolean isLoggedIn(String userName) {
        usersLock.readLock().lock();
        try {
            return loggedInUsers.contains(userName);
        }
        finally {
            usersLock.readLock().unlock();

        }
    }

    /**
     * @param username of user to return balance-info of
     * @return userBalance
     */
    public Integer userBalanceInfo(String username) {
        usersLock.readLock().lock();
        try {
            return users.get(username).getBalance();
        }
        finally {
            usersLock.readLock().unlock();

        }

    }

    /**
     * @param userName to add amount to
     * @param amount to be added to the user
     *               adds the amount to user balance
     */
    public int userAddBalance(String userName, int amount) {
        int answer;
        usersLock.writeLock().lock();
        try {
            users.get(userName).setBalance(users.get(userName).getBalance() + amount);
            answer = users.get(userName).getBalance();
        }
        finally {
            usersLock.writeLock().unlock();
        }
            refreshUsers();
            return answer;


    }

    /**
     * @param userName to sign out
     * @return true if user was in {@link #loggedInUsers} and removes him
     * else: false
     */
    @Override
    public boolean signOut(String userName){
        if (isLoggedIn(userName)) {
            usersLock.writeLock().lock();
            try {
                loggedInUsers.remove(userName);
            }
            finally {
                usersLock.writeLock().unlock();
            }
            return true;
        } else
            return false;
    }

    /**
     * @param movieName to return info of, if null- returns a list of all movies names
     * @return if (movieName==null)
     * if (movie is in movie's list) return:
     * <”movie name”> <No. copies left> <price> <”banned country”,…>
     * else if the movie does not exist
     * @return null
     * else
     * @return a list of all  movies names
     */
    public String movieInfo(String movieName) {
        movieLock.readLock().lock();
        try{
            StringBuilder answer = new StringBuilder();
            if (movieName==null){//returns a list of all movies
                for(Movie movie:movies.values()){
                    if (answer.toString().equals(""))
                        answer.append('"').append(movie.getName()).append('"');
                    else
                        answer.append(" ").append('"').append(movie.getName()).append('"');
                }
            }
            else if(movies.containsKey(movieName)){//returns details of specific movie in format specified
                answer.append('"').append(movies.get(movieName).getName()).append('"').append(' ')
                        .append(movies.get(movieName).getAvailableAmount()).append(' ')
                        .append(movies.get(movieName).getPrice());
                for(String country: movies.get(movieName).getBannedCountries()){
                    answer.append(' ').append('"').append(country).append('"');
                }
            }
            else//if #movieName was not null and it's not exist- returns null
                return null;
            return String.valueOf(answer);
            }
        finally {
                movieLock.readLock().unlock();
            }
        }

    /**
     * @param userName  who wants to rent the movieName
     * @param movieName to be rented
     *              Server tries to add the movieName to the userName rented movieName list, remove the cost from the
     *              userName’s balance and reduce the amount available for rent by 1
     * @return reasons for false:
     * 1. The userName does not have enough money in their balance
     * 2. The movieName does not exist in the system
     * 3. There are no more copies of the movieName that are available for rental
     * 4. The movieName is banned in the userName’s country
     * 5. The userName is already renting the movieName
     * 6. userName not exist in the system
     * else:
     * true
     */
    public String rentMovie(String userName, String movieName) {
        boolean check;
        String answer = null;
        usersLock.writeLock().lock();
        movieLock.writeLock().lock();
        try {
            check = (isUserExist(userName) && isMovieExist(movieName) && userCanRentMovie(userName, movieName)& availableForRent(movieName));
            if (check) {
                Movie movie = movies.get(movieName);
                User user = users.get(userName);
                MovieInUser forRent = new MovieInUser(movie.getId(), movieName);
                //adding the movieName to the userName rented movieName list
                user.getMovies().add(forRent);
                //remove the cost from the userName’s balance
                user.setBalance(user.getBalance() - movie.getPrice());
                //reduce the amount available for rent by 1
                movie.setAvailableAmount(movie.getAvailableAmount() - 1);
                answer = movieInfo(movieName);
            }
        } finally {
            usersLock.writeLock().unlock();
            movieLock.writeLock().unlock();
        }
        if(check) {
            refreshUsers();
            refreshMovies();
        }

    return answer;
}

    /**
     * @param movieName  who wants to rent the movie
     * @param userName to be rented
     *              Server tries to remove the movie from the user rented movie list and increase the amount
     *              of available copies of the movies by 1
     * @return reasons for false:
     * 1. The user is currently not renting the movie
     * 2. The movie does not exist
     */
    public String returnMovie(String userName, String movieName) {
        boolean check;
        String answer = null;
        movieLock.writeLock().lock();
        usersLock.writeLock().lock();
        try {
            check = isUserExist(userName) && isMovieExist(movieName) && !userNotRentingMovie(userName, movieName);
            if (check) {
                Movie movie = movies.get(movieName);
                User user = users.get(userName);
                //remove the movie from the user rented movie list
                for (MovieInUser toRemove : user.getMovies()) {
                    if (toRemove.getName().equals(movie.getName())) {
                        user.getMovies().remove(toRemove);
                        answer = movieInfo(movieName);
                        break;
                    }
                }
                //increase the amount of available copies of the movies by 1
                movie.setAvailableAmount(movie.getAvailableAmount() + 1);
            }
        }
        finally {
                movieLock.writeLock().unlock();
                usersLock.writeLock().unlock();
            }
            if(check) {
                refreshMovies();
                refreshUsers();
            }
        return answer;
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
    public String addMovie(String userName, String movieName, int totalAmount, int price, List<String> bannedCountries) {

        boolean priceAndAmountOK;
        priceAndAmountOK = totalAmount>0&price>0;
        boolean check;
        String answer = null;
        movieLock.writeLock().lock();
            try{
                check = !isMovieExist(movieName)&isUserAdmin(userName)&priceAndAmountOK;
                if (check) {
                    Movie newMovie = new Movie(String.valueOf(highestId.get()), movieName, price, totalAmount, bannedCountries);
                    movies.put(movieName, newMovie);
                    movielist.getMovies().add(newMovie);
                    answer = movieStatus(movieName);
                }
            }
            finally {
                movieLock.writeLock().unlock();
            }
            if (check&priceAndAmountOK) {
                refreshMovies();
            }
        return answer;
    }

    /**
     * @param movieName to remove
     *                  - Server removes a movie by the given name from the system
     * @return reasons for false:
     * 1. User is not an administrator
     * 2. Movie does not exist in the system
     * 3. There is (at least one) a copy of the movie that is currently rented by a user
     */
    public boolean removeMovie(String userName, String movieName) {
        movieLock.writeLock().lock();
        boolean check;
        try {
            check = isUserAdmin(userName) & isMovieExist(movieName) & userNotRentingMovie(userName, movieName);
            if (check) {//checks condition Number 1
                movielist.getMovies().remove(movies.get(movieName));
                movies.remove(movieName);
            }
        }
        finally {
                movieLock.writeLock().unlock();
            }
            if (check) {
                refreshMovies();
                return true;
            }



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
    public String ChangeMoviePrice(String userName, String movieName, int price) {
        movieLock.writeLock().lock();
        String answer = null;
        boolean check;
        try {
            check = isUserAdmin(userName) & isMovieExist(movieName) & price > 0;
            if (check) {
                movies.get(movieName).setPrice(price);
                answer = movieStatus(movieName);
            }
        }
        finally {
            movieLock.writeLock().unlock();
        }
        if(check) {
            refreshMovies();
        }
        return answer;
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
    private boolean userNotRentingMovie(String userName, String movieName){
        Movie movie = movies.get(movieName);
        User user = users.get(userName);
            for (MovieInUser movie1 : user.getMovies()) {
                if (movie1.getName().equals(movie.getName()))
                    return false;
            }
            return true;
    }
    private boolean isUserAdmin(String userName){
        usersLock.readLock().lock();
        try{
            return users.get(userName).getType().equals("admin");
        }
        finally {
            usersLock.readLock().unlock();
        }
    }
    //assuming movie exist in database
    public String movieStatus(String name){
        StringBuilder answer = new StringBuilder();
        Movie movie = movies.get(name);
        answer.append("movie \"").append(movie.getName()).append("\" ").
                append(movie.getAvailableAmount()).append(" ").append(movie.getPrice());
        return String.valueOf(answer);
    }

    private boolean isMovieExist(String movieName){
            return movies.containsKey(movieName);
    }
    private boolean isUserExist(String userName){
            return users.containsKey(userName);
    }
    private boolean availableForRent(String movie){
        return movies.get(movie).getAvailableAmount() > 0;
        }

    private boolean userCanRentMovie(String userName, String movieName){

        Movie movie = movies.get(movieName);
        User user = users.get(userName);
        if (movie==null|user==null)
            return false;
        boolean notBannedInCountry, userBalanceEnugh, userNotRentingMovie;
        notBannedInCountry = !(movie.getBannedCountries().contains(user.getCountry()));
        userBalanceEnugh = user.getBalance() >= movie.getPrice();
        userNotRentingMovie = userNotRentingMovie(userName, movieName);
        return notBannedInCountry & userBalanceEnugh & userNotRentingMovie;
        }
    }

