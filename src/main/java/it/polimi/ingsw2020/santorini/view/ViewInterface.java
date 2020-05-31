package it.polimi.ingsw2020.santorini.view;

import it.polimi.ingsw2020.santorini.model.Cell;
import it.polimi.ingsw2020.santorini.model.Player;
import it.polimi.ingsw2020.santorini.utils.PhaseType;
import it.polimi.ingsw2020.santorini.utils.messages.actions.AskBuildSelectionMessage;
import it.polimi.ingsw2020.santorini.utils.messages.actions.AskMoveSelectionMessage;
import it.polimi.ingsw2020.santorini.utils.messages.errors.IllegalPositionMessage;
import it.polimi.ingsw2020.santorini.utils.messages.godsParam.*;
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

    /**
     * the method asks to the current player to insert parameters need to use Apollo's power. These parameters are the choice of which builder
     * the player want to move (and swap with opponent's builder) and in which direction. If the builder selected cannot be moved, the method will choose for the player the other
     * builder. If the direction insert is not allowed the method wil ask to the player to insert it again. The method will also built the
     * message containing the parameters gathered
     * @param message contains references about the board and the currents player(About the match ).
     * @return the message containing the parameters gathered.
     */
    ApolloParamMessage displayApolloParamSel(MatchStateMessage message);


    /**
     * the method asks to the current player to insert parameters need to use  Ares's power. the Parameter asked is direction of the cell
     * where the demolition will occur
     * If the direction insert is not allowed the method wil ask to the player to insert it again. The method will also built the
     * message containing the parameters gathered
     * @param message contains information about the match such as the current player
     * @return the message containing the parameter insert by the player
     */
    AresParamMessage displayAresParamSel(MatchStateMessage message);


    /**
     * the method asks to the current player to insert parameters need to use Artemis's power. The Parameter asked is direction of the cell
     * where the player wants to moved again the builder in.
     * If the direction insert is not allowed the method wil ask to the player to insert it again. The method will also built the
     * message containing the parameters gathered
     * @param message contains information about the match such as the current player, information used for acquiring which is the playing
     * builder
     * @return the message containing the parameter acquired by the method
     */
    ArtemisParamMessage displayArtemisParamSel(MatchStateMessage message);


    /**
     * the method asks to the current player to insert parameters need to use Atlas's power. the Parameter asked is direction of the cell
     * where the dome will be built
     * If the direction insert is not allowed the method wil ask to the player to insert it again. The method will also built the
     * message containing the parameters gathered
     * @param message contains information about the match such as the current player, information used for acquiring which is the playing
     * builder
     * @return the message containing the parameter acquired by the method
     */
    AtlasParamMessage displayAtlasParamSel(MatchStateMessage message);


    /**
     * The method asks to the current player to insert parameters need to use Demeter's power. The Parameter asked is direction of the cell
     * where the player wants to build again the builder in.
     * If the direction insert is not allowed the method wil ask to the player to insert it again. The method will also built the
     * message containing the parameters gathered
     * @param message contains information about the match such as the current player, information used for acquiring which is the playing
     * builder
     * @return the message containing the parameter acquired by the method
     */
    DemeterParamMessage displayDemeterParamSel(MatchStateMessage message);


    /**
     * the method asks to the current player to insert parameters need to use Hestia's power. The Parameter asked is direction of the cell
     * where the player wants to moved again the builder in.
     * If the direction insert is not allowed the method wil ask to the player to insert it again. The method will also built the
     * message containing the parameters gathered
     * @param message contains information about the match such as the current player, information used for acquiring which is the playing
     * builder
     * @return the message containing the parameter acquired by the method
     */
    HestiaParamMessage displayHestiaParamSel(MatchStateMessage message);


    /**
     * the method asks to the current player to insert parameters need to use Minotaur's power. These parameters are che choice of which builder
     * the player want to move (and push opponent's builder) and in which direction. If the builder selected cannot be moved, the method will choose for the player the other
     * builder. If the direction insert is not allowed the method wil ask to the player to insert it again. The method will also built the
     * message containing the parameters gathered
     * @param message contains references about the board and the currents player(About the match ).
     * @return the message containing the parameters gathered.
     */
    MinotaurParamMessage displayMinotaurParamSel(MatchStateMessage message);


    /**
     * The method asks to the current player to insert parameters need to use Poseidon's power. The Parameters asked are direction of the cell
     * where the player wants to build again the builder in and how many times he wants to build. The method will display the max and min numbers
     * of times he can build
     * If the direction insert is not allowed the method wil ask to the player to insert it again. The method will also built the
     * message containing the parameters gathered
     * @param message contains information about the match such as the current player, information used for acquiring which is the playing
     * builder
     * @return the message containing the parameter acquired by the method
     */
    PoseidonParamMessage displayPoseidonParamSel(MatchStateMessage message);


    /**
     * the method asks to the current player to insert parameters need to use Prometheus's power. These parameters are the choice of which builder
     * the player want to build (the choice of the builder will be memorized to be used for the next phase of movement and building)
     * and in which direction.
     * If the builder selected cannot be moved, the method will choose for the player the other
     * builder. If the direction insert is not allowed the method wil ask to the player to insert it again. The method will also built the
     * message containing the parameters gathered
     * @param message contains references about the board and the currents player(About the match ).
     * @return the message containing the parameters gathered.
     */
    PrometheusParamMessage displayPrometheusParamSel(MatchStateMessage message);
}
