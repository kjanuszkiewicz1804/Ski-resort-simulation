package Ośrodek;

import KolejkaZdarzeń.KolejkaZdarzeń;
import Zdarzenie.Zdarzenie;
import java.util.*;

/**
 * Klasa reprezentująca ośrodek. Przechowuje wszystkie węzły i połączenia 
 * znajdujące się w ośrodku, oraz oś czasu symulacji . Odpowiada za
 *  przeprowadzenie symulacji i stworzenie końcowego raportu.
 */
public class OśrodekNarciarski {
    private List<Węzeł> węzły;
    private List<Wyciąg> wyciągi;
    private List<Trasa> trasy;

    private KolejkaZdarzeń kolejka;

    public OśrodekNarciarski(List<Węzeł> węzły, List<Wyciąg> wyciągi, List<Trasa> trasy, KolejkaZdarzeń kolejka) {
        this.węzły = węzły;
        this.wyciągi = wyciągi;
        this.trasy = trasy;
        this.kolejka = kolejka;
    }

    public List<Trasa> pobierzTrasy() {
        return this.trasy;
    }

    public List<Węzeł> pobierzWęzły() {
        return this.węzły;
    }

    public List<Wyciąg> pobierzWyciągi() {
        return this.wyciągi;
    }

    public void uruchomSymulację() {
        while (!this.kolejka.czyPusta()) {
            Zdarzenie doObsłużenia = this.kolejka.pobierzPierwsze();
            doObsłużenia.wykonaj(this.kolejka, doObsłużenia.pobierzCzas());
        }
        this.generujRaportKońcowy();
    }

    public void generujRaportKońcowy() {
        for (Trasa t : this.trasy) {
            System.out.printf(java.util.Locale.US, "Trasa %d: zjazdy %d, śnieg %.2f\n",
                    t.pobierzIdentyfikator(),
                    t.pobierzIlośćZjazdów(),
                    t.pobierzWyrównanie()
            );
        }

        for (Wyciąg w : this.wyciągi) {
            int wjazdy = w.pobierzIlośćPrzejazdów();
            int maksWjazdy = w.pobierzMaksymalnąPrzepustowość();
            
            int procent = (int) Math.round((double) wjazdy / maksWjazdy * 100);

            System.out.printf("Wyciąg %d: kol %d(śr), %d(maks), wjazdy %d / %d (%d%%)\n",
                    w.pobierzIdentyfikator(),
                    w.pobierzŚredniąKolejkę(),
                    w.pobierzMaksymalnąKolejkę(),
                    wjazdy,
                    maksWjazdy,
                    procent
            );
        }
    }
}