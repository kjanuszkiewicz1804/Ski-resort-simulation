package Zdarzenie;

import java.util.List;

import KolejkaZdarzeń.KolejkaZdarzeń;
import Ośrodek.Wyciąg;
import Sportowiec.Sportowiec;

/** 
 * Zdarzenie cykliczne symulujące odjazd wyciągu.
 * Przemieszcza grupę sportowców do węzła końcowego i wywołuje ZdarzenieWybór
 * dla każdego z nich. Wyciąg zabiera tylu sportowców ile może zgodnie z
 * ograniczeniem grupy, lub wszystkich oczekujących, jeśli jest ich mniej.
 * Na koniec umieszcza swoją kolejną iterację na osi czasu, o ile zakończy się 
 * ona przed godziną zamknięcia ośrodka - 16:00
 */
public class ZdarzenieOdjazdWyciągu extends Zdarzenie{
    private Wyciąg wyciąg;

    public ZdarzenieOdjazdWyciągu(int czas, Wyciąg wyciąg) {
        super(czas);
        this.wyciąg = wyciąg;
    }

    @Override
    public void wykonaj(KolejkaZdarzeń kolejka, int czas) {
        if (czas >= ((15 - 9) * 3600)) {
            return;
        }

        List<Sportowiec> grupa = this.wyciąg.zabierzGrupę(czas); 
        int przejazd = this.wyciąg.pobierzCzasPrzejazdu();

        for (Sportowiec pasażer : grupa) {
            pasażer.zdajRaport(czas, "rozpoczął wjazd wyciągiem nr " + this.wyciąg.pobierzIdentyfikator() + ".");
            
            pasażer.dodajDoHistoriiŚledzenia(this.wyciąg);

            pasażer.przemieść(this.wyciąg.pobierzWęzełKońcowy());

            String raportPoWjeździe = "zszedł z wyciągu nr" + this.wyciąg.pobierzIdentyfikator() + ".";
            ZdarzenieWybór nowyWybór = new ZdarzenieWybór(czas + przejazd, pasażer, raportPoWjeździe);
            
            kolejka.wstawZdarzenie(nowyWybór);
        }

        int oczekiwanie = this.wyciąg.pobierzCzasOczekiwania();
        if (czas + oczekiwanie + przejazd <= ((16 - 9) * 3600)){
            ZdarzenieOdjazdWyciągu następnyCykl = new ZdarzenieOdjazdWyciągu(czas + oczekiwanie, this.wyciąg);
            kolejka.wstawZdarzenie(następnyCykl);
        }
    }

}
