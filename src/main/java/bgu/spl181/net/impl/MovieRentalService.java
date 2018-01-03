package bgu.spl181.net.impl;

import java.util.ArrayList;

public class MovieRentalService {
    private ArrayList<User> users;
    private ArrayList<Movie> movies;

    private static class MovieRentalServiceHolder {
        private static MovieRentalService instance = new MovieRentalService();
    }
    public static MovieRentalService getInstance(){
        return MovieRentalServiceHolder.instance;
    }

    private MovieRentalService() {
        users = new ArrayList<>();
        movies  = new ArrayList<>();
    }

    public ArrayList<User> getUsers() {
        return users;
    }

    public void setUsers(ArrayList<User> users) {
        this.users = users;
    }

    public ArrayList<Movie> getMovies() {
        return movies;
    }

    public void setMovies(ArrayList<Movie> movies) {
        this.movies = movies;
    }
}
