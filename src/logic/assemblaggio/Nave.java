package galaxytrucker.src.logic.assemblaggio;

import java.util.List;
import java.util.Queue;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.LinkedList;

import galaxytrucker.src.logic.eccezioni.ConfigurazioneNaveNonValidaException;
import galaxytrucker.src.logic.eccezioni.ConnessioneTesseraNonValidaException;
import galaxytrucker.src.logic.eccezioni.PiazzamentoTesseraNonValidoException;
import galaxytrucker.src.logic.eccezioni.TronconiFluttuantiException;
import galaxytrucker.src.logic.gioco.Colore;
import galaxytrucker.src.logic.gioco.GameLogger;
import galaxytrucker.src.logic.gioco.Livello;
import galaxytrucker.src.logic.volo.Merce;

/**
 * Rappresenta una nave spaziale composta da una griglia di tessere.
 * La nave è caratterizzata da una griglia, un livello di gioco, un colore
 * e può essere contrassegnata come "nave più bella".
 * Fornisce metodi per verificare la validità della configurazione,
 * calcolare varie statistiche e gestire gli abitanti e le merci.
 */
public class Nave {
    private final Griglia griglia;
    private final Livello livello;
    private boolean navePiuBella;
    private Colore colore;

    private static final GameLogger LOGGER = GameLogger.getInstance();

    /**
     * Costruttore della nave. Inizializza la griglia in base al livello
     * e posiziona la cabina iniziale al centro.
     *
     * @param livello il livello di gioco che definisce la forma della nave
     * @param colore  il colore della nave
     * @throws NullPointerException se livello o colore sono null
     */
    public Nave(Livello livello, Colore colore) {
        if (livello == null) {
            String errore = "Il parametro 'livello' non può essere nullo!";
            LOGGER.error(errore);
            throw new NullPointerException(errore);
        }

        if (colore == null) {
            String errore = "Il parametro 'colore' non può essere nullo!";
            LOGGER.error(errore);
            throw new NullPointerException(errore);
        }

        this.livello = livello;
        this.griglia = new Griglia(livello);
        this.navePiuBella = false;
        this.colore = colore;

        Coordinate centro = livello.getCentroNave();
        EnumMap<Direzione, Connettore> lati = new EnumMap<>(Direzione.class);

        for (Direzione dir : Direzione.values()) {
            lati.put(dir, Connettore.UNIVERSALE);
        }

        String immagine = "/galaxytrucker/resources/images/tessere/cabina_partenza_" + colore.toString().toLowerCase() + ".png";
        Cabina cabinaIniziale = new Cabina(lati, true, true, immagine);
        griglia.getCella(centro.getRiga(), centro.getColonna()).inserisciTessera(cabinaIniziale);
    }

    /**
     * Restituisce il colore della nave.
     *
     * @return il colore della nave
     */
    public Colore getColore() {
        return colore;
    }

    /**
     * Restituisce la griglia della nave.
     *
     * @return la griglia della nave
     */
    public Griglia getGriglia() {
        return griglia;
    }

    /**
     * Restituisce il livello associato alla nave.
     *
     * @return il livello della nave
     */
    public Livello getLivello() {
        return livello;
    }

    /**
     * Verifica se la nave è stata contrassegnata come "più bella".
     *
     * @return true se la nave è la più bella, false altrimenti
     */
    public boolean isNavePiuBella() {
        return navePiuBella;
    }

    /**
     * Imposta lo stato "nave più bella".
     *
     * @param navePiuBella true per contrassegnare la nave come più bella
     */
    public void setNavePiuBella(boolean navePiuBella) {
        this.navePiuBella = navePiuBella;
    }

    /**
     * Verifica la validità della configurazione della nave secondo le regole del
     * gioco:
     * <ol>
     * <li>Ogni tessera rispetta le regole locali</li>
     * <li>Tutte le tessere formano un unico blocco connesso</li>
     * <li>Connettori adiacenti sono compatibili</li>
     * <li>Motori e cannoni sono posizionati correttamente</li>
     * <li>Merce e abitanti sono posizionati correttamente</li>
     * </ol>
     *
     * @throws ConfigurazioneNaveNonValidaException se la configurazione non è
     *                                              valida
     * @throws ConnessioneTesseraNonValidaException se le connessioni tra tessere
     *                                              non sono valide
     * @throws PiazzamentoTesseraNonValidoException se il posizionamento di una
     *                                              tessera non è valido
     * @throws TronconiFluttuantiException          se ci sono gruppi di tessere non
     *                                              connessi
     */
    public void verificaNave() throws ConfigurazioneNaveNonValidaException {
        int righe = griglia.getAltezza();
        int colonne = griglia.getLarghezza();
        boolean[][] presente = new boolean[righe][colonne];
        List<Coordinate> celle = new ArrayList<>();

        for (int riga = 0; riga < righe; riga++) {
            for (int colonna = 0; colonna < colonne; colonna++) {
                Cella cella = griglia.getCella(riga, colonna);
                if (cella != null) {
                    Tessera tessera = cella.getTessera();

                    if (tessera != null && !tessera.verificaConnessioni(griglia.getTessereAdiacenti(cella))) {
                        throw new ConnessioneTesseraNonValidaException(cella, livello);
                    }

                    if (tessera != null && !tessera.verificaTessera(griglia.getTessereAdiacenti(cella))) {
                        throw new PiazzamentoTesseraNonValidoException(cella, livello);
                    }
                    presente[riga][colonna] = true;
                    celle.add(new Coordinate(riga, colonna));
                }
            }
        }

        if (celle.isEmpty()) {
            throw new PiazzamentoTesseraNonValidoException("Nave vuota, nessuna tessera presente.");
        }

        verificaTronconi();
    }

    /**
     * Verifica che tutte le tessere formino un unico gruppo connesso.
     *
     * @throws TronconiFluttuantiException se vengono trovati gruppi di tessere non
     *                                     connessi
     */
    public void verificaTronconi() throws TronconiFluttuantiException {
        int righe = griglia.getAltezza();
        int colonne = griglia.getLarghezza();
        int gruppiConnessi = 0;

        boolean[][] visitato = new boolean[righe][colonne];

        for (int i = 0; i < righe; i++) {
            for (int j = 0; j < colonne; j++) {
                if (!visitato[i][j]) {
                    Cella cellaIniziale = griglia.getCella(i, j);
                    Tessera tesseraIniziale = cellaIniziale.getTessera();

                    if (tesseraIniziale == null)
                        continue;

                    gruppiConnessi++;

                    Queue<Cella> coda = new LinkedList<>();
                    coda.add(cellaIniziale);
                    visitato[i][j] = true;

                    while (!coda.isEmpty()) {
                        Cella corrente = coda.poll();
                        Tessera tesseraCorrente = corrente.getTessera();
                        if (tesseraCorrente == null)
                            continue;

                        for (Direzione dir : Direzione.values()) {
                            int nuovaRiga = corrente.getRiga() + dir.deltaRiga();
                            int nuovaColonna = corrente.getColonna() + dir.deltaColonna();

                            if (nuovaRiga < 0 || nuovaColonna < 0 || nuovaRiga >= righe || nuovaColonna >= colonne)
                                continue;
                            if (visitato[nuovaRiga][nuovaColonna])
                                continue;

                            Cella vicina = griglia.getCella(nuovaRiga, nuovaColonna);
                            if (vicina == null)
                                continue;

                            Tessera tesseraVicina = vicina.getTessera();
                            if (tesseraVicina == null)
                                continue;

                            Connettore mio = tesseraCorrente.getConnettore(dir);
                            Connettore suo = tesseraVicina.getConnettore(dir.opposta());

                            if (mio != Connettore.LISCIO && suo != Connettore.LISCIO && mio.isCompatibile(suo)) {
                                visitato[nuovaRiga][nuovaColonna] = true;
                                coda.add(vicina);
                            }
                        }
                    }
                }
            }
        }

        if (gruppiConnessi > 1) {
            throw new TronconiFluttuantiException();
        }
    }

    /**
     * Calcola la potenza motrice totale della nave.
     * Include un bonus se sono presenti alieni marroni.
     *
     * @return la potenza motrice totale
     */
    public int getPotenzaMotrice() {
        int somma = 0;
        for (int riga = 0; riga < griglia.getAltezza(); riga++) {
            for (int colonna = 0; colonna < griglia.getLarghezza(); colonna++) {
                Cella cella = griglia.getCella(riga, colonna);
                if (cella != null) {
                    Tessera tessera = cella.getTessera();
                    if (tessera != null) {
                        somma += tessera.getPotenzaMotrice();
                    }
                }
            }
        }

        if (somma > 0 && getNumeroAbitantiPerTipo(Abitante.ALIENO_MARRONE) > 0) {
            somma += 2;
        }
        return somma;
    }

    /**
     * Calcola la potenza di fuoco totale della nave.
     * Include un bonus se sono presenti alieni viola.
     *
     * @return la potenza di fuoco totale
     */
    public double getPotenzaDiFuoco() {
        double somma = 0;
        for (int riga = 0; riga < griglia.getAltezza(); riga++) {
            for (int colonna = 0; colonna < griglia.getLarghezza(); colonna++) {
                Cella cella = griglia.getCella(riga, colonna);
                if (cella != null) {
                    Tessera tessera = cella.getTessera();
                    if (tessera != null) {
                        somma += tessera.getPotenzaDiFuoco();
                    }
                }
            }
        }
        if (somma > 0 && getNumeroAbitantiPerTipo(Abitante.ALIENO_VIOLA) > 0) {
            somma += 2;
        }
        return somma;
    }

    /**
     * Verifica se la nave contiene componenti attivabili.
     *
     * @return true se ci sono componenti attivabili, false altrimenti
     */
    public boolean haComponentiAttivabili() {
        for (int riga = 0; riga < griglia.getAltezza(); riga++) {
            for (int colonna = 0; colonna < griglia.getLarghezza(); colonna++) {
                Cella cella = griglia.getCella(riga, colonna);
                if (cella != null) {
                    Tessera tessera = cella.getTessera();
                    if (tessera != null && tessera.isAttivabile()) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * Calcola il numero totale di batterie presenti nella nave.
     *
     * @return il numero totale di batterie
     */
    public int getNumeroBatterie() {
        int somma = 0;
        for (int riga = 0; riga < griglia.getAltezza(); riga++) {
            for (int colonna = 0; colonna < griglia.getLarghezza(); colonna++) {
                Cella cella = griglia.getCella(riga, colonna);
                if (cella != null) {
                    Tessera tessera = cella.getTessera();
                    if (tessera != null) {
                        somma += tessera.getBatterie();
                    }
                }
            }
        }
        return somma;
    }

    /**
     * Calcola il numero totale di abitanti presenti nella nave.
     *
     * @return il numero totale di abitanti
     */
    public int getTotAbitanti() {
        int somma = 0;
        for (int riga = 0; riga < griglia.getAltezza(); riga++) {
            for (int colonna = 0; colonna < griglia.getLarghezza(); colonna++) {
                Cella cella = griglia.getCella(riga, colonna);
                if (cella != null) {
                    Tessera tessera = cella.getTessera();
                    if (tessera != null) {
                        somma += tessera.getNumeroAbitantiTotale();
                    }
                }
            }
        }
        return somma;
    }

    /**
     * Conta il numero di abitanti di un tipo specifico presenti nella nave.
     *
     * @param a il tipo di abitante da contare
     * @return il numero di abitanti del tipo specificato
     */
    public int getNumeroAbitantiPerTipo(Abitante a) {
        int somma = 0;
        for (int riga = 0; riga < griglia.getAltezza(); riga++) {
            for (int colonna = 0; colonna < griglia.getLarghezza(); colonna++) {
                Cella cella = griglia.getCella(riga, colonna);
                if (cella != null) {
                    Tessera tessera = cella.getTessera();
                    if (tessera != null) {
                        somma += tessera.getNumeroAbitantiPerTipo(a);
                    }
                }
            }
        }
        return somma;
    }

    /**
     * Conta il numero di connettori esposti (non collegati ad altre tessere).
     *
     * @return il numero di connettori esposti
     */
    public int getConnettoriEsposti() {
        int count = 0;
        for (int riga = 0; riga < griglia.getAltezza(); riga++) {
            for (int colonna = 0; colonna < griglia.getLarghezza(); colonna++) {
                Cella cella = griglia.getCella(riga, colonna);
                if (cella != null) {
                    Tessera tessera = cella.getTessera();
                    if (tessera != null) {
                        for (Direzione dir : Direzione.values()) {
                            Cella vicina = griglia.getCella(riga + dir.deltaRiga(), colonna + dir.deltaColonna());
                            if (vicina == null || vicina.getTessera() == null) {
                                if (tessera.getConnettore(dir) != Connettore.LISCIO) {
                                    count++;
                                }
                            }
                        }
                    }
                }
            }
        }
        return count;
    }

    /**
     * Conta la quantità di merce di un tipo specifico presente nella nave.
     *
     * @param m il tipo di merce da contare
     * @return la quantità di merce del tipo specificato
     */
    public int getMercePerTipo(Merce m) {
        int somma = 0;
        for (int riga = 0; riga < griglia.getAltezza(); riga++) {
            for (int colonna = 0; colonna < griglia.getLarghezza(); colonna++) {
                Cella cella = griglia.getCella(riga, colonna);
                if (cella != null) {
                    Tessera tessera = cella.getTessera();
                    if (tessera != null) {
                        somma += tessera.getMerce(m);
                    }
                }
            }
        }
        return somma;
    }

    /**
     * Verifica se la nave è protetta nella direzione specificata.
     *
     * @param d la direzione da verificare
     * @return true se esiste almeno una tessera che offre protezione nella
     *         direzione specificata
     */
    public boolean isProtetta(Direzione d) {
        for (int riga = 0; riga < griglia.getAltezza(); riga++) {
            for (int colonna = 0; colonna < griglia.getLarghezza(); colonna++) {
                Cella cella = griglia.getCella(riga, colonna);
                if (cella != null) {
                    Tessera tessera = cella.getTessera();
                    if (tessera != null && tessera.isProtetta(d)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * Rimuove gli alieni che non sono più supportati dalle condizioni attuali della
     * nave.
     * Gli alieni vengono rimossi dalle cabine che non soddisfano più i requisiti
     * per ospitarli.
     */
    public void rimuoviAlieniNonPiuSupportati() {
        for (int riga = 0; riga < griglia.getAltezza(); riga++) {
            for (int colonna = 0; colonna < griglia.getLarghezza(); colonna++) {
                Cella cella = griglia.getCella(riga, colonna);
                if (cella != null) {
                    Tessera tessera = cella.getTessera();
                    if (tessera != null && tessera.accettaAstronauta()) {
                        Cabina cabina = (Cabina) tessera;
                        cabina.rimuoviAlieniNonPiuSupportati(griglia.getTessereAdiacenti(cella));
                    }
                }
            }
        }
    }

}