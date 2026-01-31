package galaxytrucker.src.logic.assemblaggio;

import java.util.EnumMap;
import java.util.Map;

/**
 * Classe astratta che rappresenta una tessera di tipo cannone all’interno della nave.
 * Ogni cannone possiede una canna di fuoco orientata in una specifica direzione.
 * 
 * <p>Regola: i cannoni non possono avere una tessera immediatamente adiacente nella direzione di fuoco.</p>
 * 
 * @see Tessera
 */
public abstract class Cannone extends Tessera {

    /** Direzione verso cui è orientata la canna di fuoco del cannone. */
    private Direzione cannaFuoco;

    /**
     * Costruttore più comune.
     * 
     * @param lati        Mappa dei connettori della tessera per ogni direzione.
     * @param cannaFuoco  Direzione in cui è orientata la canna di fuoco.
     * @param pathImmagine Percorso dell'immagine associata alla tessera.
     * @throws NullPointerException Se la direzione della canna di fuoco è {@code null}.
     */
    public Cannone(EnumMap<Direzione, Connettore> lati, Direzione cannaFuoco, String pathImmagine) {
        this(lati, false, cannaFuoco, pathImmagine);
    }

    /**
     * Costruttore completo.
     * 
     * @param lati        Mappa dei connettori della tessera per ogni direzione.
     * @param visibile    Specifica se la tessera è visibile.
     * @param cannaFuoco  Direzione in cui è orientata la canna di fuoco.
     * @param pathImmagine Percorso dell'immagine associata alla tessera.
     * @throws NullPointerException Se la direzione della canna di fuoco è {@code null}.
     */
    public Cannone(EnumMap<Direzione, Connettore> lati, boolean visibile, Direzione cannaFuoco, String pathImmagine) {
        super(lati, visibile, pathImmagine);

        if (cannaFuoco == null) {
            String errore = "Il parametro 'cannaFuoco' non può essere null!";
            getLogger().error(errore);
            throw new NullPointerException(errore);
        }

        this.cannaFuoco = cannaFuoco;
    }

    /**
     * Verifica se la posizione del cannone è valida rispetto alle tessere adiacenti.
     * Un cannone non può avere una tessera nella direzione di fuoco.
     *
     * @param tessereAdiacenti Mappa delle tessere adiacenti.
     * @return {@code true} se non è presente nessuna tessera nella direzione di fuoco, {@code false} altrimenti.
     */
    @Override
    public boolean verificaTessera(Map<Direzione, Tessera> tessereAdiacenti) {
        if (tessereAdiacenti.get(cannaFuoco) != null) return false;
        return true;
    }

    /**
     * Ruota i componenti della tessera in senso orario di 90°,
     * aggiornando la direzione della canna di fuoco.
     */
    @Override
    public void ruotaComponentiOrario() {
        cannaFuoco = cannaFuoco.orario90Gradi();
    }

    /**
     * Restituisce la direzione attuale della canna di fuoco.
     *
     * @return Direzione della canna di fuoco.
     */
    public Direzione getCannaFuoco() {
        return cannaFuoco;
    }

}