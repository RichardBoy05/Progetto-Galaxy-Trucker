package galaxytrucker.src.logic.gioco;

import java.util.List;
import java.util.Queue;
import galaxytrucker.src.logic.volo.Evento;

/**
 * Rappresenta la fase di volo delle astronavi.
 * <p>
 * Durante questa fase, i giocatori affrontano una serie di eventi spaziali
 * predefiniti in una coda. Ogni evento viene elaborato come un turno.
 * <p>
 * La fase termina quando non ci sono più eventi da
 * affrontare o quando nessun giocatore è rimasto in volo.
 */
public class FaseVolo extends Fase {

    /** Coda di eventi da elaborare durante la fase di volo. */
    private Queue<Evento> eventi;

    /**
     * Costruisce una nuova fase di volo con i giocatori specificati e la sequenza di eventi.
     *
     * @param giocatori la lista dei giocatori partecipanti.
     * @param il livello il livello della partita in corso.
     * @param eventi la coda di eventi che verranno affrontati durante la fase.
     */
    public FaseVolo(List<Giocatore> giocatori, Livello livello, Queue<Evento> eventi) {
        super(giocatori, livello);
        this.eventi = eventi;
    }

    /**
     * Determina se esistono ancora turni da giocare nella fase di volo.
     * <p>
     * La fase continua finché ci sono eventi nella
     * coda e almeno un giocatore è ancora in volo.
     * </p>
     *
     * @return {@code true} se esiste un prossimo evento e almeno un giocatore è ancora in volo,
     *         {@code false} altrimenti.
     */
    @Override
    public boolean esisteProssimoTurno() {
        if (eventi.isEmpty()) return false;

        for (Giocatore g : getGiocatori()) {
            if (g.isInVolo()) return true;
        }

        return false;
    }

    /**
     * Restituisce il prossimo turno della fase di volo, rappresentato da un {@link Evento}.
     * Gli eventi vengono gestiti secondo una politica FIFO (First In, First Out).
     *
     * @return il prossimo {@link Evento} da eseguire, oppure {@code null} se la fase è conclusa.
     */
    @Override
    public Turno prossimoTurno() {
        if (!esisteProssimoTurno()) return null;
        
        return eventi.poll();
    }
}