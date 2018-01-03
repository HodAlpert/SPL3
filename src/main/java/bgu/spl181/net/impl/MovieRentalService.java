package bgu.spl181.net.impl;

import com.google.gson.Gson;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.HashMap;

public class MovieRentalService {
    private HashMap<String,User> users;
    private HashMap<String,Movie> movies;

    private static class MovieRentalServiceHolder {
        private static MovieRentalService instance = new MovieRentalService();
    }
    public static MovieRentalService getInstance(){
        return MovieRentalServiceHolder.instance;
    }

    private MovieRentalService() {
        users = new HashMap<>();
        movies  = new HashMap<>();
        Gson UsersGson = new Gson();
        Gson MoviesGson = new Gson();
        MoviesList movielist = new MoviesList();
        UsersList userslist = new UsersList();
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
        }
        for (Movie movie:movies.values()){
            System.out.println(movie);
        }
        for(User user:users.values()){
            System.out.println(user);
        }



    }


}
