package galaxytrucker.src.logic.volo;

import java.util.List;
import java.util.Map;

import javax.swing.JOptionPane;

import galaxytrucker.src.logic.gioco.Giocatore;
import galaxytrucker.src.logic.gioco.Livello;
import galaxytrucker.src.view.dialogs.CaricoMerciDialog;
import galaxytrucker.src.view.dialogs.InterazioniUtenteDialog;
import galaxytrucker.src.view.frames.VoloGui;

/**
 * Evento "Stazione Abbandonata" 
 * <p>
 * Solo un {@link Giocatore} può attraccare alla stazione e raccogliere le {@link Merce} presenti,
 * a condizione che possieda almeno il numero minimo di membri dell'equipaggio richiesti.
 * L'opportunità viene offerta in ordine di rotta.
 * <p>
 * Il giocatore che accetta di attraccare perde un certo numero di giorni di volo, ma ottiene le merci.
 * Nessun membro dell'equipaggio viene perso.
 *
 * @see Evento
 * @see Giocatore
 */

public final class StazioneAbbandonata extends Evento{
	
	private final int equipaggio;
    private final int giorniVolo;
    private final Map<Merce, Integer> merci;

    /**
     * Costruisce un evento Stazione Abbandonata.
     *
     * @param giocatori     la lista dei giocatori partecipanti
     * @param livello       il livello della partita
     * @param equipaggio    il numero minimo di membri dell'equipaggio richiesti per accedere alla stazione
     * @param giorniVolo    il numero di giorni di volo persi dal giocatore che accetta
     * @param merci         la mappa di merci disponibili da caricare
     * @param pathImmagine  il percorso dell'immagine dell'evento
     * @throws NullPointerException se {@code merci} è {@code null}
     */
    public StazioneAbbandonata(List<Giocatore> giocatori, Livello livello, int equipaggio, int giorniVolo, Map<Merce, Integer> merci, String pathImmagine) {
        super(giocatori, livello, pathImmagine);
        this.equipaggio = equipaggio;
        this.giorniVolo = giorniVolo;
        
        if (merci == null) {
    		String errore = "Il parametro 'merci' non può essere null!";
    		getLogger().error(errore);
    		throw new NullPointerException(errore);
    		}
        
        this.merci = merci;
    }

    /**
     * Mostra l'interfaccia grafica associata a questo evento prima dell'esecuzione della logica.
     */
    @Override
    public void esegui() {
    	new VoloGui(this).mostraEAttendi();	
    }

    /**
     * Esegue la logica dell'evento:
     * <ul>
     *   <li>In ordine di rotta, ogni giocatore attivo con equipaggio sufficiente può decidere se attraccare.</li>
     *   <li>Se accetta, perde {@code giorniVolo} giorni di volo e carica le {@code merci} indicate.</li>
     *   <li>Solo il primo giocatore che accetta può beneficiare dell'evento; il ciclo termina subito dopo.</li>
     * </ul>
     *
     * @param gui l'interfaccia grafica del volo
     */
	@Override
	public void avviaLogica(VoloGui gui) {
		boolean scelta;
		boolean equipaggioSufficiente=false;
		for(Giocatore giocatore: getGiocatori()) {
			if(!giocatore.isInVolo()) continue;

			if(giocatore.getNave().getTotAbitanti()<equipaggio) continue;
			
			equipaggioSufficiente=true;
			
			InterazioniUtenteDialog dialog = new InterazioniUtenteDialog(gui.getFrame(), giocatore);
			
			scelta=dialog.getSceltaSiNo();
			if(!scelta) continue;
			giocatore.perdiGiorniDiVolo(giorniVolo, getGiocatori());
			new CaricoMerciDialog (gui.getFrame(), giocatore, merci);
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
     * Restituisce la descrizione dell'evento in formato HTML per l'interfaccia utente.
     *
     * @return descrizione HTML dell'evento
     */
	@Override
	public String toString() {
	    return "<html><div style='text-align:justify;'>"
	         + "Solo <b>un giocatore</b> può attraccare alla stazione per raccogliere le <b>merci</b>,<br>"
	         + "se ha abbastanza <b>equipaggio</b>.<br>"
	         + "Il <b>leader</b> decide per primo; se rinuncia, si passa al <b>prossimo in rotta</b>.<br>"
	         + "Chi attracca prende le <b>merci indicate</b> e perde i <b>giorni di volo</b> specificati.<br>"
	         + "<b>Non</b> si perdono membri dell’equipaggio."
	         + "</div></html>";
	}
}
