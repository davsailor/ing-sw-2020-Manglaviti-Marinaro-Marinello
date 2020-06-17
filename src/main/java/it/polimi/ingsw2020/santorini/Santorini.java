package it.polimi.ingsw2020.santorini;

import it.polimi.ingsw2020.santorini.network.client.Client;
import it.polimi.ingsw2020.santorini.network.server.Server;
import java.util.Arrays;

public class Santorini {
    public static void main(String[] args) {
        if(args.length == 0) {
            Client.main(args);
        } else {
            if(args[0].equals("-server"))
                Server.main(args);
            else if(args[0].equals("-client")){
                if(args.length == 1)
                    Client.main(new String[0]);
                else
                    Client.main(Arrays.copyOfRange(args, 1, args.length));
            }
        }
    }
}
