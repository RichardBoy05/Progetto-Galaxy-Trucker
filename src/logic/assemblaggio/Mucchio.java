package galaxytrucker.src.logic.assemblaggio;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Deque;
import java.util.EnumMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import galaxytrucker.src.logic.gioco.GameLogger;

/**
 * Rappresenta il mucchio di tessere di una partita, contenente sia tessere
 * visibili (classificate per tipo) sia tessere nascoste estraibili casualmente.
 * Fornisce operazioni per il recupero, la gestione e l'inizializzazione delle tessere.
 */
public class Mucchio {

    /** Logger di gioco. */
    private static final GameLogger LOGGER = GameLogger.getInstance();

    /** Lista contenente le tessere nascoste, pescabili casualmente. */
    private List<Tessera> tessereNascoste;

    /** Mappa contenente le tessere visibili, organizzate per tipo. */
    private Map<Class<? extends Tessera>, Deque<Tessera>> tessereVisibili;

    /**
     * Costruttore della classe Mucchio.
     * Inizializza le strutture dati e carica le tessere leggendo da file.
     * Il gioco prevede che all'inizio tutte le tessere siano nascoste.
     */
    public Mucchio() {
        tessereNascoste = new ArrayList<>();
        tessereVisibili = new LinkedHashMap<>();
        LOGGER.info("Inizializzazione del mucchio di tessere...");
        inizializzaMucchio();
    }

    /**
     * Fornisce una tessera nascosta dal mucchio (rimozione dalla testa),
     * secondo l'ordine determinato dopo il mescolamento iniziale.
     *
     * @return una {@link Tessera} casuale o {@code null} se la pila è vuota.
     */
    public Tessera fornisciTesseraNascosta() {
        if (tessereNascoste.isEmpty()) return null;
        return tessereNascoste.remove(0);
    }

    /**
     * Fornisce la prima tessera disponibile tra quelle visibili del tipo specificato.
     *
     * @param tipo la classe della tessera desiderata.
     * @return la {@link Tessera} visibile o {@code null} se non disponibile (pila nulla o vuota).
     * @throws NullPointerException se {@code tipo} è {@code null}.
     */
    public Tessera fornisciTesseraVisibile(Class<? extends Tessera> tipo) {
    	
    	if (tipo == null) {
    		String errore = "Il parametro 'tipo' non può essere nullo!";
    		LOGGER.error(errore);
    		throw new NullPointerException(errore);
    	}
    	
        Deque<Tessera> pila = tessereVisibili.get(tipo);
        return pila != null ? pila.pollFirst() : null;
    }

    /**
     * Aggiunge una tessera visibile in cima alla pila del tipo specificato.
     * Se la pila per quel tipo non esiste, viene creata.
     *
     * @param tessera la tessera da aggiungere.
     * @param tipo la classe della tessera.
     * @throws NullPointerException se {@code tessera} o {@code tipo} sono {@code null}.
     */
    public void accettaTesseraVisibile(Tessera tessera, Class<? extends Tessera> tipo) {
    	
    	if (tessera == null) {
    		String errore = "Il parametro 'tessera' non può essere nullo!";
    		LOGGER.error(errore);
    		throw new NullPointerException(errore);
    	}
    	
    	if (tipo == null) {
    		String errore = "Il parametro 'tipo' non può essere nullo!";
    		LOGGER.error(errore);
    		throw new NullPointerException(errore);
    	}
    	
        tessereVisibili.putIfAbsent(tipo, new ArrayDeque<>());
        tessereVisibili.get(tipo).addFirst(tessera);
    }
    
    /**
     * Restituisce il numero di tessere visibili di un determinato tipo.
     * Restituisce 0 se il tipo specificato non esiste ancora nella mappa.
     *
     * @param tipo la classe della tessera.
     * @throws NullPointerException se {@code tipo} è {@code null}.
     */
    public int getNumeroTessereVisibiliPerTipo(Class<? extends Tessera> tipo) {
    	
    	if (tipo == null) {
    		String errore = "Il parametro 'tipo' non può essere nullo!";
    		LOGGER.error(errore);
    		throw new NullPointerException(errore);
    	}
    	
    	if (tessereVisibili.get(tipo) == null) return 0;
    	return tessereVisibili.get(tipo).size();
    }

    /**
     * Sposta la prima tessera visibile del tipo specificato in fondo alla sua pila.
     * Se la pila è vuota o inesistente, non esegue alcuna azione.
     *
     * @param tipo la classe della tessera.
     * @throws NullPointerException se {@code tipo} è {@code null}.
     */
    public void mandaInFondoTesseraVisibile(Class<? extends Tessera> tipo) {
    	
    	if (tipo == null) {
    		String errore = "Il parametro 'tipo' non può essere nullo!";
    		LOGGER.error(errore);
    		throw new NullPointerException(errore);
    	}
    	
        Deque<Tessera> pila = tessereVisibili.get(tipo);
        if (pila == null || pila.isEmpty()) return;
        Tessera t = pila.pollFirst();
        pila.offerLast(t);
    }
    
    /**
     * Restituisce una copia della mappa delle tessere visibili.
     * <p>
     * Questo metodo fornisce una copia utile per la costruzione dell'interfaccia grafica.
     * Non viene restituito l'oggetto reale perché le modifiche ad esso sono gestite
     * esclusivamente mediante gli altri metodi offerti dalla classe {@code Mucchio}.
     *
     * @return una copia della mappa delle tessere visibili, in cui ogni deque è anch'essa copiata
     */
    public Map<Class<? extends Tessera>, Deque<Tessera>> getCopiaTessereVisibili() {
        Map<Class<? extends Tessera>, Deque<Tessera>> copia = new LinkedHashMap<>();
        for (Map.Entry<Class<? extends Tessera>, Deque<Tessera>> entry : tessereVisibili.entrySet()) {
            copia.put(entry.getKey(), new ArrayDeque<>(entry.getValue()));
        }
        return copia;
    }
    
    /**
     * Scarta una tessera, delegando l’azione alla tessera stessa.
     *
     * @param tessera la tessera da scartare.
     * @throws NullPointerException se {@code tessera} è {@code null}.
     */
    public void scartaTessera(Tessera tessera) {
    	
    	if (tessera == null) {
    		String errore = "Il parametro 'tessera' non può essere nullo!";
    		LOGGER.error(errore);
    		throw new NullPointerException(errore);
    	}
    	
    	tessera.scarta(this);
    }

    /**
     * Inizializza il mucchio di tessere leggendo i dati dal file di testo tessere.txt.
     * Ogni riga rappresenta una tessera con i propri parametri specificati tramite appositi separatori.
     * Gestisce la creazione dinamica delle diverse sottoclassi di {@link Tessera}.
     * Infine, mescola casualmente le tessere.
     * 
     * @throws IllegalArgumentException se la tessera letta da file non è riconosciuta fra le tessere esistenti.
     */
    private void inizializzaMucchio() {
        LOGGER.info("Caricamento delle tessere da file...");
        BufferedReader reader = null;

        try {
            InputStream is = getClass().getResourceAsStream("/galaxytrucker/resources/data/tessere.txt");

            if (is == null) {
                throw new FileNotFoundException("Il file tessere.txt non è stato trovato nelle risorse.");
            }

            reader = new BufferedReader(new InputStreamReader(is));
            String riga;

            while ((riga = reader.readLine()) != null) {
                if (riga.isBlank() || riga.trim().startsWith("#")) continue;

                String[] parti = riga.split(";");
                String tipo = parti[0];
                String latiStr = parti[1];
                EnumMap<Direzione, Connettore> lati = analizzaConnettori(latiStr);

                StringBuilder altriParametriBuilder = new StringBuilder();
                for (int i = 2; i < parti.length; i++) {
                    if (i > 2) altriParametriBuilder.append(";");
                    altriParametriBuilder.append(parti[i]);
                }
                String altriParametri = altriParametriBuilder.toString();

                String nomeFile = generaNomeFileImmagine(tipo, latiStr, altriParametri);
                String pathImmagine = "/galaxytrucker/resources/images/tessere/" + nomeFile + ".png";

                Tessera tessera;

                switch (tipo) {
                    case "Cabina":
                        tessera = new Cabina(lati, pathImmagine); break;
                    case "CannoneSingolo":
                        tessera = new CannoneSingolo(lati, Direzione.valueOf(parti[2]), pathImmagine); break;
                    case "CannoneDoppio":
                        tessera = new CannoneDoppio(lati, Direzione.valueOf(parti[2]), pathImmagine); break;
                    case "MotoreSingolo":
                        tessera = new MotoreSingolo(lati, Direzione.valueOf(parti[2]), pathImmagine); break;
                    case "MotoreDoppio":
                        tessera = new MotoreDoppio(lati, Direzione.valueOf(parti[2]), pathImmagine); break;
                    case "ModuloStrutturale":
                        tessera = new ModuloStrutturale(lati, pathImmagine); break;
                    case "Scudo":
                        EnumMap<Direzione, Boolean> protezioni = analizzaProtezioni(parti[2]);
                        tessera = new Scudo(lati, protezioni, pathImmagine); break;
                    case "Stiva":
                        String[] stivaArgs = parti[2].split(",");
                        int scomparti = Integer.parseInt(stivaArgs[0]);
                        boolean preziosa = Boolean.parseBoolean(stivaArgs[1]);
                        tessera = new Stiva(lati, scomparti, preziosa, pathImmagine); break;
                    case "SupportoVitale":
                        Abitante tipoAlieno = Abitante.valueOf(parti[2]);
                        tessera = new SupportoVitale(lati, tipoAlieno, pathImmagine); break;
                    case "VanoBatteria":
                        int batterie = Integer.parseInt(parti[2]);
                        tessera = new VanoBatteria(lati, batterie, pathImmagine); break;
                    default:
                        throw new IllegalArgumentException("Tipo di tessera sconosciuto: " + tipo);
                }

               tessereNascoste.add(tessera);
            }

        } catch (IOException e) {
            LOGGER.error(e.getMessage());
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    LOGGER.warning("Errore durante la chiusura del BufferedReader: " + e.getMessage());
                }
            }
        }
        
        Collections.shuffle(tessereNascoste);
        LOGGER.info("Caricate " + tessereNascoste.size() + " tessere nel mucchio.");
    }

    /**
     * Analizza una stringa di connettori e la converte in una mappa ordinata per direzione.
     *
     * @param input la stringa in formato "NORD=SINGOLO,SUD=LISCIO,..."
     * @return una {@link EnumMap} contenente i connettori per ogni {@link Direzione}.
     * @throws NullPointerException se {@code input} è {@code null}.
     */
    private EnumMap<Direzione, Connettore> analizzaConnettori(String input) {
    	
    	if (input == null) {
    		String errore = "Il parametro 'input' non può essere nullo!";
    		LOGGER.error(errore);
    		throw new NullPointerException(errore);
    	}
    	
        EnumMap<Direzione, Connettore> lati = new EnumMap<>(Direzione.class);
        for (String pair : input.split(",")) {
            String[] kv = pair.split("=");
            lati.put(Direzione.valueOf(kv[0]), Connettore.valueOf(kv[1]));
        }
        return lati;
    }

    /**
     * Analizza una stringa di protezioni e la converte in una mappa per direzione.
     *
     * @param input la stringa in formato "NORD=true,SUD=false,..."
     * @return una {@link EnumMap} con i booleani per ogni {@link Direzione}.
     * @throws NullPointerException se {@code input} è {@code null}.
     */
    private EnumMap<Direzione, Boolean> analizzaProtezioni(String input) {
    	
    	if (input == null) {
    		String errore = "Il parametro 'input' non può essere nullo!";
    		LOGGER.error(errore);
    		throw new NullPointerException(errore);
    	}
    	
        EnumMap<Direzione, Boolean> protezioni = new EnumMap<>(Direzione.class);
        for (String pair : input.split(",")) {
            String[] kv = pair.split("=");
            protezioni.put(Direzione.valueOf(kv[0]), Boolean.parseBoolean(kv[1]));
        }
        return protezioni;
    }

    /**
     * Genera il nome del file immagine associato a una tessera.
     *
     * @param tipo il tipo della tessera.
     * @param latiStr la rappresentazione dei lati.
     * @param altriParametri eventuali parametri aggiuntivi.
     * @return una stringa che rappresenta il nome del file immagine.
     * @throws NullPointerException se uno dei parametri passati al metodo è {@code null}.
     */
    private String generaNomeFileImmagine(String tipo, String latiStr, String altriParametri) {
    	
    	if (tipo == null || latiStr == null || altriParametri == null) {
    		String errore = "I parametri 'tipo', 'latiStr' e 'altriParametri' non possono essere nulli!";
    		LOGGER.error(errore);
    		throw new NullPointerException(errore);
    	}
    	
        return (tipo + "_" + latiStr + (altriParametri.isEmpty() ? "" : "_" + altriParametri))
                .replace(",", "-");
    }

}