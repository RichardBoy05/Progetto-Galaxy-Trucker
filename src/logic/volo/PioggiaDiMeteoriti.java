package galaxytrucker.src.logic.volo;

import java.util.List;
import javax.swing.JOptionPane;
import galaxytrucker.src.logic.gioco.Giocatore;
import galaxytrucker.src.logic.gioco.Livello;
import galaxytrucker.src.view.dialogs.UsoBatterieDialog;
import galaxytrucker.src.view.frames.VoloGui;

/**
 * Evento: Pioggia di Meteoriti
 * <p>
 * Durante questo evento, una raffica di meteoriti (sia piccoli che grandi) colpisce le navi dei giocatori,
 * ognuno dei quali ha una specifica direzione di provenienza e posizione di impatto.
 * <p>
 * Regole di impatto:
 * <ul>
 *   <li><b>Meteoriti piccoli</b>: colpiscono una tessera solo se impattano un connettore esposto. 
 *       Se la nave ha uno scudo attivo nella direzione di provenienza, l'impatto viene annullato (consuma 1 batteria).</li>
 *   <li><b>Meteoriti grandi</b>: distruggono la tessera colpita a meno che non siano intercettati da un cannone attivo 
 *       rivolto nella direzione del meteorite (situato nella stessa riga/colonna o in quelle adiacenti, in base al livello della partita).</li>
 * </ul>
 */
public final class PioggiaDiMeteoriti extends Evento {
	
	/** Lista dei meteoriti che colpiranno le navi. */
	private final List<Meteorite> meteoriti;

	 /**
     * Costruisce una nuova istanza dell'evento {@code PioggiaDiMeteoriti}.
     *
     * @param giocatori     la lista dei giocatori coinvolti nel volo
     * @param livello       il livello attuale della partita
     * @param meteoriti     la lista di meteoriti che colpiranno le navi
     * @param pathImmagine  il percorso all'immagine che rappresenta l'evento
     * @throws NullPointerException se {@code meteoriti} è {@code null}
     */
	public PioggiaDiMeteoriti(List<Giocatore> giocatori, Livello livello, List<Meteorite> meteoriti, String pathImmagine) {
		super(giocatori, livello, pathImmagine);
		
		if (meteoriti == null) {
    		String errore = "Il parametro 'meteoriti' non può essere null!";
    		getLogger().error(errore);
    		throw new NullPointerException(errore);
    		}
		
		this.meteoriti = meteoriti;
	}

	/**
     * Avvia l'interfaccia grafica per mostrare l'evento al giocatore.
     */
	@Override
	public void esegui() {
		  new VoloGui(this).mostraEAttendi();
	}

	/**
     * Esegue la logica dell'evento dopo che l'interfaccia è stata mostrata.
     * <p>
     * Per ogni meteorite nella lista, viene mostrato un messaggio informativo. Poi, per ogni
     * giocatore ancora in volo:
     * <ul>
     *   <li>Se ha batterie e componenti attivabili, può scegliere se usare uno scudo tramite un dialogo.</li>
     *   <li>Il meteorite impatta sulla sua nave, e vengono calcolati gli effetti.</li>
     * </ul>
     *
     * @param gui l'interfaccia grafica del volo attiva al momento dell'evento
     */
	@Override
	public void avviaLogica(VoloGui gui) {
		
		for(Meteorite meteorite: meteoriti) {
			
			JOptionPane.showMessageDialog(
    	            gui.getFrame(),
    	            "Meteorite " + meteorite.getDimensione() + " in arrivo da "+meteorite.getDirezioneProvenienza()+" in posizione "+meteorite.getPosizione()+"!",
    	            "Pioggia di meteoriti",
    	            JOptionPane.INFORMATION_MESSAGE
    	        );
			
			for(Giocatore giocatore:getGiocatori()) {
				if(!giocatore.isInVolo()) continue;

				if(giocatore.getNave().getNumeroBatterie() >0 && giocatore.getNave().haComponentiAttivabili()) new UsoBatterieDialog(gui.getFrame(),giocatore);
				
				String messaggioEsito = meteorite.gestisciImpatto(giocatore);
				JOptionPane.showMessageDialog(
	    	            gui.getFrame(),
	    	            messaggioEsito,
	    	            "Pioggia di meteoriti",
	    	            JOptionPane.INFORMATION_MESSAGE
	    	        );
			}
			
			resetComponentiAttivabili();
		}
	}
	
	/**
     * Restituisce una descrizione HTML dell'evento da mostrare all'utente.
     * <p>
     * Include dettagli su come i meteoriti interagiscono con le navi dei giocatori
     * e come si possono difendere.
     *
     * @return la descrizione formattata dell'evento in formato HTML
     */
	@Override
	public String toString() {
	    return "<html><div style='text-align:justify;'>"
	         + "Tutti i giocatori affrontano i <b>meteoriti</b> indicati sulla carta, dall’alto verso il basso.<br>"
	         + "Ogni colpo coinvolge tutti i giocatori e giunge dalla direzione raffigurata ma in una posizione casuale.<br>"
	         + "I <b>piccoli meteoriti</b> danneggiano solo <b>connettori esposti</b>, bloccabili da <b>scudi</b> (1 batteria).<br>"
	         + "I <b>grossi meteoriti</b> ignorano gli scudi e si distruggono solo con <b>cannoni</b> puntati nella direzione giusta.<br>"
	         + "<b>Cannoni doppi</b> richiedono 1 batteria. Se non bloccati, i componenti colpiti vengono <b>distrutti</b>."
	         + "</div></html>";
	}

}