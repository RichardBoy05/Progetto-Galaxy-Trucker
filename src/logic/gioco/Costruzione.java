package galaxytrucker.src.logic.gioco;

import java.util.List;
import galaxytrucker.src.logic.assemblaggio.Mucchio;
import galaxytrucker.src.logic.volo.Evento;
import galaxytrucker.src.view.frames.AssemblaggioGui;

/**
 * Questa classe rappresenta il turno di costruzione.
 * Implementa l'interfaccia {@link Turno}, in quanto la costruzione è uno dei tipi di turni della partita.
 * Il suo unico compito è quello di avviare la GUI {@link AssemblaggioGui}, la quale gestisce l'interazione utente.
 * <p>
 * Questa scelta progettuale è intenzionale: si è preferito evitare che la GUI implementasse direttamente {@code Turno},
 * così da mantenere una netta separazione tra la logica di gioco e la componente grafica.
 * {@code Costruzione} agisce quindi come un "ponte" tra il flusso di gioco e l'interfaccia utente.
 *
 * @see Turno
 */
public class Costruzione implements Turno {
	
	/** Logger di gioco. */
	private static final GameLogger LOGGER = GameLogger.getInstance();
    
	/** Il giocatore che sta affrontando il turno di costruzione. */
    private final Giocatore giocatore;
    
    /** Il mucchio di tessere disponibili per l'assemblaggio. */
    private final Mucchio mucchio;
    
    /** Il livello di difficoltà della partita corrente. */
    private final Livello livello;
    
    /** La lista di eventi che il giocatore può sbirciare durante la costruzione. */
    private final List<Evento> carteSbirciabili;

    /**
     * Crea un nuovo turno di costruzione per il giocatore specificato.
     *
     * @param giocatore il giocatore che sta affrontando il turno di costruzione.
     * @param mucchio il mucchio di tessere disponibili per l'assemblaggio.
     * @param livello il livello della partita corrente.
     * @param carteSbirciabili la lista di eventi che il giocatore può sbirciare durante la costruzione.
     * @throws NullPointerException se uno dei parametri è {@code null}.
     */
    public Costruzione(Giocatore giocatore, Mucchio mucchio, Livello livello, List<Evento> carteSbirciabili) {
    	
    	if (giocatore == null) {
			String errore = "Il parametro 'giocatore' non può essere nullo!";
			LOGGER.error(errore);
			throw new NullPointerException(errore);
		}
    	
    	if (mucchio == null) {
			String errore = "Il parametro 'mucchio' non può essere nullo!";
			LOGGER.error(errore);
			throw new NullPointerException(errore);
		}
    	
    	if (livello == null) {
			String errore = "Il parametro 'livello' non può essere nullo!";
			LOGGER.error(errore);
			throw new NullPointerException(errore);
		}
    	
    	if (carteSbirciabili == null) {
			String errore = "Il parametro 'carteSbirciabili' non può essere nullo!";
			LOGGER.error(errore);
			throw new NullPointerException(errore);
		}
    	
        this.giocatore = giocatore;
        this.mucchio = mucchio;
        this.livello = livello;
        this.carteSbirciabili = carteSbirciabili;
    }

    /**
     * Esegue il turno di costruzione mostrando la GUI per l'interazione utente.
     */
    @Override
    public void esegui() {
        new AssemblaggioGui(giocatore, mucchio, livello, carteSbirciabili).mostraEAttendi();    
    }

}