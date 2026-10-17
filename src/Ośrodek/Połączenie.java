package Ośrodek;

import KolejkaZdarzeń.KolejkaZdarzeń;
import Sportowiec.Sportowiec;

/**
 * Abstrakcyjna klasa bazowa dla klas połączeń. Przechowuje dane jak węzły początkowy
 * i końcowy, id i czas przejazdu. Wymusza na klasach pochodnych implementację
 * metody skorzystaj określającej co się dzieje ze sportowcem, który chce użyć
 * danego połączenia.
 */
public abstract class Połączenie {
    private Węzeł węzełStartowy;
    private Węzeł węzełKońcowy;

    private int identyfikator;

    private int czasPrzejazdu;

    public Połączenie(int id, Węzeł start, Węzeł koniec, int czas) {
        this.identyfikator = id;
        this.węzełStartowy = start;
        this.węzełKońcowy = koniec;
        this.czasPrzejazdu = czas;
    }

    public Węzeł pobierzWęzełPoczątkowy() {
        return this.węzełStartowy;
    }

    public Węzeł pobierzWęzełKońcowy() {
        return this.węzełKońcowy;
    }

    public int pobierzCzasPrzejazdu() {
        return this.czasPrzejazdu;
    }

    public int pobierzIdentyfikator() {
        return this.identyfikator;
    }

    public abstract void skorzystaj(Sportowiec s, KolejkaZdarzeń kolejka, int czas);
}
