package bgu.spl181.net.impl;

public class MovieRentalProtocol extends BidiProtocol{

    @Override
    protected void request(String[] input) {
        if(userName==null) { // if not logged in
            error("request " + input[1] + " failed");
            return;
        }
        MovieRentalService MRS = (MovieRentalService)service;
        if(input.length==3&&input[1].equals("balance")&&input[2].equals("info"))
            ack("balance "+MRS.userBalanceInfo(userName));
        else if(input.length==4&&input[1].equals("balance")&&input[2].equals("add")){
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
        else if(input[1].equals("info")) {
            if(input.length==2)
                ack("info "+MRS.movieInfo(null));
            else{
                String output = "";
                for(int i=2; i<input.length; i++)
                    output+=input[i];
                String movieName =MRS.movieInfo(output.split("\"")[1]);
                if(movieName==null)
                    error("request "+input[1]+" failed");
                else
                    ack("info "+movieName);
            }
        }
        else if(input.length>2 && input[1].equals("rent")) {
            String output = "";
            for(int i=2; i<input.length; i++)
                output+=input[i];
            String movieName =MRS.movieInfo(output.split("\"")[1]);
            if(MRS.rentMovie(userName,movieName)){
                ack("rent "+movieName+" success");
                broadcast("movie "+MRS.movieInfo(movieName));
            }
            else
                error("request "+input[1]+" failed");
        }
        else if(input.length>2 && input[1].equals("return")) {
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
        else if(input.length>4 && input[1].equals("addmovie")){

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
