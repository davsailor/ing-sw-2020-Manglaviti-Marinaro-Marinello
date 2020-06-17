package it.polimi.ingsw2020.santorini.utils.messages.actions;

public class AskBuildSelectionMessage {
    private int[][] possibleBuildings;

    public AskBuildSelectionMessage(int[][] possibleBuildings) {
        this.possibleBuildings = possibleBuildings;
    }

    public int[][] getPossibleBuildings() {
        return possibleBuildings;
    }

    public int[][] getCells(){
        int[][] cells = new int[3][3];
        int j = 0;
        int k = 0;
        for(int i = 0; i < possibleBuildings.length; ++i){
            cells[j][k] = getPossibleBuildings()[j][k];
            if(k < 3) ++k;
            if(k == 3){
                ++i;
                k = 0;
            }
        }
        return cells;
    }

}
