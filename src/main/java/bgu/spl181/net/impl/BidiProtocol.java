package bgu.spl181.net.impl;

import bgu.spl181.net.api.DataHandler;
import bgu.spl181.net.api.bidi.BidiMessagingProtocol;
import bgu.spl181.net.api.bidi.Connections;
import bgu.spl181.net.srv.ConnectionHandler;

import java.util.concurrent.atomic.AtomicBoolean;

public class BidiProtocol<T> implements BidiMessagingProtocol<T>{
    private Connections connections;
    private int connectionId;
    private AtomicBoolean terminate;
    private DataHandler service;

    public BidiProtocol() {
        this.terminate = new AtomicBoolean(false);
    }

    @Override
    public void start(int connectionId, Connections<T> connections, ConnectionHandler<T> handler) {
        this.connectionId = connectionId;
        this.connections = connections;
        connections.activate(connectionId,handler);
        System.out.println("Client "+ connectionId+ ": connected");


    }

    @Override
    public void process(T message) {
        DataHandler service = MovieRentalService.getInstance();
//        service.registerUser("hod","246315", "israel");
        System.out.println("Client "+ connectionId+ ":"+message);
        connections.send(this.connectionId,message);
        System.out.println("Client"+ connectionId+ "> "+message);
        //connections.send(this.connectionId,message);
        String [] input=splitMessage(message);
        if(input[0].equals("REGISTER")){
            if(service.registerUser(input[1],input[2],input[3]))

            error("registration failed");
        }
        if (message.equals("bye")){
            connections.disconnect(connectionId);
            System.out.println("Client "+ connectionId+ "> disconnected");
        }
    }
    private void error(String message){
        connections.send(this.connectionId,"ERROR "+message);
    }
    private void login(String [] input){

    }
    private void ack(String [] input){

    }


    @Override
    public boolean shouldTerminate() {
        return terminate.get();
    }

    private String[] splitMessage(T message){
        if(!(message instanceof String))
            throw new IllegalArgumentException("message must be of type String.");
        return ((String)message).split(" ");
    }

}
