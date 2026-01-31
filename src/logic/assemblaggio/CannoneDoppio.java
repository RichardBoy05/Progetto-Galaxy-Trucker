package galaxytrucker.src.logic.assemblaggio;

import java.util.EnumMap;

/**
 * Rappresenta un cannone doppio, una particolare tipologia di cannone 
 * che può essere attivato e la cui potenza di fuoco dipende dalla direzione.
 * 
 * <p>Se orientato a {@code NORD}, la potenza di fuoco raddoppia.</p>
 *
 * <p>Implementa l’interfaccia {@link Attivabile}, quindi può essere attivato.</p>
 * 
 * @see Cannone
 * @see Attivabile
 */
public class CannoneDoppio extends Cannone implements Attivabile {

    /** Indica se il cannone doppio è attivo. */
    private boolean attivo;

    /**
     * Costruttore più comune.
     * 
     * @param lati         Mappa dei connettori della tessera per ogni direzione.
     * @param cannaFuoco   Direzione in cui è orientata la canna di fuoco.
     * @param pathImmagine Percorso dell'immagine associata alla tessera.
     */
    public CannoneDoppio(EnumMap<Direzione, Connettore> lati, Direzione cannaFuoco, String pathImmagine) {
        this(lati, false, cannaFuoco, false, pathImmagine);
    }

    /**
     * Costruttore completo.
     * 
     * @param lati         Mappa dei connettori della tessera per ogni direzione.
     * @param visibile     Specifica se la tessera è visibile.
     * @param cannaFuoco   Direzione in cui è orientata la canna di fuoco.
     * @param attivo       Stato iniziale di attivazione del cannone.
     * @param pathImmagine Percorso dell'immagine associata alla tessera.
     */
    public CannoneDoppio(EnumMap<Direzione, Connettore> lati, boolean visibile, Direzione cannaFuoco, boolean attivo, String pathImmagine) {
        super(lati, visibile, cannaFuoco, pathImmagine);
        this.attivo = attivo;
    }

    /**
     * Restituisce la potenza di fuoco del cannone doppio.
     * <p>
     * Attivo: se la canna di fuoco è orientata a {@code NORD}, la potenza è 2. Altrimenti vale 1.
     * Non attivo: la potenza di fuoco è 0.
     * 
     * @return Potenza di fuoco calcolata in base alla direzione.
     */
    @Override
    public double getPotenzaDiFuoco() {
    	
    	if (attivo) {
    		return (getCannaFuoco() == Direzione.NORD) ? 2 : 1;
    	} else {
    		return 0;
    	}  
    	
    }
    
    /**
     * Indica se il cannone doppio è in grado di colpire in una determinata direzione.
     * 
     * @param direzione la direzione in cui voglio verificare se il cannone doppio possa colpire.
     * @return {@code true} se {@code direzione} coincide con la direzione della canna da fuoco ed è ATTIVO, {@code false} altrimenti.
     */
    @Override
    public boolean colpisceVerso(Direzione direzione) {
    	return (getCannaFuoco() == direzione) && isAttivo();
    }


    /**
     * Scarta la tessera {@code CannoneDoppio}, inserendola nel mucchio delle tessere visibili.
     * 
     * @param mucchio Il mucchio che accoglie la tessera scartata.
     */
    @Override
    public void scarta(Mucchio mucchio) {
        mucchio.accettaTesseraVisibile(this, CannoneDoppio.class);
    }

    /**
     * Verifica se il cannone doppio è attivo.
     *
     * @return {@code true} se attivo, {@code false} altrimenti.
     */
    @Override
    public boolean isAttivo() {
        return attivo;
    }

    /**
     * Imposta lo stato di attivazione del cannone doppio.
     *
     * @param attivo {@code true} per attivare il cannone, {@code false} per disattivarlo.
     */
    @Override
    public void setAttivo(boolean attivo) {
        this.attivo = attivo;
    }

    /**
     * Indica se il cannone doppio è attivabile.
     *
     * @return Sempre {@code true}, poiché i {@code CannoneDoppio} sono attivabili.
     */
    @Override
    public boolean isAttivabile() {
        return true;
    }

    /**
     * Restituisce una rappresentazione in formato HTML della tessera {@code CannoneDoppio},
     * comprensiva di tipo, direzione e stato di attivazione.
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
              "<li><span style='color: #2E86C1;'>Tipo:</span> Cannone doppio</li>" +
              "<li><span style='color: #2E86C1;'>Direzione:</span> " + getCannaFuoco() + "</li>" +
              "<li><span style='color: #2E86C1;'>Attivo:</span> " + ((attivo) ? "Sì" : "No") + "</li>" +
              """
                  </ul>
                </div>
              </html>
            """;
    }
}