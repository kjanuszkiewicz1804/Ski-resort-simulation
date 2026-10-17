package KolejkaZdarzeń;

import Zdarzenie.Zdarzenie;

/**
 * Oś czasu symulacji implementująca kolejkę priorytetową zdarzeń. Przechowuje
 * zdarzenia uporządkowane chronologicznie i pozwala na pobranie najwcześniejszego
 * z oczekujących zdarzeń.
 */
public interface KolejkaZdarzeń {
    boolean czyPusta();
    void wstawZdarzenie(Zdarzenie z);
    Zdarzenie pobierzPierwsze();
}
