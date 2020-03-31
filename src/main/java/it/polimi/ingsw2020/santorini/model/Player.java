package it.polimi.ingsw2020.santorini.model;

import java.util.Date;

public class Player {
    private String nickname;
    private Date birthdate;
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
        return birthdate;
    }

    public void setBirthdate(Date birthdate) {
        this.birthdate = birthdate;
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

    public Player(String nickname, Date birth, Color color,Builder[] Builders){        //devo passare i builder nel costruttore?
        this.nickname = nickname;
        this.birthdate = birthdate;
        this.color = color;
        this.status = PlayerStatus.WAITING;
        this.divinePower =  null;

        //metodo per i builder
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
}
