package galaxytrucker.src.logic.volo;

import java.util.List;

import javax.swing.JOptionPane;

import galaxytrucker.src.logic.gioco.Giocatore;
import galaxytrucker.src.logic.gioco.Livello;
import galaxytrucker.src.view.dialogs.UsoBatterieDialog;
import galaxytrucker.src.view.frames.VoloGui;

/**
 * Evento "Spazio Aperto"
 * <p>
 * Durante questo evento, a partire dal leader e proseguendo in ordine di rotta,
 * ogni {@link Giocatore} dichiara la propria potenza motrice e può decidere se utilizzare
 * segnalini batteria per attivare eventuali motori doppi.
 * <p>
 * Successivamente, ogni giocatore avanza il proprio razzo di un numero di spazi
 * vuoti pari alla potenza motrice disponibile, ignorando gli spazi già occupati da altre navi.
 * <p>
 * Se un giocatore ha potenza motrice pari a 0 e non si trova al livello P,
 * viene automaticamente escluso dal volo.
 *
 * @see Evento
 * @see Giocatore
 * @see Livello
 * @see VoloGui
 */
public final class SpazioAperto extends Evento {

	/**
     * Costruttore della classe SpazioAperto.
     *
     * @param giocatori     la lista dei giocatori coinvolti nel volo
     * @param livello       il livello della partita
     * @param pathImmagine  il percorso dell'immagine da mostrare per l'evento
     */
	public SpazioAperto(List<Giocatore> giocatori, Livello livello, String pathImmagine) {
		super(giocatori, livello, pathImmagine);
	}
	
	/**
     * Avvia l'interfaccia grafica dell'evento per permettere all'utente
     * di visualizzare le informazioni prima dell'avvio della logica.
     */
	@Override
	public void esegui() {
		new VoloGui(this).mostraEAttendi();
	}

	/**
     * Logica principale dell'evento:
     * - Ogni giocatore decide se attivare motori doppi tramite uso di batterie.
     * - Se la potenza motrice è 0 e il livello non è P, il giocatore viene escluso dal volo.
     * - Il giocatore avanza in base alla potenza motrice calcolata.
     *
     * @param gui l'interfaccia grafica associata all'evento
     */
	@Override
	public void avviaLogica(VoloGui gui) {
		
		for(Giocatore giocatore:getGiocatori()) {
			
			if(!giocatore.isInVolo()) continue;
			
			// Permette al giocatore di usare batterie per attivare motori doppi
			if(giocatore.getNave().getNumeroBatterie() >0 && giocatore.getNave().haComponentiAttivabili()) new UsoBatterieDialog(gui.getFrame(),giocatore);
			
			// Controllo potenza motrice e rimozione dal volo se necessaria
			if(giocatore.getNave().getPotenzaMotrice()==0 && getLivello()!=Livello.P) {
				giocatore.setInVolo(false);
				JOptionPane.showMessageDialog(
	    	            gui.getFrame(),
	    	            "Il giocatore "+giocatore.getColore()+" non è più in volo perché la sua nave ha potenza motrice nulla in spazio aperto!",
	    	            "Fine volo",
	    	            JOptionPane.INFORMATION_MESSAGE
	    	        );	
				continue;
			}
			
			giocatore.guadagnaGiorniDiVolo(giocatore.getNave().getPotenzaMotrice(), getGiocatori());

			if(getLivello()==Livello.P && giocatore.getNave().getPotenzaMotrice()==0) {
			
				JOptionPane.showMessageDialog(
					    gui.getFrame(),
					    "Il giocatore " + giocatore.getColore() + " non avanza di giorni di volo perché ha potenza motrice zero!",
					    "Spazio aperto",
					    JOptionPane.INFORMATION_MESSAGE
				);	
			} else {
				
				JOptionPane.showMessageDialog(
					    gui.getFrame(),
					    "Il giocatore " + giocatore.getColore() + " avanza di "+giocatore.getNave().getPotenzaMotrice()+" giorni di volo!",
					    "Spazio aperto",
					    JOptionPane.INFORMATION_MESSAGE
				);	
			}
			
		}
	}
	
	/**
     * Restituisce una descrizione HTML dell'evento per l'interfaccia utente.
     *
     * @return una descrizione formattata in HTML dell'effetto dell'evento
     */
	@Override
	public String toString() {
	    return "<html><div style='text-align:justify;'>"
	         + "Ogni <b>giocatore</b>, in ordine di rotta, dichiara la propria <b>potenza motrice</b>.<br>"
	         + "Può usare <b>batterie</b> per attivare motori doppi, se presenti.<br>"
	         + "Il razzo avanza di tanti <b>spazi vuoti</b> quanto la potenza, saltando quelli occupati.<br>"
	         + "Chi avanza di più può <b>superare</b> gli altri e diventare il <b>nuovo leader</b>."
	         + "</div></html>";
	}
}
