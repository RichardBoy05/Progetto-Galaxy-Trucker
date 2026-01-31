package galaxytrucker.src.logic.assemblaggio;


/**
 * Enumerazione che rappresenta i possibili tipi di connettori presenti sui lati delle tessere.
 * Ogni connettore definisce come può collegarsi ad altri connettori sulle tessere adiacenti.
 * <p>
 * I tipi disponibili sono:
 * <ul>
 *   <li>{@code SINGOLO} — connettore singolo</li>
 *   <li>{@code DOPPIO} — connettore doppio</li>
 *   <li>{@code UNIVERSALE} — connettore universale</li>
 *   <li>{@code LISCIO} — lato liscio, ossia senza connettore</li>
 * </ul>
 */
public enum Connettore {

    SINGOLO,
    DOPPIO,
    UNIVERSALE,
    LISCIO;

	/**
     * Verifica se il connettore corrente è compatibile con un connettore adiacente specificato.
     * <p>
     * La compatibilità è definita secondo le seguenti regole:
     * <ul>
     *   <li>{@code SINGOLO} è compatibile con {@code SINGOLO} e {@code UNIVERSALE}</li>
     *   <li>{@code DOPPIO} è compatibile con {@code DOPPIO} e {@code UNIVERSALE}</li>
     *   <li>{@code UNIVERSALE} è compatibile con {@code SINGOLO}, {@code DOPPIO} e {@code UNIVERSALE}</li>
     *   <li>{@code LISCIO} è compatibile solo con {@code LISCIO}</li>
     * </ul>
     *
     * @param connettoreAdiacente il connettore da confrontare
     * @return {@code true} se compatibili, altrimenti {@code false}
     */
	public boolean isCompatibile(Connettore connettoreAdiacente) {
		
	    switch (this) {
	        case SINGOLO:
	            return connettoreAdiacente == SINGOLO || connettoreAdiacente == UNIVERSALE;
	        case DOPPIO:
	            return connettoreAdiacente == DOPPIO || connettoreAdiacente == UNIVERSALE;
	        case UNIVERSALE:
	        	return connettoreAdiacente == SINGOLO || connettoreAdiacente == DOPPIO || connettoreAdiacente == UNIVERSALE;
	        case LISCIO:
	            return connettoreAdiacente == LISCIO;
	        default:
	            return false;
	    }
	}

}