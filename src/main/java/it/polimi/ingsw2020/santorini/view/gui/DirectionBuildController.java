package it.polimi.ingsw2020.santorini.view.gui;

import it.polimi.ingsw2020.santorini.utils.Direction;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;

import static it.polimi.ingsw2020.santorini.utils.Direction.*;

public class DirectionBuildController {





    @FXML
    Button insert;
    @FXML
    ChoiceBox<Direction> direction;

    public void initialize(){
        ObservableList list = FXCollections.observableArrayList(NORTH_WEST,NORTH,NORTH_EAST,WEST,EAST,SOUTH_WEST,SOUTH,SOUTH_EAST);

        direction.setItems(list);
        direction.setValue(NORTH_WEST);
    }

    public void selectDirection(ActionEvent actionEvent) {
    }
}
