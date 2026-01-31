package galaxytrucker.src.logic.eccezioni;

import galaxytrucker.src.logic.assemblaggio.Cella;
import galaxytrucker.src.logic.gioco.Livello;

/**
 * Eccezione che rappresenta un errore nel piazzamento di una tessera 
 * durante la configurazione della nave.
 * <p>
 * Viene sollevata quando una {@link Cella} viene posizionata in modo non valido.
 * </p>
 * 
 * @see ConfigurazioneNaveNonValidaException
 * @see Cella
 * @see Livello
 */

public class PiazzamentoTesseraNonValidoException extends ConfigurazioneNaveNonValidaException {
	
	private static final long serialVersionUID = 1L;
	
	/**
     * Costruisce una nuova eccezione {@code PiazzamentoTesseraNonValidoException}
     * con un messaggio predefinito.
     *
     * @param cella la cella che contiene la tessera non valida.
     * @param livello il livello della partita, utile per la conversione delle coordinate della cella.
     */
	public PiazzamentoTesseraNonValidoException (Cella cella, Livello livello) {
		super("Tessera non valida alla riga "+ cella.getCoordinateGioco(livello).getRiga() + " e colonna " + cella.getCoordinateGioco(livello).getColonna() +"!");
	}

	
	/**
     * Costruisce una nuova eccezione {@code PiazzamentoTesseraNonValidoException}
     * con il messaggio di dettaglio specificato.
     *
     * @param messaggio il messaggio descrittivo dell'eccezione
     */
	public PiazzamentoTesseraNonValidoException(String messaggio){
		super(messaggio);
	}
}