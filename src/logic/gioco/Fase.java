package galaxytrucker.src.logic.gioco;

import java.util.List;

/**
 * Classe astratta che rappresenta una fase all'interno del gioco.
 * <p>
 * Una fase è composta da un numero variabile di turni e coinvolge tutti i
 * giocatori partecipanti. Le sottoclassi dovranno definire la logica che
 * determina se ci sono ancora turni da eseguire e come ottenere il prossimo turno.
 */
public abstract class Fase {

    /** Lista dei giocatori coinvolti nella fase. */
    private final List<Giocatore> giocatori;
    
    /** Il livello della partita in corso
     * @see Livello
     */
    private final Livello livello;

    /**
     * Costruisce una nuova fase per i giocatori specificati.
     *
     * @param giocatori la lista dei giocatori partecipanti alla fase
     * @param livello il livello della partita in corso
     */
    public Fase(List<Giocatore> giocatori, Livello livello) {
        this.giocatori = giocatori;
        this.livello = livello;
    }

    /**
     * Determina se esiste ancora almeno un turno da eseguire nella fase.
     *
     * @return {@code true} se esiste un prossimo turno, {@code false} altrimenti.
     */
    public abstract boolean esisteProssimoTurno();

    /**
     * Restituisce il prossimo turno da eseguire.
     *
     * @return il prossimo {@link Turno}, oppure {@code null} se non ce ne sono.
     */
    public abstract Turno prossimoTurno();

    /**
     * Esegue il prossimo turno, se disponibile.
     * <p>
     * Questo metodo ottiene il prossimo turno tramite {@link #prossimoTurno()},
     * e lo esegue invocandone il metodo {@code esegui()}. Se non ci sono più
     * turni, il metodo non fa nulla.
     * </p>
     */
    public void eseguiProssimoTurno() {
        Turno turno = prossimoTurno();
        if (turno == null) {
            return;
        }
        turno.esegui();
    }

    /**
     * Esegue l'intera fase del gioco.
     * <p>
     * I turni vengono eseguiti uno dopo l'altro fino a che {@link #esisteProssimoTurno()}
     * restituisce {@code false}.
     * </p>
     */
    public void eseguiFase() {
        while (esisteProssimoTurno()) {
            eseguiProssimoTurno();
        }
    }
    
    // getters

    /**
     * Restituisce la lista dei giocatori partecipanti alla fase.
     *
     * @return la lista dei {@link Giocatore}
     */
    public List<Giocatore> getGiocatori() {
        return giocatori;
    }
    
    /**
     * Restituisce il livello della partita in corso
     *
     * @return il livello della partita
     */
    public Livello getLivello() {
    	return livello;
    }
}