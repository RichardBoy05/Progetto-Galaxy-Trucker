package galaxytrucker.src.logic.assemblaggio;

import java.util.EnumMap;
import java.util.Map;

/**
 * Classe astratta che rappresenta una tessera di tipo motore nella nave.
 * 
 * <p>Ogni motore possiede un tubo di scarico orientato in una specifica direzione</p>
 * 
 * <p>Per essere valido, un motore deve avere il tubo di scarico orientato verso {@code SUD}
 * e non deve avere alcuna tessera immediatamente adiacente in quella direzione.</p>
 * 
 * @see Tessera
 */
public abstract class Motore extends Tessera {

    /** Direzione verso cui è orientato il tubo di scarico del motore. */
    private Direzione tuboScarico;

    /**
     * Costruttore più comune.
     * 
     * @param lati         Mappa dei connettori della tessera per ogni direzione.
     * @param tuboScarico  Direzione del tubo di scarico del motore.
     * @param pathImmagine Percorso dell'immagine associata alla tessera.
     * @throws NullPointerException Se la direzione del tubo di scarico è {@code null}.
     */
    public Motore(EnumMap<Direzione, Connettore> lati, Direzione tuboScarico, String pathImmagine) {
        this(lati, false, tuboScarico, pathImmagine);
    }

    /**
     * Costruttore completo.
     * 
     * @param lati         Mappa dei connettori della tessera per ogni direzione.
     * @param visibile     Specifica se la tessera è visibile.
     * @param tuboScarico  Direzione del tubo di scarico del motore.
     * @param pathImmagine Percorso dell'immagine associata alla tessera.
     * @throws NullPointerException Se la direzione del tubo di scarico è {@code null}.
     */
    public Motore(EnumMap<Direzione, Connettore> lati, boolean visibile, Direzione tuboScarico, String pathImmagine) {
        super(lati, visibile, pathImmagine);

        if (tuboScarico == null) {
            String errore = "Il parametro tuboScarico non può essere null!";
            getLogger().error(errore);
            throw new NullPointerException(errore);
        }

        this.tuboScarico = tuboScarico;
    }

    /**
     * Verifica se la posizione del motore è valida rispetto alle tessere adiacenti.
     * 
     * <p>Un motore è valido solo se il tubo di scarico è orientato verso {@code SUD}
     * e non è presente alcuna tessera immediatamente adiacente in quella direzione.</p>
     *
     * @param tessereAdiacenti Mappa delle tessere adiacenti.
     * @return {@code true} se la posizione rispetta i vincoli sopra descritti, {@code false} altrimenti.
     */
    @Override
    public boolean verificaTessera(Map<Direzione, Tessera> tessereAdiacenti) {
        if (tuboScarico != Direzione.SUD) return false;
        if (tessereAdiacenti.get(Direzione.SUD) != null) return false;
        return true;
    }

    /**
     * Ruota i componenti della tessera in senso orario di 90°,
     * aggiornando la direzione del tubo di scarico.
     */
    @Override
    public void ruotaComponentiOrario() {
        tuboScarico = tuboScarico.orario90Gradi();
    }

    /**
     * Restituisce la direzione attuale del tubo di scarico.
     *
     * @return Direzione del tubo di scarico.
     */
    public Direzione getTuboScarico() {
        return tuboScarico;
    }
}