package galaxytrucker.src.logic.eccezioni;


/**
 * Eccezione che rappresenta la presenza di tronconi fluttuanti 
 * nella configurazione della nave.
 * <p>
 * Viene sollevata quando, durante la fase di assemblaggio, una o più 
 * sezioni della nave risultano non collegate al modulo principale,
 * violando così le regole di configurazione.
 * </p>
 * 
 * @see ConfigurazioneNaveNonValidaException
 */

public class TronconiFluttuantiException extends ConfigurazioneNaveNonValidaException {
	
	private static final long serialVersionUID = 1L;

	
	/**
     * Costruisce una nuova eccezione {@code TronconiFluttuantiException}
     * con un messaggio di dettaglio predefinito.
     */
	
	public TronconiFluttuantiException() {
		super("La configurazione contiene tronconi fluttuanti non collegati!");
		
	}

}