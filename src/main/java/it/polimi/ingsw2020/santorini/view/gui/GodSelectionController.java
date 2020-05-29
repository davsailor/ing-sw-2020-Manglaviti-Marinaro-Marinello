package it.polimi.ingsw2020.santorini.view.gui;

import it.polimi.ingsw2020.santorini.model.GodFactotum;
import it.polimi.ingsw2020.santorini.network.client.Client;
import it.polimi.ingsw2020.santorini.utils.Message;
import it.polimi.ingsw2020.santorini.utils.SecondHeaderType;
import it.polimi.ingsw2020.santorini.utils.messages.matchMessage.GameGodsSelectionMessage;
import it.polimi.ingsw2020.santorini.utils.messages.matchMessage.MatchSetupMessage;
import javafx.css.Match;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.Image;


public class GodSelectionController {

    private Client client;

    private MatchSetupMessage matchSetupMessage;

    private int[] selectedGods;

    public void setClient(Client client) {
        this.client = client;
    }

    public void setMatchSetupMessage(MatchSetupMessage matchSetupMessage) {
        this.selectedGods = new int[matchSetupMessage.getPlayers().size()];
        this.matchSetupMessage = matchSetupMessage;
    }

    int cont=0;

    @FXML
    Button Apollo;
    @FXML
    Button Ares;
    @FXML
    Button Artemis;
    @FXML
    Button Athena;
    @FXML
    Button Atlas;
    @FXML
    Button Chronus;
    @FXML
    Button Hephaestus;
    @FXML
    Button Hestia;
    @FXML
    Button Minotaur;
    @FXML
    Button Pan;
    @FXML
    Button Poseidon;
    @FXML
    Button Prometheus;
    @FXML
    Button Zeus;

    @FXML
    public void selectGod(ActionEvent actionEvent){
        if(cont<matchSetupMessage.getPlayers().size()) {
            cont++;
            Button pos = (Button) actionEvent.getSource();
            int selectedGod = calcInt(pos.getId());
            pos.setDisable(true);
            selectedGods[cont-1] = selectedGod;

        }
        if(cont == matchSetupMessage.getPlayers().size()){
            Message message = new Message(client.getUsername());
            message.buildSynchronizationMessage(SecondHeaderType.BEGIN_MATCH, new GameGodsSelectionMessage(selectedGods));
            client.getNetworkHandler().send(message);
        }
    }

    public static Integer calcInt(String name){
        switch (name){
            case "Apollo" :
                return GodFactotum.APOLLO.getCode();
            case "Ares" :
                return GodFactotum.ARES.getCode();
            case "Artemis" :
                return GodFactotum.ARTEMIS.getCode();
            case "Athena" :
                return GodFactotum.ATHENA.getCode();
            case "Atlas" :
                return GodFactotum.ATLAS.getCode();
            case "Chronus" :
                return GodFactotum.CHRONUS.getCode();
            case "Demeter" :
                return GodFactotum.DEMETER.getCode();
            case "Hephaestus" :
                return GodFactotum.HEPHAESTUS.getCode();
            case "Hestia" :
                return GodFactotum.HESTIA.getCode();
            case "Minotaur" :
                System.out.println("ciao");
                return GodFactotum.MINOTAUR.getCode();
            case "Pan" :
                return GodFactotum.PAN.getCode();
            case "Poseidon" :
                return GodFactotum.POSEIDON.getCode();
            case "Prometheus" :
                return GodFactotum.PROMETHEUS.getCode();
            case "Zeus" :
                return GodFactotum.ZEUS.getCode();
            default:
                System.out.println("sei stronzo");
                return null;
        }
    }

}
