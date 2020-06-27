package it.polimi.ingsw2020.santorini.view.gui;

import it.polimi.ingsw2020.santorini.network.client.Client;
import it.polimi.ingsw2020.santorini.utils.Direction;
import it.polimi.ingsw2020.santorini.utils.Message;
import it.polimi.ingsw2020.santorini.utils.messages.actions.AskBuildSelectionMessage;
import it.polimi.ingsw2020.santorini.utils.messages.actions.SelectedBuildingMessage;
import it.polimi.ingsw2020.santorini.view.AppGUI;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class PossibleBuildingsController {

    @FXML
    Label text;
    @FXML
    Label text2;
    @FXML
    Button b00;
    @FXML
    Button b01;
    @FXML
    Button b02;
    @FXML
    Button b10;
    @FXML
    Button b12;
    @FXML
    Button b20;
    @FXML
    Button b21;
    @FXML
    Button b22;
    @FXML
    Label p00;
    @FXML
    Label p01;
    @FXML
    Label p02;
    @FXML
    Label p10;
    @FXML
    Label p12;
    @FXML
    Label p20;
    @FXML
    Label p21;
    @FXML
    Label p22;

    private Button[][] matrix = new Button[3][3];
    private Label[][] labelMatrix = new Label[3][3];
    private Client client;
    private AskBuildSelectionMessage askBuildSelectionMessage;
    private Stage stage;

    public void setClient(Client client) {
        this.client = client;
    }

    public void setAskBuildSelectionMessage(AskBuildSelectionMessage askBuildSelectionMessage) {
        this.askBuildSelectionMessage = askBuildSelectionMessage;
    }

    public void setText(){
        text.setText("Select the cell where you want to build!");
    }

    /**
     * this method is used to initialize the matrix of possible buildings
     */
    public void initializeBoard() {
        int [][] possibleMatrix = askBuildSelectionMessage.getPossibleBuildings();
        AppGUI.buildButtonMatrices(matrix, b00, b01, b02, b10, b12, b20, b21, b22);
        AppGUI.buildLabelMatrices(labelMatrix, p00, p01, p02, p10, p12, p20, p21, p22);
        AppGUI.printMatrix(possibleMatrix, matrix, labelMatrix);
    }

    /**
     * this method is used to select the direction of the build
     * @param actionEvent is the event of the button clicked from which the method extracts the direction
     */
    @FXML
    public void selectBuild(ActionEvent actionEvent) {
        Direction direction = AppGUI.extractDirection(actionEvent, b00, b01, b02, b10, b12, b20, b21, b22);
        Message buildSelection = new Message(client.getUsername());
        buildSelection.buildSelectedBuildingMessage(new SelectedBuildingMessage(direction));
        client.getNetworkHandler().send(buildSelection);
        stage.setOnCloseRequest(e->stage.close());
        stage.close();
    }

    public void setStage(Stage stage){
        this.stage = stage;
    }
}
