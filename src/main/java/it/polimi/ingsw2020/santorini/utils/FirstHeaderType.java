package it.polimi.ingsw2020.santorini.utils;

public enum FirstHeaderType {
    // server per una prima classificazione dei messaggi
    SETUP,
    LOADING,
    SYNCHRONIZATION,    // client richiede la sincronizzazione in un punto della partita (usata principalmente per l'inizio del macth)
    VERIFY,             // client richiede al server di verificare la correttezza di alcuni dati
    ERROR,              // segnalare al client che sono stati inseriti dati inconsistenti
    ASK,                // Server che chiede al Client informazioni per fare un'operazione
    DO;                 // Client che chiede al Server di fare un'operazione
}
