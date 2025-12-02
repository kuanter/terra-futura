package sk.uniba.fmph.dcs.terra_futura;

public final class GridCoordinate {
    private final int x;
    private final int y;

    public GridCoordinate(final int x, final int y) {
        this.x = x;
        this.y = y;
    }

    public int x() {
        return x;
    }

    public int y() {
        return y;
    }
}
