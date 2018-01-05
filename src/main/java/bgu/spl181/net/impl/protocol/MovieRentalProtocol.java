package bgu.spl181.net.impl;

import bgu.spl181.net.api.DataHandler;
import bgu.spl181.net.impl.messages.Message;

import java.util.LinkedList;
import java.util.List;

public class MovieRentalProtocol extends BidiProtocol<Message>{

    public MovieRentalProtocol(MovieRentalService service) {
        super(service);
    }

    @Override
    public void process(Message message) {

    }

//    @Override
//    protected void request(String[] input) {
//        if(userName==null) { // if not logged in
//            error("request " + input[1] + " failed");
//            return;
//        }
//        MovieRentalService MRS = (MovieRentalService)service;
//        if(input.length==3&&input[1].equals("balance")&&input[2].equals("info"))
//            ack("balance "+MRS.userBalanceInfo(userName));
//        else if(input.length==4&&input[1].equals("balance")&&input[2].equals("add")){
//            int amount=0;
//            try{
//                amount=Integer.parseInt(input[3]);
//            }
//            catch (NumberFormatException e){
//                error("request "+input[1]+" failed");
//                return;
//            }
//            ack("balance "+MRS.userAddBalance(userName,amount)+ "added "+input[3]);
//        }
//        else if(input[1].equals("info")) {
//            if(input.length==2)
//                ack("info "+MRS.movieInfo(null));
//            else{
//                String output = "";
//                for(int i=2; i<input.length; i++)
//                    output+=input[i];
//                String movieName =MRS.movieInfo(output.split("\"")[1]);
//                if(movieName==null)
//                    error("request "+input[1]+" failed");
//                else
//                    ack("info "+movieName);
//            }
//        }
//        else if(input.length>2 && input[1].equals("rent")) {
//            String output = "";
//            for(int i=2; i<input.length; i++)
//                output+=input[i];
//            String movieName =MRS.movieInfo(output.split("\"")[1]);
//            if(MRS.rentMovie(userName,movieName)){
//                ack("rent "+movieName+" success");
//                broadcast("movie "+MRS.movieInfo(movieName));
//            }
//            else
//                error("request "+input[1]+" failed");
//        }
//        else if(input.length>2 && input[1].equals("return")) {
//            String movieName = "";
//            for(int i=2; i<input.length; i++)
//                movieName+=input[i];
//            if(MRS.returnMovie(userName,movieName)){
//                ack("return "+movieName+" success");
//                broadcast("movie "+MRS.movieInfo(movieName));
//            }
//            else
//                error("request "+input[1]+" failed");
//        }
//        else if(input.length>4 && input[1].equals("addmovie")){
//            String message="";
//            for(int i=2;i<input.length;i++)
//                message += input[i]+" ";
//            String[] split = message.split("\"");
//            String movieName = split[1];
//
//            String[] amountPrice = split[2].split(" ");
//            int amount = Integer.parseInt(amountPrice[1]);
//            int price = Integer.parseInt(amountPrice[2]);
//
//            List<String> bannedCountries = new LinkedList<>();
//            for(int i=3;i<split.length;i++)
//                if(!split[i].equals(" "))
//                    bannedCountries.add(split[i]);
//
//            if(!MRS.addMovie(movieName,amount,price,bannedCountries))
//                error("request "+input[1]+" failed");
//            else{
//                ack("addmovie "+movieName+" success");
//                broadcast("movie "+MRS.movieInfo(movieName));
//            }
//
//        }
//        else if(input.length==3 && input[1].equals("remmovie")){
//            String movieName = input[2].split("\"")[1];
//            if(!MRS.removeMovie(movieName))
//                error("request "+input[1]+" failed");
//            else{
//                ack("remmovie "+movieName+" success");
//                broadcast("movie "+movieName+);
//            }
//        }
//
//    }
//
//    @Override
//    protected void register(String [] input){
//        if (input.length == 4) {
//            String country;
//            try {
//                country = input[3].split("\"")[1];
//            }catch(IndexOutOfBoundsException e){ //if input isn't REGISTER <username> <password> country=”<country name>”
//                error("registration failed");
//                return;
//            }
//            if (!service.registerUser(input[1], input[2], country))
//                error("registration failed");
//            else
//                ack("registration succeeded");
//        }
//        else
//            error("registration failed");
//
//    }
}
