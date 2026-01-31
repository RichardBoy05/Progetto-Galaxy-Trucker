package galaxytrucker.src.logic.volo;

/**
 * Enum che rappresenta i diversi tipi di merci presenti nel gioco.
 * <p>
 * Ogni tipo di merce ha un valore associato, che ne indica il peso o l'importanza,
 * e può essere contrassegnata come preziosa.
 * </p>
 */
public enum Merce {
    
    /** Merce di tipo verde, valore 2. */
    VERDE(2),
    
    /** Merce di tipo blu, valore 1. */
    BLU(1),
    
    /** Merce di tipo gialla, valore 3. */
    GIALLA(3),
    
    /** Merce di tipo rossa, valore 4 e considerata preziosa. */
    ROSSA(4, true);

    /** Valore numerico della merce. */
    private final int valore;

    /** Indica se la merce è considerata preziosa. */
    private final boolean isPreziosa;

    /**
     * Restituisce il valore numerico associato alla merce.
     * 
     * @return valore della merce
     */
    public int getValore() {
        return valore;
    }

    /**
     * Indica se la merce è preziosa.
     * 
     * @return {@code true} se la merce è preziosa, {@code false} altrimenti
     */
    public boolean isPreziosa() {
        return isPreziosa;
    }

    /**
     * Costruttore per merci non preziose.
     * 
     * @param valore valore numerico della merce
     */
    Merce(int valore) {
        this(valore, false);
    }

    /**
     * Costruttore per merci specificando se sono preziose o meno.
     * 
     * @param valore valore numerico della merce
     * @param isPreziosa indica se la merce è preziosa
     */
    Merce(int valore, boolean isPreziosa) {
        this.valore = valore;
        this.isPreziosa = isPreziosa;
    }
}