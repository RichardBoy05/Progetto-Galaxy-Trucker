package galaxytrucker.src.logic.assemblaggio;

/**
 * Rappresenta una coppia di coordinate (riga, colonna) all'interno di una griglia.
 * Fornisce metodi per accedere alle coordinate e confrontarle con altre coordinate.
 */
public class Coordinate {
    private final int riga;
    private final int colonna;

    /**
     * Costruttore che crea un nuovo oggetto Coordinate.
     *
     * @param riga il numero di riga (coordinata verticale)
     * @param colonna il numero di colonna (coordinata orizzontale)
     */
    public Coordinate(int riga, int colonna) {
        this.riga = riga;
        this.colonna = colonna;
    }
    
    /**
     * Confronta queste coordinate con altre coordinate.
     *
     * @param coordinateDaConfrontare le coordinate da confrontare con queste
     * @return true se le coordinate sono uguali, false altrimenti
     */
    public boolean confrontaCoordinate(Coordinate coordinateDaConfrontare) {
        return coordinateDaConfrontare.getRiga() == riga && 
               coordinateDaConfrontare.getColonna() == colonna;
    }

    /**
     * Restituisce il numero di riga di queste coordinate.
     *
     * @return il numero di riga
     */
    public int getRiga() { 
        return riga; 
    }

    /**
     * Restituisce il numero di colonna di queste coordinate.
     *
     * @return il numero di colonna
     */
    public int getColonna() { 
        return colonna; 
    }
}