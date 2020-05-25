package it.polimi.ingsw2020.santorini.view.gui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;


public class ActivationPowerController {
    @FXML
    Button yesButton;
    @FXML
    Button noButton;
    @FXML
    Label power1;
    @FXML
    Label power2;
    @FXML
    Label power3;

    
    public void power(ActionEvent actionEvent) {

        Button pos = (Button) actionEvent.getSource();

        if( pos == yesButton){
            //answer YES
        }
        else{
            //answer NO
        }

    }


}
