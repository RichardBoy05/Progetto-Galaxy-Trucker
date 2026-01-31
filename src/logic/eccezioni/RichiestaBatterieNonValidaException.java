package galaxytrucker.src.logic.eccezioni;


/**
 * Eccezione che rappresenta un errore nella richiesta di batterie.
 * <p>
 * Viene sollevata quando un'azione che prevede il consumo o l'utilizzo
 * di batterie non può essere eseguita perché viola le regole di gioco
 * o perché le condizioni richieste non sono soddisfatte.
 * </p>
 * 
 * @see MossaNonValidaException
 */

public class RichiestaBatterieNonValidaException extends MossaNonValidaException {
	
	private static final long serialVersionUID = 1L;

	
	/**
     * Costruisce una nuova eccezione {@code RichiestaBatterieNonValidaException}
     * con il messaggio di dettaglio specificato.
     *
     * @param message il messaggio descrittivo dell'eccezione
     */
	
	public RichiestaBatterieNonValidaException(String message) {
		super(message);
	
	}

}