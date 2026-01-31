package galaxytrucker.src.logic.eccezioni;


/**
 * Eccezione astratta che rappresenta un errore nella configurazione della nave
 * durante la fase di assemblaggio.
 * <p>
 * Questa classe funge da base per tutte le eccezioni che derivano da una 
 * configurazione non valida della nave, come errori di collegamento o di piazzamento tessere.
 * </p>
 */

public abstract class ConfigurazioneNaveNonValidaException extends Exception {
	
	private static final long serialVersionUID = 1L;
	
	/**
     * Costruisce una nuova eccezione {@code ConfigurazioneNaveNonValidaException}
     * con il messaggio di dettaglio specificato.
     *
     * @param messaggio il messaggio descrittivo dell'eccezione
     */
	public ConfigurazioneNaveNonValidaException(String messaggio) {
		super(messaggio);
	}
	
}