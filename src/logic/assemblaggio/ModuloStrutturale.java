package galaxytrucker.src.logic.assemblaggio;

import java.util.EnumMap;
import java.util.Map;

/**
 * Rappresenta un modulo strutturale della nave.
 * 
 * <p>Il modulo strutturale non ha alcun effetto particolare e può essere posizionato ovunque
 * senza vincoli di adiacenza con altre tessere.</p>
 * 
 * @see Tessera
 */
public class ModuloStrutturale extends Tessera {

    /**
     * Costruttore più comune.
     * 
     * @param lati         Mappa dei connettori della tessera per ogni direzione.
     * @param pathImmagine Percorso dell'immagine associata alla tessera.
     */
    public ModuloStrutturale(EnumMap<Direzione, Connettore> lati, String pathImmagine) {
        this(lati, false, pathImmagine);
    }

    /**
     * Costruttore completo.
     * 
     * @param lati         Mappa dei connettori della tessera per ogni direzione.
     * @param visibile     Specifica se la tessera è visibile.
     * @param pathImmagine Percorso dell'immagine associata alla tessera.
     */
    public ModuloStrutturale(EnumMap<Direzione, Connettore> lati, boolean visibile, String pathImmagine) {
        super(lati, visibile, pathImmagine);
    }

    /**
     * Verifica se la posizione della tessera è valida rispetto alle tessere adiacenti.
     * 
     * <p>Per il {@code ModuloStrutturale} questa verifica restituisce sempre {@code true},
     * in quanto non esistono vincoli di posizionamento.</p>
     *
     * @param tessereAdiacenti Mappa delle tessere adiacenti.
     * @return {@code true} sempre.
     */
    @Override
    public boolean verificaTessera(Map<Direzione, Tessera> tessereAdiacenti) {
        return true;
    }

    /**
     * Scarta la tessera {@code ModuloStrutturale}, inserendola nel mucchio delle tessere visibili.
     * 
     * @param mucchio Il mucchio che accoglie la tessera scartata.
     */
    @Override
    public void scarta(Mucchio mucchio) {
        mucchio.accettaTesseraVisibile(this, ModuloStrutturale.class);
    }

    /**
     * Restituisce una rappresentazione in formato HTML della tessera {@code ModuloStrutturale},
     * comprensiva del tipo.
     *
     * @return Stringa HTML rappresentante la tessera.
     */
    @Override
    public String toString() {
        return 
            """
              <html>
                <div style='text-align: left; font-size: 16px; font-family: sans-serif;'>
                  <ul style='list-style-position: inside; padding: 0; margin: 0;'>
              """ +
              "<li><span style='color: #2E86C1;'>Tipo:</span> Modulo strutturale</li>" +
              """
                  </ul>
                </div>
              </html>
            """;
    }
}