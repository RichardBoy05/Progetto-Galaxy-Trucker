package galaxytrucker.src.logic.volo;

import java.util.EnumMap;
import java.util.Map;

/**
 * Rappresenta un {@code Pianeta} visitabile durante il volo.
 * <p>
 * Ogni pianeta contiene una certa quantità di {@link Merce}, memorizzata in una mappa.
 * Un pianeta può essere occupato da un solo giocatore alla volta. La mappa delle merci
 * è immutabile verso l'esterno per evitare modifiche non controllate.
 * </p>
 */
public final class Pianeta {

	/** Mappa delle merci disponibili sul pianeta, con la relativa quantità. */
    private final Map<Merce, Integer> merci;

    /** Flag che indica se il pianeta è stato occupato da un giocatore. */
    private boolean occupato = false;

	/**
	 * Costruisce un oggetto {@code Pianeta} con una mappa di merci iniziali.
	 *
	 * @param merci mappa contenente le merci presenti sul pianeta e la loro quantità
	 */
	public Pianeta(Map<Merce, Integer> merci) {
	    this.merci = merci;
	}
	
	/**
	 * Verifica se il pianeta è già stato occupato da un giocatore.
	 *
	 * @return {@code true} se il pianeta è occupato, {@code false} altrimenti
	 */
	public boolean isOccupato() {
	    return occupato;
	}
	
	/**
	 * Imposta lo stato di occupazione del pianeta.
	 *
	 * @param occupato {@code true} se il pianeta viene occupato, {@code false} se viene liberato
	 */
	public void setOccupato(boolean occupato) {
	    this.occupato = occupato;
	}
	
	/**
	 * Restituisce una copia (shallow) della mappa.
	 *
	 * @return mappa delle merci.
	 */
	public Map<Merce, Integer> getMerci() {
	    return new EnumMap<>(merci);
	}
	
	/**
	 * Restituisce una rappresentazione testuale del pianeta e delle sue merci.
	 *
	 * @return descrizione del pianeta e delle merci disponibili
	 */
	@Override
	public String toString() {
	    return String.format("Pianeta: merci disponibili %s", merci);
	}
}