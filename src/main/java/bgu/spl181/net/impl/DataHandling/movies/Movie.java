package bgu.spl181.net.impl.DataHandling.movies;

import java.util.List;

public class Movie {
    private String id;
    private String name;
    private String price,availableAmount,totalAmount;
    private List<String> bannedCountries;

    public Movie(String id, String name, int price, int totalAmount, List<String> bannedCountries) {
        this.id = id;
        this.name = name;
        this.price = String.valueOf(price);
        this.availableAmount = String.valueOf(totalAmount);
        this.totalAmount = String.valueOf(totalAmount);
        this.bannedCountries = bannedCountries;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getPrice() {
        return Integer.valueOf(price);
    }

    public List<String> getBannedCountries() {
        return bannedCountries;
    }

    public int getAvailableAmount() {
        return Integer.valueOf(availableAmount);
    }

    public int getTotalAmount() {
        return Integer.valueOf(totalAmount);
    }


    public void setPrice(int price) {
        this.price = String.valueOf(price);
    }

    public void setAvailableAmount(int availableAmount) {
        this.availableAmount = String.valueOf(availableAmount);
    }



    @Override
    public String toString() {
        return
                "id: " + id + '\n' +
                "name: " + name + '\n' +
                "price: " + price +'\n' +
                "availableAmount: " + availableAmount +'\n' +
                "totalAmount: " + totalAmount +'\n' +
                "bannedCountries: " + bannedCountries +'\n' +
                '}';
    }
}
