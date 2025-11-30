package sk.uniba.fmph.dcs.terra_futura;

import java.util.List;

public class Card {

    public Resource[] resources;
    public int pollutionSpacesL; // как в UML (pollutionSpacesL int)

    public boolean canGetResources(List<Resource> resources) {
        throw new UnsupportedOperationException("Not implemented");
    }

    public void getResources(List<Resource> resources) {
        throw new UnsupportedOperationException("Not implemented");
    }

    public boolean canPutResources(List<Resource> resources) {
        throw new UnsupportedOperationException("Not implemented");
    }

    public void putResources(List<Resource> resources) {
        throw new UnsupportedOperationException("Not implemented");
    }

    public boolean check(List<Resource> input, List<Resource> output, int polution) {
        throw new UnsupportedOperationException("Not implemented");
    }

    public boolean checkLower(List<Resource> input, List<Resource> output, int polution) {
        throw new UnsupportedOperationException("Not implemented");
    }

    public boolean hasAssistance() {
        throw new UnsupportedOperationException("Not implemented");
    }

    public String state() {
        throw new UnsupportedOperationException("Not implemented");
    }
}

