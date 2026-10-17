package WizualizatorOśrodka;

import Ośrodek.*;
import Sportowiec.Sportowiec;
import kadra.mapki.GeneratorMapek;
import kadra.mapki.pliki.WyjatekSystemuPlikow;
import kadra.mapki.styl.GruboscKonturu;
import kadra.mapki.styl.StylKrawedzi;
import kadra.mapki.styl.StylLinii;
import kadra.mapki.styl.StylWezla;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Odpowiada za generowanie reprezentacji graficznej ośrodka na podstawie 
 * danych z symulacji. Tworzy pliki wynikowe z mapą i statystykami.
 */
public class WizualizatorOśrodka {
    private final GeneratorMapek generator;
    private final OśrodekNarciarski ośrodek;
    private final List<Sportowiec> śledzeniSportowcy;

    public WizualizatorOśrodka(String sciezkaKatalogu, OśrodekNarciarski ośrodek, List<Sportowiec> śledzeni) throws WyjatekSystemuPlikow {
        this.generator = new GeneratorMapek(sciezkaKatalogu);
        this.ośrodek = ośrodek;
        this.śledzeniSportowcy = śledzeni;
    }

    public void generujWszystkieMapki() throws WyjatekSystemuPlikow {
        generujMapkęParametrów();
        generujMapkęStatystyk();
        generujMapkiSportowców();
    }

    private void narysujWęzły() {
        for (Węzeł w : ośrodek.pobierzWęzły()) {
            this.generator.dodajWezel(
                w.pobierzIdentyfikator(), 
                w.pobierzX(), 
                w.pobierzY(), 
                new StylWezla(GruboscKonturu.POGRUBIONY)
            );
        }
    }

    private void generujMapkęParametrów() throws WyjatekSystemuPlikow {
        this.generator.zeruj();
        narysujWęzły();

        for (Trasa t : ośrodek.pobierzTrasy()) {
            ArrayList<String> tekst = new ArrayList<>();
            tekst.add(String.format("t%d: poziom: %d, czas: %ds", t.pobierzIdentyfikator(), t.pobierzPoziomTrudności(), t.pobierzCzasPrzejazdu()));
            tekst.add(String.format("odporność: %.2f, %.5f", t.pobierzBazowąAtrakcyjność(), t.pobierzOdpornośćNaNierówności()));
            
            generator.dodajKrawedz(t.pobierzWęzełPoczątkowy().pobierzIdentyfikator(), t.pobierzWęzełKońcowy().pobierzIdentyfikator(), new StylKrawedzi(StylLinii.CIAGLA), tekst);
        }

        for (Wyciąg w : ośrodek.pobierzWyciągi()) {
            ArrayList<String> tekst = new ArrayList<>();
            tekst.add(String.format("w%d: %d os. co %ds", w.pobierzIdentyfikator(), w.pobierzRozmiarGrupy(), w.pobierzCzasOczekiwania()));
            tekst.add(String.format("czas: %ds", w.pobierzCzasPrzejazdu()));
            
            generator.dodajKrawedz(w.pobierzWęzełPoczątkowy().pobierzIdentyfikator(), w.pobierzWęzełKońcowy().pobierzIdentyfikator(), new StylKrawedzi(StylLinii.PRZERYWANA), tekst);
        }

        this.generator.tworzMapke("01_mapka_parametry.tex");
    }

    private void generujMapkęStatystyk() throws WyjatekSystemuPlikow {
        this.generator.zeruj();
        narysujWęzły();

        for (Trasa t : ośrodek.pobierzTrasy()) {
            ArrayList<String> tekst = new ArrayList<>();
            tekst.add(String.format("t%d: śnieg: %.2f", t.pobierzIdentyfikator(), t.pobierzWyrównanie()));
            tekst.add(String.format("zjazdy: %d", t.pobierzIlośćZjazdów()));
            
            generator.dodajKrawedz(t.pobierzWęzełPoczątkowy().pobierzIdentyfikator(), t.pobierzWęzełKońcowy().pobierzIdentyfikator(), new StylKrawedzi(StylLinii.CIAGLA), tekst);
        }

        for (Wyciąg w : ośrodek.pobierzWyciągi()) {
            ArrayList<String> tekst = new ArrayList<>();
            int maxPojemność = w.pobierzMaksymalnąPrzepustowość();
            int procent = maxPojemność > 0 ? (int) Math.round(((double) w.pobierzIlośćPrzejazdów() / maxPojemność) * 100) : 0;
            
            tekst.add(String.format("w%d: kol: %d(śr), %d(maks)", w.pobierzIdentyfikator(), w.pobierzŚredniąKolejkę(), w.pobierzMaksymalnąKolejkę()));
            tekst.add(String.format("wjazdy: %d / %d (%d%%)", w.pobierzIlośćPrzejazdów(), maxPojemność, procent));
            
            generator.dodajKrawedz(w.pobierzWęzełPoczątkowy().pobierzIdentyfikator(), w.pobierzWęzełKońcowy().pobierzIdentyfikator(), new StylKrawedzi(StylLinii.PRZERYWANA), tekst);
        }

        this.generator.tworzMapke("02_mapka_statystyki.tex");
    }

    private void generujMapkiSportowców() throws WyjatekSystemuPlikow {
        for (Sportowiec s : śledzeniSportowcy) {
            this.generator.zeruj();
            narysujWęzły();

            Map<Połączenie, List<Integer>> historia = s.pobierzHistorięŚledzenia();
            
            for (Map.Entry<Połączenie, List<Integer>> wpis : historia.entrySet()) {
                Połączenie p = wpis.getKey();
                List<Integer> przejazdy = wpis.getValue();
                
                String prefiks = (p instanceof Trasa) ? "t" : "w";
                StylLinii stylLinii = (p instanceof Trasa) ? StylLinii.CIAGLA : StylLinii.PRZERYWANA;
                
                StringBuilder tekstB = new StringBuilder();
                tekstB.append(prefiks).append(p.pobierzIdentyfikator())
                      .append("(").append(przejazdy.size()).append("): ");
                
                for (int i = 0; i < przejazdy.size(); i++) {
                    tekstB.append(przejazdy.get(i));
                    if (i < przejazdy.size() - 1) {
                        tekstB.append(",");
                    }
                }

                this.generator.dodajKrawedz(
                    p.pobierzWęzełPoczątkowy().pobierzIdentyfikator(), 
                    p.pobierzWęzełKońcowy().pobierzIdentyfikator(), 
                    new StylKrawedzi(stylLinii), 
                    tekstB.toString()
                );
            }

            this.generator.tworzMapke("03_mapka_sportowiec_" + s.pobierzIdentyfikator() + ".tex");
        }
    }
}
