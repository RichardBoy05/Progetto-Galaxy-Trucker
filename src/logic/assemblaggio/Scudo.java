package galaxytrucker.src.logic.assemblaggio;

import java.util.EnumMap;
import java.util.Map;

/**
 * Classe che rappresenta uno scudo che può proteggere i lati della nave in determinate direzioni, se attivato.
 * 
 * <p>Ogni scudo possiede una mappa di protezioni per direzione e uno stato di attivazione.</p>
 *
 * @see Tessera
 * @see Attivabile
 */
public class Scudo extends Tessera implements Attivabile {

    /** Mappa che associa ogni {@link Direzione} a un valore booleano di protezione. */
    private Map<Direzione, Boolean> protezioni;

    /** Stato di attivazione dello scudo. */
    private boolean attivo;

    /**
     * Costruttore più comune.
     *
     * @param lati        Mappa dei connettori della tessera per ogni direzione.
     * @param protezioni  Mappa delle protezioni disponibili per ogni direzione.
     * @param pathImmagine Percorso dell'immagine associata alla tessera.
     * @throws NullPointerException se {@code protezioni} è {@code null}.
     */
    public Scudo(EnumMap<Direzione, Connettore> lati, EnumMap<Direzione, Boolean> protezioni, String pathImmagine) {
        this(lati, false, protezioni, false, pathImmagine);
    }

    /**
     * Costruttore completo.
     *
     * @param lati        Mappa dei connettori della tessera per ogni direzione.
     * @param visibile    Specifica se la tessera è visibile.
     * @param protezioni  Mappa delle protezioni disponibili per ogni direzione.
     * @param attivo      Stato iniziale di attivazione dello scudo.
     * @param pathImmagine Percorso dell'immagine associata alla tessera.
     * @throws NullPointerException se {@code protezioni} è {@code null}.
     */
    public Scudo(EnumMap<Direzione, Connettore> lati, boolean visibile, EnumMap<Direzione, Boolean> protezioni, boolean attivo, String pathImmagine) {
        super(lati, visibile, pathImmagine);

        if (protezioni == null) {
            String errore = "Il parametro 'protezioni' non può essere null!";
            getLogger().error(errore);
            throw new NullPointerException(errore);
        }

        this.protezioni = protezioni;
        this.attivo = attivo;
    }

    /**
     * Verifica se lo scudo protegge la direzione specificata.
     *
     * @param direzione Direzione da controllare.
     * @return {@code true} se la direzione è protetta, {@code false} altrimenti.
     */
    @Override
    public boolean isProtetta(Direzione direzione) {
    	return protezioni.get(direzione) && isAttivo(); 

    }

    /**
     * Ruota le protezioni di 90 gradi in senso orario.
     */
    @Override
    protected void ruotaComponentiOrario() {
        Boolean nord = protezioni.get(Direzione.NORD);
        Boolean est = protezioni.get(Direzione.EST);
        Boolean sud = protezioni.get(Direzione.SUD);
        Boolean ovest = protezioni.get(Direzione.OVEST);

        protezioni.put(Direzione.NORD, ovest);
        protezioni.put(Direzione.EST, nord);
        protezioni.put(Direzione.SUD, est);
        protezioni.put(Direzione.OVEST, sud);
    }

    /**
     * Per gli scudi restituisce sempre {@code true}, perché non seguono regole di posizionamento specifiche..
     *
     * @param tessereAdiacenti Mappa delle tessere adiacenti.
     * @return {@code true} sempre.
     */
    @Override
    public boolean verificaTessera(Map<Direzione, Tessera> tessereAdiacenti) {
        return true;
    }

    /**
     * Scarta lo scudo, inserendolo nel mucchio visibile associato alla sua classe.
     *
     * @param mucchio Il mucchio in cui scartare la tessera.
     */
    @Override
    public void scarta(Mucchio mucchio) {
        mucchio.accettaTesseraVisibile(this, Scudo.class);
    }

    /**
     * Restituisce lo stato di attivazione dello scudo.
     *
     * @return {@code true} se lo scudo è attivo, {@code false} altrimenti.
     */
    @Override
    public boolean isAttivo() {
        return attivo;
    }

    /**
     * Imposta lo stato di attivazione dello scudo.
     *
     * @param attivo Nuovo stato di attivazione.
     */
    @Override
    public void setAttivo(boolean attivo) {
        this.attivo = attivo;
    }

    /**
     * Indica se la tessera è attivabile.
     *
     * @return {@code true}.
     */
    @Override
    public boolean isAttivabile() {
        return true;
    }

    /**
     * Restituisce una rappresentazione testuale HTML della tessera.
     *
     * @return Stringa HTML con le informazioni dello scudo.
     */
    @Override
    public String toString() {
        return
            """
              <html>
                <div style='text-align: left; font-size: 16px; font-family: sans-serif;'>
                  <ul style='list-style-position: inside; padding: 0; margin: 0;'>
              """ +
            "<li><span style='color: #2E86C1;'>Tipo:</span> Scudo</li>" +
            "<li><span style='color: #2E86C1;'>Protezione Nord:</span> " + (protezioni.get(Direzione.NORD) ? "Sì" : "No") + "</li>" +
            "<li><span style='color: #2E86C1;'>Protezione Est:</span> " + (protezioni.get(Direzione.EST) ? "Sì" : "No") + "</li>" +
            "<li><span style='color: #2E86C1;'>Protezione Sud:</span> " + (protezioni.get(Direzione.SUD) ? "Sì" : "No") + "</li>" +
            "<li><span style='color: #2E86C1;'>Protezione Ovest:</span> " + (protezioni.get(Direzione.OVEST) ? "Sì" : "No") + "</li>" +
            "<li><span style='color: #2E86C1;'>Attivo:</span> " + (attivo ? "Sì" : "No") + "</li>" +
            """
                  </ul>
                </div>
              </html>
            """;
    }
}