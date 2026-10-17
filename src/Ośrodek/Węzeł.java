package Ośrodek;

import java.util.ArrayList;
import java.util.List;

/**
 * Klasa reprezentująca węzeł w ośrodku. Przechowuje dynamicznie powiększane
 * tablice połączeń z niego wychodzących i udostępnia je w postaci tablic o 
 * długości równej liczbie faktycznych połączeń (bez pustych pól).
 */
public class Węzeł {
    private List<Trasa> trasyWychodzące;

    private List<Wyciąg> wyciągiWychodzące;

    private int identyfikator;
    private int wysokośćNPM;
    private int współrzędnaX;
    private int współrzędnaY;

    private boolean czySkomunikowany;

    public Węzeł(int id, int wysokość, int x, int y, boolean s) {
        this.trasyWychodzące = new ArrayList<>();

        this.wyciągiWychodzące = new ArrayList<>();

        this.identyfikator = id;
        this.wysokośćNPM = wysokość;
        this.współrzędnaX = x;
        this.współrzędnaY = y;

        this.czySkomunikowany = s;
    }

    public boolean czySkomunikowany() {
        return this.czySkomunikowany;
    }

    public int pobierzIdentyfikator() {
        return this.identyfikator;
    }

    public void dodajTrasę(Trasa nowaTrasa) {
       this.trasyWychodzące.add(nowaTrasa);
    }

    public void dodajWyciąg(Wyciąg nowyWyciąg) {
        this.wyciągiWychodzące.add(nowyWyciąg);
    }

    public List<Trasa> pobierzTrasy() {
        return this.trasyWychodzące;
    }

    public List<Wyciąg> pobierzWyciągi() {
        return this.wyciągiWychodzące;
    }

    public List<Połączenie> pobierzPołączenia() {
        List<Połączenie> połączenia = new ArrayList<>();
        połączenia.addAll(this.trasyWychodzące);
        połączenia.addAll(this.wyciągiWychodzące);

        return połączenia;
    }

    public int pobierzX() {
        return this.współrzędnaX;
    }

    public int pobierzY() {
        return this.współrzędnaY;
    }
}
