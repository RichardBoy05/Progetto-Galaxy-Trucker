package galaxytrucker.src.logic.gioco;

/**
 * Definisce i colori ricorrenti all'interno del gioco, utilizzati sia per identificare
 * univocamente i giocatori in una partita, sia per indicare il colore delle merci.
 * Ad ogni colore è anche associata la corrispondente stringa esadecimale.
 */
public enum Colore {

	ROSSO("#FF0000"),
	BLU("#0000FF"),
	GIALLO("#ECC100"),
	VERDE("#00AA00");

	private String hex;

	/**
	 * Costruisce un colore con il valore esadecimale associato.
	 *
	 * @param hex la rappresentazione esadecimale del colore.
	 */
	private Colore(String hex) {
		this.hex = hex;
	}

	/**
	 * Restituisce la rappresentazione esadecimale del colore.
	 *
	 * @return una stringa contenente il codice colore in esadecimale.
	 */
	public String toHex() {
		return hex;
	}
}