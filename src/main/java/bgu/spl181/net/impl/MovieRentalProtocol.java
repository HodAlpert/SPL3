package bgu.spl181.net.impl;

public class MovieRentalProtocol extends BidiProtocol{

    @Override
    protected void request(String[] input) {

    }

    @Override
    protected void register(String [] input){
        if (input.length == 4) {
            String country = input[3].split("\"")[1];
            if (!service.registerUser(input[1], input[2], country))
                error("registration failed");
            else
                ack("registration succeeded");
        }
        else
            error("registration failed");

    }
}
