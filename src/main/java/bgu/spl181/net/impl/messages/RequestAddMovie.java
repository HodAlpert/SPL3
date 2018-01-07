package bgu.spl181.net.impl.messages;

import java.util.LinkedList;
import java.util.List;

public class RequestAddMovie extends Request{

    private String movieName;
    private int amount;
    private int price;
    private List<String> bannedCountries;

    public RequestAddMovie(String message) {
        super(message);
        String[] s = getRequestBody().split("\"",3);
        this.movieName = s[1];

        s = s[2].split("\"",2);
        this.amount = Integer.parseInt(s[0].split(" ")[1]);
        this.price = Integer.parseInt(s[0].split(" ")[2]);

        this.bannedCountries = new LinkedList<>();
        if(s.length==2){
            String[] countries = s[1].split("\"");
            for(int i =0; i<countries.length;i++)
                if(!countries[i].equals("") && !countries[i].equals(""))
                    this.bannedCountries.add(countries[i]);
        }
    }

    public String getMovieName() {
        return movieName;
    }

    public int getAmount() {
        return amount;
    }

    public int getPrice() {
        return price;
    }

    public List<String> getBannedCountries() {
        return bannedCountries;
    }

    @Override
    public Message unpackMessage() {
        return null;
    }
}
