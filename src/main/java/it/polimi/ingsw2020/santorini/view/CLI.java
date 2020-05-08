package it.polimi.ingsw2020.santorini.view;

import it.polimi.ingsw2020.santorini.model.Cell;
import it.polimi.ingsw2020.santorini.model.Player;
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
     * @param turnPlayerMessage
     */
    @Override
    public void displaySelectionBuilderWindow(TurnPlayerMessage turnPlayerMessage) {
        String currentPlayer = turnPlayerMessage.getCurrentPlayer().getNickname();
        if(client.getUsername().equals(currentPlayer)) {
            int[] builderM, builderF;
            builderM = new int[2];
            builderF = new int[2];
            System.out.printf("\n%s, tocca a te! Dovrai inserire le coordinate di due celle per posizionare i tuoi costruttori!\n", currentPlayer);
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
                displayStartTurn(updateMessage);
                break;
            case STANDBY_PHASE_1:
                displaySP(updateMessage, PhaseType.STANDBY_PHASE_1);
                break;
            case MOVE_PHASE:
                displayMoveUpdate(updateMessage);
                break;
            case STANDBY_PHASE_2:
                displaySP(updateMessage, PhaseType.STANDBY_PHASE_2);
                break;
            case BUILD_PHASE:
                displayBuildUpdate(updateMessage);
                break;
            case STANDBY_PHASE_3:
                displaySP(updateMessage, PhaseType.STANDBY_PHASE_3);
                break;
            case END_TURN:
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
        if(client.getUsername().equals(message.getPlayers().get(message.getCurrentPlayerIndex()).getNickname())) {
            System.out.println(message.getPlayers().get(message.getCurrentPlayerIndex()).getNickname() + " tocca a te!");
            Message nextPhase = new Message(client.getUsername());
            nextPhase.buildNextPhaseMessage(new NextPhaseMessage(client.getUsername(), PhaseType.START_TURN));
            client.getNetworkHandler().send(nextPhase);
        } else {
            System.out.println("Ora è il turno di " + message.getPlayers().get(message.getCurrentPlayerIndex()).getNickname());
        }
    }

    @Override
    public void displaySP(UpdateMessage updateMessage, PhaseType phase) {
        System.out.printf(updateMessage.getPlayers().get(updateMessage.getCurrentPlayerIndex()).getDivinePower().getName());
        if(updateMessage.getPlayers().get(updateMessage.getCurrentPlayerIndex()).getNickname().equals(client.getUsername()))
            System.out.println(" ha accettato la tua richiesta di aiuto");
        else
            System.out.println(" ha aiutato " + updateMessage.getPlayers().get(updateMessage.getCurrentPlayerIndex()).getNickname());
        showBoard(updateMessage.getBoard());
        Message nextPhase = new Message(client.getUsername());
        nextPhase.buildNextPhaseMessage(new NextPhaseMessage(client.getUsername(), phase));
        client.getNetworkHandler().send(nextPhase);
    }

    @Override
    public void displayMoveUpdate(UpdateMessage updateMessage) {
        // si dice cosa è successo, e si mostra la board. oppure si mostra solo la board
        Message nextPhase = new Message(client.getUsername());
        nextPhase.buildNextPhaseMessage(new NextPhaseMessage(client.getUsername(), null));
        client.getNetworkHandler().send(nextPhase);
    }

    @Override
    public void displayBuildUpdate(UpdateMessage updateMessage) {
        // si dice cosa è successo, e si mostra la board. oppure si mostra solo la board
        Message nextPhase = new Message(client.getUsername());
        nextPhase.buildNextPhaseMessage(new NextPhaseMessage(client.getUsername(), null));
        client.getNetworkHandler().send(nextPhase);
    }

    //TODO: USARE IL NUOVO METODO STATIC DI BOARD PER RESTRINGERE IL CAMPO DELLE SCELTE DELLE MOSSE O DELLE COSTRUZIONI
    @Override
    public void displayParametersSelection(String god) {
        Message answer = new Message(client.getUsername());
        System.out.println(god + " è qui ad aiutarti!");
        switch(god){
            case "Apollo":
                ApolloParamMessage apolloParamMessage = new ApolloParamMessage();
                int[] yourBuilder = new int[2];
                int[] opponentBuilder = new int[2];
                System.out.println("Inserisci le coordinate del costruttore che vuoi muovere. Attento, dovrà essere vicino ad un avversario e la cella deve essere accessibile!");
                System.out.printf("Inserisci la riga: ");
                yourBuilder[0] = scannerIn.nextInt();
                System.out.printf("Inserisci la colonna: ");
                yourBuilder[1] = scannerIn.nextInt();
                System.out.println("Inserisci le coordinate del costruttore avversario di cui voui usurpare la cella");
                System.out.printf("Inserisci la riga: ");
                opponentBuilder[0] = scannerIn.nextInt();
                System.out.printf("Inserisci la colonna: ");
                opponentBuilder[1] = scannerIn.nextInt();
                apolloParamMessage.setYourBuilder(yourBuilder);
                apolloParamMessage.setOpponentBuilder(opponentBuilder);
                answer.buildApolloParamMessage(apolloParamMessage);
                break;
            case "Ares":
                AresParamMessage aresParamMessage = new AresParamMessage();
                int[] targetedBlock = new int[2];
                System.out.println("Inserisci le coordinate di una qualsiasi cella che abbia almeno un edificio (massimo livello 3)");
                System.out.printf("Inserisci la riga: ");
                targetedBlock[0] = scannerIn.nextInt();
                System.out.printf("Inserisci la colonna: ");
                targetedBlock[1] = scannerIn.nextInt();
                aresParamMessage.setTargetedBlock(targetedBlock);
                answer.buildAresParamMessage(aresParamMessage);
                break;
            case "Artemis":
                ArtemisParamMessage artemisParamMessage = new ArtemisParamMessage();
                int[] position = new int[2];
                System.out.println("Inserisci le coordinate della prossima cella accessibile che il tuo costruttore vuole occupare (attenzione, il tuo costruttore non vuole tornare nella cella precedente!");
                System.out.printf("Inserisci la riga: ");
                position[0] = scannerIn.nextInt();
                System.out.printf("Inserisci la colonna: ");
                position[1] = scannerIn.nextInt();
                artemisParamMessage.setPosition(position);
                answer.buildArtemisParamMessage(artemisParamMessage);
                break;
            case "Atlas":
                AtlasParamMessage atlasParamMessage = new AtlasParamMessage();
                int[] targetCell = new int[2];
                System.out.println("inserisci le coordinate della cella in cui vuoi costruire una DOME dove già non c'è");
                System.out.printf("Inserisci la riga: ");
                targetCell[0] = scannerIn.nextInt();
                System.out.printf("Inserisci la colonna: ");
                targetCell[1] = scannerIn.nextInt();
                atlasParamMessage.setTargetCell(targetCell);
                answer.buildAtlasParamMessage(atlasParamMessage);
                break;
            case "Demeter":
                DemeterParamMessage demeterParamMessage = new DemeterParamMessage();
                int[] targetedCell = new int[2];
                System.out.println("Inserisci le coorinate della cella in cui vuoi che il tuo costruttore costruisca di nuovo. Attenzione, il tuo builder non vuole costruire nella stessa cella!");
                System.out.printf("Inserisci la riga: ");
                targetedCell[0] = scannerIn.nextInt();
                System.out.printf("Inserisci la colonna: ");
                targetedCell[1] = scannerIn.nextInt();
                demeterParamMessage.setTargetedCell(targetedCell);
                answer.buildDemeterParamMessage(demeterParamMessage);
                break;
            case "Hestia":
                HestiaParamMessage hestiaParamMessage = new HestiaParamMessage();
                int[] hestiaTarget = new int[2];
                System.out.println("Inserisci le coordinate della cella in cui vuoi che il tuo costruttore costruisca di nuovo. Attenzione, il tuo costruttore soffre momentaneamente di vertigini, quindi non potrà costruire vicino alla costa");
                System.out.printf("Inserisci la riga: ");
                hestiaTarget[0] = scannerIn.nextInt();
                System.out.printf("Inserisci la colonna: ");
                hestiaTarget[1] = scannerIn.nextInt();
                hestiaParamMessage.setTargetedCell(hestiaTarget);
                answer.buildHestiaParamMessage(hestiaParamMessage);
                break;
            case "Minotaur":
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
                answer.buildMinotaurParamMessage(minotaurParamMessage);
                break;
            case "Poseidon":
                PoseidonParamMessage poseidonParamMessage = new PoseidonParamMessage();
                int[] poseidonTarget = new int[2];
                System.out.println("Inserisci le coordinate della cella in cui vuoi che il tuo costruttore rimasto fermo costruisca");
                System.out.printf("Inserisci la riga: ");
                poseidonTarget[0] = scannerIn.nextInt();
                System.out.printf("Inserisci la colonna: ");
                poseidonTarget[1] = scannerIn.nextInt();
                poseidonParamMessage.setTargetedCell(poseidonTarget);
                answer.buildPoseidonParamMessage(poseidonParamMessage);
                break;
            case "Prometheus":
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
                answer.buildPrometheusParamMessage(prometheusParamMessage);
                break;
            default:
                break;
        }
        client.getNetworkHandler().send(answer);
    }

    @Override
    public void displayChooseBuilder(TurnPlayerMessage message) {
        showBoard(message.getCells());
        System.out.println("Quale builder vuoi muovere? Il maschio o la femmina?");
        System.out.println("Premi il tasto M per selezionare il maschio, F per la femmina");
        Message answer = new Message(client.getUsername());
        String choice = scannerIn.nextLine();
        choice = choice.toUpperCase();
        boolean wrong = false;
        do {
            if (choice.equals("M"))
                answer.buildSelectedBuilderMessage(new SelectedBuilderMessage('M'));
            else if (choice.equals("F"))
                answer.buildSelectedBuilderMessage(new SelectedBuilderMessage('F'));
            else
                wrong = true;
        } while(wrong);
        client.getNetworkHandler().send(answer);
    }

    /**
     * prova
     *
     * @param updateMessage parameter
     */
    @Override
    public void displayEndTurn(UpdateMessage updateMessage) {

    }

    @Override
    public void displayWouldActivate(ActivationRequestInfoMessage question) {
        // richiediamo se il giocatore vuole attivare il potere divino
        if(client.getUsername().equals(question.getCurrentPlayer())) {
            System.out.println("Vuoi richiedere l'intervento di " + question.getGod() +"? Y-N");
            String answer = scannerIn.nextLine();
            Message message = new Message(client.getUsername());
            if (answer.equals("Y"))
                message.buildActivateGodMessage(new ActivateGodMessage(true));
            else
                message.buildActivateGodMessage(new ActivateGodMessage(false));
            client.getNetworkHandler().send(message);
        }

        /*
        String choice = scannerIn.nextLine();
        choice = choice.toUpperCase();
        boolean wrong = false;
        do {
            if (choice.equals("M"))
                answer.buildSelectedBuilderMessage(new SelectedBuilderMessage('M'));
            else if (choice.equals("F"))
                answer.buildSelectedBuilderMessage(new SelectedBuilderMessage('F'));
            else
                wrong = true;
        } while(wrong);
         */
        // creiamo un message DO - ACTIVATE_GOD in ogni caso
        // il payload avrà un boolean che rispecchierà la scelta del giocatore
        // in questo modo standardizziamo il pattern della comunicazione
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

}
