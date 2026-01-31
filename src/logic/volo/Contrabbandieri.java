package galaxytrucker.src.logic.volo;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.swing.JOptionPane;

import galaxytrucker.src.logic.assemblaggio.Cella;
import galaxytrucker.src.logic.assemblaggio.Griglia;
import galaxytrucker.src.logic.assemblaggio.Stiva;
import galaxytrucker.src.logic.assemblaggio.Tessera;
import galaxytrucker.src.logic.assemblaggio.VanoBatteria;
import galaxytrucker.src.logic.eccezioni.CaricamentoMerceNonValidoException;
import galaxytrucker.src.logic.eccezioni.RichiestaBatterieNonValidaException;
import galaxytrucker.src.logic.gioco.GameLogger;
import galaxytrucker.src.logic.gioco.Giocatore;
import galaxytrucker.src.logic.gioco.Livello;
import galaxytrucker.src.view.dialogs.CaricoMerciDialog;
import galaxytrucker.src.view.dialogs.UsoBatterieDialog;
import galaxytrucker.src.view.frames.VoloGui;

/**
 * Evento che rappresenta l'incontro con i Contrabbandieri durante il volo.
 * <p>
 * Se il giocatore sconfigge i contrabbandieri ottiene delle merci,
 * altrimenti perde una certa quantità di merci e giorni di volo.
 */
public final class Contrabbandieri extends Evento {
	
	/** Logger per la classe. */
    private static final GameLogger LOGGER = GameLogger.getInstance();

    /** Potenza dei contrabbandieri da superare per vincere lo scontro. */
    private final int potenzaNemico;

    /** Giorni di volo persi in caso di vittoria (penalità). */
    private final int giorniVolo;

    /** Mappa delle merci ottenute in caso di vittoria (tipo e quantità). */
    private final Map<Merce, Integer> merci;

    /** Numero di merci perse in caso di sconfitta. */
    private final int merciPerse;

    /**
     * Costruisce un nuovo evento Contrabbandieri.
     *
     * @param giocatori     lista dei giocatori coinvolti
     * @param livello       livello della partita
     * @param potenzaNemico potenza dei contrabbandieri da superare
     * @param giorniVolo    giorni di volo persi in caso di vittoria
     * @param merci         merci guadagnate in caso di vittoria (non null)
     * @param merciPerse    quantità di merci perse in caso di sconfitta
     * @param pathImmagine  percorso dell'immagine associata all'evento
     * @throws NullPointerException se il parametro {@code merci} è null
     */
    public Contrabbandieri(List<Giocatore> giocatori, Livello livello, int potenzaNemico, int giorniVolo, Map<Merce, Integer> merci, 
    		int merciPerse, String pathImmagine) {
    	super(giocatori, livello, pathImmagine);
    	if (merci == null) {
    		String errore = "Il parametro 'merci' non può essere null!";
    		LOGGER.error(errore);
    		throw new NullPointerException(errore);
    		}
    	this.potenzaNemico = potenzaNemico;
    	this.giorniVolo = giorniVolo;
    	this.merci = merci;
    	this.merciPerse = merciPerse;
    	}

    /**
     * Mostra la GUI dell'evento e attende l'interazione con l'utente.
     */
	@Override
    public void esegui() {
        new VoloGui(this).mostraEAttendi();
        }
	
	/**
	 * Esegue la logica dell'evento Contrabbandieri per ogni giocatore in volo.
	 * <p>
	 * - Se la potenza di fuoco del giocatore è maggiore di quella dei contrabbandieri,
	 *   riceve le merci e perde giorni di volo, se ha spazio nella stiva.<br>
	 * - Se la potenza è uguale, il turno termina senza effetti.<br>
	 * - Se la potenza è minore, il giocatore perde un numero di merci 
	 *   secondo un ordine di valore predefinito (rossa > gialla > verde > blu > batterie).
	 *
	 * @param gui l'interfaccia grafica del volo, usata per mostrare dialoghi e interagire con l'utente
	 */
	@Override
	public void avviaLogica(VoloGui gui) {
		for (Giocatore giocatore:getGiocatori()) {
			if(!giocatore.isInVolo()) continue;
			if(giocatore.getNave().getNumeroBatterie() >0 && giocatore.getNave().haComponentiAttivabili()) new UsoBatterieDialog(gui.getFrame(),giocatore);
			
    		if(giocatore.getNave().getPotenzaDiFuoco()>potenzaNemico) {
    			
    			JOptionPane.showMessageDialog(
        	            gui.getFrame(),
        	            "Il giocatore "+giocatore.getColore()+" ha sconfitto i contrabbandieri!",
        	            "Contrabbandieri",
        	            JOptionPane.INFORMATION_MESSAGE
        	        );
                
                if(verificaPresenzaStiva(giocatore.getNave())) {
                	new CaricoMerciDialog(gui.getFrame(), giocatore, merci);
                	giocatore.perdiGiorniDiVolo(giorniVolo, getGiocatori());
                } else {
                	JOptionPane.showMessageDialog(
            	            gui.getFrame(),
            	            "Il giocatore "+giocatore.getColore()+" non ha alcuna stiva per ricevere la ricompensa!",
            	            "Contrabbandieri",
            	            JOptionPane.INFORMATION_MESSAGE
            	        );
                }
                break;
                
            } else if (giocatore.getNave().getPotenzaDiFuoco()==potenzaNemico){
            	JOptionPane.showMessageDialog(
        	            gui.getFrame(),
        	            "Il giocatore "+giocatore.getColore()+" ha pareggiato!",
        	            "Contrabbandieri",
        	            JOptionPane.INFORMATION_MESSAGE
        	        );
            	continue; //Non succede niente, si passa al prossimo utente
            	
            } else {
    			int contMerceEliminate=0;
    			
    			JOptionPane.showMessageDialog(
        	            gui.getFrame(),
        	            "Il giocatore "+giocatore.getColore()+" è stato sconfitto!",
        	            "Contrabbandieri",
        	            JOptionPane.INFORMATION_MESSAGE
        	        );
    			
            	List <Merce> merceInOrdineDiValore=new ArrayList<>();
            	
            	merceInOrdineDiValore.add(Merce.ROSSA);
            	merceInOrdineDiValore.add(Merce.GIALLA);
            	merceInOrdineDiValore.add(Merce.VERDE);
            	merceInOrdineDiValore.add(Merce.BLU);
            	merceInOrdineDiValore.add(null);
            	
            	for(int i=0; i<merceInOrdineDiValore.size(); i++) {
            		if(contMerceEliminate==merciPerse) return;
            		contMerceEliminate+=eliminaMerce(merceInOrdineDiValore.get(i), giocatore);
            	}
            }
		}
		}
	
	/**
	 * Rimuove un'unità della merce specificata dalla nave del giocatore.
	 * <p>
	 * Se {@code merce} è {@code null}, rimuove una batteria da un vano batteria.
	 *
	 * @param merce     la merce da rimuovere (può essere {@code null} per le batterie)
	 * @param giocatore il giocatore da cui rimuovere la merce
	 * @return il numero di unità effettivamente rimosse 
	 */
	private int eliminaMerce(Merce merce, Giocatore giocatore) {
		Griglia griglia = giocatore.getNave().getGriglia();
		int cont=0;
		
		for (int riga = 0; riga < griglia.getAltezza(); riga++) {
			for (int colonna = 0; colonna < griglia.getLarghezza(); colonna++) {
				Cella cella = griglia.getCella(riga, colonna);
                if(cella==null) continue;
                Tessera tessera = cella.getTessera();
                if(tessera==null) continue;
                
                if(tessera.accettaMerce() && merce!=null) {
                	Stiva stiva=(Stiva)tessera; //casting sicuro
                	try {
						stiva.setMerce(merce, stiva.getMerce(merce)-1);
						cont++;
					} catch (CaricamentoMerceNonValidoException e) {
						continue; //non c'è merce da rimuovere
					}
                } else if(tessera.fornisceBatterie() && merce==null) {
                	VanoBatteria vanoBatteria=(VanoBatteria)tessera; //casting sicuro
                	try {
						vanoBatteria.setNumeroBatterie(vanoBatteria.getBatterie()-1);
						cont++;
					} catch (RichiestaBatterieNonValidaException e) {
						continue; //non ci sono batterie da rimuovere
					}
                }
            }
		}
        return cont;
	}
	
	/**
	 * Restituisce una descrizione HTML dell'evento, da mostrare nella GUI.
	 *
	 * @return una stringa formattata HTML con la descrizione dell'evento
	 */
	@Override
	public String toString() {
	    return "<html><div style='text-align:justify;'>"
	         + "I contrabbandieri attaccano in ordine di rotta finché non sono sconfitti.<br>"
	         + "Ogni giocatore confronta la propria <b>potenza di fuoco</b> con quella del nemico, "
	         + "usando anche cannoni doppi.<br>"
	         + "Chi li sconfigge ottiene ricompensa e perde giorni di volo; chi perde subisce la penalità.<br>"
	         + "In caso di pareggio, il giocatore non subisce nulla ma l’attacco continua."
	         + "</div></html>";
	}
}