package KolejkaZdarzeń;

import Zdarzenie.Zdarzenie;
import Zdarzenie.ParaZdarzenieNr;

import java.util.Comparator;
import java.util.PriorityQueue;
public class BibliotecznaKolejkaZdarzeń implements KolejkaZdarzeń{
    private final PriorityQueue<ParaZdarzenieNr> kolejka;
    private long licznik = 0;

    public BibliotecznaKolejkaZdarzeń() {
        Comparator<ParaZdarzenieNr> komparator = (z1,z2) -> {
            int czas1 = z1.pobierzZdarzenie().pobierzCzas();
            int czas2 = z2.pobierzZdarzenie().pobierzCzas();

            if (czas1 != czas2) {
                return Integer.compare(czas1, czas2);
            }

            return Long.compare(z1.pobierzNP(), z2.pobierzNP());
        };
        this.kolejka = new PriorityQueue<>(komparator);
    }

    @Override
    public boolean czyPusta() {
        return this.kolejka.isEmpty();
    }

    @Override
    public void wstawZdarzenie(Zdarzenie z) {
        ParaZdarzenieNr para = new ParaZdarzenieNr(z, licznik++); 
        this.kolejka.add(para);
    }

    @Override
    public Zdarzenie pobierzPierwsze() {
        if (this.czyPusta()) {
            throw new IllegalStateException("Próba pobrania zdarzenia z pustej kolejki");
        }
        return this.kolejka.poll().pobierzZdarzenie();
    }

}
