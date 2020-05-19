package it.polimi.ingsw2020.santorini.network.client;


import it.polimi.ingsw2020.santorini.utils.Message;
import it.polimi.ingsw2020.santorini.view.AppGUI;
import it.polimi.ingsw2020.santorini.view.CLI;
import it.polimi.ingsw2020.santorini.view.ViewInterface;
import javafx.application.Application;

import java.util.ArrayList;
import java.util.Date;

public class Client {
    private ServerAdapter networkHandler;
    private ViewAdapter viewHandler;
    private ViewInterface view;

    private String username;
    private Date birthDate;
    private int selectedMatch;

    private final ArrayList<Message> messageQueue;

    public Client() {
        messageQueue = new ArrayList<>();
    }

    public static void main(String[] args) {
        if(args.length == 0){
            Application.launch(AppGUI.class);
        } else {
            CLI cli = new CLI();
            cli.displaySetupWindow();
        }
    }

    /*
     * getter and setter of the class
     */
    public ServerAdapter getNetworkHandler() {
        return networkHandler;
    }

    public void setNetworkHandler(ServerAdapter networkHandler) {
        this.networkHandler = networkHandler;
    }

    public ViewAdapter getViewHandler() {
        return viewHandler;
    }

    public void setViewHandler(ViewAdapter viewHandler) {
        this.viewHandler = viewHandler;
    }

    public ViewInterface getView() {
        return view;
    }

    public void setView(ViewInterface view) {
        this.view = view;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Date getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
    }

    public int getSelectedMatch() {
        return selectedMatch;
    }

    public void setSelectedMatch(int selectedMatch) {
        this.selectedMatch = selectedMatch;
    }

    /**
     * synchronized method to remove a message from the queue
     * @param message the message to remove
     */
    synchronized public void removeMessageQueue(Message message){
        messageQueue.remove(message);
        //System.out.println(messageQueue.toString());
    }

    /**
     * synchronized method to add a message from the queue
     * @param message the message to add
     */
    synchronized public void addMessageQueue(Message message) {
        messageQueue.add(message);
        //System.out.println(messageQueue.toString());
    }

    /**
     * synchronized method that inspects the queue of messages
     * @return false if the queue is empty, true otherwise
     */
    synchronized public boolean hasNextMessage(){
        return !messageQueue.isEmpty();
    }

    /**
     * synchronized method that gets the next message of the queue
     * @return the next message of the queue, null if the queue is empty
     */
    synchronized public Message getNextMessage(){
        if(messageQueue.isEmpty()) return null;
        //System.out.println(messageQueue.toString());
        return messageQueue.get(0);
    }
}
