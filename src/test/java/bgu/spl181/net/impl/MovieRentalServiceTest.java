package bgu.spl181.net.impl;

import bgu.spl181.net.impl.DataHandling.MovieRentalService;
import bgu.spl181.net.impl.DataHandling.movies.Movie;
import bgu.spl181.net.impl.DataHandling.movies.MoviesList;
import bgu.spl181.net.impl.DataHandling.users.User;
import bgu.spl181.net.impl.DataHandling.users.UsersList;
import com.google.gson.Gson;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CountDownLatch;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertFalse;

public class MovieRentalServiceTest {

    HashMap<String,User> currentUsers;
    HashMap<String,Movie> currentMovies;
    static MovieRentalService service;
    String defaultuserFile = "{\"users\":[{\"username\":\"john\",\"password\":\"potato\",\"type\":\"admin\",\"country\":\"united states\",\"movies\":[],\"balance\":0},{\"username\":\"lisa\",\"password\":\"chips123\",\"type\":\"normal\",\"country\":\"spain\",\"movies\":[{\"id\":\"2\",\"name\":\"The Pursuit Of Happyness\"},{\"id\":\"3\",\"name\":\"The Notebook\"}],\"balance\":37},{\"username\":\"shlomi\",\"password\":\"cocacola\",\"type\":\"normal\",\"country\":\"israel\",\"movies\":[{\"id\":\"1\",\"name\":\"The Godfather\"},{\"id\":\"2\",\"name\":\"The Pursuit Of Happyness\"}],\"balance\":112},{\"username\":\"hod\",\"password\":\"246315\",\"type\":\"normal\",\"country\":\"israel\",\"movies\":[],\"balance\":0}]}";
    String DefaultMovieFile = "{\n" +
            "  \"movies\": [\n" +
            "    {\n" +
            "      \"id\": \"1\",\n" +
            "      \"name\": \"The Godfather\",\n" +
            "      \"price\": \"25\",\n" +
            "      \"bannedCountries\": [\n" +
            "        \"united kingdom\",\n" +
            "        \"italy\"\n" +
            "      ],\n" +
            "      \"availableAmount\": \"1\",\n" +
            "      \"totalAmount\": \"2\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"id\": \"2\",\n" +
            "      \"name\": \"The Pursuit Of Happyness\",\n" +
            "      \"price\": \"14\",\n" +
            "      \"bannedCountries\": [],\n" +
            "      \"availableAmount\": \"3\",\n" +
            "      \"totalAmount\": \"5\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"id\": \"3\",\n" +
            "      \"name\": \"The Notebook\",\n" +
            "      \"price\": \"5\",\n" +
            "      \"bannedCountries\": [],\n" +
            "      \"availableAmount\": \"0\",\n" +
            "      \"totalAmount\": \"1\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"id\": \"4\",\n" +
            "      \"name\": \"Justice League\",\n" +
            "      \"price\": \"17\",\n" +
            "      \"bannedCountries\": [\n" +
            "        \"jordan\",\n" +
            "        \"iran\",\n" +
            "        \"lebanon\"\n" +
            "      ],\n" +
            "      \"availableAmount\": \"4\",\n" +
            "      \"totalAmount\": \"4\"\n" +
            "    }\n" +
            "  ]\n" +
            "}";

    @BeforeClass
    public static void beforeClass() {
        service = new MovieRentalService();
    }

    @Before
    public void beforeTest() {
        refreshUsers();
        refreshMovies();

    }

    /**
     * 1) positive test- add user that's not exist- should be created
     * 2) negative test- add user that's already registered- should not be created
     * 3) ThreadSafeTest- registering same user with 10 threads- should only be added once- the rest should return false
     * assuming user sending it is not logged in
     */
    @Test
    public void registerUser() {
        print("--------------------REGISTER USER TEST--------------------\n" +
                "\n");
    }

    @Test
    public void registerUserPositiveTest() {
        boolean register = service.registerUser("omri", "246315", "israel");
        assertTrue("registerUser returned false, should have return true",register);
        currentUsers=getCurrentUsers();
        assertTrue("user was not registered",currentUsers.containsKey("omri"));
        assertTrue("student was registered with wrong password",
                currentUsers.get("omri").getPassword().equals("246315"));
        assertTrue("student was registered with wrong Type",
                currentUsers.get("omri").getType().equals("normal"));
        assertTrue("student was registered with wrong country",
                currentUsers.get("omri").getCountry().equals("israel"));
        assertTrue("student was registered with wrong password",
                currentUsers.get("omri").getBalance()==0);
        assertTrue("student was registered with wrong movies",
                currentUsers.get("omri").getMovies()!=null&&
                        currentUsers.get("omri").getMovies().isEmpty());

    }
    public void registerUserNegativeTest(){
        boolean register = service.registerUser("john", "246315", "israel");
        assertFalse("register returned true- should have failed",!register);
        currentUsers=getCurrentUsers();
        assertFalse("user was added to system, should not be added",
                currentUsers.get("john").getPassword().equals("246315"));
    }
    public void registerUserMultiThreadTest(){
        CopyOnWriteArrayList<Boolean> answer = new CopyOnWriteArrayList<>();
        int counter=0;
        CountDownLatch count = new CountDownLatch(10);
        for(int i=0;i<10;i++){
            Thread t = new Thread(()->{boolean register = service.registerUser("omri", "246315", "israel");
                answer.add(register);
                count.countDown();
            });
            t.start();
        }
        try {
            count.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        for(Boolean register: answer){
            if (register)
                counter++;
        }
        assertTrue("register should have been completed once, completed " +counter+ " times"
                ,counter==1);
        assertTrue("user was not registered",currentUsers.containsKey("omri"));
        assertTrue("student was registered with wrong password",
                currentUsers.get("omri").getPassword().equals("246315"));
        assertTrue("student was registered with wrong Type",
                currentUsers.get("omri").getType().equals("normal"));
        assertTrue("student was registered with wrong country",
                currentUsers.get("omri").getCountry().equals("israel"));
        assertTrue("student was registered with wrong password",
                currentUsers.get("omri").getBalance()==0);
        assertTrue("student was registered with wrong movies",
                currentUsers.get("omri").getMovies()!=null&&
                        currentUsers.get("omri").getMovies().isEmpty());

    }

    /**
     * 1) positiveTest- check match userName and password
     * 2) negativeTest- check unMatch userName and password
     * 3) threadSafeTest- send request with five threads, check rightness
     */
    @Test
    public void loginValidation() {
    }

    /**
     * 1) positive Test- login a user and then check for true
     * 2) negatice Test- check unloggedIn user
     * 3) threadSafeTest- send request with 10 threads and check result
     */
    @Test
    public void isLoggedIn() {
    }

    /**
     * 1) positive Test- check value is correct
     * 2) threadSafeTest- send request with 10 threads and check result
     */
    @Test
    public void userBalanceInfo() {
    }

    /**
     * 1) positive Test- check value is added
     * 2) threadSafe Test and Fairness Test- send requests with 10 threads check fairness
     */
    @Test
    public void userAddBalance() {
    }

    /**
     * 1) positiveTest- login user and then signout him- should return true
     * 2) negative Test- signout user before logging him out- should return false
     */
    @Test
    public void signOut() {
    }

    /**
     * 1) positive test- with a movieName
     * 2) positive Test- without movieName
     * 3) negative Test- with a movie that's not exist
     */

    @Test
    public void movieInfo() {
    }

    /**
     * 1) positive Test- renting movie that should be rented
     * a) should return true
     * b) should reduce balanca
     * c) available amount should be reduced
     * 2) negative Test- renting a movie when there is not enough balance
     * 3) negative Test renting a movie that does not exist
     * 4) negative Test- movie has no available copies for renting
     * 5) negative Test- movie is banned in user's country
     * 6) negative Test- movie is already rented by user
     * 7) negative Test- userName not exist in the system
     * 8) threadSafe Test- 3 threads trying to rent movie with two available copies- only two should succeed
     * 9) threadSafe Test- 3 threads trying to rent 3 movies, while user have money only for one of them
     */

    @Test
    public void rentMovie() {
    }

    /**
     * 1) positive Test- returning a movie currently rented by user
     * 2) negative Test- returning a movie currently not rented by user
     * 3) negative Test- returning a movie not existing
     * 4) threadSafe Test- returning a movie rented by user 3 times by 3 different threads
     * (should return only once- others should return false and do noting)
     */
    @Test
    public void returnMovie() {
    }

    /**
     * 1)positive Test- adding a movie that's can be added
     * 2) negative Test- adding a movie with a user who's not admin
     * 3) negative Test- adding a movie that's already in the system
     * 4) negative Test- adding a movie with wrong price
     * 5) negative Test- adding a movie with wrong amount
     * 6) threadSafe Test- adding a movie that's not exist by 3 threads- only one should succeed
     */
    @Test
    public void addMovie() {
    }

    /**
     * 1) positive Test- removing a movie that's not rented by anyone
     * 2) negative Test- user not admin
     * 3) negative Test- movie not in the system
     * 4) negative Test- movie is rented by some user
     * 5) threadSafe Test- remove movie by 3 threads, only 1 should succeed
     */
    @Test
    public void removeMovie() {
    }

    /**
     * 1) positive Test- changing price of movie- should return true and change
     * 2) negative Test- changing price with not admin user, should return false and do nothing
     * 3) negative Test- changing price to a price lower then 0
     * 4) negative Test- changing price to movie that's not exist
     * 5) threadSafe Test- change same movie's price by 5 threads, price result should by of one of the threads
     */
    @Test
    public void changeMoviePrice() {
    }

    private void refreshUsers() {
        try (PrintWriter out = new PrintWriter("Database/Users.json")) {
            out.print(defaultuserFile);
            out.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void refreshMovies() {
        try (PrintWriter out = new PrintWriter("Database/Movies.json")) {
            out.print(DefaultMovieFile);
            out.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private HashMap<String, User> getCurrentUsers() {
        UsersList userslist = new UsersList();
        HashMap<String,User> users=new HashMap<>();
        Gson gson = new Gson();
        try {
            userslist = gson.fromJson(new FileReader("Database/Users.json"), UsersList.class);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        for (User user : userslist.getUsers()) {
            users.put(user.getUsername(), user);

        }
        return users;
    }
    private HashMap<String, Movie> getCurrentMovies() {
        MoviesList movieslist = new MoviesList();
        HashMap<String,Movie> movies=new HashMap<>();
        Gson gson = new Gson();
        try {
            movieslist = gson.fromJson(new FileReader("Database/Users.json"), MoviesList.class);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        for (Movie movie : movieslist.getMovies()) {
            movies.put(movie.getName(), movie);

        }
        return movies;
    }
    private void print(Object o){
        System.out.println(o);
    }
}
