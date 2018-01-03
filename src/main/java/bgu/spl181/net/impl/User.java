package bgu.spl181.net.impl;

import java.util.ArrayList;

public class User {
    private String username;
    private String type;
    private String country;
    private ArrayList<MovieInUser> movies;
    private int balance;

    public User(String username, String type, String country, int balance) {
        this.username = username;
        this.type = type;
        this.country = country;
        this.balance = balance;
        this.movies = new ArrayList<>();
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
        return balance;
    }

    public void setBalance(int balance) {
        this.balance = balance;
    }

    @Override
    public String toString() {
        return "User{" +
                "username='" + username + '\n' +
                ", type='" + type + '\n' +
                ", country='" + country + '\n' +
                ", movies=" + movies +'\n' +
                ", balance=" + balance +'\n' +
                '}';
    }
}

