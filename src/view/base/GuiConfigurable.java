package galaxytrucker.src.view.base;

/**
 * Definisce un contratto per la configurazione
 * dell'interfaccia grafica (GUI) di un componente. Le classi che implementano questa
 * interfaccia sono responsabili dell'inizializzazione dei componenti grafici, della
 * definizione del layout e della registrazione dei listener degli eventi.
 */
public interface GuiConfigurable {

    /**
     * Inizializza e configura i componenti grafici dell'interfaccia utente.
     */
    void setupComponents();

    /**
     * Organizza i componenti grafici nel layout desiderato.
     */
    void setupLayout();

    /**
     * Registra i listener degli eventi per i componenti dell'interfaccia.
     */
    void setupListeners();
}