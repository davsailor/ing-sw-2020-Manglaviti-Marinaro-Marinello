package it.polimi.ingsw2020.santorini.model;
import it.polimi.ingsw2020.santorini.exceptions.UnexpectedGodException;
import it.polimi.ingsw2020.santorini.model.gods.*;

import java.lang.reflect.Constructor;

public enum GodNameType {
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
    GodNameType(int code)
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
                return Persephone.class.getSimpleName();
            case 12:
                return Poseidon.class.getSimpleName();
            case 13:
                return Zeus.class.getSimpleName();
            default:
                throw new UnexpectedGodException();
        }
    }

    /**
     * getter of the class' constructor defined by god's code
     * @return the correct constructor
     * @throws UnexpectedGodException: the code does not correspond to any god
     * @throws NoSuchMethodException: constructor not found
     */
    public Constructor getConstructor() throws UnexpectedGodException, NoSuchMethodException {
        switch(this.code){
            case 0:
                return Apollo.class.getConstructor();
            case 1:
                return Artemis.class.getConstructor();
            case 2:
                return Athena.class.getConstructor();
            case 3:
                return Atlas.class.getConstructor();
            case 4:
                return Demeter.class.getConstructor();
            case 5:
                return Hephaestus.class.getConstructor();
            case 6:
                return Minotaur.class.getConstructor();
            case 7:
                return Pan.class.getConstructor();
            case 8:
                return Prometheus.class.getConstructor();
            case 9:
                return Ares.class.getConstructor();
            case 10:
                return Hestia.class.getConstructor();
            case 11:
                return Persephone.class.getConstructor();
            case 12:
                return Poseidon.class.getConstructor();
            case 13:
                return Zeus.class.getConstructor();
            default:
                throw new UnexpectedGodException();
        }
    }
}
