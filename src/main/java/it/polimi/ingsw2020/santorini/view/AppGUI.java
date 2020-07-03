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
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;

import static it.polimi.ingsw2020.santorini.utils.PhaseType.*;

public class AppGUI extends Application implements ViewInterface{
    private Client client;
    private Stage primaryStage;
    private ArrayList<Stage> modalStages = new ArrayList<>();
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
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        client = new Client();
        client.setView(this);
        displaySetupWindow(true);
    }

    private Scene loadScene(String path, FXMLLoader fxmlLoader){
        Scene scene;
        Parent root;
        fxmlLoader.setLocation(getClass().getResource(path));
        try {
            root = fxmlLoader.load();
            scene = new Scene(root);
        } catch (IOException e) {
            root = null;
            scene = new Scene(new Label("Graphical Resources not found. Fatal Error"));
        }
        scene.getStylesheets().add(getClass().getResource("/stylesheets/Font.css").toExternalForm());
        return scene;
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
                Scene registerScene = loadScene("/FXML/Register.fxml", fxmlLoader);
                registerController = fxmlLoader.getController();
                registerController.setClient(client);
                primaryStage.setTitle("Santorini");
                primaryStage.setScene(registerScene);
                primaryStage.setResizable(false);
                primaryStage.sizeToScene();
                primaryStage.show();
            });
        } else {
            Platform.runLater(()-> {
                FXMLLoader fxmlLoader = new FXMLLoader();
                NewUsernameController newUsernameController;
                Scene newUsername = loadScene("/FXML/NewUsername.fxml" ,fxmlLoader);
                newUsernameController = fxmlLoader.getController();
                newUsernameController.setClient(client);
                primaryStage.setTitle("Santorini - New Username");
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
            FXMLLoader fxmlLoader = new FXMLLoader();
            Scene loadingScene = loadScene("/FXML/loadingWindow.fxml", fxmlLoader);
            primaryStage.setScene(loadingScene);
            primaryStage.setTitle("Santorini - Loading");
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
                Scene setUpScene;
                switch (players.size()) {
                    case (2):
                        setUpScene = loadScene("/FXML/GodSelectionWindow.fxml", fxmlLoader);
                        break;
                    case (3):
                        setUpScene = loadScene("/FXML/GodSelectionWindow_3.fxml", fxmlLoader);
                        break;
                    default:
                        setUpScene = new Scene(new Label("Graphical Resources not found. Fatal Error"));
                }
                godSelectionController = fxmlLoader.getController();
                godSelectionController.setClient(client);
                godSelectionController.setMatchSetupMessage(matchSetupMessage);
                godSelectionController.initializeToolTip();
                primaryStage.setScene(setUpScene);
                primaryStage.setTitle("Santorini - God Selection");
                primaryStage.show();
            } else {
                FXMLLoader fxmlLoader = new FXMLLoader();
                Scene setUpScene = loadScene("/FXML/selectingGod.fxml", fxmlLoader);
                primaryStage.setScene(setUpScene);
                primaryStage.setTitle("Santorini - God Selection");
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
                Scene scene;
                switch (matchSetupMessage.getSelectedGods().size()) {
                    case (2):
                        scene = loadScene("/FXML/selectGod.fxml", fxmlLoader);
                        break;
                    case (3):
                        scene = loadScene("/FXML/selectGod3.fxml", fxmlLoader);
                        break;
                    default:
                        scene = new Scene(new Label("Graphical Resources not found. Fatal Error"));
                }
                selectGodController = fxmlLoader.getController();
                selectGodController.setClient(client);
                selectGodController.initializeGods(matchSetupMessage.getSelectedGods());
                selectGodController.setMatchSetupMessage(matchSetupMessage);
                primaryStage.setScene(scene);
                primaryStage.setTitle("Santorini - God Selection");
                primaryStage.show();
            }else{
                FXMLLoader fxmlLoader = new FXMLLoader();
                Scene setUpScene;
                setUpScene = loadScene("/FXML/selectionGod.fxml", fxmlLoader);
                primaryStage.setScene(setUpScene);
                primaryStage.setTitle("Santorini - God Selection");
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
                Scene scene;
                FXMLLoader fxmlLoader = new FXMLLoader();
                switch (players.size()) {
                    case (2):
                        scene = loadScene("/FXML/InfoMatch.fxml", fxmlLoader);
                        break;
                    case (3):
                        scene = loadScene("/FXML/InfoMatch3.fxml", fxmlLoader);
                        break;
                    default:
                        scene = new Scene(new Label("Graphical Resources not found. Fatal Error"));
                }
                infoMatchController = fxmlLoader.getController();
                infoMatchController.setClient(client);
                infoMatchController.initializePlayers(players);
                primaryStage.setScene(scene);
                primaryStage.setTitle("Santorini - Challengers");
                primaryStage.show();
            });
            try {
                Thread.sleep(5000);
            } catch (InterruptedException ignored) {}
        }
        infoMatchDisplay = false;
        Platform.runLater(()-> {
            Scene scene;
            FXMLLoader loader = new FXMLLoader();
            switch (players.size()) {
                case (2):
                    scene = loadScene("/FXML/board.fxml", loader);
                    break;
                case (3):
                    scene = loadScene("/FXML/board_3.fxml", loader);
                    break;
                default:
                    scene = new Scene(new Label("Graphical Resources not found. Fatal Error"));
            }
            selectionBuilderController = loader.getController();
            selectionBuilderController.setClient(client);
            selectionBuilderController.setMatchStateMessage(turnPlayerMessage);
            selectionBuilderController.initializePlayers(players);
            selectionBuilderController.initializeBoard(turnPlayerMessage.getBoard());
            selectionBuilderController.setText();
            primaryStage.setTitle("Santorini - Builder Setup");
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
            Scene scene;
            FXMLLoader fxmlLoader = new FXMLLoader();
            switch (players.size()) {
                case (2):
                    scene = loadScene("/FXML/updateBoard.fxml", fxmlLoader);
                    break;
                case (3):
                    scene = loadScene("/FXML/updateBoard_3.fxml", fxmlLoader);
                    break;
                default:
                    scene = new Scene(new Label("Graphical Resources not found. Fatal Error"));
            }
            updateMatchController = fxmlLoader.getController();
            updateMatchController.setClient(client);
            updateMatchController.setUpdateMessage(message);
            updateMatchController.initializePlayers(players);
            updateMatchController.initializeBoard(message.getCells());
            updateMatchController.setText();
            primaryStage.setTitle("Santorini - Start Turn");
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
            FXMLLoader fxmlLoader = new FXMLLoader();
            Scene scene = loadScene("/FXML/activationPower.fxml", fxmlLoader);
            activationPowerController = fxmlLoader.getController();
            activationPowerController.setMatchStateMessage(question);
            activationPowerController.setClient(client);
            activationPowerController.initializeText();
            activationPowerController.setStage(powerStage);
            showModalStage(powerStage, scene, "Santorini - God Power");
        });
    }

    /**
     * the method calls a method linked to a specific God that will ask to the player to insert parameters needed to use the god
     * @param message is a message that contains the name of the player (that will receive the question) and the name of the god
     */
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
            Scene scene;
            FXMLLoader fxmlLoader = new FXMLLoader();
            switch (players.size()) {
                case (2):
                    scene = loadScene("/FXML/updateBoard.fxml", fxmlLoader);
                    break;
                case (3):
                    scene = loadScene("/FXML/updateBoard_3.fxml", fxmlLoader);
                    break;
                default:
                    scene = new Scene(new Label("Graphical Resources not found. Fatal Error"));
            }
            updateMatchController = fxmlLoader.getController();
            updateMatchController.setClient(client);
            updateMatchController.setUpdateMessage(updateMessage);
            updateMatchController.initializeBoard(updateMessage.getCells());
            updateMatchController.initializePlayers(players);
            updateMatchController.setText();
            primaryStage.setTitle("Santorini - SP");
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
            Scene scene;
            FXMLLoader fxmlLoader = new FXMLLoader();
            switch (players.size()) {
                case (2):
                    scene = loadScene("/FXML/board_builder.fxml", fxmlLoader);
                    break;
                case (3):
                    scene = loadScene("/FXML/board_3_builder.fxml", fxmlLoader);
                    break;
                default:
                    scene = new Scene(new Label("Graphical Resources not found. Fatal Error"));
            }
            chooseBuilderController = fxmlLoader.getController();
            chooseBuilderController.setClient(client);
            chooseBuilderController.setMatchStateMessage(message);
            chooseBuilderController.initializeBoard(message.getBoard());
            chooseBuilderController.initializePlayers(players);
            chooseBuilderController.setText();
            chooseBuilderController.initializeBuilder();
            primaryStage.setTitle("Santorini - Choose Builder");
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
            Scene scene;
            FXMLLoader fxmlLoader = new FXMLLoader();
            scene = loadScene("/FXML/directionMove.fxml", fxmlLoader);
            possibleMovesController = fxmlLoader.getController();
            possibleMovesController.setAskMoveSelectionMessage(message);
            possibleMovesController.setClient(client);
            possibleMovesController.initializeBoard();
            possibleMovesController.setText();
            possibleMovesController.setStage(moveStage);
            showModalStage(moveStage, scene, "Santorini - Move Request");
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
            Scene scene;
            FXMLLoader fxmlLoader = new FXMLLoader();
            switch (players.size()) {
                case (2):
                    scene = loadScene("/FXML/updateBoard.fxml", fxmlLoader);
                    break;
                case (3):
                    scene = loadScene("/FXML/updateBoard_3.fxml", fxmlLoader);
                    break;
                default:
                    scene = new Scene(new Label("Graphical Resources not found. Fatal Error"));
            }
            updateMatchController = fxmlLoader.getController();
            updateMatchController.setClient(client);
            updateMatchController.setUpdateMessage(updateMessage);
            updateMatchController.initializeBoard(updateMessage.getCells());
            updateMatchController.initializePlayers(players);
            updateMatchController.setText();
            primaryStage.setTitle("Santorini - Move");
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
            FXMLLoader fxmlLoader = new FXMLLoader();
            Scene scene = loadScene("/FXML/directionBuild.fxml", fxmlLoader);
            possibleBuildingsController = fxmlLoader.getController();
            possibleBuildingsController.setAskBuildSelectionMessage(message);
            possibleBuildingsController.setClient(client);
            possibleBuildingsController.initializeBoard();
            possibleBuildingsController.setText();
            possibleBuildingsController.setStage(buildStage);
            showModalStage(buildStage, scene, "Santorini - Build Request");
        });
    }

    /**
     * the method shows the board updated after the build phase
     * @param updateMessage is a message that contains the reference to the board, which is used to print the board itself
     */
    @Override
    public void displayBuildUpdate(UpdateMessage updateMessage) {
        Platform.runLater(()->{
            Scene scene;
            FXMLLoader fxmlLoader = new FXMLLoader();
            switch (players.size()) {
                case (2):
                    scene = loadScene("/FXML/updateBoard.fxml", fxmlLoader);
                    break;
                case (3):
                    scene = loadScene("/FXML/updateBoard_3.fxml", fxmlLoader);
                    break;
                default:
                    scene = new Scene(new Label("Graphical Resources not found. Fatal Error"));
            }
            updateMatchController = fxmlLoader.getController();
            updateMatchController.setClient(client);
            updateMatchController.setUpdateMessage(updateMessage);
            updateMatchController.initializeBoard(updateMessage.getCells());
            updateMatchController.initializePlayers(players);
            updateMatchController.setText();
            primaryStage.setTitle("Santorini - Build");
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
            Scene scene;
            FXMLLoader fxmlLoader = new FXMLLoader();
            switch (players.size()) {
                case (2):
                    scene = loadScene("/FXML/updateBoard.fxml", fxmlLoader);
                    break;
                case (3):
                    scene = loadScene("/FXML/updateBoard_3.fxml", fxmlLoader);
                    break;
                default:
                    scene = new Scene(new Label("Graphical Resources not found. Fatal Error"));
            }
            updateMatchController = fxmlLoader.getController();
            updateMatchController.setClient(client);
            updateMatchController.setUpdateMessage(updateMessage);
            updateMatchController.initializeBoard(updateMessage.getCells());
            updateMatchController.initializePlayers(players);
            updateMatchController.setText();
            primaryStage.setTitle("Santorini - End Turn");
            primaryStage.setScene(scene);
            primaryStage.show();
        });
    }

    /**
     * method that shows winner. It then close the match or if the players wants to begin a new match
     * metodo che mostra vincitori e vinti. conclude la partita con epic sax guy
     * @param winner winner of the match
     */
    @Override
    public void displayEndMatch(String winner) {
        Platform.runLater(()-> {
            for(Stage s : modalStages)
                s.toBack();

            FXMLLoader fxmlLoader = new FXMLLoader();
            Scene scene;
            if(client.getUsername().equals(winner)) {
                scene = loadScene("/FXML/WinnerWindow.fxml", fxmlLoader);
                endMatchController = fxmlLoader.getController();
                endMatchController.setClient(client);
                endMatchController.setWinner();
            } else {
                scene = loadScene("/FXML/LoserWindow.fxml", fxmlLoader);
                endMatchController = fxmlLoader.getController();
                endMatchController.setClient(client);
                endMatchController.setText();
            }
            primaryStage.setScene(scene);
            primaryStage.setTitle("Santorini - End Match");
            primaryStage.show();

            try{
                Thread.sleep(1000);
            } catch (InterruptedException ignored) {}

            Stage stage = new Stage();
            FXMLLoader askNewMatchLoader = new FXMLLoader();
            scene = loadScene("/FXML/AskNewMatch.fxml", askNewMatchLoader);
            askNewMatchController = askNewMatchLoader.getController();
            askNewMatchController.setStage(stage);
            showModalStage(stage, scene, "Santorini - New Match");

            for(Stage s : modalStages)
                s.close();
            modalStages = new ArrayList<>();

            FXMLLoader newMatchLoader = new FXMLLoader();
            if (wantNewMatch.equals("YES")) {
                infoMatchDisplay = true;
                Stage selection = new Stage();
                scene = loadScene("/FXML/NewMatch.fxml", newMatchLoader);
                newMatchController = newMatchLoader.getController();
                newMatchController.setClient(client);
                newMatchController.setStage(selection);
                selection.setOnCloseRequest(Event::consume);
                selection.setScene(scene);
                selection.showAndWait();
                displayLoadingWindow(null);
            } else {
                scene = loadScene("/FXML/NoMatch.fxml", newMatchLoader);
                primaryStage.setScene(scene);
                primaryStage.setTitle("Santorini - Goodbye");
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
            Stage firstStage = new Stage();
            FXMLLoader firstLoader = new FXMLLoader();
            Scene firstScene = loadScene("/FXML/askBuilder.fxml", firstLoader);
            apolloController = firstLoader.getController();
            apolloController.setStage(firstStage);
            apolloController.setMatchStateMessage(message);
            apolloController.initializeButtons();
            showModalStage(firstStage, firstScene, "Santorini - Apollo Power");

            Builder chosen = apolloController.getChosen();
            Stage secondStage = new Stage();
            FXMLLoader secondLoader = new FXMLLoader();
            Scene secondScene = loadScene("/FXML/apolloMatrix.fxml", secondLoader);
            apolloController = secondLoader.getController();
            apolloController.setStage(secondStage);
            apolloController.setMatchStateMessage(message);
            apolloController.initializeApolloMatrix(Board.neighboringSwappingCell(chosen, AccessType.OCCUPIED));

            showModalStage(secondStage, secondScene, "Santorini - Apollo Power");
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
            Stage firstStage = new Stage();
            FXMLLoader firstLoader = new FXMLLoader();
            Scene firstScene = loadScene("/FXML/AresMatrix.fxml", firstLoader);
            aresController = firstLoader.getController();
            aresController.setStage(firstStage);
            aresController.setMatchStateMessage(message);
            aresController.initializeAresMatrix();
            showModalStage(firstStage, firstScene, "Santorini - Ares Power");

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
    @Override
    public ArtemisParamMessage displayArtemisParamSel(MatchStateMessage message) {
        Platform.runLater(()->{
            Stage firstStage = new Stage();
            FXMLLoader firstLoader = new FXMLLoader();
            Scene firstScene = loadScene("/FXML/ArtemisMatrix.fxml", firstLoader);
            artemisController = firstLoader.getController();
            artemisController.setStage(firstStage);
            artemisController.setMatchStateMessage(message);
            artemisController.initializeArtemisMatrix();
            showModalStage(firstStage, firstScene, "Santorini - Artemis Power");

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
            Stage firstStage = new Stage();
            FXMLLoader firstLoader = new FXMLLoader();
            Scene firstScene = loadScene("/FXML/AtlasMatrix.fxml", firstLoader);
            atlasController = firstLoader.getController();
            atlasController.setStage(firstStage);
            atlasController.setMatchStateMessage(message);
            atlasController.initializeAtlasMatrix();
            showModalStage(firstStage, firstScene, "Santorini - Atlas Power");

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
            Stage firstStage = new Stage();
            FXMLLoader firstLoader = new FXMLLoader();
            Scene firstScene = loadScene("/FXML/DemeterMatrix.fxml", firstLoader);
            demeterController = firstLoader.getController();
            demeterController.setStage(firstStage);
            demeterController.setMatchStateMessage(message);
            demeterController.initializeDemeterMatrix();
            showModalStage(firstStage, firstScene, "Santorini - Demeter Power");

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
            Stage firstStage = new Stage();
            FXMLLoader firstLoader = new FXMLLoader();
            Scene firstScene = loadScene("/FXML/HestiaMatrix.fxml", firstLoader);
            hestiaController = firstLoader.getController();
            hestiaController.setStage(firstStage);
            hestiaController.setMatchStateMessage(message);
            hestiaController.initializeHestiaMatrix();
            showModalStage(firstStage, firstScene, "Santorini - Hestia Power");

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
            Stage firstStage = new Stage();
            FXMLLoader firstLoader = new FXMLLoader();
            Scene firstScene = loadScene("/FXML/askPush.fxml", firstLoader);
            minotaurController = firstLoader.getController();
            minotaurController.setStage(firstStage);
            minotaurController.setMatchStateMessage(message);
            minotaurController.initializeButtons();
            showModalStage(firstStage, firstScene, "Santorini - Minotaur Power");

            Builder chosen = minotaurController.getChosen();
            Stage secondStage = new Stage();
            FXMLLoader secondLoader = new FXMLLoader();
            Scene secondScene = loadScene("/FXML/MinotaurMatrix.fxml", secondLoader);
            minotaurController = secondLoader.getController();
            minotaurController.setStage(secondStage);
            minotaurController.setMatchStateMessage(message);
            minotaurController.initializeMinotaurMatrix(chosen, Board.neighboringSwappingCell(chosen, AccessType.OCCUPIED));
            showModalStage(secondStage, secondScene, "Santorini - Minotaur Power");

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
            Stage firstStage = new Stage();
            FXMLLoader firstLoader = new FXMLLoader();
            Scene firstScene = loadScene("/FXML/askNumberBuildings.fxml", firstLoader);
            poseidonController = firstLoader.getController();
            poseidonController.setStage(firstStage);
            poseidonController.setMatchStateMessage(message);
            poseidonController.initializeButtons();
            showModalStage(firstStage, firstScene, "Santorini - Poseidon Power");

            Stage secondStage = new Stage();
            FXMLLoader secondLoader = new FXMLLoader();
            Scene secondScene = loadScene("/FXML/PoseidonMatrix.fxml", secondLoader);
            poseidonController = secondLoader.getController();
            poseidonController.setStage(secondStage);
            poseidonController.setMatchStateMessage(message);
            poseidonController.initializePoseidonMatrix();
            showModalStage(secondStage, secondScene, "Santorini - Poseidon Power");

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
            Stage firstStage = new Stage();
            FXMLLoader firstLoader = new FXMLLoader();
            Scene firstScene = loadScene("/FXML/askBuild.fxml", firstLoader);
            prometheusController = firstLoader.getController();
            prometheusController.setStage(firstStage);
            prometheusController.setMatchStateMessage(message);
            prometheusController.initializeButtons();
            showModalStage(firstStage, firstScene, "Santorini - Prometheus Power");

            Builder chosen = prometheusController.getChosen();
            Stage secondStage = new Stage();
            FXMLLoader secondLoader = new FXMLLoader();
            Scene secondScene = loadScene("/FXML/PrometheusMatrix.fxml", secondLoader);
            prometheusController = secondLoader.getController();
            prometheusController.setStage(secondStage);
            prometheusController.setMatchStateMessage(message);
            prometheusController.initializePrometheusMatrix(Board.neighboringLevelCell(chosen));
            showModalStage(secondStage, secondScene, "Santorini - Prometheus Power");

            Message paramMessage = new Message(client.getUsername());
            paramMessage.buildPrometheusParamMessage(prometheusParamMessage);
            client.getNetworkHandler().send(paramMessage);
        });
        return null;
    }

    /**
     * method that displays the stage that request a mandatory choice to the player
     * @param stage the stage that has to be shown
     * @param scene the scene of the stage
     * @param title the title of the stage
     */
    private void showModalStage(Stage stage, Scene scene, String title){
        modalStages.add(stage);
        stage.setOnCloseRequest(Event::consume);
        stage.setAlwaysOnTop(true);
        stage.setTitle(title);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.sizeToScene();
        stage.showAndWait();
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

    /**
     * common method to calculate the direction starting from the pressed button
     * @param actionEvent the pressed button
     * @param b00 North-West button
     * @param b01 North Button
     * @param b02 North-East button
     * @param b10 West Button
     * @param b12 East Button
     * @param b20 South-West Button
     * @param b21 South Button
     * @param b22 South-East Button
     * @return the direction pressed
     */
    public static Direction extractDirection(ActionEvent actionEvent, Button b00, Button b01, Button b02, Button b10, Button b12, Button b20, Button b21, Button b22) {
        Button pos = (Button) actionEvent.getSource();
        Direction direction = null;
        if(pos.equals(b00))
            direction = Direction.NORTH_WEST;
        else if (pos.equals(b01))
            direction = Direction.NORTH;
        else if (pos.equals(b02))
            direction = Direction.NORTH_EAST;
        else if (pos.equals(b10))
            direction = Direction.WEST;
        else if (pos.equals(b12))
            direction = Direction.EAST;
        else if (pos.equals(b20))
            direction = Direction.SOUTH_WEST;
        else if (pos.equals(b21))
            direction = Direction.SOUTH;
        else if (pos.equals(b22))
            direction = Direction.SOUTH_EAST;
        b00.setDisable(true);
        b01.setDisable(true);
        b02.setDisable(true);
        b10.setDisable(true);
        b12.setDisable(true);
        b20.setDisable(true);
        b21.setDisable(true);
        b22.setDisable(true);
        return direction;
    }

    /**
     * method to build matrices that collect buttons
     * @param matrix collector of buttons
     * @param b00 North-West Button
     * @param b01 North Button
     * @param b02 North-East Button
     * @param b10 West Button
     * @param b12 East Button
     * @param b20 South-West Button
     * @param b21 South Button
     * @param b22 South-East Button
     */
    public static void buildButtonMatrices(Button[][] matrix, Button b00, Button b01, Button b02, Button b10, Button b12, Button b20, Button b21, Button b22) {
        matrix[0][0] = b00;
        matrix[0][1] = b01;
        matrix[0][2] = b02;
        matrix[1][0] = b10;
        matrix[1][2] = b12;
        matrix[2][0] = b20;
        matrix[2][1] = b21;
        matrix[2][2] = b22;
    }

    /**
     * method to build matrices that collect labels
     * @param labelMatrix collector of labels
     * @param p00 North-West Label
     * @param p01 North Label
     * @param p02 North-East Label
     * @param p10 West Label
     * @param p12 East Label
     * @param p20 South-West Label
     * @param p21 South Label
     * @param p22 South-East Label
     */
    public static void buildLabelMatrices(Label[][] labelMatrix, Label p00, Label p01, Label p02, Label p10, Label p12, Label p20, Label p21, Label p22){
        labelMatrix[0][0] = p00;
        labelMatrix[0][1] = p01;
        labelMatrix[0][2] = p02;
        labelMatrix[1][0] = p10;
        labelMatrix[1][2] = p12;
        labelMatrix[2][0] = p20;
        labelMatrix[2][1] = p21;
        labelMatrix[2][2] = p22;
    }

    /**
     * method used to prepare the matrix that has to be shown in GUI Box
     * @param neighboringLevelCell the matrix of int, point of start to build the other two matrices
     * @param matrix the matrix of buttons
     * @param labelMatrix the matrix of labels
     */
    public static void printMatrix(int[][] neighboringLevelCell, Button[][] matrix, Label[][] labelMatrix) {
        for(int i=0 ; i<3 ;++i)
            for(int j=0; j<3 ; ++j)
                if (i != 1 || j != 1) {
                    if (neighboringLevelCell[i][j] < 0 || neighboringLevelCell[i][j] >= 4) {
                        matrix[i][j].setStyle("-fx-background-color: #ff0000");
                        matrix[i][j].setDisable(true);
                    }
                    labelMatrix[i][j].setText (neighboringLevelCell[i][j] == -1 ? " "  : String.valueOf(neighboringLevelCell[i][j]));
                }
    }
}