package galaxytrucker.src.view.base;

/**
 * Interfaccia per le GUI Swing che richiedono un comportamento bloccante.
 * <p>
 * Viene implementata da tutte le GUI che devono interrompere temporaneamente
 * il flusso del programma finché l'utente non ha completato l'interazione.
 * L'implementazione tipica prevede l'uso di meccanismi di sincronizzazione,
 * come {@code CountDownLatch}.
 */
public interface BlockingView {
    
    /**
     * Mostra la GUI e blocca il flusso del programma chiamante fino al termine dell'interazione utente.
     */
    void mostraEAttendi();
}