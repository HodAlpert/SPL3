package bgu.spl181.net.impl;

import bgu.spl181.net.impl.DataHandling.MovieRentalService;
import bgu.spl181.net.impl.DataHandling.movies.Movie;
import bgu.spl181.net.impl.DataHandling.movies.MoviesList;
import bgu.spl181.net.impl.DataHandling.users.MovieInUser;
import bgu.spl181.net.impl.DataHandling.users.User;
import bgu.spl181.net.impl.DataHandling.users.UsersList;
import com.google.gson.Gson;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CountDownLatch;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertFalse;

@RunWith(Parameterized.class)
public class MovieRentalServiceTest{

    @Parameterized.Parameters
    public static List<Object[]> data() {
        return Arrays.asList(new Object[500][0]);
    }

    HashMap<String,User> currentUsers;
    HashMap<String,Movie> currentMovies;
    static MovieRentalService service;
    String defaultuserFile = "{\"users\":[{\"username\":\"john\",\"password\":\"potato\",\"type\":\"admin\",\"country\":\"united states\",\"movies\":[],\"balance\":0},{\"username\":\"lisa\",\"password\":\"chips123\",\"type\":\"normal\",\"country\":\"spain\",\"movies\":[{\"id\":\"2\",\"name\":\"The Pursuit Of Happyness\"},{\"id\":\"3\",\"name\":\"The Notebook\"}],\"balance\":37},{\"username\":\"shlomi\",\"password\":\"cocacola\",\"type\":\"normal\",\"country\":\"israel\",\"movies\":[{\"id\":\"1\",\"name\":\"The Godfather\"},{\"id\":\"2\",\"name\":\"The Pursuit Of Happyness\"}],\"balance\":112},{\"username\":\"hod0\",\"password\":\"246315\",\"type\":\"normal\",\"country\":\"israel\",\"movies\":[],\"balance\":15},{\"username\":\"hod1\",\"password\":\"246315\",\"type\":\"normal\",\"country\":\"israel\",\"movies\":[],\"balance\":15},{\"username\":\"hod2\",\"password\":\"246315\",\"type\":\"normal\",\"country\":\"israel\",\"movies\":[],\"balance\":15},{\"username\":\"hod3\",\"password\":\"246315\",\"type\":\"normal\",\"country\":\"israel\",\"movies\":[],\"balance\":15},{\"username\":\"hod4\",\"password\":\"246315\",\"type\":\"normal\",\"country\":\"israel\",\"movies\":[],\"balance\":55}]}";
    String DefaultMovieFile = "{\n" +
            "  \"movies\": [\n" +
            "    {\n" +
            "      \"id\": \"1\",\n" +
            "      \"name\": \"The Godfather\",\n" +
            "      \"price\": \"25\",\n" +
            "      \"bannedCountries\": [\n" +
            "        \"united kingdom\",\n" +
            "        \"spain\"\n" +
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

    @Before
    public void beforeTest() {
        refreshUsers();
        refreshMovies();
        service = new MovieRentalService();

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
        refreshUsers();
        boolean register = service.registerUser("omri", "246315", "israel");
        currentUsers=getCurrentUsers();
        assertTrue("registerUser returned false, should have return true",register);
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
    @Test
    public void registerUserNegativeTest(){
        boolean register = service.registerUser("john", "246315", "israel");
        assertFalse("register returned true- should have failed",register);
        currentUsers=getCurrentUsers();
        assertFalse("user was added to system, should not be added",
                currentUsers.get("john").getPassword().equals("246315"));
    }
    @Test
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
        currentUsers = getCurrentUsers();
        assertTrue("register should have been completed once, completed " +counter+ " times\n\n "+ currentUsers
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
        print("---------------------LOGIN VALIDATION TEST-------------------");
    }
    @Test
    public void loginPositiveTest(){
        boolean login = service.loginValidation("hod0","246315");
        assertTrue("login faild, should have return true",login);
    }
    @Test
    public void loginNegativeTest(){
        boolean badName = service.loginValidation("hodii","246315");
        boolean badPassword = service.loginValidation("hod0","123456");
        assertFalse("login succeded, should have failed due to wrong name", badName);
        assertFalse("login succeded, should have failed due to wrong password", badPassword);
    }
    @Test
    public void loginMultiThreadTest() {
        CopyOnWriteArrayList<Boolean> answer = new CopyOnWriteArrayList<>();
        CountDownLatch count = new CountDownLatch(10);
        for (int i = 0; i < 10; i++) {
            Thread t = new Thread(() -> {
                boolean login = service.loginValidation("hod0", "246315");
                answer.add(login);
                count.countDown();
            });
            t.start();
        }
        try {
            count.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        for (Boolean login : answer) {
            assertTrue("loginvalidation request should return true- failed", login);
        }
    }

    /**
     * 1) positive Test- login a user and then check for true
     * 2) negatice Test- check unloggedIn user
     * 3) threadSafeTest- send request with 10 threads and check result
     */
    @Test
    public void isLoggedIn() {
        print("-------------------ISLOGGEDIN TEST-------------");
        service.loginValidation("hod0","246315");
        boolean loggedIn = service.isLoggedIn("hod0");
        boolean notLoggedIn = service.isLoggedIn("john");
        assertTrue("user hod should be logged in", loggedIn);
        assertFalse("user john should not be logged in",notLoggedIn);
        for(int i=0;i<10;i++){
            Thread t = new Thread(()->{
               boolean trueTest = service.isLoggedIn("hod");
               boolean falseTest = service.isLoggedIn("john");
                assertTrue("user hod should be logged in- MultiThread Test", trueTest);
                assertFalse("user john should not be logged in- MultiThread Test",falseTest);
            });
        }
    }


    /**
     * 1) positive Test- check value is correct
     */
    @Test
    public void userBalanceInfo() {
        int balance = service.userBalanceInfo("shlomi");
        assertTrue("user info should be 112, returned "+balance,balance==112);
        for (int i=0;i<10;i++){
            Thread t = new Thread(()->{
               assertTrue("balance should be 0",service.userBalanceInfo("john")==0);
            });
            t.start();
        }
    }

    /**
     * 1) positive Test- check value is added
     * 2) threadSafe Test and Fairness Test- send requests with 10 threads check fairness
     */
    @Test
    public void userAddBalance() {
        int newBalance = service.userAddBalance("shlomi",8);
        assertTrue("newBalance should be 120, returned "+newBalance,newBalance==120);
    }
    @Test
    public void addBalanceMultiThreadTest(){
        CountDownLatch latch = new CountDownLatch(15);
        for(int i=0;i<15;i++){
            Thread t = new Thread(()->{
               service.userAddBalance("john",1);
               latch.countDown();
            });
            t.start();

        }
        try {
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        assertTrue("user balance should be 15, returned "+ service.userBalanceInfo("john"),
                service.userBalanceInfo("john")==15);
    }

    /**
     * 1) positiveTest- login user and then signout him- should return true
     * 2) negative Test- signout user before logging him out- should return false
     */
    @Test
    public void signOut() {
        service.loginValidation("hod0","246315");
        assertTrue("user signout should return true",service.signOut("hod0"));
        assertFalse("user should not be in logged in list",service.isLoggedIn("hod0"));
        assertFalse("user was not logged in and should not be able to signout",service.signOut("john"));
    }

    /**
     * 1) positive test- with a movieName
     * 2) positive Test- without movieName
     * 3) negative Test- with a movie that's not exist
     */

    @Test
    public void movieInfo() {
        String answerWithNoName = "\"The Godfather\" \"The Pursuit Of Happyness\" \"The Notebook\" \"Justice League\"";
        String actualAnswer = service.movieInfo(null);
        assertTrue("movie info should have returned "+ answerWithNoName+ "insted- got:\n" +
                actualAnswer,actualAnswer.equals(answerWithNoName));
        String answerWithName = "\"Justice League\" 4 17 \"jordan\" \"iran\" \"lebanon\"";
        actualAnswer = service.movieInfo("Justice League");
        assertTrue("movie info should have returned "+ answerWithName+ "insted- got:\n" +
                actualAnswer,actualAnswer.equals(answerWithName));
        assertTrue("movie info should have returned null", service.movieInfo("test")==null);
    }

    /**
     * 1) positive Test- renting movie that should be rented
         * a) should return true
         * b) should reduce balanca
         * c) available amount should be reduced
         * d) user should have movie in moviesList
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
    public void RentingAMoviePositiveTest() {
        boolean rentMovie =service.rentMovie("lisa","Justice League");
        assertTrue("movie should have been rented",rentMovie);
        currentUsers = getCurrentUsers();
        currentMovies = getCurrentMovies();
        assertTrue("user balance should be reduced to 20, got "+currentUsers.get("lisa").getBalance()+" instead",
                currentUsers.get("lisa").getBalance()==20);
        assertTrue("available amount of \" justice leage\" should be 3, got "+currentMovies.get("Justice League")+ " instead",
                currentMovies.get("Justice League").getAvailableAmount()==3);
        assertTrue("lisa should have \"Justice League\" in her movies List, her actuall movies list is \n"+
        currentUsers.get("lisa").getMovies(),currentUsers.get("lisa").getMovies().contains(new MovieInUser("4","Justice League")));
    }
    @Test
    public void RentingAMovieNotExist() {
        assertFalse("movie name not exist- should not be rented",
                service.rentMovie("lisa","nonExistingMovie"));
    }
    @Test
    public void RentingAMovieNoAvailableCopies() {
        assertFalse("movie \"The Notebook\" has no available copies",
                service.rentMovie("lisa","The Notebook"));
    }
    @Test
    public void RentingAMovieBannedUserCountry() {
        assertFalse("movie cannot be rented in user's country",
                service.rentMovie("lisa","The Godfather"));
    }
    @Test
    public void RentingAMovieRentedByUser() {
        assertFalse("movie is already rented by user",
                service.rentMovie("lisa","The Pursuit Of Happyness"));
    }
    @Test
    public void RentingAMovieUserNotExist() {
        assertFalse("user not exist",
                service.rentMovie("lalaa","The Pursuit Of Happyness"));
    }
    @Test
    public void RentingAMovieMultiThreadTest1(){
        CopyOnWriteArrayList<Boolean> answer = new CopyOnWriteArrayList<>();
        int counter = 0;
        Thread[] threads =new Thread[5];
        CountDownLatch count = new CountDownLatch(5);
            threads[0] = new Thread(()->{
               answer.add(service.rentMovie("hod"+0,"The Pursuit Of Happyness"));
                count.countDown();

            });
        threads[1] = new Thread(()->{
            answer.add(service.rentMovie("hod"+1,"The Pursuit Of Happyness"));
            count.countDown();

        });threads[2] = new Thread(()->{
            answer.add(service.rentMovie("hod"+2,"The Pursuit Of Happyness"));
            count.countDown();

        });threads[3] = new Thread(()->{
            answer.add(service.rentMovie("hod"+3,"The Pursuit Of Happyness"));
            count.countDown();

        });threads[4] = new Thread(()->{
            answer.add(service.rentMovie("hod"+4,"The Pursuit Of Happyness"));
            count.countDown();

        });
        for (int i=0;i<5;i++){
            threads[i].start();
        }
        try {
            count.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        for (boolean ans: answer){
            if (ans)
                counter++;
        }
        assertTrue("only 3 users should be able to rent the movie, "+ counter + " manaegd to",counter==3);
    }
    @Test
    public void RentingAMovieMultiThreadTest2(){
        CopyOnWriteArrayList<Boolean> answer = new CopyOnWriteArrayList<>();
        int counter = 0;
        Thread[] threads =new Thread[3];
        CountDownLatch count = new CountDownLatch(3);
        threads[0]=new Thread(()->{
           answer.add(service.rentMovie("hod4","The Pursuit Of Happyness"));
           count.countDown();
        });
        threads[1]=new Thread(()->{
            answer.add(service.rentMovie("hod4","Justice League"));
            count.countDown();
        });
        threads[2]=new Thread(()->{
            answer.add(service.rentMovie("hod4","The Godfather"));
            count.countDown();
        });
        for(int i=0;i<3;i++){
            threads[i].start();
        }
        try {
            count.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        for(boolean ans: answer){
            if (ans)
                counter++;
        }
        assertTrue("user can rent only two movies, rented " + counter,
                counter==2);

    }

    /**
     * 1) positive Test- returning a movie currently rented by user
     *      a) movie is removed from uer's movie List
     *      b) available amount of movie should be increased by 1
     * 2) negative Test- returning a movie currently not rented by user
     * 3) negative Test- returning a movie not existing
     * 4) threadSafe Test- returning a movie rented by user 3 times by 3 different threads
     * (should return only once- others should return false and do noting)
     */
    @Test
    public void returnMoviePositiveTest() {
        assertTrue("return movie call should have return true",service.returnMovie("lisa","The Pursuit Of Happyness"));
        currentUsers = getCurrentUsers();
        currentMovies = getCurrentMovies();
        assertFalse("movie \"The Pursuit Of Happyness\" should be removed from user's movie List",
                currentUsers.get("lisa").getMovies().contains(new MovieInUser("2","The Pursuit Of Happyness")));
        assertTrue("available amount should be 4, returned " +currentMovies.get("The Pursuit Of Happyness").getAvailableAmount(),
                currentMovies.get("The Pursuit Of Happyness").getAvailableAmount()==4);
    }
    @Test
    public void returnMovieNotRentedByUserTest() {
        assertFalse("movie was not rented by user",
                service.returnMovie("john","The Pursuit Of Happyness"));
    }
    @Test
    public void returnMovieNotExistTest() {
        assertFalse("movie was not exist",
                service.returnMovie("john","The Pursuit Of Happynes2s"));
    }
    @Test
    public void returnMovieMultiThreadTest() {
        CopyOnWriteArrayList<Boolean> answer = new CopyOnWriteArrayList<>();
        int counter = 0;
        Thread[] threads =new Thread[3];
        CountDownLatch count = new CountDownLatch(3);
        for (int i=0;i<3;i++){
            threads[i]=new Thread(()->{

               answer.add(service.returnMovie("lisa","The Pursuit Of Happyness"));
               count.countDown();
            });
        }
        for(int i=0;i<3;i++){
            threads[i].start();
        }
        try {
            count.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        for (boolean ans:answer){
            if (ans)
                counter++;
        }

        currentUsers=getCurrentUsers();
        currentMovies = getCurrentMovies();
        assertTrue("should be able to return movie only once, returned "+ counter+ " times"
                ,counter==1);
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
        assertTrue("movie should return true",
                service.addMovie("john","hod's new movie",3 ,10, new ArrayList<>(Arrays.asList("country1", "country2"))));
        currentMovies = getCurrentMovies();
        assertTrue("new movie should be added",
              currentMovies.containsKey("hod's new movie"));
        Movie hodMovie = currentMovies.get("hod's new movie");
        assertTrue("id should be 5, was "+ hodMovie.getId(),
                hodMovie.getId().equals("5"));
        assertTrue("movie Name should be \"hod's new movie\" was "+hodMovie.getName(),
                hodMovie.getName().equals("hod's new movie"));
        assertTrue("movie price should be 10, was "+ hodMovie.getPrice(),
                hodMovie.getPrice()==10);
        assertTrue("banned countries list is not what expected",
                hodMovie.getBannedCountries().containsAll((Arrays.asList("country1", "country2"))));
        assertTrue("available amount should be 3 was" + hodMovie.getAvailableAmount(),
                hodMovie.getAvailableAmount()==3);
        assertTrue("total amount should be 3, was "+ hodMovie.getTotalAmount(),
                hodMovie.getTotalAmount()==3);
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
            movieslist = gson.fromJson(new FileReader("Database/Movies.json"), MoviesList.class);
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
