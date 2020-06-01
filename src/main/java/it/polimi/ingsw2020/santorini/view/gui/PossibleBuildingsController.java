package it.polimi.ingsw2020.santorini.view.gui;

import it.polimi.ingsw2020.santorini.model.Cell;
import it.polimi.ingsw2020.santorini.network.client.Client;
import it.polimi.ingsw2020.santorini.utils.Direction;
import it.polimi.ingsw2020.santorini.utils.Message;
import it.polimi.ingsw2020.santorini.utils.messages.actions.AskBuildSelectionMessage;
import it.polimi.ingsw2020.santorini.utils.messages.actions.AskMoveSelectionMessage;
import it.polimi.ingsw2020.santorini.utils.messages.actions.SelectedBuildingMessage;
import it.polimi.ingsw2020.santorini.view.AppGUI;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
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
        text.setText("Seleziona la cella in cui vuoi costruire!");
    }

    public void initializeBoard() {
        int [][] possibleMatrix = askBuildSelectionMessage.getPossibleBuildings();

        matrix[0][0] = b00;
        matrix[0][1] = b01;
        matrix[0][2] = b02;
        matrix[1][0] = b10;
        matrix[1][2] = b12;
        matrix[2][0] = b20;
        matrix[2][1] = b21;
        matrix[2][2] = b22;

        labelMatrix[0][0] = p00;
        labelMatrix[0][1] = p01;
        labelMatrix[0][2] = p02;
        labelMatrix[1][0] = p10;
        labelMatrix[1][2] = p12;
        labelMatrix[2][0] = p20;
        labelMatrix[2][1] = p21;
        labelMatrix[2][2] = p22;

        for(int i=0 ; i<3 ;++i){
            for(int j=0; j<3 ; ++j){
                if (i != 1 || j != 1) {
                    if (possibleMatrix[i][j] < 0 || possibleMatrix[i][j] >= 4) {
                        matrix[i][j].setStyle("-fx-background-color: #ff0000");
                        matrix[i][j].setDisable(true);
                    }
                    labelMatrix[i][j].setText (possibleMatrix[i][j] == -1 ? " "  : String.valueOf(possibleMatrix[i][j]));
                }
            }
        }
    }
    @FXML
    public void selectBuild(ActionEvent actionEvent) {
        Button pos = (Button) actionEvent.getSource();
        Direction direction = null;
        if(pos.equals(b00)){
            direction = Direction.NORTH_WEST;
        }else if ( pos.equals(b01)){
            direction = Direction.NORTH;
        }else if ( pos.equals(b02)){
            direction = Direction.NORTH_EAST;
        }else if ( pos.equals(b10)){
            direction = Direction.WEST;
        }else if ( pos.equals(b12)){
            direction = Direction.EAST;
        }else if ( pos.equals(b20)){
            direction = Direction.SOUTH_WEST;
        }else if ( pos.equals(b21)){
            direction = Direction.SOUTH;
        }else if ( pos.equals(b22)){
            direction = Direction.SOUTH_EAST;
        }
        b00.setDisable(true);
        b01.setDisable(true);
        b02.setDisable(true);
        b10.setDisable(true);
        b12.setDisable(true);
        b20.setDisable(true);
        b21.setDisable(true);
        b22.setDisable(true);

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
