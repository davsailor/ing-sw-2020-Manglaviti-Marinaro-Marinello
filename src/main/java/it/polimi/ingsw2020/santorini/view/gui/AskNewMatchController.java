package it.polimi.ingsw2020.santorini.view.gui;

import it.polimi.ingsw2020.santorini.view.AppGUI;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class AskNewMatchController {
    private Stage stage;
    @FXML
    Button yesButton;
    @FXML
    Button noButton;

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    /**
     * the method extracts from the actionEvent the answer clicked by the player. Then it close the stage
     * @param actionEvent is the event of the click on one of the buttons representing the answer that could be yes or no
     */
    public void newMatch(ActionEvent actionEvent) {
        Button pos = (Button) actionEvent.getSource();
        if(pos.equals(yesButton)) AppGUI.setWantNewMatch("YES");
        else AppGUI.setWantNewMatch("NO");
        yesButton.setDisable(true);
        noButton.setDisable(true);
        stage.setOnCloseRequest(e -> stage.close());
        stage.close();
    }
}
