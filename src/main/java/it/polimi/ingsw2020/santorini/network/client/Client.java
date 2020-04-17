package it.polimi.ingsw2020.santorini.network.client;

import it.polimi.ingsw2020.santorini.view.CLI;

import java.util.Date;

public class Client {
    /**
     * classe client che farà partire la cli, saprà gestire i messaggi ecc...
     */

    // vogliamo tenere la view come parametro del client? probabilmente si
    // vogliamo tenere l'ip del server? probabilmente si
    // input e output stream potrebbero servire? probabilmente si per comunicare i comandi
    private String username;
    private Date birthDate;

    public static void main(String[] args) {
        Client client = new Client();
        ServerAdapter adapter = new ServerAdapter(client, "127.0.0.1");
        adapter.start();

        //CLI view = new CLI(client);
        //view.start();
    }

    // metodo per gestire i messaggi lo mettiamo qua o in server adapter?
}
