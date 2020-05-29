package it.polimi.ingsw2020.santorini.view.gui;

import it.polimi.ingsw2020.santorini.model.GodCard;
import it.polimi.ingsw2020.santorini.model.GodDeck;
import it.polimi.ingsw2020.santorini.model.GodFactotum;
import it.polimi.ingsw2020.santorini.model.Player;
import it.polimi.ingsw2020.santorini.model.gods.Apollo;
import it.polimi.ingsw2020.santorini.model.gods.Ares;
import it.polimi.ingsw2020.santorini.network.client.Client;
import it.polimi.ingsw2020.santorini.utils.Message;
import it.polimi.ingsw2020.santorini.utils.messages.matchMessage.GameGodsSelectionMessage;
import it.polimi.ingsw2020.santorini.utils.messages.matchMessage.GodSelectionMessage;
import it.polimi.ingsw2020.santorini.utils.messages.matchMessage.MatchSetupMessage;
import it.polimi.ingsw2020.santorini.view.AppGUI;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
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


    public void selectGod(ActionEvent actionEvent) {
        Button pos = (Button) actionEvent.getSource();
        System.out.println(name1.getText());
        System.out.println(GodSelectionController.calcInt(name1.getText()));

        int selectedGod = GodSelectionController.calcInt(name1.getText());
        if(pos.equals(godButton1)){
            // setto la divinità scelta
            selectedGod = GodSelectionController.calcInt(name1.getText());
        } else if(pos.equals(godButton2)){
            // setto la divinità scelta
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


    public void initializeGods(ArrayList<Integer> selectedGods) {

        String name = findName(selectedGods.get(0));
        System.out.println(name);
        name1.setText(name);
        name1.setAlignment(Pos.TOP_CENTER);
        Image image = findImage(name);
        ImageView imageView = new ImageView(image);
        imageView.setFitWidth(160.0);
        imageView.setFitHeight(217.0);
        god1.setGraphic(imageView);

        name = findName(selectedGods.get(1));
        name2.setText(name);
        name2.setAlignment(Pos.TOP_CENTER);
        image = findImage(name);
        imageView = new ImageView(image);
        imageView.setFitWidth(160.0);
        imageView.setFitHeight(217.0);
        god2.setGraphic(imageView);

        if(selectedGods.size() == 3){
            name = findName(selectedGods.get(2));
            name3.setText(name);
            name3.setAlignment(Pos.TOP_CENTER);
            image = findImage(name);
            imageView = new ImageView(image);
            imageView.setFitWidth(160.0);
            imageView.setFitHeight(217.0);
            god3.setGraphic(imageView);
        }
    }

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
}
