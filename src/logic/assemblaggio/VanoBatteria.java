package galaxytrucker.src.logic.assemblaggio;

import java.util.EnumMap;
import java.util.Map;
import galaxytrucker.src.logic.eccezioni.RichiestaBatterieNonValidaException;

/**
 * Rappresenta vano batteria, ossia una tessera che consente di accumulare
 * e fornire batterie per alimentare altre tessere della nave, nello specifico
 * le tessere attivabili {@link MotoreDoppio}, {@link CannoneDoppio} e {@link Scudo}.
 *
 * @see Tessera
 */
public class VanoBatteria extends Tessera {

    /** Numero massimo di batterie che il vano può contenere (può essere 2 o 3). */
    private final int maxBatterie;

    /** Numero corrente di batterie caricate nel vano. */
    private int numeroBatterie;

    /**
     * Costruttore più comune.
     *
     * @param lati         Mappa dei connettori della tessera per ogni direzione.
     * @param maxBatterie  Numero massimo di batterie supportate (può essere 2 o 3).
     * @param pathImmagine Percorso dell'immagine associata alla tessera.
     * @throws IllegalArgumentException se {@code maxBatterie} è diverso da 2 o 3.
     */
    public VanoBatteria(EnumMap<Direzione, Connettore> lati, int maxBatterie, String pathImmagine) {
        this(lati, false, maxBatterie, pathImmagine);
    }

    /**
     * Costruttore completo.
     *
     * @param lati         Mappa dei connettori della tessera per ogni direzione.
     * @param visibile     Specifica se la tessera è visibile.
     * @param maxBatterie  Numero massimo di batterie supportate (può essere 2 o 3).
     * @param pathImmagine Percorso dell'immagine associata alla tessera.
     * @throws IllegalArgumentException se {@code maxBatterie} è diverso da 2 o 3.
     */
    public VanoBatteria(EnumMap<Direzione, Connettore> lati, boolean visibile, int maxBatterie, String pathImmagine) {
        super(lati, visibile, pathImmagine);

        if (maxBatterie < 2 || maxBatterie > 3) {
            String errore = "Il numero massimo di batterie può essere 2 o 3!";
            getLogger().error(errore);
            throw new IllegalArgumentException(errore);
        }

        this.maxBatterie = maxBatterie;
    }

    /**
     * Imposta il numero corrente di batterie caricate nel vano.
     *
     * @param numeroBatterie Numero di batterie da caricare.
     * @throws RichiestaBatterieNonValidaException se il numero richiesto è negativo o supera {@code maxBatterie}.
     */
    public void setNumeroBatterie(int numeroBatterie) throws RichiestaBatterieNonValidaException {

        if (numeroBatterie < 0) {
            String errore = "Batterie esaurite, non puoi ottenerne altre!";
            getLogger().error(errore);
            throw new RichiestaBatterieNonValidaException(errore);
        }

        if (numeroBatterie > maxBatterie) {
            String errore = "Il numero di batterie non può superare " + maxBatterie + "!";
            getLogger().error(errore);
            throw new RichiestaBatterieNonValidaException(errore);
        }

        this.numeroBatterie = numeroBatterie;
    }

    /**
     * Restituisce il numero massimo di batterie che il vano può contenere.
     *
     * @return Numero massimo di batterie.
     */
    public int getMaxBatterie() {
        return maxBatterie;
    }

    /**
     * Indica se questa tessera fornisce batterie.
     *
     * @return {@code true}, in quanto un {@code VanoBatteria} fornisce batterie.
     */
    @Override
    public boolean fornisceBatterie() {
        return true;
    }

    /**
     * Restituisce il numero corrente di batterie caricate nel vano.
     *
     * @return Numero di batterie attualmente disponibili.
     */
    @Override
    public int getBatterie() {
        return numeroBatterie;
    }

    /**
     * Verifica la validità della tessera.
     * Per questa versione, restituisce sempre {@code true}.
     *
     * @param tessereAdiacenti Mappa delle tessere adiacenti.
     * @return {@code true} sempre.
     */
    @Override
    public boolean verificaTessera(Map<Direzione, Tessera> tessereAdiacenti) {
        return true;
    }

    /**
     * Scarta la tessera {@code VanoBatteria}, inserendola nel mucchio visibile associato alla sua classe.
     *
     * @param mucchio Il mucchio in cui scartare la tessera.
     */
    @Override
    public void scarta(Mucchio mucchio) {
        mucchio.accettaTesseraVisibile(this, VanoBatteria.class);
    }

    /**
     * Restituisce una rappresentazione testuale in HTML della tessera.
     *
     * @return Stringa HTML con le informazioni del vano batteria.
     */
    @Override
    public String toString() {
        return
            """
              <html>
                <div style='text-align: left; font-size: 16px; font-family: sans-serif;'>
                  <ul style='list-style-position: inside; padding: 0; margin: 0;'>
              """ +
            "<li><span style='color: #2E86C1;'>Tipo:</span> Vano batteria</li>" +
            "<li><span style='color: #2E86C1;'>Batterie:</span> " + getBatterie() + "</li>" +
            """
                  </ul>
                </div>
              </html>
            """;
    }

}