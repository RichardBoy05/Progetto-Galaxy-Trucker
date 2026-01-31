package galaxytrucker.src.logic.volo;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import galaxytrucker.src.logic.assemblaggio.Direzione;
import galaxytrucker.src.logic.gioco.Giocatore;
import galaxytrucker.src.logic.gioco.Livello;

/**
 * La classe rappresenta il mazzo di carte evento utilizzato nella fase di volo del gioco.
 * Essa si occupa di inizializzare, mescolare e distribuire le carte evento secondo il livello di gioco corrente.
 * <p>
 * <strong>NOTA IMPORTANTE:</strong> si è scelto di procedere tramite "hardcoding" delle carte evento anziché lettura da file per i seguenti motivi:
 * <ul>
 *   <li>Mancanza di tempo per implementarlo, si è preferito valorizzare altri aspetti del progetto.</li>
 *   <li>A differenza delle tessere (circa 152), le carte sono circa 60, per cui generarle da file risulta non così necessario.</li>
 *   <li>Le carte presentano parametri molto variegati rispetto alle tessere, il che rende complesso creare un sistema di conversione compatto e potenzialmente estendibile.</li>
 * </ul>
 * Per questi motivi, si è optato per l'hardcoding delle carte. Si precisa ancora che con un tempo più ampio a disposizione si sarebbe potuto procedere
 * nell'implementazione di un sistema di lettura da file anche in questo caso.
 * </p>
 * 
 * <p>Il mazzo gestisce sia le carte effettive di gioco che quelle sbirciabili, organizzandole a seconda del livello (Prova, I, II, III).</p>
 */
public class Mazzo {

	/** Percorso delle immagini associate alle carte evento. */
    private static final String DIR_PATH = "/galaxytrucker/resources/images/carte/";
    
    // altri attributi
    
    private final List<Giocatore> giocatori;
    private final Livello livello;
    
    private final Queue<Evento> codaEventi;
    private final List<Evento> carteSbirciabili;
    
    private final List<Evento> carteLivelloProva;
    private final List<Evento> carteLivelloUno;
    private final List<Evento> carteLivelloDue;
    private final List<Evento> carteLivelloTre;
    
    /**
     * Costruisce un nuovo {@code Mazzo} per i giocatori e il livello specificato.
     * Inizializza le carte hardcoded e genera il mazzo da utilizzare nella partita.
     *
     * @param giocatori lista dei giocatori partecipanti alla partita.
     * @param livello livello di difficoltà corrente (P, I, II, III).
     * @throws NullPointerException se {@code giocatori} o {@code livello} sono {@code null}.
     */
    public Mazzo(List<Giocatore> giocatori, Livello livello) {
    	
    	if (giocatori == null) {
    		String errore = "Il parametro 'giocatori' non può essere nullo!";
    		Evento.getLogger().error(errore);
    		throw new NullPointerException(errore);
    	}
    	
    	if (livello == null) {
    		String errore = "Il parametro 'livello' non può essere nullo!";
    		Evento.getLogger().error(errore);
    		throw new NullPointerException(errore);
    	}
    	
    	this.giocatori = giocatori;
    	this.livello = livello;
    	
    	codaEventi = new ArrayDeque<>();
    	carteSbirciabili = new ArrayList<>();
    	carteLivelloProva = new ArrayList<>();
    	carteLivelloUno = new ArrayList<>();
    	carteLivelloDue = new ArrayList<>();
    	carteLivelloTre = new ArrayList<>();
    	
    	Evento.getLogger().info("Inizializzazione del mazzo di carte evento...");
    	
    	setupCarteLivelloDiProva();
    	setupCarteLivelloUno();
    	setupCarteLivelloDue();
    	setupCarteLivelloTre();
    	
    	generaMazzoSbirciabile();  	
    	generaMazzoDiGioco();
    	
    	Evento.getLogger().info("Mazzo di gioco generato con successo!");
    	
    }
    
    /**
     * Genera un mazzo di carte sbirciabili. Non utilizzato se il livello è di prova ({@code Livello.P}).
     */
    private void generaMazzoSbirciabile() {
    	
    	if (livello != Livello.P) Evento.getLogger().info("Generazione del mazzo di carte sbirciabili...");
    	
    	carteSbirciabili.addAll(generaMazzetto());
    	carteSbirciabili.addAll(generaMazzetto()); 
    	carteSbirciabili.addAll(generaMazzetto()); 
    	Collections.shuffle(carteSbirciabili);
    }
    
    /**
     * Genera il mazzo di carte evento che verrà effettivamente usato nella partita.
     */
    private void generaMazzoDiGioco() {
    	
    	Evento.getLogger().info("Generazione del mazzo di gioco...");
    	
    	if (livello == Livello.P) {
    		codaEventi.addAll(carteLivelloProva);
    	} else {
    		codaEventi.addAll(carteSbirciabili);
    		codaEventi.addAll(generaMazzetto()); 
    	}
    	
    }
    
    /**
     * Genera un "mazzetto" di carte evento casuale in base al livello corrente.
     * Il mazzetto è una selezione parziale delle carte appropriate per il livello.
     *
     * @return lista di carte evento selezionate.
     */
    private List<Evento> generaMazzetto() {
    	
    	List<Evento> mazzetto = new ArrayList<>();
    	
    	switch (livello) {
		case P:
			// non è previsto alcun mazzetto nella modalità di prova, poiché vengono usate tutte e le sole carte di prova
			break;
		case I:
			mazzetto.add(carteLivelloUno.remove(0));
			mazzetto.add(carteLivelloUno.remove(0));
			break;
		case II:
			mazzetto.add(carteLivelloUno.remove(0));
			mazzetto.add(carteLivelloDue.remove(0));
			mazzetto.add(carteLivelloDue.remove(0));
			break;
		case III:
			mazzetto.add(carteLivelloUno.remove(0));
			mazzetto.add(carteLivelloDue.remove(0));
			mazzetto.add(carteLivelloTre.remove(0));
			mazzetto.add(carteLivelloTre.remove(0));
			break;
		default:
			break;
    	
    	}
    	
    	return mazzetto;
    }
    
    /**
     * Inizializza e mescola le carte evento utilizzate nel {@code Livello.P}.
     */
    private void setupCarteLivelloDiProva() {
    	
    	Evento.getLogger().info("Generazione del mazzo di prova...");
    	
    	List<Pianeta> pianeti = new ArrayList<>();
    	pianeti.add(generaPianeta(2, 0, 0, 0));
    	pianeti.add(generaPianeta(1, 2, 0, 0));
    	pianeti.add(generaPianeta(0, 0, 1, 0));
    	
    	List<Meteorite> meteoriti = new ArrayList<>();
    	meteoriti.add(generaMeteorite(DimensioneColpo.GRANDE, Direzione.NORD));
    	meteoriti.add(generaMeteorite(DimensioneColpo.PICCOLO, Direzione.OVEST));
    	meteoriti.add(generaMeteorite(DimensioneColpo.PICCOLO, Direzione.EST));
    	
    	carteLivelloProva.add(new Pianeti(giocatori, Livello.P, pianeti, 2, DIR_PATH + "p_pianeti.png"));
    	carteLivelloProva.add(new PolvereStellare(giocatori, Livello.P, DIR_PATH + "p_polverestellare.png"));
    	carteLivelloProva.add(new StazioneAbbandonata(giocatori, Livello.P, 5, 1, generaMerci(0, 0, 1, 1), DIR_PATH + "p_stazioneabbandonata.png"));
    	carteLivelloProva.add(new NaveAbbandonata(giocatori, Livello.P, 4, 3, 1, DIR_PATH + "p_naveabbandonata.png"));
    	carteLivelloProva.add(new Contrabbandieri(giocatori, Livello.P, 4, 1, generaMerci(0, 1, 1, 1), 2, DIR_PATH + "p_contrabbandieri.png"));
    	carteLivelloProva.add(new SpazioAperto(giocatori, Livello.P, DIR_PATH + "p_spazioaperto.png"));
    	carteLivelloProva.add(new PioggiaDiMeteoriti(giocatori, Livello.P, meteoriti, DIR_PATH + "p_pioggiadimeteoriti.png"));

    	Collections.shuffle(carteLivelloProva);
    }
    
    /**
     * Inizializza e mescola le carte evento utilizzate nel {@code Livello.I}.
     */
    private void setupCarteLivelloUno() {
    	
    	Evento.getLogger().info("Generazione del mazzo di livello uno...");
    	
    	List<Cannonata> cannonate = new ArrayList<>();
    	cannonate.add(generaCannonata(DimensioneColpo.PICCOLO, Direzione.NORD));
    	cannonate.add(generaCannonata(DimensioneColpo.GRANDE, Direzione.NORD));
    	cannonate.add(generaCannonata(DimensioneColpo.PICCOLO, Direzione.NORD));
    	
    	List<Pianeta> pianeti1 = new ArrayList<>();
    	pianeti1.add(generaPianeta(0, 2, 1, 1));
    	pianeti1.add(generaPianeta(0, 0, 2, 0));  	
    	List<Pianeta> pianeti2 = new ArrayList<>();
    	pianeti2.add(generaPianeta(1, 3, 0, 1));
    	pianeti2.add(generaPianeta(1, 1, 1, 0));
    	pianeti2.add(generaPianeta(1, 3, 0, 0));
    	pianeti2.add(generaPianeta(1, 0, 0, 1));   	
    	List<Pianeta> pianeti3 = new ArrayList<>();
    	pianeti3.add(generaPianeta(0, 0, 0, 2));
    	pianeti3.add(generaPianeta(0, 0, 1, 0));
    	pianeti3.add(generaPianeta(0, 3, 0, 0));
    	
    	List<Meteorite> meteoriti1 = new ArrayList<>();
    	meteoriti1.add(generaMeteorite(DimensioneColpo.GRANDE, Direzione.NORD));
    	meteoriti1.add(generaMeteorite(DimensioneColpo.PICCOLO, Direzione.OVEST));
    	meteoriti1.add(generaMeteorite(DimensioneColpo.PICCOLO, Direzione.EST));
    	List<Meteorite> meteoriti2 = new ArrayList<>();
    	meteoriti2.add(generaMeteorite(DimensioneColpo.PICCOLO, Direzione.NORD));
    	meteoriti2.add(generaMeteorite(DimensioneColpo.GRANDE, Direzione.OVEST));
    	meteoriti2.add(generaMeteorite(DimensioneColpo.PICCOLO, Direzione.OVEST));
    	meteoriti2.add(generaMeteorite(DimensioneColpo.GRANDE, Direzione.OVEST));
    	meteoriti2.add(generaMeteorite(DimensioneColpo.PICCOLO, Direzione.SUD));
    	
    	carteLivelloUno.add(new NaveAbbandonata(giocatori, Livello.I, 3, 2, 1, DIR_PATH + "1_naveabbandonata1.png"));  	
    	carteLivelloUno.add(new Pianeti(giocatori, Livello.I, pianeti1, 3, DIR_PATH + "1_pianeti1.png"));
    	carteLivelloUno.add(new Pianeti(giocatori, Livello.I, pianeti2, 3, DIR_PATH + "1_pianeti2.png"));
    	carteLivelloUno.add(new Pianeti(giocatori, Livello.I, pianeti3, 1, DIR_PATH + "1_pianeti3.png"));
    	carteLivelloUno.add(new PioggiaDiMeteoriti(giocatori, Livello.I, meteoriti1, DIR_PATH + "1_pioggiadimeteoriti1.png"));   	
    	carteLivelloUno.add(new PioggiaDiMeteoriti(giocatori, Livello.I, meteoriti2, DIR_PATH + "1_pioggiadimeteoriti2.png"));   	
    	carteLivelloUno.add(new Pirati(giocatori, Livello.I, 5, 1, 4, cannonate, DIR_PATH + "1_pirati1.png"));
    	carteLivelloUno.add(new Schiavisti(giocatori, Livello.I, 5, 3, 1, 6, DIR_PATH + "1_schiavisti1.png"));
    	carteLivelloUno.add(new StazioneAbbandonata(giocatori, Livello.I, 6, 1, generaMerci(2, 0, 0, 0), DIR_PATH + "1_stazioneabbandonata1.png"));
    	carteLivelloUno.add(new SpazioAperto(giocatori, Livello.I, DIR_PATH + "1_spazioaperto.png"));
    	carteLivelloUno.add(new SpazioAperto(giocatori, Livello.I, DIR_PATH + "1_spazioaperto.png"));
    	carteLivelloUno.add(new SpazioAperto(giocatori, Livello.I, DIR_PATH + "1_spazioaperto.png"));
    	
    	Collections.shuffle(carteLivelloUno);  	
    }

    /**
     * Inizializza e mescola le carte evento utilizzate nel {@code Livello.II}.
     */
	private void setupCarteLivelloDue() {
		
		Evento.getLogger().info("Generazione del mazzo di livello due...");
		
		List<Cannonata> cannonate = new ArrayList<>();
    	cannonate.add(generaCannonata(DimensioneColpo.GRANDE, Direzione.NORD));
    	cannonate.add(generaCannonata(DimensioneColpo.PICCOLO, Direzione.NORD));
    	cannonate.add(generaCannonata(DimensioneColpo.GRANDE, Direzione.NORD));
    	
    	List<Pianeta> pianeti1 = new ArrayList<>();
    	pianeti1.add(generaPianeta(3, 0, 1, 0));
    	pianeti1.add(generaPianeta(2, 0, 0, 2));  	
    	pianeti1.add(generaPianeta(1, 4, 0, 0));  	
    	List<Pianeta> pianeti2 = new ArrayList<>();
    	pianeti2.add(generaPianeta(2, 0, 0, 0));
    	pianeti2.add(generaPianeta(0, 0, 0, 4));  	
    	List<Pianeta> pianeti3 = new ArrayList<>();
    	pianeti3.add(generaPianeta(1, 0, 1, 0));
    	pianeti3.add(generaPianeta(0, 1, 1, 1));
    	pianeti3.add(generaPianeta(0, 0, 0, 2));
    	pianeti3.add(generaPianeta(0, 0, 1, 0));
    	List<Pianeta> pianeti4 = new ArrayList<>();
    	pianeti4.add(generaPianeta(0, 0, 0, 4));
    	pianeti4.add(generaPianeta(0, 0, 2, 0));
    	pianeti4.add(generaPianeta(0, 4, 0, 0));
    	
    	List<Meteorite> meteoriti1 = new ArrayList<>();
    	meteoriti1.add(generaMeteorite(DimensioneColpo.PICCOLO, Direzione.NORD));
    	meteoriti1.add(generaMeteorite(DimensioneColpo.GRANDE, Direzione.EST));
    	meteoriti1.add(generaMeteorite(DimensioneColpo.PICCOLO, Direzione.EST));
    	meteoriti1.add(generaMeteorite(DimensioneColpo.GRANDE, Direzione.EST));
    	meteoriti1.add(generaMeteorite(DimensioneColpo.PICCOLO, Direzione.SUD));
    	List<Meteorite> meteoriti2 = new ArrayList<>();
    	meteoriti2.add(generaMeteorite(DimensioneColpo.PICCOLO, Direzione.NORD));
    	meteoriti2.add(generaMeteorite(DimensioneColpo.GRANDE, Direzione.OVEST));
    	meteoriti2.add(generaMeteorite(DimensioneColpo.PICCOLO, Direzione.OVEST));
    	meteoriti2.add(generaMeteorite(DimensioneColpo.GRANDE, Direzione.OVEST));
    	meteoriti2.add(generaMeteorite(DimensioneColpo.PICCOLO, Direzione.SUD));
    	List<Meteorite> meteoriti3 = new ArrayList<>();
    	meteoriti3.add(generaMeteorite(DimensioneColpo.GRANDE, Direzione.NORD));
    	meteoriti3.add(generaMeteorite(DimensioneColpo.PICCOLO, Direzione.OVEST));
    	meteoriti3.add(generaMeteorite(DimensioneColpo.PICCOLO, Direzione.EST));
    	
    	carteLivelloDue.add(new Contrabbandieri(giocatori, Livello.II, 8, 1, generaMerci(1, 0, 2, 0), 3, DIR_PATH + "2_contrabbandieri1.png")); 
    	carteLivelloDue.add(new Epidemia(giocatori, Livello.II, DIR_PATH + "2_epidemia1.png"));
    	carteLivelloDue.add(new NaveAbbandonata(giocatori, Livello.II, 6, 4, 1, DIR_PATH + "2_naveabbandonata1.png"));  	
    	carteLivelloDue.add(new NaveAbbandonata(giocatori, Livello.II, 8, 5, 2, DIR_PATH + "2_naveabbandonata2.png"));  	 	
    	carteLivelloDue.add(new Pianeti(giocatori, Livello.II, pianeti1, 4, DIR_PATH + "2_pianeti1.png"));
    	carteLivelloDue.add(new Pianeti(giocatori, Livello.II, pianeti2, 3, DIR_PATH + "2_pianeti2.png"));
    	carteLivelloDue.add(new Pianeti(giocatori, Livello.II, pianeti3, 2, DIR_PATH + "2_pianeti3.png"));
    	carteLivelloDue.add(new Pianeti(giocatori, Livello.II, pianeti4, 3, DIR_PATH + "2_pianeti4.png"));  	
    	carteLivelloDue.add(new PioggiaDiMeteoriti(giocatori, Livello.II, meteoriti1, DIR_PATH + "2_pioggiadimeteoriti1.png"));   	
    	carteLivelloDue.add(new PioggiaDiMeteoriti(giocatori, Livello.II, meteoriti2, DIR_PATH + "2_pioggiadimeteoriti2.png"));  
    	carteLivelloDue.add(new PioggiaDiMeteoriti(giocatori, Livello.II, meteoriti3, DIR_PATH + "2_pioggiadimeteoriti3.png"));   	
    	carteLivelloDue.add(new Pirati(giocatori, Livello.II, 6, 2, 7, cannonate, DIR_PATH + "2_pirati1.png"));
    	carteLivelloDue.add(new PolvereStellare(giocatori, Livello.II, DIR_PATH + "2_polverestellare1.png"));  	
    	carteLivelloDue.add(new Schiavisti(giocatori, Livello.II, 8, 4, 2, 7, DIR_PATH + "2_schiavisti1.png"));
    	carteLivelloDue.add(new SpazioAperto(giocatori, Livello.II, DIR_PATH + "2_spazioaperto.png"));
    	carteLivelloDue.add(new SpazioAperto(giocatori, Livello.II, DIR_PATH + "2_spazioaperto.png"));
    	carteLivelloDue.add(new SpazioAperto(giocatori, Livello.II, DIR_PATH + "2_spazioaperto.png"));
    	carteLivelloDue.add(new StazioneAbbandonata(giocatori, Livello.II, 8, 2, generaMerci(0, 0, 2, 1), DIR_PATH + "2_stazioneabbandonata1.png"));
    	carteLivelloDue.add(new StazioneAbbandonata(giocatori, Livello.II, 7, 1, generaMerci(1, 0, 1, 0), DIR_PATH + "2_stazioneabbandonata2.png"));
	
    	Collections.shuffle(carteLivelloDue);
	}
	
	/**
     * Inizializza e mescola le carte evento utilizzate nel {@code Livello.III}.
     */
	private void setupCarteLivelloTre() {
		
		Evento.getLogger().info("Generazione del mazzo di livello tre...");
		
		List<Cannonata> cannonate = new ArrayList<>();
    	cannonate.add(generaCannonata(DimensioneColpo.GRANDE, Direzione.NORD));
    	cannonate.add(generaCannonata(DimensioneColpo.PICCOLO, Direzione.NORD));
    	cannonate.add(generaCannonata(DimensioneColpo.GRANDE, Direzione.NORD));
    	cannonate.add(generaCannonata(DimensioneColpo.PICCOLO, Direzione.OVEST));
    	cannonate.add(generaCannonata(DimensioneColpo.PICCOLO, Direzione.EST));
    	
    	List<Pianeta> pianeti1 = new ArrayList<>();
    	pianeti1.add(generaPianeta(0, 3, 0, 2));
    	pianeti1.add(generaPianeta(0, 2, 1, 0));  	  	
    	List<Pianeta> pianeti2 = new ArrayList<>();
    	pianeti2.add(generaPianeta(3, 0, 0, 0));
    	pianeti2.add(generaPianeta(0, 0, 3, 0));  	
    	pianeti2.add(generaPianeta(0, 0, 0, 3));  	
    	pianeti2.add(generaPianeta(0, 3, 0, 0));  	
    	List<Pianeta> pianeti3 = new ArrayList<>();
    	pianeti3.add(generaPianeta(0, 0, 5, 0));
    	pianeti3.add(generaPianeta(1, 0, 2, 0));
    	pianeti3.add(generaPianeta(2, 0, 0, 0));
    	List<Pianeta> pianeti4 = new ArrayList<>();
    	pianeti4.add(generaPianeta(1, 1, 1, 0));
    	pianeti4.add(generaPianeta(1, 2, 0, 1));
    	pianeti4.add(generaPianeta(1, 4, 0, 0));
    	
    	List<Meteorite> meteoriti1 = new ArrayList<>();
    	meteoriti1.add(generaMeteorite(DimensioneColpo.PICCOLO, Direzione.NORD));
    	meteoriti1.add(generaMeteorite(DimensioneColpo.GRANDE, Direzione.EST));
    	meteoriti1.add(generaMeteorite(DimensioneColpo.PICCOLO, Direzione.EST));
    	meteoriti1.add(generaMeteorite(DimensioneColpo.GRANDE, Direzione.EST));
    	meteoriti1.add(generaMeteorite(DimensioneColpo.PICCOLO, Direzione.SUD));
    	List<Meteorite> meteoriti2 = new ArrayList<>();
    	meteoriti2.add(generaMeteorite(DimensioneColpo.PICCOLO, Direzione.NORD));
    	meteoriti2.add(generaMeteorite(DimensioneColpo.GRANDE, Direzione.OVEST));
    	meteoriti2.add(generaMeteorite(DimensioneColpo.PICCOLO, Direzione.OVEST));
    	meteoriti2.add(generaMeteorite(DimensioneColpo.GRANDE, Direzione.OVEST));
    	meteoriti2.add(generaMeteorite(DimensioneColpo.PICCOLO, Direzione.SUD));
    	
    	carteLivelloTre.add(new Contrabbandieri(giocatori, Livello.III, 9, 2, generaMerci(2, 0, 2, 1), 4, DIR_PATH + "3_contrabbandieri1.png")); 
    	carteLivelloTre.add(new Epidemia(giocatori, Livello.III, DIR_PATH + "3_epidemia1.png"));
    	carteLivelloTre.add(new NaveAbbandonata(giocatori, Livello.III, 10, 6, 2, DIR_PATH + "3_naveabbandonata1.png"));  	
    	carteLivelloTre.add(new NaveAbbandonata(giocatori, Livello.III, 11, 7, 2, DIR_PATH + "3_naveabbandonata2.png"));  	 	
    	carteLivelloTre.add(new Pianeti(giocatori, Livello.III, pianeti1, 2, DIR_PATH + "3_pianeti1.png"));
    	carteLivelloTre.add(new Pianeti(giocatori, Livello.III, pianeti2, 4, DIR_PATH + "3_pianeti2.png"));
    	carteLivelloTre.add(new Pianeti(giocatori, Livello.III, pianeti3, 5, DIR_PATH + "3_pianeti3.png"));
    	carteLivelloTre.add(new Pianeti(giocatori, Livello.III, pianeti4, 3, DIR_PATH + "3_pianeti4.png"));  	
    	carteLivelloTre.add(new PioggiaDiMeteoriti(giocatori, Livello.III, meteoriti1, DIR_PATH + "3_pioggiadimeteoriti1.png"));   	
    	carteLivelloTre.add(new PioggiaDiMeteoriti(giocatori, Livello.III, meteoriti2, DIR_PATH + "3_pioggiadimeteoriti2.png"));  
    	carteLivelloTre.add(new Pirati(giocatori, Livello.III, 10, 2, 12, cannonate, DIR_PATH + "3_pirati1.png"));
    	carteLivelloTre.add(new Sabotaggio(giocatori, Livello.III, DIR_PATH + "3_sabotaggio1.png"));  	
    	carteLivelloTre.add(new Schiavisti(giocatori, Livello.III, 10, 5, 2, 8, DIR_PATH + "3_schiavisti1.png"));
    	carteLivelloTre.add(new SpazioAperto(giocatori, Livello.III, DIR_PATH + "3_spazioaperto.png"));
    	carteLivelloTre.add(new SpazioAperto(giocatori, Livello.III, DIR_PATH + "3_spazioaperto.png"));
    	carteLivelloTre.add(new SpazioAperto(giocatori, Livello.III, DIR_PATH + "3_spazioaperto.png"));
    	carteLivelloTre.add(new StazioneAbbandonata(giocatori, Livello.III, 10, 2, generaMerci(0, 0, 2, 2), DIR_PATH + "3_stazioneabbandonata1.png"));
    	carteLivelloTre.add(new StazioneAbbandonata(giocatori, Livello.III, 9, 2, generaMerci(1, 1, 1, 1), DIR_PATH + "3_stazioneabbandonata2.png"));
		
    	Collections.shuffle(carteLivelloTre);
	}
	
	/**
	 * Crea una mappa che associa a ciascun tipo di {@link Merce} la rispettiva quantità.
	 *
	 * @param merceRossa  il numero di merci rosse.
	 * @param merceBlu    il numero di merci blu.
	 * @param merceGialla il numero di merci gialle.
	 * @param merceVerde  il numero di merci verdi.
	 * @return una mappa contenente i tipi di merce e le rispettive quantità.
	 */
	private Map<Merce, Integer> generaMerci(int merceRossa, int merceBlu, int merceGialla, int merceVerde) {
		Map<Merce, Integer> merce = new EnumMap<>(Merce.class);
		merce.put(Merce.ROSSA, merceRossa);
		merce.put(Merce.BLU, merceBlu);
		merce.put(Merce.GIALLA, merceGialla);
		merce.put(Merce.VERDE, merceVerde);
		
		return merce;
	}

	/**
	 * Genera un nuovo {@link Pianeta} popolato con merci secondo i valori forniti.
	 *
	 * @param merceRossa  il numero di merci rosse.
	 * @param merceBlu    il numero di merci blu.
	 * @param merceGialla il numero di merci gialle.
	 * @param merceVerde  il numero di merci verdi.
	 * @return un nuovo oggetto {@link Pianeta} contenente le merci specificate.
	 */
	private Pianeta generaPianeta(int merceRossa, int merceBlu, int merceGialla, int merceVerde) {	
		return new Pianeta(generaMerci(merceRossa, merceBlu, merceGialla, merceVerde));
	}
	
	/**
	 * Genera un nuovo {@link Meteorite} con colpo di dimensione e direzione specificate.
	 *
	 * @param dimensione             la dimensione del meteorite.
	 * @param direzioneProvenienza   la direzione da cui proviene il meteorite.
	 * @return un nuovo oggetto {@link Meteorite}.
	 * @throws NullPointerException se uno dei parametri è {@code null}.
	 */
	private Meteorite generaMeteorite(DimensioneColpo dimensione, Direzione direzioneProvenienza) {
		
		if (dimensione == null || direzioneProvenienza == null) {
			String errore = "I parametri 'dimensione' e 'direzioneProvenienza' non possono essere nulli!";
    		Evento.getLogger().error(errore);
    		throw new NullPointerException(errore);
		}
		
		return new Meteorite(rand(), dimensione, direzioneProvenienza);
	}
	
	/**
	 * Genera una nuova {@link Cannonata} con colpo di dimensione e direzione specificate.
	 *
	 * @param dimensione             la dimensione della cannonata
	 * @param direzioneProvenienza   la direzione da cui proviene la cannonata
	 * @return un nuovo oggetto {@link Cannonata}
	 * @throws NullPointerException se uno dei parametri è {@code null}
	 */
	private Cannonata generaCannonata(DimensioneColpo dimensione, Direzione direzioneProvenienza) {
		
		if (dimensione == null || direzioneProvenienza == null) {
			String errore = "I parametri 'dimensione' e 'direzioneProvenienza' non possono essere nulli!";
    		Evento.getLogger().error(errore);
    		throw new NullPointerException(errore);
		}
		
		return new Cannonata(rand(), dimensione, direzioneProvenienza);
	}
	
	/**
	 * Simula il lancio di due dadi e restituisce la somma dei valori ottenuti.
	 * <p>
	 * Ogni dado ha valori compresi tra {@code Evento.MIN_DADO} e {@code Evento.MAX_DADO} (estremi inclusi).
	 *
	 * @return la somma di due tiri casuali di dado.
	 */
    private int rand() {
        return Evento.getDado().nextInt(Evento.MIN_DADO, Evento.MAX_DADO + 1) + Evento.getDado().nextInt(Evento.MIN_DADO, Evento.MAX_DADO + 1);
    }
    
    // getters

    /**
     * Restituisce la coda degli eventi pronti per essere eseguiti.
     *
     * @return la coda FIFO delle carte {@link Evento}.
     */
	public Queue<Evento> getCodaEventi() {
		return codaEventi;
	}

	/**
	 * Restituisce una lista non modificabile delle carte evento che possono essere sbirciate.
	 *
	 * @return una lista immutabile delle carte {@link Evento}.
	 */
	public List<Evento> getCarteSbirciabili() {
		return Collections.unmodifiableList(carteSbirciabili);
	}
	
}