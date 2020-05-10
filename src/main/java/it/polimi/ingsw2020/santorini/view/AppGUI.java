package it.polimi.ingsw2020.santorini.view;

import it.polimi.ingsw2020.santorini.model.Cell;
import it.polimi.ingsw2020.santorini.network.client.Client;
import it.polimi.ingsw2020.santorini.utils.PhaseType;
import it.polimi.ingsw2020.santorini.utils.messages.actions.AskBuildSelectionMessage;
import it.polimi.ingsw2020.santorini.utils.messages.actions.AskMoveSelectionMessage;
import it.polimi.ingsw2020.santorini.utils.messages.errors.IllegalPositionMessage;
import it.polimi.ingsw2020.santorini.utils.messages.matchMessage.MatchSetupMessage;
import it.polimi.ingsw2020.santorini.utils.messages.matchMessage.MatchStateMessage;
import it.polimi.ingsw2020.santorini.utils.messages.matchMessage.UpdateMessage;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;

public class AppGUI extends Application implements ViewInterface {

    @FXML
    Label messageLabel;

    private Client client;

    @Override
    public void start(Stage primaryStage) throws IOException {
        client = new Client();
        client.setView(this);

        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("/FXML/sample.fxml"));
        Parent root = fxmlLoader.load();
        primaryStage.setTitle("sample");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }

    public void surpriseAction(ActionEvent actionEvent) {
        messageLabel.setText("La carne è TENERISSIMA");
    }

    /**
     * metodo in cui si chiede l'iP del server, dopodichè di fanno inserire username, data di nascita e tipo di partita (numero di giocatori nella partita)
     */
    @Override
    public void displaySetupWindow() {

    }

    @Override
    public void displayNewUsernameWindow() {

    }

    /**
     * metodo per intrattenere l'utente mentre aspettiamo altri utenti che vogliono giocare
     *
     * @param message
     */
    @Override
    public void displayLoadingWindow(String message) {

    }

    /**
     * metodo in cui si da il welcome alla partita, vengono assegnate le carte e i colori.
     * viene visualizzata una board semplificata per facilitare il posizionamento delle pedine
     *
     * @param matchSetupMessage
     */
    @Override
    public void displayMatchSetupWindow(MatchSetupMessage matchSetupMessage) {

    }

    /**
     * metodo addetto alla selezione dei builder secondo l'ordine definito dal controller
     *
     * @param matchStateMessage
     */
    @Override
    public void displaySelectionBuilderWindow(MatchStateMessage matchStateMessage) {

    }

    @Override
    public void displayNewSelectionBuilderWindow(IllegalPositionMessage message) {

    }

    /**
     * metodo che aggiorna la board ogni volta che viene fatta una mossa (modificato il model)
     * parametro un messaggio con scritte le informazioni sulla board.
     *
     * @param message
     */
    @Override
    public void updateMatch(UpdateMessage message) {

    }

    /**
     * far visualizzare la board con le pedine e tutta l'interfaccia testuale e il primo giocatore che gioca
     *
     * @param message
     */
    @Override
    public void displayStartTurn(UpdateMessage message) {

    }

    @Override
    public void displaySP(UpdateMessage updateMessage, PhaseType phase) {

    }

    @Override
    public void displayMoveUpdate(UpdateMessage updateMessage) {

    }

    @Override
    public void displayBuildUpdate(UpdateMessage updateMessage) {

    }

    @Override
    public void displayParametersSelection(MatchStateMessage message) {

    }

    @Override
    public void displayChooseBuilder(MatchStateMessage message) {

    }

    /**
     * prova
     *
     * @param updateMessage parameter
     */
    @Override
    public void displayEndTurn(UpdateMessage updateMessage) {

    }

    @Override
    public void displayWouldActivate(MatchStateMessage question) {

    }

    /**
     * metodo che mostra all'utente le possibili mosse che il builder selezionato può fare
     *
     * @param message
     */
    @Override
    public void displayPossibleMoves(AskMoveSelectionMessage message) {

    }

    /**
     * metodo che mostra all'utente le possibili costruzioni che il builder mosso può fare
     *
     * @param message
     */
    @Override
    public void displayPossibleBuildings(AskBuildSelectionMessage message) {

    }

    /**
     * metodo che mostra vincitori e vinti. conclude la partita con epic sax guy
     *
     * @param winner
     */
    @Override
    public void displayEndMatch(String winner) {

    }

    /**
     * metodo che mostra all'utente possibili errori che sono capitati
     *
     * @param error
     */
    @Override
    public void displayErrorMessage(String error) {

    }

    @Override
    public void showBoard(ArrayList<Cell> listOfCells) {

    }
}
