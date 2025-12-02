package sk.uniba.fmph.dcs.terra_futura;

import org.json.JSONObject;
import org.junit.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;
import java.util.Optional;

import static org.junit.Assert.*;

public class SelectRewardTest {

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
    public void initiallyNoRewardPending() {
        SelectReward sr = new SelectReward();

        assertFalse(sr.player().isPresent());
        assertFalse(sr.canSelectReward(Resource.Green));

        JSONObject json = new JSONObject(sr.state());
        assertTrue(json.isNull("player"));
        assertEquals(0, json.getJSONArray("options").length());
    }

    @Test
    public void setRewardStoresPlayerAndOptions() {
        SelectReward sr = new SelectReward();

        int playerId = 7;
        Card card = newDummyCard();
        Resource[] options = {Resource.Green, Resource.Car};

        sr.setReward(playerId, card, options);

        Optional<Integer> p = sr.player();
        assertTrue(p.isPresent());
        assertEquals(playerId, p.get().intValue());

        assertTrue(sr.canSelectReward(Resource.Green));
        assertTrue(sr.canSelectReward(Resource.Car));
        assertFalse(sr.canSelectReward(Resource.Red));

        JSONObject json = new JSONObject(sr.state());
        assertEquals(playerId, json.getInt("player"));
        assertEquals(2, json.getJSONArray("options").length());
    }

    @Test(expected = IllegalStateException.class)
    public void cannotSetRewardTwiceWithoutSelection() {
        SelectReward sr = new SelectReward();

        sr.setReward(1, newDummyCard(), new Resource[]{Resource.Green});
        sr.setReward(2, newDummyCard(), new Resource[]{Resource.Car});
    }

    @Test
    public void selectRewardClearsState() {
        SelectReward sr = new SelectReward();

        sr.setReward(3, newDummyCard(),
                new Resource[]{Resource.Green, Resource.Car});

        assertTrue(sr.canSelectReward(Resource.Green));

        sr.selectReward(Resource.Green);

        assertFalse(sr.player().isPresent());
        assertFalse(sr.canSelectReward(Resource.Green));
        assertFalse(sr.canSelectReward(Resource.Car));

        JSONObject json = new JSONObject(sr.state());
        assertTrue(json.isNull("player"));
        assertEquals(0, json.getJSONArray("options").length());
    }

    @Test(expected = IllegalStateException.class)
    public void cannotSelectRewardIfNotAllowed() {
        SelectReward sr = new SelectReward();

        sr.setReward(3, newDummyCard(),
                new Resource[]{Resource.Green});

        sr.selectReward(Resource.Car);
    }
}
