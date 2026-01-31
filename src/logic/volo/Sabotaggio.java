package galaxytrucker.src.logic.volo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.swing.JOptionPane;

import galaxytrucker.src.logic.assemblaggio.Cella;
import galaxytrucker.src.logic.assemblaggio.Coordinate;
import galaxytrucker.src.logic.gioco.Giocatore;
import galaxytrucker.src.logic.gioco.Livello;
import galaxytrucker.src.view.frames.VoloGui;

/**
 * Rappresenta l'evento "Sabotaggio" durante la fase di volo.
 * <p>
 * Questo evento colpisce la nave del giocatore con il minor numero di membri dell'equipaggio.
 * In caso di parità, viene colpita la nave più avanti lungo la rotta.
 * Il sabotaggio consiste nel tentativo di distruggere un componente della nave, 
 * selezionato casualmente tramite tiri di dado. Il tiro può essere ripetuto fino
 * a tre volte se la cella risultante è vuota o non contiene un componente.
 * </p>
 */
public final class Sabotaggio extends Evento {
	
	/** Numero massimo di tentativi di tiro per colpire un componente. */
	private final static int MAX_LANCI = 3;
	
	/**
     * Costruttore dell'evento Sabotaggio.
     *
     * @param giocatori     Lista dei giocatori in gioco.
     * @param livello       Livello della partita.
     * @param pathImmagine  Percorso all'immagine associata all'evento.
     */
    public Sabotaggio(List<Giocatore> giocatori, Livello livello, String pathImmagine) {
        super(giocatori, livello, pathImmagine);
    }

    /**
     * Avvia la visualizzazione dell'evento tramite l'interfaccia grafica.
     * Mostra una schermata che descrive l'evento e attende l'interazione dell'utente.
     */
    @Override
    public void esegui() {
        new VoloGui(this).mostraEAttendi();
    }
    
    /**
     * Contiene la logica dell'evento Sabotaggio:
     * <ul>
     *   <li>Determina il giocatore con meno membri dell'equipaggio ancora in volo.</li>
     *   <li>In caso di parità, sceglie il primo nella lista (che rappresenta quello più avanti sulla rotta).</li>
     *   <li>Tenta di distruggere un componente della nave del giocatore selezionato.</li>
     * </ul>
     *
     * @param gui L'interfaccia grafica attiva per la fase di volo.
     */
    @Override
    public void avviaLogica(VoloGui gui) {
    	List <Integer> equipaggioPerGiocatore=new ArrayList<>();
    	int giocatoreInVolo=0;
    	for(Giocatore g:getGiocatori()) {
			if(!g.isInVolo()) continue;
    		equipaggioPerGiocatore.add(g.getNave().getTotAbitanti());
    		giocatoreInVolo++;
    	}
    	if(giocatoreInVolo<=1) {
    		JOptionPane.showMessageDialog(
                    gui.getFrame(),
                    "L'evento sabotaggio non può essere giocato se c'è solo un giocatore rimasto in volo!",
                    "Sabotaggio",
                    JOptionPane.INFORMATION_MESSAGE
                );
    		return;
    	}
    	int minEquipaggio=Collections.min(equipaggioPerGiocatore);
    	List <Integer> indiciGiocatori=new ArrayList<>();
    	for(int i=0; i<getGiocatori().size(); i++) {
			if(!getGiocatori().get(i).isInVolo()) continue;

    		if(getGiocatori().get(i).getNave().getTotAbitanti()==minEquipaggio) indiciGiocatori.add(i);
    	}
    	
    	if(indiciGiocatori.size()==1) {
        	
        	JOptionPane.showMessageDialog(
                    gui.getFrame(),
                    "Il giocatore " + getGiocatori().get(indiciGiocatori.get(0)).getColore() + ", avendo il minor numero di equipaggio, verrà sabotato!",
                    "Sabotaggio",
                    JOptionPane.INFORMATION_MESSAGE
                );
        	
        	distruggiComponente(getGiocatori().get(indiciGiocatori.get(0)));

	        
    	} else {
        	List <Giocatore> giocatoriInPareggio=new ArrayList<>();
        	for(Integer indice:indiciGiocatori) {
        		giocatoriInPareggio.add(getGiocatori().get(indice));
        	}
        	        	
        	JOptionPane.showMessageDialog(
                    gui.getFrame(),
                    "Più giocatori hanno lo stesso numero minimo di equipaggio. Tra questi, solo \n" +
                    "il giocatore " + getGiocatori().get(indiciGiocatori.get(0)).getColore() + ", essendo più avanti sulla rotta, verrà sabotato!",
                    "Sabotaggio",
                    JOptionPane.INFORMATION_MESSAGE
                );
        	
        	distruggiComponente(giocatoriInPareggio.get(0));

    	}
    }

    /**
     * Tenta di distruggere un componente della nave del giocatore specificato.
     * Vengono effettuati fino a tre tentativi, tirando due dadi per selezionare una riga e una colonna.
     * Se la cella scelta contiene una tessera, questa viene rimossa.
     *
     * @param giocatore Il giocatore la cui nave sarà oggetto del sabotaggio.
     */
    private void distruggiComponente(Giocatore giocatore) {
    	boolean componenteDistrutto=false;
    	for(int i=0; i<MAX_LANCI; i++) {
    		
    		int rigaDiGioco= getDado().nextInt(Evento.MIN_DADO, Evento.MAX_DADO + 1) + getDado().nextInt(Evento.MIN_DADO, Evento.MAX_DADO + 1);
    		int colonnaDiGioco= getDado().nextInt(Evento.MIN_DADO, Evento.MAX_DADO + 1) + getDado().nextInt(Evento.MIN_DADO, Evento.MAX_DADO + 1);

    		Coordinate coordinateInterne=giocatore.getNave().getLivello().convertiDaGiocoAReali(rigaDiGioco, colonnaDiGioco);
    		Cella c = giocatore.getNave().getGriglia().getCella(coordinateInterne.getRiga(), coordinateInterne.getColonna());
    		
    		if(c==null) continue;
    		if(c.getTessera()==null) continue;
    		
    		c.rimuoviTessera();
    		giocatore.aggiungiDebiti(1); // ogni tessera rimossa costituisce 1 debito
    		giocatore.getNave().rimuoviAlieniNonPiuSupportati();
    		componenteDistrutto=true;
    		
    		JOptionPane.showMessageDialog(
                    null,
                    "Sabotaggio riuscito! Componente della nave del giocatore " + giocatore.getColore() + 
                    " distrutto alla riga " + rigaDiGioco + " e alla colonna " + colonnaDiGioco + "!",
                    "Sabotaggio",
                    JOptionPane.INFORMATION_MESSAGE
                );
    		
    		break;
    	}
    	
    	if (!componenteDistrutto) {
            JOptionPane.showMessageDialog(
                null,
                "Sabotaggio fallito! Nessun componente distrutto dopo " + MAX_LANCI + " tentativi.",
                "Sabotaggio",
                JOptionPane.INFORMATION_MESSAGE
            );
        }
    }
    
    /**
     * Restituisce la descrizione HTML dell'evento, formattata per la visualizzazione grafica.
     *
     * @return Stringa HTML con la descrizione dell'effetto del sabotaggio.
     */
    @Override
    public String toString() {
        return "<html><div style='text-align:justify;'>"
             + "Un <b>sabotaggio</b> colpisce la nave con il minor numero di <b>membri dell'equipaggio</b>.<br>"
             + "(In caso di parità, viene colpita quella più avanti sulla <b>rotta</b>).<br>"
             + "Vengono lanciati due dadi per determinare la <b>colonna</b> e due dadi per la <b>riga</b> del componente da colpire.<br>"
             + "Se non c'è alcun componente, il tiro può essere ripetuto fino a <b>tre volte</b>.<br>"
             + "Se ancora nulla viene colpito, non accade nulla.<br>"
             + "Se un componente viene distrutto, anche quelli <b>collegati male</b> possono staccarsi."
             + "</div></html>";
    }
}
