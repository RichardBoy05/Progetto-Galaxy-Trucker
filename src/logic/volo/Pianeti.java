package galaxytrucker.src.logic.volo;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;

import galaxytrucker.src.logic.gioco.GameLogger;
import galaxytrucker.src.logic.gioco.Giocatore;
import galaxytrucker.src.logic.gioco.Livello;
import galaxytrucker.src.view.dialogs.CaricoMerciDialog;
import galaxytrucker.src.view.dialogs.InterazioniUtenteDialog;
import galaxytrucker.src.view.frames.VoloGui;

/**
 * Evento che rappresenta una carta con da 2 a 4 pianeti.
 * <p>
 * In ordine di rotta, ogni giocatore può scegliere di atterrare su un pianeta libero
 * per raccogliere le merci disponibili, perdendo però un certo numero di giorni di volo.
 * Ogni pianeta può essere occupato da un solo giocatore.
 * </p>
 * <p>
 * Se un giocatore non ha una stiva disponibile nella sua nave, non può atterrare su un pianeta.
 * L'evento termina mostrando un messaggio se nessun giocatore possiede una stiva.
 * </p>
 */
public final class Pianeti extends Evento {

	private static final GameLogger LOGGER = GameLogger.getInstance();

	/** Numero minimo di pianeti presenti sulla carta evento. */
    private static final int MIN_PIANETI = 2;

    /** Numero massimo di pianeti presenti sulla carta evento. */
    private static final int MAX_PIANETI = 4;

    /** Lista dei pianeti disponibili nell'evento. */
    private final List<Pianeta> pianeti;

    /** Numero di giorni di volo persi in caso di atterraggio su un pianeta. */
    private final int giorniVolo;

    /**
     * Costruisce una carta evento con una lista di pianeti e i giorni di volo persi.
     * 
     * @param giocatori lista dei giocatori coinvolti
     * @param livello livello di difficoltà o di gioco
     * @param pianeti lista dei pianeti (non null, con dimensione compresa tra 2 e 4)
     * @param giorniVolo giorni di volo che si perdono atterrando su un pianeta
     * @param pathImmagine percorso dell'immagine associata all'evento
     * @throws NullPointerException se la lista pianeti è null
     * @throws IllegalArgumentException se il numero di pianeti non è tra 2 e 4
     */
    public Pianeti(List<Giocatore> giocatori, Livello livello, List<Pianeta> pianeti, int giorniVolo, String pathImmagine) {
        super(giocatori, livello, pathImmagine);
        
        if(pianeti == null) {
        	String errore = "Il parametro 'pianeti' non può essere null!";
        	LOGGER.error(errore);
        	throw new NullPointerException(errore);      	
        }
        
        if (pianeti.size() < MIN_PIANETI || pianeti.size() > MAX_PIANETI) {
           	String errore = "Il numero di pianeti possono essere minimo "+MIN_PIANETI+" e massimo "+MAX_PIANETI+"!";
           	getLogger().error(errore);
           	throw new IllegalArgumentException(errore); 
           }    		  
      
        this.pianeti = pianeti; 
        this.giorniVolo=giorniVolo;
    }
	
    /**
     * Avvia la visualizzazione grafica dell'evento e attende l'interazione dell'utente.
     */
	@Override
	public void esegui() {
		new VoloGui(this).mostraEAttendi();			
	}

	 /**
     * Logica principale dell'evento:
     * per ogni giocatore in volo, se possiede una stiva libera,
     * può scegliere un pianeta su cui atterrare per caricare merci,
     * perdendo giorni di volo indicati.
     * 
     * Se nessun giocatore ha stiva, mostra un messaggio informativo.
     * 
     * @param gui interfaccia grafica per le interazioni
     */
	@Override
	public void avviaLogica(VoloGui gui) {
		List<Giocatore> giocatoriAtterrati=new ArrayList<>();
		boolean almenoUnGiocatoreConStiva=false;
		
		for (Giocatore giocatore : getGiocatori()) {
			if(!esistePianetaNonOccupato()) break;
			if(!giocatore.isInVolo()) continue;
			
			if(verificaPresenzaStiva(giocatore.getNave())) {
				InterazioniUtenteDialog dialog = new InterazioniUtenteDialog(gui.getFrame(), giocatore, pianeti);
		        Pianeta scelto = dialog.getSceltaPianeta();
		        almenoUnGiocatoreConStiva=true;
		        
		        if(scelto!=null) {
		        	scelto.setOccupato(true);
		        	giocatoriAtterrati.add(giocatore);
		            new CaricoMerciDialog(gui.getFrame(),giocatore, scelto.getMerci());
		        } 
		        
		    }
			
			for(int i=giocatoriAtterrati.size()-1; i>=0; i--) {
				giocatoriAtterrati.get(i).perdiGiorniDiVolo(giorniVolo, getGiocatori());
			}
			}
		
		if (!almenoUnGiocatoreConStiva) {
	        JOptionPane.showMessageDialog(
	            gui.getFrame(),
	            "Nessun giocatore possiede una stiva!",
	            "Pianeti",
	            JOptionPane.INFORMATION_MESSAGE
	        );
	    }
	}
	
	public boolean esistePianetaNonOccupato(){
		for (Pianeta p : pianeti) {
			if (!p.isOccupato()) {
				return true;
			}
		}
		return false;
	}

	/**
     * Descrive sinteticamente la dinamica dell'evento per la visualizzazione all'utente.
     */
	@Override
	public String toString() {
	    return "<html><div style='text-align:justify;'>"
	         + "<b>Ogni giocatore</b> può scegliere un pianeta su cui atterrare, a partire dal <b>leader</b>.<br>"
	         + "Ogni pianeta può accogliere <b>un solo razzo</b>, e l’atterraggio costa <b>giorni di volo</b>.<br>"
	         + "Chi atterra carica le <b>merci indicate</b> e arretra lungo la rotta del numero di giorni corrispondente.<br>"
	         + "È possibile atterrare anche solo per <b>bloccare gli altri</b>."
	         + "</div></html>";
	}
	
}