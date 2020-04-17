package it.polimi.ingsw2020.santorini.utils;

import com.google.gson.Gson;
import it.polimi.ingsw2020.santorini.utils.messages.*;

import java.io.Serializable;

public class Message implements Serializable {

    private static final long serialVersionUID = 5140286385535003407L;

    private MessageType headerMessageType;
    private String serializedPayload;

    public void buildSampleMessage(SampleMessage payload){
        Gson gson = new Gson();
        this.headerMessageType = MessageType.PROVA;
        this.serializedPayload = gson.toJson(payload);
    }

    public SampleMessage deserializeSampleMessage(String payload){
        Gson gson = new Gson();
        return gson.fromJson(payload, SampleMessage.class);
    }

    public MessageType getHeaderMessageType() {
        return headerMessageType;
    }

    public String getSerializedPayload() {
        return serializedPayload;
    }
}
