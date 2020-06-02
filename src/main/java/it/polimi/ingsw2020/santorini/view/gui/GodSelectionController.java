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
import javafx.scene.control.Tooltip;
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

    private int cont = 0;

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
    Button Demeter;
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
        if(cont < matchSetupMessage.getPlayers().size()) {
            Button pos = (Button) actionEvent.getSource();
            int selectedGod = calcInt(pos.getId());
            pos.setDisable(true);
            selectedGods[cont] = selectedGod;
            cont++;
        }
        if(cont == matchSetupMessage.getPlayers().size()){
            Message message = new Message(client.getUsername());
            message.buildSynchronizationMessage(SecondHeaderType.BEGIN_MATCH, new GameGodsSelectionMessage(selectedGods));
            client.getNetworkHandler().send(message);
            ++cont;
        }
    }

    @FXML
    public void initialize(){
        Tooltip tooltipApollo = new Tooltip();
        tooltipApollo.setText("Your Worker may move into an opponent Worker’s space\n" +
                "(using normal movement rules) and force their Worker to the space yours\n" +
                        "just vacated (swapping their positions).");
        Apollo.setTooltip(tooltipApollo);
        Tooltip tooltipAres = new Tooltip();
        tooltipAres.setText("You may remove an unoccupied block\n" +
                "(not dome) neighboring your unmoved Worker");
        Ares.setTooltip(tooltipAres);
        Tooltip tooltipArtemis = new Tooltip();
        tooltipArtemis.setText("Your Worker may move one additional time,\n" +
                "but not back to the space it started on.");
        Artemis.setTooltip(tooltipArtemis);
        Tooltip tooltipAthena = new Tooltip();
        tooltipAthena.setText("If one of your Workers moved up on your last turn, \n" +
                "opponent Workers cannot move up this turn.");
        Athena.setTooltip(tooltipAthena);
        Tooltip tooltipAtlas = new Tooltip();
        tooltipAtlas.setText("Your Worker may build a dome\n" +
                "at any level including the ground.");
        Atlas.setTooltip(tooltipAtlas);
        Tooltip tooltipChronus = new Tooltip();
        tooltipChronus.setText("You also win\n" +
                "when there are at least five\n" +
                        "Complete Towers on the board..");
        Chronus.setTooltip(tooltipChronus);
        Tooltip tooltipDemeter = new Tooltip();
        tooltipDemeter.setText("Your Worker may build one additional time,\n" +
                "but not on the same space.");
        Demeter.setTooltip(tooltipDemeter);
        Tooltip tooltipHephaestus = new Tooltip();
        tooltipHephaestus.setText("Your Worker may build one additional block\n" +
                "(not dome) on top of your first block.");
        Hephaestus.setTooltip(tooltipHephaestus);
        Tooltip tooltipHestia = new Tooltip();
        tooltipHestia.setText("Your Worker may build one additional time.\n" +
                "The additional build cannot be on a perimeter space.");
        Hestia.setTooltip(tooltipHestia);
        Tooltip tooltipMinotaur = new Tooltip();
        tooltipMinotaur.setText("Your Worker may move into an opponent Worker’s\n" +
                "space (using normal movement rules), if the next space in the same direction is\n" +
                        "unoccupied. Their Worker is forced into that space (regardless of its level).");
        Minotaur.setTooltip(tooltipMinotaur);
        Tooltip tooltipPan = new Tooltip();
        tooltipPan.setText("You also win if your Worker moves down two or more levels.");
        Pan.setTooltip(tooltipPan);
        Tooltip tooltipPoseidon = new Tooltip();
        tooltipPoseidon.setText("If your unmoved Worker is on the ground level,\n" +
                "it may build up to three times in neighboring spaces.");
        Poseidon.setTooltip(tooltipPoseidon);
        Tooltip tooltipPrometheus = new Tooltip();
        tooltipPrometheus.setText("If your Worker does not move up,\n" +
                "it may build both before and after moving.");
        Prometheus.setTooltip(tooltipPrometheus);
        Tooltip tooltipZeus = new Tooltip();
        tooltipZeus.setText("Your Worker may build under itself in its current\n" +
                "space, forcing it up one level. You do not win by forcing yourself up to the third level.");
        Zeus.setTooltip(tooltipZeus);
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
                System.out.println("Errore");
                return null;
        }
    }

}
