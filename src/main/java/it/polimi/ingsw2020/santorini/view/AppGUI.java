package it.polimi.ingsw2020.santorini.view;

import it.polimi.ingsw2020.santorini.model.Board;
import it.polimi.ingsw2020.santorini.model.Builder;
import it.polimi.ingsw2020.santorini.model.Player;
import it.polimi.ingsw2020.santorini.network.client.Client;
import it.polimi.ingsw2020.santorini.utils.*;
import it.polimi.ingsw2020.santorini.utils.messages.actions.*;
import it.polimi.ingsw2020.santorini.utils.messages.actions.AskMoveSelectionMessage;
import it.polimi.ingsw2020.santorini.utils.messages.errors.IllegalPositionMessage;
import it.polimi.ingsw2020.santorini.utils.messages.godsParam.*;
import it.polimi.ingsw2020.santorini.utils.messages.matchMessage.MatchSetupMessage;
import it.polimi.ingsw2020.santorini.utils.messages.matchMessage.MatchStateMessage;
import it.polimi.ingsw2020.santorini.utils.messages.matchMessage.NewMatchMessage;
import it.polimi.ingsw2020.santorini.utils.messages.matchMessage.UpdateMessage;
import it.polimi.ingsw2020.santorini.view.gui.*;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.Event;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;

import static it.polimi.ingsw2020.santorini.utils.PhaseType.*;

public class AppGUI extends Application implements ViewInterface{

    // TODO: settare le finestre non ridimensionabili
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
    private ApolloController apolloController;
    private AresController aresController;
    private ArtemisController artemisController;
    private AtlasController atlasController;
    private DemeterController demeterController;
    private HestiaController hestiaController;
    private MinotaurController minotaurController;
    private PoseidonController poseidonController;
    private PrometheusController prometheusController;

    private ArrayList<Player> players;
    private boolean infoMatchDisplay = true;

    private static ApolloParamMessage apolloParamMessage = new ApolloParamMessage();
    private static AresParamMessage aresParamMessage = new AresParamMessage();
    private static ArtemisParamMessage artemisParamMessage = new ArtemisParamMessage();
    private static AtlasParamMessage atlasParamMessage = new AtlasParamMessage();
    private static DemeterParamMessage demeterParamMessage = new DemeterParamMessage();
    private static HestiaParamMessage hestiaParamMessage = new HestiaParamMessage();
    private static MinotaurParamMessage minotaurParamMessage = new MinotaurParamMessage();
    private static PoseidonParamMessage poseidonParamMessage = new PoseidonParamMessage();
    private static PrometheusParamMessage prometheusParamMessage = new PrometheusParamMessage();
    private static String wantNewMatch;

    public static ApolloParamMessage getApolloParamMessage() {
        return apolloParamMessage;
    }

    public static AresParamMessage getAresParamMessage() {
        return aresParamMessage;
    }

    public static ArtemisParamMessage getArtemisParamMessage() {
        return artemisParamMessage;
    }

    public static AtlasParamMessage getAtlasParamMessage() {
        return atlasParamMessage;
    }

    public static DemeterParamMessage getDemeterParamMessage() {
        return demeterParamMessage;
    }

    public static HestiaParamMessage getHestiaParamMessage() {
        return hestiaParamMessage;
    }

    public static MinotaurParamMessage getMinotaurParamMessage() {
        return minotaurParamMessage;
    }

    public static PoseidonParamMessage getPoseidonParamMessage() {
        return poseidonParamMessage;
    }

    public static PrometheusParamMessage getPrometheusParamMessage() {
        return prometheusParamMessage;
    }

    public static void setWantNewMatch(String wantNewMatch) {
        AppGUI.wantNewMatch = wantNewMatch;
    }

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

    @Override
    public void start(Stage primaryStage) throws Exception {
        this.primaryStage = primaryStage;
        client = new Client();
        client.setView(this);
        displaySetupWindow(true);
    }

    /**
     * method in which it's asked to the client to insert server's IP, and after that the username, birth date and type of match(number of players)
     * metodo in cui si chiede l'iP del server, dopodichè di fanno inserire username, data di nascita e tipo di partita (numero di giocatori nella partita)
     * @param firstTime is true if it is the first time we call the method
     *                  is false if the username is unavailable, and ask the client a new username
     */
    @Override
    public void displaySetupWindow(boolean firstTime) {
        if(firstTime){
            Platform.runLater(() -> {
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
            });
        } else {
            Platform.runLater(()-> {
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
                primaryStage.setTitle("New Username");
                primaryStage.setScene(newUsername);
                primaryStage.show();
            });
        }
    }

    /**
     * method that display a Loading window to the client while the server waits other clients to join
     * metodo per intrattenere l'utente mentre aspettiamo altri utenti che vogliono giocare
     */
    @Override
    public void displayLoadingWindow(String message) {
        Platform.runLater(()-> {
            Parent root;
            Scene loadingScene;
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("/FXML/loadingWindow.fxml"));
            try {
                root = fxmlLoader.load();
                loadingScene = new Scene(root);
            } catch (IOException e) {
                root = null;
                loadingScene = new Scene(new Label("Graphical Resources not found. Fatal Error"));
            }
            primaryStage.setScene(loadingScene);
            primaryStage.show();
        });
    }

    /**
     * method that gives the welcome to the clients and distributes color of the builders and Gods'cards
     * metodo in cui si da il welcome alla partita, vengono assegnate le carte e i colori.
     * viene visualizzata una board semplificata per facilitare il posizionamento delle pedine
     */
    @Override
    public void displayMatchSetupWindow(MatchSetupMessage matchSetupMessage) {
        Platform.runLater(()-> {
            players = matchSetupMessage.getPlayers();
            if(matchSetupMessage.getPlayers().get(matchSetupMessage.getCurrentPlayerIndex()).getNickname().equals(client.getUsername())) {
                FXMLLoader fxmlLoader = new FXMLLoader();
                Parent root;
                Scene setUpScene;
                switch (players.size()) {
                    case (2):
                        fxmlLoader.setLocation(getClass().getResource("/FXML/GodSelectionWindow.fxml"));
                        break;
                    case (3):
                        fxmlLoader.setLocation(getClass().getResource("/FXML/GodSelectionWindow_3.fxml"));
                        break;
                }
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
                godSelectionController.initializeToolTip();
                primaryStage.setScene(setUpScene);
                primaryStage.show();
            } else {
                FXMLLoader fxmlLoader = new FXMLLoader();
                Parent root;
                Scene setUpScene;
                fxmlLoader.setLocation(getClass().getResource("/FXML/selectingGod.fxml"));
                try {
                    root = fxmlLoader.load();
                    setUpScene = new Scene(root);
                } catch (IOException e) {
                    root = null;
                    setUpScene = new Scene(new Label("Graphical Resources not found. Fatal Error"));
                    e.printStackTrace();
                }
                primaryStage.setScene(setUpScene);
                primaryStage.show();
                Message message = new Message(client.getUsername());
                message.buildSynchronizationMessage(SecondHeaderType.BEGIN_MATCH, null);
                client.getNetworkHandler().send(message);
            }


        });

    }

    /**
     * method that asks to the current player to choose which want he wants to his side
     * @param matchSetupMessage contains all the information needed to perform this choice
     */
    @Override
    public void displayGodSelectionWindow(MatchSetupMessage matchSetupMessage) {
        Platform.runLater(() -> {
            if(matchSetupMessage.getPlayers().get(matchSetupMessage.getCurrentPlayerIndex()).getNickname().equals(client.getUsername())) {
                FXMLLoader fxmlLoader = new FXMLLoader();
                Parent root;
                Scene scene;
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
            }else{
                FXMLLoader fxmlLoader = new FXMLLoader();
                Parent root;
                Scene setUpScene;
                fxmlLoader.setLocation(getClass().getResource("/FXML/selectionGod.fxml"));
                try {
                    root = fxmlLoader.load();
                    setUpScene = new Scene(root);
                } catch (IOException e) {
                    root = null;
                    setUpScene = new Scene(new Label("Graphical Resources not found. Fatal Error"));
                    e.printStackTrace();
                }
                primaryStage.setScene(setUpScene);
                primaryStage.show();
            }
        });
    }

    /**
     * the method aks to the current player to choose where he wants to place the builders in the board. This method will be called only
     * in the setup phase
     * metodo addetto alla selezione dei builder secondo l'ordine definito dal controller
     * @param turnPlayerMessage contains all the references to the current player, the match and the board
     */
    @Override
    public void displaySelectionBuilderWindow(MatchStateMessage turnPlayerMessage) {
        players = turnPlayerMessage.getPlayers();
        if(infoMatchDisplay) {
            Platform.runLater(() -> {
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
                    children = null;
                    scene = new Scene(new Label("Graphical Resources not found. Fatal Error"));
                }
                infoMatchController = fxmlLoader.getController();
                infoMatchController.setClient(client);
                infoMatchController.initializePlayers(players);
                primaryStage.setScene(scene);
                primaryStage.show();
            });
            try {
                Thread.sleep(5000);
            } catch (InterruptedException ignored) {}
        }
        infoMatchDisplay = false;
        Platform.runLater(()-> {
            Parent children;
            Scene scene;
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

    /**
     * the method will be called only if a player will insert wrong parameters in displaySelectionBuilderWindow, and will ask to the player
     * to insert them again.
     * @param message contains the username of the player that has made the mistake and also a boolean that indicates which builder has the
     * wrong coordinates
     */
    @Override
    public void displayNewSelectionBuilderWindow(IllegalPositionMessage message) {}

    /**
     * method that update the board every time that the model is modified, it does that by calling other methods
     * metodo che aggiorna la board ogni volta che viene fatta una mossa (modificato il model)
     * parametro un messaggio con scritte le informazioni sulla board.
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
     * method that shows board, builders, the textual interface and the first player to play
     * far visualizzare la board con le pedine e tutta l'interfaccia testuale e il primo giocatore che gioca
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
            updateMatchController.initializeBoard(message.getCells());
            updateMatchController.setText();
            primaryStage.setTitle("START TURN");
            primaryStage.setScene(scene);
            primaryStage.show();
            try {
                Thread.sleep(1500);
            } catch (InterruptedException ignored) {}
        });
    }

    /**
     * the method asks to the player if he wants to activate his god's power
     * @param question is a message that contains the name of the player (that will receive the question) and the name of the god
     */
    @Override
    public void displayWouldActivate(MatchStateMessage question) {
        Platform.runLater(()->{

            Stage powerStage = new Stage();
            powerStage.setOnCloseRequest(Event::consume);
            powerStage.initModality(Modality.APPLICATION_MODAL);
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
            activationPowerController.setStage(powerStage);
            powerStage.setScene(scene);
            powerStage.show();
        });
    }

    /**
     * the method calls a method linked to a specific God that will ask to the player to insert parameters needed to use the god
     * @param message is a message that contains the name of the player (that will receive the question) and the name of the god
     */
    //TODO: Tooltip per potere divinità
    @Override
    public void displayParametersSelection(MatchStateMessage message) {
        if(message.getCurrentPlayer().getNickname().equals(client.getUsername())) {
            String god = message.getCurrentPlayer().getDivinePower().getName();
            switch (god) {
                case "Apollo":
                    displayApolloParamSel(message);
                    break;
                case "Ares":
                    displayAresParamSel(message);
                    break;
                case "Artemis":
                    displayArtemisParamSel(message);
                    break;
                case "Atlas":
                    displayAtlasParamSel(message);
                    break;
                case "Demeter":
                    displayDemeterParamSel(message);
                    break;
                case "Hestia":
                    displayHestiaParamSel(message);
                    break;
                case "Minotaur":
                    displayMinotaurParamSel(message);
                    break;
                case "Poseidon":
                    displayPoseidonParamSel(message);
                    break;
                case "Prometheus":
                    displayPrometheusParamSel(message);
                    break;
                default:
                    break;
            }
        }
    }

    /**
     * the method prints to the players when and which god helped a player
     * @param updateMessage contains the name of the god used and the user name of the player that used it
     * @param phase is the phase in which the god helped the player
     */
    @Override
    public void displaySP(UpdateMessage updateMessage, PhaseType phase) {
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
            primaryStage.setTitle("SP UPDATE");
            primaryStage.setScene(scene);
            primaryStage.show();
        });
    }

    /**
     * the method asks to the current player which one of his builder he wants to move and build with.
     * To help the player in his choice the method also shows the board in its current state and it allows the player to select a builder only
     * if he or she can move
     * @param message contains the username of the player and
     */
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
     * the method asks to the player in which direction he wants to move the builder. The method shows a little matrix to represents the
     * allowed direction of movements. If the player inserts a wrong direction, he will be asked again to insert the direction
     * metodo che mostra all'utente le possibili mosse che il builder selezionato può fare
     */
    @Override
    public void displayPossibleMoves(AskMoveSelectionMessage message) {
        Platform.runLater(()->{
            Stage moveStage = new Stage();
            moveStage.setOnCloseRequest(Event::consume);
            moveStage.initModality(Modality.APPLICATION_MODAL);
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
            possibleMovesController.setStage(moveStage);
            moveStage.setScene(scene);
            moveStage.show();
        });

    }

    /**
     * the method asks to the player in which direction he wants to move the builder. The method shows a little matrix to represents the
     * allowed direction of movements. If the player inserts a wrong direction, he will be asked again to insert the direction
     * metodo che mostra all'utente le possibili mosse che il builder selezionato può fare
     */
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

    /**
     * the method asks to the player in which direction he wants to build. The method shows a little matrix to represents the
     * allowed direction to construct near his playing builder. If the player inserts a wrong direction, he will be asked again to insert the direction
     * metodo che mostra all'utente le possibili costruzioni che il builder mosso può fare
     */
    @Override
    public void displayPossibleBuildings(AskBuildSelectionMessage message) {
        Platform.runLater(()->{

            Stage buildStage = new Stage();
            buildStage.setOnCloseRequest(Event::consume);
            buildStage.initModality(Modality.APPLICATION_MODAL);
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
            possibleBuildingsController.setStage(buildStage);
            buildStage.setScene(scene);
            buildStage.show();
        });
    }

    /**
     * the method shows the board updated after the build phase
     * @param updateMessage is a message that contains the reference to the board, which is used to print the board itself
     */
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

    /**
     *the method prints a message to the players showing how the board has been modified during the turn and communicates the end of the turn
     * of the current player
     * @param updateMessage contains the the user name of current player and the reference to the board
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

    /**
     * method that shows winner. It then close the match or if the players wants to begin a new match
     * metodo che mostra vincitori e vinti. conclude la partita con epic sax guy
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
            } else {
                fxmlLoader.setLocation(getClass().getResource("/FXML/LoserWindow.fxml"));
                try {
                    children = fxmlLoader.load();
                    scene = new Scene(children);
                } catch (IOException e) {
                    children = null;
                    scene = new Scene(new Label("ERROR "));
                }
                endMatchController = fxmlLoader.getController();
                endMatchController.setClient(client);
                endMatchController.setText();
                primaryStage.setScene(scene);
                primaryStage.show();
                System.out.println("LOSER");
            }
            Stage stage = new Stage();
            stage.setOnCloseRequest(Event::consume);
            stage.initModality(Modality.APPLICATION_MODAL);
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
            askNewMatchController.setStage(stage);
            stage.setScene(scene);
            stage.showAndWait();

            FXMLLoader loader = new FXMLLoader();
            if (wantNewMatch.equals("YES")) {
                Stage selection = new Stage();
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
                newMatchController.setStage(selection);
                selection.setOnCloseRequest(Event::consume);
                selection.setScene(scene);
                selection.showAndWait();
                displayLoadingWindow(null);
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
                Message message = new Message(client.getUsername());
                message.buildNewMatchMessage(new NewMatchMessage(false, 0, null));
                client.getNetworkHandler().send(message);
            }
        });
    }

    /**
     * method that shows possible errors occurred
     * metodo che mostra all'utente possibili errori che sono capitati
     */
    @Override
    public void displayErrorMessage(String error) {}

    /**
     * the method asks to the current player to insert parameters need to use Apollo's power. These parameters are the choice of which builder
     * the player want to move (and swap with opponent's builder) and in which direction. If the builder selected cannot be moved, the method will choose for the player the other
     * builder. If the direction insert is not allowed the method wil ask to the player to insert it again. The method will also built the
     * message containing the parameters gathered
     *
     * @param message contains references about the board and the currents player(About the match ).
     * @return the message containing the parameters gathered.
     */
    @Override
    public ApolloParamMessage displayApolloParamSel(MatchStateMessage message) {
        Platform.runLater(() -> {
            FXMLLoader fxmlLoader = new FXMLLoader();
            Stage swapStage = new Stage();
            swapStage.setOnCloseRequest(Event::consume);
            swapStage.initModality(Modality.APPLICATION_MODAL);
            Parent root;
            Scene scene;
            fxmlLoader.setLocation(getClass().getResource("/FXML/askBuilder.fxml"));
            try {
                root = fxmlLoader.load();
                scene = new Scene(root);
            } catch (IOException e) {
                root = null;
                scene = new Scene(new Label("ERROR "));
            }
            apolloController = fxmlLoader.getController();
            apolloController.setStage(swapStage);
            apolloController.setMatchStateMessage(message);
            apolloController.initializeButtons();
            swapStage.setTitle("APOLLO POWER");
            swapStage.setScene(scene);
            swapStage.showAndWait();
            Builder chosen = apolloController.getChosen();

            Stage stage = new Stage();
            stage.setOnCloseRequest(Event::consume);
            stage.initModality(Modality.APPLICATION_MODAL);
            FXMLLoader loader = new FXMLLoader();
            Parent children;
            Scene swapScene;
            loader.setLocation(getClass().getResource("/FXML/apolloMatrix.fxml"));
            try {
                children = loader.load();
                swapScene = new Scene(children);
            } catch (IOException e) {
                root = null;
                swapScene = new Scene(new Label("ERROR "));
            }

            apolloController = loader.getController();
            apolloController.setStage(stage);
            apolloController.setMatchStateMessage(message);
            apolloController.initializeApolloMatrix(Board.neighboringSwappingCell(chosen, AccessType.OCCUPIED));
            stage.setScene(swapScene);
            stage.showAndWait();
            Message paramMessage = new Message(client.getUsername());
            paramMessage.buildApolloParamMessage(apolloParamMessage);
            client.getNetworkHandler().send(paramMessage);
        });
        return null;
    }

    /**
     * the method asks to the current player to insert parameters need to use  Ares's power. the Parameter asked is direction of the cell
     * where the demolition will occur
     * If the direction insert is not allowed the method wil ask to the player to insert it again. The method will also built the
     * message containing the parameters gathered
     *
     * @param message contains information about the match such as the current player
     * @return the message containing the parameter insert by the player
     */
    @Override
    public AresParamMessage displayAresParamSel(MatchStateMessage message) {
        Platform.runLater(()->{
            Stage stage = new Stage();
            stage.setOnCloseRequest(Event::consume);
            stage.initModality(Modality.APPLICATION_MODAL);
            FXMLLoader loader = new FXMLLoader();
            Parent children;
            Scene demolitionScene;
            loader.setLocation(getClass().getResource("/FXML/AresMatrix.fxml"));
            try {
                children = loader.load();
                demolitionScene = new Scene(children);
            } catch (IOException e) {
                children = null;
                demolitionScene = new Scene(new Label("ERROR "));
            }

            aresController = loader.getController();
            aresController.setStage(stage);
            aresController.setMatchStateMessage(message);
            aresController.initializeAresMatrix();
            stage.setScene(demolitionScene);
            stage.showAndWait();
            Message paramMessage = new Message(client.getUsername());
            paramMessage.buildAresParamMessage(aresParamMessage);
            client.getNetworkHandler().send(paramMessage);
        });
        return null;
    }

    /**
     * the method asks to the current player to insert parameters need to use Artemis's power. The Parameter asked is direction of the cell
     * where the player wants to moved again the builder in.
     * If the direction insert is not allowed the method wil ask to the player to insert it again. The method will also built the
     * message containing the parameters gathered
     * @param message contains information about the match such as the current player, information used for acquiring which is the playing
     * builder
     * @return the message containing the parameter acquired by the method
     */
    // TODO: verificare la condizione di attivazione di Artemis, anche sulla CLI -> GUI sicuramente sbagliato
    @Override
    public ArtemisParamMessage displayArtemisParamSel(MatchStateMessage message) {
        Platform.runLater(()->{
            Stage stage = new Stage();
            stage.setOnCloseRequest(Event::consume);
            stage.initModality(Modality.APPLICATION_MODAL);
            FXMLLoader loader = new FXMLLoader();
            Parent children;
            Scene moveScene;
            loader.setLocation(getClass().getResource("/FXML/ArtemisMatrix.fxml"));
            try {
                children = loader.load();
                moveScene = new Scene(children);
            } catch (IOException e) {
                children = null;
                moveScene = new Scene(new Label("ERROR "));
            }

            artemisController = loader.getController();
            artemisController.setStage(stage);
            artemisController.setMatchStateMessage(message);
            artemisController.initializeArtemisMatrix();
            stage.setScene(moveScene);
            stage.showAndWait();
            Message paramMessage = new Message(client.getUsername());
            paramMessage.buildArtemisParamMessage(artemisParamMessage);
            client.getNetworkHandler().send(paramMessage);
        });
        return null;
    }

    /**
     * the method asks to the current player to insert parameters need to use Atlas's power. the Parameter asked is direction of the cell
     * where the dome will be built
     * If the direction insert is not allowed the method wil ask to the player to insert it again. The method will also built the
     * message containing the parameters gathered
     *
     * @param message contains information about the match such as the current player, information used for acquiring which is the playing
     *                builder
     * @return the message containing the parameter acquired by the method
     */
    @Override
    public AtlasParamMessage displayAtlasParamSel(MatchStateMessage message) {
        Platform.runLater(()->{
            Stage stage = new Stage();
            stage.setOnCloseRequest(Event::consume);
            stage.initModality(Modality.APPLICATION_MODAL);
            FXMLLoader loader = new FXMLLoader();
            Parent children;
            Scene demolitionScene;
            loader.setLocation(getClass().getResource("/FXML/AtlasMatrix.fxml"));
            try {
                children = loader.load();
                demolitionScene = new Scene(children);
            } catch (IOException e) {
                children = null;
                demolitionScene = new Scene(new Label("ERROR "));
            }

            atlasController = loader.getController();
            atlasController.setStage(stage);
            atlasController.setMatchStateMessage(message);
            atlasController.initializeAtlasMatrix();
            stage.setScene(demolitionScene);
            stage.showAndWait();
            Message paramMessage = new Message(client.getUsername());
            paramMessage.buildAtlasParamMessage(atlasParamMessage);
            client.getNetworkHandler().send(paramMessage);
        });
        return null;
    }

    /**
     * The method asks to the current player to insert parameters need to use Demeter's power. The Parameter asked is direction of the cell
     * where the player wants to build again the builder in.
     * If the direction insert is not allowed the method wil ask to the player to insert it again. The method will also built the
     * message containing the parameters gathered
     *
     * @param message contains information about the match such as the current player, information used for acquiring which is the playing
     *                builder
     * @return the message containing the parameter acquired by the method
     */
    @Override
    public DemeterParamMessage displayDemeterParamSel(MatchStateMessage message) {
        Platform.runLater(()->{
            Stage stage = new Stage();
            stage.setOnCloseRequest(Event::consume);
            stage.initModality(Modality.APPLICATION_MODAL);
            FXMLLoader loader = new FXMLLoader();
            Parent children;
            Scene demolitionScene;
            loader.setLocation(getClass().getResource("/FXML/DemeterMatrix.fxml"));
            try {
                children = loader.load();
                demolitionScene = new Scene(children);
            } catch (IOException e) {
                children = null;
                demolitionScene = new Scene(new Label("ERROR "));
            }

            demeterController = loader.getController();
            demeterController.setStage(stage);
            demeterController.setMatchStateMessage(message);
            demeterController.initializeDemeterMatrix();
            stage.setScene(demolitionScene);
            stage.showAndWait();
            Message paramMessage = new Message(client.getUsername());
            paramMessage.buildDemeterParamMessage(demeterParamMessage);
            client.getNetworkHandler().send(paramMessage);
        });
        return null;
    }

    /**
     * the method asks to the current player to insert parameters need to use Hestia's power. The Parameter asked is direction of the cell
     * where the player wants to moved again the builder in.
     * If the direction insert is not allowed the method wil ask to the player to insert it again. The method will also built the
     * message containing the parameters gathered
     *
     * @param message contains information about the match such as the current player, information used for acquiring which is the playing
     *                builder
     * @return the message containing the parameter acquired by the method
     */
    @Override
    public HestiaParamMessage displayHestiaParamSel(MatchStateMessage message) {
        Platform.runLater(()->{
            Stage stage = new Stage();
            stage.setOnCloseRequest(Event::consume);
            stage.initModality(Modality.APPLICATION_MODAL);
            FXMLLoader loader = new FXMLLoader();
            Parent children;
            Scene demolitionScene;
            loader.setLocation(getClass().getResource("/FXML/HestiaMatrix.fxml"));
            try {
                children = loader.load();
                demolitionScene = new Scene(children);
            } catch (IOException e) {
                children = null;
                demolitionScene = new Scene(new Label("ERROR "));
            }

            hestiaController = loader.getController();
            hestiaController.setStage(stage);
            hestiaController.setMatchStateMessage(message);
            hestiaController.initializeHestiaMatrix();
            stage.setScene(demolitionScene);
            stage.showAndWait();
            Message paramMessage = new Message(client.getUsername());
            paramMessage.buildHestiaParamMessage(hestiaParamMessage);
            client.getNetworkHandler().send(paramMessage);
        });
        return null;
    }

    /**
     * the method asks to the current player to insert parameters need to use Minotaur's power. These parameters are che choice of which builder
     * the player want to move (and push opponent's builder) and in which direction. If the builder selected cannot be moved, the method will choose for the player the other
     * builder. If the direction insert is not allowed the method wil ask to the player to insert it again. The method will also built the
     * message containing the parameters gathered
     *
     * @param message contains references about the board and the currents player(About the match ).
     * @return the message containing the parameters gathered.
     */
    @Override
    public MinotaurParamMessage displayMinotaurParamSel(MatchStateMessage message) {
        Platform.runLater(()->{
            FXMLLoader fxmlLoader = new FXMLLoader();
            Stage swapStage = new Stage();
            swapStage.setOnCloseRequest(Event::consume);
            swapStage.initModality(Modality.APPLICATION_MODAL);
            Parent root;
            Scene scene;
            fxmlLoader.setLocation(getClass().getResource("/FXML/askPush.fxml"));
            try {
                root = fxmlLoader.load();
                scene = new Scene(root);
            } catch (IOException e) {
                root = null;
                scene = new Scene(new Label("ERROR "));
            }
            minotaurController = fxmlLoader.getController();
            minotaurController.setStage(swapStage);
            minotaurController.setMatchStateMessage(message);
            minotaurController.initializeButtons();
            swapStage.setTitle("MINOTAUR POWER");
            swapStage.setScene(scene);
            swapStage.showAndWait();
            Builder chosen = minotaurController.getChosen();

            Stage stage = new Stage();
            stage.setOnCloseRequest(Event::consume);
            stage.initModality(Modality.APPLICATION_MODAL);
            FXMLLoader loader = new FXMLLoader();
            Parent children;
            Scene pushScene;
            loader.setLocation(getClass().getResource("/FXML/MinotaurMatrix.fxml"));
            try {
                children = loader.load();
                pushScene = new Scene(children);
            } catch (IOException e) {
                children = null;
                pushScene = new Scene(new Label("ERROR "));
            }
            minotaurController = loader.getController();
            minotaurController.setStage(stage);
            minotaurController.setMatchStateMessage(message);
            minotaurController.initializeMinotaurMatrix(chosen, Board.neighboringSwappingCell(chosen, AccessType.OCCUPIED));
            stage.setScene(pushScene);
            stage.showAndWait();
            Message paramMessage = new Message(client.getUsername());
            paramMessage.buildMinotaurParamMessage(minotaurParamMessage);
            client.getNetworkHandler().send(paramMessage);
        });
        return null;
    }

    /**
     * The method asks to the current player to insert parameters need to use Poseidon's power. The Parameters asked are direction of the cell
     * where the player wants to build again the builder in and how many times he wants to build. The method will display the max and min numbers
     * of times he can build
     * If the direction insert is not allowed the method wil ask to the player to insert it again. The method will also built the
     * message containing the parameters gathered
     *
     * @param message contains information about the match such as the current player, information used for acquiring which is the playing
     *                builder
     * @return the message containing the parameter acquired by the method
     */
    @Override
    public PoseidonParamMessage displayPoseidonParamSel(MatchStateMessage message) {
        Platform.runLater(()->{
            FXMLLoader fxmlLoader = new FXMLLoader();
            Stage swapStage = new Stage();
            swapStage.setOnCloseRequest(Event::consume);
            swapStage.initModality(Modality.APPLICATION_MODAL);
            Parent root;
            Scene scene;
            fxmlLoader.setLocation(getClass().getResource("/FXML/askNumberBuildings.fxml"));
            try {
                root = fxmlLoader.load();
                scene = new Scene(root);
            } catch (IOException e) {
                root = null;
                scene = new Scene(new Label("ERROR "));
            }
            poseidonController = fxmlLoader.getController();
            poseidonController.setStage(swapStage);
            poseidonController.setMatchStateMessage(message);
            poseidonController.initializeButtons();
            swapStage.setTitle("POSEIDON POWER");
            swapStage.setScene(scene);
            swapStage.showAndWait();

            Stage stage = new Stage();
            stage.setOnCloseRequest(Event::consume);
            stage.initModality(Modality.APPLICATION_MODAL);
            FXMLLoader loader = new FXMLLoader();
            Parent children;
            Scene buildScene;
            loader.setLocation(getClass().getResource("/FXML/PoseidonMatrix.fxml"));
            try {
                children = loader.load();
                buildScene = new Scene(children);
            } catch (IOException e) {
                children = null;
                buildScene = new Scene(new Label("ERROR "));
            }

            poseidonController = loader.getController();
            poseidonController.setStage(stage);
            poseidonController.setMatchStateMessage(message);
            poseidonController.initializePoseidonMatrix();
            stage.setScene(buildScene);
            stage.showAndWait();
            Message paramMessage = new Message(client.getUsername());
            paramMessage.buildPoseidonParamMessage(poseidonParamMessage);
            client.getNetworkHandler().send(paramMessage);
        });
        return null;
    }

    /**
     * the method asks to the current player to insert parameters need to use Prometheus's power. These parameters are the choice of which builder
     * the player want to build (the choice of the builder will be memorized to be used for the next phase of movement and building)
     * and in which direction.
     * If the builder selected cannot be moved, the method will choose for the player the other
     * builder. If the direction insert is not allowed the method wil ask to the player to insert it again. The method will also built the
     * message containing the parameters gathered
     *
     * @param message contains references about the board and the currents player(About the match ).
     * @return the message containing the parameters gathered.
     */
    @Override
    public PrometheusParamMessage displayPrometheusParamSel(MatchStateMessage message) {
        Platform.runLater(()->{
            FXMLLoader fxmlLoader = new FXMLLoader();
            Stage swapStage = new Stage();
            swapStage.setOnCloseRequest(Event::consume);
            swapStage.initModality(Modality.APPLICATION_MODAL);
            Parent root;
            Scene scene;
            fxmlLoader.setLocation(getClass().getResource("/FXML/ask.fxml"));
            try {
                root = fxmlLoader.load();
                scene = new Scene(root);
            } catch (IOException e) {
                root = null;
                scene = new Scene(new Label("ERROR "));
            }
            prometheusController = fxmlLoader.getController();
            prometheusController.setStage(swapStage);
            prometheusController.setMatchStateMessage(message);
            prometheusController.initializeButtons();
            swapStage.setTitle("PROMETHEUS POWER");
            swapStage.setScene(scene);
            swapStage.showAndWait();
            Builder chosen = prometheusController.getChosen();

            Stage stage = new Stage();
            stage.setOnCloseRequest(Event::consume);
            stage.initModality(Modality.APPLICATION_MODAL);
            FXMLLoader loader = new FXMLLoader();
            Parent children;
            Scene demolitionScene;
            loader.setLocation(getClass().getResource("/FXML/PrometheusMatrix.fxml"));
            try {
                children = loader.load();
                demolitionScene = new Scene(children);
            } catch (IOException e) {
                children = null;
                demolitionScene = new Scene(new Label("ERROR "));
            }
            prometheusController = loader.getController();
            prometheusController.setStage(stage);
            prometheusController.setMatchStateMessage(message);
            prometheusController.initializePrometheusMatrix(Board.neighboringLevelCell(chosen));
            stage.setScene(demolitionScene);
            stage.showAndWait();
            Message paramMessage = new Message(client.getUsername());
            paramMessage.buildPrometheusParamMessage(prometheusParamMessage);
            client.getNetworkHandler().send(paramMessage);
        });
        return null;
    }

    /**
     * the method is used to obtain the ascii character that we use to represent the female and the male builders
     * @param gender is the char that contains the symbols used in the cli to represents the female and male builders
     * @return the ascii character needed
     */
    public static String gender(char gender){
        if(gender == '\u2642')
            return "✱";
        return "✿";
    }

    /**
     * the method gives the code corresponding to Colors
     * @param color is the color of which we want the code
     * @return the code as a String of the Color input
     */
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
    /*
    public void initializeGodsMatrix(ArrayList<Button> buttons, ArrayList<Label> labels){
        Button[][] matrixButton = new Button[3][3];
        Label[][] matrixLabel = new Label[3][3];
        for(int i = 0; i < 3; i++){
            for(int j = 0; j < 3; i++){

            }
        }
    }
    
     */
}

