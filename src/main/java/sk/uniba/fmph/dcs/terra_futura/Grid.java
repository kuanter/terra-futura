package sk.uniba.fmph.dcs.terra_futura;

import java.util.List;
import java.util.Optional;

public class Grid {

    public Optional<Card> getCard(GridPosition coordinate) {
        throw new UnsupportedOperationException("Not implemented");
    }

    public boolean canPutCard(GridPosition coordinate) {
        throw new UnsupportedOperationException("Not implemented");
    }

    public void putCard(GridPosition coordinate, Card card) {
        throw new UnsupportedOperationException("Not implemented");
    }

    public boolean canBeActivated(GridPosition coordinate) {
        throw new UnsupportedOperationException("Not implemented");
    }

    public void setActivated(GridPosition coordinate) {
        throw new UnsupportedOperationException("Not implemented");
    }

    public void setActivationPattern(List<GridPosition> pattern) {
        throw new UnsupportedOperationException("Not implemented");
    }

    public void endTurn() {
        throw new UnsupportedOperationException("Not implemented");
    }

    public String state() {
        throw new UnsupportedOperationException("Not implemented");
    }
}
