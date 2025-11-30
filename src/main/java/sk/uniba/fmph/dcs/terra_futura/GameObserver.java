package sk.uniba.fmph.dcs.terra_futura;

import java.util.HashMap;
import java.util.Map;

public class GameObserver {

    private final Map<Integer, TerraFuturaObserverInterface> observers;

    public GameObserver(Map<Integer, TerraFuturaObserverInterface> observers) {
        this.observers = new HashMap<>(observers);
    }

    public void notifyAll(Map<Integer, String> newState) {
        for (Map.Entry<Integer, String> entry : newState.entrySet()) {
            int playerId = entry.getKey();
            String stateForPlayer = entry.getValue();

            TerraFuturaObserverInterface observer = observers.get(playerId);
            if (observer != null) {
                observer.notify(stateForPlayer);
            }
        }
    }
}
