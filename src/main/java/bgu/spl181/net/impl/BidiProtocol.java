package bgu.spl181.net.impl;

import bgu.spl181.net.api.DataHandler;
import bgu.spl181.net.api.bidi.BidiMessagingProtocol;
import bgu.spl181.net.api.bidi.Connections;
import bgu.spl181.net.srv.ConnectionHandler;

import java.util.concurrent.atomic.AtomicBoolean;

public abstract class BidiProtocol<T> implements BidiMessagingProtocol<T>{
    private Connections connections;
    private int connectionId;
    private String userName;
    private AtomicBoolean terminate;
    private DataHandler service;

    public BidiProtocol() {
        this.terminate = new AtomicBoolean(false);
    }

    @Override
    public void start(int connectionId, Connections<T> connections, ConnectionHandler<T> handler, DataHandler service) {
        this.connectionId = connectionId;
        this.connections = connections;
        this.service = service;
        connections.activate(connectionId,handler);
        System.out.println("Client "+ connectionId+ ": connected");


    }

    @Override
    public void process(T message) {
        if(userName!=null)
            System.out.println(userName+ "> "+message);
        else
            System.out.println("Guest"+ connectionId+ "> "+message);
        String [] input=splitMessage(message);
        if(input[0].equals("REGISTER"))
            register(input);
        else if(input[0].equals("LOGIN"))
            login(input);
        else if(input[0].equals("REQUEST"))
            request(input);
        else if(message.equals("SIGNOUT"))
            signout(); //disconnected after signout
    }
    private void register(String [] input){
        if (input.length == 4) {
            if (!service.registerUser(input[1], input[2], input[3]))
                error("registration failed");
            else
                ack("registration succeeded");
        }
        else{
            if (!service.registerUser(input[1], input[2], null))
                error("registration failed");
            else
                ack("registration succeeded");
        }
    }

    private void error(String message){
        connections.send(this.connectionId,"ERROR "+message);
    }
    protected abstract void request(String[] input);

    private void signout(){
        if(userName==null) // if not logged in
            error("signout failed");
        else{
            userName=null;
            ack("signout succeeded");
            connections.disconnect(this.connectionId);
        }
    }
    private void login(String [] input){
        if(userName!=null||!service.loginValidation(input[1],input[2])) // cant login if already logged in
            error("login failed");
        else{
            connections.connect(this.connectionId); //connection to the server
            service.getConnectedUsers().add(input[1]);
            this.userName=input[1]; // connection as a user to the server
            ack("login succeeded");
        }
    }
    private void ack(String message){
        connections.send(this.connectionId,"ACK "+message);
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
