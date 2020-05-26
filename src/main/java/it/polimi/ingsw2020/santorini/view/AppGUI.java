package it.polimi.ingsw2020.santorini.view;

import it.polimi.ingsw2020.santorini.model.Cell;
import it.polimi.ingsw2020.santorini.model.Player;
import it.polimi.ingsw2020.santorini.network.client.Client;
import it.polimi.ingsw2020.santorini.utils.Color;
import it.polimi.ingsw2020.santorini.utils.Message;
import it.polimi.ingsw2020.santorini.utils.PhaseType;
import it.polimi.ingsw2020.santorini.utils.SecondHeaderType;
import it.polimi.ingsw2020.santorini.utils.messages.actions.*;
import it.polimi.ingsw2020.santorini.utils.messages.actions.AskMoveSelectionMessage;
import it.polimi.ingsw2020.santorini.utils.messages.errors.IllegalPositionMessage;
import it.polimi.ingsw2020.santorini.utils.messages.matchMessage.MatchSetupMessage;
import it.polimi.ingsw2020.santorini.utils.messages.matchMessage.MatchStateMessage;
import it.polimi.ingsw2020.santorini.utils.messages.matchMessage.UpdateMessage;
import it.polimi.ingsw2020.santorini.view.gui.*;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;

public class AppGUI extends Application implements ViewInterface{

    private Client client;
    private Stage primaryStage;
    private RegisterController registerController;
    private BoardController boardController;
    private InfoMatchController infoMatchController;
    private SelectionBuilderController selectionBuilderController;
    private Scene registerScene;
    private ArrayList<Player> players;

    public Client getClient() {
        return client;
    }
    public void setClient(Client client) {
        this.client = client;
    }


    /**
     * metodo in cui si chiede l'iP del server, dopodichè di fanno inserire username, data di nascita e tipo di partita (numero di giocatori nella partita)
     */
    @Override
    public void displaySetupWindow(boolean firstTime) {
        Platform.runLater(()-> {
            if (firstTime) {
                FXMLLoader fxmlLoader = new FXMLLoader();
                Parent root;
                Scene registerScene;
                fxmlLoader.setLocation(getClass().getResource("/FXML/Register.fxml"));
                try {
                    root = fxmlLoader.load();
                    registerScene = new Scene(root);
                } catch (IOException e) {
                    root = null;
                    registerScene = new Scene(new Label("Graphical Resources not found. Fatal Error"));
                }
                registerController = fxmlLoader.getController();
                registerController.setClient(client);
                primaryStage.setTitle("Santorini");
                primaryStage.setScene(registerScene);
                primaryStage.show();
            } else {
                FXMLLoader fxmlLoader = new FXMLLoader();
                NewUsernameController newUsernameController;
                Parent root;
                Scene newUsername;
                fxmlLoader.setLocation(getClass().getResource("/FXML/NewUsername.fxml"));
                try {
                    root = fxmlLoader.load();
                    newUsername = new Scene(root);
                } catch (IOException e) {
                    root = null;
                    newUsername = new Scene(new Label("Graphical Resources not found. Fatal Error"));
                }
                newUsernameController = fxmlLoader.getController();
                newUsernameController.setClient(client);
                primaryStage.setScene(newUsername);
                primaryStage.show();
            }
        });
    }


    /**
     * metodo per intrattenere l'utente mentre aspettiamo altri utenti che vogliono giocare
     *
     * @param message
     */
    @Override
    public void displayLoadingWindow(String message) {
        Platform.runLater(()-> {
            FXMLLoader fxmlLoader = new FXMLLoader();
            Parent root;
            Scene loadingScene;
            fxmlLoader.setLocation(getClass().getResource("/FXML/loadingWindow.fxml"));
            try {
                root = fxmlLoader.load();
                loadingScene = new Scene(root);
            } catch (IOException e) {
                root = null;
                loadingScene = new Scene(new Label("Graphical Resources not found. Fatal Error"));
                e.printStackTrace();
            }
            primaryStage.setScene(loadingScene);
            primaryStage.show();
        });
    }

    /**
     * metodo in cui si da il welcome alla partita, vengono assegnate le carte e i colori.
     * viene visualizzata una board semplificata per facilitare il posizionamento delle pedine
     *
     * @param matchSetupMessage
     */
    @Override
    public void displayMatchSetupWindow(MatchSetupMessage matchSetupMessage) {
        Platform.runLater(()-> {
            players = matchSetupMessage.getPlayers();
            Parent children;
            Scene scene;
            FXMLLoader fxmlLoader = new FXMLLoader();
            switch (players.size()) {
                case (2):
                    fxmlLoader.setLocation(getClass().getResource("/FXML/InfoMatch.fxml"));
                    break;
                case (3):
                    fxmlLoader.setLocation(getClass().getResource("/FXML/InfoMatch3.fxml"));
                    break;
            }

            try {
                children = fxmlLoader.load();
                scene = new Scene(children);
            } catch (IOException e) {
                e.printStackTrace();
                children = null;
                scene = new Scene(new Label ("Graphical Resources not found. Fatal Error"));
            }
            infoMatchController = fxmlLoader.getController();
            infoMatchController.setClient(client);
            infoMatchController.initializePlayers(players);
            primaryStage.setScene(scene);
            primaryStage.show();
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();//TODO ricordiamo di cancellare sto print stack trace
            }
            Message message = new Message(client.getUsername());
            message.buildSynchronizationMessage(SecondHeaderType.BEGIN_MATCH);
            client.getNetworkHandler().send(message);
        });

    }

    /**
     * metodo addetto alla selezione dei builder secondo l'ordine definito dal controller
     *
     * @param turnPlayerMessage
     */
    @Override
    public void displaySelectionBuilderWindow(MatchStateMessage turnPlayerMessage) {
        Platform.runLater(()-> {
            Parent children;
            Scene scene;

            FXMLLoader fxmlLoader = new FXMLLoader();

            switch (players.size()){
                case(2):
                    fxmlLoader.setLocation(getClass().getResource("/FXML/board.fxml"));
                    break;
                case(3):
                    fxmlLoader.setLocation(getClass().getResource("/FXML/board_3.fxml"));
                    break;
            }

            try {
                children = fxmlLoader.load();
                scene = new Scene(children);
            } catch (IOException e) {
                children = null;
                scene = new Scene(new Label ("ERROR "));
            }
            selectionBuilderController = fxmlLoader.getController();
            selectionBuilderController.setClient(client);
            selectionBuilderController.setMatchStateMessage(turnPlayerMessage);
            selectionBuilderController.initializePlayers(players);
            primaryStage.setScene(scene);
            primaryStage.show();
        });
    }


    @Override
    public void displayNewSelectionBuilderWindow(IllegalPositionMessage message) {
        Stage stage = new Stage();
        Parent children;
        Scene scene;

        FXMLLoader fxmlLoader = new FXMLLoader();
        switch (players.size()){
            case(2):
                fxmlLoader.setLocation(getClass().getResource("/FXML/board.fxml"));
                break;
            case(3):
                fxmlLoader.setLocation(getClass().getResource("/FXML/board_3.fxml"));
                break;
        }
        try {
            children = fxmlLoader.load();
            scene = new Scene(children);
        } catch (IOException e) {
            children = null;
            scene = new Scene(new Label("ERROR "));
        }

        stage.setTitle("POSITION SELECTED IS OCCUPIED. INSERT NEW COORDINATES");
        stage.setScene(scene);
        stage.show();
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
        Stage stage = new Stage();
        Parent children;
        Scene scene;

        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("/FXML/newUsername.fxml"));

        try {
            children = fxmlLoader.load();
            scene = new Scene(children);
        } catch (IOException e) {
            children = null;
            scene = new Scene(new Label ("ERROR "));
        }

        stage.setTitle("NOT VALID USERNAME");
        stage.setScene(scene);
        stage.show();
    }

    @Override
    public void displayBuildUpdate(UpdateMessage updateMessage) {
        Stage stage = new Stage();
        Parent children;
        Scene scene;

        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("/FXML/newUsername.fxml"));

        try {
            children = fxmlLoader.load();
            scene = new Scene(children);
        } catch (IOException e) {
            children = null;
            scene = new Scene(new Label ("ERROR "));
        }

        stage.setTitle("NOT VALID USERNAME");
        stage.setScene(scene);
        stage.show();
    }

    @Override
    public void displayParametersSelection(MatchStateMessage message) {

    }

    @Override
    public void displayChooseBuilder(MatchStateMessage message) {
        Stage stage = new Stage();
        Parent children;
        Scene scene;

        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("/FXML/newUsername.fxml"));

        try {
            children = fxmlLoader.load();
            scene = new Scene(children);
        } catch (IOException e) {
            children = null;
            scene = new Scene(new Label ("ERROR "));
        }

        stage.setTitle("NOT VALID USERNAME");
        stage.setScene(scene);
        stage.show();
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
        Stage stage = new Stage();
        Parent children;
        Scene scene;

        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("/FXML/newUsername.fxml"));

        try {
            children = fxmlLoader.load();
            scene = new Scene(children);
        } catch (IOException e) {
            children = null;
            scene = new Scene(new Label ("ERROR "));
        }

        stage.setTitle("NOT VALID USERNAME");
        stage.setScene(scene);
        stage.show();
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
    public void showBoard(ArrayList<Cell> listOfCells, ArrayList<Player> players) {

    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        this.primaryStage = primaryStage;
        client = new Client();
        client.setView(this);

        displaySetupWindow(true);
    }
}
