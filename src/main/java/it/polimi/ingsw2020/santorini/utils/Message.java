package it.polimi.ingsw2020.santorini.utils;

import com.google.gson.Gson;
import it.polimi.ingsw2020.santorini.utils.messages.*;

import java.io.Serializable;

public class Message implements Serializable {

    private static final long serialVersionUID = 5140286385535003407L;

    private FirstHeaderType firstLevelHeader;
    private SecondHeaderType secondLevelHeader;
    private String serializedPayload;

    public void buildLoginMessage(LoginMessage payload){
        Gson gson = new Gson();
        this.firstLevelHeader = FirstHeaderType.SETUP;
        this.secondLevelHeader = SecondHeaderType.LOGIN;
        this.serializedPayload = gson.toJson(payload);
    }

    public LoginMessage deserializeLoginMessage(String payload){
        Gson gson = new Gson();
        return gson.fromJson(payload, LoginMessage.class);
    }

    public void buildUsernameErrorMessage(UsernameErrorMessage payload){
        Gson gson = new Gson();
        this.firstLevelHeader = FirstHeaderType.ERROR;
        this.secondLevelHeader = SecondHeaderType.USERNAME_ERROR;
        this.serializedPayload = gson.toJson(payload);
    }

    public UsernameErrorMessage deserializeUsernameErrorMessage(String payload){
        Gson gson = new Gson();
        return gson.fromJson(payload, UsernameErrorMessage.class);
    }

/*
    public void buildSampleMessage(SampleMessage payload){
        Gson gson = new Gson();
        this.firstLevelHeader = FirstHeaderType.PROVA;
        this.secondLevelHeader = null;
        this.serializedPayload = gson.toJson(payload);
    }

    public SampleMessage deserializeSampleMessage(String payload){
        Gson gson = new Gson();
        return gson.fromJson(payload, SampleMessage.class);
    }

    public void buildSampleMessage2(SampleMessage2 payload){
        Gson gson = new Gson();
        this.firstLevelHeader = FirstHeaderType.PROVA2;
        this.secondLevelHeader = null;
        this.serializedPayload = gson.toJson(payload);
    }

    public SampleMessage2 deserializeSampleMessage2(String payload){
        Gson gson = new Gson();
        return gson.fromJson(payload, SampleMessage2.class);
    }
 */

    public FirstHeaderType getFirstLevelHeader() {
        return firstLevelHeader;
    }

    public SecondHeaderType getSecondLevelHeader() { return secondLevelHeader; }

    public String getSerializedPayload() {
        return serializedPayload;
    }
}
