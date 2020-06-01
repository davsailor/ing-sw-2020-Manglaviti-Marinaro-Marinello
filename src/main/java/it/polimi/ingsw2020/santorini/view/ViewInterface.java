package it.polimi.ingsw2020.santorini.view;

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

    /**
     * method in which it's asked to the client to insert server's IP, and after that the username, birth date and type of match(number of players)
     * metodo in cui si chiede l'iP del server, dopodichè di fanno inserire username, data di nascita e tipo di partita (numero di giocatori nella partita)
     * @param firstTime is true if it is the first time we call the method
     *                  is false if the username is unavailable, and ask the client a new username
     */
    void displaySetupWindow(boolean firstTime);

    /**
     * method that display a Loading window to the client while the server waits other clients to join
     * metodo per intrattenere l'utente mentre aspettiamo altri utenti che vogliono giocare
     */
    void displayLoadingWindow(String message);

    /**
     * method that gives the welcome to the clients and distributes color of the builders and Gods'cards
     * metodo in cui si da il welcome alla partita, vengono assegnate le carte e i colori.
     * viene visualizzata una board semplificata per facilitare il posizionamento delle pedine
     */
    void displayMatchSetupWindow(MatchSetupMessage matchSetupMessage);

    /**
     * method that asks to the current player to choose which want he wants to his side
     * @param matchSetupMessage contains all the information needed to perform this choice
     */
    void displayGodSelectionWindow(MatchSetupMessage matchSetupMessage);

    /**
     * the method aks to the current player to choose where he wants to place the builders in the board. This method will be called only
     * in the setup phase
     * metodo addetto alla selezione dei builder secondo l'ordine definito dal controller
     * @param matchStateMessage contains all the references to the current player, the match and the board
     */
    void displaySelectionBuilderWindow(MatchStateMessage matchStateMessage);

    /**
     * the method will be called only if a player will insert wrong parameters in displaySelectionBuilderWindow, and will ask to the player
     * to insert them again.
     * @param message contains the username of the player that has made the mistake and also a boolean that indicates which builder has the
     * wrong coordinates
     */
    void displayNewSelectionBuilderWindow(IllegalPositionMessage message);

    /**
     * method that update the board every time that the model is modified, it does that by calling other methods
     * metodo che aggiorna la board ogni volta che viene fatta una mossa (modificato il model)
     * parametro un messaggio con scritte le informazioni sulla board.
     */
    void updateMatch(UpdateMessage message);

    /**
     * method that shows board, builders, the textual interface and the first player to play
     * far visualizzare la board con le pedine e tutta l'interfaccia testuale e il primo giocatore che gioca
     */
    void displayStartTurn(UpdateMessage message);

    /**
     * the method asks to the player if he wants to activate his god's power
     * @param question is a message that contains the name of the player (that will receive the question) and the name of the god
     */
    void displayWouldActivate(MatchStateMessage question);

    /**
     * the method calls a method linked to a specific God that will ask to the player to insert parameters needed to use the god
     * @param message is a message that contains the name of the player (that will receive the question) and the name of the god
     */
    void displayParametersSelection(MatchStateMessage message);

    /**
     * the method prints to the players when and which god helped a player
     * @param updateMessage contains the name of the god used and the user name of the player that used it
     * @param phase is the phase in which the god helped the player
     */
    void displaySP(UpdateMessage updateMessage, PhaseType phase);

    /**
     * the method asks to the current player which one of his builder he wants to move and build with.
     * To help the player in his choice the method also shows the board in its current state and it allows the player to select a builder only
     * if he or she can move
     * @param message contains the username of the player and
     */
    void displayChooseBuilder(MatchStateMessage message);

    /**
     * the method asks to the player in which direction he wants to move the builder. The method shows a little matrix to represents the
     * allowed direction of movements. If the player inserts a wrong direction, he will be asked again to insert the direction
     * metodo che mostra all'utente le possibili mosse che il builder selezionato può fare
     */
    void displayPossibleMoves(AskMoveSelectionMessage message);

    /**
     * the method shows to the current player the board after the movement.
     * @param updateMessage contains the username of current player and also a reference to the board(used to show the board to the player).
     */
    void displayMoveUpdate(UpdateMessage updateMessage);

    /**
     * the method asks to the player in which direction he wants to build. The method shows a little matrix to represents the
     * allowed direction to construct near his playing builder. If the player inserts a wrong direction, he will be asked again to insert the direction
     * metodo che mostra all'utente le possibili costruzioni che il builder mosso può fare
     */
    void displayPossibleBuildings(AskBuildSelectionMessage message);

    /**
     * the method shows the board updated after the build phase
     * @param updateMessage is a message that contains the reference to the board, which is used to print the board itself
     */
    void displayBuildUpdate(UpdateMessage updateMessage);

    /**
     *the method prints a message to the players showing how the board has been modified during the turn and communicates the end of the turn
     * of the current player
     * @param updateMessage contains the the user name of current player and the reference to the board
     */
    void displayEndTurn(UpdateMessage updateMessage);

    /**
     * method that shows winner. It then close the match or if the players wants to begin a new match
     * metodo che mostra vincitori e vinti. conclude la partita con epic sax guy
     * @param winner
     */
    void displayEndMatch(String winner);

    /**
     * method that shows possible errors occurred
     * metodo che mostra all'utente possibili errori che sono capitati
     */
    void displayErrorMessage(String error);

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
