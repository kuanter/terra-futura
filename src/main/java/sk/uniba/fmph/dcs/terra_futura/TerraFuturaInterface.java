package sk.uniba.fmph.dcs.terra_futura;

import java.util.List;
import java.util.AbstractMap.SimpleEntry;

public interface TerraFuturaInterface {
    boolean takeCard(int playerId,
                     CardSource source,
                     GridPosition destination);

    boolean discardLastCardFromDeck(int playerId, Deck deck);
    boolean activateCard(
            int playerId,
            GridCoordinate card,
            List<SimpleEntry<Resource, GridPosition>> inputs,
            List<SimpleEntry<Resource, GridPosition>> outputs,
            List<GridPosition> pollution,
            Integer otherPlayerId,
            GridPosition otherCard
    );

    void selectReward(int playerId, Resource resource);
    boolean turnFinished(int playerId);
    boolean selectActivationPattern(int playerId, int cardIndex);
    boolean selectScoring(int playerId, int cardIndex);
    String state();
}