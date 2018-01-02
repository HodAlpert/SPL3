package bgu.spl181.net.impl;

import java.util.List;

public class Movie {
    private String id;
    private String name;
    private int price,availableAmount,totalAmount;
    private List<String> bannedCountries;

    public Movie(String id, String name, int price, int availableAmount, int totalAmount, List<String> bannedCountries) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.availableAmount = availableAmount;
        this.totalAmount = totalAmount;
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

    public int getAvailableAmount() {
        return availableAmount;
    }

    public int getTotalAmount() {
        return totalAmount;
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

    public void setAvailableAmount(int availableAmount) {
        this.availableAmount = availableAmount;
    }

    public void setTotalAmount(int totalAmount) {
        this.totalAmount = totalAmount;
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
