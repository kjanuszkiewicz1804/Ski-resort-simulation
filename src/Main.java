/*
Projekt: Ośrodek Narciarski
Autor: Kamil Januszkiewicz
Projekt realizuje symulację jednego dnia funkcjonowania ośrodka narciarskiego.
Symulacja modeluje zachowanie sportowców, podejmujących decyzje dotyczące
wyboru kolejnego połączenia, na podstawie wielu czynników. Ponadto program 
pozwala na śledzenie wybranych sportowców i sporządza statystykę podsumowującą
sumaryczne liczby przejazdów na wszystkich połączeniach w ośrodku.
*/
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;
import java.util.Random;

import KolejkaZdarzeń.FabrykaKolejek;
import KolejkaZdarzeń.KolejkaZdarzeń;
import Ośrodek.OśrodekNarciarski;
import Ośrodek.Trasa;
import Ośrodek.Wyciąg;
import Ośrodek.Węzeł;
import Sportowiec.Sportowiec;
import StrategiaWyboru.StrategiaKolekcjonera;
import StrategiaWyboru.StrategiaLokalna;
import StrategiaWyboru.StrategiaWyboru;
import StrategiaWyboru.StrategiaZachłanna;
import Zdarzenie.ZdarzenieOdjazdWyciągu;
import Zdarzenie.ZdarzenieWybór;
import WizualizatorOśrodka.WizualizatorOśrodka;
import kadra.mapki.pliki.WyjatekSystemuPlikow;

/**
 * Główna klasa symulacji. Odpowiada za wczytanie danych, utworzenie
 * odpowiednich
 * obiektów oraz utworznie osi czasu i wywołania metody uruchomienia symulacji
 * od utworzonego ośrodka.
 */
public class Main {

    private static void pomińPusteWiersze(Scanner skaner) {
        skaner.nextLine();
    }

    private static int konwersjaCzasu(String czas) {
        String[] części = czas.split(":");
        int godziny = Integer.parseInt(części[0]);
        int minuty = Integer.parseInt(części[1]);
        int sekundy = Integer.parseInt(części[2]);
        return (godziny * 3600) + (minuty * 60) + sekundy - (9 * 3600);
    }

    public static void main(String[] args) {

        if (args.length == 0) {
            System.err.println("Błąd: Nie podano ścieżki do katalogu na mapki jako argumentu programu.");
            return;
        }

        String sciezkaKatalogu = args[0];

        try {
            Scanner skaner = new Scanner(System.in);

            KolejkaZdarzeń ośCzasu = FabrykaKolejek.stwórzKolejkę();

            Węzeł[] węzły = wczytajWęzły(skaner);
            Wyciąg[] wyciągi = wczytajWyciągi(skaner, węzły, ośCzasu);
            Trasa[] trasy = wczytajTrasy(skaner, węzły);

            OśrodekNarciarski ośrodek = new OśrodekNarciarski(
                    Arrays.asList(węzły),
                    Arrays.asList(wyciągi),
                    Arrays.asList(trasy),
                    ośCzasu);

            List<Sportowiec> śledzeniSportowcy = wczytajSportowców(skaner, węzły, ośCzasu, ośrodek);

            skaner.close();

            ośrodek.uruchomSymulację();

            System.out.println("Generowanie mapek do katalogu: " + sciezkaKatalogu);
            WizualizatorOśrodka wizualizator = new WizualizatorOśrodka(sciezkaKatalogu, ośrodek, śledzeniSportowcy);
            wizualizator.generujWszystkieMapki();
            System.out.println("Symulacja zakończona sukcesem!");

        } catch (WyjatekSystemuPlikow e) {
            System.err.println(
                    "Błąd generowania mapek. Upewnij się, że podałeś poprawną ścieżkę do katalogu oraz że masz odpowiednie uprawnienia do zapisu w tym miejscu.");
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println(
                    "Wystąpił nieoczekiwany błąd programu. Prosimy zgłosić ten błąd zespołowi deweloperskiemu.");
            e.printStackTrace();
        }
    }

    private static Węzeł[] wczytajWęzły(Scanner skaner) {
        int liczbaWęzłów = Integer.parseInt(skaner.nextLine().trim());
        Węzeł[] węzły = new Węzeł[liczbaWęzłów];

        for (int i = 0; i < liczbaWęzłów; i++) {
            Scanner skanerWiersza = new Scanner(skaner.nextLine());
            skanerWiersza.useLocale(Locale.ENGLISH);
            int wysokość = skanerWiersza.nextInt();
            int x = skanerWiersza.nextInt();
            int y = skanerWiersza.nextInt();
            boolean skomunikowany = (skanerWiersza.hasNext() && skanerWiersza.next().equals("s"));
            węzły[i] = new Węzeł(i, wysokość, x, y, skomunikowany);
            skanerWiersza.close();
        }
        pomińPusteWiersze(skaner);
        return węzły;
    }

    private static Wyciąg[] wczytajWyciągi(Scanner skaner, Węzeł[] węzły, KolejkaZdarzeń ośCzasu) {
        int liczbaWyciągów = Integer.parseInt(skaner.nextLine().trim());
        Wyciąg[] wyciągi = new Wyciąg[liczbaWyciągów];

        for (int i = 0; i < liczbaWyciągów; i++) {
            Scanner skanerWiersza = new Scanner(skaner.nextLine());
            skanerWiersza.useLocale(Locale.ENGLISH);
            int początek = skanerWiersza.nextInt();
            int koniec = skanerWiersza.nextInt();
            int czasOczekiwania = skanerWiersza.nextInt();
            int rozmiarGrupy = skanerWiersza.nextInt();
            int czasPrzejazdu = skanerWiersza.nextInt();
            wyciągi[i] = new Wyciąg(i, węzły[początek], węzły[koniec], czasPrzejazdu, rozmiarGrupy, czasOczekiwania);
            węzły[początek].dodajWyciąg(wyciągi[i]);

            ZdarzenieOdjazdWyciągu pierwszyOdjazd = new ZdarzenieOdjazdWyciągu(0, wyciągi[i]);
            ośCzasu.wstawZdarzenie(pierwszyOdjazd);
            skanerWiersza.close();
        }
        pomińPusteWiersze(skaner);
        return wyciągi;
    }

    private static Trasa[] wczytajTrasy(Scanner skaner, Węzeł[] węzły) {
        int liczbaTras = Integer.parseInt(skaner.nextLine().trim());
        Trasa[] trasy = new Trasa[liczbaTras];

        for (int i = 0; i < liczbaTras; i++) {
            Scanner skanerWiersza = new Scanner(skaner.nextLine());
            skanerWiersza.useLocale(Locale.ENGLISH);
            int początek = skanerWiersza.nextInt();
            int koniec = skanerWiersza.nextInt();
            int poziomTrudności = skanerWiersza.nextInt();
            int czasPrzejazdu = skanerWiersza.nextInt();
            double bazowaAtrakcyjność = skanerWiersza.nextDouble();
            double odpornośćNaNierówności = skanerWiersza.nextDouble();
            trasy[i] = new Trasa(i, węzły[początek], węzły[koniec], czasPrzejazdu, poziomTrudności, bazowaAtrakcyjność,
                    odpornośćNaNierówności);
            węzły[początek].dodajTrasę(trasy[i]);
            skanerWiersza.close();
        }
        pomińPusteWiersze(skaner);
        return trasy;
    }

    private static List<Sportowiec> wczytajSportowców(Scanner skaner, Węzeł[] węzły, KolejkaZdarzeń ośCzasu,
            OśrodekNarciarski ośrodek) {
        int liczbaGrup = Integer.parseInt(skaner.nextLine().trim());
        Random generator = new Random();
        int idSportowców = 1;
        List<Sportowiec> śledzeni = new ArrayList<>();

        for (int g = 0; g < liczbaGrup; g++) {
            Scanner wiersz1 = new Scanner(skaner.nextLine());
            wiersz1.useLocale(Locale.ENGLISH);
            int rozmiarGrupy = wiersz1.nextInt();
            int poziomZaawansowania = wiersz1.nextInt();
            double spontaniczność = wiersz1.nextDouble();
            double wspZnudzenia = wiersz1.nextDouble();
            String rodzaj = wiersz1.next();
            boolean śledzony = (wiersz1.hasNext() && wiersz1.next().equals("s"));

            Scanner wiersz2 = new Scanner(skaner.nextLine());
            wiersz2.useLocale(Locale.ENGLISH);
            double wagaDopasowania = wiersz2.nextDouble();
            double wagaWyrównania = wiersz2.nextDouble();
            double wagaZnudzenia = wiersz2.nextDouble();

            Scanner wiersz3 = new Scanner(skaner.nextLine());
            wiersz3.useLocale(Locale.ENGLISH);
            int start = wiersz3.nextInt();
            String godzinaStartu = wiersz3.next();
            int odstęp = (rozmiarGrupy > 1) ? wiersz3.nextInt() : 0;
            int czasStartu = konwersjaCzasu(godzinaStartu);

            for (int i = 0; i < rozmiarGrupy; i++) {
                int czasObecnie = czasStartu + (i * odstęp);

                StrategiaWyboru strategia;
                switch (rodzaj) {
                    case "L":
                        strategia = new StrategiaLokalna();
                        break;
                    case "Z":
                        strategia = new StrategiaZachłanna();
                        break;
                    case "K":
                        strategia = new StrategiaKolekcjonera();
                        break;
                    default:
                        throw new IllegalArgumentException("Nieznany typ sportowca w pliku: " + rodzaj);
                }

                Sportowiec s = new Sportowiec(
                        idSportowców, śledzony, poziomZaawansowania,
                        wagaDopasowania, wagaWyrównania, wagaZnudzenia,
                        spontaniczność, wspZnudzenia, węzły[start],
                        czasObecnie, generator, strategia, ośrodek);

                if (śledzony)
                    śledzeni.add(s);
                idSportowców++;
                ośCzasu.wstawZdarzenie(new ZdarzenieWybór(czasObecnie, s));
            }
            wiersz1.close();
            wiersz2.close();
            wiersz3.close();
        }
        return śledzeni;
    }
}