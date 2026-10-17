package StrategiaWyboru;

import java.util.ArrayList;
import java.util.List;

import Ośrodek.Połączenie;
import Ośrodek.Trasa;
import Ośrodek.Wyciąg;
import Ośrodek.Węzeł;
import Sportowiec.Sportowiec;

/**
 * Strategia analizująca jedynie najbliższe dostępne trasy 
 * (z obecnego węzła i z węzłów osiągalnych jednym wyciągiem) i wybierająca 
 * najbardziej atrakcyjną z nich.
 */
public class StrategiaLokalna implements StrategiaWyboru{
    @Override
    public Połączenie podejmijDecyzję(Sportowiec s) {
        Węzeł obecnyWęzeł = s.pobierzAktualnąPozycję();
        List<Trasa> trasyBezpośrednie = obecnyWęzeł.pobierzTrasy();
        List<Wyciąg> wyciągi = obecnyWęzeł.pobierzWyciągi();

        int liczbaOpcji = trasyBezpośrednie.size();
        for (Wyciąg w : wyciągi) {
            liczbaOpcji += w.pobierzWęzełKońcowy().pobierzTrasy().size();
        }

        if (liczbaOpcji == 0) {
            if (!wyciągi.isEmpty()) {
                return wyciągi.get(0);
            }
            return null;
        }

        if(s.czySpontanicznaDecyzja()) {
            return s.wybierzLosowePołączenie();
        }

        List<Połączenie> możliwePołączenia = new ArrayList<>();
        List<Double> atrakcyjnośćPołączeń = new ArrayList<>();

        for (Trasa t : trasyBezpośrednie) {
            możliwePołączenia.add(t);
            atrakcyjnośćPołączeń.add(s.obliczAtrakcyjność(t));
        }

        for (Wyciąg w : wyciągi) {
            for (Trasa t : w.pobierzWęzełKońcowy().pobierzTrasy()) {
                możliwePołączenia.add(w);
                atrakcyjnośćPołączeń.add(s.obliczAtrakcyjność(t));
            }
        } 

        int najlepszyWybór = 0;
        double maksymalnaAtrakcyjność = atrakcyjnośćPołączeń.get(0);
    
        for (int i = 0; i < atrakcyjnośćPołączeń.size(); i++) {
            if (atrakcyjnośćPołączeń.get(i) > maksymalnaAtrakcyjność) {
                maksymalnaAtrakcyjność = atrakcyjnośćPołączeń.get(i);
                najlepszyWybór = i;
            }
        }
        
        return możliwePołączenia.get(najlepszyWybór);
    }

}
