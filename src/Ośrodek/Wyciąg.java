package Ośrodek;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import KolejkaZdarzeń.KolejkaZdarzeń;
import Sportowiec.Sportowiec;

/**
 * Klasa reprezentująca wyciąg w ośrodku, która trzyma kolejkę sportowców oczekujących
 * na przejazd i w odpowiednim momencie tworzy z nich grupę do obsłużenia.
 */
public class Wyciąg extends Połączenie{
    private int rozmiarGrupy;
    private int czasOczekiwania;
    private Queue<Sportowiec> kolejka = new LinkedList<>();
    private int ilośćPrzejazdów;

    private int liczbaOdjazdów;             
    
    private int obecnaDługośćKolejki = 0;
    private int maksymalnaDługośćKolejki = 0;
    
    private long sumaDlugosciKolejki = 0;    
    private int czasOstatniejZmianyKolejki = 0;

    private final int CZAS_ZAMKNIĘCIA = (16 - 9) * 3600;

    public Wyciąg(int id, Węzeł start, Węzeł koniec, int czas, int rozmiar, int oczekiwanie) {
        super(id, start, koniec, czas);
        this.rozmiarGrupy = rozmiar;
        this.czasOczekiwania = oczekiwanie;
    }

    public int pobierzRozmiarGrupy() {
        return this.rozmiarGrupy;
    }

    public int pobierzCzasOczekiwania() {
        return this.czasOczekiwania;
    }

    public int pobierzIlośćPrzejazdów() {
        return this.ilośćPrzejazdów;
    }

    public List<Sportowiec> zabierzGrupę(int czas) {
        aktualizujStatystykiKolejki(czas);
        this.liczbaOdjazdów++;

        List<Sportowiec> grupa = new ArrayList<>();

        while (grupa.size() < this.rozmiarGrupy && !this.kolejka.isEmpty()) {
            grupa.add(this.kolejka.poll());
            this.obecnaDługośćKolejki--;
        }
        this.ilośćPrzejazdów += grupa.size();
        return grupa;
    }

    private void aktualizujStatystykiKolejki(int aktualnyCzas) {
        int roznicaCzasu = aktualnyCzas - this.czasOstatniejZmianyKolejki;
        if (roznicaCzasu > 0) {
            this.sumaDlugosciKolejki += (long) roznicaCzasu * this.obecnaDługośćKolejki;
            this.czasOstatniejZmianyKolejki = aktualnyCzas;
        }
    }

    @Override
    public void skorzystaj(Sportowiec s, KolejkaZdarzeń kolejka, int czas) {
        aktualizujStatystykiKolejki(czas);
        
        this.obecnaDługośćKolejki++;
        if (this.obecnaDługośćKolejki > this.maksymalnaDługośćKolejki) {
            this.maksymalnaDługośćKolejki = this.obecnaDługośćKolejki;
        }

        s.zdajRaport(czas, "ustawił się w kolejce do wyciągu nr " + this.pobierzIdentyfikator() + ".");

        this.kolejka.offer(s);
    }

    public int pobierzMaksymalnąKolejkę() { return this.maksymalnaDługośćKolejki; }
    public int pobierzMaksymalnąPojemność() { return this.liczbaOdjazdów * this.rozmiarGrupy; }

    public int pobierzŚredniąKolejkę() {
        long ostatecznaSuma = this.sumaDlugosciKolejki + ((long) (CZAS_ZAMKNIĘCIA - this.czasOstatniejZmianyKolejki) * this.kolejka.size());
       
        return (int) Math.round((double) ostatecznaSuma / CZAS_ZAMKNIĘCIA);
    }

    public int pobierzMaksymalnąPrzepustowość() {
        int ileOdjazdów = 0;
        int t = 0;
        
        while (t < ((15 - 9) * 3600)) {
            ileOdjazdów++;
            t += this.czasOczekiwania;
            
            if (t + this.pobierzCzasPrzejazdu() > ((16 - 9) * 3600)) {
                break;
            }
        }
        return ileOdjazdów * this.rozmiarGrupy;
    }
}
