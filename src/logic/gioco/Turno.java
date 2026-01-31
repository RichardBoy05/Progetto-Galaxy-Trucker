package galaxytrucker.src.logic.gioco;

/**
 * Questa interfaccia rappresenta un turno che può
 * essere eseguito durante lo svolgimento della partita.
 * In particolare, nel gioco distinguiamo turni di
 * costruzione (fase di assemblaggio) e di evento (fase di volo).
 * 
 * @see Costruzione
 * @see Evento
 * 
 */
public interface Turno {

    /**
     * Esegue la logica associata al turno. L'implementazione di questo metodo
     * deve definire cosa accade quando questo turno viene eseguito nel contesto del gioco.
     */
    void esegui();
}