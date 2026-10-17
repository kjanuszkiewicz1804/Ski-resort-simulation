package Zdarzenie;

import KolejkaZdarzeń.KolejkaZdarzeń;

/**
 * Abstrakcyjna klasa bazowa dla klas zdarzeń. Przechowuje czas wystąpienia
 * i wymusza na klasach pochodnych implementację metody wykonaj, która 
 * określa zachowanie zdarzenia na osi czasu.
 */
public abstract class Zdarzenie {
    private int czasZdarzenia;

    public Zdarzenie(int czas) {
        this.czasZdarzenia = czas;
    }
    public abstract void wykonaj(KolejkaZdarzeń kolejka, int czas);

    public int pobierzCzas() {
        return this.czasZdarzenia;
    }
}
