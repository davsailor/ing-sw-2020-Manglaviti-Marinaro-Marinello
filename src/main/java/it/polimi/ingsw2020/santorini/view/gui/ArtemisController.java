package it.polimi.ingsw2020.santorini.view.gui;

import it.polimi.ingsw2020.santorini.utils.Direction;
import it.polimi.ingsw2020.santorini.utils.messages.matchMessage.MatchStateMessage;
import it.polimi.ingsw2020.santorini.view.AppGUI;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class ArtemisController {
    private Stage stage;
    private Button[][] matrix = new Button[3][3];
    private MatchStateMessage matchStateMessage;

    public void setStage(Stage stage){
        this.stage = stage;
    }

    public void setMatchStateMessage(MatchStateMessage matchStateMessage) {
        this.matchStateMessage = matchStateMessage;
    }

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

    /**
     * the method checks and disables the buttons that represents directions not allowed by rules.
     */
    public void initializeArtemisMatrix() {
        int[][] possibleMoves = matchStateMessage.getCurrentPlayer().getPlayingBuilder().getPossibleMoves();
        AppGUI.buildButtonMatrices(matrix, b00, b01, b02, b10, b12, b20, b21, b22);
        for(int i=0 ; i<3 ;++i)
            for(int j=0; j<3 ; ++j)
                if (i!=1 || j!= 1)
                    if (possibleMoves[i][j] == 0 || possibleMoves[i][j] == 4) {
                        matrix[i][j].setStyle("-fx-background-color: #ff0000");
                        matrix[i][j].setDisable(true);
                    }
    }

    /**
     * the method extracts from the actionEvent the direction click by the player, and inserts it in to ArtemisParamMessage
     * @param actionEvent is the event of the click on one of the buttons representing the directions
     */
    @FXML
    public void selectMove(ActionEvent actionEvent) {
        Button pos = (Button) actionEvent.getSource();
        Direction direction = AppGUI.extractDirection(actionEvent, b00, b01, b02, b10, b12, b20, b21, b22);
        AppGUI.getArtemisParamMessage().setDirection(direction);
        stage.setOnCloseRequest(e->stage.close());
        stage.close();
    }
}