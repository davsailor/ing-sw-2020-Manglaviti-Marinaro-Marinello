package it.polimi.ingsw2020.santorini.model;

public enum GodNameType {
    // order of god, divided into simple gods (codes 0-8) and advanced gods (9-13)
    APOLLO      (0, "Apollo"),
    ARTEMIS     (1, "Artemis"),
    ATHENA      (2, "Athena"),
    ATLAS       (3, "Atlas"),
    DEMETER     (4, "Demeter"),
    HEPHAESTUS  (5, "Hephaestus"),
    MINOTAUR    (6, "Minotaur"),
    PAN         (7, "Pan"),
    PROMETHEUS  (8, "Prometheus"),
    ARES        (9, "Ares"),
    HESTIA      (10, "Hestia"),
    PERSEPHONE  (11, "Persephone"),
    POSEIDON    (12, "Poseidon"),
    ZEUS        (13, "Zeus");

    private final int code;
    private final String name;
    GodNameType(int code, String name)
    {
        this.code = code;
        this.name = name;
    }

    // getter of codes and names
    public int getCode(){ return this.code; }
    public String getName() { return this.name; }
}
