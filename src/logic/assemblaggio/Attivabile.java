package galaxytrucker.src.logic.assemblaggio;


/**
 * Interfaccia che definisce il comportamento di una tessera che può essere
 * attivata.
 * 
 *<p>Le tessere attivabili in base alle regole del gioco sono:
 * <ul>
 *   <li>{@link MotoreDoppio}</li>
 *   <li>{@link CannoneDoppio}</li>
 *   <li>{@link Scudo}</li>
 * </ul>
 */
public interface Attivabile {

    /**
     * Restituisce lo stato di attivazione dell'elemento.
     * 
     * @return {@code true} se l'elemento è attivo, {@code false} altrimenti
     */
    boolean isAttivo();

    /**
     * Imposta lo stato di attivazione dell'elemento.
     * 
     * @param attivo {@code true} per attivare l'elemento, {@code false} per disattivarlo
     */
    void setAttivo(boolean attivo);
}