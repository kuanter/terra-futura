package sk.uniba.fmph.dcs.terra_futura;

import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;
import java.util.AbstractMap.SimpleEntry;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;

public class GridTest {

    private Card newDummyCard() {
        try {
            Class<Card> clazz = Card.class;

            for (Constructor<?> c : clazz.getDeclaredConstructors()) {
                if (!Modifier.isPublic(c.getModifiers())) {
                    continue;
                }
                Class<?>[] types = c.getParameterTypes();
                Object[] args = new Object[types.length];
                for (int i = 0; i < types.length; i++) {
                    Class<?> t = types[i];
                    if (!t.isPrimitive()) {
                        args[i] = null;
                    } else if (t == boolean.class) {
                        args[i] = false;
                    } else if (t == int.class) {
                        args[i] = 0;
                    } else if (t == long.class) {
                        args[i] = 0L;
                    } else if (t == double.class) {
                        args[i] = 0.0;
                    } else if (t == float.class) {
                        args[i] = 0.0f;
                    } else if (t == short.class) {
                        args[i] = (short) 0;
                    } else if (t == byte.class) {
                        args[i] = (byte) 0;
                    } else if (t == char.class) {
                        args[i] = '\0';
                    } else {
                        args[i] = 0;
                    }
                }
                return (Card) c.newInstance(args);
            }

            Constructor<Card> noArg = clazz.getDeclaredConstructor();
            noArg.setAccessible(true);
            return noArg.newInstance();
        } catch (Exception e) {
            throw new RuntimeException("Cannot create Card for tests", e);
        }
    }

    @Test
    public void canPutCardInsideAndNotOutside() {
        Grid grid = new Grid();

        GridPosition inside = new GridPosition(0, 0);
        GridPosition outside = new GridPosition(3, 0);

        assertTrue(grid.canPutCard(inside));
        assertFalse(grid.canPutCard(outside));
    }

    @Test
    public void putCardStoresAndBlocksPosition() {
        Grid grid = new Grid();
        GridPosition pos = new GridPosition(1, -1);
        Card card = newDummyCard();

        assertTrue(grid.canPutCard(pos));

        grid.putCard(pos, card);

        Optional<Card> fromGrid = grid.getCard(pos);
        assertTrue(fromGrid.isPresent());
        assertSame(card, fromGrid.get());

        assertFalse(grid.canPutCard(pos));
    }

    @Test(expected = IllegalArgumentException.class)
    public void putCardThrowsWhenCannotPut() {
        Grid grid = new Grid();
        GridPosition pos = new GridPosition(0, 0);
        Card c1 = newDummyCard();
        Card c2 = newDummyCard();

        grid.putCard(pos, c1);
        grid.putCard(pos, c2);
    }

    @Test
    public void canBeActivatedOnlyIfInsideHasCardAndNotActivated() {
        Grid grid = new Grid();
        GridPosition pos = new GridPosition(0, 0);
        GridPosition outside = new GridPosition(5, 0);

        Card card = newDummyCard();
        grid.putCard(pos, card);

        assertTrue(grid.canBeActivated(pos));
        assertFalse(grid.canBeActivated(outside));
    }

    @Test
    public void setActivatedPreventsSecondActivation() {
        Grid grid = new Grid();
        GridPosition pos = new GridPosition(0, 0);
        Card card = newDummyCard();
        grid.putCard(pos, card);

        assertTrue(grid.canBeActivated(pos));

        grid.setActivated(pos);

        assertFalse(grid.canBeActivated(pos));
    }

    @Test(expected = IllegalArgumentException.class)
    public void setActivatedOutsideThrows() {
        Grid grid = new Grid();
        GridPosition outside = new GridPosition(5, 5);

        grid.setActivated(outside);
    }

    @Test
    public void endTurnClearsActivated() {
        Grid grid = new Grid();
        GridPosition pos = new GridPosition(0, 0);
        Card card = newDummyCard();
        grid.putCard(pos, card);

        grid.setActivated(pos);
        assertFalse(grid.canBeActivated(pos));

        grid.endTurn();
        assertTrue(grid.canBeActivated(pos));
    }

    @Test
    public void activationPatternIsStoredViaInterface() {
        Grid grid = new Grid();

        List<SimpleEntry<Integer, Integer>> pattern =
                List.of(
                        new SimpleEntry<>(0, 0),
                        new SimpleEntry<>(1, -1)
                );

        grid.setActivationPattern(pattern);

        JSONObject json = new JSONObject(grid.state());
        JSONArray arr = json.getJSONArray("activationPattern");

        assertEquals(2, arr.length());

        JSONObject p0 = arr.getJSONObject(0);
        JSONObject p1 = arr.getJSONObject(1);

        assertEquals(0, p0.getInt("x"));
        assertEquals(0, p0.getInt("y"));
        assertEquals(1, p1.getInt("x"));
        assertEquals(-1, p1.getInt("y"));
    }

    @Test
    public void stateContainsCardsAndActivated() {
        Grid grid = new Grid();

        GridPosition pos1 = new GridPosition(0, 0);
        GridPosition pos2 = new GridPosition(1, 1);
        Card c1 = newDummyCard();
        Card c2 = newDummyCard();

        grid.putCard(pos1, c1);
        grid.putCard(pos2, c2);
        grid.setActivated(pos2);

        JSONObject json = new JSONObject(grid.state());

        JSONArray cardsArr = json.getJSONArray("cards");
        JSONArray activatedArr = json.getJSONArray("activated");

        assertEquals(2, cardsArr.length());
        assertEquals(1, activatedArr.length());
    }
}
