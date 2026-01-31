package galaxytrucker.src.logic.eccezioni;


/**
 * Eccezione astratta che rappresenta un errore nell'esecuzione di una mossa.
 * <p>
 * Questa classe funge da base per tutte le eccezioni che derivano 
 * da una mossa non valida eseguita dal giocatore durante le varie fasi di gioco.
 * </p>
 */

public abstract class MossaNonValidaException extends Exception {
	
	private static final long serialVersionUID = 1L;
	
	/**
     * Costruisce una nuova eccezione {@code MossaNonValidaException}
     * con il messaggio di dettaglio specificato.
     *
     * @param messaggio il messaggio descrittivo dell'eccezione
     */
	public MossaNonValidaException(String messaggio) {
		super(messaggio);
	}

}