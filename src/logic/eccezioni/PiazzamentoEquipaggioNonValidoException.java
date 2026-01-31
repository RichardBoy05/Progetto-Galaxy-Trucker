package galaxytrucker.src.logic.eccezioni;


/**
 * Eccezione che rappresenta un errore nel piazzamento di un membro 
 * dell'equipaggio.
 * <p>
 * Viene sollevata quando un'operazione di piazzamento equipaggio non 
 * rispetta le regole previste dal gioco o risulta impossibile da eseguire.
 * </p>
 * 
 * @see MossaNonValidaException
 */

public class PiazzamentoEquipaggioNonValidoException extends MossaNonValidaException {

	private static final long serialVersionUID = 1L;
	
	/**
     * Costruisce una nuova eccezione {@code PiazzamentoEquipaggioNonValidoException}
     * con il messaggio di dettaglio specificato.
     *
     * @param message il messaggio descrittivo dell'eccezione
     */
	public PiazzamentoEquipaggioNonValidoException(String message) {
		super(message);
	
	}
}