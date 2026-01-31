package galaxytrucker.src.logic.volo;

import java.util.List;

import javax.swing.JOptionPane;

import galaxytrucker.src.logic.gioco.Giocatore;
import galaxytrucker.src.logic.gioco.Livello;
import galaxytrucker.src.view.dialogs.InterazioniUtenteDialog;
import galaxytrucker.src.view.dialogs.SceltaEquipaggioDialog;
import galaxytrucker.src.view.frames.VoloGui;

/**
 * Rappresenta l'evento di una {@code NaveAbbandonata} durante il volo.
 * <p>
 * In ordine di rotta, ogni giocatore ha la possibilità (una sola in totale per l'intero gruppo)
 * di accettare un'offerta: ottenere un certo numero di crediti cosmici in cambio della perdita
 * di membri dell’equipaggio e di giorni di volo. L'opportunità viene proposta solo a giocatori
 * ancora in volo e con equipaggio sufficiente, e può essere colta da un solo giocatore.
 * </p>
 */
public final class NaveAbbandonata extends Evento {
	
	 /** Numero di crediti offerti in cambio dell'equipaggio e dei giorni di volo. */
    private final int crediti;

    /** Numero di membri dell'equipaggio da eliminare se si accetta l'offerta. */
    private final int equipaggioDaEliminare;

    /** Numero di giorni di volo da perdere se si accetta l'offerta. */
    private final int giorniVolo;
	
    /**
     * Costruisce un evento {@code NaveAbbandonata}.
     *
     * @param giocatori             la lista dei giocatori coinvolti nel volo
     * @param livello               il livello della partita
     * @param crediti               il numero di crediti offerti
     * @param equipaggioDaEliminare il numero di membri dell'equipaggio da sacrificare
     * @param giorniVolo            i giorni di volo da perdere
     * @param pathImmagine          il percorso dell'immagine associata all'evento
     */
	public NaveAbbandonata(List<Giocatore> giocatori, Livello livello, int crediti, int equipaggioDaEliminare, int giorniVolo, String pathImmagine) {
		super(giocatori, livello, pathImmagine);
		this.crediti=crediti;
		this.equipaggioDaEliminare=equipaggioDaEliminare;
		this.giorniVolo=giorniVolo;
	}

	/**
     * Avvia l'interfaccia grafica dell'evento e attende l'interazione con l'utente.
     */
	@Override
	public void esegui() {
		new VoloGui(this).mostraEAttendi();			
	}

	/**
     * Avvia la logica dell'evento: in ordine di rotta, ogni giocatore può scegliere
     * se accettare l'offerta (se ha abbastanza equipaggio e se è ancora in volo).
     * Solo il primo che accetta potrà usufruirne.
     *
     * @param gui l'interfaccia grafica del volo
     */
	@Override
	public void avviaLogica(VoloGui gui) {
		boolean scelta;
		boolean equipaggioSufficiente=false;

		for(Giocatore giocatore: getGiocatori()) {
			if(!giocatore.isInVolo()) continue;

			if(giocatore.getNave().getTotAbitanti()<equipaggioDaEliminare) continue;
			
			equipaggioSufficiente=true;
			
			InterazioniUtenteDialog dialog = new InterazioniUtenteDialog(gui.getFrame(), giocatore);
			scelta=dialog.getSceltaSiNo();
			if(!scelta) continue;
			
			giocatore.aggiungiCrediti(crediti);
			giocatore.perdiGiorniDiVolo(giorniVolo, getGiocatori());
			new SceltaEquipaggioDialog (gui.getFrame(), giocatore, equipaggioDaEliminare);
			break;
		} 
		
		if (!equipaggioSufficiente) {
	        JOptionPane.showMessageDialog(
	            gui.getFrame(),
	            "Nessuna nave ha equipaggio a sufficienza per attraccare!",
	            "Stazione Abbandonata",
	            JOptionPane.INFORMATION_MESSAGE
	        );
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
	         + "Solo un <b>giocatore</b> può approfittare della <b>nave abbandonata</b>, a partire dal <b>leader</b>.<br>"
	         + "Chi accetta perde un certo numero di <b>membri dell’equipaggio</b> in cambio di <b>crediti cosmici</b>, <br> "
	         + "ma deve anche arretrare sulla rotta per i <b>giorni indicati</b>.<br>"
	         + "Una volta scelta, l’<b>opportunità</b> non è più disponibile per gli altri."
	         + "</div></html>";
	}
}