package it.polimi.ingsw2020.santorini.network;

import it.polimi.ingsw2020.santorini.utils.Message;

import java.io.IOException;

public interface NetworkInterface {
    /**
     * method that sends a message
     * @param message is the message that has to be sent
     * @throws IOException if the socket is not available
     */
    void send(Message message) throws IOException;

    /**
     * method that receive a message
     * @param message the message that has to be received
     */
    void receive(Message message);

    /**
     * method that check the connection sending ping messages
     */
    void checkConnection();
}
