package bgu.spl181.net.impl.DataHandling.users;

import java.io.Serializable;
import java.util.ArrayList;

public class User implements Serializable{
    private String username;
    private String password;
    private String type;
    private String country;
    private ArrayList<MovieInUser> movies;
    private String balance;

    public User(String username, String password, String country ) {
        this.username = username;
        this.type = "normal";
        this.password = password;
        this.country = country;
        this.balance = String.valueOf(0);
        this.movies = new ArrayList<>();
    }

    public String getPassword() {
        return password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public ArrayList<MovieInUser> getMovies() {
        return movies;
    }

    public int getBalance() {
        return Integer.valueOf(balance);
    }

    public void setBalance(int balance) {
        this.balance = String.valueOf(balance);
    }

    @Override
    public String toString() {
        return "\n" +
                "username: " + username + '\n' +
                "type: " + type + '\n' +
                "password: " + password + '\n' +
                "country: " + country + '\n' +
                "movies: " + movies +'\n' +
                "balance: " + balance +'\n' +
                '}';
    }
}

