package PrzeszukiwaczGrafu;

import java.util.*;

import Ośrodek.Połączenie;
import Ośrodek.Węzeł;

/**
 * Odpowiada za wyszukiwanie najkrótszej ścieżki pomiędzy węzłami w grafie ośrodka.
 * Implementuje algorytm BFS.
 */
public class PrzeszukiwaczGrafu {

    public List<Połączenie> znajdźNajkrótsząŚcieżkę (Węzeł start, Węzeł cel) {
        Queue<Węzeł> kolejka = new LinkedList<>();
        Set<Węzeł> odwiedzone = new HashSet<>();

        Map<Węzeł, Połączenie> historiaŚcieżki = new HashMap<>();

        kolejka.add(start);
        odwiedzone.add(start);

        while (!kolejka.isEmpty()) {
            Węzeł obecny = kolejka.poll();
        
            if (obecny.equals(cel)) {
                return odtwórzŚcieżkę(start, cel, historiaŚcieżki);
            }

            List<Połączenie> połączenia = obecny.pobierzPołączenia();

            for(Połączenie v : połączenia) {
                Węzeł sąsiad = v.pobierzWęzełKońcowy();

                if (!odwiedzone.contains(sąsiad)) {
                    odwiedzone.add(sąsiad);
                    historiaŚcieżki.put(sąsiad, v);
                    kolejka.add(sąsiad);
                }
            }
        }

        return new ArrayList<>();
    }

    private List<Połączenie> odtwórzŚcieżkę(Węzeł start, Węzeł cel, Map<Węzeł, Połączenie> historiaŚcieżki) {
        List<Połączenie> ścieżka = new ArrayList<>();
        Węzeł krok = cel;

        while (!krok.equals(start)) {
            Połączenie krawędźWejściowa = historiaŚcieżki.get(krok);
            ścieżka.add(krawędźWejściowa);
            krok = krawędźWejściowa.pobierzWęzełPoczątkowy(); 
        }

        Collections.reverse(ścieżka);
        return ścieżka;
    }

}
