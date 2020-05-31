package it.polimi.ingsw2020.santorini.view.gui;

import it.polimi.ingsw2020.santorini.network.client.Client;
import it.polimi.ingsw2020.santorini.utils.Message;
import it.polimi.ingsw2020.santorini.utils.messages.actions.ActivateGodMessage;
import it.polimi.ingsw2020.santorini.utils.messages.matchMessage.MatchStateMessage;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.stage.Stage;


public class ActivationPowerController {

    private MatchStateMessage matchStateMessage;
    private Client client;


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
    private Stage stage;


    public void setMatchStateMessage(MatchStateMessage matchStateMessage) {
        this.matchStateMessage = matchStateMessage;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    @FXML
    public void power(ActionEvent actionEvent) {

        Button pos = (Button) actionEvent.getSource();
        Message message = new Message(client.getUsername());

        if( pos.equals(yesButton)){
            message.buildActivateGodMessage(new ActivateGodMessage(true));
        }
        else{
            message.buildActivateGodMessage(new ActivateGodMessage(false));
        }
        yesButton.setDisable(true);
        noButton.setDisable(true);
        client.getNetworkHandler().send(message);
        stage.setOnCloseRequest(e->stage.close());
        stage.close();

    }
    public void setStage(Stage stage){
        this.stage = stage;
    }

    public  void initializeText(){
        switch (matchStateMessage.getCurrentPlayer().getDivinePower().getName()){
            case "Apollo" :
                power1.setText("Your Worker may move into an opponent Worker’s space using");
                power2.setText("(normal movement rules) and force their Worker to the space ");
                power3.setText("yours just vacated (swapping their positions).");
                break;
            case "Ares" :
                power1.setText("You may remove an unoccupied block (not dome)");
                power2.setText(" neighboring your unmoved Worker ");
                break;
            case "Artemis" :
                power1.setText("Your Worker may move one additional time,");
                power2.setText("but not back to the space it started on.");
                break;
            case "Athena" :
                power1.setText("If one of your Workers moved up on your last turn,");
                power2.setText("opponent Workers cannot move up this turn.");
                break;
            case "Atlas" :
                power1.setText("Your Worker may build a dome at");
                power2.setText(" any level including the ground.");
                break;
            case "Chronus" :
                power1.setText("You also win when there are at least five");
                power2.setText("Complete Towers on the board.");
                break;
            case "Demeter" :
                power1.setText("Your Worker may build one additional ");
                power2.setText("time, but not on the same space.");
                break;
            case "Hephaestus" :
                power1.setText("Your Worker may build one additional block");
                power2.setText("(not dome) on top of your first block.");
                break;
            case "Hestia" :
                power1.setText("Your Worker may build one additional time.");
                power2.setText("The additional build cannot be on a perimeter space.");
                break;
            case "Minotaur" :
                power1.setText("Your Worker may move into an opponent Worker’s space (normal");
                power2.setText("rules), if the next space in the same direction is unoccupied.");
                power3.setText("Their Worker is forced into that space (regardless of its level).");
                break;
            case "Pan" :
                power1.setText("You also win if your Worker moves down two or more levels.");
                break;
            case "Poseidon" :
                power1.setText("If your unmoved Worker is on the ground level,");
                power2.setText("it may build up to three times in neighboring spaces.");
                break;
            case "Prometheus" :
                power1.setText("If your Worker does not move up,");
                power2.setText("it may build both before and after moving.");
                break;
            case "Zeus" :
                power1.setText("Your Worker may build under itself in its current space,");
                power2.setText("forcing it up one level. You do not win by forcing ");
                power3.setText("yourself up to the third level.");
                break;
        }
    }


}
