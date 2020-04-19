package it.polimi.ingsw2020.santorini.view;

import it.polimi.ingsw2020.santorini.utils.Message;

public interface ViewInterface {
    /**
     * interfaccia che standardizza i metodi di CLI e GUI
     */

    // metodi sprovvisti di parametri perchè ancora non sappiamo cosa mettere

    /**
     * metodo in cui si chiede l'iP del server, dopodichè di fanno inserire username, data di nascita e tipo di partita (numero di giocatori nella partita)
     */
    void displaySetupWindow();

    void displayNewUsernameWindow();
    /**
     * metodo per intrattenere l'utente mentre aspettiamo altri carusi che vogliono giocare
     */
    void displayLoadingWindow();

    /**
     * metodo in cui si da il welcome alla partita, vengono assegnate le carte e i colori.
     * viene visualizzata una board semplificata per facilitare il posizionamento delle pedine
     */
    void displayChoices();

    /**
     * far visualizzare la board con le pedine e tutta l'interfaccia testuale e il primo giocatore che gioca
     */
    void displayMatchStart();

    /**
     * metodo che aggiorna la board ogni volta che viene fatta una mossa (modificato il model)
     * parametro un messaggio con scritte le informazioni sulla board.
     */
    void updateMatch();

    /**
     * metodo che mostra all'utente le possibili mosse che il builder selezionato può fare
     */
    void displayPossibleMoves();

    /**
     * metodo che mostra all'utente le possibili costruzioni che il builder mosso può fare
     */
    void displayPossibleBuildings();

    /**
     * metodo che mostra vincitori e vinti. conclude la partita con epic sax guy
     */
    void displayEndMatch();

    /**
     * metodo che mostra all'utente possibili errori che sono capitati
     */
    void displayErrorMessage(String error);

    // void displaySample();
    // void displaySample2();
}
