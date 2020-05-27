package it.polimi.ingsw2020.santorini.view;

import it.polimi.ingsw2020.santorini.model.Cell;
import it.polimi.ingsw2020.santorini.model.Player;
import it.polimi.ingsw2020.santorini.utils.PhaseType;
import it.polimi.ingsw2020.santorini.utils.messages.actions.AskBuildSelectionMessage;
import it.polimi.ingsw2020.santorini.utils.messages.actions.AskMoveSelectionMessage;
import it.polimi.ingsw2020.santorini.utils.messages.errors.IllegalPositionMessage;
import it.polimi.ingsw2020.santorini.utils.messages.matchMessage.MatchSetupMessage;
import it.polimi.ingsw2020.santorini.utils.messages.matchMessage.MatchStateMessage;
import it.polimi.ingsw2020.santorini.utils.messages.matchMessage.UpdateMessage;

import java.util.ArrayList;

public interface ViewInterface {
    /*
     * interfaccia che standardizza i metodi di CLI e GUI
     */

    /**
     * metodo in cui si chiede l'iP del server, dopodichè di fanno inserire username, data di nascita e tipo di partita (numero di giocatori nella partita)
     */
    void displaySetupWindow(boolean firstTime);

    /**
     * metodo per intrattenere l'utente mentre aspettiamo altri utenti che vogliono giocare
     */
    void displayLoadingWindow(String message);

    /**
     * metodo in cui si da il welcome alla partita, vengono assegnate le carte e i colori.
     * viene visualizzata una board semplificata per facilitare il posizionamento delle pedine
     */
    void displayMatchSetupWindow(MatchSetupMessage matchSetupMessage);

    /**
     * metodo addetto alla selezione dei builder secondo l'ordine definito dal controller
     * @param matchStateMessage
     */
    void displaySelectionBuilderWindow(MatchStateMessage matchStateMessage);

    void displayNewSelectionBuilderWindow(IllegalPositionMessage message);

    /**
     * metodo che aggiorna la board ogni volta che viene fatta una mossa (modificato il model)
     * parametro un messaggio con scritte le informazioni sulla board.
     */
    void updateMatch(UpdateMessage message);

    /**
     * far visualizzare la board con le pedine e tutta l'interfaccia testuale e il primo giocatore che gioca
     */
    void displayStartTurn(UpdateMessage message);

    void displaySP(UpdateMessage updateMessage, PhaseType phase);

    void displayMoveUpdate(UpdateMessage updateMessage);

    void displayBuildUpdate(UpdateMessage updateMessage);

    void displayParametersSelection(MatchStateMessage message);

    void displayChooseBuilder(MatchStateMessage message);

    /**
     * prova
     * @param updateMessage parameter
     */
    void displayEndTurn(UpdateMessage updateMessage);

    void displayWouldActivate(MatchStateMessage question);

    /**
     * metodo che mostra all'utente le possibili mosse che il builder selezionato può fare
     */
    void displayPossibleMoves(AskMoveSelectionMessage message);

    /**
     * metodo che mostra all'utente le possibili costruzioni che il builder mosso può fare
     */
    void displayPossibleBuildings(AskBuildSelectionMessage message);

    /**
     * metodo che mostra vincitori e vinti. conclude la partita con epic sax guy
     * @param winner
     */
    void displayEndMatch(String winner);


    /**
     * metodo che mostra all'utente possibili errori che sono capitati
     */
    void displayErrorMessage(String error);

    void showBoard(ArrayList<Cell> listOfCells, ArrayList<Player> players);

    void displayGodSelectionWindow(MatchSetupMessage matchSetupMessage);

    // void displaySample();
    // void displaySample2();
}
