package it.polimi.ingsw2020.santorini.model;

import it.polimi.ingsw2020.santorini.utils.Color;
import it.polimi.ingsw2020.santorini.utils.PhaseType;
import it.polimi.ingsw2020.santorini.utils.PlayerStatus;

import java.util.Date;

public class Player {
    private String nickname;
    private Date birthDate;
    private Color color;
    private PlayerStatus status;
    private GodCard divinePower ;
    private Builder builderF;
    private Builder builderM;
    private Builder playingBuilder;
    private Boolean riseActions;
    private Boolean moveActions;

    //METODI        ->GETTER e SETTER<-

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public Date getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(Date birthdate) {
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

    public Builder getPlayingBuilder() {
        return playingBuilder;
    }

    public void setPlayingBuilder(Builder playingBuilder) {
        this.playingBuilder = playingBuilder;
    }

    public Boolean getRiseActions() {
        return riseActions;
    }

    public void setRiseActions(Boolean riseActions) {
        this.riseActions = riseActions;
    }

    public Boolean getMoveActions() {
        return moveActions;
    }

    public void setMoveActions(Boolean moveActions) {
        this.moveActions = moveActions;
    }

    //COSTRUTTORE

    public Player(String nickname, Date birthDate){
        this.nickname = nickname;
        this.birthDate = birthDate;
        this.color = null;
        this.status = PlayerStatus.WAITING;
        this.divinePower =  null;

        //metodo per i builder
        this.builderF =null;
        this.builderM =null;
        riseActions=true;
        moveActions=true;
    }

    public Builder getBuilderF() {
        return builderF;
    }

    public void setBuilderF(Builder builderF) {
        this.builderF = builderF;
    }

    public Builder getBuilderM() {
        return builderM;
    }

    public void setBuilderM(Builder builderM) {
        this.builderM = builderM;
    }

    public boolean canMove(){
        return builderF.canMove() || builderM.canMove();
    }

    public boolean canBuild(){
        return builderF.canBuild() || builderM.canBuild();
    }

    @Override
    public String toString(){
        return color+"Username: "+nickname+"\nBirthDate: "+birthDate+"\nGod: "+divinePower.getName();
    }

}
