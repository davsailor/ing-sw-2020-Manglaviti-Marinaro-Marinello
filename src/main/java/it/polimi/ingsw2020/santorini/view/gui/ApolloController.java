package it.polimi.ingsw2020.santorini.view.gui;

import it.polimi.ingsw2020.santorini.model.Board;
import it.polimi.ingsw2020.santorini.model.Builder;
import it.polimi.ingsw2020.santorini.utils.AccessType;
import it.polimi.ingsw2020.santorini.utils.Direction;
import it.polimi.ingsw2020.santorini.utils.messages.godsParam.ApolloParamMessage;
import it.polimi.ingsw2020.santorini.utils.messages.matchMessage.MatchStateMessage;
import it.polimi.ingsw2020.santorini.view.AppGUI;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

import javafx.scene.control.Label;
import javafx.stage.Stage;

public class ApolloController {
    private MatchStateMessage matchStateMessage;
    private ApolloParamMessage apolloParamMessage = new ApolloParamMessage();
    private Stage stage;
    private Builder chosen = null;
    private Button[][] buttonMatrix = new Button[3][3];

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
    Button F;
    @FXML
    Button M;

    public void setMatchStateMessage(MatchStateMessage matchStateMessage) {
        this.matchStateMessage = matchStateMessage;
    }

    /**
     * the method inserts the buttons of the small 3*3 matrix, showed in the window, ina a 3*3 matrix of buttons. After the initialization
     * of the buttons matrix the method checks if an element of apolloMatrix is equals to 0 (which represents a cell that cannot be reached
     * though the power of the god). For each 0 found the method disables the correspondent button and changes the color to red.
     * @param apolloMatrix is a matrix containing the values of PossibleSwap
     */
    public void initializeApolloMatrix(int[][] apolloMatrix) {
        AppGUI.buildButtonMatrices(buttonMatrix, b00, b01, b02, b10, b12, b20, b21, b22);
        for(int i=0; i<3; ++i)
            for( int j=0 ; j < 3; ++j)
                if(i!=1 || j!= 1)
                    if(apolloMatrix[i][j]==0){
                        buttonMatrix[i][j].setDisable(true);
                        buttonMatrix[i][j].setStyle("-fx-background-color: #ff0000");
                    }
    }

    public void setStage(Stage stage){
        this.stage = stage;
    }

    public Builder getChosen() {
        return chosen;
    }

    /**
     * the method checks if each builder of the player can use the power of Apollo, there it checks if a builder can swap its position
     * with an opponent's builder. If the builder can swap its position the method will allow the player to click the builder's button
     * if not the method will disable the builder's button
     */
    public void initializeButtons() {
        Builder chosen = matchStateMessage.getCurrentPlayer().getBuilderF();
        chosen.setBoard(new Board(matchStateMessage.getBoard()));
        chosen.setPlayer(matchStateMessage.getCurrentPlayer());
        int[][] neighboringSwappingCell = Board.neighboringSwappingCell(chosen, AccessType.OCCUPIED);
        boolean allZeros = true;
        for(int i = 0; i < 3 && allZeros; ++i)
            for(int j = 0; j < 3 && allZeros; ++j)
                if(neighboringSwappingCell[i][j] != 0) allZeros = false;
        if(allZeros){
            F.setDisable(true);
            F.setStyle("-fx-border-color: #ff0000; -fx-border-width: 5px;");
        }else
            F.setStyle("-fx-border-color: #00ff00; -fx-border-width: 5px;");
        chosen = matchStateMessage.getCurrentPlayer().getBuilderM();
        chosen.setBoard(new Board(matchStateMessage.getBoard()));
        chosen.setPlayer(matchStateMessage.getCurrentPlayer());
        neighboringSwappingCell = Board.neighboringSwappingCell(chosen, AccessType.OCCUPIED);
        allZeros = true;
        for(int i = 0; i < 3 && allZeros; ++i)
            for(int j = 0; j < 3 && allZeros; ++j)
                if(neighboringSwappingCell[i][j] != 0) allZeros = false;
        if(allZeros){
            M.setDisable(true);
            M.setStyle("-fx-border-color: #ff0000; -fx-border-width: 5px;");
        }else
            M.setStyle("-fx-border-color: #00ff00; -fx-border-width: 5px;");
    }

    /**
     * the method extract from the actionEvent the direction selected by the player, and then insert it in ApolloParamMessage
     * @param actionEvent is the event of the click on one of the buttons representing the directions
     */
    @FXML
    public void selectSwap(ActionEvent actionEvent){
        Direction direction = AppGUI.extractDirection(actionEvent, b00, b01, b02, b10, b12, b20, b21, b22);
        AppGUI.getApolloParamMessage().setOpponentBuilderDirection(direction);
        stage.setOnCloseRequest(e->stage.close());
        stage.close();
    }

    /**
     * the method extract from the actionEvent the builder selected by the player to utilize Apollo's power
     * @param actionEvent is the event of the click on one of the buttons representing the genres of the builders
     */
    @FXML
    public void selectGender(ActionEvent actionEvent) {
        Button pos = (Button) actionEvent.getSource();
        char yourBuilderGender;
        if(pos.getId().equals("F")){
            chosen = matchStateMessage.getCurrentPlayer().getBuilderF();
            yourBuilderGender = 'F';
        }else{
            chosen = matchStateMessage.getCurrentPlayer().getBuilderM();
            yourBuilderGender = 'M';
        }
        chosen.setBoard(new Board(matchStateMessage.getBoard()));
        chosen.setPlayer(matchStateMessage.getCurrentPlayer());
        F.setDisable(true);
        M.setDisable(true);
        AppGUI.getApolloParamMessage().setYourBuilderGender(yourBuilderGender);
        stage.setOnCloseRequest(e->stage.close());
        stage.close();
    }
}
