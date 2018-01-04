package bgu.spl181.net.impl;

public class MovieRentalProtocol extends BidiProtocol{

    @Override
    protected void request(String[] input) {
        if(userName==null) // if not logged in
            error("request "+input[1]+" failed");
        MovieRentalService MRS = (MovieRentalService)service;
        if(input.length==3&&input[1].equals("balance")&&input[2].equals("info"))
            ack("balance "+MRS.userBalanceInfo(userName));
        if(input.length==4&&input[1].equals("balance")&&input[2].equals("add")){
            int amount=0;
            try{
                amount=Integer.parseInt(input[3]);
            }
            catch (NumberFormatException e){
                error("request "+input[1]+" failed");
                return;
            }
            ack("balance "+MRS.userAddBalance(userName,amount)+ "added "+input[3]);
        }
        if(input[1].equals("info")) {
            if(input.length==2)
                ack("info "+MRS.movieInfo(null));
            else{
                String movieName = "";
                for(int i=2; i<input.length; i++)
                    movieName+=input[i];
                String output =MRS.movieInfo(movieName.split("\"")[1]);
                if(output==null)
                    error("request "+input[1]+" failed");
                else
                    ack("info "+output);
            }
        }
        if(input.length>2 && input[1].equals("rent")) {
            String movieName = "";
            for(int i=2; i<input.length; i++)
                movieName+=input[i];
            if(MRS.rentMovie(userName,movieName)){
                ack("rent "+movieName+" success");
                broadcast("movie "+MRS.movieInfo(movieName));
            }
            else
                error("request "+input[1]+" failed");
        }
        if(input.length>2 && input[1].equals("return")) {
            String movieName = "";
            for(int i=2; i<input.length; i++)
                movieName+=input[i];
            if(MRS.returnMovie(userName,movieName)){
                ack("return "+movieName+" success");
                broadcast("movie "+MRS.movieInfo(movieName));
            }
            else
                error("request "+input[1]+" failed");
        }

    }

    @Override
    protected void register(String [] input){
        if (input.length == 4) {
            String country;
            try {
                country = input[3].split("\"")[1];
            }catch(IndexOutOfBoundsException e){ //if input isn't REGISTER <username> <password> country=”<country name>”
                error("registration failed");
                return;
            }
            if (!service.registerUser(input[1], input[2], country))
                error("registration failed");
            else
                ack("registration succeeded");
        }
        else
            error("registration failed");

    }
}
