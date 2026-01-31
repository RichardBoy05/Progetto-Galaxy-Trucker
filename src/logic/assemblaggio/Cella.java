package galaxytrucker.src.logic.assemblaggio;

import galaxytrucker.src.logic.gioco.Livello;

/**
 * Rappresenta una singola cella all'interno della griglia di una nave spaziale.
 * Ogni cella può contenere una tessera e ha informazioni sulla sua posizione e disponibilità.
 */
public class Cella {
    
    private final Coordinate coordinate;
    private final boolean disponibile;
    private Tessera tessera;

    /**
     * Costruttore della cella.
     *
     * @param riga la riga della cella nella griglia
     * @param colonna la colonna della cella nella griglia
     * @param disponibile indica se la cella è disponibile per il posizionamento di tessere
     */
    public Cella(int riga, int colonna, boolean disponibile) {
        this.coordinate = new Coordinate(riga, colonna);
        this.disponibile = disponibile;
        this.tessera = null;
    }

    /**
     * Verifica se la cella è disponibile per il posizionamento di una tessera.
     *
     * @return true se la cella è disponibile e non contiene già una tessera, false altrimenti
     */
    public boolean isDisponibilePerPosizionamento() {
        return disponibile && tessera == null;
    }

    /**
     * Inserisce una tessera nella cella, se possibile.
     *
     * @param tessera la tessera da inserire
     * @return true se la tessera è stata inserita con successo, false altrimenti
     */
    public boolean inserisciTessera(Tessera tessera) {
        if (isDisponibile()) {
            this.tessera = tessera;
            return true;
        }
        return false;
    }

    /**
     * Restituisce la tessera attualmente presente nella cella.
     *
     * @return la tessera presente, o null se la cella è vuota
     */
    public Tessera getTessera() {
        return tessera;
    }

    /**
     * Rimuove e restituisce la tessera presente nella cella.
     *
     * @return la tessera rimossa, o null se la cella era vuota
     */
    public Tessera rimuoviTessera() {
        Tessera rimossa = this.tessera;
        this.tessera = null;
        return rimossa;
    }

    /**
     * Restituisce il numero di riga della cella.
     *
     * @return la riga della cella
     */
    public int getRiga() { return coordinate.getRiga(); }

    /**
     * Restituisce il numero di colonna della cella.
     *
     * @return la colonna della cella
     */
    public int getColonna() { return coordinate.getColonna(); }

    /**
     * Verifica se la cella è disponibile (indipendentemente dalla presenza di una tessera).
     *
     * @return true se la cella è disponibile, false altrimenti
     */
    public boolean isDisponibile() { return disponibile; }

    /**
     * Restituisce le coordinate della cella.
     *
     * @return l'oggetto Coordinate che rappresenta la posizione della cella
     */
    public Coordinate getCoordinate() {
        return coordinate;
    }

    /**
     * Restituisce le coordinate della cella convertite nel sistema di coordinate di gioco.
     *
     * @param livello il livello di gioco utilizzato per la conversione
     * @return le coordinate convertite
     */
    public Coordinate getCoordinateGioco(Livello livello) {
        return livello.convertiDaRealiAGioco(coordinate.getRiga(), coordinate.getColonna());
    }
}