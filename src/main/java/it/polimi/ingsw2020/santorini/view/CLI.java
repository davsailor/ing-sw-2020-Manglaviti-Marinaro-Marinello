package it.polimi.ingsw2020.santorini.view;

import it.polimi.ingsw2020.santorini.model.*;
import it.polimi.ingsw2020.santorini.model.gods.Prometheus;
import it.polimi.ingsw2020.santorini.network.client.Client;
import it.polimi.ingsw2020.santorini.network.client.ServerAdapter;
import it.polimi.ingsw2020.santorini.network.client.ViewAdapter;
import it.polimi.ingsw2020.santorini.utils.*;
import it.polimi.ingsw2020.santorini.utils.messages.actions.*;
import it.polimi.ingsw2020.santorini.utils.messages.matchMessage.SelectedBuilderPositionMessage;
import it.polimi.ingsw2020.santorini.utils.messages.errors.IllegalPositionMessage;
import it.polimi.ingsw2020.santorini.utils.messages.godsParam.*;
import it.polimi.ingsw2020.santorini.utils.messages.matchMessage.*;

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
        message.buildSynchronizationMessage(SecondHeaderType.BEGIN_MATCH);
        client.getNetworkHandler().send(message);
    }

    /**
     * metodo addetto alla selezione dei builder secondo l'ordine definito dal controller
     * @param matchStateMessage
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
            message.buildSelectedBuilderPosMessage(new SelectedBuilderPositionMessage(client.getUsername(), builderF, builderM));
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
        newPos.buildSelectedBuilderPosMessage(new SelectedBuilderPositionMessage(client.getUsername(), builderF, builderM));
        System.out.println("In attesa che gli dei controllino le tue scelte...");
        client.getNetworkHandler().send(newPos);
    }

    /**
     * method that update the board every time that the model is modified
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
                    AresParamMessage aresParamMessage = new AresParamMessage();
                    int[] targetedBlock = new int[2];
                    System.out.println("Inserisci le coordinate di una qualsiasi cella che abbia almeno un edificio (massimo livello 3)");
                    System.out.printf("Inserisci la riga: ");
                    targetedBlock[0] = scannerIn.nextInt();
                    System.out.printf("Inserisci la colonna: ");
                    targetedBlock[1] = scannerIn.nextInt();
                    aresParamMessage.setTargetedBlock(targetedBlock);
                    selectedParam.buildAresParamMessage(displayAresParamSel(message));
                    break;
                case "Artemis":
                    selectedParam.buildArtemisParamMessage(displayArtemisParamSel(message));
                    ArtemisParamMessage artemisParamMessage = new ArtemisParamMessage();
                    int[] position = new int[2];
                    System.out.println("Inserisci le coordinate della prossima cella accessibile che il tuo costruttore vuole occupare (attenzione, il tuo costruttore non vuole tornare nella cella precedente!");
                    System.out.printf("Inserisci la riga: ");
                    position[0] = scannerIn.nextInt();
                    System.out.printf("Inserisci la colonna: ");
                    position[1] = scannerIn.nextInt();
                    artemisParamMessage.setPosition(position);
                    selectedParam.buildArtemisParamMessage(artemisParamMessage);
                    break;
                case "Atlas":
                    selectedParam.buildAtlasParamMessage(displayAtlasParamSel(message));
                    AtlasParamMessage atlasParamMessage = new AtlasParamMessage();
                    int[] targetCell = new int[2];
                    System.out.println("inserisci le coordinate della cella in cui vuoi costruire una DOME dove già non c'è");
                    System.out.printf("Inserisci la riga: ");
                    targetCell[0] = scannerIn.nextInt();
                    System.out.printf("Inserisci la colonna: ");
                    targetCell[1] = scannerIn.nextInt();
                    atlasParamMessage.setTargetCell(targetCell);
                    selectedParam.buildAtlasParamMessage(atlasParamMessage);
                    break;
                case "Demeter":
                    selectedParam.buildDemeterParamMessage(displayDemeterParamSel(message));
                    DemeterParamMessage demeterParamMessage = new DemeterParamMessage();
                    int[] targetedCell = new int[2];
                    System.out.println("Inserisci le coorinate della cella in cui vuoi che il tuo costruttore costruisca di nuovo. Attenzione, il tuo builder non vuole costruire nella stessa cella!");
                    System.out.printf("Inserisci la riga: ");
                    targetedCell[0] = scannerIn.nextInt();
                    System.out.printf("Inserisci la colonna: ");
                    targetedCell[1] = scannerIn.nextInt();
                    demeterParamMessage.setTargetedCell(targetedCell);
                    selectedParam.buildDemeterParamMessage(demeterParamMessage);
                    break;
                case "Hestia":
                    selectedParam.buildHestiaParamMessage(displayHestiaParamSel(message));
                    HestiaParamMessage hestiaParamMessage = new HestiaParamMessage();
                    int[] hestiaTarget = new int[2];
                    System.out.println("Inserisci le coordinate della cella in cui vuoi che il tuo costruttore costruisca di nuovo. Attenzione, il tuo costruttore soffre momentaneamente di vertigini, quindi non potrà costruire vicino alla costa");
                    System.out.printf("Inserisci la riga: ");
                    hestiaTarget[0] = scannerIn.nextInt();
                    System.out.printf("Inserisci la colonna: ");
                    hestiaTarget[1] = scannerIn.nextInt();
                    hestiaParamMessage.setTargetedCell(hestiaTarget);
                    selectedParam.buildHestiaParamMessage(hestiaParamMessage);
                    break;
                case "Minotaur":
                    selectedParam.buildMinotaurParamMessage(displayMinotaurParamSel(message));
                    MinotaurParamMessage minotaurParamMessage = new MinotaurParamMessage();
                    int[] yourBuilderMinotaur = new int[2];
                    int[] opponentBuilderMinotaur = new int[2];
                    System.out.println("Inserisci le coordinate del costruttore che vuoi muovere. Attenzione, il tuo costruttore deve essere vicino ad un costruttore avversario; inoltre la cella successiva guardando verso il costruttore avversario deve essere libera.");
                    System.out.printf("Inserisci la riga: ");
                    yourBuilderMinotaur[0] = scannerIn.nextInt();
                    System.out.printf("Inserisci la colonna: ");
                    yourBuilderMinotaur[1] = scannerIn.nextInt();
                    System.out.println("Inserisci le coordinate del costruttore avversario vicino al tuo costruttore che hai scelto. L'avversario deve prepararsi all'incornata!");
                    System.out.printf("Inserisci la riga: ");
                    opponentBuilderMinotaur[0] = scannerIn.nextInt();
                    System.out.printf("Inserisci la colonna: ");
                    opponentBuilderMinotaur[1] = scannerIn.nextInt();
                    minotaurParamMessage.setYourBuilder(yourBuilderMinotaur);
                    minotaurParamMessage.setOpponentBuilder(opponentBuilderMinotaur);
                    selectedParam.buildMinotaurParamMessage(minotaurParamMessage);
                    break;
                case "Poseidon":
                    selectedParam.buildPoseidonParamMessage(displayPoseidonParamSel(message));
                    PoseidonParamMessage poseidonParamMessage = new PoseidonParamMessage();
                    int[] poseidonTarget = new int[2];
                    System.out.println("Inserisci le coordinate della cella in cui vuoi che il tuo costruttore rimasto fermo costruisca");
                    System.out.printf("Inserisci la riga: ");
                    poseidonTarget[0] = scannerIn.nextInt();
                    System.out.printf("Inserisci la colonna: ");
                    poseidonTarget[1] = scannerIn.nextInt();
                    poseidonParamMessage.setTargetedCell(poseidonTarget);
                    selectedParam.buildPoseidonParamMessage(poseidonParamMessage);
                    break;
                case "Prometheus":
                    selectedParam.buildPrometheusParamMessage(displayPrometheusParamSel(message));
                    PrometheusParamMessage prometheusParamMessage = new PrometheusParamMessage();
                    int[] builder = new int[2];
                    int[] prometheusTarget = new int[2];
                    System.out.println("Scegli il builder che userai in questo turno, mi raccomando sceglilo con cura, perchè non portrà salire di livello");
                    System.out.printf("Inserisci la riga: ");
                    builder[0] = scannerIn.nextInt();
                    System.out.printf("Inserisci la colonna: ");
                    builder[1] = scannerIn.nextInt();
                    System.out.println("Ora scegli la cella in cui vuoi che il builder scelto costruisca prima di muoversi");
                    System.out.printf("Inserisci la riga: ");
                    prometheusTarget[0] = scannerIn.nextInt();
                    System.out.printf("Inserisci la colonna: ");
                    prometheusTarget[1] = scannerIn.nextInt();
                    prometheusParamMessage.setBuilder(builder);
                    prometheusParamMessage.setTargetedCell(prometheusTarget);
                    selectedParam.buildPrometheusParamMessage(prometheusParamMessage);
                    break;
                default:
                    break;
            }
            client.getNetworkHandler().send(selectedParam);
        }
    }

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

    @Override
    public void displayChooseBuilder(MatchStateMessage message) {
        if(message.getCurrentPlayer().getNickname().equals(client.getUsername())) {
            showBoard(message.getCells());
            System.out.println("Quale builder vuoi muovere? Il maschio o la femmina?");
            System.out.println("Premi il tasto M per selezionare il maschio, F per la femmina");
            Message chosenBuilder = new Message(client.getUsername());
            String choice = scannerIn.nextLine();
            choice = choice.toUpperCase();
            boolean wrong = false;
            do {
                if (choice.equals("M"))
                    chosenBuilder.buildSelectedBuilderMessage(new SelectedBuilderMessage('M'));
                else if (choice.equals("F"))
                    chosenBuilder.buildSelectedBuilderMessage(new SelectedBuilderMessage('F'));
                else
                    wrong = true;
            } while (wrong);
            client.getNetworkHandler().send(chosenBuilder);
        }
    }

    /**
     * method that shows to the player his possible moves
     * metodo che mostra all'utente le possibili mosse che il builder selezionato può fare
     */
    @Override
    public void displayPossibleMoves(AskMoveSelectionMessage message) {
        Direction direction = Direction.EAST;
        Message moveSelection = new Message(client.getUsername());
        moveSelection.buildSelectedMoveMessage(new SelectedMoveMessage(direction));
        client.getNetworkHandler().send(moveSelection);
        // display delle possible moves per restringere il campo d'azione -> vedi canMove
        // conversione della direzione
        // creazione del messaggio di selezione
        // invio del messaggio al server
    }

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
     * method that shows to the player the possible block that his builder can do
     * metodo che mostra all'utente le possibili costruzioni che il builder mosso può fare
     */
    @Override
    public void displayPossibleBuildings(AskBuildSelectionMessage message) {
        Direction direction = Direction.EAST;
        Message buildSelection = new Message(client.getUsername());
        buildSelection.buildSelectedBuildingMessage(new SelectedBuildingMessage(direction));
        client.getNetworkHandler().send(buildSelection);
        // display delle possible buildings per restringere il campo d'azione -> vedi canBuild
        // conversione della direzione
        // creazione del messaggio di selezione
        // invio del messaggio al server
    }

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
     * prova
     *
     * @param updateMessage parameter
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
     * method that shows winner and losers. It then close the match
     * metodo che mostra vincitori e vinti. conclude la partita con epic sax guy
     * @param winner
     */
    @Override
    public void displayEndMatch(String winner) {
        System.out.println("AND THE WINNER IS... " + winner);
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
        String coast = "\u25DE\u25DC\u25B2 ";
        //wave: \u25DE\u25DC
        //mountain: \u25B2
        //configurazione funzionante: "\u25DE\u25DC\u25B2 "
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

    private ApolloParamMessage displayApolloParamSel(MatchStateMessage message){
        ApolloParamMessage apolloParamMessage = new ApolloParamMessage();
        //Inserire la sampa per la scelta del builder
        char yourBuilderGender = 'O';
        Builder chosen = null;
        Direction direction = null;
        System.out.println("Seleziona il builder più adatto a servire Apollo. Ricorda che deve essere vicino ad un builder avversario affinché sia degno!");
        String choice = scannerIn.nextLine();
        choice = choice.toUpperCase();
        boolean wrong;
        do {
            wrong  = false;
            if (choice.equals("M")) {
                chosen = message.getCurrentPlayer().getBuilderM();
                yourBuilderGender = 'M';
            }
            else if (choice.equals("F")){
                chosen = message.getCurrentPlayer().getBuilderF();
                yourBuilderGender = 'F';
            }
            else wrong = true;
        } while (wrong);

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
        //Mettere un display per far vedere le possibili scelte
        do {

            System.out.println("Ora è il momento di scegliere il costruttore avversario, premi il numero indicato per scegliere la direzione che preferisci");
            if (neighboringSwappingCell[0][0] != 0) System.out.println("Premi 1 per andare a NORD-OVEST");
            if (neighboringSwappingCell[0][1] != 0) System.out.println("Premi 2 per andare a NORD");
            if (neighboringSwappingCell[0][2] != 0) System.out.println("Premi 3 per andare a NORD-EST");
            if (neighboringSwappingCell[1][0] != 0) System.out.println("Premi 4 per andare a OVEST");
            if (neighboringSwappingCell[1][2] != 0) System.out.println("Premi 5 per andare a EST");
            if (neighboringSwappingCell[2][0] != 0) System.out.println("Premi 6 per andare a SUD-OVEST");
            if (neighboringSwappingCell[2][1] != 0) System.out.println("Premi 7 per andare a SUD");
            if (neighboringSwappingCell[2][2] != 0) System.out.println("Premi 8 per andare a SUD-EST");

            int pressedButton;
            pressedButton = scannerIn.nextInt();
            scannerIn.nextLine();
            wrong = true;

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
            }
        } while(wrong);

        apolloParamMessage.setYourBuilderGender(yourBuilderGender);
        apolloParamMessage.setOpponentBuilderDirection(direction);
        return apolloParamMessage;
    }

    private AresParamMessage displayAresParamSel(MatchStateMessage message){
        AresParamMessage aresParamMessage = new AresParamMessage();
        Direction direction = null;
        Builder demolitionBuilder = null;
        char demolitionBuilderSex ='O' ;
        if (message.getCurrentPlayer().getPlayingBuilder() == message.getCurrentPlayer().getBuilderF()) {
            demolitionBuilder = message.getCurrentPlayer().getBuilderM();
            demolitionBuilderSex = 'M';
        }
        else {
            demolitionBuilder = message.getCurrentPlayer().getBuilderF();
            demolitionBuilderSex = 'F';
        }
        int[][] neighboringLevelCell = Board.neighboringLevelCell(demolitionBuilder);

        //INserire display per mostrare le possibili costruzioni

        boolean wrong;
        do {

            System.out.println("Ora è il momento di scegliere il blocco da demolire, premi il numero indicato per scegliere la direzione del blocco che preferisci distruggere");
            if (neighboringLevelCell[0][0] != 0) System.out.println("Premi 1 per demolire il blocco a NORD-OVEST");
            if (neighboringLevelCell[0][1] != 0) System.out.println("Premi 2 per demolire il blocco a NORD");
            if (neighboringLevelCell[0][2] != 0) System.out.println("Premi 3 per demolire il blocco a NORD-EST");
            if (neighboringLevelCell[1][0] != 0) System.out.println("Premi 4 per demolire il blocco a OVEST");
            if (neighboringLevelCell[1][2] != 0) System.out.println("Premi 5 per demolire il blocco a EST");
            if (neighboringLevelCell[2][0] != 0) System.out.println("Premi 6 per demolire il blocco a SUD-OVEST");
            if (neighboringLevelCell[2][1] != 0) System.out.println("Premi 7 per demolire il blocco a SUD");
            if (neighboringLevelCell[2][2] != 0) System.out.println("Premi 8 per demolire il blocco a SUD-EST");

            int pressedButton;
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
            } else if (pressedButton == 5 && neighboringLevelCell[1][2] != 0 && neighboringLevelCell[2][0] != 0) {
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
            if(wrong) System.out.println("hai selezionato una direzione per un blocco sbagliate! ricorda non puoi demolire il terreno o una cupola");
        } while(wrong);


        aresParamMessage.setDemolitionBuilderSex(demolitionBuilderSex);
        aresParamMessage.setTargetedBlock(direction);
        return aresParamMessage;
    }

    private ArtemisParamMessage displayArtemisParamSel(MatchStateMessage message){
        ArtemisParamMessage artemisParamMessage = new ArtemisParamMessage();
        int[][] possibleMoves = message.getCurrentPlayer().getPlayingBuilder().getPossibleMoves();
        Direction direction = null;

        //INserire display per mostrare
        boolean wrong;
        do {

            System.out.println("Ora è il momento di scegliere dove muovere nuovamente il builder, premi il numero indicato per scegliere la direzione del movimento");
            if (possibleMoves[0][0] != 0) System.out.println("Premi 1 per spostare il builder a NORD-OVEST");
            if (possibleMoves[0][1] != 0) System.out.println("Premi 2 per spostare il builder a NORD");
            if (possibleMoves[0][2] != 0) System.out.println("Premi 3 per spostare il builder a NORD-EST");
            if (possibleMoves[1][0] != 0) System.out.println("Premi 4 per spostare il builder a OVEST");
            if (possibleMoves[1][2] != 0) System.out.println("Premi 5 per spostare il builder a EST");
            if (possibleMoves[2][0] != 0) System.out.println("Premi 6 per spostare il builder a SUD-OVEST");
            if (possibleMoves[2][1] != 0) System.out.println("Premi 7 per spostare il builder a SUD");
            if (possibleMoves[2][2] != 0) System.out.println("Premi 8 per spostare il builder a SUD-EST");

            int pressedButton;
            pressedButton = scannerIn.nextInt();
            scannerIn.nextLine();
            wrong = true;

            if (pressedButton == 1 && possibleMoves[0][0] != 0 && possibleMoves[0][0] != 4) {
                direction = Direction.NORTH_WEST;
                wrong = false;
            } else if (pressedButton == 2 && possibleMoves[0][1] != 0 && possibleMoves[0][1] != 4) {
                direction = Direction.NORTH;
                wrong = false;
            } else if (pressedButton == 3 && possibleMoves[0][2] != 0 && possibleMoves[0][2] != 4) {
                direction = Direction.NORTH_EAST;
                wrong = false;
            } else if (pressedButton == 4 && possibleMoves[1][0] != 0 && possibleMoves[1][0] != 4) {
                direction = Direction.WEST;
                wrong = false;
            } else if (pressedButton == 5 && possibleMoves[1][2] != 0 && possibleMoves[2][0] != 0) {
                direction = Direction.EAST;
                wrong = false;
            } else if (pressedButton == 6 && possibleMoves[2][0] != 0 && possibleMoves[2][0] != 4) {
                direction = Direction.SOUTH_WEST;
                wrong = false;
            } else if (pressedButton == 7 && possibleMoves[2][1] != 0 &&possibleMoves[2][1] != 4) {
                direction = Direction.SOUTH;
                wrong = false;
            } else if (pressedButton == 8 && possibleMoves[2][2] != 0 && possibleMoves[2][2] != 4) {
                direction = Direction.SOUTH_EAST;
                wrong = false;
            }
            if(wrong) System.out.println("hai selezionato una direzione per una cella sbagliata! ricorda non puoi andare su celle edifici troppo alti, sulle sscogliere, o sulle cupole ");
        } while(wrong);

        artemisParamMessage.setDirection(direction);
        artemisParamMessage.setPlayingBuilderGender(message.getCurrentPlayer().getPlayingBuilder().getGender());
        return artemisParamMessage;
    }

    private AtlasParamMessage displayAtlasParamSel(MatchStateMessage message){
        AtlasParamMessage atlasParamMessage = new AtlasParamMessage();
        Direction direction = null;
        int[][] neighboringLevelCell = Board.neighboringLevelCell(message.getCurrentPlayer().getPlayingBuilder());
        //INserire display per mostrare
        boolean wrong;
        do {

            System.out.println("Ora è il momento di scegliere dove far costruire al builder una cupola, premi il numero indicato per scegliere la direzione della costruzione");
            if (neighboringLevelCell[0][0] != 0) System.out.println("Premi 1 per costruire il builder a NORD-OVEST");
            if (neighboringLevelCell[0][1] != 0) System.out.println("Premi 2 per costruire il builder a NORD");
            if (neighboringLevelCell[0][2] != 0) System.out.println("Premi 3 per costruire il builder a NORD-EST");
            if (neighboringLevelCell[1][0] != 0) System.out.println("Premi 4 per costruire il builder a OVEST");
            if (neighboringLevelCell[1][2] != 0) System.out.println("Premi 5 per costruire il builder a EST");
            if (neighboringLevelCell[2][0] != 0) System.out.println("Premi 6 per costruire il builder a SUD-OVEST");
            if (neighboringLevelCell[2][1] != 0) System.out.println("Premi 7 per costruire il builder a SUD");
            if (neighboringLevelCell[2][2] != 0) System.out.println("Premi 8 per costruire il builder a SUD-EST");

            int pressedButton;
            pressedButton = scannerIn.nextInt();
            scannerIn.nextLine();
            wrong = true;

            if (pressedButton == 1 && neighboringLevelCell[0][0] != 4 && neighboringLevelCell[0][0] != -1)  {
                direction = Direction.NORTH_WEST;
                wrong = false;
            } else if (pressedButton == 2 && neighboringLevelCell[0][1] != 4 && neighboringLevelCell[0][1] != -1) {
                direction = Direction.NORTH;
                wrong = false;
            } else if (pressedButton == 3 && neighboringLevelCell[0][2] != 0 && neighboringLevelCell[0][2] != -1) {
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
            if(wrong) System.out.println("hai selezionato una direzione per una cella sbagliata! ricorda non puoi costruire cupole su delle altre cupole o sulla costa ");
        } while(wrong);

        atlasParamMessage.setDirection(direction);
        atlasParamMessage.setPlayingBuilderSex(message.getCurrentPlayer().getPlayingBuilder().getGender());
        return  atlasParamMessage;
    }

    private DemeterParamMessage displayDemeterParamSel(MatchStateMessage message){
        DemeterParamMessage demeterParamMessage = new DemeterParamMessage();
        Direction direction = null;
        int[][] possibleBuildings = message.getCurrentPlayer().getPlayingBuilder().getPossibleBuildings();
        //INserire display per mostrare
        boolean wrong;
        do {

            System.out.println("Ora è il momento di scegliere dove far costruire nuovamente al builder , premi il numero indicato per scegliere la direzione della costruzione");
            if (possibleBuildings[0][0] != 0) System.out.println("Premi 1 per costruire con il builder a NORD-OVEST");
            if (possibleBuildings[0][1] != 0) System.out.println("Premi 2 per costruire con il builder a NORD");
            if (possibleBuildings[0][2] != 0) System.out.println("Premi 3 per costruire con il builder a NORD-EST");
            if (possibleBuildings[1][0] != 0) System.out.println("Premi 4 per costruire con il builder a OVEST");
            if (possibleBuildings[1][2] != 0) System.out.println("Premi 5 per costruire con il builder a EST");
            if (possibleBuildings[2][0] != 0) System.out.println("Premi 6 per costruire con il builder a SUD-OVEST");
            if (possibleBuildings[2][1] != 0) System.out.println("Premi 7 per costruire con il builder a SUD");
            if (possibleBuildings[2][2] != 0) System.out.println("Premi 8 per costruire con il builder a SUD-EST");

            int pressedButton;
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
            } else if (pressedButton == 6 && possibleBuildings[2][0] != 4 && possibleBuildings[2][0] != -1 && possibleBuildings[1][2] != -2) {
                direction = Direction.SOUTH_WEST;
                wrong = false;
            } else if (pressedButton == 7 && possibleBuildings[2][1] != 4 && possibleBuildings[2][1] != -1 && possibleBuildings[2][1] != -2) {
                direction = Direction.SOUTH;
                wrong = false;
            } else if (pressedButton == 8 && possibleBuildings[2][2] != 4 && possibleBuildings[2][2] != -1 && possibleBuildings[2][2] != -2) {
                direction = Direction.SOUTH_EAST;
                wrong = false;
            }
            if(wrong) System.out.println("hai selezionato una direzione per una cella sbagliata! ricorda non puoi costruire  su delle  cupole, sulla costa, o nella cella in cui hai costruito prima ");
        } while(wrong);

        demeterParamMessage.setDirection(direction);
        demeterParamMessage.setPlayingBuilderSex(message.getCurrentPlayer().getPlayingBuilder().getGender());
        return demeterParamMessage;
    }

    private HestiaParamMessage displayHestiaParamSel(MatchStateMessage message){
        HestiaParamMessage hestiaParamMessage = new HestiaParamMessage();
        Direction direction = null;
        int[] posBuilder = new int[2];
        posBuilder[0] = message.getCurrentPlayer().getPlayingBuilder().getPosX();
        posBuilder[1] = message.getCurrentPlayer().getPlayingBuilder().getPosY();
        int[][] possibleBuildingsH = message.getCurrentPlayer().getPlayingBuilder().getPossibleBuildings();
        //INserire display per mostrare
        boolean wrong;
        do {

            System.out.println("Ora è il momento di scegliere dove far costruire nuovamente al builder , premi il numero indicato per scegliere la direzione della costruzione");
            if (possibleBuildingsH[0][0] != 0) System.out.println("Premi 1 per costruire con il builder a NORD-OVEST");
            if (possibleBuildingsH[0][1] != 0) System.out.println("Premi 2 per costruire con il builder a NORD");
            if (possibleBuildingsH[0][2] != 0) System.out.println("Premi 3 per costruire con il builder a NORD-EST");
            if (possibleBuildingsH[1][0] != 0) System.out.println("Premi 4 per costruire con il builder a OVEST");
            if (possibleBuildingsH[1][2] != 0) System.out.println("Premi 5 per costruire con il builder a EST");
            if (possibleBuildingsH[2][0] != 0) System.out.println("Premi 6 per costruire con il builder a SUD-OVEST");
            if (possibleBuildingsH[2][1] != 0) System.out.println("Premi 7 per costruire con il builder a SUD");
            if (possibleBuildingsH[2][2] != 0) System.out.println("Premi 8 per costruire con il builder a SUD-EST");

            int pressedButton;
            pressedButton = scannerIn.nextInt();
            scannerIn.nextLine();
            wrong = true;

            if (pressedButton == 1 && possibleBuildingsH[0][0] != 4 && possibleBuildingsH[0][0] != -1 )  {
                if(3 <= posBuilder[0] && posBuilder[0] <= 4 && 3 <= posBuilder[1] && posBuilder[1] <= 4 ){
                    direction = Direction.NORTH_WEST;
                    wrong = false;
                }
                else{
                    wrong = true;
                }

            } else if (pressedButton == 2 && possibleBuildingsH[0][1] != 4 && possibleBuildingsH[0][1] != -1 && possibleBuildingsH[0][1] != -2) {
                if(3 <= posBuilder[0] && posBuilder[0] <= 4 && 2 <= posBuilder[1] && posBuilder[1] <= 4 ){
                    direction = Direction.NORTH;
                    wrong = false;
                }
                else{
                    wrong = true;
                }
            } else if (pressedButton == 3 && possibleBuildingsH[0][2] != 0 && possibleBuildingsH[0][2] != -1 && possibleBuildingsH[0][2] != -2) {
                if(3 <= posBuilder[0] && posBuilder[0] <= 4 && 2 <= posBuilder[1] && posBuilder[1] <= 3 ){
                    direction = Direction.NORTH_EAST;
                    wrong = false;
                }
                else{
                    wrong = true;
                }
            } else if (pressedButton == 4 && possibleBuildingsH[1][0] != 4 && possibleBuildingsH[1][0] != -1 && possibleBuildingsH[1][0] != -2) {
                if(2 <= posBuilder[0] && posBuilder[0] <= 4 && 3 <= posBuilder[1] && posBuilder[1] <= 4 ){
                    direction = Direction.WEST;
                    wrong = false;
                }
                else{
                    wrong = true;
                }
            } else if (pressedButton == 5 && possibleBuildingsH[1][2] != 4 && possibleBuildingsH[1][2] != -1 && possibleBuildingsH[1][2] != -2) {
                if(2 <= posBuilder[0] && posBuilder[0] <= 4 && 2 <= posBuilder[1] && posBuilder[1] <= 3 ){
                    direction = Direction.EAST;
                    wrong = false;
                }
                else{
                    wrong = true;
                }
            } else if (pressedButton == 6 && possibleBuildingsH[2][0] != 4 && possibleBuildingsH[2][0] != -1 && possibleBuildingsH[1][2] != -2) {
                if(2 <= posBuilder[0] && posBuilder[0] <= 3 && 3 <= posBuilder[1] && posBuilder[1] <= 4 ){
                    direction = Direction.SOUTH_WEST;
                    wrong = false;
                }
                else{
                    wrong = true;
                }
            } else if (pressedButton == 7 && possibleBuildingsH[2][1] != 4 && possibleBuildingsH[2][1] != -1 && possibleBuildingsH[2][1] != -2) {
                if(2 <= posBuilder[0] && posBuilder[0] <= 3 && 2 <= posBuilder[1] && posBuilder[1] <= 4 ){
                    direction = Direction.SOUTH;
                    wrong = false;
                }
                else{
                    wrong = true;
                }
            } else if (pressedButton == 8 && possibleBuildingsH[2][2] != 4 && possibleBuildingsH[2][2] != -1 && possibleBuildingsH[2][2] != -2) {
                if(2 <= posBuilder[0] && posBuilder[0] <= 3 && 2 <= posBuilder[1] && posBuilder[1] <= 3 ){
                    direction = Direction.SOUTH_EAST;
                    wrong = false;
                }
                else{
                    wrong = true;
                }
            }
            if(wrong) System.out.println("hai selezionato una direzione per una cella sbagliata! ricorda non puoi costruire  su delle  cupole, sulla costa, o nella in una cella vicino alla costa ");
        } while(wrong);

        hestiaParamMessage.setDirection(direction);
        hestiaParamMessage.setPlayingBuilderSex(message.getCurrentPlayer().getPlayingBuilder().getGender());
        return hestiaParamMessage;
    }

    private MinotaurParamMessage displayMinotaurParamSel(MatchStateMessage message){
        MinotaurParamMessage minotaurParamMessage = new MinotaurParamMessage();
        //Inserire la stampa per la scelta del builder
        char playerBuilderGender = 'G';
        int[] posBuilder = new int[2];

        Builder chosenBuilderM = null;
        Direction direction = null;
        System.out.println("Seleziona il builder più adatto a servire Apollo. Ricorda che deve essere vicino ad un builder avversario affinché sia degno!");
        String choice = scannerIn.nextLine();
        choice = choice.toUpperCase();
        boolean wrong;
        do {
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
        } while (wrong);

        int[][] possibleSwap = Board.neighboringSwappingCell(chosenBuilderM, AccessType.OCCUPIED);

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
        //Mettere un display per far vedere le possibili scelte
        do {

            System.out.println("Ora è il momento di scegliere il builder avversario, premi il numero indicato per scegliere la direzione che preferisci");
            if (possibleSwap[0][0] != 0) System.out.println("Premi 1 per muoverti a NORD-OVEST");
            if (possibleSwap[0][1] != 0) System.out.println("Premi 2 per muoverti a NORD");
            if (possibleSwap[0][2] != 0) System.out.println("Premi 3 per muoverti a NORD-EST");
            if (possibleSwap[1][0] != 0) System.out.println("Premi 4 per muoverti a OVEST");
            if (possibleSwap[1][2] != 0) System.out.println("Premi 5 per muoverti a EST");
            if (possibleSwap[2][0] != 0) System.out.println("Premi 6 per muoverti a SUD-OVEST");
            if (possibleSwap[2][1] != 0) System.out.println("Premi 7 per muoverti a SUD");
            if (possibleSwap[2][2] != 0) System.out.println("Premi 8 per muoverti a SUD-EST");

            int pressedButton;
            pressedButton = scannerIn.nextInt();
            scannerIn.nextLine();
            wrong = true;

            if (pressedButton == 1 && possibleSwap[0][0] != 0) {
                if(message.getBoard()[posBuilder[0]-2][posBuilder[1]-2].getStatus() == AccessType.FREE ) {
                    direction = Direction.NORTH_WEST;
                    wrong = false;
                }
                else{
                    wrong = true;
                }
            }
            else if (pressedButton == 2 && possibleSwap[0][1] != 0) {
                if(message.getBoard()[posBuilder[0]-2][posBuilder[1]].getStatus() == AccessType.FREE ) {
                    direction = Direction.NORTH;
                    wrong = false;
                }
                else{
                    wrong = true;
                }
            }
            else if (pressedButton == 3 && possibleSwap[0][2] != 0) {
                if(message.getBoard()[posBuilder[0]-2][posBuilder[1]+2].getStatus() == AccessType.FREE ) {
                    direction = Direction.NORTH_EAST;
                    wrong = false;
                }
                else{
                    wrong = true;
                }
            }
            else if (pressedButton == 4 && possibleSwap[1][0] != 0) {
                if(message.getBoard()[posBuilder[0]][posBuilder[1]-2].getStatus() == AccessType.FREE ) {
                    direction = Direction.WEST;
                    wrong = false;
                }
                else{
                    wrong = true;
                }
            }
            else if (pressedButton == 5 && possibleSwap[1][2] != 0) {
                if(message.getBoard()[posBuilder[0]][posBuilder[1]+2].getStatus() == AccessType.FREE ) {
                    direction = Direction.EAST;
                    wrong = false;
                }
                else{
                    wrong = true;
                }
            }
            else if (pressedButton == 6 && possibleSwap[2][0] != 0) {
                if(message.getBoard()[posBuilder[0]+2][posBuilder[1]-2].getStatus() == AccessType.FREE ) {
                    direction = Direction.SOUTH_WEST;
                    wrong = false;
                }
                else{
                    wrong = true;
                }
            }
            else if (pressedButton == 7 && possibleSwap[2][1] != 0) {
                if(message.getBoard()[posBuilder[0]+2][posBuilder[1]].getStatus() == AccessType.FREE ) {
                    direction = Direction.SOUTH;
                    wrong = false;
                }
                else{
                    wrong = true;
                }
            }
            else if (pressedButton == 8 && possibleSwap[2][2] != 0) {
                if(message.getBoard()[posBuilder[0]+2][posBuilder[1]+2].getStatus() == AccessType.FREE ) {
                    direction = Direction.SOUTH_EAST;
                    wrong = false;
                }
                else{
                    wrong = true;
                }
            }
            if(wrong) System.out.println("hai selezionato una direzione sbaglitata! ricorda non puoi lanciare un builder avversario su una scogliera o su una cupola");
        } while(wrong);

        minotaurParamMessage.setOpponentBuilderDirection(direction);
        minotaurParamMessage.setPlayingBuilderSex(message.getCurrentPlayer().getPlayingBuilder().getGender());
        return minotaurParamMessage;

    }

    private PoseidonParamMessage displayPoseidonParamSel(MatchStateMessage message){
        PoseidonParamMessage poseidonParamMessage = new PoseidonParamMessage();

        //Ricerca del builder non mosso
        Builder constructionBuilder = null;
        char constructionBuilderSex = 'o';
        if (message.getCurrentPlayer().getPlayingBuilder() == message.getCurrentPlayer().getBuilderF()) {
            constructionBuilder = message.getCurrentPlayer().getBuilderM();
            constructionBuilderSex = 'M';
        }
        else {
            constructionBuilder = message.getCurrentPlayer().getBuilderF();
            constructionBuilderSex = 'F';
        }

        //Salvataggio della posizione del builder
        int[] posBuilder = new int[2];
        posBuilder[0] = constructionBuilder.getPosX();
        posBuilder[1] = constructionBuilder.getPosY();

        //Salvataggio della possible buildings
        int[][] possibleBuildingsP = constructionBuilder.getPossibleBuildings();

        //Mostrare la possible buildings

        //Creazione delle variabili per l'acquisizione di quante volte si vuole costruire
        System.out.println("Indicare il numero di volte che si vuole costruire grazie al potere di Poseidone. Inserire un numero compreso tra 1 e 3: ");
        int numeroBuild = 0;
        boolean wrong = true;
        do{
            numeroBuild = scannerIn.nextInt();
            scannerIn.nextLine();
            if( numeroBuild > 0 && numeroBuild < 4) {wrong = false;}
            if(wrong) System.out.println("hai selezionato un numero non compreso tra quelli indicati");
        }while (wrong);


        //Preparazione alla ricezione delle direzioni
        Direction[] directions = new Direction[3];

        int i = 0;

        System.out.printf("inserire ora un numero di direzioni pari a %d /n", numeroBuild);
        wrong = true;

        do {

            if (possibleBuildingsP[0][0] != 0 && possibleBuildingsP[0][0] != 4) System.out.println("Premi 1 per costruire con il builder a NORD-OVEST");
            if (possibleBuildingsP[0][1] != 0 && possibleBuildingsP[0][1] != 4) System.out.println("Premi 2 per costruire con il builder a NORD");
            if (possibleBuildingsP[0][2] != 0 && possibleBuildingsP[0][2] != 4) System.out.println("Premi 3 per costruire con il builder a NORD-EST");
            if (possibleBuildingsP[1][0] != 0 && possibleBuildingsP[1][0] != 4) System.out.println("Premi 4 per costruire con il builder a OVEST");
            if (possibleBuildingsP[1][2] != 0 && possibleBuildingsP[1][2] != 4) System.out.println("Premi 5 per costruire con il builder a EST");
            if (possibleBuildingsP[2][0] != 0 && possibleBuildingsP[2][0] != 4) System.out.println("Premi 6 per costruire con il builder a SUD-OVEST");
            if (possibleBuildingsP[2][1] != 0 && possibleBuildingsP[2][1] != 4) System.out.println("Premi 7 per costruire con il builder a SUD");
            if (possibleBuildingsP[2][2] != 0 && possibleBuildingsP[2][2] != 4) System.out.println("Premi 8 per costruire con il builder a SUD-EST");

            int[] pressedButton = new int[3];
            pressedButton[i] = scannerIn.nextInt();
            scannerIn.nextLine();
            wrong = true;

            if (pressedButton[i] == 1 && possibleBuildingsP[0][0] != -1 && possibleBuildingsP[0][0] != 4) {
                if( message.getBoard()[posBuilder[0]-2][posBuilder[1]-2].getStatus() == AccessType.FREE ) {
                    directions[i] = Direction.NORTH_WEST;
                    possibleBuildingsP[0][0] = possibleBuildingsP[0][0] + 1;
                    wrong = false;
                }
                else{
                    wrong = true;
                }
            }
            else if (pressedButton[i] == 2 && possibleBuildingsP[0][1] != -1 && possibleBuildingsP[0][1] != -4) {
                if(message.getBoard()[posBuilder[0]-2][posBuilder[1]].getStatus() == AccessType.FREE ) {
                    directions[i] = Direction.NORTH;
                    possibleBuildingsP[0][1] = possibleBuildingsP[0][1] + 1;
                    wrong = false;
                }
                else{
                    wrong = true;
                }
            }
            else if (pressedButton[i] == 3 && possibleBuildingsP[0][2] != -1 && possibleBuildingsP[0][2] != 4) {
                if(message.getBoard()[posBuilder[0]-2][posBuilder[1]+2].getStatus() == AccessType.FREE ) {
                    directions[i] = Direction.NORTH_EAST;
                    possibleBuildingsP[0][2] = possibleBuildingsP[0][2] + 1;
                    wrong = false;
                }
                else{
                    wrong = true;
                }
            }
            else if (pressedButton[i] == 4 && possibleBuildingsP[1][0] != -1 && possibleBuildingsP[1][0] != 4) {
                if(message.getBoard()[posBuilder[0]][posBuilder[1]-2].getStatus() == AccessType.FREE ) {
                    directions[i] = Direction.WEST;
                    possibleBuildingsP[1][0] = possibleBuildingsP[1][0] + 1;
                    wrong = false;
                }
                else{
                    wrong = true;
                }
            }
            else if (pressedButton[i] == 5 && possibleBuildingsP[1][2] != -1 && possibleBuildingsP[1][2] != 4) {
                if(message.getBoard()[posBuilder[0]][posBuilder[1]+2].getStatus() == AccessType.FREE ) {
                    directions[i] = Direction.EAST;
                    possibleBuildingsP[1][2] = possibleBuildingsP[1][2] +1;
                    wrong = false;
                }
                else{
                    wrong = true;
                }
            }
            else if (pressedButton[i] == 6 && possibleBuildingsP[2][0] != -1 && possibleBuildingsP[2][0] != 4) {
                if(message.getBoard()[posBuilder[0]+2][posBuilder[1]-2].getStatus() == AccessType.FREE ) {
                    directions[i] = Direction.SOUTH_WEST;
                    possibleBuildingsP[2][0] = possibleBuildingsP[2][0] +1;
                    wrong = false;
                }
                else{
                    wrong = true;
                }
            }
            else if (pressedButton[i] == 7 && possibleBuildingsP[2][1] != -1 && possibleBuildingsP[2][1] != 4) {
                if(message.getBoard()[posBuilder[0]+2][posBuilder[1]].getStatus() == AccessType.FREE ) {
                    directions[i] = Direction.SOUTH;
                    possibleBuildingsP[2][1] = possibleBuildingsP[2][1] + 1;
                    wrong = false;
                }
                else{
                    wrong = true;
                }
            }
            else if (pressedButton[i] == 8 && possibleBuildingsP[2][2] != -1 && possibleBuildingsP[2][2] != 4) {
                if(message.getBoard()[posBuilder[0]+2][posBuilder[1]+2].getStatus() == AccessType.FREE ) {
                    directions[i] = Direction.SOUTH_EAST;
                    possibleBuildingsP[2][2] = possibleBuildingsP[2][2] + 1;
                    wrong = false;
                }
                else{
                    wrong = true;
                }
            }
            if(wrong) System.out.println("hai selezionato una direzione sbaglitata! ricorda non puoi costruire qui");
            else { ++i;}
        } while(i < numeroBuild);

        poseidonParamMessage.setDirection1(directions[0]);
        poseidonParamMessage.setDirection2(directions[1]);
        poseidonParamMessage.setDirection3(directions[2]);
        poseidonParamMessage.setNumberOfBuild(numeroBuild);
        return poseidonParamMessage;
    }

    private PrometheusParamMessage displayPrometheusParamSel(MatchStateMessage message){
        PrometheusParamMessage prometheusParamMessage = new PrometheusParamMessage();
        Builder builderScelto = null;
        char builderSex = '0';
        int[] posBuilder = new int[2];
        Direction direction = null;



        
        return prometheusParamMessage;

    }

}
