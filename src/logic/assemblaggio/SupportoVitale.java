package galaxytrucker.src.logic.assemblaggio;

import java.util.EnumMap;
import java.util.Map;

/**
 * Rappresenta una tessera supporto vitale, ossia una tessera che consente di ospitare
 * un abitante alieno in una cabina adiacente.
 * Può supportare soltanto abitanti appartenenti alla specie {@link Specie#ALIENO}.
 *
 * @see Tessera
 */
public class SupportoVitale extends Tessera {

    /** Abitante alieno supportato da questa tessera. */
    private final Abitante alienoSupportato;

    /**
     * Costruttore più comune.
     *
     * @param lati               Mappa dei connettori della tessera per ogni direzione.
     * @param alienoSupportato   Abitante alieno che sarà supportato dalla tessera.
     * @param pathImmagine       Percorso dell'immagine associata alla tessera.
     * @throws IllegalArgumentException se {@code alienoSupportato} non è di specie {@code ALIENO}.
     */
    public SupportoVitale(EnumMap<Direzione, Connettore> lati, Abitante alienoSupportato, String pathImmagine) {
        this(lati, false, alienoSupportato, pathImmagine);
    }

    /**
     * Costruttore completo.
     *
     * @param lati               Mappa dei connettori della tessera per ogni direzione.
     * @param visibile           Specifica se la tessera è visibile.
     * @param alienoSupportato   Abitante alieno che sarà supportato dalla tessera.
     * @param pathImmagine       Percorso dell'immagine associata alla tessera.
     * @throws IllegalArgumentException se {@code alienoSupportato} non è di specie {@code ALIENO}.
     */
    public SupportoVitale(EnumMap<Direzione, Connettore> lati, boolean visibile, Abitante alienoSupportato, String pathImmagine) {
        super(lati, visibile, pathImmagine);

        if (alienoSupportato.getSpecie() != Specie.ALIENO) {
            String errore = "La tessera SupportoVitale non può essere associata a specie non aliene!";
            getLogger().error(errore);
            throw new IllegalArgumentException(errore);
        }

        this.alienoSupportato = alienoSupportato;
    }

    /**
     * Verifica se la tessera può essere collocata.
     * Per le tessere SupportoVitale, è sempre valida.
     *
     * @param tessereAdiacenti Mappa delle tessere adiacenti.
     * @return {@code true} sempre.
     */
    @Override
    public boolean verificaTessera(Map<Direzione, Tessera> tessereAdiacenti) {
        return true;
    }

    /**
     * Restituisce l'abitante alieno supportato da questa tessera.
     *
     * @return {@link Abitante} supportato.
     */
    @Override
    public Abitante getAlienoSupportato() {
        return alienoSupportato;
    }
    
    /**
     * Scarta la tessera {@code SupportoVitale}, inserendola nel mucchio visibile associato alla sua classe.
     *
     * @param mucchio Il mucchio in cui scartare la tessera.
     */
    @Override
    public void scarta(Mucchio mucchio) {
        mucchio.accettaTesseraVisibile(this, SupportoVitale.class);
    }

    /**
     * Restituisce una rappresentazione testuale in HTML della tessera.
     *
     * @return Stringa HTML con le informazioni del supporto vitale.
     */
    @Override
    public String toString() {
        return
            """
              <html>
                <div style='text-align: left; font-size: 16px; font-family: sans-serif;'>
                  <ul style='list-style-position: inside; padding: 0; margin: 0;'>
              """ +
            "<li><span style='color: #2E86C1;'>Tipo:</span> Supporto vitale</li>" +
            "<li><span style='color: #2E86C1;'>Supporto:</span> " + getAlienoSupportato() + "</li>" +
            """
                  </ul>
                </div>
              </html>
            """;
    }
}