package sk.uniba.fmph.dcs.terra_futura;

public enum Deck {
    I(0),
    II(1);
    private final int deckIndex;
    Deck(int deckIndex){
        this.deckIndex = deckIndex;
    }
    public int getIndex(){
        return deckIndex;
    }
}
