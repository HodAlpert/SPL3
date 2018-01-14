package bgu.spl181.net.impl.protocol;

import bgu.spl181.net.impl.DataHandling.MovieRentalService;
import bgu.spl181.net.impl.messages.*;

public class MovieRentalProtocol extends BidiProtocol<Message>{

    public MovieRentalProtocol(MovieRentalService service) {
        super(service);
    }

    @Override
    public void process(Message message) {
        MovieRentalService MRS = (MovieRentalService) service;
            if(message instanceof Login){
                Login login = (Login) message;
                    if(this.userName==null && MRS.loginValidation(login.getUserName(),login.getPassword()) ){
                    this.userName = login.getUserName();
                    connections.connect(connectionId);
                    connections.send(this.connectionId,new Message("ACK login succeeded"));
                }
                else
                    connections.send(this.connectionId,new Message("ERROR login failed"));
            }

            else if(message instanceof Signout){
                if(this.userName!=null && MRS.signOut(userName)){
                    this.userName = null;
                    connections.send(this.connectionId, new Message("ACK signout succeeded"));
                    connections.disconnect(connectionId);
                    terminate.set(true);
                }
                else
                    connections.send(this.connectionId, new Message("ERROR signout failed"));
            }

            else if(message instanceof RegisterMRS){
                RegisterMRS register = (RegisterMRS) message;
                if(this.userName==null && MRS.registerUser(register.getUserName(),register.getPassword(),register.getCountry()))
                    connections.send(this.connectionId, new Message("ACK registration succeeded"));
                else
                    connections.send(this.connectionId, new Message("ERROR registration failed"));
            }

            else if(message instanceof RequestBalanceInfo){
                if(userName!=null)
                    connections.send(this.connectionId, new Message("ACK balance "+MRS.userBalanceInfo(userName)));
                else
                    connections.send(this.connectionId, new Message("ERROR request balance info failed"));
            }

            else if(message instanceof RequestBalanceAdd){
                RequestBalanceAdd balanceAdd = (RequestBalanceAdd) message;
                if(userName!=null && balanceAdd.getAmount()>0)
                    connections.send(this.connectionId, new Message("ACK balance "+MRS.userAddBalance(userName,balanceAdd.getAmount())+" added "+balanceAdd.getAmount()));
                else
                    connections.send(this.connectionId, new Message("ERROR request balance add failed"));
            }

            else if(message instanceof RequestInfo){
                RequestInfo info = (RequestInfo) message;
                String movieInfo = MRS.movieInfo(info.getMovieName());
                if(userName!=null && movieInfo!=null)
                    connections.send(this.connectionId, new Message("ACK info "+movieInfo));
                else
                    connections.send(this.connectionId, new Message("ERROR request info failed"));
            }

            else if(message instanceof RequestRent){
                if(userName!=null) {
                    RequestRent rent = (RequestRent) message;
                    String data = MRS.rentMovie(userName, rent.getMovieName());
                    if (data != null) {
                        connections.send(this.connectionId, new Message("ACK rent \"" + rent.getMovieName() + "\" success"));
                        broadcast(new Message("BROADCAST " + data));
                    } else
                        connections.send(this.connectionId, new Message("ERROR request rent failed"));
                }
                else
                    connections.send(this.connectionId, new Message("ERROR request rent failed"));
            }

            else if(message instanceof RequestReturn){
                if(userName!=null) {
                    RequestReturn returnMovie = (RequestReturn) message;
                    String data = MRS.returnMovie(userName, returnMovie.getMovieName());
                    if (userName != null && data != null) {
                        connections.send(this.connectionId, new Message("ACK return \" " + returnMovie.getMovieName() + "\" success"));
                        broadcast(new Message("BROADCAST " + data));
                    } else
                        connections.send(this.connectionId, new Message("ERROR request return failed"));
                }
                else
                    connections.send(this.connectionId, new Message("ERROR request return failed"));
            }

            else if(message instanceof RequestAddMovie){
                RequestAddMovie addMovie = (RequestAddMovie) message;
                if(userName!=null && MRS.addMovie(userName,addMovie.getMovieName(),addMovie.getAmount(),addMovie.getPrice(),addMovie.getBannedCountries())!=null){
                    connections.send(this.connectionId, new Message("ACK addmovie \"" + addMovie.getMovieName() + "\" success"));
                    broadcast(new Message("BROADCAST movie \"" + addMovie.getMovieName() + "\" "+addMovie.getAmount()+" "+addMovie.getPrice()));
                }
                else
                    connections.send(this.connectionId, new Message("ERROR request addmovie failed"));

            }

            else if(message instanceof RequestRemMovie){
                RequestRemMovie remMovie = (RequestRemMovie) message;
                if(userName!=null && MRS.removeMovie(userName,remMovie.getMovieName())){
                    connections.send(this.connectionId, new Message("ACK remmovie \"" + remMovie.getMovieName() + "\" success"));
                    broadcast(new Message("BROADCAST movie \"" + remMovie.getMovieName() + "\" removed"));
                }
                else
                    connections.send(this.connectionId, new Message("ERROR request remmovie failed"));
            }

            else if(message instanceof RequestChangePrice){
                RequestChangePrice changePrice = (RequestChangePrice) message;
                if(userName!=null) {
                    String data = MRS.ChangeMoviePrice(userName, changePrice.getMovieName(), changePrice.getPrice());
                    if (data != null) {
                        connections.send(this.connectionId, new Message("ACK changeprice \" " + changePrice.getMovieName() + "\" success"));
                        broadcast(new Message("BROADCAST " + data));
                    } else
                        connections.send(this.connectionId, new Message("ERROR request changeprice failed"));
                }
                else
                    connections.send(this.connectionId, new Message("ERROR request changeprice failed"));

            }
    }

    /**
     * sends a message to all logged in users
     * @param message
     */
    private void broadcast(Message message){
        for(Object clientId :((ServerConnections)connections).getMap().keySet())
            if((connections.isloggedIn((Integer)clientId)))
                connections.send((Integer)clientId,message);

    }

}
