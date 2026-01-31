package galaxytrucker.src.logic.volo;

import java.util.List;

import javax.swing.JOptionPane;

import galaxytrucker.src.logic.gioco.Giocatore;
import galaxytrucker.src.logic.gioco.Livello;
import galaxytrucker.src.view.dialogs.SceltaEquipaggioDialog;
import galaxytrucker.src.view.dialogs.UsoBatterieDialog;
import galaxytrucker.src.view.frames.VoloGui;

/**
 * Rappresenta l'evento "Schiavisti" durante il volo.
 * <p>
 * Ogni giocatore, in ordine di rotta, affronta un gruppo di schiavisti. 
 * Se il giocatore ha sufficiente potenza di fuoco per sconfiggerli, ottiene crediti cosmici
 * e perde un numero specifico di giorni di volo. Se viene sconfitto, deve perdere parte
 * del proprio equipaggio. Se un giocatore sconfigge gli schiavisti, gli altri non li affrontano.
 * </p>
 */
public final class Schiavisti extends Evento {

	 private final int crediti;                  // Crediti cosmici ottenibili
	    private final int equipaggioDaEliminare;    // Equipaggio da sacrificare in caso di sconfitta
	    private final int giorniVolo;               // Giorni di volo persi se si sconfiggono gli schiavisti
	    private final int potenzaNemico;            // Potenza degli schiavisti

    /**
     * Costruisce una nuova carta evento "Schiavisti".
     *
     * @param giocatori              lista dei giocatori in partita
     * @param livello                livello della partita
     * @param crediti                crediti ottenibili se si sconfiggono gli schiavisti
     * @param equipaggioDaEliminare equipaggio da sacrificare in caso di sconfitta
     * @param giorniVolo             giorni di volo persi se si vincono i crediti
     * @param potenzaNemico          potenza degli schiavisti
     * @param pathImmagine           percorso dell'immagine della carta
     */
    public Schiavisti(List<Giocatore> giocatori, Livello livello, int crediti, int equipaggioDaEliminare, int giorniVolo, int potenzaNemico, String pathImmagine) {
        super(giocatori, livello, pathImmagine);
        this.crediti= crediti;
        this.equipaggioDaEliminare = equipaggioDaEliminare;
        this.giorniVolo=giorniVolo;
        this.potenzaNemico=potenzaNemico;
    }
    
    /**
     * Mostra l'interfaccia grafica dell'evento e attende l'interazione dell'utente.
     */
    @Override
    public void esegui() {
        new VoloGui(this).mostraEAttendi();
    }

    /**
     * Avvia la logica dell'evento: ogni giocatore affronta gli schiavisti in ordine di rotta.
     * Se un giocatore li sconfigge, ottiene i crediti e perde giorni di volo;
     * se viene sconfitto, deve rinunciare a parte dell'equipaggio.
     * Il primo giocatore a vincere termina l'interazione per tutti.
     *
     * @param gui la GUI corrente del volo
     */
    @Override
    public void avviaLogica(VoloGui gui) {
   
    	for (Giocatore giocatore:getGiocatori()) {
			if(!giocatore.isInVolo()) continue;

			if(giocatore.getNave().getNumeroBatterie() >0 && giocatore.getNave().haComponentiAttivabili()) new UsoBatterieDialog(gui.getFrame(),giocatore);
    		
    		if(giocatore.getNave().getPotenzaDiFuoco()>potenzaNemico) {
    			
    			JOptionPane.showMessageDialog(
    		            gui.getFrame(),
    		            "Il giocatore "+giocatore.getColore()+" ha sconfitto gli schiavisti!",
    		            "Schiavisti",
    		            JOptionPane.INFORMATION_MESSAGE
    		        );
    			
                    giocatore.aggiungiCrediti(crediti);
                    giocatore.perdiGiorniDiVolo(giorniVolo, getGiocatori());
                // Schiavisti sconfitti, nessun altro li affronta
                break;
                
            } else if (giocatore.getNave().getPotenzaDiFuoco()==potenzaNemico){
            	JOptionPane.showMessageDialog(
    		            gui.getFrame(),
    		            "Il giocatore "+giocatore.getColore()+" ha pareggiato con gli schiavisti!",
    		            "Schiavisti",
    		            JOptionPane.INFORMATION_MESSAGE
    		        );
            	continue;
            } else {
            	JOptionPane.showMessageDialog(
    		            gui.getFrame(),
    		            "Il giocatore "+giocatore.getColore()+" è stato sconfitto dagli schiavisti,\n"
    		            +"pertanto dovrà cedere parte del proprio equipaggio!",
    		            "Schiavisti",
    		            JOptionPane.INFORMATION_MESSAGE
    		        );
            	
                // Giocatore sconfitto: deve perdere pedine equipaggio
            	if(giocatore.getNave().getTotAbitanti()<equipaggioDaEliminare){
            		new SceltaEquipaggioDialog (gui.getFrame(), giocatore, giocatore.getNave().getTotAbitanti());
            	} else new SceltaEquipaggioDialog (gui.getFrame(), giocatore, equipaggioDaEliminare);
    			
            }
		}
	}
    
    /**
     * Restituisce la descrizione testuale dell’evento, in formato HTML per la GUI.
     *
     * @return descrizione HTML dell’evento Schiavisti.
     */
    @Override
    public String toString() {
        return "<html><div style='text-align:justify;'>"
             + "Gli <b>Schiavisti</b> attaccano i giocatori in ordine di <b>rotta</b>.<br>"
             + "Se li sconfiggi, ottieni i <b>crediti cosmici</b> indicati e perdi <b>giorni di volo</b>.<br>"
             + "Se vieni sconfitto, devi rinunciare a un certo numero di <b>pedine equipaggio</b> "
             + "(umani o alieni, a tua scelta).<br>"
             + "Dopo che qualcuno li ha sconfitti, nessun altro può <b>affrontarli</b>."
             + "</div></html>";
    }
    
}