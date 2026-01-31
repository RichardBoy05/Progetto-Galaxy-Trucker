package galaxytrucker.src.logic.volo;

import java.util.Collections;
import java.util.List;
import java.util.Random;

import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

import galaxytrucker.src.logic.assemblaggio.Abitante;
import galaxytrucker.src.logic.assemblaggio.Attivabile;
import galaxytrucker.src.logic.assemblaggio.Cella;
import galaxytrucker.src.logic.assemblaggio.Griglia;
import galaxytrucker.src.logic.assemblaggio.Nave;
import galaxytrucker.src.logic.assemblaggio.Tessera;
import galaxytrucker.src.logic.gioco.GameLogger;
import galaxytrucker.src.logic.gioco.Giocatore;
import galaxytrucker.src.logic.gioco.Livello;
import galaxytrucker.src.logic.gioco.Turno;
import galaxytrucker.src.view.frames.VoloGui;

/**
 * Classe astratta che rappresenta un evento durante la fase di volo.
 * Ogni evento è un {@link Turno} e influisce sulla nave dei giocatori in base a una logica specifica.
 * Fornisce metodi comuni per l'esecuzione e la gestione delle conseguenze di ogni evento.
 */
public abstract class Evento implements Turno {
	
    /** Logger condiviso per la registrazione degli eventi di gioco. */
	private static final GameLogger LOGGER = GameLogger.getInstance();
    
	/** Valore minimo ottenibile con il lancio di due dadi. */
	public final static int MIN_DADO=1; 
	
    /** Valore massimo ottenibile con il lancio di due dadi. */
	public final static int MAX_DADO=6; 
	
	/** Oggetto di tipo {@code Random} che rappresenta il dado di gioco. */
    private static final Random DADO = new Random();
	
	private final List<Giocatore> giocatori;
	private final Livello livello;
	private ImageIcon immagine;
	
	/**
     * Costruisce un nuovo evento.
     *
     * @param giocatori la lista dei giocatori partecipanti al volo; non può essere null
     * @param livello il livello della partita
     * @param pathImmagine il percorso dell'immagine associata all'evento; non può essere null
     * @throws NullPointerException se uno dei parametri è null
     */
	public Evento(List<Giocatore> giocatori, Livello livello, String pathImmagine) {
		if(giocatori == null) {
        	String errore = "Il parametro 'giocatori' non può essere null!";
        	LOGGER.error(errore);
        	throw new NullPointerException(errore);      	
        }
		
		if(livello == null) {
        	String errore = "Il parametro 'livello' non può essere null!";
        	LOGGER.error(errore);
        	throw new NullPointerException(errore);      	
        }
		
		if(pathImmagine == null) {
        	String errore = "Il parametro pathImmagine non può essere null!";
        	LOGGER.error(errore);
        	throw new NullPointerException(errore);      	
        }
		
	    this.giocatori = giocatori; 
	    this.livello = livello;	    
	    this.immagine = new ImageIcon(getClass().getResource(pathImmagine));
	}
	
	/**
	 * Avvia l'evento eseguendo prima la logica specifica dell'evento e poi le operazioni comuni di fine turno.
	 * Questo metodo è `final` per impedire la sua sovrascrittura nelle sottoclassi, garantendo una gestione coerente del turno.
	 *
	 * @param gui l'interfaccia grafica del volo
	 */
	public final void avviaEvento(VoloGui gui){
		avviaLogica(gui);
		operazioniDiFineTurno(gui);
	}
	
	/**
     * Metodo astratto che definisce la logica principale dell'evento.
     * Deve essere implementato dalle sottoclassi.
     *
     * @param gui l'interfaccia grafica del volo
     */
	public abstract void avviaLogica(VoloGui gui);
	
	/**
     * Disattiva tutte le tessere attivabili delle navi dei giocatori.
     * Questo viene eseguito alla fine di ogni evento dove possono essere attivate.
     */
	protected void resetComponentiAttivabili() {
		for (Giocatore giocatore : getGiocatori()) {
	        Griglia griglia = giocatore.getNave().getGriglia();

	        for (int riga = 0; riga < griglia.getAltezza(); riga++) {
	            for (int colonna = 0; colonna < griglia.getLarghezza(); colonna++) {
	                Tessera tessera = griglia.getCella(riga, colonna).getTessera();

	                if (tessera != null && tessera.isAttivabile()) {
	                	Attivabile tesseraAttivabile= (Attivabile) tessera; //casting sicuro per il controllo fatto in precedenza
	                	tesseraAttivabile.setAttivo(false);
	                	
	                }
	            }
	        }
	    }
	}
	
	/**
     * Verifica se la nave contiene almeno una tessera stiva, cioè una tessera
     * in grado di contenere merci.
     *
     * @param nave la nave da esaminare
     * @return true se esiste almeno una tessera stiva, false altrimenti
     */
	protected boolean verificaPresenzaStiva(Nave nave) {
		Griglia griglia = nave.getGriglia();

        for (int riga = 0; riga < griglia.getAltezza(); riga++) {
        	for (int colonna = 0; colonna < griglia.getLarghezza(); colonna++) {
            	Cella c=griglia.getCella(riga, colonna);
            	if(c==null) continue;
            	if(c.getTessera()==null) continue;
            	if(c.getTessera().accettaMerce()) return true;
        		}
        }
        return false;
	}
	
	/**
     * Esegue operazioni comuni alla fine di ogni evento, come:
     * <ul>
     *   <li>Disattivare tutte le tessere attivabili</li>
     *   <li>Controllare se i giocatori possono ancora rimanere in volo</li>
     *   <li>Verificare se un giocatore è stato doppiato, in base ai dati di ogni livello</li>
     * </ul>
     *
     * @param gui l'interfaccia grafica del volo
     */
	private void operazioniDiFineTurno(VoloGui gui) {
		Collections.sort(giocatori);
	    resetComponentiAttivabili();
	    
	    for(Giocatore giocatore:giocatori) {
	    	if(giocatore.isInVolo() && giocatore.getNave().getNumeroAbitantiPerTipo(Abitante.ASTRONAUTA)==0 && livello!=Livello.P) {
	    		giocatore.setInVolo(false);
	    		JOptionPane.showMessageDialog(
	    	            gui.getFrame(),
	    	            "Il giocatore "+giocatore.getColore()+" non è più un volo perché non ha più astronauti che guidino la nave!",
	    	            "Fine volo",
	    	            JOptionPane.INFORMATION_MESSAGE
	    	        );	
	    	}
	    	
	    }
	    
		int giorniPersiGiocatoreInTesta=giocatori.get(0).getGiorniDiVoloPersi();
		for(int i=1; i<giocatori.size();i++) {
			if(!giocatori.get(i).isInVolo() && livello==Livello.P) continue;
			int distanzaDalPrimoGiocatore=giocatori.get(i).getGiorniDiVoloPersi()-giorniPersiGiocatoreInTesta;
			
			if(distanzaDalPrimoGiocatore>=livello.getDistanzaDoppiaggio()) {
				giocatori.get(i).setInVolo(false);
				
				JOptionPane.showMessageDialog(
	    	            gui.getFrame(),
	    	            "<html><div style='text-align:justify;'>"
	    	   	             + "Il giocatore "+giocatori.get(i).getColore()+" non è più in volo perché è stato doppiato, <br>"
	    	   	             + "ossia è rimasto indietro di "+livello.getDistanzaDoppiaggio()+" giorni di volo!<br>"
	    	   	             + "</div></html>",
	    	            "Fine volo",
	    	            JOptionPane.INFORMATION_MESSAGE
	    	        );	
			}
			
		}
	}
	
	/**
     * Restituisce la lista dei giocatori coinvolti nell'evento.
     *
     * @return la lista dei giocatori
     */
	public List<Giocatore> getGiocatori() {
		return giocatori;
	}
	
	/**
     * Restituisce l'immagine associata all'evento.
     *
     * @return un'icona con l'immagine dell'evento
     */
	public ImageIcon getImmagine() {
		return immagine;
	}
	
	/**
     * Restituisce l'immagine associata all'evento.
     *
     * @return un'icona con l'immagine dell'evento
     */
	public Livello getLivello() {
		return livello;
	}
	
	/**
     * Restituisce l'istanza del logger usato per registrare messaggi durante l'esecuzione dell'evento.
     *
     * @return il logger globale
     */
	protected static GameLogger getLogger() { //quando ci sono eccezioni viene scritto su un file
		return LOGGER;
	}
	
	/**
     * Restituisce l'oggetto di tipo {@code Random} che rappresenta il dado di gioco.
     *
     * @return oggetto di tipo {@code Random} 
     */
	public static Random getDado() {
		return DADO;
	}
}