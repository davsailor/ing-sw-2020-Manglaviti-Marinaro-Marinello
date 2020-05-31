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
import it.polimi.ingsw2020.santorini.utils.messages.matchMessage.NewMatchMessage;
import it.polimi.ingsw2020.santorini.utils.messages.matchMessage.UpdateMessage;
import it.polimi.ingsw2020.santorini.view.gui.*;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;

import static it.polimi.ingsw2020.santorini.utils.PhaseType.*;

public class AppGUI extends Application implements ViewInterface{

    private Client client;
    private Stage primaryStage;
    private RegisterController registerController;
    private InfoMatchController infoMatchController;
    private SelectionBuilderController selectionBuilderController;
    private SelectGodController selectGodController;
    private GodSelectionController godSelectionController;
    private UpdateMatchController updateMatchController;
    private ActivationPowerController activationPowerController;
    private ChooseBuilderController chooseBuilderController;
    private PossibleMovesController possibleMovesController;
    private PossibleBuildingsController possibleBuildingsController;
    private EndMatchController endMatchController;
    private NewMatchController newMatchController;
    private AskNewMatchController askNewMatchController;
    private ArrayList<Player> players;
    private boolean infoMatchDisplay = true;


    public Client getClient() {
        return client;
    }
    public void setClient(Client client) {
        this.client = client;
    }

    private void closeSantorini(){
        primaryStage.close();
        System.exit(1);
    }
    /**
     * metodo in cui si chiede l'iP del server, dopodichè di fanno inserire username, data di nascita e tipo di partita (numero di giocatori nella partita)
     */
    @Override
    public void displaySetupWindow(boolean firstTime) {
        Platform.runLater(()-> {
            if (firstTime) {
                primaryStage.setOnCloseRequest(e -> closeSantorini());
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
                Stage alertBox = new Stage();
                alertBox.initModality(Modality.APPLICATION_MODAL);
                alertBox.setTitle("New Username");
                alertBox.setScene(newUsername);
                alertBox.show();
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
            if(matchSetupMessage.getPlayers().get(matchSetupMessage.getCurrentPlayerIndex()).getNickname().equals(client.getUsername())) {
                FXMLLoader fxmlLoader = new FXMLLoader();
                Parent root;
                Scene setUpScene;
                fxmlLoader.setLocation(getClass().getResource("/FXML/GodSelectionWindow.fxml"));
                try {
                    root = fxmlLoader.load();
                    setUpScene = new Scene(root);
                } catch (IOException e) {
                    root = null;
                    setUpScene = new Scene(new Label("Graphical Resources not found. Fatal Error"));
                    e.printStackTrace();
                }

                godSelectionController = fxmlLoader.getController();
                godSelectionController.setClient(client);
                godSelectionController.setMatchSetupMessage(matchSetupMessage);
                primaryStage.setScene(setUpScene);
                primaryStage.show();
            } else {
                Message message = new Message(client.getUsername());
                message.buildSynchronizationMessage(SecondHeaderType.BEGIN_MATCH, null);
                client.getNetworkHandler().send(message);
            }


        });

    }

    @Override
    public void displayGodSelectionWindow(MatchSetupMessage matchSetupMessage) {
        Platform.runLater(() -> {
            if(matchSetupMessage.getPlayers().get(matchSetupMessage.getCurrentPlayerIndex()).getNickname().equals(client.getUsername())) {
                FXMLLoader fxmlLoader = new FXMLLoader();
                Parent root;
                Scene scene;
                System.out.println(matchSetupMessage.getSelectedGods().size());
                switch (matchSetupMessage.getSelectedGods().size()) {
                    case (2):
                        fxmlLoader.setLocation(getClass().getResource("/FXML/selectGod.fxml"));
                        break;
                    case (3):
                        fxmlLoader.setLocation(getClass().getResource("/FXML/selectGod3.fxml"));
                        break;
                }
                try {
                    root = fxmlLoader.load();
                    scene = new Scene(root);
                } catch (IOException e) {
                    root = null;
                    scene = new Scene(new Label("Graphical Resources not found. Fatal Error"));
                    e.printStackTrace();
                }

                selectGodController = fxmlLoader.getController();
                selectGodController.setClient(client);
                selectGodController.initializeGods(matchSetupMessage.getSelectedGods());
                selectGodController.setMatchSetupMessage(matchSetupMessage);
                primaryStage.setScene(scene);
                primaryStage.show();
            }
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

            if(infoMatchDisplay) {
                players = turnPlayerMessage.getPlayers();

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
                    scene = new Scene(new Label("Graphical Resources not found. Fatal Error"));
                }
                infoMatchController = fxmlLoader.getController();
                infoMatchController.setClient(client);
                infoMatchController.initializePlayers(players);
                primaryStage.setScene(scene);
                primaryStage.show();
                System.out.println("per la miseria");
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();//TODO ricordiamo di cancellare sto print stack trace
                }
            }
            infoMatchDisplay = false;
            FXMLLoader loader = new FXMLLoader();
            switch (players.size()) {
                case (2):
                    loader.setLocation(getClass().getResource("/FXML/board.fxml"));
                    break;
                case (3):
                    loader.setLocation(getClass().getResource("/FXML/board_3.fxml"));
                    break;
            }

            try {
                children = loader.load();
                scene = new Scene(children);
            } catch (IOException e) {
                children = null;
                scene = new Scene(new Label("ERROR "));
            }
            selectionBuilderController = loader.getController();
            selectionBuilderController.setClient(client);
            selectionBuilderController.setMatchStateMessage(turnPlayerMessage);
            selectionBuilderController.initializePlayers(players);
            selectionBuilderController.initializeBoard(turnPlayerMessage.getBoard());
            selectionBuilderController.setText();
            primaryStage.setScene(scene);
            primaryStage.show();

        });
    }


    @Override
    public void displayNewSelectionBuilderWindow(IllegalPositionMessage message) {}

    /**
     * metodo che aggiorna la board ogni volta che viene fatta una mossa (modificato il model)
     * parametro un messaggio con scritte le informazioni sulla board.
     *
     * @param updateMessage
     */
    @Override
    public void updateMatch(UpdateMessage updateMessage) {

        switch(updateMessage.getPhase()) {
            case START_TURN:
                displayStartTurn(updateMessage);
                break;
            case STANDBY_PHASE_1:
                displaySP(updateMessage, PhaseType.STANDBY_PHASE_1);
                break;
            case MOVE_PHASE:
                displayMoveUpdate(updateMessage);
                break;
            case STANDBY_PHASE_2:
                displaySP(updateMessage, PhaseType.STANDBY_PHASE_2);
                break;
            case BUILD_PHASE:
                displayBuildUpdate(updateMessage);
                break;
            case STANDBY_PHASE_3:
                displaySP(updateMessage, STANDBY_PHASE_3);
                break;
            case END_TURN:
                displayEndTurn(updateMessage);
                break;
            default:
                break;
        }
    }

    /**
     * far visualizzare la board con le pedine e tutta l'interfaccia testuale e il primo giocatore che gioca
     *
     * @param message
     */
    @Override
    public void displayStartTurn(UpdateMessage message) {
        Platform.runLater(()->{
            Parent children;
            Scene scene;
            FXMLLoader fxmlLoader = new FXMLLoader();

            switch (players.size()) {
                case (2):
                    fxmlLoader.setLocation(getClass().getResource("/FXML/updateBoard.fxml"));
                    break;
                case (3):
                    fxmlLoader.setLocation(getClass().getResource("/FXML/updateBoard_3.fxml"));
                    break;
            }
            try {
                children = fxmlLoader.load();
                scene = new Scene(children);
            } catch (IOException e) {
                children = null;
                scene = new Scene(new Label("ERROR "));
            }
            updateMatchController = fxmlLoader.getController();
            updateMatchController.setClient(client);
            updateMatchController.setUpdateMessage(message);
            updateMatchController.initializePlayers(players);
            System.out.println(message.getCells()[0][0].getLevel());
            System.out.println(message.getCells()[0][0].getStatus());
            updateMatchController.initializeBoard(message.getCells());
            updateMatchController.setText();
            primaryStage.setTitle("START TURN");
            primaryStage.setScene(scene);
            primaryStage.show();
            try {
                Thread.sleep(1500);
            } catch (InterruptedException e) {
                e.printStackTrace();//TODO ricordiamo di cancellare sto print stack trace
            }

        });

    }

    @Override
    public void displaySP(UpdateMessage updateMessage, PhaseType phase) {

    }

    @Override
    public void displayMoveUpdate(UpdateMessage updateMessage) {
        Platform.runLater(()->{
            Parent children;
            Scene scene;
            FXMLLoader fxmlLoader = new FXMLLoader();
            switch (players.size()) {
                case (2):
                    fxmlLoader.setLocation(getClass().getResource("/FXML/updateBoard.fxml"));
                    break;
                case (3):
                    fxmlLoader.setLocation(getClass().getResource("/FXML/updateBoard_3.fxml"));
                    break;
            }
            try {
                children = fxmlLoader.load();
                scene = new Scene(children);
            } catch (IOException e) {
                children = null;
                scene = new Scene(new Label("ERROR "));
            }
            updateMatchController = fxmlLoader.getController();
            updateMatchController.setClient(client);
            updateMatchController.setUpdateMessage(updateMessage);
            updateMatchController.initializeBoard(updateMessage.getCells());
            updateMatchController.initializePlayers(players);
            updateMatchController.setText();
            primaryStage.setTitle("MOVE UPDATE");
            primaryStage.setScene(scene);
            primaryStage.show();

        });
    }

    @Override
    public void displayBuildUpdate(UpdateMessage updateMessage) {
        Platform.runLater(()->{
            Parent children;
            Scene scene;
            FXMLLoader fxmlLoader = new FXMLLoader();
            switch (players.size()) {
                case (2):
                    fxmlLoader.setLocation(getClass().getResource("/FXML/updateBoard.fxml"));
                    break;
                case (3):
                    fxmlLoader.setLocation(getClass().getResource("/FXML/updateBoard_3.fxml"));
                    break;
            }
            try {
                children = fxmlLoader.load();
                scene = new Scene(children);
            } catch (IOException e) {
                children = null;
                scene = new Scene(new Label("ERROR "));
            }
            updateMatchController = fxmlLoader.getController();
            updateMatchController.setClient(client);
            updateMatchController.setUpdateMessage(updateMessage);
            updateMatchController.initializeBoard(updateMessage.getCells());
            updateMatchController.initializePlayers(players);
            updateMatchController.setText();
            primaryStage.setTitle("BUILD UPDATE");
            primaryStage.setScene(scene);
            primaryStage.show();

        });
    }

    @Override
    public void displayParametersSelection(MatchStateMessage message) {

    }

    @Override
    public void displayChooseBuilder(MatchStateMessage message) {
        Platform.runLater(()->{
            Parent children;
            Scene scene;
            FXMLLoader fxmlLoader = new FXMLLoader();
            switch (players.size()) {
                case (2):
                    fxmlLoader.setLocation(getClass().getResource("/FXML/board_builder.fxml"));
                    break;
                case (3):
                    fxmlLoader.setLocation(getClass().getResource("/FXML/board_3_builder.fxml"));
                    break;
            }
            try {
                children = fxmlLoader.load();
                scene = new Scene(children);
            } catch (IOException e) {
                children = null;
                scene = new Scene(new Label("ERROR "));
            }
            chooseBuilderController = fxmlLoader.getController();
            chooseBuilderController.setClient(client);
            chooseBuilderController.setMatchStateMessage(message);
            chooseBuilderController.initializeBoard(message.getBoard());
            chooseBuilderController.initializePlayers(players);
            chooseBuilderController.setText();
            chooseBuilderController.initializeBuilder();
            primaryStage.setTitle("CHOOSE BUILDER");
            primaryStage.setScene(scene);
            primaryStage.show();
        });

    }

    /**
     * prova
     *
     * @param updateMessage parameter
     */
    @Override
    public void displayEndTurn(UpdateMessage updateMessage) {
        Platform.runLater(()->{
            Parent children;
            Scene scene;
            FXMLLoader fxmlLoader = new FXMLLoader();
            switch (players.size()) {
                case (2):
                    fxmlLoader.setLocation(getClass().getResource("/FXML/updateBoard.fxml"));
                    break;
                case (3):
                    fxmlLoader.setLocation(getClass().getResource("/FXML/updateBoard_3.fxml"));
                    break;
            }
            try {
                children = fxmlLoader.load();
                scene = new Scene(children);
            } catch (IOException e) {
                children = null;
                scene = new Scene(new Label("ERROR "));
            }
            updateMatchController = fxmlLoader.getController();
            updateMatchController.setClient(client);
            updateMatchController.setUpdateMessage(updateMessage);
            updateMatchController.initializeBoard(updateMessage.getCells());
            updateMatchController.initializePlayers(players);
            updateMatchController.setText();
            primaryStage.setTitle("END TURN");
            primaryStage.setScene(scene);
            primaryStage.show();

        });

    }

    @Override
    public void displayWouldActivate(MatchStateMessage question) {
        Platform.runLater(()->{

            Parent children;
            Scene scene;

            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("/FXML/activationPower.fxml"));

            try {
                children = fxmlLoader.load();
                scene = new Scene(children);
            } catch (IOException e) {
                children = null;
                scene = new Scene(new Label ("ERROR "));
            }
            activationPowerController = fxmlLoader.getController();
            activationPowerController.setMatchStateMessage(question);
            activationPowerController.setClient(client);
            activationPowerController.initializeText();
            primaryStage.setScene(scene);
            primaryStage.show();
        });
    }

    /**
     * metodo che mostra all'utente le possibili mosse che il builder selezionato può fare
     *
     * @param message
     */
    @Override
    public void displayPossibleMoves(AskMoveSelectionMessage message) {
        Platform.runLater(()->{

            Parent children;
            Scene scene;

            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("/FXML/directionMove.fxml"));

            try {
                children = fxmlLoader.load();
                scene = new Scene(children);
            } catch (IOException e) {
                children = null;
                scene = new Scene(new Label ("ERROR "));
            }
            possibleMovesController = fxmlLoader.getController();
            possibleMovesController.setAskMoveSelectionMessage(message);
            possibleMovesController.setClient(client);
            possibleMovesController.initializeBoard();
            possibleMovesController.setText();
            primaryStage.setScene(scene);
            primaryStage.show();
        });

    }

    /**
     * metodo che mostra all'utente le possibili costruzioni che il builder mosso può fare
     *
     * @param message
     */
    @Override
    public void displayPossibleBuildings(AskBuildSelectionMessage message) {
        Platform.runLater(()->{

            Parent children;
            Scene scene;

            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("/FXML/directionBuild.fxml"));

            try {
                children = fxmlLoader.load();
                scene = new Scene(children);
            } catch (IOException e) {
                children = null;
                scene = new Scene(new Label ("ERROR "));
            }
            possibleBuildingsController = fxmlLoader.getController();
            possibleBuildingsController.setAskBuildSelectionMessage(message);
            possibleBuildingsController.setClient(client);
            possibleBuildingsController.initializeBoard();
            possibleBuildingsController.setText();
            primaryStage.setScene(scene);
            primaryStage.show();
        });


    }

    /**
     * metodo che mostra vincitori e vinti. conclude la partita con epic sax guy
     *
     * @param winner
     */
    @Override
    public void displayEndMatch(String winner) {
        Platform.runLater(()->{

            Parent children;
            Scene scene;
            FXMLLoader fxmlLoader = new FXMLLoader();
            if(client.getUsername().equals(winner)) {

                fxmlLoader.setLocation(getClass().getResource("/FXML/WinnerWindow.fxml"));

                try {
                    children = fxmlLoader.load();
                    scene = new Scene(children);
                } catch (IOException e) {
                    children = null;
                    scene = new Scene(new Label("ERROR "));
                }
                endMatchController = fxmlLoader.getController();
                endMatchController.setClient(client);
                endMatchController.setWinner();
                primaryStage.setScene(scene);
                primaryStage.show();
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();//TODO ricordiamo di cancellare sto print stack trace
                }
            }else{
                fxmlLoader.setLocation(getClass().getResource("/FXML/LoserWindow.fxml"));

                try {
                    children = fxmlLoader.load();
                    scene = new Scene(children);
                } catch (IOException e) {
                    children = null;
                    scene = new Scene(new Label("ERROR "));
                }
                endMatchController.setClient(client);
                endMatchController.setText();
                primaryStage.setScene(scene);
                primaryStage.show();
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();//TODO ricordiamo di cancellare sto print stack trace
                }
            }
            FXMLLoader loader_new = new FXMLLoader();
            loader_new.setLocation(getClass().getResource("/FXML/AskNewMatch.fxml"));
            try {
                children = loader_new.load();
                scene = new Scene(children);
            } catch (IOException e) {
                children = null;
                scene = new Scene(new Label ("ERROR "));
            }
            askNewMatchController = loader_new.getController();
            primaryStage.setScene(scene);
            primaryStage.show();

            while (true) {
                if (askNewMatchController.getAnswer() == null) {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                } else {
                    break;
                }
            }
            FXMLLoader loader = new FXMLLoader();
            if (askNewMatchController.getAnswer().equals("YES")) {
                loader.setLocation(getClass().getResource("/FXML/newMatch.fxml"));
                try {
                    children = loader.load();
                    scene = new Scene(children);
                } catch (IOException e) {
                    children = null;
                    scene = new Scene(new Label("ERROR "));
                }
                newMatchController = loader.getController();
                newMatchController.setClient(client);
                primaryStage.setScene(scene);
                primaryStage.show();
            } else {
                loader.setLocation(getClass().getResource("/FXML/NoMatch.fxml"));
                try {
                    children = loader.load();
                    scene = new Scene(children);
                } catch (IOException e) {
                    children = null;
                    scene = new Scene(new Label("ERROR "));
                }
                primaryStage.setScene(scene);
                primaryStage.show();
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();//TODO ricordiamo di cancellare sto print stack trace
                }
                Message message = new Message(client.getUsername());
                message.buildNewMatchMessage(new NewMatchMessage(false, 0, null));
                client.getNetworkHandler().send(message);
                primaryStage.close();
            }


        });
    }


    /**
     * metodo che mostra all'utente possibili errori che sono capitati
     *
     * @param error
     */
    @Override
    public void displayErrorMessage(String error) {}

    @Override
    public void showBoard(ArrayList<Cell> listOfCells, ArrayList<Player> players) {}


    @Override
    public void start(Stage primaryStage) throws Exception {
        this.primaryStage = primaryStage;
        client = new Client();
        client.setView(this);

        displaySetupWindow(true);

    }
    public static String gender(char gender){
        if(gender == '\u2642')
            return "✱";
        return "✿";
    }
    public static String color(Color color){
        switch(color){
            case PLAYER_CYAN:
                return "#00ffcc";
            case PLAYER_GREEN:
                return "#f44040";
            case PLAYER_PURPLE:
                return "#b57fb8";
        }
        return null;
    }
}
