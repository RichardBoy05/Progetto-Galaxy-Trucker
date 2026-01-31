package galaxytrucker.src.logic.eccezioni;


/**
 * Eccezione che rappresenta un errore durante il caricamento di una merce.
 * <p>
 * Viene sollevata quando un'operazione di caricamento merce non rispetta
 * le regole del gioco o risulta impossibile da eseguire.
 * </p>
 * 
 * @see MossaNonValidaException
 */

public class CaricamentoMerceNonValidoException extends MossaNonValidaException {
	
	private static final long serialVersionUID = 1L;
	
	/**
     * Costruisce una nuova eccezione {@code CaricamentoMerceNonValidoException}
     * con il messaggio di dettaglio specificato.
     *
     * @param message il messaggio descrittivo dell'eccezione
     */
	public CaricamentoMerceNonValidoException(String message) {
		super(message);
	
	}

}