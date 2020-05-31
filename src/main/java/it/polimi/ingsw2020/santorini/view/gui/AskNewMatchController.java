package it.polimi.ingsw2020.santorini.view.gui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;


public class AskNewMatchController {

    @FXML
    Button yesButton;
    @FXML
    Button noButton;
    private String answer;

    private boolean notDone = true;

    public void newMatch(ActionEvent actionEvent) {
        Button pos = (Button) actionEvent.getSource();
        if(pos.equals(yesButton)){
            setAnswer("YES");
        }else{
            setAnswer("NO");
        }
        yesButton.setDisable(true);
        noButton.setDisable(true);
        notDone = false;
    }
    public boolean isNotDone() {
        return notDone;
    }


    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public String getAnswer() {
        return answer;
    }
}
