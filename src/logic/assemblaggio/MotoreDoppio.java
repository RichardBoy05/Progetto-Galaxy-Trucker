package galaxytrucker.src.logic.assemblaggio;

import java.util.EnumMap;

/**
 * Classe che rappresenta un motore doppio, una tessera motore che può essere attivata
 * e fornisce una potenza motrice doppia quando attivo.
 * 
 * <p>Ogni motore doppio possiede un tubo di scarico orientato in una specifica direzione,
 * può essere ruotato, attivato e scartato.</p>
 *
 * @see Motore
 * @see Attivabile
 */
public class MotoreDoppio extends Motore implements Attivabile {

    /** Indica se il motore doppio è attivo. */
    private boolean attivo;

    /**
     * Costruttore più comune.
     * 
     * @param lati         Mappa dei connettori della tessera per ogni direzione.
     * @param tuboScarico  Direzione del tubo di scarico del motore.
     * @param pathImmagine Percorso dell'immagine associata alla tessera.
     */
    public MotoreDoppio(EnumMap<Direzione, Connettore> lati, Direzione tuboScarico, String pathImmagine) {
        this(lati, false, tuboScarico, false, pathImmagine);
    }

    /**
     * Costruttore completo.
     * 
     * @param lati         Mappa dei connettori della tessera per ogni direzione.
     * @param visibile     Specifica se la tessera è visibile.
     * @param tuboScarico  Direzione del tubo di scarico del motore.
     * @param attivo       Stato di attivazione del motore.
     * @param pathImmagine Percorso dell'immagine associata alla tessera.
     */
    public MotoreDoppio(EnumMap<Direzione, Connettore> lati, boolean visibile, Direzione tuboScarico, boolean attivo, String pathImmagine) {
        super(lati, visibile, tuboScarico, pathImmagine);
        this.attivo = attivo;
    }

    /**
     * Restituisce la potenza motrice fornita dal motore doppio.
     * 
     * @return {@code 2} se il motore è attivo, altrimenti {@code 0}.
     */
    @Override
    public int getPotenzaMotrice() {
        return attivo ? 2 : 0;
    }

    /**
     * Restituisce lo stato di attivazione del motore doppio.
     *
     * @return {@code true} se il motore è attivo, {@code false} altrimenti.
     */
    @Override
    public boolean isAttivo() {
        return attivo;
    }

    /**
     * Imposta lo stato di attivazione del motore doppio.
     *
     * @param attivo {@code true} per attivare il motore, {@code false} per disattivarlo.
     */
    @Override
    public void setAttivo(boolean attivo) {
        this.attivo = attivo;
    }

    /**
     * Verifica se il motore doppio è attivabile.
     *
     * @return Sempre {@code true}, poiché un motore doppio è attivabile.
     */
    @Override
    public boolean isAttivabile() {
        return true;
    }
    
    /**
     * Scarta il motore doppio, inserendolo nel mucchio visibile associato alla sua classe.
     *
     * @param mucchio Il mucchio in cui scartare la tessera.
     */
    @Override
    public void scarta(Mucchio mucchio) {
        mucchio.accettaTesseraVisibile(this, MotoreDoppio.class);
    }

    /**
     * Restituisce una rappresentazione testuale HTML della tessera.
     *
     * @return Stringa HTML con le informazioni del motore doppio.
     */
    @Override
    public String toString() {
        return
            """
              <html>
                <div style='text-align: left; font-size: 16px; font-family: sans-serif;'>
                  <ul style='list-style-position: inside; padding: 0; margin: 0;'>
              """ +
              "<li><span style='color: #2E86C1;'>Tipo:</span> Motore doppio</li>" +
              "<li><span style='color: #2E86C1;'>Direzione:</span> " + getTuboScarico() + "</li>" +
              "<li><span style='color: #2E86C1;'>Attivo:</span> " + (attivo ? "Sì" : "No") + "</li>" +
              """
                  </ul>
                </div>
              </html>
            """;
    }
}