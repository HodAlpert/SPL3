/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bgu.spl181.net.api.bidi;

import bgu.spl181.net.api.DataHandler;
import bgu.spl181.net.srv.ConnectionHandler;

public interface BidiMessagingProtocol<T>  {

    void start(int connectionId, Connections<T> connections, ConnectionHandler<T> handler,DataHandler service);

    void process(T message);

    /**
     * @return true if the connection should be terminated
     */
    boolean shouldTerminate();
}