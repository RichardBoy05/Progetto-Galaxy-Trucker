package galaxytrucker.src.logic.assemblaggio;

/**
 * Rappresenta le tipologie di abitanti che possono essere presenti
 * all'interno di una tessera {@link Cabina}. Ogni abitante è associato a una {@link Specie}.
 * 
 * <p>Le possibili istanze di {@code Abitante} sono:
 * <ul>
 *   <li>{@link #ASTRONAUTA}</li>
 *   <li>{@link #ALIENO_MARRONE}</li>
 *   <li>{@link #ALIENO_VIOLA}</li>
 * </ul>
 * 
 * @see Cabina
 * @see Specie
 */
public enum Abitante {

    ASTRONAUTA(Specie.UMANO),
    ALIENO_MARRONE(Specie.ALIENO),
    ALIENO_VIOLA(Specie.ALIENO);

    /**
     * La specie associata all'abitante.
     */
    private final Specie specie;

    /**
     * Costruisce un nuovo abitante associandogli una {@link Specie}.
     * 
     * @param specie la specie associata all'abitante
     */
    private Abitante(Specie specie) {
        this.specie = specie;
    }

    /**
     * Restituisce la {@link Specie} associata a questo abitante.
     * 
     * @return la specie dell'abitante
     */
    public Specie getSpecie() {
        return specie;
    }

    /**
     * Restituisce una rappresentazione in forma di stringa leggibile del nome dell'abitante.
     * <p>
     * Il formato restituito è il nome in minuscolo dell'enum con gli underscore rimpiazzati dagli spazi.
     * 
     * @return il nome formattato dell'abitante
     */
    @Override
    public String toString() {
    	  return name().toLowerCase().replaceAll("_", " ");
    }
}