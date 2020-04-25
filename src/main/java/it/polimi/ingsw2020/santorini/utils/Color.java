package it.polimi.ingsw2020.santorini.utils;

public enum Color {
    GREEN(0),
    ORANGE(1),
    PURPLE(2);

    private final int code;

    Color(int code){
        this.code = code;
    }

    public static Color getColor(int code){
        switch(code){
            case 0: return GREEN;
            case 1: return ORANGE;
            case 2: return PURPLE;
            default: return null;
        }
    }
}
