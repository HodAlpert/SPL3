package bgu.spl181.net.impl;

import bgu.spl181.net.api.DataHandler;
import bgu.spl181.net.api.bidi.BidiMessagingProtocol;
import bgu.spl181.net.api.bidi.Connections;
import bgu.spl181.net.srv.ConnectionHandler;

import java.util.concurrent.atomic.AtomicBoolean;

public abstract class BidiProtocol<T> implements BidiMessagingProtocol<T>{
    private Connections connections;
    private int connectionId;
    protected String userName;
    private AtomicBoolean terminate;
    protected DataHandler service;

    public BidiProtocol(DataHandler service) {
        this.service=service;
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
    public abstract void process(T message);
//    {
//        if(userName!=null)
//            System.out.println(userName+ "> "+message);
//        else
//            System.out.println("Guest"+ connectionId+ "> "+message);
//        String [] input=splitMessage(message);
//        if(input[0].equals("REGISTER"))
//            register(input);
//        else if(input[0].equals("LOGIN"))
//            login(input);
//        else if(input[0].equals("REQUEST"))
//            request(message);
//        else if(message.equals("SIGNOUT"))
//            signout(); //disconnected after signout
//    }

//    protected void broadcast(T message){
//        connections.broadcast("BROADCAST "+message);
//    }
//
//    protected void error(T message){
//        connections.send(this.connectionId,"ERROR "+message);
//    }
//
//    protected void request(T message){
////        error("request "+input[1]+" failed");
//    }
//    protected void register(T message){
//        if (input.length >= 3) {
//            String dataBlocks="";
//            for(int i=3; i<input.length;i++)
//                dataBlocks += input[i];
//
//            if (!service.registerUser(input[1], input[2], dataBlocks))
//                error("registration failed");
//            else
//                ack("registration succeeded");
//        }
//        else
//            error("registration failed");

//    }
//
//    private void signout(){
//        if(userName==null) // if not logged in
//            error("signout failed");
//        else{
//            service.signOut(userName);
//            userName=null;
//            ack("signout succeeded");
//            connections.disconnect(this.connectionId);
//        }
//    }
//    private void login(String [] input){
//        if(userName!=null||!service.loginValidation(input[1],input[2])) // cant login if already logged in
//            error("login failed");
//        else{
//            connections.connect(this.connectionId); //connection to the server
//            this.userName=input[1]; // connection as a user to the server
//            ack("login succeeded");
//        }
//    }
//    protected void ack(T message){
//        connections.send(this.connectionId,"ACK "+message);
//    }

    @Override
    public boolean shouldTerminate() {
        return terminate.get();
    }

//    private String[] splitMessage(T message){
//        if(!(message instanceof String))
//            throw new IllegalArgumentException("message must be of type String.");
//        return ((String)message).split(" ");
//    }

}
