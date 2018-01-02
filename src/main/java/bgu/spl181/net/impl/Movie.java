package bgu.spl181.net.impl;

import java.util.List;

public class Movie {
    private String id;
    private String name;
    private int price;
    private List<String> bannedCountries;

    public Movie(String id, String name, int price, List<String> bannedCountries) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.bannedCountries = bannedCountries;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getPrice() {
        return price;
    }

    public List<String> getBannedCountries() {
        return bannedCountries;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public void setBannedCountries(List<String> bannedCountries) {
        this.bannedCountries = bannedCountries;
    }

    @Override
    public String toString() {
        return "Movie{ " +
                "id= '" + id + '\'' +
                ", name= '" + name + '\'' +
                ", price= " + price +
                ", bannedCountries= " + bannedCountries +
                '}';
    }
}
