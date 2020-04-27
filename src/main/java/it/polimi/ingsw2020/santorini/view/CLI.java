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
        System.out.println("\n\nE' ora di scegliere la posizione dei builder! inizierà il primo giocatore a scegliere!");
        System.out.println("Abbiamo ordinato in base all'età, i più giovani avranno un piccolo vantaggio!");
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
        message.buildBeginMatchSynMessage();
        client.getNetworkHandler().send(message);
    }

    /**
     * metodo addetto alla selezione dei builder secondo l'ordine definito dal controller
     *
     * @param turnPlayerMessage
     */
    @Override
    public void displaySelectionBuilderWindow(TurnPlayerMessage turnPlayerMessage) {
        String currentPlayer = turnPlayerMessage.getCurrentPlayer();
        if(client.getUsername().equals(currentPlayer)) {
            int[] builderM, builderF;
            builderM = new int[2];
            builderF = new int[2];
            System.out.printf("%s, tocca a te! Dovrai inserire le coordinate di due celle per posizionare i tuoi costruttori!\n", currentPlayer);
            showBoard(turnPlayerMessage.getCells());
            System.out.printf("iniziamo con la costruttrice\n");
            do{
                System.out.printf("Inserisci la riga, deve essere compresa tra 1 e 5 e libera, come puoi vedere dalla board: ");
                builderF[0] = scannerIn.nextInt();
                scannerIn.nextLine();
            } while(builderF[0] < 1 || builderF[0] > 5);
            do{
                System.out.printf("Inserisci la colonna, deve essere compresa tra 1 e 5 e libera, come puoi vedere dalla board: ");
                builderF[1] = scannerIn.nextInt();
                scannerIn.nextLine();
            } while(builderF[1] < 1 || builderF[1] > 5);

            System.out.printf("ora tocca al costruttore\n");
            do{
                System.out.printf("Inserisci la riga, deve essere compresa tra 1 e 5 e libera, come puoi vedere dalla board: ");
                builderM[0] = scannerIn.nextInt();
                scannerIn.nextLine();
            } while(builderM[0] < 1 || builderM[0] > 5);
            do{
                System.out.printf("Inserisci la colonna, deve essere compresa tra 1 e 5 e libera, come puoi vedere dalla board: ");
                builderM[1] = scannerIn.nextInt();
                scannerIn.nextLine();
            } while(builderM[1] < 1 || builderM[1] > 5 || (builderM[1] == builderF[1] && builderM[0] == builderF[0]));

            Message message = new Message(client.getUsername());
            message.buildSelectedBuilderPosMessage(new SelectedBuilderPosMessage(client.getUsername(), builderF, builderM));
            System.out.println("In attesa che gli dei controllino le tue scelte...");
            client.getNetworkHandler().send(message);
        } else {
            System.out.printf("Ok, %s sta scegliendo la posizione dei suoi builder! Attendi...", currentPlayer);
        }
    }

    @Override
    public void displayNewSelectionBuilderWindow(IllegalPositionMessage message){
        int[] builderM = null;
        int[] builderF = null;
        if(message.isBuilderFToChange()){
            builderF = new int[2];
            System.out.printf("la tua costruttrice è in una posizione illegale\n");
            do{
                System.out.printf("Inserisci la riga, deve essere compresa tra 1 e 5 e libera, come puoi vedere dalla board: ");
                builderF[0] = scannerIn.nextInt();
                scannerIn.nextLine();
            } while(builderF[0] < 1 || builderF[0] > 5);
            do{
                System.out.printf("Inserisci la colonna, deve essere compresa tra 1 e 5 e libera, come puoi vedere dalla board: ");
                builderF[1] = scannerIn.nextInt();
                scannerIn.nextLine();
            } while(builderF[1] < 1 || builderF[1] > 5);
        }
        if(message.isBuilderMToChange()) {
            builderM = new int[2];
            System.out.printf("il tuo costruttore è in una posizione illegale\n");
            do {
                System.out.printf("Inserisci la riga, deve essere compresa tra 1 e 5 e libera, come puoi vedere dalla board: ");
                builderM[0] = scannerIn.nextInt();
                scannerIn.nextLine();
            } while (builderM[0] < 1 || builderM[0] > 5);
            do {
                System.out.printf("Inserisci la colonna, deve essere compresa tra 1 e 5 e libera, come puoi vedere dalla board: ");
                builderM[1] = scannerIn.nextInt();
                scannerIn.nextLine();
            } while (builderM[1] < 1 || builderM[1] > 5);
        }

        Message newPos = new Message(client.getUsername());
        newPos.buildSelectedBuilderPosMessage(new SelectedBuilderPosMessage(client.getUsername(), builderF, builderM));
        System.out.println("In attesa che gli dei controllino le tue scelte...");
        client.getNetworkHandler().send(newPos);
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

    public void showBoard(ArrayList<Cell> listOfCells){
        String coast = "  X  ";
        //wave: \uD83C\uDF0A
        //mountain: \u26F0
        //rock: \uD83E\uDEA8
        System.out.println("\n\nBoard:\n");
        System.out.printf(                  "                                 NORTH                \n" +
                "                 0     1     2     3     4     5     6\n" +
                "              █═════╦═════╦═════╦═════╦═════╦═════╦═════█");
        int j = 0;
        for(int i = 0; i < listOfCells.size(); ++i){
            if(i % 7 == 0){
                if(i == 0)  System.out.printf("\n           %d  ║%s║", i%7, coast);  //☠
                else {
                    if(j == 2) {
                        System.out.printf(                                                            "  %d", j);
                        System.out.printf(  "\n              ╠═════╬═════╬═════╬═════╬═════╬═════╬═════╣\n ");
                        System.out.printf(  "    WEST  %d  ║%s║", ++j, coast);
                    }
                    else if(j == 3){
                        System.out.printf(                                                            "  %d  EAST", j);
                        System.out.printf(  "\n              ╠═════╬═════╬═════╬═════╬═════╬═════╬═════╣\n ");
                        System.out.printf(  "          %d  ║%s║", ++j, coast);
                    }
                    else {
                        System.out.printf(                                                            "  %d", j);
                        System.out.printf(  "\n              ╠═════╬═════╬═════╬═════╬═════╬═════╬═════╣\n ");
                        System.out.printf(  "          %d  ║%s║", ++j, coast);
                    }
                }
            } else {
                if(listOfCells.get(i).getLevel() == LevelType.COAST) System.out.printf("%s║", coast);
                else{
                    if(listOfCells.get(i).getStatus() == AccessType.OCCUPIED){
                        System.out.printf(" %d%2c ║", listOfCells.get(i).getLevel().getHeight(), listOfCells.get(i).getBuilder().getGender());
                    }
                    else {
                        System.out.printf(" %d   ║", listOfCells.get(i).getLevel().getHeight());
                    }
                }
            }
        }
        System.out.printf("  6");
        System.out.printf(                  "\n              █═════╩═════╩═════╩═════╩═════╩═════╩═════█" +
                "\n                 0     1     2     3     4     5     6" +
                "\n                                 SOUTH                   \n");
    }

}
