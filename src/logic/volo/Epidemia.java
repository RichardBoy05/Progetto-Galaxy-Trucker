package galaxytrucker.src.logic.volo;

import java.util.List;
import java.util.Map;

import javax.swing.JOptionPane;

import galaxytrucker.src.view.dialogs.InterazioniUtenteDialog;

import galaxytrucker.src.logic.assemblaggio.Cabina;
import galaxytrucker.src.logic.assemblaggio.Cella;
import galaxytrucker.src.logic.assemblaggio.Direzione;
import galaxytrucker.src.logic.assemblaggio.Griglia;
import galaxytrucker.src.logic.assemblaggio.Tessera;
import galaxytrucker.src.logic.gioco.Giocatore;
import galaxytrucker.src.logic.gioco.Livello;
import galaxytrucker.src.view.frames.VoloGui;

/**
 * Evento "Epidemia" che simula la diffusione di una malattia tra le cabine di una nave spaziale.
 * <p>
 * Durante questo evento, per ogni giocatore in volo, viene controllata ogni cabina occupata della sua nave.
 * Se una cabina è collegata ad almeno un'altra cabina anch'essa occupata, viene attivata un'interazione
 * con l'utente che consente di rimuovere un membro dell'equipaggio da quella cabina.
 * Se nessuna cabina è a rischio, viene mostrato un messaggio informativo.
 */
public final class Epidemia extends Evento {

	/**
     * Costruttore dell'evento Epidemia.
     *
     * @param giocatori     lista dei giocatori partecipanti.
     * @param livello       livello della partita.
     * @param pathImmagine  percorso dell'immagine rappresentativa dell'evento.
     */
	public Epidemia(List<Giocatore> giocatori, Livello livello, String pathImmagine) {
		super(giocatori, livello, pathImmagine);
	}

	/**
     * Avvia la GUI per visualizzare l'evento e attendere che venga completato.
     */
	@Override
    public void esegui() {
        new VoloGui(this).mostraEAttendi();
    }

	/**
     * Logica dell'evento "Epidemia". Per ogni giocatore in volo, controlla tutte le cabine
     * della nave. Se una cabina occupata è adiacente a un'altra cabina anch'essa occupata,
     * viene aperto un dialogo per consentire all'utente di gestire la rimozione di un membro
     * dell'equipaggio.
     * <p>
     * Se nessuna cabina è a rischio, viene mostrato un messaggio che lo indica.
     *
     * @param gui interfaccia grafica del volo, utilizzata per interazioni con l'utente.
     */
	@Override
	public void avviaLogica(VoloGui gui) {
		boolean almenoUnaCabinaRischio = false;

	    for (Giocatore giocatore : getGiocatori()) {
			if(!giocatore.isInVolo()) continue;

	        Griglia griglia = giocatore.getNave().getGriglia();

	        for (int riga = 0; riga < griglia.getAltezza(); riga++) {
	            for (int colonna = 0; colonna < griglia.getLarghezza(); colonna++) {
	                Cella cella = griglia.getCella(riga, colonna);
	                if(cella==null) continue;
	                Tessera tessera = cella.getTessera();

	                if (tessera != null && tessera.accettaAstronauta()) {
	                    Cabina cabina = (Cabina) tessera; //casting sicuro per il controllo fatto in precedenza
                        Map<Direzione, Tessera> adiacenti = griglia.getTessereAdiacenti(cella);
                        
                        if (cabina.pericoloEpidemia(adiacenti)) {
                        	almenoUnaCabinaRischio = true;
                            new InterazioniUtenteDialog(gui.getFrame(), giocatore, cella);
	                        }
	                }
	            }
	        }
	    }
	    if (!almenoUnaCabinaRischio) {
	        JOptionPane.showMessageDialog(
	            gui.getFrame(),
	            "Nessuna nave è a rischio di epidemia!",
	            "Epidemia",
	            JOptionPane.INFORMATION_MESSAGE
	        );
	    }
	}
	
	/**
	 * Restituisce una descrizione HTML formattata dell'evento "Epidemia",
	 * da visualizzare nella GUI. Il testo descrive l'effetto dell'epidemia
	 * e fornisce un consiglio tattico su come limitare i danni.
	 *
	 * @return una stringa HTML con la descrizione dell'evento.
	 */
	@Override
	public String toString() {
	    return "<html><div style='text-align:justify;'>"
	         + "Un'<b>epidemia</b> colpisce tutte le <b>navi</b>: rimuovi 1 membro dell'equipaggio "
	         + "da ogni <b>cabina</b> connessa a un'altra <b>cabina occupata</b>.<br>"
	         + "Per limitare i danni, evita <b>cabine adiacenti</b>.<br>"
	         + "Se svuoti una <b>cabina</b> prima dell’epidemia, puoi salvarla."
	         + "</div></html>";
	}
	
}