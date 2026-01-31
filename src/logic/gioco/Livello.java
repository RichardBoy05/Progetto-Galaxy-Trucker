package galaxytrucker.src.logic.gioco;

import java.awt.Color;
import galaxytrucker.src.logic.assemblaggio.Coordinate;

/**
 * Enumerazione che rappresenta i diversi livelli di difficoltà disponibili nel gioco.
 * Ogni livello definisce:
 * <ul>
 *   <li>Il numero base di crediti di vittoria assegnati a fine partita.</li>
 *   <li>Il numero di crediti bonus assegnati per la nave più bella (quella con meno connettori esposti).</li>
 *   <li>Il colore delle celle valide e non valide nella plancia nave.</li>
 *   <li>La forma della nave come matrice bidimensionale di booleani, dove {@code true} indica una cella valida per la costruzione.</li>
 *   <li>Coordinate minime utilizzate per tradurre tra coordinate reali (della matrice) e coordinate di gioco.</li>
 * </ul>
 *
 * I livelli disponibili sono:
 * <ul>
 *   <li>{@link #P} - Livello di prova</li>
 *   <li>{@link #I} - Livello I (iniziale)</li>
 *   <li>{@link #II} - Livello II (intermedio)</li>
 *   <li>{@link #III} - Livello III (avanzato)</li>
 * </ul>
 */
public enum Livello {

    /**
     * Livello di prova (stessi attributi del livello {@link #I}, ma con regole di gioco differenti).
     */
    P(1, 2, 19, 5, 4, new Color(0, 103, 207), new Color(0, 74, 148), new boolean[][] {
        {false, false, false, true,  false, false, false},
        {false, false, true,  true,  true,  false, false},
        {false, true,  true,  true,  true,  true,  false},
        {false, true,  true,  true,  true,  true,  false},
        {false, true,  true,  false, true,  true,  false}
    }) {
        @Override
        public Coordinate convertiDaRealiAGioco(int riga, int colonna) {
            return new Coordinate(getRigaMinima() + riga, getColonnaMinima() + colonna);
        }
        @Override
        public Coordinate convertiDaGiocoAReali(int riga, int colonna) {
            return new Coordinate(riga - getRigaMinima(), colonna - getColonnaMinima());
        }
        @Override
        public Coordinate getCentroNave() {
            return new Coordinate(2, 3);
        }
    },

    /**
     * Livello I, iniziale (stessi attributi del livello {@link #P}, ma con regole di gioco differenti).
     */
    I(1, 2, 19, 5, 4, new Color(0, 103, 207), new Color(0, 74, 148), new boolean[][] {
        {false, false, false, true,  false, false, false},
        {false, false, true,  true,  true,  false, false},
        {false, true,  true,  true,  true,  true,  false},
        {false, true,  true,  true,  true,  true,  false},
        {false, true,  true,  false, true,  true,  false}
    }) {
        @Override
        public Coordinate convertiDaRealiAGioco(int riga, int colonna) {
            return new Coordinate(getRigaMinima() + riga, getColonnaMinima() + colonna);
        }
        @Override
        public Coordinate convertiDaGiocoAReali(int riga, int colonna) {
            return new Coordinate(riga - getRigaMinima(), colonna - getColonnaMinima());
        }
        @Override
        public Coordinate getCentroNave() {
            return new Coordinate(2, 3);
        }
    },

    /**
     * Livello II, intermedio.
     */
    II(2, 4, 24, 5, 4, new Color(110, 0, 204), new Color(67, 0, 125), new boolean[][] {
        {false, false, true, false, true, false, false},
        {false, true,  true, true,  true, true,  false},
        {true,  true,  true, true,  true, true,  true},
        {true,  true,  true, true,  true, true,  true},
        {true,  true,  true, false, true, true,  true}
    }) {
        @Override
        public Coordinate convertiDaRealiAGioco(int riga, int colonna) {
            return new Coordinate(getRigaMinima() + riga, getColonnaMinima() + colonna);
        }
        @Override
        public Coordinate convertiDaGiocoAReali(int riga, int colonna) {
            return new Coordinate(riga - getRigaMinima(), colonna - getColonnaMinima());
        }
        @Override
        public Coordinate getCentroNave() {
            return new Coordinate(2, 3);
        }
    },

    /**
     * Livello III, avanzato.
     */
    III(3, 6, 35, 4, 3, new Color(199, 0, 0), new Color(128, 0, 0), new boolean[][] {
        {false, false, false, false, true, false, false, false, false},
        {false, false, false, true,  true, true,  false, false, false},
        {true,  false, true,  true,  true, true,  true,  false, true},
        {true,  true,  true,  true,  true, true,  true,  true,  true},
        {true,  true,  true,  true,  true, true,  true,  true,  true},
        {true,  true,  false, true,  true, true,  false, true,  true}
    }) {
        @Override
        public Coordinate convertiDaRealiAGioco(int riga, int colonna) {
            return new Coordinate(getRigaMinima() + riga, getColonnaMinima() + colonna);
        }
        @Override
        public Coordinate convertiDaGiocoAReali(int riga, int colonna) {
            return new Coordinate(riga - getRigaMinima(), colonna - getColonnaMinima());
        }
        @Override
        public Coordinate getCentroNave() {
            return new Coordinate(3, 4);
        }
    };

    /**
     * Crediti base assegnati ai giocatori che hanno completato la fase di volo.
     * Il numero di crediti assegnato effettivamente varia in base alla posizione raggiunta.
     */
    private final int creditiBaseDiVittoria;

    /**
     * Crediti bonus assegnati al giocatore con la nave più bella, ovvero con meno connettori esposti.
     */
    private final int creditiNavePiuBella;
    
    /**
     * Distanza oltre la quale un giocatore è considerato doppiato nella fase di volo.
     */
    private final int distanzaDoppiaggio;
    
    /**
     * Colore (formato RGB) delle celle valide nella plancia nave.
     */
    private final Color coloreCelleValide;
    
    /**
     * Colore (formato RGB) delle celle NON valide nella plancia nave.
     */
    private final Color coloreCelleNonValide;

    /**
     * Rappresentazione della forma della nave come matrice di boolean.
     * {@code true} cella dove è possibile piazzare una tessera, {@code false} cella non utilizzabile.
     */
    private final boolean[][] formaNave;
    
    /**
     * Riga minima (in coordinate di gioco) corrispondente alla riga 0 della matrice della nave.
     * Serve per consentire la conversione fra le coordinate interne delle matrice e quelle reali di gioco.
     */
    private final int rigaMinima;
    
    /**
     * Colonna minima (in coordinate di gioco) corrispondente alla colonna 0 della matrice della nave.
     * Serve per consentire la conversione fra le coordinate interne delle matrice e quelle reali di gioco.
     */
    private final int colonnaMinima;
    

    /**
     * Costruttore dell'enumerazione.
     *
     * @param creditiBaseDiVittoria crediti di base assegnati a ogni giocatore a fine gioco.
     * @param creditiNavePiuBella crediti bonus per la nave più bella.
     * @param distanzaDoppiaggio distanza oltre la quale un giocatore è considerato doppiato nella fase di volo.
     * @param rigaMinima riga minima (in coordinate di gioco) corrispondente alla riga 0 della matrice della nave.
     * @param colonnaMinima colonna minima (in coordinate di gioco) corrispondente alla colonna 0 della matrice della nave.
     * @param coloreCelleValide colore delle celle valide della plancia nave.
     * @param coloreCelleNonValide colore delle celle non valide della plancia nave.
     * @param formaNave matrice che rappresenta la forma della nave per questo livello.
     */

    private Livello(int creditiBaseDiVittoria, int creditiNavePiuBella, int distanzaDoppiaggio, int rigaMinima, int colonnaMinima, Color coloreCelleValide, Color coloreCelleNonValide, boolean[][] formaNave) {
        this.creditiBaseDiVittoria = creditiBaseDiVittoria;
        this.creditiNavePiuBella = creditiNavePiuBella;
        this.distanzaDoppiaggio=distanzaDoppiaggio;
        this.rigaMinima = rigaMinima;
        this.colonnaMinima = colonnaMinima;
        this.coloreCelleValide = coloreCelleValide;
        this.coloreCelleNonValide = coloreCelleNonValide;
        this.formaNave = formaNave;
    }
    
    
    /**
     * Converte le coordinate reali (ossia quelle utilizzate internamente per gestire la matrice
     * della nave) nelle coordinate di gioco (ossia quelle relative alla plancia nave del gioco).
     * La conversione dipende dalla forma della nave e di conseguenza dal {@link Livello}.
     *
     * @param riga   la riga nelle coordinate reali.
     * @param colonna la colonna nelle coordinate reali.
     * @return le coordinate di gioco corrispondenti.
     */
    public abstract Coordinate convertiDaRealiAGioco(int riga, int colonna);
    

    /**
     * Converte le coordinate di gioco (ossia quelle relative alla plancia nave del gioco) nelle
     * coordinate reali (ossia quelle utilizzate internamente per gestire la matrice della nave).
     * La conversione dipende dalla forma della nave e di conseguenza dal {@link Livello}.
     *
     * @param riga   la riga nelle coordinate di gioco.
     * @param colonna la colonna nelle coordinate di gioco.
     * @return le coordinate reali corrispondenti.
     */
    public abstract Coordinate convertiDaGiocoAReali(int riga, int colonna);
    

    /**
     * Restituisce le coordinate del centro della nave per il livello corrente.
     * Queste coordinate rappresentano la posizione di partenza per la
     * costruzione della nave e sono usate per posizionare la cabina iniziale.
     *
     * @return le coordinate centrali della nave.
     */
    public abstract Coordinate getCentroNave();
    
    
    /**
     * Restituisce una descrizione testuale leggibile del livello.
     * 
     * @return "Livello di prova" se {@code this == P}, altrimenti "Livello I/II/III"
     */
    @Override
    public String toString() {
        if (this == P) {
            return "Livello di prova";
        }
        return "Livello " + name();
    }
    
    
    // getters

    /**
     * Restituisce i crediti di vittoria base per questo livello.
     *
     * @return crediti di vittoria base.
     */
    public int getCreditiBaseDiVittoria() {
        return creditiBaseDiVittoria;
    }

    /**
     * Restituisce i crediti bonus assegnati per la nave più bella in questo livello.
     *
     * @return crediti bonus per la nave più bella
     */
    public int getCreditiNavePiuBella() {
        return creditiNavePiuBella;
    }
    
    /**
     * Restituisce la distanza di doppiaggio di questo livello.
     *
     * @return distanza di doppiaggio livello.
     */
    public int getDistanzaDoppiaggio() {
        return distanzaDoppiaggio;
    }
    
    /**
     * Restituisce la riga minima in coordinate di gioco, corrispondente alla riga 0 della matrice della nave.
     *
     * @return riga minima in coordinate di gioco.
     */
    public int getRigaMinima() {
        return rigaMinima;
    }
    
    /**
     * Restituisce la colonna minima in coordinate di gioco, corrispondente alla colonna 0 della matrice della nave.
     *
     * @return colonna minima in coordinate di gioco.
     */
    public int getColonnaMinima() {
        return colonnaMinima;
    }

    /**
     * Restituisce il colore delle celle valide della plancia nave per questo livello.
     *
     * @return colore delle celle valide.
     */
    public Color getColoreCelleValide() {
        return coloreCelleValide;
    }
    
    /**
     * Restituisce il colore delle celle non valide della plancia nave per questo livello.
     *
     * @return colore delle celle non valide.
     */
    public Color getColoreCelleNonValide() {
        return coloreCelleNonValide;
    }

    /**
     * Restituisce una copia della matrice che rappresenta la forma della nave per questo livello.
     * La copia è utilizzata per evitare modifiche esterne alla struttura originale.
     *
     * @return copia della matrice della forma della nave.
     */
    public boolean[][] getFormaNave() {
        boolean[][] copia = new boolean[this.formaNave.length][];
        for (int i = 0; i < formaNave.length; i++) {
            copia[i] = new boolean[formaNave[i].length];
            for (int j = 0; j < formaNave[i].length; j++) {
                copia[i][j] = formaNave[i][j];
            }
        }
        return copia;
    }
    
}