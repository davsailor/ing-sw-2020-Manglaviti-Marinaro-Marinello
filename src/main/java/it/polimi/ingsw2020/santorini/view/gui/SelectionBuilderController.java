package it.polimi.ingsw2020.santorini.view.gui;

import it.polimi.ingsw2020.santorini.network.client.Client;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Circle;

public class SelectionBuilderController {

    private Client client;

    public void setClient(Client client) {
    }


    @FXML
    Label title;
    @FXML
    Button b00;
    @FXML
    Button b01;
    @FXML
    Button b02;
    @FXML
    Button b03;
    @FXML
    Button b04;
    @FXML
    Button b10;
    @FXML
    Button b11;
    @FXML
    Button b12;
    @FXML
    Button b13;
    @FXML
    Button b14;
    @FXML
    Button b20;
    @FXML
    Button b21;
    @FXML
    Button b22;
    @FXML
    Button b23;
    @FXML
    Button b24;
    @FXML
    Button b30;
    @FXML
    Button b31;
    @FXML
    Button b32;
    @FXML
    Button b33;
    @FXML
    Button b34;
    @FXML
    Button b40;
    @FXML
    Button b41;
    @FXML
    Button b42;
    @FXML
    Button b43;
    @FXML
    Button b44;

    @FXML
    public void setPos(MouseEvent mouseEvent) {
        title.setText("Clicca la cella dove vuoi inserire la tua costruttrice ");
        Node pos = (Node) mouseEvent.getSource();

        if(client.getUsername().equals(currentPlayer)) {
            int[] builderM, builderF;
            builderM = new int[2];
            builderF = new int[2];
            builderF[0]=-1;
            builderF[1]=-1;


             if (pos==b00){
                 builderF[0]=1;
                 builderF[1]=1;
            }
             else if (pos==b01){
                 builderF[0]=1;
                 builderF[1]=2;
             }
             else if (pos==b02){
                 builderF[0]=1;
                 builderF[1]=3;
             }
             else if (pos==b03){
                 builderF[0]=1;
                 builderF[1]=4;
             }
             else if (pos==b04){
                 builderF[0]=1;
                 builderF[1]=5;
             }
             else if (pos==b10){
                 builderF[0]=2;
                 builderF[1]=1;
             }
             else if (pos==b11){
                 builderF[0]=2;
                 builderF[1]=2;
             }
             else if (pos==b12){
                 builderF[0]=2;
                 builderF[1]=3;
             }
             else if (pos==b13){
                 builderF[0]=2;
                 builderF[1]=4;
             }
             else if (pos==b14){
                 builderF[0]=2;
                 builderF[1]=5;
             }
             else if (pos==b20){
                 builderF[0]=3;
                 builderF[1]=1;
             }
             else if (pos==b21){
                 builderF[0]=3;
                 builderF[1]=2;
             }
             else if (pos==b22){
                 builderF[0]=3;
                 builderF[1]=3;
             }
             else if (pos==b23){
                 builderF[0]=3;
                 builderF[1]=4;
             }
             else if (pos==b24){
                 builderF[0]=3;
                 builderF[1]=5;
             }
             else if (pos==b30){
                 builderF[0]=4;
                 builderF[1]=1;
             }
             else if (pos==b31){
                 builderF[0]=4;
                 builderF[1]=2;
             }
             else if (pos==b32){
                 builderF[0]=4;
                 builderF[1]=3;
             }
             else if (pos==b33){
                 builderF[0]=4;
                 builderF[1]=4;
             }
             else if (pos==b34){
                 builderF[0]=4;
                 builderF[1]=5;
             }
             else if (pos==b40){
                 builderF[0]=5;
                 builderF[1]=1;
             }
             else if (pos==b41){
                 builderF[0]=5;
                 builderF[1]=2;
             }
             else if (pos==b42){
                 builderF[0]=5;
                 builderF[1]=3;
             }
             else if (pos==b43){
                 builderF[0]=5;
                 builderF[1]=4;
             }
             else if (pos==b44){
                 builderF[0]=5;
                 builderF[1]=5;
             }
            title.setText("Clicca la cella dove vuoi inserire il tuo costruttore ");



            
        }





    }


}

