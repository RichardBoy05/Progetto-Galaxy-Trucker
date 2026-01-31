package galaxytrucker.src.logic.assemblaggio;

import java.util.EnumMap;

/**
 * Rappresenta un cannone singolo.
 * 
 * <p>Se orientato a {@code NORD}, la potenza di fuoco è 1. In qualsiasi altra direzione, la potenza è 0.5.</p>
 *
 * @see Cannone
 */
public class CannoneSingolo extends Cannone {

    /**
     * Costruttore più comune.
     * 
     * @param lati         Mappa dei connettori della tessera per ogni direzione.
     * @param cannaFuoco   Direzione in cui è orientata la canna di fuoco.
     * @param pathImmagine Percorso dell'immagine associata alla tessera.
     */
    public CannoneSingolo(EnumMap<Direzione, Connettore> lati, Direzione cannaFuoco, String pathImmagine) {
        this(lati, false, cannaFuoco, pathImmagine);
    }

    /**
     * Costruttore completo.
     * 
     * @param lati         Mappa dei connettori della tessera per ogni direzione.
     * @param visibile     Specifica se la tessera è visibile.
     * @param cannaFuoco   Direzione in cui è orientata la canna di fuoco.
     * @param pathImmagine Percorso dell'immagine associata alla tessera.
     */
    public CannoneSingolo(EnumMap<Direzione, Connettore> lati, boolean visibile, Direzione cannaFuoco, String pathImmagine) {
        super(lati, visibile, cannaFuoco, pathImmagine);
    }

    /**
     * Restituisce la potenza di fuoco del cannone singolo.
     * 
     * <p>Se la canna di fuoco è orientata a {@code NORD}, la potenza è 1. 
     * Altrimenti vale 0.5.</p>
     *
     * @return Potenza di fuoco calcolata in base alla direzione.
     */
    @Override
    public double getPotenzaDiFuoco() {
        return (getCannaFuoco() == Direzione.NORD) ? 1 : 0.5;
    }
    
    /**
     * Indica se il cannone è in grado di colpire in una determinata direzione.
     * 
     * @param direzione la direzione in cui voglio verificare se il cannone possa colpire.
     * @return {@code true} se {@code direzione} coincide con la direzione della canna da fuoco, {@code false} altrimenti.
     */
    @Override
    public boolean colpisceVerso(Direzione direzione) {
    	return getCannaFuoco() == direzione;
    }

    /**
     * Scarta la tessera {@code CannoneSingolo}, inserendola nel mucchio delle tessere visibili.
     * 
     * @param mucchio Il mucchio che accoglie la tessera scartata.
     */
    @Override
    public void scarta(Mucchio mucchio) {
        mucchio.accettaTesseraVisibile(this, CannoneSingolo.class);
    }

    /**
     * Restituisce una rappresentazione in formato HTML della tessera {@code CannoneSingolo},
     * comprensiva di tipo e direzione della canna di fuoco.
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
              "<li><span style='color: #2E86C1;'>Tipo:</span> Cannone singolo</li>" +
              "<li><span style='color: #2E86C1;'>Direzione:</span> " + getCannaFuoco() + "</li>" +
              """
                  </ul>
                </div>
              </html>
            """;
    }
}