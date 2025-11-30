package sk.uniba.fmph.dcs.terra_futura;

import org.json.JSONArray;
import org.json.JSONObject;
import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

public final class Grid implements InterfaceActivateGrid {
    private final Map<GridPosition, Card> cards = new HashMap<>();
    private final Set<GridPosition> activatedThisTurn = new HashSet<>();

    private final java.util.List<GridPosition> activationPattern =
            new ArrayList<>();

    public Optional<Card> getCard(final GridPosition coordinate) {
        return Optional.ofNullable(cards.get(coordinate));
    }

    public boolean canPutCard(final GridPosition coordinate) {
        return inside(coordinate) && !cards.containsKey(coordinate);
    }

    public void putCard(final GridPosition coordinate, final Card card) {
        if (!canPutCard(coordinate)) {
            throw new IllegalArgumentException("Cannot put card at " + coordinate);
        }
        cards.put(coordinate, card);
    }

    public boolean canBeActivated(final GridPosition coordinate) {
        return inside(coordinate)
                && cards.containsKey(coordinate)
                && !activatedThisTurn.contains(coordinate);
    }

    public void setActivated(final GridPosition coordinate) {
        if (!inside(coordinate)) {
            throw new IllegalArgumentException("Outside grid");
        }
        activatedThisTurn.add(coordinate);
    }

    @Override
    public void setActivationPattern(
            final Collection<SimpleEntry<Integer, Integer>> pattern) {
        activationPattern.clear();
        for (SimpleEntry<Integer, Integer> p : pattern) {
            activationPattern.add(new GridPosition(p.getKey(), p.getValue()));
        }
    }

    public void endTurn() {
        activatedThisTurn.clear();
    }

    public String state() {
        JSONObject json = new JSONObject();
        JSONArray cardsArr = new JSONArray();
        for (Map.Entry<GridPosition, Card> e : cards.entrySet()) {
            JSONObject c = new JSONObject();
            c.put("x", e.getKey().x());
            c.put("y", e.getKey().y());
            c.put("card", new JSONObject(e.getValue().state()));
            cardsArr.put(c);
        }
        json.put("cards", cardsArr);

        JSONArray activatedArr = new JSONArray();
        for (GridPosition pos : activatedThisTurn) {
            JSONObject p = new JSONObject();
            p.put("x", pos.x());
            p.put("y", pos.y());
            activatedArr.put(p);
        }
        json.put("activated", activatedArr);

        JSONArray patternArr = new JSONArray();
        for (GridPosition pos : activationPattern) {
            JSONObject p = new JSONObject();
            p.put("x", pos.x());
            p.put("y", pos.y());
            patternArr.put(p);
        }
        json.put("activationPattern", patternArr);
        return json.toString();
    }
    private boolean inside(final GridPosition p) {
        return p.x() >= -2 && p.x() <= 2 && p.y() >= -2 && p.y() <= 2;
    }
}
