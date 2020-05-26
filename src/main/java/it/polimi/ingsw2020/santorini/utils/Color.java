package it.polimi.ingsw2020.santorini.utils;

public enum Color {
    PLAYER_GREEN    ("\u001B[32m"),
    PLAYER_CYAN     ("\u001B[36m"),
    PLAYER_PURPLE   ("\u001B[35m"),
    OCEAN_BLUE      ("\u001B[34m"),
    MOUNTAIN_BROWN  ("\u001B[31m"),
    BORDER_YELLOW   ("\u001B[33m"),
    CORNER_WHITE    ("\u001B[37m"),
    RESET           ("\u001B[0m");

    private final String ansi;

    Color(String ansi){
        this.ansi = ansi;
    }

    public static Color getColor(int code){
        switch(code){
            case 1: return PLAYER_GREEN;
            case 0: return PLAYER_CYAN;
            case 2: return PLAYER_PURPLE;
            default: return null;
        }
    }
    

    @Override
    public String toString(){
        return ansi;
    }
}
