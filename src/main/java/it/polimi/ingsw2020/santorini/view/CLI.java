package it.polimi.ingsw2020.santorini.view;

import it.polimi.ingsw2020.santorini.model.*;
import it.polimi.ingsw2020.santorini.network.client.Client;
import it.polimi.ingsw2020.santorini.network.client.ServerAdapter;
import it.polimi.ingsw2020.santorini.network.client.ViewAdapter;
import it.polimi.ingsw2020.santorini.utils.*;
import it.polimi.ingsw2020.santorini.utils.messages.actions.*;
import it.polimi.ingsw2020.santorini.utils.messages.errors.*;
import it.polimi.ingsw2020.santorini.utils.messages.godsParam.*;
import it.polimi.ingsw2020.santorini.utils.messages.matchMessage.*;

import java.io.IOException;
import java.text.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.InputMismatchException;
import java.util.Scanner;

@SuppressWarnings("deprecation")

public class CLI implements ViewInterface{

    private Client client;
    private Scanner scannerIn;

    public CLI(){
        this.scannerIn = new Scanner(System.in);
    }

    /**
     * method in which it's asked to the client to insert server's IP, and after that the username, birth date and type of match(number of players)
     * metodo in cui si chiede l'iP del server, dopodichè di fanno inserire username, data di nascita e tipo di partita (numero di giocatori nella partita)
     */
    @Override
    public void displaySetupWindow() {
        client = new Client();
        client.setView(this);
        String ip;
        boolean wrong;
        do{
            try {
                System.out.printf("Inserisci l'indirizzo IP del server: ");
                ip = scannerIn.nextLine();
                client.setNetworkHandler(new ServerAdapter(client, ip));
                client.setViewHandler(new ViewAdapter(client));
                wrong = false;
            } catch (IOException e) {
                System.out.println("Il server inserito non esiste, riprovare");
                wrong = true;
            }
        } while(wrong);

        client.getNetworkHandler().start();
        client.getViewHandler().start();

        do{
            try{
                System.out.printf("Inserisci il tuo username: ");
                client.setUsername(scannerIn.nextLine());
                wrong = false;
            } catch(InputMismatchException e){
                wrong = true;
            }
            if(wrong) System.out.println("errore nell'username, reinserire");
        }while(wrong);

        System.out.printf("Inserisci la tua data di nascita (dd/mm/yyyy): ");
        String date = scannerIn.nextLine();
        DateFormat parser = new SimpleDateFormat("dd/MM/yyyy");
        client.setBirthDate(new Date(1900, 0, 1));
        try {
            client.setBirthDate(parser.parse(date));
        } catch (ParseException ignored) {}

        do{
            try{
                System.out.printf("Inserisci il numero di giocatori della partita (2 o 3): ");
                client.setSelectedMatch(scannerIn.nextInt());
                scannerIn.nextLine();
                wrong = client.getSelectedMatch() != 2 && client.getSelectedMatch() != 3;
            }catch (InputMismatchException e){
                wrong = true;
            }
            if(wrong) System.out.println("Inserire 2 o 3!");
        }while(wrong);

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
        System.out.println("Partita creata!\n");
        System.out.println("L'ordine voluto dagli dei è questo: ");
        ArrayList<Player> listOfPlayers = matchSetupMessage.getPlayers();
        for(Player player : listOfPlayers) System.out.println(player.toString() + Color.RESET);
        System.out.println("\n\nE' ora di scegliere la posizione dei builder! inizierà il primo giocatore a scegliere!");
        System.out.println("Abbiamo ordinato in base all'età, i più giovani avranno un piccolo vantaggio!");
        for(Player p : listOfPlayers) System.out.println(p.getNickname());
        System.out.println("Attendi le direttive degli dei");

        // bisogna creare un messaggio che dica che i client siano correttamente entrati nella partita
        // il server manderà uno alla volta i messaggi di scelta delle posizioni dei builder ai client nell'ordine prestabilito (verranno inviati a tutti i componenti della partita)
        // il payload del messaggio inviato dal server conterrà il giocatore che deve scegliere
        // verrà invocato il display choices per le scelte e ci sarà un if fondamentale:
        // se il nome del giocatore corrisponde a quello nel payload, inizierà la procedura di scelta
        // altrimenti comparirà "nome nel payload sta scegliendo"

        Message message = new Message(client.getUsername());
        message.buildSynchronizationMessage(SecondHeaderType.BEGIN_MATCH);
        client.getNetworkHandler().send(message);
    }

    /**
     * the method aks to the current player to choose where he wants to place the builders in the board. This method will be called only
     * in the setup phase
     * metodo addetto alla selezione dei builder secondo l'ordine definito dal controller
     * @param matchStateMessage contains all the references to the current player, the match and the board
     */
    @Override
    public void displaySelectionBuilderWindow(MatchStateMessage matchStateMessage) {
        String currentPlayer = matchStateMessage.getCurrentPlayer().getNickname();
        if(client.getUsername().equals(currentPlayer)) {
            int[] builderM, builderF;
            builderM = new int[2];
            builderF = new int[2];
            System.out.printf("\n%s, tocca a te! Dovrai inserire le coordinate di due celle per posizionare i tuoi costruttori!\n", currentPlayer);
            showBoard(matchStateMessage.getCells());
            System.out.printf("iniziamo con la costruttrice\n");
            boolean wrong;

            do{
                wrong = true;
                try{
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
                    wrong = false;
                }catch (InputMismatchException ignored){
                    scannerIn.nextLine();
                }
                if(wrong) System.out.println("oh-oh, devi inserire dei numeri che rappresentino coordinate libere!");
            }while(wrong);

            do{
                wrong = false;
                try{
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
                    wrong = false;
                }catch (InputMismatchException e){
                    wrong = true;
                    scannerIn.nextLine();
                }
                if(wrong) System.out.println("oh-oh, devi inserire dei numeri che rappresentino coordinate libere!");
            }while(wrong);

            Message message = new Message(client.getUsername());
            message.buildSelectedBuilderPosMessage(new SelectedBuilderPositionMessage(client.getUsername(), builderF, builderM));
            System.out.println("In attesa che gli dei controllino le tue scelte...");
            client.getNetworkHandler().send(message);
        } else {
            System.out.printf("Ok, %s sta scegliendo la posizione dei suoi builder! Attendi...", currentPlayer);
        }
    }

    /**
     * the method will be called only if a player will insert wrong parameters in displaySelectionBuilderWindow, and will ask to the player
     * to insert them again.
     * @param message contains the username of the player that has made the mistake and also a boolean that indicates which builder has the
     * wrong coordinates
     */
    @Override
    public void displayNewSelectionBuilderWindow(IllegalPositionMessage message){
        int[] builderM = null;
        int[] builderF = null;
        boolean wrong;
        if(message.isBuilderFToChange()){
            builderF = new int[2];
            do {
                try {
                    System.out.printf("la tua costruttrice è in una posizione illegale\n");
                    do {
                        System.out.printf("Inserisci la riga, deve essere compresa tra 1 e 5 e libera, come puoi vedere dalla board: ");
                        builderF[0] = scannerIn.nextInt();
                        scannerIn.nextLine();
                    } while (builderF[0] < 1 || builderF[0] > 5);
                    do {
                        System.out.printf("Inserisci la colonna, deve essere compresa tra 1 e 5 e libera, come puoi vedere dalla board: ");
                        builderF[1] = scannerIn.nextInt();
                        scannerIn.nextLine();
                    } while (builderF[1] < 1 || builderF[1] > 5);
                    wrong = false;
                } catch (InputMismatchException e) {
                    wrong = true;
                    scannerIn.nextLine();
                }
                if(wrong) System.out.println("errore, è necessario reinserire!");
            }while(wrong);
        }
        if(message.isBuilderMToChange()) {
            builderM = new int[2];
            do{
                try{
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
                    wrong = false;
                }catch (InputMismatchException e){
                    wrong = true;
                    scannerIn.nextLine();
                }
                if(wrong) System.out.println("errore, è necessario reinserire!");
            }while(wrong);
        }

        Message newPos = new Message(client.getUsername());
        newPos.buildSelectedBuilderPosMessage(new SelectedBuilderPositionMessage(client.getUsername(), builderF, builderM));
        System.out.println("In attesa che gli dei controllino le tue scelte...");
        client.getNetworkHandler().send(newPos);
    }

    /**
     * method that update the board every time that the model is modified, it does that by calling other methods
     * metodo che aggiorna la board ogni volta che viene fatta una mossa (modificato il model)
     * parametro un messaggio con scritte le informazioni sulla board.
     */
    @Override
    public void updateMatch(UpdateMessage updateMessage) {
        switch(updateMessage.getPhase()){
            case START_TURN:
                System.out.println("DISPLAY START TURN");
                displayStartTurn(updateMessage);
                break;
            case STANDBY_PHASE_1:
                System.out.println("DISPLAY SP1, POTERE ATTIVATO");
                displaySP(updateMessage, PhaseType.STANDBY_PHASE_1);
                break;
            case MOVE_PHASE:
                System.out.println("DISPLAY MOVE");
                displayMoveUpdate(updateMessage);
                break;
            case STANDBY_PHASE_2:
                System.out.println("DISPLAY SP2, POTERE ATTIVATO");
                displaySP(updateMessage, PhaseType.STANDBY_PHASE_2);
                break;
            case BUILD_PHASE:
                System.out.println("DISPLAY BUILD");
                displayBuildUpdate(updateMessage);
                break;
            case STANDBY_PHASE_3:
                System.out.println("DISPLAY SP3, POTERE ATTIVATO");
                displaySP(updateMessage, PhaseType.STANDBY_PHASE_3);
                break;
            case END_TURN:
                System.out.println("DISPLAY END TURN");
                displayEndTurn(updateMessage);
                break;
            default:
                break;
        }
    }

    /**
     * method that shows board, builders, the textual interface and the first player to play
     * far visualizzare la board con le pedine e tutta l'interfaccia testuale e il primo giocatore che gioca
     */
    @Override
    public void displayStartTurn(UpdateMessage message) {
        showBoard(message.getBoard());
        if(client.getUsername().equals(message.getCurrentPlayer().getNickname())) {
            System.out.println(message.getCurrentPlayer().getNickname() + " tocca a te!");
            Message nextPhase = new Message(client.getUsername());
            nextPhase.buildNextPhaseMessage();
            client.getNetworkHandler().send(nextPhase);
        } else {
            System.out.println("Ora è il turno di " + message.getCurrentPlayer().getNickname());
        }
    }

    /**
     * the method asks to the player if he wants to activate his god's power
     * @param question is a message that contains the name of the player (that will receive the question) and the name of the god
     */
    @Override
    public void displayWouldActivate(MatchStateMessage question) {
        // richiediamo se il giocatore vuole attivare il potere divino
        if (client.getUsername().equals(question.getCurrentPlayer().getNickname())) {
            System.out.println("Vuoi richiedere l'intervento di " + question.getCurrentPlayer().getDivinePower().getName() + "? Y-N");
            Message message = new Message(client.getUsername());
            boolean wrong;
            do{
                wrong = false;
                String answer = scannerIn.nextLine();
                answer = answer.toUpperCase();
                if (answer.equals("Y"))
                    message.buildActivateGodMessage(new ActivateGodMessage(true));
                else if (answer.equals("N"))
                    message.buildActivateGodMessage(new ActivateGodMessage(false));
                else
                    wrong = true;
            } while(wrong);
            client.getNetworkHandler().send(message);
        }
    }

    /**
     * the method calls a method linked to a specific God that will ask to the player to insert parameters needed to use the god
     * @param message is a message that contains the name of the player (that will receive the question) and the name of the god
     */
    @Override
    public void displayParametersSelection(MatchStateMessage message) {
        if(message.getCurrentPlayer().getNickname().equals(client.getUsername())) {
            Message selectedParam = new Message(client.getUsername());
            String god = message.getCurrentPlayer().getDivinePower().getName();
            System.out.println(god + " è qui ad aiutarti!");
            switch (god) {
                case "Apollo":
                    selectedParam.buildApolloParamMessage(displayApolloParamSel(message));
                    break;
                case "Ares":
                    selectedParam.buildAresParamMessage(displayAresParamSel(message));
                    break;
                case "Artemis":
                    selectedParam.buildArtemisParamMessage(displayArtemisParamSel(message));
                    break;
                case "Atlas":
                    selectedParam.buildAtlasParamMessage(displayAtlasParamSel(message));
                    break;
                case "Demeter":
                    selectedParam.buildDemeterParamMessage(displayDemeterParamSel(message));
                    break;
                case "Hestia":
                    selectedParam.buildHestiaParamMessage(displayHestiaParamSel(message));
                    break;
                case "Minotaur":
                    selectedParam.buildMinotaurParamMessage(displayMinotaurParamSel(message));
                    break;
                case "Poseidon":
                    selectedParam.buildPoseidonParamMessage(displayPoseidonParamSel(message));
                    break;
                case "Prometheus":
                    selectedParam.buildPrometheusParamMessage(displayPrometheusParamSel(message));
                    break;
                default:
                    break;
            }
            client.getNetworkHandler().send(selectedParam);
        }
    }

    /**
     * the method prints to the players when and which god helped a player
     * @param updateMessage contains the name of the god used and the user name of the player that used it
     * @param phase is the phase in which the god helped the player
     */
    @Override
    public void displaySP(UpdateMessage updateMessage, PhaseType phase) {
        System.out.printf(updateMessage.getCurrentPlayer().getDivinePower().getName());
        if(updateMessage.getCurrentPlayer().getNickname().equals(client.getUsername())) {
            System.out.println(" ha accettato la tua richiesta di aiuto");
            Message nextPhase = new Message(client.getUsername());
            nextPhase.buildNextPhaseMessage();
            client.getNetworkHandler().send(nextPhase);
        }
        else
            System.out.println(" ha aiutato " + updateMessage.getCurrentPlayer().getNickname());
        showBoard(updateMessage.getBoard());
    }

    /**
     * the method asks to the current player which one of his builder he wants to move and build with.
     * To help the player in his choice the method also shows the board in its current state and it allows the player to select a builder only
     * if he or she can move
     * @param message contains the username of the player and
     */
    @Override
    public void displayChooseBuilder(MatchStateMessage message) {
        if(message.getCurrentPlayer().getNickname().equals(client.getUsername())) {
            showBoard(message.getCells());
            System.out.println("Quale builder vuoi muovere? Il maschio o la femmina?");
            Message chosenBuilder = new Message(client.getUsername());
            Builder builder = null;
            String choice = null;
            boolean wrong;
            message.getCurrentPlayer().getBuilderF().setBoard(new Board(message.getBoard()));
            message.getCurrentPlayer().getBuilderF().setPlayer(message.getCurrentPlayer());
            message.getCurrentPlayer().getBuilderM().setBoard(new Board(message.getBoard()));
            message.getCurrentPlayer().getBuilderM().setPlayer(message.getCurrentPlayer());
            do {
                if(message.getCurrentPlayer().getBuilderM().canMove())
                    System.out.println("Premi il tasto M per selezionare il maschio");
                if(message.getCurrentPlayer().getBuilderF().canMove())
                    System.out.println("Premi il tasto F per selezionare la femmina");
                try{
                    choice = scannerIn.nextLine();
                    choice = choice.toUpperCase();
                    if (choice.equals("M")) {
                        builder = message.getCurrentPlayer().getBuilderM();
                        builder.setBoard(new Board(message.getBoard()));
                        builder.setPlayer(message.getCurrentPlayer());
                        chosenBuilder.buildSelectedBuilderMessage(new SelectedBuilderMessage('M'));
                        if(builder.canMove()) wrong = false;
                        else wrong = true;
                    }
                    else if (choice.equals("F")){
                        builder = message.getCurrentPlayer().getBuilderF();
                        builder.setBoard(new Board(message.getBoard()));
                        builder.setPlayer(message.getCurrentPlayer());
                        chosenBuilder.buildSelectedBuilderMessage(new SelectedBuilderMessage('F'));
                        if(builder.canMove()) wrong = false;
                        else wrong = true;
                    }
                    else
                        wrong = true;
                } catch(InputMismatchException e){
                    wrong = true;
                    scannerIn.nextLine();
                }
            } while (wrong);
            client.getNetworkHandler().send(chosenBuilder);
        }
    }

    /**
     * the method asks to the player in which direction he wants to move the builder. The method shows a little matrix to represents the
     * allowed direction of movements. If the player inserts a wrong direction, he will be asked again to insert the direction
     * metodo che mostra all'utente le possibili mosse che il builder selezionato può fare
     */
    @Override
    public void displayPossibleMoves(AskMoveSelectionMessage message) {
        int[][] possibleMoves = message.getPossibleMoves();
        Direction direction = null;
        boolean wrong;
        int buttonPressed;
        showPossibleMatrix(possibleMoves, 'm');
        do {
            try {
                scannerIn.reset();
                System.out.println("Inserire la direzione scelta per lo spostamento");
                if (possibleMoves[0][0] != 0) System.out.println("Premi 1 per muovere con il builder a NORD-OVEST");
                if (possibleMoves[0][1] != 0) System.out.println("Premi 2 per muovere con il builder a NORD");
                if (possibleMoves[0][2] != 0) System.out.println("Premi 3 per muovere con il builder a NORD-EST");
                if (possibleMoves[1][0] != 0) System.out.println("Premi 4 per muovere con il builder a OVEST");
                if (possibleMoves[1][2] != 0) System.out.println("Premi 5 per muovere con il builder a EST");
                if (possibleMoves[2][0] != 0) System.out.println("Premi 6 per muovere con il builder a SUD-OVEST");
                if (possibleMoves[2][1] != 0) System.out.println("Premi 7 per muovere con il builder a SUD");
                if (possibleMoves[2][2] != 0) System.out.println("Premi 8 per muovere con il builder a SUD-EST");

                buttonPressed = scannerIn.nextInt();
                scannerIn.nextLine();

                if (buttonPressed == 1 && possibleMoves[0][0] != 0) {
                    direction = Direction.NORTH_WEST;
                    wrong = false;
                } else if (buttonPressed == 2 && possibleMoves[0][1] != 0) {
                    direction = Direction.NORTH;
                    wrong = false;
                } else if (buttonPressed == 3 && possibleMoves[0][2] != 0) {
                    direction = Direction.NORTH_EAST;
                    wrong = false;
                } else if (buttonPressed == 4 && possibleMoves[1][0] != 0) {
                    direction = Direction.WEST;
                    wrong = false;
                } else if (buttonPressed == 5 && possibleMoves[1][2] != 0) {
                    direction = Direction.EAST;
                    wrong = false;
                } else if (buttonPressed == 6 && possibleMoves[2][0] != 0) {
                    direction = Direction.SOUTH_WEST;
                    wrong = false;
                } else if (buttonPressed == 7 && possibleMoves[2][1] != 0) {
                    direction = Direction.SOUTH;
                    wrong = false;
                } else if (buttonPressed == 8 && possibleMoves[2][2] != 0) {
                    direction = Direction.SOUTH_EAST;
                    wrong = false;
                }else wrong = true;
            }catch(InputMismatchException e){
                scannerIn.nextLine();
                wrong = true;
            }
            if(wrong) System.out.println("Direzione non valida! Inserire una direzione valida");
        }while(wrong);

        Message moveSelection = new Message(client.getUsername());
        moveSelection.buildSelectedMoveMessage(new SelectedMoveMessage(direction));
        client.getNetworkHandler().send(moveSelection);
        // display delle possible moves per restringere il campo d'azione -> vedi canMove
        // conversione della direzione
        // creazione del messaggio di selezione
        // invio del messaggio al server
    }

    /**
     * the method shows to the current player the board after the movement.
     * @param updateMessage contains the username of current player and also a reference to the board(used to show the board to the player).
     */
    @Override
    public void displayMoveUpdate(UpdateMessage updateMessage) {
        // si dice cosa è successo, e si mostra la board. oppure si mostra solo la board
        showBoard(updateMessage.getBoard());
        if(updateMessage.getCurrentPlayer().getNickname().equals(client.getUsername())) {
            System.out.println("richiedo prossima fase: build");
            Message nextPhase = new Message(client.getUsername());
            nextPhase.buildNextPhaseMessage();
            client.getNetworkHandler().send(nextPhase);
        }
    }

    /**
     * the method asks to the player in which direction he wants to build. The method shows a little matrix to represents the
     * allowed direction to construct near his playing builder. If the player inserts a wrong direction, he will be asked again to insert the direction
     * metodo che mostra all'utente le possibili costruzioni che il builder mosso può fare
     */
    @Override
    public void displayPossibleBuildings(AskBuildSelectionMessage message) {
        int[][] possibleBuildings = message.getPossibleBuildings();
        Direction direction = null;
        boolean wrong;
        int buttonPressed;
        showPossibleMatrix(possibleBuildings, 'b');
        do {
            try {
                System.out.println("Inserire la direzione scelta per la costruzione");
                if (possibleBuildings[0][0] >= 0 && possibleBuildings[0][0] < 4)
                    System.out.println("Premi 1 per costruire con il builder a NORD-OVEST");
                if (possibleBuildings[0][1] >= 0 && possibleBuildings[0][1] < 4)
                    System.out.println("Premi 2 per costruire con il builder a NORD");
                if (possibleBuildings[0][2] >= 0 && possibleBuildings[0][2] < 4)
                    System.out.println("Premi 3 per costruire con il builder a NORD-EST");
                if (possibleBuildings[1][0] >= 0 && possibleBuildings[1][0] < 4)
                    System.out.println("Premi 4 per costruire con il builder a OVEST");
                if (possibleBuildings[1][2] >= 0 && possibleBuildings[1][2] < 4)
                    System.out.println("Premi 5 per costruire con il builder a EST");
                if (possibleBuildings[2][0] >= 0 && possibleBuildings[2][0] < 4)
                    System.out.println("Premi 6 per costruire con il builder a SUD-OVEST");
                if (possibleBuildings[2][1] >= 0 && possibleBuildings[2][1] < 4)
                    System.out.println("Premi 7 per costruire con il builder a SUD");
                if (possibleBuildings[2][2] >= 0 && possibleBuildings[2][2] < 4)
                    System.out.println("Premi 8 per costruire con il builder a SUD-EST");

                buttonPressed = scannerIn.nextInt();
                scannerIn.nextLine();

                if (buttonPressed == 1 && possibleBuildings[0][0] >= 0 && possibleBuildings[0][0] < 4) {
                    direction = Direction.NORTH_WEST;
                    wrong = false;
                } else if (buttonPressed == 2 && possibleBuildings[0][1] >= 0 && possibleBuildings[0][1] < 4) {
                    direction = Direction.NORTH;
                    wrong = false;
                } else if (buttonPressed == 3 && possibleBuildings[0][2] >= 0 && possibleBuildings[0][2] < 4) {
                    direction = Direction.NORTH_EAST;
                    wrong = false;
                } else if (buttonPressed == 4 && possibleBuildings[1][0] >= 0 && possibleBuildings[1][0] < 4) {
                    direction = Direction.WEST;
                    wrong = false;
                } else if (buttonPressed == 5 && possibleBuildings[1][2] >= 0 && possibleBuildings[1][2] < 4) {
                    direction = Direction.EAST;
                    wrong = false;
                } else if (buttonPressed == 6 && possibleBuildings[2][0] >= 0 && possibleBuildings[2][0] < 4) {
                    direction = Direction.SOUTH_WEST;
                    wrong = false;
                } else if (buttonPressed == 7 && possibleBuildings[2][1] >= 0 && possibleBuildings[2][1] < 4) {
                    direction = Direction.SOUTH;
                    wrong = false;
                } else if (buttonPressed == 8 && possibleBuildings[2][2] >= 0 && possibleBuildings[2][2] < 4) {
                    direction = Direction.SOUTH_EAST;
                    wrong = false;
                } else wrong = true;
            } catch(InputMismatchException e){
                scannerIn.nextLine();
                wrong = true;
            }
            if(wrong) System.out.println("Direzione non valida! Inserire una direzione valida");
        }while(wrong);
        Message buildSelection = new Message(client.getUsername());
        buildSelection.buildSelectedBuildingMessage(new SelectedBuildingMessage(direction));
        client.getNetworkHandler().send(buildSelection);
        // display delle possible buildings per restringere il campo d'azione -> vedi canBuild
        // conversione della direzione
        // creazione del messaggio di selezione
        // invio del messaggio al server
    }

    /**
     * the method shows the board updated after the build phase
     * @param updateMessage is a message that contains the reference to the board, which is used to print the board itself
     */
    @Override
    public void displayBuildUpdate(UpdateMessage updateMessage) {
        // si dice cosa è successo, e si mostra la board. oppure si mostra solo la board
        showBoard(updateMessage.getBoard());
        if(updateMessage.getCurrentPlayer().getNickname().equals(client.getUsername())) {
            System.out.println("richiedo prossima fase: end");
            Message nextPhase = new Message(client.getUsername());
            nextPhase.buildNextPhaseMessage();
            client.getNetworkHandler().send(nextPhase);
        }
    }

    /**
     *the method prints a message to the players showing how the board has been modified during the turn and communicates the end of the turn
     * of the current player
     * @param updateMessage contains the the user name of current player and the reference to the board
     */
    @Override
    public void displayEndTurn(UpdateMessage updateMessage) {
        showBoard(updateMessage.getBoard());
        System.out.println("Il turno di " + updateMessage.getCurrentPlayer().getNickname() + " è terminato!");
        Message nextPhase = new Message(client.getUsername());
        nextPhase.buildNextPhaseMessage();
        client.getNetworkHandler().send(nextPhase);
    }

    /**
     * method that shows winner. It then close the match or if the players wants to begin a new match
     * metodo che mostra vincitori e vinti. conclude la partita con epic sax guy
     * @param winner
     */
    @Override
    public void displayEndMatch(String winner) {
        System.out.println("AND THE WINNER IS... " + winner);
        boolean wrong;
        Message message = new Message(client.getUsername());
        do{
            wrong = false;
            System.out.println("Vuoi fare una nuova partita? Rispondi Y-N: ");
            String answer = scannerIn.nextLine();
            answer = answer.toUpperCase();
            if (answer.equals("Y")) {
                do{
                    try{
                        System.out.printf("Inserisci il numero di giocatori della partita (2 o 3): ");
                        client.setSelectedMatch(scannerIn.nextInt());
                        scannerIn.nextLine();
                        wrong = client.getSelectedMatch() != 2 && client.getSelectedMatch() != 3;
                    }catch (InputMismatchException e){
                        wrong = true;
                    }
                    if(wrong) System.out.println("Inserire 2 o 3!");
                }while(wrong);
                message.buildNewMatchMessage(new NewMatchMessage(true, client.getSelectedMatch(), client.getBirthDate()));
            } else if (answer.equals("N")) {
                System.out.println("Grazie per aver giocato con noi, a presto!");
                message.buildNewMatchMessage(new NewMatchMessage(false, 0, null));
                System.exit(0);
            }
            else
                wrong = true;
        } while(wrong);
        client.getNetworkHandler().send(message);
    }

    /**
     * method that shows possible errors occurred
     * metodo che mostra all'utente possibili errori che sono capitati
     */
    @Override
    public void displayErrorMessage(String error) {
        System.out.println(error);
    }

    /**
     * the method prints the scheme of the board, whit numbers inside the cells to indicate the height of the buildings, nothing if there isn't
     * any builder, the coloured symbol of the gender to indicate the gender of the builder and to whom it belongs. The triangle and the wave
     * represent the coast.
     * @param listOfCells is an array list of cells containing all the information of the board
     */
    public void showBoard(ArrayList<Cell> listOfCells){
        String coast = Color.OCEAN_BLUE+"\u25DE\u25DC"+Color.MOUNTAIN_BROWN +"\u25B2 ";
        //wave: \u25DE\u25DC
        //mountain: \u25B2
        //configurazione funzionante: "\u25DE\u25DC\u25B2 "
        System.out.println("\n\nBoard:\n");
        System.out.printf(                  "                                 NORTH                \n" +
                "                 0     1     2     3     4     5     6\n" +
                "              "+Color.CORNER_WHITE +"█"+Color.BORDER_YELLOW+"═════╦═════╦═════╦═════╦═════╦═════╦═════"+Color.CORNER_WHITE +"█");
        int j = 0;
        for(int i = 0; i < listOfCells.size(); ++i){
            if(i % 7 == 0){
                if(i == 0)  System.out.printf(Color.RESET+"\n           %d  "+Color.BORDER_YELLOW+"║%s"+Color.BORDER_YELLOW+"║", i%7, coast);
                else {
                    if(j == 2) {
                        System.out.printf(                                                            Color.RESET+"  %d", j);
                        System.out.printf(  "\n              "+Color.BORDER_YELLOW+"╠═════╬═════╬═════╬═════╬═════╬═════╬═════╣\n ");
                        System.out.printf(  Color.RESET+"    WEST  %d  "+Color.BORDER_YELLOW+"║%s"+Color.BORDER_YELLOW+"║", ++j, coast);
                    }
                    else if(j == 3){
                        System.out.printf(                                                           Color.RESET + "  %d  EAST", j);
                        System.out.printf(  "\n              "+Color.BORDER_YELLOW+"╠═════╬═════╬═════╬═════╬═════╬═════╬═════╣\n ");
                        System.out.printf(  Color.RESET+"          %d  "+Color.BORDER_YELLOW+"║%s"+Color.BORDER_YELLOW+"║", ++j, coast);
                    }
                    else {
                        System.out.printf(                                                            Color.RESET+"  %d", j);
                        System.out.printf(  "\n              "+Color.BORDER_YELLOW+"╠═════╬═════╬═════╬═════╬═════╬═════╬═════╣\n ");
                        System.out.printf(  Color.RESET+"          %d  "+Color.BORDER_YELLOW+"║%s"+Color.BORDER_YELLOW+"║", ++j, coast);
                    }
                }
            } else {
                if(listOfCells.get(i).getLevel() == LevelType.COAST) System.out.printf("%s"+Color.BORDER_YELLOW+"║", coast);
                else{
                    if(listOfCells.get(i).getStatus() == AccessType.OCCUPIED){
                        System.out.printf(Color.RESET+" %d"+listOfCells.get(i).getBuilder().getColor()+"%2c"+Color.BORDER_YELLOW+" ║", listOfCells.get(i).getLevel().getHeight(), listOfCells.get(i).getBuilder().getGender());
                    }
                    else {
                        System.out.printf(Color.RESET+" %d   "+Color.BORDER_YELLOW+"║", listOfCells.get(i).getLevel().getHeight());
                    }
                }
            }
        }
        System.out.printf(Color.RESET+"  6");
        System.out.printf(                  "\n              "+Color.CORNER_WHITE +"█"+Color.BORDER_YELLOW+"═════╩═════╩═════╩═════╩═════╩═════╩═════"+Color.CORNER_WHITE +"█" + Color.RESET+
                "\n                 0     1     2     3     4     5     6" +
                "\n                                 SOUTH                   \n");
    }

    /**
     * the method prints a 3*3 matrix that represents the possible choices both for building and moving the builders. The symbol 'X' is used
     * to represent a cell in which a builder cannot build or move into.
     * @param matrixToShow is the reference to a matrix 3*3 such as possible moves or possible buildings
     * @param type is a char that is used for understand if matrixToShow is used for representing possible moves or buildings
     */
    public void showPossibleMatrix(int[][] matrixToShow, char type){
        System.out.println("\n\nPossibilità:\n");
        System.out.printf(                  "                     NORTH                \n" +
                "                 0     1     2 \n" +
                "              "+Color.CORNER_WHITE +"█"+Color.BORDER_YELLOW+"═════╦═════╦═════"+Color.CORNER_WHITE +"█");
        char[] cell = new char[3];
        if(type == 'b') {
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    if (matrixToShow[i][j] < 0) {
                        cell[j] = 'X';
                    } else if (matrixToShow[i][j] == 0) {
                        cell[j] = '0';
                    } else if (matrixToShow[i][j] == 1) {
                        cell[j] = '1';
                    } else if (matrixToShow[i][j] == 2) {
                        cell[j] = '2';
                    } else if (matrixToShow[i][j] == 3) {
                        cell[j] = '3';
                    } else cell[j] = 'X';
                }
                if (i == 0 || i == 2) {
                    System.out.printf(Color.RESET + "\n              " + Color.BORDER_YELLOW + "║  %c  " + Color.BORDER_YELLOW + "║" + Color.BORDER_YELLOW + "  %c  " + Color.BORDER_YELLOW + "║" + Color.BORDER_YELLOW + "  %c  " + Color.BORDER_YELLOW + "║", cell[0], cell[1], cell[2]);
                    if (i == 0) {
                        System.out.printf("\n              " + Color.BORDER_YELLOW + "╠═════╬═════╬═════╬ ");
                    }
                } else {
                    System.out.printf(Color.RESET + "\n       OVEST  " + Color.BORDER_YELLOW + "║  %c  " + Color.BORDER_YELLOW + "║" + Color.BORDER_YELLOW + "  %c  " + Color.BORDER_YELLOW + "║" + Color.BORDER_YELLOW + "  %c  " + Color.BORDER_YELLOW + "║" + Color.RESET + " EAST\n", cell[0], cell[1], cell[2]);
                    System.out.printf("              " + Color.BORDER_YELLOW + "╠═════╬═════╬═════╬ ");
                }
            }
        } else{
            int k = 1;
            for(int i = 0; i < 3; ++i) {
                for (int j = 0; j < 3; ++j) {
                    if (matrixToShow[i][j] > 0 && matrixToShow[i][j] < 4) cell[j] = Character.forDigit(k, 10);
                    else cell[j] = ' ';
                    if(i != 1 || j != 1) ++k;
                }
                if (i == 0 || i == 2) {
                    System.out.printf(Color.RESET + "\n              " + Color.BORDER_YELLOW + "║  %c  " + Color.BORDER_YELLOW + "║" + Color.BORDER_YELLOW + "  %c  " + Color.BORDER_YELLOW + "║" + Color.BORDER_YELLOW + "  %c  " + Color.BORDER_YELLOW + "║", cell[0], cell[1], cell[2]);
                    if (i == 0) {
                        System.out.printf("\n              " + Color.BORDER_YELLOW + "╠═════╬═════╬═════╬ ");
                    }
                } else {
                    System.out.printf(Color.RESET + "\n       OVEST  " + Color.BORDER_YELLOW + "║  %c  " + Color.BORDER_YELLOW + "║" + Color.BORDER_YELLOW + "  %c  " + Color.BORDER_YELLOW + "║" + Color.BORDER_YELLOW + "  %c  " + Color.BORDER_YELLOW + "║" + Color.RESET + " EAST\n", cell[0], cell[1], cell[2]);
                    System.out.printf("              " + Color.BORDER_YELLOW + "╠═════╬═════╬═════╬ ");
                }
            }

        }
        System.out.printf("\n              "+Color.CORNER_WHITE +"█"+Color.BORDER_YELLOW+"═════╩═════╩═════"+Color.CORNER_WHITE +"█" + Color.RESET+
                "\n                 0     1     2     " +
                "\n                     SOUTH                   \n\n");
    }

    /**
     * the method asks to the current player to insert parameters need to use Apollo's power. These parameters are the choice of which builder
     * the player want to move (and swap with opponent's builder) and in which direction. If the builder selected cannot be moved, the method will choose for the player the other
     * builder. If the direction insert is not allowed the method wil ask to the player to insert it again. The method will also built the
     * message containing the parameters gathered
     * @param message contains references about the board and the currents player(About the match ).
     * @return the message containing the parameters gathered.
     */
    private ApolloParamMessage displayApolloParamSel(MatchStateMessage message){
        ApolloParamMessage apolloParamMessage = new ApolloParamMessage();
        //Inserire la sampa per la scelta del builder
        char yourBuilderGender = 'O';
        Builder chosen = null;
        Direction direction = null;
        System.out.println("Seleziona il builder più adatto a servire Apollo.Premi M o F. Ricorda che deve essere vicino ad un builder avversario affinché sia degno!");
        String choice = null;
        boolean wrong;
        do {
            try{choice = scannerIn.nextLine();
                choice = choice.toUpperCase();
                wrong  = false;
            } catch (InputMismatchException e){wrong = true;}

            if (choice.equals("M")) {
                chosen = message.getCurrentPlayer().getBuilderM();
                yourBuilderGender = 'M';
            } else if (choice.equals("F")){
                chosen = message.getCurrentPlayer().getBuilderF();
                yourBuilderGender = 'F';
            } else wrong = true;
            if(wrong){
                System.out.println("Lettera Sbagliata, reinserisci");
            }
        } while (wrong);

        chosen.setBoard(new Board(message.getBoard()));
        chosen.setPlayer(message.getCurrentPlayer());
        int[][] neighboringSwappingCell = Board.neighboringSwappingCell(chosen, AccessType.OCCUPIED);

        boolean allZeros = true;
        for(int i = 0; i < 3 && allZeros; ++i)
            for(int j = 0; j < 3 && allZeros; ++j)
                if(neighboringSwappingCell[i][j] != 0) allZeros = false;

        if(allZeros){
            System.out.println("Il builder che hai selezionato non è adatto a servire Apollo, viene selezionato l'altro builder automaticamente");
            if(yourBuilderGender == 'M'){
                yourBuilderGender = 'F';
                chosen = message.getCurrentPlayer().getBuilderF();
            } else {
                yourBuilderGender = 'M';
                chosen = message.getCurrentPlayer().getBuilderM();
            }
        }
        //Ricalcola neighboringSwappingCell e la display

        chosen.setBoard(new Board(message.getBoard()));
        chosen.setPlayer(message.getCurrentPlayer());
        neighboringSwappingCell = Board.neighboringSwappingCell(chosen, AccessType.OCCUPIED);
        System.out.println("Ora è il momento di scegliere il costruttore avversario, premi il numero indicato per scegliere la direzione che preferisci");
        showPossibleMatrix(neighboringSwappingCell, 'm');
        int pressedButton;
        do {
            //System.out.println("Ora è il momento di scegliere il costruttore avversario, premi il numero indicato per scegliere la direzione che preferisci");
            if (neighboringSwappingCell[0][0] != 0) System.out.println("Premi 1 per andare a NORD-OVEST");
            if (neighboringSwappingCell[0][1] != 0) System.out.println("Premi 2 per andare a NORD");
            if (neighboringSwappingCell[0][2] != 0) System.out.println("Premi 3 per andare a NORD-EST");
            if (neighboringSwappingCell[1][0] != 0) System.out.println("Premi 4 per andare a OVEST");
            if (neighboringSwappingCell[1][2] != 0) System.out.println("Premi 5 per andare a EST");
            if (neighboringSwappingCell[2][0] != 0) System.out.println("Premi 6 per andare a SUD-OVEST");
            if (neighboringSwappingCell[2][1] != 0) System.out.println("Premi 7 per andare a SUD");
            if (neighboringSwappingCell[2][2] != 0) System.out.println("Premi 8 per andare a SUD-EST");

            try{pressedButton = scannerIn.nextInt();
                scannerIn.nextLine();

                if (pressedButton == 1 && neighboringSwappingCell[0][0] != 0) {
                    direction = Direction.NORTH_WEST;
                    wrong = false;
                } else if (pressedButton == 2 && neighboringSwappingCell[0][1] != 0) {
                    direction = Direction.NORTH;
                    wrong = false;
                } else if (pressedButton == 3 && neighboringSwappingCell[0][2] != 0) {
                    direction = Direction.NORTH_EAST;
                    wrong = false;
                } else if (pressedButton == 4 && neighboringSwappingCell[1][0] != 0) {
                    direction = Direction.WEST;
                    wrong = false;
                } else if (pressedButton == 5 && neighboringSwappingCell[1][2] != 0) {
                    direction = Direction.EAST;
                    wrong = false;
                } else if (pressedButton == 6 && neighboringSwappingCell[2][0] != 0) {
                    direction = Direction.SOUTH_WEST;
                    wrong = false;
                } else if (pressedButton == 7 && neighboringSwappingCell[2][1] != 0) {
                    direction = Direction.SOUTH;
                    wrong = false;
                } else if (pressedButton == 8 && neighboringSwappingCell[2][2] != 0) {
                    direction = Direction.SOUTH_EAST;
                    wrong = false;
                }else wrong = true;
            } catch (InputMismatchException e){
                wrong = true;
                scannerIn.nextLine();
            }
            if(wrong) System.out.println("Hai selezionato un carattere sbagliato: inserisci un numero corretto");
        } while(wrong);

        apolloParamMessage.setYourBuilderGender(yourBuilderGender);
        apolloParamMessage.setOpponentBuilderDirection(direction);
        return apolloParamMessage;
    }

    /**
     * the method asks to the current player to insert parameters need to use  Ares's power. the Parameter asked is direction of the cell
     * where the demolition will occur
     * If the direction insert is not allowed the method wil ask to the player to insert it again. The method will also built the
     * message containing the parameters gathered
     * @param message contains information about the match such as the current player
     * @return the message containing the parameter insert by the player
     */
    private AresParamMessage displayAresParamSel(MatchStateMessage message){
        AresParamMessage aresParamMessage = new AresParamMessage();
        Direction direction = null;
        Builder demolitionBuilder = null;
        char demolitionBuilderSex ='O' ;
        if (message.getCurrentPlayer().getPlayingBuilder().getGender() == '\u2640') {
            demolitionBuilder = message.getCurrentPlayer().getBuilderM();
            demolitionBuilderSex = 'M';
        } else {
            demolitionBuilder = message.getCurrentPlayer().getBuilderF();
            demolitionBuilderSex = 'F';
        }

        demolitionBuilder.setBoard(new Board(message.getBoard()));
        demolitionBuilder.setPlayer(message.getCurrentPlayer());
        int[][] neighboringLevelCell = Board.neighboringLevelCell(demolitionBuilder);

        //INserire display per mostrare le possibili costruzioni

        boolean wrong;
        int pressedButton;
        System.out.println("Ora è il momento di scegliere il blocco da demolire, premi il numero indicato per scegliere la direzione del blocco che preferisci distruggere");
        showPossibleMatrix(neighboringLevelCell, 'b');
        do {
            //System.out.println("Ora è il momento di scegliere il blocco da demolire, premi il numero indicato per scegliere la direzione del blocco che preferisci distruggere");
            if (neighboringLevelCell[0][0] > 0 && neighboringLevelCell[0][0] < 4) System.out.println("Premi 1 per demolire il blocco a NORD-OVEST");
            if (neighboringLevelCell[0][1] > 0 && neighboringLevelCell[0][1] < 4) System.out.println("Premi 2 per demolire il blocco a NORD");
            if (neighboringLevelCell[0][2] > 0 && neighboringLevelCell[0][2] < 4) System.out.println("Premi 3 per demolire il blocco a NORD-EST");
            if (neighboringLevelCell[1][0] > 0 && neighboringLevelCell[1][0] < 4) System.out.println("Premi 4 per demolire il blocco a OVEST");
            if (neighboringLevelCell[1][2] > 0 && neighboringLevelCell[1][2] < 4) System.out.println("Premi 5 per demolire il blocco a EST");
            if (neighboringLevelCell[2][0] > 0 && neighboringLevelCell[2][0] < 4) System.out.println("Premi 6 per demolire il blocco a SUD-OVEST");
            if (neighboringLevelCell[2][1] > 0 && neighboringLevelCell[2][1] < 4) System.out.println("Premi 7 per demolire il blocco a SUD");
            if (neighboringLevelCell[2][2] > 0 && neighboringLevelCell[2][2] < 4) System.out.println("Premi 8 per demolire il blocco a SUD-EST");
            try{
                pressedButton = scannerIn.nextInt();
                scannerIn.nextLine();
                wrong = true;

                if (pressedButton == 1 && neighboringLevelCell[0][0] != 0 && neighboringLevelCell[0][0] != 4) {
                    direction = Direction.NORTH_WEST;
                    wrong = false;
                } else if (pressedButton == 2 && neighboringLevelCell[0][1] != 0 && neighboringLevelCell[0][1] != 4) {
                    direction = Direction.NORTH;
                    wrong = false;
                } else if (pressedButton == 3 && neighboringLevelCell[0][2] != 0 && neighboringLevelCell[0][2] != 4) {
                    direction = Direction.NORTH_EAST;
                    wrong = false;
                } else if (pressedButton == 4 && neighboringLevelCell[1][0] != 0 && neighboringLevelCell[1][0] != 4) {
                    direction = Direction.WEST;
                    wrong = false;
                } else if (pressedButton == 5 && neighboringLevelCell[1][2] != 0 && neighboringLevelCell[1][2] != 4) {
                    direction = Direction.EAST;
                    wrong = false;
                } else if (pressedButton == 6 && neighboringLevelCell[2][0] != 0 && neighboringLevelCell[2][0] != 4) {
                    direction = Direction.SOUTH_WEST;
                    wrong = false;
                } else if (pressedButton == 7 && neighboringLevelCell[2][1] != 0 && neighboringLevelCell[2][1] != 4) {
                    direction = Direction.SOUTH;
                    wrong = false;
                } else if (pressedButton == 8 && neighboringLevelCell[2][2] != 0 && neighboringLevelCell[2][2] != 4) {
                    direction = Direction.SOUTH_EAST;
                    wrong = false;
                }
            }catch (InputMismatchException  e){
                wrong = true;
                scannerIn.nextLine();
            }

            if(wrong) System.out.println("hai selezionato un carattere sbagliato! ricorda non puoi demolire il terreno o una cupola");
        } while(wrong);

        aresParamMessage.setDemolitionBuilderSex(demolitionBuilderSex);
        aresParamMessage.setTargetedBlock(direction);
        return aresParamMessage;
    }

    /**
     * the method asks to the current player to insert parameters need to use Artemis's power. The Parameter asked is direction of the cell
     * where the player wants to moved again the builder in.
     * If the direction insert is not allowed the method wil ask to the player to insert it again. The method will also built the
     * message containing the parameters gathered
     * @param message contains information about the match such as the current player, information used for acquiring which is the playing
     * builder
     * @return the message containing the parameter acquired by the method
     */
    private ArtemisParamMessage displayArtemisParamSel(MatchStateMessage message){
        ArtemisParamMessage artemisParamMessage = new ArtemisParamMessage();
        int[][] possibleMoves = message.getCurrentPlayer().getPlayingBuilder().getPossibleMoves();
        Direction direction = null;
        boolean wrong;
        int pressedButton;
        System.out.println("Ora è il momento di scegliere dove muovere nuovamente il builder, premi il numero indicato per scegliere la direzione del movimento");
        showPossibleMatrix(possibleMoves, 'm');
        do {
            //System.out.println("Ora è il momento di scegliere dove muovere nuovamente il builder, premi il numero indicato per scegliere la direzione del movimento");
            if (possibleMoves[0][0] > 0 && possibleMoves[0][0] < 4) System.out.println("Premi 1 per spostare il builder a NORD-OVEST");
            if (possibleMoves[0][1] > 0 && possibleMoves[0][1] < 4) System.out.println("Premi 2 per spostare il builder a NORD");
            if (possibleMoves[0][2] > 0 && possibleMoves[0][2] < 4) System.out.println("Premi 3 per spostare il builder a NORD-EST");
            if (possibleMoves[1][0] > 0 && possibleMoves[1][0] < 4) System.out.println("Premi 4 per spostare il builder a OVEST");
            if (possibleMoves[1][2] > 0 && possibleMoves[1][2] < 4) System.out.println("Premi 5 per spostare il builder a EST");
            if (possibleMoves[2][0] > 0 && possibleMoves[2][0] < 4) System.out.println("Premi 6 per spostare il builder a SUD-OVEST");
            if (possibleMoves[2][1] > 0 && possibleMoves[2][1] < 4) System.out.println("Premi 7 per spostare il builder a SUD");
            if (possibleMoves[2][2] > 0 && possibleMoves[2][2] < 4) System.out.println("Premi 8 per spostare il builder a SUD-EST");
            try{
                pressedButton = scannerIn.nextInt();
                scannerIn.nextLine();
                wrong = true;

                if (pressedButton == 1 && possibleMoves[0][0] > 0 && possibleMoves[0][0] < 4) {
                    direction = Direction.NORTH_WEST;
                    wrong = false;
                } else if (pressedButton == 2 && possibleMoves[0][1] > 0 && possibleMoves[0][1] < 4) {
                    direction = Direction.NORTH;
                    wrong = false;
                } else if (pressedButton == 3 && possibleMoves[0][2] > 0 && possibleMoves[0][2] < 4) {
                    direction = Direction.NORTH_EAST;
                    wrong = false;
                } else if (pressedButton == 4 && possibleMoves[1][0] > 0 && possibleMoves[1][0] < 4) {
                    direction = Direction.WEST;
                    wrong = false;
                } else if (pressedButton == 5 && possibleMoves[1][2] > 0 && possibleMoves[1][2] < 4) {
                    direction = Direction.EAST;
                    wrong = false;
                } else if (pressedButton == 6 && possibleMoves[2][0] > 0 && possibleMoves[2][0] < 4) {
                    direction = Direction.SOUTH_WEST;
                    wrong = false;
                } else if (pressedButton == 7 && possibleMoves[2][1] > 0 && possibleMoves[2][1] < 4) {
                    direction = Direction.SOUTH;
                    wrong = false;
                } else if (pressedButton == 8 && possibleMoves[2][2] > 0 && possibleMoves[2][2] < 4) {
                    direction = Direction.SOUTH_EAST;
                    wrong = false;
                }
            } catch(InputMismatchException e) {
                scannerIn.nextLine();
                wrong = true;
            }
            if(wrong) System.out.println("hai selezionato un carattere errato per una cella sbagliata! ricorda non puoi andare su celle edifici troppo alti, sulle sscogliere, o sulle cupole ");
        } while(wrong);
        artemisParamMessage.setDirection(direction);
        return artemisParamMessage;
    }

    /**
     * the method asks to the current player to insert parameters need to use Atlas's power. the Parameter asked is direction of the cell
     * where the dome will be built
     * If the direction insert is not allowed the method wil ask to the player to insert it again. The method will also built the
     * message containing the parameters gathered
     * @param message contains information about the match such as the current player, information used for acquiring which is the playing
     * builder
     * @return the message containing the parameter acquired by the method
     */
    private AtlasParamMessage displayAtlasParamSel(MatchStateMessage message){
        AtlasParamMessage atlasParamMessage = new AtlasParamMessage();
        Direction direction = null;
        message.getCurrentPlayer().getPlayingBuilder().setBoard(new Board(message.getBoard()));
        message.getCurrentPlayer().getPlayingBuilder().setPlayer(message.getCurrentPlayer());
        int[][] neighboringLevelCell = Board.neighboringLevelCell(message.getCurrentPlayer().getPlayingBuilder());
        //INserire display per mostrare
        boolean wrong;
        int pressedButton;
        System.out.println("Ora è il momento di scegliere dove far costruire al builder una cupola, premi il numero indicato per scegliere la direzione della costruzione");
        showPossibleMatrix(neighboringLevelCell, 'b');
        do {
            //System.out.println("Ora è il momento di scegliere dove far costruire al builder una cupola, premi il numero indicato per scegliere la direzione della costruzione");
            if (neighboringLevelCell[0][0] != 4 && neighboringLevelCell[0][0] != -1) System.out.println("Premi 1 per costruire il builder a NORD-OVEST");
            if (neighboringLevelCell[0][1] != 4 && neighboringLevelCell[0][1] != -1) System.out.println("Premi 2 per costruire il builder a NORD");
            if (neighboringLevelCell[0][2] != 4 && neighboringLevelCell[0][2] != -1) System.out.println("Premi 3 per costruire il builder a NORD-EST");
            if (neighboringLevelCell[1][0] != 4 && neighboringLevelCell[1][0] != -1) System.out.println("Premi 4 per costruire il builder a OVEST");
            if (neighboringLevelCell[1][2] != 4 && neighboringLevelCell[1][2] != -1) System.out.println("Premi 5 per costruire il builder a EST");
            if (neighboringLevelCell[2][0] != 4 && neighboringLevelCell[2][0] != -1) System.out.println("Premi 6 per costruire il builder a SUD-OVEST");
            if (neighboringLevelCell[2][1] != 4 && neighboringLevelCell[2][1] != -1) System.out.println("Premi 7 per costruire il builder a SUD");
            if (neighboringLevelCell[2][2] != 4 && neighboringLevelCell[2][2] != -1) System.out.println("Premi 8 per costruire il builder a SUD-EST");
            try{
                pressedButton = scannerIn.nextInt();
                scannerIn.nextLine();
                wrong = true;

                if (pressedButton == 1 && neighboringLevelCell[0][0] != 4 && neighboringLevelCell[0][0] != -1)  {
                    direction = Direction.NORTH_WEST;
                    wrong = false;
                } else if (pressedButton == 2 && neighboringLevelCell[0][1] != 4 && neighboringLevelCell[0][1] != -1) {
                    direction = Direction.NORTH;
                    wrong = false;
                } else if (pressedButton == 3 && neighboringLevelCell[0][2] != 4 && neighboringLevelCell[0][2] != -1) {
                    direction = Direction.NORTH_EAST;
                    wrong = false;
                } else if (pressedButton == 4 && neighboringLevelCell[1][0] != 4 && neighboringLevelCell[1][0] != -1) {
                    direction = Direction.WEST;
                    wrong = false;
                } else if (pressedButton == 5 && neighboringLevelCell[1][2] != 4 && neighboringLevelCell[1][2] != -1) {
                    direction = Direction.EAST;
                    wrong = false;
                } else if (pressedButton == 6 && neighboringLevelCell[2][0] != 4 && neighboringLevelCell[2][0] != -1) {
                    direction = Direction.SOUTH_WEST;
                    wrong = false;
                } else if (pressedButton == 7 && neighboringLevelCell[2][1] != 4 && neighboringLevelCell[2][1] != -1) {
                    direction = Direction.SOUTH;
                    wrong = false;
                } else if (pressedButton == 8 && neighboringLevelCell[2][2] != 4 && neighboringLevelCell[2][2] != -1) {
                    direction = Direction.SOUTH_EAST;
                    wrong = false;
                }
            }catch(InputMismatchException e){
                scannerIn.nextLine();
                wrong = true;
            }
            if(wrong) System.out.println("hai selezionato un crattere errato! ricorda non puoi costruire cupole su delle altre cupole o sulla costa ");
        } while(wrong);
        atlasParamMessage.setDirection(direction);
        return  atlasParamMessage;
    }

    /**
     * The method asks to the current player to insert parameters need to use Demeter's power. The Parameter asked is direction of the cell
     * where the player wants to build again the builder in.
     * If the direction insert is not allowed the method wil ask to the player to insert it again. The method will also built the
     * message containing the parameters gathered
     * @param message contains information about the match such as the current player, information used for acquiring which is the playing
     * builder
     * @return the message containing the parameter acquired by the method
     */
    private DemeterParamMessage displayDemeterParamSel(MatchStateMessage message){
        DemeterParamMessage demeterParamMessage = new DemeterParamMessage();
        Direction direction = null;
        int[][] possibleBuildings = message.getCurrentPlayer().getPlayingBuilder().getPossibleBuildings();
        //INserire display per mostrare
        boolean wrong;
        int pressedButton;
        System.out.println("Ora è il momento di scegliere dove far costruire nuovamente al builder , premi il numero indicato per scegliere la direzione della costruzione");
        showPossibleMatrix(possibleBuildings, 'b');
        do {
            //System.out.println("Ora è il momento di scegliere dove far costruire nuovamente al builder , premi il numero indicato per scegliere la direzione della costruzione");
            if (possibleBuildings[0][0] != 4 && possibleBuildings[0][0] != -1 && possibleBuildings[0][0] != -2) System.out.println("Premi 1 per costruire con il builder a NORD-OVEST");
            if (possibleBuildings[0][1] != 4 && possibleBuildings[0][1] != -1 && possibleBuildings[0][1] != -2) System.out.println("Premi 2 per costruire con il builder a NORD");
            if (possibleBuildings[0][2] != 0 && possibleBuildings[0][2] != -1 && possibleBuildings[0][2] != -2) System.out.println("Premi 3 per costruire con il builder a NORD-EST");
            if (possibleBuildings[1][0] != 4 && possibleBuildings[1][0] != -1 && possibleBuildings[1][0] != -2) System.out.println("Premi 4 per costruire con il builder a OVEST");
            if (possibleBuildings[1][2] != 4 && possibleBuildings[1][2] != -1 && possibleBuildings[1][2] != -2) System.out.println("Premi 5 per costruire con il builder a EST");
            if (possibleBuildings[2][0] != 4 && possibleBuildings[2][0] != -1 && possibleBuildings[2][0] != -2) System.out.println("Premi 6 per costruire con il builder a SUD-OVEST");
            if (possibleBuildings[2][1] != 4 && possibleBuildings[2][1] != -1 && possibleBuildings[2][1] != -2) System.out.println("Premi 7 per costruire con il builder a SUD");
            if (possibleBuildings[2][2] != 4 && possibleBuildings[2][2] != -1 && possibleBuildings[2][2] != -2) System.out.println("Premi 8 per costruire con il builder a SUD-EST");
            try {
                pressedButton = scannerIn.nextInt();
                scannerIn.nextLine();
                wrong = true;

                if (pressedButton == 1 && possibleBuildings[0][0] != 4 && possibleBuildings[0][0] != -1 && possibleBuildings[0][0] != -2)  {
                    direction = Direction.NORTH_WEST;
                    wrong = false;
                } else if (pressedButton == 2 && possibleBuildings[0][1] != 4 && possibleBuildings[0][1] != -1 && possibleBuildings[0][1] != -2) {
                    direction = Direction.NORTH;
                    wrong = false;
                } else if (pressedButton == 3 && possibleBuildings[0][2] != 0 && possibleBuildings[0][2] != -1 && possibleBuildings[0][2] != -2) {
                    direction = Direction.NORTH_EAST;
                    wrong = false;
                } else if (pressedButton == 4 && possibleBuildings[1][0] != 4 && possibleBuildings[1][0] != -1 && possibleBuildings[1][0] != -2) {
                    direction = Direction.WEST;
                    wrong = false;
                } else if (pressedButton == 5 && possibleBuildings[1][2] != 4 && possibleBuildings[1][2] != -1 && possibleBuildings[1][2] != -2) {
                    direction = Direction.EAST;
                    wrong = false;
                } else if (pressedButton == 6 && possibleBuildings[2][0] != 4 && possibleBuildings[2][0] != -1 && possibleBuildings[2][0] != -2) {
                    direction = Direction.SOUTH_WEST;
                    wrong = false;
                } else if (pressedButton == 7 && possibleBuildings[2][1] != 4 && possibleBuildings[2][1] != -1 && possibleBuildings[2][1] != -2) {
                    direction = Direction.SOUTH;
                    wrong = false;
                } else if (pressedButton == 8 && possibleBuildings[2][2] != 4 && possibleBuildings[2][2] != -1 && possibleBuildings[2][2] != -2) {
                    direction = Direction.SOUTH_EAST;
                    wrong = false;
                }
            }catch (InputMismatchException e){
                scannerIn.nextLine();
                wrong = true;
            }
            if(wrong) System.out.println("hai selezionato un carattere sbagliato! ricorda non puoi costruire  su delle  cupole, sulla costa, o nella cella in cui hai costruito prima ");
        } while(wrong);
        demeterParamMessage.setDirection(direction);
        return demeterParamMessage;
    }

    /**
     * the method asks to the current player to insert parameters need to use Hestia's power. The Parameter asked is direction of the cell
     * where the player wants to moved again the builder in.
     * If the direction insert is not allowed the method wil ask to the player to insert it again. The method will also built the
     * message containing the parameters gathered
     * @param message contains information about the match such as the current player, information used for acquiring which is the playing
     * builder
     * @return the message containing the parameter acquired by the method
     */
    private HestiaParamMessage displayHestiaParamSel(MatchStateMessage message){
        HestiaParamMessage hestiaParamMessage = new HestiaParamMessage();
        Direction direction = null;
        int[] posBuilder = new int[2];
        posBuilder[0] = message.getCurrentPlayer().getPlayingBuilder().getPosX();
        posBuilder[1] = message.getCurrentPlayer().getPlayingBuilder().getPosY();

        message.getCurrentPlayer().getPlayingBuilder().setBoard(new Board(message.getBoard()));
        message.getCurrentPlayer().getPlayingBuilder().setPlayer(message.getCurrentPlayer());

        int[][] neighboringLevelCell = Board.neighboringLevelCell(message.getCurrentPlayer().getPlayingBuilder());
        if(message.getCurrentPlayer().getPlayingBuilder().getPosX() == 1 || message.getCurrentPlayer().getPlayingBuilder().getPosX() == 5){
            neighboringLevelCell[1][0] = -1;
            neighboringLevelCell[1][1] = -1;
            neighboringLevelCell[1][2] = -1;
        } else if(message.getCurrentPlayer().getPlayingBuilder().getPosX() == 2){
            neighboringLevelCell[0][0] = -1;
            neighboringLevelCell[0][1] = -1;
            neighboringLevelCell[0][2] = -1;
        } else if(message.getCurrentPlayer().getPlayingBuilder().getPosX() == 4){
            neighboringLevelCell[2][0] = -1;
            neighboringLevelCell[2][1] = -1;
            neighboringLevelCell[2][2] = -1;
        }
        if(message.getCurrentPlayer().getPlayingBuilder().getPosY() == 1 || message.getCurrentPlayer().getPlayingBuilder().getPosY() == 5){
            neighboringLevelCell[0][1] = -1;
            neighboringLevelCell[1][1] = -1;
            neighboringLevelCell[2][1] = -1;
        } else if(message.getCurrentPlayer().getPlayingBuilder().getPosY() == 2){
            neighboringLevelCell[0][0] = -1;
            neighboringLevelCell[1][0] = -1;
            neighboringLevelCell[2][0] = -1;
        } else if(message.getCurrentPlayer().getPlayingBuilder().getPosY() == 4){
            neighboringLevelCell[0][2] = -1;
            neighboringLevelCell[1][2] = -1;
            neighboringLevelCell[2][2] = -1;
        }
        //INserire display per mostrare
        boolean wrong;
        int pressedButton;
        System.out.println("Ora è il momento di scegliere dove far costruire nuovamente al builder , premi il numero indicato per scegliere la direzione della costruzione, attenzione a non scegliere una cella perimetrale!");
        showPossibleMatrix(neighboringLevelCell, 'b');
        do {
            //System.out.println("Ora è il momento di scegliere dove far costruire nuovamente al builder , premi il numero indicato per scegliere la direzione della costruzione, attenzione a non scegliere una cella perimetrale!");
            if (neighboringLevelCell[0][0] != -1 && neighboringLevelCell[0][0] != 4) System.out.println("Premi 1 per costruire con il builder a NORD-OVEST");
            if (neighboringLevelCell[0][1] != -1 && neighboringLevelCell[0][1] != 4) System.out.println("Premi 2 per costruire con il builder a NORD");
            if (neighboringLevelCell[0][2] != -1 && neighboringLevelCell[0][2] != 4) System.out.println("Premi 3 per costruire con il builder a NORD-EST");
            if (neighboringLevelCell[1][0] != -1 && neighboringLevelCell[1][0] != 4) System.out.println("Premi 4 per costruire con il builder a OVEST");
            if (neighboringLevelCell[1][2] != -1 && neighboringLevelCell[1][2] != 4) System.out.println("Premi 5 per costruire con il builder a EST");
            if (neighboringLevelCell[2][0] != -1 && neighboringLevelCell[2][0] != 4) System.out.println("Premi 6 per costruire con il builder a SUD-OVEST");
            if (neighboringLevelCell[2][1] != -1 && neighboringLevelCell[2][1] != 4) System.out.println("Premi 7 per costruire con il builder a SUD");
            if (neighboringLevelCell[2][2] != -1 && neighboringLevelCell[2][2] != 4) System.out.println("Premi 8 per costruire con il builder a SUD-EST");
            try {
                pressedButton = scannerIn.nextInt();
                scannerIn.nextLine();
                wrong = true;

                if (pressedButton == 1 && neighboringLevelCell[0][0] != 4 && neighboringLevelCell[0][0] != -1 )  {
                    //if(3 <= posBuilder[0] && posBuilder[0] <= 4 && 3 <= posBuilder[1] && posBuilder[1] <= 4 ){
                    //}
                    direction = Direction.NORTH_WEST;
                    wrong = false;

                } else if (pressedButton == 2 && neighboringLevelCell[0][1] != 4 && neighboringLevelCell[0][1] != -1) {
                    //if(3 <= posBuilder[0] && posBuilder[0] <= 4 && 2 <= posBuilder[1] && posBuilder[1] <= 4 ){
                    //}
                    direction = Direction.NORTH;
                    wrong = false;
                } else if (pressedButton == 3 && neighboringLevelCell[0][2] != 4 && neighboringLevelCell[0][2] != -1) {
                    //if(3 <= posBuilder[0] && posBuilder[0] <= 4 && 2 <= posBuilder[1] && posBuilder[1] <= 3 ){
                    //}
                    direction = Direction.NORTH_EAST;
                    wrong = false;

                } else if (pressedButton == 4 && neighboringLevelCell[1][0] != 4 && neighboringLevelCell[1][0] != -1 ) {
                    //if(2 <= posBuilder[0] && posBuilder[0] <= 4 && 3 <= posBuilder[1] && posBuilder[1] <= 4 ){
                    //}
                    direction = Direction.WEST;
                    wrong = false;

                } else if (pressedButton == 5 && neighboringLevelCell[1][2] != 4 && neighboringLevelCell[1][2] != -1 ) {
                    //if(2 <= posBuilder[0] && posBuilder[0] <= 4 && 2 <= posBuilder[1] && posBuilder[1] <= 3 ){
                    //}
                    direction = Direction.EAST;
                    wrong = false;
                } else if (pressedButton == 6 && neighboringLevelCell[2][0] != 4 && neighboringLevelCell[2][0] != -1 ) {
                    //if(2 <= posBuilder[0] && posBuilder[0] <= 3 && 3 <= posBuilder[1] && posBuilder[1] <= 4 ){
                    //}
                    direction = Direction.SOUTH_WEST;
                    wrong = false;
                } else if (pressedButton == 7 && neighboringLevelCell[2][1] != 4 && neighboringLevelCell[2][1] != -1 ) {
                    //if(2 <= posBuilder[0] && posBuilder[0] <= 3 && 2 <= posBuilder[1] && posBuilder[1] <= 4 ){
                    //}
                    direction = Direction.SOUTH;
                    wrong = false;
                } else if (pressedButton == 8 && neighboringLevelCell[2][2] != 4 && neighboringLevelCell[2][2] != -1 ) {
                    //if(2 <= posBuilder[0] && posBuilder[0] <= 3 && 2 <= posBuilder[1] && posBuilder[1] <= 3 ){
                    //}
                    direction = Direction.SOUTH_EAST;
                    wrong = false;
                }
            }catch (InputMismatchException e){
                scannerIn.nextLine();
                wrong = true;
            }
            if(wrong) System.out.println("hai selezionato un carattere sbagliato! ricorda non puoi costruire su delle  cupole, sulla costa, o nella in una cella vicino alla costa ");
        } while(wrong);
        hestiaParamMessage.setDirection(direction);
        return hestiaParamMessage;
    }

    /**
     * the method asks to the current player to insert parameters need to use Minotaur's power. These parameters are che choice of which builder
     * the player want to move (and push opponent's builder) and in which direction. If the builder selected cannot be moved, the method will choose for the player the other
     * builder. If the direction insert is not allowed the method wil ask to the player to insert it again. The method will also built the
     * message containing the parameters gathered
     * @param message contains references about the board and the currents player(About the match ).
     * @return the message containing the parameters gathered.
     */
    private MinotaurParamMessage displayMinotaurParamSel(MatchStateMessage message){
        MinotaurParamMessage minotaurParamMessage = new MinotaurParamMessage();
        //Inserire la stampa per la scelta del builder
        char playerBuilderGender = 'G';
        int[] posBuilder = new int[2];

        Builder chosenBuilderM = null;
        Direction direction = null;
        boolean wrong;
        String choice = null;
        do {
            try {
                System.out.println("Seleziona il builder più adatto a servire il Minotauro. Premi M o F. Ricorda che deve essere vicino ad un builder avversario affinché sia degno!");
                choice = scannerIn.nextLine();
                choice = choice.toUpperCase();
                wrong  = false;
                if (choice.equals("M")) {
                    chosenBuilderM = message.getCurrentPlayer().getBuilderM();
                    posBuilder[0] = message.getCurrentPlayer().getBuilderM().getPosX();
                    posBuilder[1] = message.getCurrentPlayer().getBuilderM().getPosY();
                    playerBuilderGender = 'M';
                }
                else if (choice.equals("F")){
                    chosenBuilderM = message.getCurrentPlayer().getBuilderF();
                    posBuilder[0] = message.getCurrentPlayer().getBuilderF().getPosX();
                    posBuilder[1] = message.getCurrentPlayer().getBuilderF().getPosY();
                    playerBuilderGender = 'F';
                }
                else wrong = true;
            } catch (InputMismatchException e){
                scannerIn.nextLine();
                wrong = true;
            }
            if (wrong) System.out.println("Hai inserito un carattere errato!");
        } while (wrong);

        chosenBuilderM.setBoard(new Board(message.getBoard()));
        chosenBuilderM.setPlayer( message.getCurrentPlayer());
        int[][] possibleSwap = Board.neighboringSwappingCell(chosenBuilderM, AccessType.OCCUPIED);
        //Modifico possibleSwap per includere l'impossibilità di spingere l'avversario perché la cella dopo non è FREE
        for(int i = 0; i < 3; ++i)
            for(int j = 0; j < 3; ++j)
                if(possibleSwap[i][j] != 0)
                    try {
                        if (message.getBoard()[posBuilder[0] + (i - 1) * 2][posBuilder[1] + (j - 1) * 2].getStatus() != AccessType.FREE)
                            possibleSwap[i][j] = 0;
                    }catch (IndexOutOfBoundsException e){
                        possibleSwap[i][j] = 0;
                    }

        boolean allZeros = true;
        for(int i = 0; i < 3 && allZeros; ++i)
            for(int j = 0; j < 3 && allZeros; ++j)
                if(possibleSwap[i][j] != 0) allZeros = false;

        if(allZeros){
            System.out.println("Il builder che hai selezionato non è adatto a servire il Minotauro, viene selezionato l'altro builder automaticamente");
            if(playerBuilderGender == 'M'){
                playerBuilderGender = 'F';
                chosenBuilderM = message.getCurrentPlayer().getBuilderF();
                posBuilder[0] = message.getCurrentPlayer().getBuilderF().getPosX();
                posBuilder[1] = message.getCurrentPlayer().getBuilderF().getPosY();
            } else {
                playerBuilderGender = 'M';
                chosenBuilderM = message.getCurrentPlayer().getBuilderM();
                posBuilder[0] = message.getCurrentPlayer().getBuilderM().getPosX();
                posBuilder[1] = message.getCurrentPlayer().getBuilderM().getPosY();
            }
        }
        chosenBuilderM.setBoard(new Board(message.getBoard()));
        chosenBuilderM.setPlayer( message.getCurrentPlayer());
        possibleSwap = Board.neighboringSwappingCell(chosenBuilderM, AccessType.OCCUPIED);
        //Modifico possibleSwap per includere l'impossibilità di spingere l'avversario perché la cella dopo non è FREE
        for(int i = 0; i < 3; ++i)
            for(int j = 0; j < 3; ++j)
                if(possibleSwap[i][j] != 0)
                    try {
                        if (message.getBoard()[posBuilder[0] + (i - 1) * 2][posBuilder[1] + (j - 1) * 2].getStatus() != AccessType.FREE)
                            possibleSwap[i][j] = 0;
                    }catch (IndexOutOfBoundsException e){
                        possibleSwap[i][j] = 0;
                    }
        int pressedButton;
        System.out.println("Ora è il momento di scegliere il builder avversario, premi il numero indicato per scegliere la direzione che preferisci");
        showPossibleMatrix(possibleSwap, 'm');
        //Mettere un display per far vedere le possibili scelte
        do {
            //System.out.println("Ora è il momento di scegliere il builder avversario, premi il numero indicato per scegliere la direzione che preferisci");
            if (possibleSwap[0][0] != 0) System.out.println("Premi 1 per muoverti a NORD-OVEST");
            if (possibleSwap[0][1] != 0) System.out.println("Premi 2 per muoverti a NORD");
            if (possibleSwap[0][2] != 0) System.out.println("Premi 3 per muoverti a NORD-EST");
            if (possibleSwap[1][0] != 0) System.out.println("Premi 4 per muoverti a OVEST");
            if (possibleSwap[1][2] != 0) System.out.println("Premi 5 per muoverti a EST");
            if (possibleSwap[2][0] != 0) System.out.println("Premi 6 per muoverti a SUD-OVEST");
            if (possibleSwap[2][1] != 0) System.out.println("Premi 7 per muoverti a SUD");
            if (possibleSwap[2][2] != 0) System.out.println("Premi 8 per muoverti a SUD-EST");
            try {
                pressedButton = scannerIn.nextInt();
                //scannerIn.nextLine();
                wrong = true;

                if (pressedButton == 1 && possibleSwap[0][0] != 0) {
                    //if(message.getBoard()[posBuilder[0]-2][posBuilder[1]-2].getStatus() == AccessType.FREE ) {
                    //}
                    direction = Direction.NORTH_WEST;
                    wrong = false;
                } else if (pressedButton == 2 && possibleSwap[0][1] != 0) {
                    //if(message.getBoard()[posBuilder[0]-2][posBuilder[1]].getStatus() == AccessType.FREE ) {
                    //}
                    direction = Direction.NORTH;
                    wrong = false;
                } else if (pressedButton == 3 && possibleSwap[0][2] != 0) {
                    //if(message.getBoard()[posBuilder[0]-2][posBuilder[1]+2].getStatus() == AccessType.FREE ) {
                    //}
                    direction = Direction.NORTH_EAST;
                    wrong = false;
                } else if (pressedButton == 4 && possibleSwap[1][0] != 0) {
                    //if(message.getBoard()[posBuilder[0]][posBuilder[1]-2].getStatus() == AccessType.FREE ) {
                    //}
                    direction = Direction.WEST;
                    wrong = false;
                } else if (pressedButton == 5 && possibleSwap[1][2] != 0) {
                    //if(message.getBoard()[posBuilder[0]][posBuilder[1]+2].getStatus() == AccessType.FREE ) {
                    //}
                    direction = Direction.EAST;
                    wrong = false;
                } else if (pressedButton == 6 && possibleSwap[2][0] != 0) {
                    //if(message.getBoard()[posBuilder[0]+2][posBuilder[1]-2].getStatus() == AccessType.FREE ) {
                    //}
                    direction = Direction.SOUTH_WEST;
                    wrong = false;
                } else if (pressedButton == 7 && possibleSwap[2][1] != 0) {
                    //if(message.getBoard()[posBuilder[0]+2][posBuilder[1]].getStatus() == AccessType.FREE ) {
                    //}
                    direction = Direction.SOUTH;
                    wrong = false;
                } else if (pressedButton == 8 && possibleSwap[2][2] != 0) {
                    //if(message.getBoard()[posBuilder[0]+2][posBuilder[1]+2].getStatus() == AccessType.FREE ) {
                    //}
                    direction = Direction.SOUTH_EAST;
                    wrong = false;
                }
            } catch (InputMismatchException e){
                scannerIn.nextLine();
                wrong = true;
            }
            if(wrong) System.out.println("hai selezionato una direzione sbaglitata! ricorda non puoi lanciare un builder avversario su una scogliera o su una cupola");
        } while(wrong);
        minotaurParamMessage.setOpponentBuilderDirection(direction);
        minotaurParamMessage.setPlayingBuilderSex(playerBuilderGender);
        return minotaurParamMessage;
    }

    /**
     * The method asks to the current player to insert parameters need to use Poseidon's power. The Parameters asked are direction of the cell
     * where the player wants to build again the builder in and how many times he wants to build. The method will display the max and min numbers
     * of times he can build
     * If the direction insert is not allowed the method wil ask to the player to insert it again. The method will also built the
     * message containing the parameters gathered
     * @param message contains information about the match such as the current player, information used for acquiring which is the playing
     * builder
     * @return the message containing the parameter acquired by the method
     */
    private PoseidonParamMessage displayPoseidonParamSel(MatchStateMessage message){
        PoseidonParamMessage poseidonParamMessage = new PoseidonParamMessage();
        //Ricerca del builder non mosso
        Builder constructionBuilder = null;
        char constructionBuilderSex = 'o';
        if (message.getCurrentPlayer().getPlayingBuilder().getGender() == '\u2640') {
            constructionBuilder = message.getCurrentPlayer().getBuilderM();
            constructionBuilderSex = 'M';
        } else {
            constructionBuilder = message.getCurrentPlayer().getBuilderF();
            constructionBuilderSex = 'F';
        }
        //Salvataggio della possible buildings
        constructionBuilder.setBoard(new Board(message.getBoard()));
        constructionBuilder.setPlayer(message.getCurrentPlayer());
        int[][] possibleBuildingsP = Board.neighboringLevelCell(constructionBuilder);

        int count = 0;
        for(int i = 0; i < 3; ++i)
            for(int j = 0; j < 3; ++j)
                if(possibleBuildingsP[i][j] >= 0 && possibleBuildingsP[i][j] < 4) count = count + 4 - possibleBuildingsP[i][j];

        //Salvataggio della posizione del builder
        int[] posBuilder = new int[2];
        posBuilder[0] = constructionBuilder.getPosX();
        posBuilder[1] = constructionBuilder.getPosY();
        //Mostrare la possible buildings
        //Creazione delle variabili per l'acquisizione di quante volte si vuole costruire
        if(count < 3)
            System.out.printf("Indicare il numero di volte che si vuole costruire grazie al potere di Poseidone. Inserire un numero compreso tra 1 e %d: ", count);
        else
            System.out.println("Indicare il numero di volte che si vuole costruire grazie al potere di Poseidone. Inserire un numero compreso tra 1 e 3: ");
        int numeroBuild = 0;
        boolean wrong = true;
        do{
            try{
                numeroBuild = scannerIn.nextInt();
                scannerIn.nextLine();
                if(numeroBuild > 0 && numeroBuild < 4) {wrong = false;}
            }catch (InputMismatchException e) {
                scannerIn.nextLine();
                wrong = true;
            }
            if(wrong) System.out.println("hai selezionato un caarattere non compreso tra quelli indicati");
        }while (wrong);
        //Preparazione alla ricezione delle direzioni
        ArrayList<Direction> directions = new ArrayList<>();
        int i = 0;
        int pressedButton ;
        System.out.printf("inserire ora un numero di direzioni pari a %d /n", numeroBuild);
        do {
            showPossibleMatrix(possibleBuildingsP, 'b');
            if (possibleBuildingsP[0][0] != -1 && possibleBuildingsP[0][0] != 4) System.out.println("Premi 1 per costruire con il builder a NORD-OVEST");
            if (possibleBuildingsP[0][1] != -1 && possibleBuildingsP[0][1] != 4) System.out.println("Premi 2 per costruire con il builder a NORD");
            if (possibleBuildingsP[0][2] != -1 && possibleBuildingsP[0][2] != 4) System.out.println("Premi 3 per costruire con il builder a NORD-EST");
            if (possibleBuildingsP[1][0] != -1 && possibleBuildingsP[1][0] != 4) System.out.println("Premi 4 per costruire con il builder a OVEST");
            if (possibleBuildingsP[1][2] != -1 && possibleBuildingsP[1][2] != 4) System.out.println("Premi 5 per costruire con il builder a EST");
            if (possibleBuildingsP[2][0] != -1 && possibleBuildingsP[2][0] != 4) System.out.println("Premi 6 per costruire con il builder a SUD-OVEST");
            if (possibleBuildingsP[2][1] != -1 && possibleBuildingsP[2][1] != 4) System.out.println("Premi 7 per costruire con il builder a SUD");
            if (possibleBuildingsP[2][2] != -1 && possibleBuildingsP[2][2] != 4) System.out.println("Premi 8 per costruire con il builder a SUD-EST");
            try {
                pressedButton = scannerIn.nextInt();
                scannerIn.nextLine();
                wrong = true;
                if (pressedButton == 1 && possibleBuildingsP[0][0] != -1 && possibleBuildingsP[0][0] != 4) {
                    directions.add(Direction.NORTH_WEST);
                    possibleBuildingsP[0][0] = possibleBuildingsP[0][0] + 1;
                    wrong = false;

                } else if (pressedButton == 2 && possibleBuildingsP[0][1] != -1 && possibleBuildingsP[0][1] != -4) {
                    directions.add( Direction.NORTH);
                    possibleBuildingsP[0][1] = possibleBuildingsP[0][1] + 1;
                    wrong = false;

                } else if (pressedButton == 3 && possibleBuildingsP[0][2] != -1 && possibleBuildingsP[0][2] != 4) {
                    directions.add( Direction.NORTH_EAST);
                    possibleBuildingsP[0][2] = possibleBuildingsP[0][2] + 1;
                    wrong = false;

                } else if (pressedButton == 4 && possibleBuildingsP[1][0] != -1 && possibleBuildingsP[1][0] != 4) {
                    directions.add( Direction.WEST);
                    possibleBuildingsP[1][0] = possibleBuildingsP[1][0] + 1;
                    wrong = false;

                } else if (pressedButton == 5 && possibleBuildingsP[1][2] != -1 && possibleBuildingsP[1][2] != 4) {
                    directions.add(Direction.EAST);
                    possibleBuildingsP[1][2] = possibleBuildingsP[1][2] +1;
                    wrong = false;

                } else if (pressedButton == 6 && possibleBuildingsP[2][0] != -1 && possibleBuildingsP[2][0] != 4) {
                    directions.add(Direction.SOUTH_WEST);
                    possibleBuildingsP[2][0] = possibleBuildingsP[2][0] +1;
                    wrong = false;

                } else if (pressedButton == 7 && possibleBuildingsP[2][1] != -1 && possibleBuildingsP[2][1] != 4) {
                    directions.add(Direction.SOUTH);
                    possibleBuildingsP[2][1] = possibleBuildingsP[2][1] + 1;
                    wrong = false;

                } else if (pressedButton == 8 && possibleBuildingsP[2][2] != -1 && possibleBuildingsP[2][2] != 4) {
                    directions.add(Direction.SOUTH_EAST);
                    possibleBuildingsP[2][2] = possibleBuildingsP[2][2] + 1;
                    wrong = false;
                }
            } catch (InputMismatchException e){
                scannerIn.nextLine();
                wrong = true;
            }
            if(wrong) System.out.println("hai selezionato una direzione sbaglitata! ricorda non puoi costruire qui");
            else { ++i;}
        } while(i < numeroBuild);
        poseidonParamMessage.setConstructionGender(constructionBuilderSex);
        poseidonParamMessage.setDirection(directions);
        poseidonParamMessage.setNumberOfBuild(numeroBuild);
        return poseidonParamMessage;
    }

    /**
     * the method asks to the current player to insert parameters need to use Prometheus's power. These parameters are the choice of which builder
     * the player want to build (the choice of the builder will be memorized to be used for the next phase of movement and building)
     * and in which direction.
     * If the builder selected cannot be moved, the method will choose for the player the other
     * builder. If the direction insert is not allowed the method wil ask to the player to insert it again. The method will also built the
     * message containing the parameters gathered
     * @param message contains references about the board and the currents player(About the match ).
     * @return the message containing the parameters gathered.
     */
    private PrometheusParamMessage displayPrometheusParamSel(MatchStateMessage message){
        PrometheusParamMessage prometheusParamMessage = new PrometheusParamMessage();
        Builder builderScelto = null;
        char builderSex = '0';
        int[] posBuilder = new int[2];
        Direction direction = null;
        //Scelta del builder
        String choice;
        boolean wrong;
        message.getCurrentPlayer().setRiseActions(false);
        message.getCurrentPlayer().setMoveActions(true);
        message.getCurrentPlayer().getBuilderF().setBoard(new Board(message.getBoard()));
        message.getCurrentPlayer().getBuilderF().setPlayer(message.getCurrentPlayer());
        message.getCurrentPlayer().getBuilderM().setBoard(new Board(message.getBoard()));
        message.getCurrentPlayer().getBuilderM().setPlayer(message.getCurrentPlayer());
        System.out.println("Seleziona il builder più adatto a servire Prometeo.");
        do {
            if(message.getCurrentPlayer().getBuilderM().canMove() && message.getCurrentPlayer().getBuilderM().canBuild())
                System.out.println("Premi il tasto M per selezionare il maschio");
            if(message.getCurrentPlayer().getBuilderF().canMove())
                System.out.println("Premi il tasto F per selezionare la femmina");
            try{
                wrong  = false;
                choice = scannerIn.nextLine();
                choice = choice.toUpperCase();
                if (choice.equals("M")) {
                    builderScelto = message.getCurrentPlayer().getBuilderM();
                    posBuilder[0] = message.getCurrentPlayer().getBuilderM().getPosX();
                    posBuilder[1] = message.getCurrentPlayer().getBuilderM().getPosY();
                    builderSex = 'M';
                    if(!builderScelto.canMove()) wrong = true;
                } else if (choice.equals("F")){
                    builderScelto = message.getCurrentPlayer().getBuilderF();
                    posBuilder[0] = message.getCurrentPlayer().getBuilderF().getPosX();
                    posBuilder[1] = message.getCurrentPlayer().getBuilderF().getPosY();
                    builderSex = 'F';
                    if(!builderScelto.canMove()) wrong = true;
                } else wrong = true;
            }catch(InputMismatchException e){
                scannerIn.nextLine();
                wrong = true;
            }
            if (wrong){
                System.out.println("Lettera Sbagliata, reinserisci");
            }
        } while (wrong);
        //scelta direzione di costruzione
        builderScelto.setBoard(new Board(message.getBoard()));
        builderScelto.setPlayer(message.getCurrentPlayer());
        int[][] possibleBuildingsPr = Board.neighboringLevelCell(builderScelto);
        int pressedButton;
        System.out.println("Ora è il momento di scegliere il builder avversario, premi il numero indicato per scegliere la direzione che preferisci");
        showPossibleMatrix(possibleBuildingsPr, 'b');
        do {
            //System.out.println("Ora è il momento di scegliere il builder avversario, premi il numero indicato per scegliere la direzione che preferisci");
            if (possibleBuildingsPr[0][0] >= 0 && possibleBuildingsPr[0][0] < 4) System.out.println("Premi 1 per costruire con il builder a NORD-OVEST");
            if (possibleBuildingsPr[0][1] >= 0 && possibleBuildingsPr[0][1] < 4) System.out.println("Premi 2 per costruire con il builder a NORD");
            if (possibleBuildingsPr[0][2] >= 0 && possibleBuildingsPr[0][2] < 4) System.out.println("Premi 3 per costruire con il builder a NORD-EST");
            if (possibleBuildingsPr[1][0] >= 0 && possibleBuildingsPr[1][0] < 4) System.out.println("Premi 4 per costruire con il builder a OVEST");
            if (possibleBuildingsPr[1][2] >= 0 && possibleBuildingsPr[1][2] < 4) System.out.println("Premi 5 per costruire con il builder a EST");
            if (possibleBuildingsPr[2][0] >= 0 && possibleBuildingsPr[2][0] < 4) System.out.println("Premi 6 per costruire con il builder a SUD-OVEST");
            if (possibleBuildingsPr[2][1] >= 0 && possibleBuildingsPr[2][1] < 4) System.out.println("Premi 7 per costruire con il builder a SUD");
            if (possibleBuildingsPr[2][2] >= 0 && possibleBuildingsPr[2][2] < 4) System.out.println("Premi 8 per costruire con il builder a SUD-EST");
            try{
                pressedButton = scannerIn.nextInt();
                scannerIn.nextLine();
                wrong = true;

                if (pressedButton == 1 && possibleBuildingsPr[0][0] >= 0 && possibleBuildingsPr[0][0] < 4) {
                    if(message.getBoard()[posBuilder[0]-1][posBuilder[1]-1].getStatus() == AccessType.FREE ) {
                        direction = Direction.NORTH_WEST;
                        wrong = false;
                    }

                } else if (pressedButton == 2 && possibleBuildingsPr[0][1] >= 0 && possibleBuildingsPr[0][1] < 4) {
                    if(message.getBoard()[posBuilder[0]-1][posBuilder[1]].getStatus() == AccessType.FREE ) {
                        direction = Direction.NORTH;
                        wrong = false;
                    }

                } else if (pressedButton == 3 && possibleBuildingsPr[0][2] >= 0 && possibleBuildingsPr[0][2] < 4) {
                    if(message.getBoard()[posBuilder[0]-1][posBuilder[1]+1].getStatus() == AccessType.FREE ) {
                        direction = Direction.NORTH_EAST;
                        wrong = false;
                    }

                } else if (pressedButton == 4 && possibleBuildingsPr[1][0] >= 0 && possibleBuildingsPr[1][0] < 4) {
                    if(message.getBoard()[posBuilder[0]][posBuilder[1]-1].getStatus() == AccessType.FREE ) {
                        direction = Direction.WEST;
                        wrong = false;
                    }

                } else if (pressedButton == 5 && possibleBuildingsPr[1][2] >= 0 && possibleBuildingsPr[1][2] < 4) {
                    if(message.getBoard()[posBuilder[0]][posBuilder[1]+1].getStatus() == AccessType.FREE ) {
                        direction = Direction.EAST;
                        wrong = false;
                    }

                } else if (pressedButton == 6 && possibleBuildingsPr[2][0] >= 0 && possibleBuildingsPr[2][0] < 4) {
                    if(message.getBoard()[posBuilder[0]+1][posBuilder[1]-1].getStatus() == AccessType.FREE ) {
                        direction = Direction.SOUTH_WEST;
                        wrong = false;
                    }

                } else if (pressedButton == 7 && possibleBuildingsPr[2][1] >= 0 && possibleBuildingsPr[2][1] < 4) {
                    if(message.getBoard()[posBuilder[0]+1][posBuilder[1]].getStatus() == AccessType.FREE ) {
                        direction = Direction.SOUTH;
                        wrong = false;
                    }

                } else if (pressedButton == 8 && possibleBuildingsPr[2][2] >= 0 && possibleBuildingsPr[2][2] < 4) {
                    if(message.getBoard()[posBuilder[0]+1][posBuilder[1]+1].getStatus() == AccessType.FREE ) {
                        direction = Direction.SOUTH_EAST;
                        wrong = false;
                    }
                }
            }catch(InputMismatchException e){
                scannerIn.nextLine();
                wrong = true;
            }
            if(wrong) System.out.println("hai selezionato un carattere sbagliato! ricorda non puoi lanciare un builder avversario su una scogliera o su una cupola");
        } while(wrong);
        prometheusParamMessage.setDirection(direction);
        prometheusParamMessage.setBuilderSex(builderSex);
        return prometheusParamMessage;
    }
}
