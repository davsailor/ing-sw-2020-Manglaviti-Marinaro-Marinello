package it.polimi.ingsw2020.santorini.view.gui;

import it.polimi.ingsw2020.santorini.model.Board;
import it.polimi.ingsw2020.santorini.model.Builder;
import it.polimi.ingsw2020.santorini.utils.Direction;
import it.polimi.ingsw2020.santorini.utils.messages.matchMessage.MatchStateMessage;
import it.polimi.ingsw2020.santorini.view.AppGUI;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class PrometheusController {
    private Stage stage;
    private Button[][] matrix = new Button[3][3];
    private Label[][] labelMatrix = new Label[3][3];
    private MatchStateMessage matchStateMessage;
    private Builder chosen;

    public void setStage(Stage stage){
        this.stage = stage;
    }

    public Builder getChosen() {
        return chosen;
    }

    public void setMatchStateMessage(MatchStateMessage matchStateMessage) {
        this.matchStateMessage = matchStateMessage;
    }

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
    Button M;
    @FXML
    Button F;


    @FXML
    public void build(ActionEvent actionEvent) {
        Direction direction = AppGUI.extractDirection(actionEvent, b00, b01, b02, b10, b12, b20, b21, b22);
        b02.setDisable(true);
        b10.setDisable(true);
        b12.setDisable(true);
        b20.setDisable(true);
        b21.setDisable(true);
        b22.setDisable(true);
        AppGUI.getPrometheusParamMessage().setDirection(direction);
        stage.setOnCloseRequest(e->stage.close());
        stage.close();
    }

    public void initializePrometheusMatrix(int[][] PrometheusMatrix){
        if(PrometheusMatrix == null) return;
        AppGUI.buildMatrices(matrix, b00, b01, b02, b10, b12, b20, b21, b22, labelMatrix, p00, p01, p02, p10, p12, p20, p21, p22);

        matchStateMessage.getCurrentPlayer().setRiseActions(false);
        matchStateMessage.getCurrentPlayer().setMoveActions(true);
        matchStateMessage.getCurrentPlayer().getBuilderF().setBoard(new Board(matchStateMessage.getBoard()));
        matchStateMessage.getCurrentPlayer().getBuilderF().setPlayer(matchStateMessage.getCurrentPlayer());
        matchStateMessage.getCurrentPlayer().getBuilderM().setBoard(new Board(matchStateMessage.getBoard()));
        matchStateMessage.getCurrentPlayer().getBuilderM().setPlayer(matchStateMessage.getCurrentPlayer());

        AppGUI.printMatrix(PrometheusMatrix, matrix, labelMatrix);

    }

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
        AppGUI.getPrometheusParamMessage().setBuilderSex(yourBuilderGender);
        stage.setOnCloseRequest(e->stage.close());
        stage.close();
    }

    public void initializeButtons() {
        matchStateMessage.getCurrentPlayer().getBuilderM().setBoard(new Board(matchStateMessage.getBoard()));
        matchStateMessage.getCurrentPlayer().getBuilderM().setPlayer(matchStateMessage.getCurrentPlayer());
        matchStateMessage.getCurrentPlayer().getBuilderF().setBoard(new Board(matchStateMessage.getBoard()));
        matchStateMessage.getCurrentPlayer().getBuilderF().setPlayer(matchStateMessage.getCurrentPlayer());
        if (!matchStateMessage.getCurrentPlayer().getBuilderM().canMove() || !matchStateMessage.getCurrentPlayer().getBuilderM().canBuild()) {
            M.setDisable(true);
            M.setStyle("-fx-border-color: #ff0000; -fx-border-width: 5px;");
        }else
            M.setStyle("-fx-border-color: #00ff00; -fx-border-width: 5px;");
        if(!matchStateMessage.getCurrentPlayer().getBuilderF().canMove() ||  !matchStateMessage.getCurrentPlayer().getBuilderF().canBuild()){
            F.setDisable(true);
            F.setStyle("-fx-border-color: #ff0000; -fx-border-width: 5px;");
        }else
            F.setStyle("-fx-border-color: #00ff00; -fx-border-width: 5px;");
    }
}
