package Zdarzenie;

public class ParaZdarzenieNr {
    private final Zdarzenie zdarzenie;
    private final long np;

    public ParaZdarzenieNr(Zdarzenie z, long np) {
        this.zdarzenie = z;
        this.np = np;
    }

    public Zdarzenie pobierzZdarzenie() {
        return this.zdarzenie;
    }

    public long pobierzNP() {
        return this.np;
    }
}
