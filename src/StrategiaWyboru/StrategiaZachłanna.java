package StrategiaWyboru;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import Ośrodek.Połączenie;
import Ośrodek.Trasa;
import Ośrodek.Węzeł;
import PrzeszukiwaczGrafu.PrzeszukiwaczGrafu;
import Sportowiec.Sportowiec;

/**
 * Strategia wyszukująca najatrakcyjniejszą trasę w ośrodku. Układa plan tak,
 * aby do niej dotrzeć.
 */
public class StrategiaZachłanna implements StrategiaWyboru{
    private Queue<Połączenie> plan = new LinkedList<>();
    private PrzeszukiwaczGrafu przeszukiwacz = new PrzeszukiwaczGrafu();

    @Override
    public Połączenie podejmijDecyzję(Sportowiec s) {
        if (!plan.isEmpty()) {
            return plan.poll();
        }

        if (s.czySpontanicznaDecyzja()) {
            return s.wybierzLosowePołączenie();
        }

        List<Trasa> wszystkieTrasy = s.pobierzOśrodek().pobierzTrasy();
        Trasa wymarzonaTrasa = null;
        double maxAtr = -Double.MAX_VALUE;

        for (Trasa t : wszystkieTrasy) {
            double atr = s.obliczAtrakcyjność(t);
            if (atr > maxAtr) {
                maxAtr = atr;
                wymarzonaTrasa = t;
            }
        }

        Węzeł start = s.pobierzAktualnąPozycję();
        Węzeł cel = wymarzonaTrasa.pobierzWęzełPoczątkowy();
        
        List<Połączenie> ścieżkaDojazdu = przeszukiwacz.znajdźNajkrótsząŚcieżkę(start, cel);
        
        plan.addAll(ścieżkaDojazdu);
        plan.add(wymarzonaTrasa);

        return plan.poll();
    }

}
