package galaxytrucker.src.logic.gioco;

import java.util.ArrayList;
import java.util.List;
import galaxytrucker.src.logic.assemblaggio.Mucchio;
import galaxytrucker.src.logic.volo.Evento;

/**
 * Rappresenta la fase di assemblaggio delle astronavi.
 * <p>
 * Durante questa fase, i giocatori costruiscono la propria nave pescando tessere
 * da un mucchio comune. Ogni turno corrisponde a un'azione di assemblaggio da
 * parte di un giocatore che non ha ancora completato l’assemblaggio.
 * <p>
 * La fase termina quando tutti i giocatori hanno completato l'assemblaggio.
 */
public class FaseAssemblaggio extends Fase {

    /** Il mucchio di tessere da cui i giocatori pescano durante l'assemblaggio. */
    private final Mucchio mucchio;
    
    /** La lista di carte evento che un giocatore può sbirciare durante l'assemblaggio */
    private final List<Evento> carteSbirciabili;

    /** Indice per trovare nella lista il giocatore che effettua il turno. */
    private int indiceGiocatoreCorrente;
    
    /**
     * Copia ordinata dei giocatori che hanno completato l'assemblaggio.
     * <p>
     * Questa lista viene popolata dinamicamente per evitare modifiche dirette
     * alla lista originale dei giocatori, che è già in uso per la gestione dei
     * turni di assemblaggio. Serve a mantenere traccia, in ordine, dei giocatori
     * che hanno già completato la loro fase di assemblaggio.
     * </p>
     */
    private final List<Giocatore> listaOrdinata;

    /**
     * Costruisce una nuova fase di assemblaggio con i giocatori specificati e il mucchio di tessere.
     *
     * @param giocatori la lista dei giocatori partecipanti.
     * @param livello il livello della partita in corso.
     * @param carteSbirciabili la lista di carte evento che un giocatore può sbirciare durante l'assemblaggio.
     * @param mucchio il mucchio di tessere disponibile per l'assemblaggio.
     */
    public FaseAssemblaggio(List<Giocatore> giocatori, Livello livello, Mucchio mucchio, List<Evento> carteSbirciabili) {
        this(giocatori, livello, mucchio, carteSbirciabili, 0);
    }

    /**
     * Costruisce una nuova fase di assemblaggio con un indice iniziale specifico.
     *
     * @param giocatori la lista dei giocatori partecipanti.
     * @param livello il livello della partita in corso.
     * @param mucchio il mucchio di tessere disponibile per l'assemblaggio.
     * @param carteSbirciabili la lista di carte evento che un giocatore può sbirciare durante l'assemblaggio.
     * @param indiceGiocatoreCorrente l'indice del prossimo giocatore da considerare.
     */
    public FaseAssemblaggio(List<Giocatore> giocatori, Livello livello, Mucchio mucchio, List<Evento> carteSbirciabili, int indiceGiocatoreCorrente) {
        super(giocatori, livello);
        this.mucchio = mucchio;
        this.carteSbirciabili = carteSbirciabili;
        this.indiceGiocatoreCorrente = indiceGiocatoreCorrente;
        this.listaOrdinata = new ArrayList<Giocatore>();
    }

    /**
     * Determina se esistono ancora turni da giocare nella fase di assemblaggio.
     * <p>
     * La fase continua fino a che almeno un giocatore non ha completato
     * l'assemblaggio della propria nave.
     * I giocatori che hanno completato l'assemblaggio vengono aggiunti alla
     * {@link #listaOrdinata}, in modo da ordinarli in partenza secondo quest'ordine.
     * </p>
     *
     * @return {@code true} se almeno un giocatore non ha completato l'assemblaggio,
     *         {@code false} altrimenti.
     */
    @Override
    public boolean esisteProssimoTurno() {
    	
    	boolean esiste = false;
    	
        for (Giocatore g : getGiocatori()) {
        	if (g.isAssemblaggioCompletato() && !listaOrdinata.contains(g)) listaOrdinata.add(g);
            if (!g.isAssemblaggioCompletato()) esiste = true;
        }
        
        return esiste;
    }

    /**
     * Restituisce il prossimo turno di assemblaggio.
     * <p>
     * Viene selezionato il primo giocatore, partendo dall'indice corrente, che
     * non ha ancora completato l'assemblaggio. L'indice scorre in modo circolare.
     * </p>
     *
     * @return un nuovo {@link Costruzione} se un giocatore può ancora agire,
     *         {@code null} se tutti hanno completato l'assemblaggio.
     */
    @Override
    public Turno prossimoTurno() {

        if (!esisteProssimoTurno()) return null;

        for (int i = 0; i < getGiocatori().size(); i++) {
            Giocatore g = getGiocatori().get(indiceGiocatoreCorrente);
            indiceGiocatoreCorrente = (indiceGiocatoreCorrente + 1) % getGiocatori().size();

            if (!g.isAssemblaggioCompletato()) {
                return new Costruzione(g, mucchio, getLivello(), carteSbirciabili);
            }
        }

        return null; // questo caso non dovrebbe mai verificarsi grazie al controllo iniziale
    }

    // getters
    
    /**
     * Restituisce la lista ordinata dei giocatori che hanno completato l'assemblaggio.
     * <p>
     * Questa lista viene popolata progressivamente durante l’esecuzione del metodo
     * {@link #esisteProssimoTurno()}, man mano che i giocatori terminano l’assemblaggio.
     * </p>
     *
     * @return la lista dei giocatori con assemblaggio completato, in ordine di completamento
     */
    public List<Giocatore> getListaOrdinata() {
        return listaOrdinata;
    }
}