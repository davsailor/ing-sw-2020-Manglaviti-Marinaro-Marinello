package it.polimi.ingsw2020.santorini.model;

import it.polimi.ingsw2020.santorini.model.Builder;
import it.polimi.ingsw2020.santorini.model.GodCard;
import it.polimi.ingsw2020.santorini.utils.Color;
import it.polimi.ingsw2020.santorini.utils.PlayerStatus;

import java.util.Date;

public class Player {
    private String nickname;
    private Date birthDate;
    private Color color;
    private PlayerStatus status;
    private GodCard divinePower ;
    private Builder[] builders;
    private Boolean riseActions;
    private Boolean buildActions;
    private Boolean moveActions;

    //METODI        ->GETTER e SETTER<-

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public Date getBirthdate() {
        return birthDate;
    }

    public void setBirthdate(Date birthdate) {
        this.birthDate = birthdate;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public PlayerStatus getStatus() {
        return status;
    }

    public void setStatus(PlayerStatus status) {
        this.status = status;
    }

    public GodCard getDivinePower() {
        return divinePower;
    }

    public void setDivinePower(GodCard divinePower) {
        this.divinePower = divinePower;
    }

    public void setBuilders(Builder[] builders) {
        this.builders = builders;
    }

    //COSTRUTTORE
    public Player(String nickname, Date birthDate){
        this.nickname = nickname;
        this.birthDate = birthDate;
        this.color = null;
        this.status = PlayerStatus.WAITING;
        this.divinePower =  null;
        builders = new Builder[2];
        builders[0]=null;
        builders[1]=null;
        riseActions=true;
        moveActions=true;
        buildActions=true;
    }

    public void setGod(GodCard god){

    }

    public void tostring(){}

    // public String chooseBuilder(Builder[] Builders){ }

    // public void movePlayer(Builder builder){
    //    builder.move();
    // }
}
