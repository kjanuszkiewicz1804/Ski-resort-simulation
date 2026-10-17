package Sportowiec;

import Ośrodek.*;
import StrategiaWyboru.*;

import java.util.*;

/**
 * Klasa reprezentująca narciarza w ośrodku. Odpowiada za podejmowanie decyzji
 * odnośnie wyboru kolejnego połączenia, i analizowanie w tym celu ich atrakcyjności,
 * oraz za zdawanie raportów, w przypadku sportowców śledzonych.
 */
public class Sportowiec {
    private int identyfikator;
    private boolean śledzony;

    private int poziomZaawansowania;

    private double wagaDopasowania;
    private double wagaWyrównania;
    private double wagaZnudzenia;
    private double spontaniczność;
    private double współczynnikZnudzenia;
    
    private Węzeł węzełStartowy;
    private int czasStartu;

    private Random generator;

    private Węzeł aktualnaPozycja;
    private OśrodekNarciarski aktualnyOśrodek;

    private StrategiaWyboru strategia;

    private Map<Trasa, ZapisZnudzenia> historiaZnudzenia = new HashMap<>();
    private int łącznaLiczbaZjazdów = 0;

    private int licznikPrzejazdów = 0;
    private final Map<Połączenie, List<Integer>> historiaŚledzenia = new HashMap<>();

    public void dodajDoHistoriiŚledzenia(Połączenie p) {
        if (this.śledzony) {
            this.licznikPrzejazdów++;
            
            this.historiaŚledzenia.putIfAbsent(p, new ArrayList<>());
            
            this.historiaŚledzenia.get(p).add(this.licznikPrzejazdów);
        }
    }

    public Sportowiec(int id, boolean s, int pz, double wd, double ww, double wz, double sp, double wspZ, Węzeł ws, int cs, Random g, StrategiaWyboru strat, OśrodekNarciarski os) {
        this.identyfikator = id;
        this.śledzony = s;
        this.poziomZaawansowania = pz;
        this.wagaDopasowania = wd;
        this.wagaWyrównania = ww;
        this.wagaZnudzenia = wz;           
        this.spontaniczność = sp;
        this.współczynnikZnudzenia = wspZ; 
        this.węzełStartowy = ws;
        this.czasStartu = cs;
        this.aktualnaPozycja = ws;
        this.generator = g;
        this.strategia = strat;           
        this.aktualnyOśrodek = os;
    }

    public boolean czyŚledzony() {
        return this.śledzony;
    }

    public void przemieść(Węzeł dokąd) {
        this.aktualnaPozycja = dokąd;
    }

    public void zdajRaport(int czas, String akcja) {
        if (this.śledzony) {
            int godzina = 9 +(czas / 3600);
            int minuty = (czas % 3600) / 60;
            int sekundy = czas % 60;
            String sformatowanyCzas = String.format("%02d:%02d:%02d",godzina,minuty,sekundy);
            System.out.println(sformatowanyCzas + ": Sportowiec " + this.identyfikator + " " + akcja);
        }
    }

    public void zarejestrujZjazdTrasą(Trasa t) {
        double obecne = pobierzAktualneZnudzenie(t);
        
        int poprzednieRazy = this.historiaZnudzenia.getOrDefault(t, new ZapisZnudzenia(0, 0.0, 0)).ileRazy;
        
        double noweZnudzenie = this.współczynnikZnudzenia + (1.0 - this.współczynnikZnudzenia) * obecne;
        this.łącznaLiczbaZjazdów++;
        
        this.historiaZnudzenia.put(t, new ZapisZnudzenia(this.łącznaLiczbaZjazdów, noweZnudzenie, poprzednieRazy + 1));
    }

    public double pobierzAktualneZnudzenie(Trasa t) {
        ZapisZnudzenia zapis = this.historiaZnudzenia.getOrDefault(t, new ZapisZnudzenia(0, 0.0, 0));
        int ileInnychZjazdów = this.łącznaLiczbaZjazdów - zapis.numerZjazdu;

        return zapis.znudzenie * Math.pow(1.0 - this.współczynnikZnudzenia, ileInnychZjazdów);
    }



    public Połączenie dokonajWyboru(int czas) {
        //Zgodnie z poleceniem od godziny 15:00 kierują się z powrotem do 
        //swoich węzłów startowych, ale na razie symulacja tego nie obsługuje
        //więc zwracane jest puste (null) połączenie.
        if(czas >= ((15-9) * 3600)) {
            return null;
        }

        return strategia.podejmijDecyzję(this);
       
    }

    public double obliczAtrakcyjność(Trasa t) {
        double dopasowanie = 0.0;
        int trudność = t.pobierzPoziomTrudności();
        int poziom = this.poziomZaawansowania;
        
        if (trudność < (poziom + 5)) {
            if (poziom > trudność) { 
                dopasowanie = Math.max(0.2, 1.0 - ((poziom - trudność) / 7.0)); 
            } else { 
                dopasowanie = 1.0 - ((trudność - poziom) / 5.0); 
            }
        }
        
        return (this.wagaDopasowania * dopasowanie) + 
               (this.wagaWyrównania * t.pobierzWyrównanie()) + 
               (this.wagaZnudzenia * (1.0 - this.pobierzAktualneZnudzenie(t)));
    }

    public boolean czySpontanicznaDecyzja() {
        return this.generator.nextDouble() < this.spontaniczność;
    }

    public Połączenie wybierzLosowePołączenie() {
        List<Trasa> trasy = this.aktualnaPozycja.pobierzTrasy();
        List<Wyciąg> wyciągi = this.aktualnaPozycja.pobierzWyciągi();
        
        int liczbaOpcji = trasy.size() + wyciągi.size();
        if (liczbaOpcji == 0) {
            return null; 
        }

        int losowyWybór = this.generator.nextInt(liczbaOpcji);
        
        if (losowyWybór < trasy.size()) {
            return trasy.get(losowyWybór);
        } else {
            return wyciągi.get(losowyWybór - trasy.size());
        }
    }

    public int pobierzPoziomZaawansowania() { return this.poziomZaawansowania; }
    public double pobierzWagęDopasowania() { return this.wagaDopasowania; }
    public double pobierzWagęWyrównania() { return this.wagaWyrównania; }
    public double pobierzWagęZnudzenia() { return this.wagaZnudzenia; }
    public double pobierzSpontaniczność() { return this.spontaniczność; }
    public Random pobierzGenerator() { return this.generator; }
    public Węzeł pobierzAktualnąPozycję() { return this.aktualnaPozycja; }
    public OśrodekNarciarski pobierzOśrodek() { return this.aktualnyOśrodek; }
    public int pobierzIdentyfikator() { return this.identyfikator; }

    public int pobierzLiczbęZjazdów(Trasa t) {
        return this.historiaZnudzenia.getOrDefault(t, new ZapisZnudzenia(0, 0.0, 0)).ileRazy;
    }

    public Map<Połączenie, List<Integer>> pobierzHistorięŚledzenia() {
        return this.historiaŚledzenia;
    }

    private static class ZapisZnudzenia {
        int numerZjazdu;
        double znudzenie;
        int ileRazy; 

        ZapisZnudzenia(int numerZjazdu, double znudzenie, int ileRazy) { 
            this.numerZjazdu = numerZjazdu; 
            this.znudzenie = znudzenie; 
            this.ileRazy = ileRazy; 
        }
    }
}
