package it.polimi.ingsw2020.santorini.model;
import it.polimi.ingsw2020.santorini.exceptions.UnexpectedGodException;
import it.polimi.ingsw2020.santorini.model.gods.*;

public enum GodFactotum {
    // order of god, divided into simple gods (codes 0-8) and advanced gods (9-13)
    APOLLO      (0),
    ARTEMIS     (1),
    ATHENA      (2),
    ATLAS       (3),
    DEMETER     (4),
    HEPHAESTUS  (5),
    MINOTAUR    (6),
    PAN         (7),
    PROMETHEUS  (8),
    ARES        (9),
    HESTIA      (10),
    PERSEPHONE  (11),
    POSEIDON    (12),
    ZEUS        (13);

    private final int code;
    GodFactotum(int code)
    {
        this.code = code;
    }

    /**
     * getter of code
     * @return the code of the specific god
     */
    public int getCode() {
        return this.code;
    }

    /**
     * getter of the name of the god in base of its code
     * @return the name of the god
     * @throws UnexpectedGodException: the code does not correspond to any god
     */
    public String getName() throws UnexpectedGodException{
        switch(this.code){
            case 0:
                return Apollo.class.getSimpleName();
            case 1:
                return Artemis.class.getSimpleName();
            case 2:
                return Athena.class.getSimpleName();
            case 3:
                return Atlas.class.getSimpleName();
            case 4:
                return Demeter.class.getSimpleName();
            case 5:
                return Hephaestus.class.getSimpleName();
            case 6:
                return Minotaur.class.getSimpleName();
            case 7:
                return Pan.class.getSimpleName();
            case 8:
                return Prometheus.class.getSimpleName();
            case 9:
                return Ares.class.getSimpleName();
            case 10:
                return Hestia.class.getSimpleName();
            case 11:
                return Chronus.class.getSimpleName();
            case 12:
                return Poseidon.class.getSimpleName();
            case 13:
                return Zeus.class.getSimpleName();
            default:
                throw new UnexpectedGodException();
        }
    }

    /**
     * method that creates god's object that will be assigned to the player
     * @return the god chosen, using its code
     * @throws UnexpectedGodException if the god does not exist
     */
    public GodCard summon() throws UnexpectedGodException{
        switch(this.code){
            case 0:
                return new Apollo();
            case 1:
                return new Artemis();
            case 2:
                return new Athena();
            case 3:
                return new Atlas();
            case 4:
                return new Demeter();
            case 5:
                return new Hephaestus();
            case 6:
                return new Minotaur();
            case 7:
                return new Pan();
            case 8:
                return new Prometheus();
            case 9:
                return new Ares();
            case 10:
                return new Hestia();
            case 11:
                return new Chronus();
            case 12:
                return new Poseidon();
            case 13:
                return new Zeus();
            default:
                throw new UnexpectedGodException();
        }
    }
}
