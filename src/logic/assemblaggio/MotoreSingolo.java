package galaxytrucker.src.logic.assemblaggio;

import java.util.EnumMap;

/**
 * Classe che rappresenta un motore singolo, una tessera motore che fornisce
 * una potenza motrice fissa pari a 1.
 * 
 * <p>Ogni motore singolo possiede un tubo di scarico orientato in una direzione,
 * può essere ruotato e scartato, ma non ha uno stato di attivazione come il motore doppio.</p>
 *
 * @see Motore
 */
public class MotoreSingolo extends Motore {

    /**
     * Costruttore più comune.
     *
     * @param lati         Mappa dei connettori della tessera per ogni direzione.
     * @param tuboScarico  Direzione del tubo di scarico del motore.
     * @param pathImmagine Percorso dell'immagine associata alla tessera.
     */
    public MotoreSingolo(EnumMap<Direzione, Connettore> lati, Direzione tuboScarico, String pathImmagine) {
        this(lati, false, tuboScarico, pathImmagine);
    }

    /**
     * Costruttore completo.
     *
     * @param lati         Mappa dei connettori della tessera per ogni direzione.
     * @param visibile     Specifica se la tessera è visibile.
     * @param tuboScarico  Direzione del tubo di scarico del motore.
     * @param pathImmagine Percorso dell'immagine associata alla tessera.
     */
    public MotoreSingolo(EnumMap<Direzione, Connettore> lati, boolean visibile, Direzione tuboScarico, String pathImmagine) {
        super(lati, visibile, tuboScarico, pathImmagine);
    }

    /**
     * Restituisce la potenza motrice fornita dal motore singolo.
     *
     * @return {@code 1}, valore fisso della potenza motrice.
     */
    @Override
    public int getPotenzaMotrice() {
        return 1;
    }

    /**
     * Scarta il motore singolo, inserendolo nel mucchio visibile associato alla sua classe.
     *
     * @param mucchio Il mucchio in cui scartare la tessera.
     */
    @Override
    public void scarta(Mucchio mucchio) {
        mucchio.accettaTesseraVisibile(this, MotoreSingolo.class);
    }
    
    /**
     * Restituisce una rappresentazione testuale HTML della tessera.
     *
     * @return Stringa HTML con le informazioni del motore singolo.
     */
    @Override
    public String toString() {
        return
            """
              <html>
                <div style='text-align: left; font-size: 16px; font-family: sans-serif;'>
                  <ul style='list-style-position: inside; padding: 0; margin: 0;'>
              """ +
              "<li><span style='color: #2E86C1;'>Tipo:</span> Motore singolo</li>" +
              "<li><span style='color: #2E86C1;'>Direzione:</span> " + getTuboScarico() + "</li>" +
              """
                  </ul>
                </div>
              </html>
            """;
    }
}