package galaxytrucker.src.logic.eccezioni;

import galaxytrucker.src.logic.assemblaggio.Cella;
import galaxytrucker.src.logic.gioco.Livello;

/**
 * Eccezione che rappresenta un errore di connessione non valida tra tessere
 * durante la configurazione della nave.
 * <p>
 * Viene sollevata quando una {@link Cella} presenta una connessione non valida.
 * </p>
 * 
 * @see ConfigurazioneNaveNonValidaException
 * @see Cella
 * @see Livello
 */

public class ConnessioneTesseraNonValidaException extends ConfigurazioneNaveNonValidaException {
	
	private static final long serialVersionUID = 1L;
	
	
	/**
     * Costruisce una nuova eccezione {@code ConnessioneTesseraNonValidaException}
     * con un messaggio di dettaglio che indica la posizione della cella con la connessione
     * non valida.
     *
     * @param cella la cella che contiene la tessera connessa in modo invalido.
     * @param livello il livello della partita, utile per la conversione delle coordinate della cella.
     */

	public ConnessioneTesseraNonValidaException(Cella cella, Livello livello) {
		super("Connessione non valida nella tessera alla riga "+ cella.getCoordinateGioco(livello).getRiga() +" e colonna "+ cella.getCoordinateGioco(livello).getColonna()+"!");
	}

}
