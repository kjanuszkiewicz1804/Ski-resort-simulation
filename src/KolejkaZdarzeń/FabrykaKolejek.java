package KolejkaZdarzeń;

public class FabrykaKolejek {
    public static KolejkaZdarzeń stwórzKolejkę() {
        return new BibliotecznaKolejkaZdarzeń();
    }
}
