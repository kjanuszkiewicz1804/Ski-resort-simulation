package StrategiaWyboru;

import java.util.*;
import Ośrodek.*;
import PrzeszukiwaczGrafu.PrzeszukiwaczGrafu;
import Sportowiec.Sportowiec;

/**
 * Strategia starająca się zapewnić odwiedzenie jak największej liczby dostępnych tras.
 * W tym celu znajduje trasę odwiedzoną minimalną liczbę razy i układa plan
 * tak aby do niej dotrzeć.
 */
public class StrategiaKolekcjonera implements StrategiaWyboru {
    private final Queue<Połączenie> plan = new LinkedList<>();
    private final PrzeszukiwaczGrafu przeszukiwacz = new PrzeszukiwaczGrafu();

    @Override
    public Połączenie podejmijDecyzję(Sportowiec s) {
        if (!plan.isEmpty()) {
            return plan.poll();
        }

        if (s.czySpontanicznaDecyzja()) {
            return s.wybierzLosowePołączenie();
        }

        List<Trasa> wszystkieTrasy = s.pobierzOśrodek().pobierzTrasy();
        Węzeł start = s.pobierzAktualnąPozycję();
        
        Trasa wymarzonaTrasa = null;
        int minZjazdy = Integer.MAX_VALUE;
        int minOdległość = Integer.MAX_VALUE;
        double maxAtr = -Double.MAX_VALUE;

        for (Trasa t : wszystkieTrasy) {
            int zjazdy = s.pobierzLiczbęZjazdów(t);
            int odległość = przeszukiwacz.znajdźNajkrótsząŚcieżkę(start, t.pobierzWęzełPoczątkowy()).size();
            double atr = s.obliczAtrakcyjność(t);

            boolean lepsza = false;
            
            if (zjazdy < minZjazdy) { 
                lepsza = true; 
            } else if (zjazdy == minZjazdy) {
                if (odległość < minOdległość) { 
                    lepsza = true; 
                } else if (odległość == minOdległość) {
                    if (atr > maxAtr) { 
                        lepsza = true; 
                    }
                }
            }

            if (lepsza) {
                minZjazdy = zjazdy;
                minOdległość = odległość;
                maxAtr = atr;
                wymarzonaTrasa = t;
            }
        }

        List<Połączenie> ścieżkaDojazdu = przeszukiwacz.znajdźNajkrótsząŚcieżkę(start, wymarzonaTrasa.pobierzWęzełPoczątkowy());
        plan.addAll(ścieżkaDojazdu);
        plan.add(wymarzonaTrasa);

        return plan.poll();
    }
}