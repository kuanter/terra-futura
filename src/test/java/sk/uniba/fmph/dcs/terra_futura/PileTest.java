package sk.uniba.fmph.dcs.terra_futura;

import org.json.JSONObject;
import org.junit.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;

public class PileTest {

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

    private Pile newPileWithHidden(int hiddenCount) {
        List<Card> visible = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            visible.add(newDummyCard());
        }

        List<Card> hidden = new ArrayList<>();
        for (int i = 0; i < hiddenCount; i++) {
            hidden.add(newDummyCard());
        }

        return new Pile(visible, hidden, 42L);
    }

    @Test
    public void initialStateCountsAreCorrect() {
        Pile pile = newPileWithHidden(3);

        JSONObject json = new JSONObject(pile.state());
        assertEquals(4, json.getInt("visible_count"));
        assertEquals(3, json.getInt("hidden_count"));
        assertEquals(0, json.getInt("discard_count"));
        assertEquals(4, json.getJSONArray("visible_cards").length());
    }

    @Test
    public void getCardValidAndInvalidIndices() {
        Pile pile = newPileWithHidden(0);

        for (int i = 1; i <= 4; i++) {
            Optional<Card> c = pile.getCard(i);
            assertTrue("index " + i + " should be present", c.isPresent());
        }

        assertFalse(pile.getCard(0).isPresent());
        assertFalse(pile.getCard(5).isPresent());
        assertFalse(pile.getCard(-1).isPresent());
    }

    @Test
    public void takeCardUsesHiddenToRefillIfAvailable() {
        Pile pile = newPileWithHidden(2);

        JSONObject before = new JSONObject(pile.state());
        int visibleBefore = before.getInt("visible_count");
        int hiddenBefore = before.getInt("hidden_count");
        int discardBefore = before.getInt("discard_count");

        pile.takeCard(2);

        JSONObject after = new JSONObject(pile.state());
        int visibleAfter = after.getInt("visible_count");
        int hiddenAfter = after.getInt("hidden_count");
        int discardAfter = after.getInt("discard_count");

        assertEquals(visibleBefore, visibleAfter);
        assertEquals(hiddenBefore - 1, hiddenAfter);
        assertEquals(discardBefore, discardAfter);
    }

    @Test
    public void takeCardWithoutHiddenReducesVisibleCount() {
        Pile pile = newPileWithHidden(0);

        JSONObject before = new JSONObject(pile.state());
        int visibleBefore = before.getInt("visible_count");

        pile.takeCard(2);

        JSONObject after = new JSONObject(pile.state());
        int visibleAfter = after.getInt("visible_count");

        assertEquals(visibleBefore - 1, visibleAfter);
    }

    @Test
    public void removeLastCardMovesOldestToDiscardAndUsesHidden() {
        Pile pile = newPileWithHidden(1);

        JSONObject before = new JSONObject(pile.state());
        int visibleBefore = before.getInt("visible_count");
        int hiddenBefore = before.getInt("hidden_count");
        int discardBefore = before.getInt("discard_count");

        pile.removeLastCard();

        JSONObject after = new JSONObject(pile.state());
        int visibleAfter = after.getInt("visible_count");
        int hiddenAfter = after.getInt("hidden_count");
        int discardAfter = after.getInt("discard_count");

        assertEquals(visibleBefore, visibleAfter);
        assertEquals(hiddenBefore - 1, hiddenAfter);
        assertEquals(discardBefore + 1, discardAfter);
    }

    @Test(expected = IllegalStateException.class)
    public void removeLastCardWithoutHiddenOrDiscardFails() {
        Pile pile = newPileWithHidden(0);
        pile.removeLastCard();
    }
}
