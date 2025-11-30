package sk.uniba.fmph.dcs.terra_futura;

import java.util.List;
import java.util.Optional;

public class SelectReward {

    public Optional<Integer> player;         // Optional[int]
    public List<Resource> selelction;        // да, в UML там опечатка selelction

    public void setReward(int player, Card card, Resource[] reward) {
        throw new UnsupportedOperationException("Not implemented");
    }

    public boolean canSelectReward(Resource resource) {
        throw new UnsupportedOperationException("Not implemented");
    }

    public void selectReward(Resource resource) {
        throw new UnsupportedOperationException("Not implemented");
    }

    public String state() {
        throw new UnsupportedOperationException("Not implemented");
    }
}

