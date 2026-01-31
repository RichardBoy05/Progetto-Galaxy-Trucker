package galaxytrucker.src.logic.volo;

import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;
import galaxytrucker.src.logic.gioco.Giocatore;
import galaxytrucker.src.logic.gioco.Livello;
import galaxytrucker.src.view.dialogs.UsoBatterieDialog;
import galaxytrucker.src.view.frames.VoloGui;

/**
 * Evento che rappresenta un attacco da parte dei <b>Pirati spaziali</b>.
 * <p>
 * I giocatori li affrontano in ordine di rotta. Se un giocatore ha una potenza di fuoco superiore
 * a quella dei pirati, li sconfigge, ottiene crediti cosmici e perde giorni di volo.
 * Se invece è sconfitto, subisce un bombardamento da parte dei pirati.
 */
public final class Pirati extends Evento {

	/** Potenza d'attacco dei pirati. */
    private final int potenzaNemico;

    /** Giorni di volo persi se i pirati vengono sconfitti. */
    private final int giorniVolo;

    /** Crediti cosmici ottenuti in caso di vittoria. */
    private final int crediti;

    /** Lista delle cannonate subite dai giocatori sconfitti. */
    private final List<Cannonata> cannonate;

    /**
     * Costruisce un evento {@code Pirati} con i parametri specificati.
     *
     * @param giocatori     lista dei giocatori presenti nel volo
     * @param livello       il livello della partita
     * @param potenzaNemico potenza d'attacco dei pirati
     * @param giorniVolo    giorni di volo persi in caso di vittoria
     * @param crediti       crediti cosmici guadagnati in caso di vittoria
     * @param cannonate     lista delle cannonate subite dai giocatori sconfitti
     * @param pathImmagine  percorso all'immagine dell'evento
     * @throws NullPointerException se {@code cannonate} è {@code null}
     */
    public Pirati(List<Giocatore> giocatori, Livello livello, int potenzaNemico, int giorniVolo, int crediti, List<Cannonata> cannonate, String pathImmagine) {
        super(giocatori, livello, pathImmagine);
        this.potenzaNemico = potenzaNemico;
        this.giorniVolo = giorniVolo;
        this.crediti = crediti;
        
        if (cannonate == null) {
    		String errore = "Il parametro 'cannonate' non può essere null!";
    		getLogger().error(errore);
    		throw new NullPointerException(errore);
    	}
        
        this.cannonate = cannonate;
    }
    
    /**
     * Mostra l'interfaccia grafica dell'evento {@code Pirati}.
     */
    @Override
    public void esegui() {
        new VoloGui(this).mostraEAttendi();
    }
    
    /**
     * Esegue la logica dell'evento {@code Pirati}.
     * <p>
     * I giocatori affrontano i pirati uno alla volta:
     * <ul>
     *   <li>Se la loro potenza di fuoco è maggiore di quella dei pirati, li sconfiggono e ottengono crediti e perdono giorni di volo.</li>
     *   <li>Se pari, non accade nulla.</li>
     *   <li>Se inferiore, vengono aggiunti alla lista dei giocatori sconfitti.</li>
     * </ul>
     * I giocatori sconfitti subiscono le cannonate indicate.
     *
     * @param gui l'interfaccia grafica del volo in corso.
     */
    @Override
    public void avviaLogica(VoloGui gui) {
    	
        List<Giocatore> giocatoriSconfitti = new ArrayList<>();

        for (Giocatore giocatore : getGiocatori()) {
            if (!giocatore.isInVolo()) continue;

            if (giocatore.getNave().getNumeroBatterie() > 0 && giocatore.getNave().haComponentiAttivabili()) {
                new UsoBatterieDialog(gui.getFrame(), giocatore);
            }

            if (giocatore.getNave().getPotenzaDiFuoco() > potenzaNemico) {
                giocatore.aggiungiCrediti(crediti);
                giocatore.perdiGiorniDiVolo(giorniVolo, getGiocatori());
                
                JOptionPane.showMessageDialog(
        	            gui.getFrame(),
        	            "Il giocatore " + giocatore.getColore() + " ha sconfitto i pirati e ottenuto la ricompensa!",
        	            "Pirati",
        	            JOptionPane.INFORMATION_MESSAGE
        	        );
                
                return; // nemico sconfitto: evento finito
                
            } else if (giocatore.getNave().getPotenzaDiFuoco() == potenzaNemico) {
            	
            	JOptionPane.showMessageDialog(
        	            gui.getFrame(),
        	            "Il giocatore " + giocatore.getColore() + " ha pareggiato con i pirati, per cui è salvo. Avanti il prossimo!",
        	            "Pirati",
        	            JOptionPane.INFORMATION_MESSAGE
        	        );
            	
            	continue; // se pari potenza, non succede nulla
            } else {
            	
            	JOptionPane.showMessageDialog(
        	            gui.getFrame(),
        	            "Il giocatore " + giocatore.getColore() + " è stato sconfitto dai pirati! Al termine dell'evento, dovrà affrontare le loro cannonate!",
        	            "Pirati",
        	            JOptionPane.INFORMATION_MESSAGE
        	        );
            	giocatoriSconfitti.add(giocatore);
            }
            
        }

        if (!giocatoriSconfitti.isEmpty()) {   	
            affrontaCannonate(giocatoriSconfitti, gui);
            
        } else {
        	
        	JOptionPane.showMessageDialog(
    	            gui.getFrame(),
    	            "Nessun giocatore è stato sconfitto!",
    	            "Pirati",
    	            JOptionPane.INFORMATION_MESSAGE
    	        );
        }
        
    }
    
    /**
     * Gestisce la fase in cui i giocatori sconfitti affrontano le cannonate dei pirati.
     * <p>
     * Per ogni cannonata presente, viene mostrato un messaggio che indica la dimensione,
     * la direzione e la posizione dell'impatto. Ogni giocatore sconfitto ha la possibilità 
     * di attivare le proprie batterie, se disponibili, tramite una finestra di dialogo 
     * {@link UsoBatterieDialog}. Dopo il tentativo di difesa, l'impatto viene gestito e 
     * viene mostrato l'esito. Al termine di ogni cannonata, i componenti attivabili vengono 
     * resettati per prepararsi all'attacco successivo.
     * </p>
     *
     * @param giocatoriSconfitti la lista dei giocatori che sono stati sconfitti nel combattimento con i pirati.
     * @param gui la GUI di gioco necessaria per accedere al frame principale e mostrare i dialoghi.
     */
    private void affrontaCannonate(List<Giocatore> giocatoriSconfitti, VoloGui gui) {
    	
    	JOptionPane.showMessageDialog(
	            gui.getFrame(),
	            "Adesso i giocatori sconfitti dovranno affrontare le cannonate dei pirati!",
	            "Pirati",
	            JOptionPane.INFORMATION_MESSAGE
	        );
    	
    	for(Cannonata cannonata: cannonate) {
			
			JOptionPane.showMessageDialog(
    	            gui.getFrame(),
    	            "Cannonata " + ((cannonata.getDimensione() == DimensioneColpo.PICCOLO) ? "PICCOLA" : "GRANDE") + " in arrivo da "+ cannonata.getDirezioneProvenienza()+" in posizione "+ cannonata.getPosizione()+"!",
    	            "Pirati",
    	            JOptionPane.INFORMATION_MESSAGE
    	        );
			
			for(Giocatore giocatore:giocatoriSconfitti) {

				if(giocatore.getNave().getNumeroBatterie() > 0 && giocatore.getNave().haComponentiAttivabili() && cannonata.getDimensione() != DimensioneColpo.GRANDE) new UsoBatterieDialog(gui.getFrame(),giocatore);
				
				String messaggioEsito = cannonata.gestisciImpatto(giocatore);
				JOptionPane.showMessageDialog(
	    	            gui.getFrame(),
	    	            messaggioEsito,
	    	            "Pirati",
	    	            JOptionPane.INFORMATION_MESSAGE
	    	        );
			}
			
			resetComponentiAttivabili();
		}
    	
    }
    
    /**
     * Restituisce una descrizione HTML formattata dell'effetto della carta evento.
     *
     * @return una stringa HTML descrittiva dell'evento.
     */
    @Override
    public String toString() {
        return "<html><div style='text-align:justify;'>"
             + "I <b>Pirati</b> attaccano in ordine di rotta.<br>"
             + "Se li sconfiggi, ottieni <b>crediti cosmici</b> e perdi <b>giorni di volo</b>.<br>"
             + "Se vieni sconfitto, la tua nave subisce <b>cannonate</b>:<br>"
             + "la carta indica <b>grandezza</b> e <b>direzione</b> dell'attacco.<br>"
             + "Ogni colpo giunge in una posizione casuale: il risultato <br>"
             + "si applica a <b>tutti i giocatori sconfitti</b>.<br>"
             + "Qualora i pirati venissero sconfitti, l'evento <b>terminerà</b>."
             + "</div></html>";
	    }
    
}