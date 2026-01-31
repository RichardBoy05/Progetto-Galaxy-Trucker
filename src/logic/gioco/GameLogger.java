package galaxytrucker.src.logic.gioco;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.logging.FileHandler;
import java.util.logging.Formatter;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

/**
 * Classe singleton che gestisce la registrazione dei log per il gioco Galaxy Trucker.
 * I log includono messaggi informativi, avvisi ed errori relativi agli eventi di gioco,
 * e vengono salvati su file (log.txt).
 */

public final class GameLogger {

    private static GameLogger instance;
    private static final String LOG_FILE = "log.txt";
    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private Logger logger;
    private FileHandler fileHandler;
    private boolean enabled = false;

    /**
     * Costruttore privato per applicare il pattern Singleton.
     * Inizializza il logger e imposta un hook di arresto per chiudere correttamente il file di log.
     */
    private GameLogger() {
        logger = Logger.getLogger("GalaxyTruckersLogger");
        logger.setUseParentHandlers(false);
        logger.setLevel(Level.ALL);

        // garantisce la chiusura del file di log alla chiusura dell'applicazione
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            if (fileHandler != null) {
                fileHandler.close();
            }
        }));
    }

    /**
     * Restituisce l'unica istanza della classe GameLogger.
     *
     * @return l'istanza singleton
     */
    public static GameLogger getInstance() {
        if (instance == null) {
            instance = new GameLogger();
        }
        return instance;
    }

    /**
     * Abilita la scrittura dei log su file.
     * Se il file non esiste, viene creato e viene scritta un'intestazione iniziale.
     */
    public void enable() {
        if (enabled) return;

        try {
            File logFile = new File(LOG_FILE);
            boolean isNewFile = !logFile.exists();
            fileHandler = new FileHandler(LOG_FILE, true);

            fileHandler.setFormatter(new Formatter() {
                @Override
                public String format(LogRecord record) {
                    return String.format("[%s] [%s] %s%n",
                            LocalDateTime.now().format(DATE_FORMAT),
                            record.getLevel().getName(),
                            record.getMessage());
                }
            });

            logger.addHandler(fileHandler);
            enabled = true;

            if (isNewFile) {
                logger.info("\n========== GALAXY TRUCKER - LOG DI GIOCO ==========\n" +
                            "Questo file registra tutti gli eventi significativi delle partite di Galaxy Trucker,\n" +
                            "inclusi dettagli dei giocatori, messaggi informativi, avvisi e errori.\n" +
                            "Ogni voce è accompagnata da data e ora per favorirne la tracciabilità.\n" +
                            "N.B.: l'attività di logging può essere disabilitata prima di ogni partita.\n"+
                            "In tal caso le informazioni di gioco NON potranno essere in alcun modo recuperate.\n");
            }

        } catch (IOException e) {
            System.err.println("Errore nell'inizializzazione del logger: " + e.getMessage());
        }
    }

    /**
     * Disabilita la scrittura dei log e rilascia le risorse del file handler.
     */
    public void disable() {
        enabled = false;
        if (fileHandler != null) {
            fileHandler.close();
            logger.removeHandler(fileHandler);
            fileHandler = null;
        }
    }

    /**
     * Registra nel log l'inizio di una nuova partita, con l'elenco dei giocatori e i relativi dettagli.
     *
     * @param giocatori lista dei giocatori partecipanti alla partita
     */
    public void logInizioPartita(List<Giocatore> giocatori) {
        if (!enabled) return;

        StringBuilder sb = new StringBuilder();
        sb.append("Creazione nuova partita...");
        sb.append("\n\n==============================\n");
        sb.append("======= INIZIO PARTITA =======");
        sb.append("\n==============================\n\n");
        sb.append("Data/Ora: ").append(LocalDateTime.now().format(DATE_FORMAT)).append("\n");
        sb.append("Giocatori:\n");

        for (Giocatore p : giocatori) {
            sb.append(" - ").append(p.getNome())
              .append(" [Colore: ").append(p.getColore()).append("]\n");
        }

        logger.info(sb.toString());
    }

    /**
     * Scrive un messaggio informativo nel log.
     *
     * @param message il messaggio da registrare
     */
    public void info(String message) {
        if (enabled) logger.info(message);
    }

    /**
     * Scrive un messaggio di avviso nel log.
     *
     * @param message il messaggio da registrare
     */
    public void warning(String message) {
        if (enabled) logger.warning(message);
    }

    /**
     * Scrive un messaggio di errore nel log.
     *
     * @param message il messaggio da registrare
     */
    public void error(String message) {
        if (enabled) logger.severe(message);
    }
}