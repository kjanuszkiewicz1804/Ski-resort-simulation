package Zdarzenie;

import KolejkaZdarzeń.KolejkaZdarzeń;
import Ośrodek.Połączenie;
import Sportowiec.Sportowiec;

/**
 * Zdarzenie, które najpierw pyta sportowca o to, jakiego dokonał wyboru 
 * w danym węźle, a następnie wywołuje metodę skorzystaj dla wybranego połączenia.
 */
public class ZdarzenieWybór extends Zdarzenie {
    private Sportowiec sportowiec;
    private String wiadomośćPoPrzybyciu;

    public ZdarzenieWybór(int czas, Sportowiec s) {
        super(czas);
        this.sportowiec = s;
        this.wiadomośćPoPrzybyciu = null;
    }

    public ZdarzenieWybór(int czas, Sportowiec s, String wiadomość) {
        super(czas);
        this.sportowiec = s;
        this.wiadomośćPoPrzybyciu = wiadomość;
    }

    @Override
    public void wykonaj(KolejkaZdarzeń kolejka, int czas) {
        if (this.wiadomośćPoPrzybyciu != null) {
            this.sportowiec.zdajRaport(czas, this.wiadomośćPoPrzybyciu);
        }

        Połączenie wybrane = this.sportowiec.dokonajWyboru(czas);
        if (wybrane != null) {
            wybrane.skorzystaj(this.sportowiec, kolejka, czas);
        }
    }
}
