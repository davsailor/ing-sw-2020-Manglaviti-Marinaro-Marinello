package it.polimi.ingsw2020.santorini.view;

import com.sun.org.apache.bcel.internal.classfile.SourceFile;
import it.polimi.ingsw2020.santorini.network.client.Client;
import it.polimi.ingsw2020.santorini.network.client.ServerAdapter;
import it.polimi.ingsw2020.santorini.utils.Message;
import it.polimi.ingsw2020.santorini.utils.messages.*;

import java.io.IOException;
import java.text.*;
import java.util.Date;
import java.util.Scanner;

@SuppressWarnings("deprecation")

public class CLI implements ViewInterface{

    private Client client;
    private Scanner scannerIn;
    private String username;
    private Date birthDate;
    private int selectedMatch;

    public CLI(Client client){
        this.client = client;
        this.scannerIn = new Scanner(System.in);
    }

    /**
     * metodo in cui si chiede l'iP del server, dopodichè di fanno inserire username, data di nascita e tipo di partita (numero di giocatori nella partita)
     */
    @Override
    public void displaySetupWindow() {
        System.out.printf("Inserisci l'indirizzo IP del server: ");
        String ip = scannerIn.nextLine();

        ServerAdapter adapter = new ServerAdapter(client, ip);
        client.setAdapter(adapter);
        adapter.start();

        System.out.printf("Inserisci il tuo username: ");
        username = scannerIn.nextLine();
        System.out.printf("Inserisci la tua data di nascita (dd/mm/yyyy): ");
        String date = scannerIn.nextLine();
        DateFormat parser = new SimpleDateFormat("dd/MM/yyyy");
        birthDate = new Date(1900, 1, 1);
        try {
            birthDate = parser.parse(date);
        } catch (ParseException e) {
            // do nothing
        }
        System.out.printf("Inserisci il numero di giocatori della partita (2 o 3): ");
        selectedMatch= scannerIn.nextInt();

        Message message = new Message();
        message.buildLoginMessage(new LoginMessage(username, birthDate, selectedMatch));

        try {
            client.getAdapter().send(message);
        } catch (IOException e) {
            // do nothing
        }
    }

    @Override
    public void displayNewUsernameWindow() {
        System.out.printf("Inserisci il tuo username: ");
        username = scannerIn.nextLine();
        Message message = new Message();
        message.buildLoginMessage(new LoginMessage(username, birthDate, selectedMatch));
        try {
            client.getAdapter().send(message);
        } catch (IOException e) {
            // do nothing
        }
    }

    /**
     * metodo per intrattenere l'utente mentre aspettiamo altri carusi che vogliono giocare
     */
    @Override
    public void displayLoadingWindow() {

    }

    /**
     * metodo in cui si da il welcome alla partita, vengono assegnate le carte e i colori.
     * viene visualizzata una board semplificata per facilitare il posizionamento delle pedine
     */
    @Override
    public void displayChoices() {

    }

    /**
     * far visualizzare la board con le pedine e tutta l'interfaccia testuale e il primo giocatore che gioca
     */
    @Override
    public void displayMatchStart() {

    }

    /**
     * metodo che aggiorna la board ogni volta che viene fatta una mossa (modificato il model)
     * parametro un messaggio con scritte le informazioni sulla board.
     */
    @Override
    public void updateMatch() {

    }

    /**
     * metodo che mostra all'utente le possibili mosse che il builder selezionato può fare
     */
    @Override
    public void displayPossibleMoves() {

    }

    /**
     * metodo che mostra all'utente le possibili costruzioni che il builder mosso può fare
     */
    @Override
    public void displayPossibleBuildings() {

    }

    /**
     * metodo che mostra vincitori e vinti. conclude la partita con epic sax guy
     */
    @Override
    public void displayEndMatch() {

    }

    /**
     * metodo che mostra all'utente possibili errori che sono capitati
     */
    @Override
    public void displayErrorMessage(String error) {
        // stampa a video l'errore fatto, poi bisogna premere un tasto per continuare
    }
/*
    public void displaySample() {

        Message messageOut = new Message();
        SampleMessage payload = new SampleMessage("SampleMessage inviato dalla CLI al Server");
        messageOut.buildSampleMessage(payload);
        try {
            client.getAdapter().send(messageOut);
        } catch(IOException e) {
            System.out.println("FUCK!");
        }
    }

    @Override
    public void displaySample2() {
        System.out.println("LA CARNE E' TENERISSIMA!");
        try {
            client.getAdapter().getServer().close();
        } catch (IOException e){
            System.out.println("FUCK CLI!");
        }
    }

 */
}
