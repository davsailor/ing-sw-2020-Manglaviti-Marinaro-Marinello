package it.polimi.ingsw2020.santorini.network;

import it.polimi.ingsw2020.santorini.utils.Message;

import java.io.IOException;

public interface NetworkInterface {
    //parametro è il messaggio da inviare
    void send(Message message) throws IOException;

    //potremmo aggiungere receive
}
