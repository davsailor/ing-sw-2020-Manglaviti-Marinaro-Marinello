package it.polimi.ingsw2020.santorini.network;

import it.polimi.ingsw2020.santorini.utils.Message;

import java.io.IOException;

public interface NetworkInterface {
    /**
     * method that sends a message
     * @param message is the message that has to be sent
     * @throws IOException
     */
    void send(Message message) throws IOException;

    void receive(Message message);

    //potremmo aggiungere receive
}
