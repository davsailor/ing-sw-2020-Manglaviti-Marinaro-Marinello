package it.polimi.ingsw2020.santorini.view.gui;

import it.polimi.ingsw2020.santorini.model.Board;
import it.polimi.ingsw2020.santorini.network.client.Client;
import it.polimi.ingsw2020.santorini.utils.Direction;
import it.polimi.ingsw2020.santorini.utils.Message;
import it.polimi.ingsw2020.santorini.utils.messages.godsParam.HestiaParamMessage;
import it.polimi.ingsw2020.santorini.utils.messages.matchMessage.MatchStateMessage;
import it.polimi.ingsw2020.santorini.view.AppGUI;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class HestiaController {

    private Stage stage;
    private Button[][] matrix = new Button[3][3];
    private Label[][] labelMatrix = new Label[3][3];
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

    @FXML
    public void build(ActionEvent actionEvent) {
        HestiaParamMessage hestiaParamMessage = new HestiaParamMessage();
        Button pos = (Button) actionEvent.getSource();
        Direction direction = null;
        if(pos.equals(b00)){
            direction = Direction.NORTH_WEST;
        } else if (pos.equals(b01)){
            direction = Direction.NORTH;
        } else if (pos.equals(b02)){
            direction = Direction.NORTH_EAST;
        } else if (pos.equals(b10)){
            direction = Direction.WEST;
        } else if (pos.equals(b12)){
            direction = Direction.EAST;
        } else if (pos.equals(b20)){
            direction = Direction.SOUTH_WEST;
        } else if (pos.equals(b21)){
            direction = Direction.SOUTH;
        } else if (pos.equals(b22)){
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
        AppGUI.getHestiaParamMessage().setDirection(direction);
        stage.setOnCloseRequest(e->stage.close());
        stage.close();
    }


    public void initializeHestiaMatrix(){

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

        int[] posBuilder = new int[2];
        posBuilder[0] = matchStateMessage.getCurrentPlayer().getPlayingBuilder().getPosX();
        posBuilder[1] = matchStateMessage.getCurrentPlayer().getPlayingBuilder().getPosY();

        matchStateMessage.getCurrentPlayer().getPlayingBuilder().setBoard(new Board(matchStateMessage.getBoard()));
        matchStateMessage.getCurrentPlayer().getPlayingBuilder().setPlayer(matchStateMessage.getCurrentPlayer());

        int[][] neighboringLevelCell = Board.neighboringLevelCell(matchStateMessage.getCurrentPlayer().getPlayingBuilder());

        if(matchStateMessage.getCurrentPlayer().getPlayingBuilder().getPosX() == 1 || matchStateMessage.getCurrentPlayer().getPlayingBuilder().getPosX() == 5){
            neighboringLevelCell[1][0] = -1;
            neighboringLevelCell[1][1] = -1;
            neighboringLevelCell[1][2] = -1;
        } else if(matchStateMessage.getCurrentPlayer().getPlayingBuilder().getPosX() == 2){
            neighboringLevelCell[0][0] = -1;
            neighboringLevelCell[0][1] = -1;
            neighboringLevelCell[0][2] = -1;
        } else if(matchStateMessage.getCurrentPlayer().getPlayingBuilder().getPosX() == 4){
            neighboringLevelCell[2][0] = -1;
            neighboringLevelCell[2][1] = -1;
            neighboringLevelCell[2][2] = -1;
        }
        if(matchStateMessage.getCurrentPlayer().getPlayingBuilder().getPosY() == 1 || matchStateMessage.getCurrentPlayer().getPlayingBuilder().getPosY() == 5){
            neighboringLevelCell[0][1] = -1;
            neighboringLevelCell[1][1] = -1;
            neighboringLevelCell[2][1] = -1;
        } else if(matchStateMessage.getCurrentPlayer().getPlayingBuilder().getPosY() == 2){
            neighboringLevelCell[0][0] = -1;
            neighboringLevelCell[1][0] = -1;
            neighboringLevelCell[2][0] = -1;
        } else if(matchStateMessage.getCurrentPlayer().getPlayingBuilder().getPosY() == 4){
            neighboringLevelCell[0][2] = -1;
            neighboringLevelCell[1][2] = -1;
            neighboringLevelCell[2][2] = -1;
        }
        for(int i=0 ; i<3 ;++i){
            for(int j=0; j<3 ; ++j){
                if (i != 1 || j != 1) {
                    if (neighboringLevelCell[i][j] < 0 || neighboringLevelCell[i][j] >= 4) {
                        matrix[i][j].setStyle("-fx-background-color: #ff0000");
                        matrix[i][j].setDisable(true);
                    }
                    labelMatrix[i][j].setText (neighboringLevelCell[i][j] == -1 ? " "  : String.valueOf(neighboringLevelCell[i][j]));
                }
            }
        }
    }
}
