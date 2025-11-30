package sk.uniba.fmph.dcs.terra_futura;

import java.util.List;
import java.util.Optional;

public class ScoringMethod {

    public List<Resource> resources;
    public Points pointsPerCombination;
    public Optional<Points> calculatedTotal;

    public void selectThisMethodAndCalculate() {
        throw new UnsupportedOperationException("Not implemented");
    }

    public String state() {
        throw new UnsupportedOperationException("Not implemented");
    }
}

