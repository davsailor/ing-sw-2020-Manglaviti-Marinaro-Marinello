package it.polimi.ingsw2020.santorini.view.gui;

import it.polimi.ingsw2020.santorini.network.client.Client;
import it.polimi.ingsw2020.santorini.utils.Message;
import it.polimi.ingsw2020.santorini.utils.messages.matchMessage.GodSelectionMessage;
import it.polimi.ingsw2020.santorini.utils.messages.matchMessage.MatchSetupMessage;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import java.util.ArrayList;

public class SelectGodController {
    private Client client;
    private MatchSetupMessage matchSetupMessage;

    public void setClient(Client client) {
        this.client = client;
    }

    public void setMatchSetupMessage(MatchSetupMessage matchSetupMessage) {
        this.matchSetupMessage = matchSetupMessage;
    }

    @FXML
    Button godButton1;
    @FXML
    Button godButton2;
    @FXML
    Button godButton3;

    @FXML
    Label name1;
    @FXML
    Label name2;
    @FXML
    Label name3;

    @FXML
    Label god1;
    @FXML
    Label god2;
    @FXML
    Label god3;


    /**
     * this method is used to set chosen gods with the mouse click
     * @param actionEvent is the event of the mouse clicked
     */
    public void selectGod(ActionEvent actionEvent) {
        Button pos = (Button) actionEvent.getSource();

        int selectedGod = GodSelectionController.calcInt(name1.getText());
        if(pos.equals(godButton1)){
            selectedGod = GodSelectionController.calcInt(name1.getText());
        } else if(pos.equals(godButton2)){
            selectedGod = GodSelectionController.calcInt(name2.getText());
            pos.setDisable(true);
        }else if(pos.equals(godButton3)) {
            selectedGod = GodSelectionController.calcInt(name3.getText());
        }
        pos.setDisable(true);
        matchSetupMessage.getSelectedGods().remove((Integer)selectedGod);
        Message message = new Message(client.getUsername());
        message.buildInvokedGodMessage(new GodSelectionMessage(selectedGod, matchSetupMessage.getSelectedGods()));
        client.getNetworkHandler().send(message);
    }

    /**
     * this method initialize the image of gods
     * @param name is the name of the god
     * @return the image associated to each god
     * */
    private Image findImage(String name){
        switch (name){
            case "Apollo" :
                return new Image(getClass().getResourceAsStream("/images/Apollo.png"));
            case "Ares" :
                return new Image(getClass().getResourceAsStream("/images/Ares.png"));
            case "Artemis" :
                return new Image(getClass().getResourceAsStream("/images/Artemis.png"));
            case "Athena" :
                return new Image(getClass().getResourceAsStream("/images/Athena.png"));
            case "Atlas" :
                return new Image(getClass().getResourceAsStream("/images/Atlas.png"));
            case "Chronus" :
                return new Image(getClass().getResourceAsStream("/images/Chronus.png"));
            case "Demeter" :
                return new Image(getClass().getResourceAsStream("/images/Demeter.png"));
            case "Hephaestus" :
                return new Image(getClass().getResourceAsStream("/images/Hephaestus.png"));
            case "Hestia" :
                return new Image(getClass().getResourceAsStream("/images/Hestia.png"));
            case "Minotaur" :
                return new Image(getClass().getResourceAsStream("/images/Minotaur.png"));
            case "Pan" :
                return new Image(getClass().getResourceAsStream("/images/Pan.png"));
            case "Poseidon" :
                return new Image(getClass().getResourceAsStream("/images/Poseidon.png"));
            case "Prometheus" :
                return new Image(getClass().getResourceAsStream("/images/Prometheus.png"));
            case "Zeus" :
                return new Image(getClass().getResourceAsStream("/images/Zeus.png"));
        }
        return null;
    }


    /**
     * this method initialize gods on the SelectGod screen
     * @param selectedGods is the ArrayList of the gods selected
     */
    public void initializeGods(ArrayList<Integer> selectedGods) {

        String name = findName(selectedGods.get(0));
        Tooltip tooltip1 = new Tooltip();
        tooltip1.setText(findEffect(selectedGods.get(0)));
        godButton1.setTooltip(tooltip1);
        name1.setText(name);
        name1.setAlignment(Pos.TOP_CENTER);
        Image image = findImage(name);
        ImageView imageView = new ImageView(image);
        imageView.setFitWidth(160.0);
        imageView.setFitHeight(217.0);
        god1.setGraphic(imageView);

        name = findName(selectedGods.get(1));
        Tooltip tooltip2 = new Tooltip();
        tooltip2.setText(findEffect(selectedGods.get(1)));
        godButton2.setTooltip(tooltip2);
        name2.setText(name);
        name2.setAlignment(Pos.TOP_CENTER);
        image = findImage(name);
        imageView = new ImageView(image);
        imageView.setFitWidth(160.0);
        imageView.setFitHeight(217.0);
        god2.setGraphic(imageView);

        if(selectedGods.size() == 3){
            name = findName(selectedGods.get(2));
            Tooltip tooltip3 = new Tooltip();
            tooltip3.setText(findEffect(selectedGods.get(2)));
            godButton3.setTooltip(tooltip3);
            name3.setText(name);
            name3.setAlignment(Pos.TOP_CENTER);
            image = findImage(name);
            imageView = new ImageView(image);
            imageView.setFitWidth(160.0);
            imageView.setFitHeight(217.0);
            god3.setGraphic(imageView);
        }
    }

    /**
     *  this method is used to find the name of the god by the code
     * @param numGod is the code of the god
     * @return the name of each god
     */
    private String findName(int numGod) {
        switch (numGod) {
            case 0:
                return "Apollo";
            case 9:
                return "Ares";
            case 1:
                return "Artemis";
            case 2:
                return "Athena";
            case 3:
                return "Atlas";
            case 11:
                return "Chronus";
            case 4:
                return "Demeter";
            case 5:
                return "Hephaestus";
            case 10:
                return "Hestia";
            case 6:
                return "Minotaur";
            case 7:
                return "Pan";
            case 12:
                return "Poseidon";
            case 8:
                return "Prometheus";
            case 13:
                return "Zeus";
            default:
                return null;
        }
    }

    /**
     * this method return the effect of the god by the code
     * @param numGods is the code of the god
     * @return the effect associated to each god
     */
    private String findEffect(int numGods) {
        switch (numGods) {
            case 0:
                return "Your Worker may move into an opponent Worker’s space\n" +
            "(using normal movement rules) and force their Worker to the space yours\n" +
                    "just vacated (swapping their positions).";
            case 9:
                return "You may remove an unoccupied block\n" +
                        "(not dome) neighboring your unmoved Worker";
            case 1:
                return "Your Worker may move one additional time,\n" +
                        "but not back to the space it started on.";
            case 2:
                return "If one of your Workers moved up on your last turn, \n" +
                        "opponent Workers cannot move up this turn.";
            case 3:
                return "Your Worker may build a dome\n" +
                        "at any level including the ground.";
            case 11:
                return "You also win\n" +
                        "when there are at least five\n" +
                        "Complete Towers on the board..";
            case 4:
                return "Your Worker may build one additional time,\n" +
                        "but not on the same space.";
            case 5:
                return "Your Worker may build one additional block\n" +
                        "(not dome) on top of your first block.";
            case 10:
                return "Your Worker may build one additional time.\n" +
                        "The additional build cannot be on a perimeter space.";
            case 6:
                return "Your Worker may move into an opponent Worker’s\n" +
                        "space (using normal movement rules), if the next space in the same direction is\n" +
                        "unoccupied. Their Worker is forced into that space (regardless of its level).";
            case 7:
                return "You also win if your Worker moves down two or more levels.";
            case 12:
                return "If your unmoved Worker is on the ground level,\n" +
                        "it may build up to three times in neighboring spaces.";
            case 8:
                return "If your Worker does not move up,\n" +
                        "it may build both before and after moving.";
            case 13:
                return "Your Worker may build under itself in its current\n" +
                        "space, forcing it up one level. You do not win by forcing yourself up to the third level.";
            default:
                return null;
        }
    }
}
