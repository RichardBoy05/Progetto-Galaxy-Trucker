package galaxytrucker.src.logic.gioco;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import galaxytrucker.src.logic.assemblaggio.Cella;
import galaxytrucker.src.logic.assemblaggio.Direzione;
import galaxytrucker.src.logic.assemblaggio.Mucchio;
import galaxytrucker.src.logic.assemblaggio.Nave;
import galaxytrucker.src.logic.assemblaggio.Tessera;
import galaxytrucker.src.logic.volo.Merce;

/**
 * Rappresenta un giocatore all'interno della partita.
 * <p>
 * Tiene traccia delle informazioni personali, dello stato della nave,
 * delle tessere prenotate e delle statistiche del giocatore.
 * Fornisce i metodi che rappresentano le azioni del giocatore durante la partita.
 */
public class Giocatore implements Comparable<Giocatore> {
	
	// attributi
	
	private static final GameLogger LOGGER = GameLogger.getInstance();
	private static final int TESSERE_PRENOTABILI = 2;
	
	private final String nome;
	private final Colore colore;
	private final Nave nave;
	private int crediti;
	private int debiti;
	private int giorniDiVoloPersi; // criterio di ordinamento sulla rotta di volo
	private int punteggioFinale;
	private int rankFinale;
	private boolean inVolo;
	private boolean assemblaggioCompletato;
	private final List<Tessera> tesserePrenotate;
	
	/**
	 * Costruttore più comune.
	 * <p>
	 * Costruisce un giocatore di default allo stato iniziale della partita.
	 *
	 * @param nome il nome del giocatore.
	 * @param colore il colore del giocatore.
	 * @param nave la nave assegnata al giocatore.
	 */
	public Giocatore(String nome, Colore colore, Nave nave) {
	    this(nome, colore, nave, 0, 0, 0, 0, 0, false, false, new ArrayList<Tessera>());
	}
	
	/**
	 * Costruttore completo.
	 *
	 * @param nome il nome del giocatore.
	 * @param colore il colore del giocatore.
	 * @param nave la nave assegnata al giocatore.
	 * @param crediti numero iniziale di crediti (non negativo).
	 * @param debiti numero iniziale di debiti (non negativo).
	 * @param giorniDiVoloPersi giorni di volo persi del giocatore, che funge da indice di rotta.
	 * @param punteggioFinale punteggio finale del giocatore.
	 * @param rankFinale la posizione in classifica finale del giocatore.
	 * @param inVolo {@code true} se il giocatore è in stato di volo, {@code false} se non è ancora partito o è già atterrato.
	 * @param assemblaggioCompletato {@code true} se ha completato l'assemblaggio, {@code false} altrimenti.
	 * @param tesserePrenotate lista delle tessere prenotate (dimensione massima 2)
	 * 
	 * @throws NullPointerException se uno dei tipi di riferimento passati come parametro è {@code null}. 
	 * @throws IllegalArgumentException se almeno uno dei parametri {@code crediti} o {@code debiti} è negativo. 
	 */
	public Giocatore(String nome, Colore colore, Nave nave, int crediti, int debiti,
					int giorniDiVoloPersi, int punteggioFinale, int rankFinale, boolean inVolo,
					boolean assemblaggioCompletato, ArrayList<Tessera> tesserePrenotate) {
		
		if (nome == null) {
			String errore = "Il parametro 'nome' del giocatore non può essere nullo!";
			LOGGER.error(errore);
			throw new NullPointerException(errore);
		}

		if (colore == null) {
			String errore = "Il parametro 'colore' del giocatore non può essere nullo!";
			LOGGER.error(errore);
			throw new NullPointerException(errore);
		}
		
		if (nave == null) {
			String errore = "Il parametro 'nave' del giocatore non può essere nullo!";
			LOGGER.error(errore);
			throw new NullPointerException(errore);
		}

		if (tesserePrenotate == null) {
			String errore = "Il parametro 'tesserePrenotate' del giocatore non può essere nullo!";
			LOGGER.error(errore);
			throw new NullPointerException(errore);
		}
		
		if (crediti < 0) {
			String errore = "Il numero di crediti non può essere negativo!";
			LOGGER.error(errore);
			throw new IllegalArgumentException(errore);
		}
		
		if (debiti < 0) {
			String errore = "Il numero di debiti non può essere negativo!";
			LOGGER.error(errore);
			throw new IllegalArgumentException(errore);
		}

		this.nome = nome;
		this.colore = colore;
		this.nave = nave;
		this.crediti = crediti;
		this.debiti = debiti;
		this.giorniDiVoloPersi = giorniDiVoloPersi;
		this.punteggioFinale = punteggioFinale;
		this.rankFinale = rankFinale;
		this.inVolo = inVolo;
		this.assemblaggioCompletato = assemblaggioCompletato;
		this.tesserePrenotate = tesserePrenotate;
		
		// inizializzo gli slot disponibili
		for (int i = 0; i < TESSERE_PRENOTABILI; i++) {
			this.tesserePrenotate.add(null);
		}	
		
	}
	
	 /**
     * Inserisce la tessera specificata nella cella indicata.
     *
     * @param cella  la cella su cui piazzare la tessera.
     * @param tessera la tessera da piazzare.
     * @throws NullPointerException se {@code cella} o {@code tessera} sono {@code null}.
     */
	public void piazzaTessera(Cella cella, Tessera tessera) {
		
		if (cella == null) {
			String errore = "Il parametro 'cella' non può essere nullo!";
			LOGGER.error(errore);
			throw new NullPointerException(errore);
		}
		
		if (tessera == null) {
			String errore = "Il parametro 'tessera' non può essere nullo!";
			LOGGER.error(errore);
			throw new NullPointerException(errore);
		}
		
		cella.inserisciTessera(tessera);
	}
	
	/**
     * Rimuove la tessera presente nella cella indicata.
     *
     * @param cella la cella da cui rimuovere la tessera.
     * @return la tessera rimossa, oppure {@code null} se la cella era vuota.
     * @throws NullPointerException se il parametro {@code cella} è {@code null}.
     */
	public Tessera rimuoviTessera(Cella cella) {
		
		if (cella == null) {
			String errore = "Il parametro 'cella' non può essere nullo!";
			LOGGER.error(errore);
			throw new NullPointerException(errore);
		}
		
		return cella.rimuoviTessera();
	}
	
	/**
     * Ruota la tessera in senso orario.
     *
     * @param tessera la tessera da ruotare.
     * @throws NullPointerException se il parametro {@code tessera} è {@code null}.
     */
	public void ruotaTessera(Tessera tessera) {
		
		if (tessera == null) {
			String errore = "Il parametro 'tessera' non può essere nullo!";
			LOGGER.error(errore);
			throw new NullPointerException(errore);
		}
		
		tessera.ruotaTesseraOrario();
	}
	
	/**
     * Pesca una tessera nascosta dal mucchio.
     *
     * @param mucchio il mucchio da cui pescare.
     * @return la tessera pescata.
     * @throws NullPointerException se il parametro {@code mucchio} è {@code null}.
     */
	public Tessera pescaTesseraNascosta(Mucchio mucchio) {
		
		if (mucchio == null) {
			String errore = "Il parametro 'mucchio' non può essere nullo!";
			LOGGER.error(errore);
			throw new NullPointerException(errore);
		}
		
		return mucchio.fornisciTesseraNascosta();
	}
	
	/**
	 * Pesca una tessera visibile di un determinato tipo dal mucchio.
	 *
	 * @param mucchio il mucchio da cui pescare la tessera.
	 * @param tipo la classe della tessera da pescare, che identifica la pila d'interesse.
	 * @return la tessera visibile pescata dal mucchio.
	 * @throws NullPointerException se {@code mucchio} o {@code tipo} sono {@code null}.
	 */
	public Tessera pescaTesseraVisibile(Mucchio mucchio, Class<? extends Tessera> tipo) {
		
		if (mucchio == null) {
			String errore = "Il parametro 'mucchio' non può essere nullo!";
			LOGGER.error(errore);
			throw new NullPointerException(errore);
		}
		
		if (tipo == null) {
			String errore = "Il parametro 'tipo' non può essere nullo!";
			LOGGER.error(errore);
			throw new NullPointerException(errore);
		}
		
		return mucchio.fornisciTesseraVisibile(tipo);
	}
	
	/**
	 * Scarta una tessera nel mucchio.
	 *
	 * @param mucchio il mucchio in cui scartare la tessera.
	 * @param tessera la tessera da scartare.
	 * @throws NullPointerException se {@code mucchio} o {@code tessera} sono {@code null}.
	 */
	public void scartaTessera(Mucchio mucchio, Tessera tessera) {
		
		if (mucchio == null) {
			String errore = "Il parametro 'mucchio' non può essere nullo!";
			LOGGER.error(errore);
			throw new NullPointerException(errore);
		}
		
		if (tessera == null) {
			String errore = "Il parametro 'tessera' non può essere nullo!";
			LOGGER.error(errore);
			throw new NullPointerException(errore);
		}
		
		mucchio.scartaTessera(tessera);
	}
	
	/**
	 * Manda una tessera visibile in fondo alla propria pila.
	 *
	 * @param mucchio il mucchio delle tessere.
	 * @param tipo la classe che identifica la tessera da mandare in fondo.
	 * @throws NullPointerException se {@code mucchio} o {@code tipo} sono {@code null}.
	 */
	public void seppellisciTesseraVisibile(Mucchio mucchio, Class<? extends Tessera> tipo) {
		
		if (mucchio == null) {
			String errore = "Il parametro 'mucchio' non può essere nullo!";
			LOGGER.error(errore);
			throw new NullPointerException(errore);
		}
		
		if (tipo == null) {
			String errore = "Il parametro 'tipo' non può essere nullo!";
			LOGGER.error(errore);
			throw new NullPointerException(errore);
		}
		
		mucchio.mandaInFondoTesseraVisibile(tipo);
	}
	
	/**
     * Prenota una tessera nello slot indicato.
     *
     * @param tessera    la tessera da prenotare.
     * @param slot       lo slot in cui prenotare la tessera.
     * @throws NullPointerException se il parametro {@code tessera} è {@code null}.
     * @throws IllegalArgumentException se lo slot è fuori intervallo.
     */
	public void prenotaTessera(Tessera tessera, int slot) {
		
		if (tessera == null) {
			String errore = "Il parametro 'tessera' non può essere nullo!";
			LOGGER.error(errore);
			throw new NullPointerException(errore);
		}
		
		if (slot < 0 || slot >= TESSERE_PRENOTABILI) {
			String errore = "Lo slot deve essere compreso tra 0 e " + (TESSERE_PRENOTABILI - 1) + ", estremi inclusi!";
			LOGGER.error(errore);
			throw new IllegalArgumentException(errore);
		}	
		
		tesserePrenotate.set(slot, tessera);

	}
	
	 /**
     * Rimuove la tessera prenotata dallo slot indicato.
     *
     * @param slot lo slot da liberare.
     * @throws IllegalArgumentException se lo slot è fuori intervallo.
     */
	public void rimuoviTesseraPrenotata(int slot) {
		
		if (slot < 0 || slot >= TESSERE_PRENOTABILI) {
			String errore = "Lo slot deve essere compreso tra 0 e " + (TESSERE_PRENOTABILI - 1) + ", estremi inclusi!";
			LOGGER.error(errore);
			throw new IllegalArgumentException(errore);
		}
		
		tesserePrenotate.set(slot, null);
	}
	
	/**
     * Restituisce la lista delle tessere prenotate (immutabile).
     *
     * @return lista non modificabile delle tessere prenotate.
     */
	public List<Tessera> vediTesserePrenotate(){
		return Collections.unmodifiableList(tesserePrenotate);
	}
	
	/**
	 * Recupera e rimuove la tessera prenotata nello slot indicato.
	 *
	 * @param slot l'indice dello slot da cui prelevare la tessera.
	 * @return la tessera prenotata nello slot, oppure {@code null} se vuoto.
	 * @throws IllegalArgumentException se {@code slot} è fuori dai limiti validi (0 incluso, {@code TESSERE_PRENOTABILI - 1} incluso)
	 */
	public Tessera prendiTesseraPrenotata(int slot) {
		
		if (slot < 0 || slot >= TESSERE_PRENOTABILI) {
			String errore = "Lo slot deve essere compreso tra 0 e " + (TESSERE_PRENOTABILI - 1) + ", estremi inclusi!";
			LOGGER.error(errore);
			throw new IllegalArgumentException(errore);
		}
		
		Tessera tessera = tesserePrenotate.get(slot);
		tesserePrenotate.set(slot, null);
		return tessera;
	}
	
	/**
	 * Aggiunge un certo numero di crediti al giocatore.
	 *
	 * @param creditiAggiunti numero di crediti da aggiungere.
	 * @throws IllegalArgumentException se {@code creditiAggiunti} è negativo, 
	 * infatti il regolamento del gioco non prevede la rimozione di crediti.
	 */
	public void aggiungiCrediti(int creditiAggiunti) {
		
		if (creditiAggiunti < 0) {
			String errore = "Il numero di crediti aggiunti deve essere positivo!";
			LOGGER.error(errore);
			throw new IllegalArgumentException(errore);
		}
		
		this.crediti += creditiAggiunti;
	}
	
	/**
	 * Aggiunge un certo numero di debiti al giocatore.
	 *
	 * @param debitiAggiunti numero di debiti da aggiungere.
	 * @throws IllegalArgumentException se {@code debitiAggiunti} è negativo, 
	 * infatti il regolamento del gioco non prevede la rimozione di crediti.
	 */
	public void aggiungiDebiti(int debitiAggiunti) {	
		
		if (debitiAggiunti < 0) {
			String errore = "Il numero di debiti aggiunti deve essere positivo!";
			LOGGER.error(errore);
			throw new IllegalArgumentException(errore);
		}
		
		this.debiti += debitiAggiunti;
	}
	
	/**
	 * Azzera i debiti del giocatore.
	 * <p>
	 * È utilizzato solo nelle partite di {@code Livello.P} (modalità di prova), dove i
	 * debiti dovuti ad eventuali errori di costruzione della nave non vengono considerati.
	 */
	public void azzeraDebiti() {
		this.debiti = 0;
	}
	
	/**
	 * Riduce il numero di giorni di volo persi per i giocatori specificati.
	 *
	 * @param giorniVoloGuadagnati numero di giorni da recuperare.
	 * @param giocatori lista dei giocatori da aggiornare.
	 * @throws NullPointerException se {@code giocatori} è {@code null}.
	 * @throws IllegalArgumentException se {@code giorniVoloGuadagnati} è negativo.
	 */
	public void guadagnaGiorniDiVolo(int giorniVoloGuadagnati, List<Giocatore> giocatori) {
		
		if (giorniVoloGuadagnati < 0) {
			String errore = "Il parametro 'giorniVoloGuadagnati' non può essere negativo!";
			LOGGER.error(errore);
			throw new IllegalArgumentException(errore);
		}
		
		modificaGiorniDiVolo(-giorniVoloGuadagnati, giocatori);	
		
	}
	
	/**
     * Applica una penalità di giorni di volo, tenendo conto delle posizioni già occupate da altri giocatori.
     * <p>
     * N.B.: se più giocatori devono perdere contemporaneamente giorni di volo, questo
     * metodo va applicato sugli stessi in ordine inverso di rotta, in accordo con il regolamento.
     *
     * @param giorniVoloPersi numero di giorni da perdere.
     * @param giocatori       lista dei giocatori presenti.
     * @throws NullPointerException se il parametro {@code giocatori} è {@code null}.
     * @throws IllegalArgumentException se il parametro {@code giorniVoloPersi} è negativo.
     */
	public void perdiGiorniDiVolo(int giorniVoloPersi, List<Giocatore> giocatori) {
		
		if (giorniVoloPersi < 0) {
			String errore = "Il parametro 'giorniVoloPersi' non può essere negativo!";
			LOGGER.error(errore);
			throw new IllegalArgumentException(errore);
		}
		
		modificaGiorniDiVolo(giorniVoloPersi, giocatori);		
	}
	
	/**
	 * Modifica il numero di giorni di volo persi da questo giocatore, evitando di sovrapporsi
	 * a un'altra posizione già occupata da un altro giocatore.
	 * <p>
	 * Se la nuova posizione è già occupata, il giocatore continua a muoversi in avanti o indietro
	 * (a seconda del segno di {@code deltaGiorniDiVolo}) fino a trovare una posizione libera.
	 *
	 * @param deltaGiorniDiVolo il numero di giorni di volo da aggiungere (positivo) o sottrarre (negativo).
	 * @param giocatori la lista di tutti i giocatori presenti nella partita.
	 * @throws NullPointerException se {@code giocatori} è {@code null}.
	 */
	private void modificaGiorniDiVolo(int deltaGiorniDiVolo, List<Giocatore> giocatori) {
		
		if (giocatori == null) {
			String errore = "Il parametro 'giocatori' non può essere nullo!";
			LOGGER.error(errore);
			throw new NullPointerException(errore);
		}
		
	    Set<Integer> posizioniOccupate = getPosizioniOccupate(giocatori);
	    int passi = Math.abs(deltaGiorniDiVolo);
	    int direzione = Integer.signum(deltaGiorniDiVolo);
	    for (int i = 0; i < passi; i++) {
	        giorniDiVoloPersi += direzione;
	        if (posizioniOccupate.contains(giorniDiVoloPersi)) i--;
	    }
	}
	
	/**
	 * Restituisce l'insieme delle posizioni attualmente occupate dagli altri giocatori,
	 * rappresentate dal numero di giorni di volo persi.
	 *
	 * @param giocatori la lista di tutti i giocatori presenti nella partita.
	 * @return un {@code Set<Integer>} contenente le posizioni occupate.
	 * @throws NullPointerException se {@code giocatori} è {@code null}.
	 */
	private Set<Integer> getPosizioniOccupate(List<Giocatore> giocatori){
		
		if (giocatori == null) {
			String errore = "Il parametro 'giocatori' non può essere nullo!";
			LOGGER.error(errore);
			throw new NullPointerException(errore);
		}
		
		Set<Integer> posizioniOccupate = new HashSet<>();
		for (Giocatore g : giocatori) {
			if (g != this) posizioniOccupate.add(g.getGiorniDiVoloPersi());
		}
		return posizioniOccupate;
	}
	
	 /**
     * Ordina i giocatori in base ai giorni di volo persi (ordine crescente, nel senso
     * che il giocatore in testa è quello con il minor numero di giorni di volo persi).
     * Si è scelto si sfruttare l'interfaccia {@code Comparable} e questo suo metodo poiché
     * l'ordinamento dei giocatori in base ai giorni di volo persi è il più frequente durante la partita.
     *
     * @param altroGiocatore altro giocatore da confrontare.
     * @return un valore negativo, nullo o positivo in base al confronto effettuato tramite il metodo {@code Integer.compare(int x, int y)}.
     * @throws NullPointerException se {@code altroGiocatore} è {@code null}.
     * @see Integer
     */
	@Override
	public int compareTo(Giocatore altroGiocatore) {
		
		if (altroGiocatore == null) {
			String errore = "Il parametro 'altroGiocatore' non può essere nullo!";
			LOGGER.error(errore);
			throw new NullPointerException(errore);
		}
		
		return Integer.compare(this.giorniDiVoloPersi, altroGiocatore.giorniDiVoloPersi);
		
	}
	
	/**
	 * Restituisce una rappresentazione HTML del giocatore, comprensiva del nome, delle statistiche
	 * e delle caratteristiche della nave.
	 * <p>
	 * Il formato è pensato per essere mostrato in un componente Swing (es. {@code JOptionPane}).
	 *
	 * @return una stringa HTML che rappresenta graficamente il giocatore.
	 */
	@Override
	public String toString() {
	    StringBuilder sb = new StringBuilder("<html>");
	    sb.append("<div style='font-family:sans-serif; font-size: 14px;'>");

	    // titolo
	    sb.append("<h1 style='margin: 0; color:").append(colore.toHex()).append(";'>")
	      .append("&#x1F3AE;&nbsp;").append(nome)
	      .append("</h1>");

	    // sezione statistiche
	    sb.append("<hr style='border: 0; height: 1px; background-color: #ccc; margin: 12px 0;'>");
	    sb.append("<h2 style='margin-top: 0; margin-bottom: 4px;'>")
	      .append("&#x1F4CA;&nbsp;Statistiche")
	      .append("</h2>");

	    sb.append("<table cellpadding='6' cellspacing='0' style='font-size: 14px;'>");

	    sb.append("<tr><td style='white-space: nowrap;'><b>Crediti:</b></td><td>")
	      .append(crediti).append("</td></tr>");

	    sb.append("<tr><td style='white-space: nowrap;'><b>Debiti:</b></td><td>")
	      .append(debiti).append("</td></tr>");

	    sb.append("<tr><td style='white-space: nowrap;'><b>Giorni di volo persi:</b></td><td>")
	      .append(giorniDiVoloPersi).append("</td></tr>");

	    sb.append("<tr><td style='white-space: nowrap;'><b>In volo:</b></td><td>")
	      .append(inVolo ? "<span style='color:green; font-weight:bold;'>Sì</span>"
	                     : "<span style='color:red; font-weight:bold;'>No</span>")
	      .append("</td></tr>");

	    sb.append("</table>");

	    // sezione nave
	    sb.append("<hr style='border: 0; height: 1px; background-color: #ccc; margin: 12px 0;'>");
	    sb.append("<h2 style='margin-top: 0; margin-bottom: 4px;'>")
	      .append("&#x1F680;&nbsp;Nave")
	      .append("</h2>");

	    sb.append("<table cellpadding='6' cellspacing='0' style='font-size: 14px;'>");

	    sb.append("<tr><td style='white-space: nowrap;'><b>Potenza di fuoco:</b></td><td>")
	      .append(getNave().getPotenzaDiFuoco()).append("</td></tr>");

	    sb.append("<tr><td style='white-space: nowrap;'><b>Potenza motrice:</b></td><td>")
	      .append(getNave().getPotenzaMotrice()).append("</td></tr>");

	    sb.append("<tr><td style='white-space: nowrap;'><b>Merce:</b></td><td>")
	      .append("<span style='color:").append(Colore.ROSSO.toHex()).append("'>").append(getNave().getMercePerTipo(Merce.ROSSA)).append("</span>-")
	      .append("<span style='color:").append(Colore.BLU.toHex()).append("'>").append(getNave().getMercePerTipo(Merce.BLU)).append("</span>-")
	      .append("<span style='color:").append(Colore.GIALLO.toHex()).append("'>").append(getNave().getMercePerTipo(Merce.GIALLA)).append("</span>-")
	      .append("<span style='color:").append(Colore.VERDE.toHex()).append("'>").append(getNave().getMercePerTipo(Merce.VERDE)).append("</span>")
	      .append("</td></tr>");

	    sb.append("<tr><td style='white-space: nowrap;'><b>Equipaggio:</b></td><td>")
	      .append(getNave().getTotAbitanti()).append("</td></tr>");

	    sb.append("<tr><td style='white-space: nowrap;'><b>Batterie:</b></td><td>")
	      .append(getNave().getNumeroBatterie()).append("</td></tr>");

	    sb.append("<tr><td style='white-space: nowrap;'><b>Protezioni:</b></td><td>");

	    for (Direzione dir : Direzione.values()) {
	        boolean protetta = getNave().isProtetta(dir);
	        String colore = protetta ? "green" : "red";
	        String abbreviazione = switch (dir) {
	            case NORD -> "N";
	            case EST -> "E";
	            case SUD -> "S";
	            case OVEST -> "O";
	        };
	        sb.append("<span style='color:").append(colore).append(";'>")
	          .append(abbreviazione)
	          .append(!abbreviazione.equals("O") ? "-" : "")
	          .append("</span>");
	    }


	    sb.append("</td></tr>");

	    sb.append("<tr><td style='white-space: nowrap;'><b>Connettori esposti:</b></td><td>")
	      .append(getNave().getConnettoriEsposti()).append("</td></tr>");

	    sb.append("</table>");
	    sb.append("</div></html>");
	    
	    return sb.toString();
	}
	
	
	/**
	 * @return il nome del giocatore.
	 */
	public String getNome() {
	    return nome;
	}

	/**
	 * @return il colore del giocatore.
	 */
	public Colore getColore() {
	    return colore;
	}

	/**
	 * @return la nave del giocatore.
	 */
	public Nave getNave() {
	    return nave;
	}

	/**
	 * @return i crediti attuali del giocatore.
	 */
	public int getCrediti() {
	    return crediti;
	}

	/**
	 * @return i debiti attuali del giocatore.
	 */
	public int getDebiti() {
	    return debiti;
	}

	/**
	 * @return i giorni di volo persi.
	 */
	public int getGiorniDiVoloPersi() {
	    return giorniDiVoloPersi;
	}

	/**
	 * @return {@code true} se il giocatore è in volo, altrimenti {@code false}.
	 */
	public boolean isInVolo() {
	    return inVolo;
	}

	/**
	 * @param inVolo {@code true} se il giocatore è in volo, altrimenti {@code false}.
	 */
	public void setInVolo(boolean inVolo) {
	    this.inVolo = inVolo;
	}

	/**
	 * @return il punteggio finale del giocatore.
	 */
	public int getPunteggioFinale() {
	    return punteggioFinale;
	}

	/**
	 * @param punteggioFinale il punteggio finale da assegnare.
	 */
	public void setPunteggioFinale(int punteggioFinale) {
	    this.punteggioFinale = punteggioFinale;
	}

	/**
	 * @return il posizionamento finale.
	 */
	public int getRankFinale() {
	    return rankFinale;
	}

	/**
	 * @param rankFinale il valore da assegnare come posizione finale.
	 */
	public void setRankFinale(int rankFinale) {
	    this.rankFinale = rankFinale;
	}

	/**
	 * @return {@code true} se l'assemblaggio è stato completato, altrimenti {@code false}.
	 */
	public boolean isAssemblaggioCompletato() {
	    return assemblaggioCompletato;
	}

	/**
	 * @param assemblaggioCompletato {@code true} se completato, altrimenti {@code false}.
	 */
	public void setAssemblaggioCompletato(boolean assemblaggioCompletato) {
	    this.assemblaggioCompletato = assemblaggioCompletato;
	}

}