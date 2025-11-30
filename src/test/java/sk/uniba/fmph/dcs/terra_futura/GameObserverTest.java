package sk.uniba.fmph.dcs.terra_futura;

import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

class FakeObserver implements TerraFuturaObserverInterface {
    String lastState = null;
    int notifyCount = 0;

    @Override
    public void notify(String gameState) {
        this.lastState = gameState;
        this.notifyCount++;
    }
}

public class GameObserverTest {

    private FakeObserver observer1;
    private FakeObserver observer2;
    private GameObserver gameObserver;

    @Before
    public void setUp() {
        observer1 = new FakeObserver();
        observer2 = new FakeObserver();

        Map<Integer, TerraFuturaObserverInterface> observers = new HashMap<>();
        observers.put(1, observer1);
        observers.put(2, observer2);

        gameObserver = new GameObserver(observers);
    }

    @Test
    public void testNotifyAllForwardsStateToEachObserver() {
        Map<Integer, String> newState = new HashMap<>();
        newState.put(1, "state-for-player-1");
        newState.put(2, "state-for-player-2");

        gameObserver.notifyAll(newState);

        assertEquals("state-for-player-1", observer1.lastState);
        assertEquals(1, observer1.notifyCount);

        assertEquals("state-for-player-2", observer2.lastState);
        assertEquals(1, observer2.notifyCount);
    }

    @Test
    public void testNotifyAllIgnoresPlayersWithoutObserver() {
        Map<Integer, String> newState = new HashMap<>();
        newState.put(1, "state-for-player-1");
        newState.put(99, "state-for-nonexistent-player");

        gameObserver.notifyAll(newState);

        assertEquals("state-for-player-1", observer1.lastState);
        assertEquals(1, observer1.notifyCount);

        assertNull(observer2.lastState);
        assertEquals(0, observer2.notifyCount);
    }
}
