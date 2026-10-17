package Ośrodek;

import Sportowiec.Sportowiec;
import Zdarzenie.ZdarzenieWybór;
import KolejkaZdarzeń.KolejkaZdarzeń;

/**
 * Klasa reprezentująca trasę w ośrodku. Metoda skorzystaj przemieszcza sportowca 
 * do węzła końcowego i wywołuje zdarzenie wyboru po zakończeniu zjazdu.
 */
public class Trasa extends Połączenie {
    private int poziomTrudności;
    private double bazowaAtrakcyjność;
    private double odpornośćNaNierówności;
    private int ilośćZjazdów;

    public Trasa(int id, Węzeł start, Węzeł koniec, int czas, int trud, double atrakcyjność, double nierówności) {
        super(id, start, koniec, czas);
        this.poziomTrudności = trud;
        this.bazowaAtrakcyjność = atrakcyjność;
        this.odpornośćNaNierówności = nierówności;
        this.ilośćZjazdów = 0;
    }

    public int pobierzPoziomTrudności() {
        return this.poziomTrudności;
    }

    public int pobierzIlośćZjazdów() {
        return this.ilośćZjazdów;
    }

    public double pobierzBazowąAtrakcyjność() {
        return this.bazowaAtrakcyjność;
    }

    public double pobierzOdpornośćNaNierówności() {
        return this.odpornośćNaNierówności;
    }

    public double pobierzWyrównanie() {
        double wyrównanie = this.bazowaAtrakcyjność
                + (1 - this.bazowaAtrakcyjność) * Math.pow(this.odpornośćNaNierówności, this.ilośćZjazdów);
        return wyrównanie;
    }

    public void dodajZjazd() {
        this.ilośćZjazdów++;
    }

    @Override
    public void skorzystaj(Sportowiec s, KolejkaZdarzeń kolejka, int czas) {
        s.zdajRaport(czas, "rozpoczął zjazd trasą nr " + this.pobierzIdentyfikator() + ".");

        this.dodajZjazd();

        s.zarejestrujZjazdTrasą(this);

        s.dodajDoHistoriiŚledzenia(this);

        s.przemieść(this.pobierzWęzełKońcowy());

        String raportPoZjeździe = "zakończył zjazd trasą nr " + this.pobierzIdentyfikator() + ".";
        ZdarzenieWybór nowyWybór = new ZdarzenieWybór(czas + this.pobierzCzasPrzejazdu(), s, raportPoZjeździe);

        kolejka.wstawZdarzenie(nowyWybór);
    }

}
