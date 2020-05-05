package it.polimi.ingsw2020.santorini.utils;

import com.google.gson.Gson;
import it.polimi.ingsw2020.santorini.utils.messages.actions.*;
import it.polimi.ingsw2020.santorini.utils.messages.errors.*;
import it.polimi.ingsw2020.santorini.utils.messages.godsParam.*;
import it.polimi.ingsw2020.santorini.utils.messages.matchMessage.*;

import java.io.Serializable;

public class Message implements Serializable {

    private static final long serialVersionUID = 5140286385535003407L;

    private String username;
    private FirstHeaderType firstLevelHeader;
    private SecondHeaderType secondLevelHeader;
    private String serializedPayload;

    public Message(String username){
        this.username = username;
    }

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

    public void buildCorrectLoginMessage(CorrectLoginMessage payload){
        Gson gson = new Gson();
        this.firstLevelHeader = FirstHeaderType.LOADING;
        this.secondLevelHeader = SecondHeaderType.LOGIN;
        this.serializedPayload = gson.toJson(payload);
    }

    public CorrectLoginMessage deserializeCorrectLoginMessage(String payload){
        Gson gson = new Gson();
        return gson.fromJson(payload, CorrectLoginMessage.class);
    }

    public void buildMatchSetupMessage(MatchSetupMessage payload){
        Gson gson = new Gson();
        this.firstLevelHeader = FirstHeaderType.SETUP;
        this.secondLevelHeader = SecondHeaderType.MATCH;
        this.serializedPayload = gson.toJson(payload);
    }

    public MatchSetupMessage deserializeMatchSetupMessage(String payload){
        Gson gson = new Gson();
        return gson.fromJson(payload, MatchSetupMessage.class);
    }

    public void buildSynchronizationMessage(SecondHeaderType type){
        this.firstLevelHeader = FirstHeaderType.SYNCHRONIZATION;
        this.secondLevelHeader = type;
    }

    public void buildTurnPlayerMessage(TurnPlayerMessage payload){
        Gson gson = new Gson();
        this.firstLevelHeader = FirstHeaderType.SETUP;
        this.secondLevelHeader = SecondHeaderType.PLAYER_SELECTION;
        this.serializedPayload = gson.toJson(payload);
    }

    public TurnPlayerMessage deserializeTurnPlayerMessage(String payload) {
        Gson gson = new Gson();
        return gson.fromJson(payload, TurnPlayerMessage.class);
    }

    public void buildSelectedBuilderPosMessage(SelectedBuilderPositionMessage payload){
        Gson gson = new Gson();
        this.firstLevelHeader = FirstHeaderType.VERIFY;
        this.secondLevelHeader = SecondHeaderType.CORRECT_SELECTION_POS;
        this.serializedPayload = gson.toJson(payload);
    }

    public SelectedBuilderPositionMessage deserializeSelectedBuilderPosMessage(String payload) {
        Gson gson = new Gson();
        return gson.fromJson(payload, SelectedBuilderPositionMessage.class);
    }

    public void buildIllegalPositionMessage(IllegalPositionMessage payload){
        Gson gson = new Gson();
        this.firstLevelHeader = FirstHeaderType.ERROR;
        this.secondLevelHeader = SecondHeaderType.INVALID_CELL_SELECTION;
        this.serializedPayload = gson.toJson(payload);
    }

    public IllegalPositionMessage deserializeIllegalPositionMessage(String payload) {
        Gson gson = new Gson();
        return gson.fromJson(payload, IllegalPositionMessage.class);
    }

    public void buildUpdateMessage(UpdateMessage payload){
        Gson gson = new Gson();
        this.firstLevelHeader = FirstHeaderType.LOADING;
        this.secondLevelHeader = SecondHeaderType.MATCH;
        this.serializedPayload = gson.toJson(payload);
    }

    public UpdateMessage deserializeUpdateMessage(String payload) {
        Gson gson = new Gson();
        return gson.fromJson(payload, UpdateMessage.class);
    }

    public void buildNextPhaseMessage(NextPhaseMessage payload) {
        Gson gson = new Gson();
        this.firstLevelHeader = FirstHeaderType.DO;
        this.secondLevelHeader = SecondHeaderType.NEXT_PHASE;
        this.serializedPayload = gson.toJson(payload);
    }

    public NextPhaseMessage deserializeNextPhaseMessage(String payload) {
        Gson gson = new Gson();
        return gson.fromJson(payload, NextPhaseMessage.class);
    }

    public void buildWouldActivateGodMessage(ActivationRequestInfoMessage payload){
        Gson gson = new Gson();
        this.firstLevelHeader = FirstHeaderType.ASK;
        this.secondLevelHeader = SecondHeaderType.ACTIVATE_GOD;
        this.serializedPayload = gson.toJson(payload);
    }

    public void buildSelectParametersMessage(ActivationRequestInfoMessage payload) {
        Gson gson = new Gson();
        this.firstLevelHeader = FirstHeaderType.ASK;
        this.secondLevelHeader = SecondHeaderType.SELECT_PARAMETERS;
        this.serializedPayload = gson.toJson(payload);
    }

    public void buildSelectBuilderMessage(TurnPlayerMessage payload){
        Gson gson = new Gson();
        this.firstLevelHeader = FirstHeaderType.ASK;
        this.secondLevelHeader = SecondHeaderType.SELECT_BUILDER;
        this.serializedPayload = gson.toJson(payload);
    }

    public ActivationRequestInfoMessage deserializeActivationRequestInfoMessage(String payload){
        Gson gson = new Gson();
        return gson.fromJson(payload, ActivationRequestInfoMessage.class);
    }

    public void buildActivateGodMessage(ActivateGodMessage payload) {
        Gson gson = new Gson();
        this.firstLevelHeader = FirstHeaderType.DO;
        this.secondLevelHeader = SecondHeaderType.ACTIVATE_GOD;
        this.serializedPayload = gson.toJson(payload);
    }

    public ActivateGodMessage deserializeActivateGodMessage(String payload) {
        Gson gson = new Gson();
        return gson.fromJson(payload, ActivateGodMessage.class);
    }

    public void buildSelectedBuilderMessage(SelectedBuilderMessage payload){
        Gson gson = new Gson();
        this.firstLevelHeader = FirstHeaderType.DO;
        this.secondLevelHeader = SecondHeaderType.SELECT_BUILDER;
        this.serializedPayload = gson.toJson(payload);
    }

    public SelectedBuilderMessage deserializeSelectedBuilderMessage(){
        Gson gson = new Gson();
        return gson.fromJson(this.serializedPayload, SelectedBuilderMessage.class);
    }

    public void buildInvalidParametersMessage(InvalidParametersMessage payload){
        Gson gson = new Gson();
        this.firstLevelHeader = FirstHeaderType.ERROR;
        this.secondLevelHeader = SecondHeaderType.INVALID_PARAMETERS;
        this.serializedPayload = gson.toJson(payload);
    }

    public InvalidParametersMessage deserializeInvalidParametersMessage(){
        Gson gson = new Gson();
        return gson.fromJson(this.serializedPayload, InvalidParametersMessage.class);
    }

    public void buildApolloParamMessage(ApolloParamMessage payload) {
        Gson gson = new Gson();
        this.firstLevelHeader = FirstHeaderType.DO;
        this.secondLevelHeader = SecondHeaderType.SELECT_PARAMETERS;
        this.serializedPayload = gson.toJson(payload);
    }

    public ApolloParamMessage deserializeApolloParamMessage(String payload) {
        Gson gson = new Gson();
        return gson.fromJson(payload, ApolloParamMessage.class);
    }

    public void buildAresParamMessage(AresParamMessage payload) {
        Gson gson = new Gson();
        this.firstLevelHeader = FirstHeaderType.DO;
        this.secondLevelHeader = SecondHeaderType.SELECT_PARAMETERS;
        this.serializedPayload = gson.toJson(payload);
    }

    public AresParamMessage deserializeAresParamMessage() {
        Gson gson = new Gson();
        return gson.fromJson(this.serializedPayload, AresParamMessage.class);
    }

    public void buildArtemisParamMessage(ArtemisParamMessage payload) {
        Gson gson = new Gson();
        this.firstLevelHeader = FirstHeaderType.DO;
        this.secondLevelHeader = SecondHeaderType.SELECT_PARAMETERS;
        this.serializedPayload = gson.toJson(payload);
    }

    public ArtemisParamMessage deserializeArtemisParamMessage() {
        Gson gson = new Gson();
        return gson.fromJson(this.serializedPayload, ArtemisParamMessage.class);
    }

    public void buildAtlasParamMessage(AtlasParamMessage payload) {
        Gson gson = new Gson();
        this.firstLevelHeader = FirstHeaderType.DO;
        this.secondLevelHeader = SecondHeaderType.SELECT_PARAMETERS;
        this.serializedPayload = gson.toJson(payload);
    }

    public AtlasParamMessage deserializeAtlasParamMessage() {
        Gson gson = new Gson();
        return gson.fromJson(this.serializedPayload, AtlasParamMessage.class);
    }

    public void buildDemeterParamMessage(DemeterParamMessage payload) {
        Gson gson = new Gson();
        this.firstLevelHeader = FirstHeaderType.DO;
        this.secondLevelHeader = SecondHeaderType.SELECT_PARAMETERS;
        this.serializedPayload = gson.toJson(payload);
    }

    public DemeterParamMessage deserializeDemeterParamMessage() {
        Gson gson = new Gson();
        return gson.fromJson(this.serializedPayload, DemeterParamMessage.class);
    }

    public void buildHestiaParamMessage(HestiaParamMessage payload) {
        Gson gson = new Gson();
        this.firstLevelHeader = FirstHeaderType.DO;
        this.secondLevelHeader = SecondHeaderType.SELECT_PARAMETERS;
        this.serializedPayload = gson.toJson(payload);
    }

    public HestiaParamMessage deserializeHestiaParamMessage() {
        Gson gson = new Gson();
        return gson.fromJson(this.serializedPayload, HestiaParamMessage.class);
    }

    public void buildMinotaurParamMessage(MinotaurParamMessage payload) {
        Gson gson = new Gson();
        this.firstLevelHeader = FirstHeaderType.DO;
        this.secondLevelHeader = SecondHeaderType.SELECT_PARAMETERS;
        this.serializedPayload = gson.toJson(payload);
    }

    public MinotaurParamMessage deserializeMinotaurParamMessage() {
        Gson gson = new Gson();
        return gson.fromJson(this.serializedPayload, MinotaurParamMessage.class);
    }

    public void buildPoseidonParamMessage(PoseidonParamMessage payload) {
        Gson gson = new Gson();
        this.firstLevelHeader = FirstHeaderType.DO;
        this.secondLevelHeader = SecondHeaderType.SELECT_PARAMETERS;
        this.serializedPayload = gson.toJson(payload);
    }

    public PoseidonParamMessage deserializePoseidonParamMessage() {
        Gson gson = new Gson();
        return gson.fromJson(this.serializedPayload, PoseidonParamMessage.class);
    }

    public void buildPrometheusParamMessage(PrometheusParamMessage payload) {
        Gson gson = new Gson();
        this.firstLevelHeader = FirstHeaderType.DO;
        this.secondLevelHeader = SecondHeaderType.SELECT_PARAMETERS;
        this.serializedPayload = gson.toJson(payload);
    }

    public PrometheusParamMessage deserializePrometheusParamMessage() {
        Gson gson = new Gson();
        return gson.fromJson(this.serializedPayload, PrometheusParamMessage.class);
    }

    public void buildAskMoveSelectionMessage(AskMoveSelectionMessage payload){
        Gson gson = new Gson();
        this.firstLevelHeader = FirstHeaderType.ASK;
        this.secondLevelHeader = SecondHeaderType.SELECT_CELL_MOVE;
        this.serializedPayload = gson.toJson(payload);
    }

    public AskMoveSelectionMessage deserializeAskMoveSelectionMessage(){
        Gson gson = new Gson();
        return gson.fromJson(this.serializedPayload, AskMoveSelectionMessage.class);
    }

    public void buildSelectedMoveMessage(SelectedMoveMessage payload){
        Gson gson = new Gson();
        this.firstLevelHeader = FirstHeaderType.DO;
        this.secondLevelHeader = SecondHeaderType.SELECT_CELL_MOVE;
        this.serializedPayload = gson.toJson(payload);
    }

    public SelectedMoveMessage deserializeSelectedMoveMessage(){
        Gson gson = new Gson();
        return gson.fromJson(this.serializedPayload, SelectedMoveMessage.class);
    }


    public String getUsername(){ return username; }

    public FirstHeaderType getFirstLevelHeader() {
        return firstLevelHeader;
    }

    public SecondHeaderType getSecondLevelHeader() { return secondLevelHeader; }

    public String getSerializedPayload() {
        return serializedPayload;
    }

    @Override
    public String toString(){
        return firstLevelHeader.toString() + secondLevelHeader.toString();
    }
}
