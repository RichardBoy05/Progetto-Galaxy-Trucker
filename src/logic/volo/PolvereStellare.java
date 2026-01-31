package galaxytrucker.src.logic.volo;

import java.util.List;
import galaxytrucker.src.logic.gioco.Giocatore;
import galaxytrucker.src.logic.gioco.Livello;
import galaxytrucker.src.view.frames.VoloGui;

/**
 * Evento speciale {@code PolvereStellare} 
 * <p>
 * Questo evento rappresenta una nube di polvere stellare che rallenta le navi
 * dei giocatori. Ogni giocatore perde 1 giorno di volo per ogni connettore esposto
 * presente sulla propria nave, indipendentemente dal tipo (singolo, doppio o universale).
 * L'effetto viene applicato in ordine inverso di rotta, iniziando dall'ultimo giocatore.
 * </p>
 *
 * <p>
 * L'evento mostra prima una finestra grafica con la descrizione tramite {@link VoloGui},
 * e poi applica l'effetto nel metodo {@code avviaLogica}.
 * </p>
 *
 * @author 
 */
public final class PolvereStellare extends Evento {

	 /**
     * Costruisce un evento {@code PolvereStellare} con i giocatori coinvolti,
     * il livello attuale e il percorso all'immagine associata.
     *
     * @param giocatori    la lista dei giocatori coinvolti nel volo
     * @param livello      il livello della partita
     * @param pathImmagine il percorso all'immagine rappresentativa dell'evento
     */
	public PolvereStellare(List<Giocatore> giocatori, Livello livello, String pathImmagine) {
        super(giocatori, livello, pathImmagine);
    }

	/**
     * Mostra la GUI dell'evento e attende la conferma dell'utente.
     */
    @Override
    public void esegui() {
        new VoloGui(this).mostraEAttendi();
    }

    /**
     * Applica la logica dell'evento: ogni giocatore ancora in volo perde
     * un numero di giorni di volo pari al numero di connettori esposti sulla propria nave.
     * L'effetto si applica in ordine inverso di rotta.
     *
     * @param gui la GUI del volo in cui visualizzare le informazioni
     */
    @Override
    public void avviaLogica(VoloGui gui) {
    	for(int i=getGiocatori().size()-1; i>=0; i--) {
			if(!getGiocatori().get(i).isInVolo()) continue;

			getGiocatori().get(i).perdiGiorniDiVolo(getGiocatori().get(i).getNave().getConnettoriEsposti(), getGiocatori());
		}
    }
    
    /**
     * Restituisce la descrizione HTML dell'evento per essere visualizzata nella GUI.
     *
     * @return una stringa HTML descrittiva dell'evento
     */
    @Override
    public String toString() {
        return "<html><div style='text-align:justify;'>"
             + "Una <b>nube di polvere stellare</b> investe le navi, rallentandone l’avanzata.<br>"
             + "Ogni giocatore perde <b>1 giorno di volo</b> per ogni <b>connettore esposto</b> sulla propria nave.<br>"
             + "(Non importa se singolo, doppio o universale: ogni connettore conta una sola volta).<br>"
             + "In <b>ordine inverso di rotta</b>, a partire dall’ultimo, ciascuno conta i connettori esposti<br>"
             + "e arretra il proprio razzo di un numero di spazi vuoti pari."
             + "</div></html>";
    }
}
