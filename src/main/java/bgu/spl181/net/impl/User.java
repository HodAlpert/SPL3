package bgu.spl181.net.impl;

import java.util.ArrayList;
import java.util.HashMap;

public class User {
    private String username;
    private String type;
    private String country;
    private HashMap<String,String> movies;
    private int balance;

    public User(String username, String type, String country, int balance) {
        this.username = username;
        this.type = type;
        this.country = country;
        this.balance = balance;
        this.movies = new HashMap<>();
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

    public HashMap<String,String> getMovies() {
        return movies;
    }

    public int getBalance() {
        return balance;
    }

    public void setBalance(int balance) {
        this.balance = balance;
    }

    @Override
    public String toString() {
        return "User{" +
                "username='" + username + '\'' +
                ", type='" + type + '\'' +
                ", country='" + country + '\'' +
                ", movies=" + movies +
                ", balance=" + balance +
                '}';
    }
}
