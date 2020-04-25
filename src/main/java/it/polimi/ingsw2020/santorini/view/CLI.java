package it.polimi.ingsw2020.santorini.view;

import it.polimi.ingsw2020.santorini.model.Cell;
import it.polimi.ingsw2020.santorini.model.Player;
import it.polimi.ingsw2020.santorini.network.client.Client;
import it.polimi.ingsw2020.santorini.network.client.ServerAdapter;
import it.polimi.ingsw2020.santorini.network.client.ViewAdapter;
import it.polimi.ingsw2020.santorini.utils.AccessType;
import it.polimi.ingsw2020.santorini.utils.LevelType;
import it.polimi.ingsw2020.santorini.utils.Message;
import it.polimi.ingsw2020.santorini.utils.messages.*;

import java.io.IOException;
import java.text.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.Scanner;

@SuppressWarnings("deprecation")

public class CLI implements ViewInterface{

    private Client client;
    private Scanner scannerIn;

    public CLI(Client client){
        this.client = client;
        this.scannerIn = new Scanner(System.in);
    }

    /**
     * method in which it's asked to the client to insert server's IP, and after that the username, birth date and type of match(number of players)
     * metodo in cui si chiede l'iP del server, dopodichè di fanno inserire username, data di nascita e tipo di partita (numero di giocatori nella partita)
     */
    @Override
    public void displaySetupWindow() {
        System.out.printf("Inserisci l'indirizzo IP del server: ");
        String ip = scannerIn.nextLine();

        client.setNetworkHandler(new ServerAdapter(client, ip));
        client.setViewHandler(new ViewAdapter(client));

        client.getNetworkHandler().start();
        client.getViewHandler().start();

        System.out.printf("Inserisci il tuo username: ");
        client.setUsername(scannerIn.nextLine());

        System.out.printf("Inserisci la tua data di nascita (dd/mm/yyyy): ");
        String date = scannerIn.nextLine();
        DateFormat parser = new SimpleDateFormat("dd/MM/yyyy");
        client.setBirthDate(new Date(1900, 1, 1));
        try {
            client.setBirthDate(parser.parse(date));
        } catch (ParseException e) {
            // do nothing
        }

        System.out.printf("Inserisci il numero di giocatori della partita (2 o 3): ");
        client.setSelectedMatch(scannerIn.nextInt());
        scannerIn.nextLine();

        Message message = new Message(client.getUsername());
        message.buildLoginMessage(new LoginMessage(client.getUsername(), client.getBirthDate(), client.getSelectedMatch()));
        client.getNetworkHandler().send(message);
    }

    /**
     * method that re-ask the client to insert a username
     */
    @Override
    public void displayNewUsernameWindow() {
        System.out.printf("Inserisci di nuovo il tuo username: ");
        client.setUsername(scannerIn.nextLine());
        Message message = new Message(client.getUsername());
        message.buildLoginMessage(new LoginMessage(client.getUsername(), client.getBirthDate(), client.getSelectedMatch()));
        client.getNetworkHandler().send(message);

    }

    /**
     * method that display a Loading window to the client while the server waits other clients to join
     * metodo per intrattenere l'utente mentre aspettiamo altri utenti che vogliono giocare
     */
    @Override
    public void displayLoadingWindow(String message) {
        System.out.println(message);
    }

    /**
     * method that gives the welcome to the clients and distributes color of the builders and Gods'cards
     * metodo in cui si da il welcome alla partita, vengono assegnate le carte e i colori.
     * viene visualizzata una board semplificata per facilitare il posizionamento delle pedine
     */
    @Override
    public void displayMatchSetupWindow(MatchSetupMessage matchSetupMessage) {
        System.out.println("Giocatori della partita:\n");
        ArrayList<Player> listOfPlayers = matchSetupMessage.getPlayers();
        for(Player player : listOfPlayers) {
            System.out.printf("Username: %s\nGod:\n%s\nColor: %s\n", player.getNickname(), player.getDivinePower().toStringEffect(), player.getColor());
        }
        System.out.println("\n\nBoard:\n");
        ArrayList<Cell> listOfCells = matchSetupMessage.getCells();

        System.out.printf( "        0     1     2     3     4     5     6\n" +
                            "     █═════╦═════╦═════╦═════╦═════╦═════╦═════█");
        int j = 0;
        for(int i = 0; i < listOfCells.size(); ++i){
            if(i % 7 == 0){
                if(i == 0)  System.out.printf("\n  %d  ║  X  ║", i%7);
                else System.out.printf( "\n     ╠═════╬═════╬═════╬═════╬═════╬═════╬═════╣\n" +
                                        "  %d  ║  X  ║", ++j);
            } else {
                if(listOfCells.get(i).getLevel() == LevelType.COAST) System.out.printf("  X  ║");
                else{
                    if(listOfCells.get(i).getStatus() == AccessType.OCCUPIED){
                        System.out.printf(" %d %c ║", listOfCells.get(i).getLevel().getHeight(), "B");
                    }
                    else {
                        System.out.printf("  %d  ║", listOfCells.get(i).getLevel().getHeight());
                    }
                }
            }
        }
        System.out.printf("\n     █═════╩═════╩═════╩═════╩═════╩═════╩═════█\n");
/*
        System.out.println(     "        0     1     2     3     4     5     6\n" +
                                "     █═════╦═════╦═════╦═════╦═════╦═════╦═════█\n" +
                                "  0  ║  X  ║  X  ║  X  ║  X  ║  X  ║  X  ║  X  ║\n" +
                                "     ╠═════╬═════╬═════╬═════╬═════╬═════╬═════╣\n" +
                                "  1  ║  X  ║ % % ║ % % ║ % % ║ % % ║ % % ║  X  ║\n" +
                                "     ╠═════╬═════╬═════╬═════╬═════╬═════╬═════╣\n" +
                                "  2  ║  X  ║ %c %c ║ %c %c ║ %c %c ║ %c %c ║ %c %c ║  X  ║\n" +
                                "     ╠═════╬═════╬═════╬═════╬═════╬═════╬═════╣\n" +
                                "  3  ║  X  ║ %c %c ║ %c %c ║ %c %c ║ %c %c ║ %c %c ║  X  ║\n" +
                                "     ╠═════╬═════╬═════╬═════╬═════╬═════╬═════╣\n" +
                                "  4  ║  X  ║ %c %c ║ %c %c ║ %c %c ║ %c %c ║ %c %c ║  X  ║\n" +
                                "     ╠═════╬═════╬═════╬═════╬═════╬═════╬═════╣\n" +
                                "  5  ║  X  ║ %c %c ║ %c %c ║ %c %c ║ %c %c ║ %c %c ║  X  ║\n" +
                                "     ╠═════╬═════╬═════╬═════╬═════╬═════╬═════╣\n" +
                                "  6  ║  X  ║  X  ║  X  ║  X  ║  X  ║  X  ║  X  ║\n" +
                                "     █═════╩═════╩═════╩═════╩═════╩═════╩═════█\n");
*/
        System.out.println("\n\nE' ora di scegliere la posizione dei builder! inizierà il primo giocatore a scegliere!");
        System.out.println("Abbiamo ordinato in base all'età, così gli eletti di Dioniso avranno un piccolo vantaggio!");
        System.out.println("L'ordine voluto dagli dei è questo: ");
        for(Player p : listOfPlayers) System.out.println(p.getNickname());
        System.out.println("Attendi le direttive degli dei");

        // bisogna creare un messaggio che dica che i client siano correttamente entrati nella partita
        // il server manderà uno alla volta i messaggi di scelta delle posizioni dei builder ai client nell'ordine prestabilito (verranno inviati a tutti i componenti della partita)
        // il payload del messaggio inviato dal server conterrà il giocatore che deve scegliere
        // verrà invocato il display choices per le scelte e ci sarà un if fondamentale:
        // se il nome del giocatore corrisponde a quello nel payload, inizierà la procedura di scelta
        // altrimenti comparirà "nome nel payload sta scegliendo"

        Message message = new Message(client.getUsername());
        message.buildAskSelectionOrderMessage();
        client.getNetworkHandler().send(message);

        if(client.getUsername().equals(listOfPlayers.get(0).getNickname())) {
            System.out.println("%s, tocca a te! Dovrai inserire le coordinate di due celle per posizionare i tuoi costruttori!");
            for(int i = 0; i < 2; ++i){
                System.out.println("iniziamo con il costruttore");
                System.out.printf("Inserisci la riga: ");
                scannerIn.nextInt();
                scannerIn.nextLine();
                System.out.printf("Inserisci la colonna: ");
                scannerIn.nextInt();
                scannerIn.nextLine();
            }
        } else {
            System.out.printf("%s, sta scegliendo la posizione dei suoi builder! Attendi...", listOfPlayers.get(0).getNickname());
        }

    }

    /**
     * method that shows board, builders, the textual interface and the first player to play
     * far visualizzare la board con le pedine e tutta l'interfaccia testuale e il primo giocatore che gioca
     */
    @Override
    public void displayMatchStart() {

    }

    /**
     * method that update the board every time that the model is modified
     * metodo che aggiorna la board ogni volta che viene fatta una mossa (modificato il model)
     * parametro un messaggio con scritte le informazioni sulla board.
     */
    @Override
    public void updateMatch() {

    }

    /**
     * method that shows to the player his possible moves
     * metodo che mostra all'utente le possibili mosse che il builder selezionato può fare
     */
    @Override
    public void displayPossibleMoves() {

    }

    /**
     * method that shows to the player the possible block that his builder can do
     * metodo che mostra all'utente le possibili costruzioni che il builder mosso può fare
     */
    @Override
    public void displayPossibleBuildings() {

    }

    /**
     * method that shows winner and losers. It then close the match
     * metodo che mostra vincitori e vinti. conclude la partita con epic sax guy
     */
    @Override
    public void displayEndMatch() {

    }

    /**
     * method that shows possible errors occurred
     * metodo che mostra all'utente possibili errori che sono capitati
     */
    @Override
    public void displayErrorMessage(String error) {
        System.out.println(error);
        System.out.println("Press any key to proceed");
        scannerIn.nextLine();
    }
}
