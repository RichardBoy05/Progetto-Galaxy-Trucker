package galaxytrucker.src.logic.assemblaggio;
import java.util.EnumMap;
import java.util.Map;

import galaxytrucker.src.logic.gioco.Livello;

/**
 * Rappresenta una griglia di celle che costituisce la struttura base di una nave spaziale.
 * La griglia viene inizializzata in base alla forma definita da un livello di gioco
 * e permette di posizionare e gestire tessere nelle varie celle.
 */
public class Griglia {
    private final Cella[][] celle;

    /**
     * Costruttore della griglia. Inizializza la griglia in base alla forma della nave
     * definita dal livello specificato.
     *
     * @param livello il livello di gioco che definisce la forma della nave
     */
    public Griglia(Livello livello) {
        boolean[][] forma = livello.getFormaNave();
        celle = new Cella[forma.length][forma[0].length];

        for (int i = 0; i < forma.length; i++) {
            for (int j = 0; j < forma[i].length; j++) {
                celle[i][j] = new Cella(i, j, forma[i][j]);
            }
        }
    }

    /**
     * Verifica se una posizione specificata è valida all'interno della griglia.
     *
     * @param riga la riga della cella da verificare
     * @param colonna la colonna della cella da verificare
     * @return true se la posizione è valida, false altrimenti
     */
    private boolean isPosizioneValida(int riga, int colonna) {
        return riga >= 0 && riga < celle.length && colonna >= 0 && colonna < celle[0].length;
    }

    /**
     * Posiziona una tessera nella cella specificata, se possibile.
     *
     * @param riga la riga della cella
     * @param colonna la colonna della cella
     * @param tessera la tessera da posizionare
     * @return true se la tessera è stata posizionata con successo, false altrimenti
     */
    public boolean posizionaTessera(int riga, int colonna, Tessera tessera) {
        if (isPosizioneValida(riga, colonna) && celle[riga][colonna].isDisponibilePerPosizionamento()) {
            if (celle[riga][colonna].inserisciTessera(tessera)) {
                return true;
            }
        }
        rimuoviTessera(riga, colonna);
        return false;
    }

    /**
     * Rimuove la tessera dalla cella specificata, se presente.
     *
     * @param riga la riga della cella
     * @param colonna la colonna della cella
     */
    public void rimuoviTessera(int riga, int colonna) {
        if (isPosizioneValida(riga, colonna)) {
            celle[riga][colonna].rimuoviTessera();
        }
    }
    
    /**
     * Restituisce la cella alla posizione specificata.
     *
     * @param riga la riga della cella
     * @param colonna la colonna della cella
     * @return la cella richiesta, o null se la posizione non è valida
     */
    public Cella getCella(int riga, int colonna) {
        if (isPosizioneValida(riga, colonna)) {
            return celle[riga][colonna];
        }
        return null;
    }

    /**
     * Restituisce l'altezza della griglia (numero di righe).
     *
     * @return l'altezza della griglia
     */
    public int getAltezza() {
        return celle.length;
    }

    /**
     * Restituisce la larghezza della griglia (numero di colonne).
     *
     * @return la larghezza della griglia
     */
    public int getLarghezza() {
        return celle[0].length;
    }

    /**
     * Restituisce una mappa delle tessere adiacenti alla cella specificata.
     * La mappa associa ogni direzione (NORD, SUD, EST, OVEST) alla tessera corrispondente,
     * o null se non c'è tessera in quella direzione o la direzione è fuori dalla griglia.
     *
     * @param cella la cella di cui trovare le adiacenze
     * @return una mappa delle tessere adiacenti
     */
    public Map<Direzione, Tessera> getTessereAdiacenti(Cella cella) {
        Map<Direzione, Tessera> adiacenti = new EnumMap<>(Direzione.class);

        int riga = cella.getRiga();
        int colonna = cella.getColonna();
        
        for (Direzione direzione : Direzione.values()) {
            int nuovaRiga = riga;
            int nuovaColonna = colonna;

            switch (direzione) {
                case NORD:
                    nuovaRiga--;
                    break;
                case SUD:
                    nuovaRiga++;
                    break;
                case EST:
                    nuovaColonna++;
                    break;
                case OVEST:
                    nuovaColonna--;
                    break;
            }

            if(isPosizioneValida(nuovaRiga, nuovaColonna)){
                Cella cellaAdiacente = getCella(nuovaRiga, nuovaColonna);
                adiacenti.put(direzione, cellaAdiacente != null ? cellaAdiacente.getTessera() : null);
            }
            else{
                adiacenti.put(direzione, null);
            }  
        }
        return adiacenti;
    }
}