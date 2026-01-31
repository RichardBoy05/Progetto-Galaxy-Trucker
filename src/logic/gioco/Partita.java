package galaxytrucker.src.logic.gioco;

import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import galaxytrucker.src.logic.assemblaggio.Abitante;
import galaxytrucker.src.logic.assemblaggio.Cabina;
import galaxytrucker.src.logic.assemblaggio.Cella;
import galaxytrucker.src.logic.assemblaggio.Griglia;
import galaxytrucker.src.logic.assemblaggio.Mucchio;
import galaxytrucker.src.logic.assemblaggio.Nave;
import galaxytrucker.src.logic.assemblaggio.Tessera;
import galaxytrucker.src.logic.assemblaggio.VanoBatteria;
import galaxytrucker.src.logic.eccezioni.ConfigurazioneNaveNonValidaException;
import galaxytrucker.src.logic.eccezioni.PiazzamentoEquipaggioNonValidoException;
import galaxytrucker.src.logic.eccezioni.RichiestaBatterieNonValidaException;
import galaxytrucker.src.logic.volo.Mazzo;
import galaxytrucker.src.logic.volo.Merce;
import galaxytrucker.src.view.dialogs.SceltaEquipaggioDialog;
import galaxytrucker.src.view.dialogs.ValidazioneNaveDialog;
import galaxytrucker.src.view.frames.FineGui;

/**
 * La classe gestisce il flusso di gioco di una partita.
 * In particolare, si occupa di:
 * <ul>
 *   <li>Creare gli oggetti Mucchio e Mazzo, responsabili della generazione del mucchio di tessere e del mazzo di carte.</li>
 *   <li>Avviare le fasi di assemblaggio, preparazione al volo e volo.</li>
 *   <li>Assegnare punteggi e generare la classifica finale.</li>
 * </ul>
 * Ogni fase del gioco viene eseguita seguendo le regole definite dal livello selezionato.
 * Le fasi vengono eseguite in thread separati per mantenere responsiva l'interfaccia utente.
 */
public class Partita {
	
	/** Logger di gioco, riporta eventuali messaggi di informazione/errore. */
	private static final GameLogger LOGGER = GameLogger.getInstance();
	
	/** Costante indicante il numero minimo di giocatori consentito. */
	public static final int MIN_GIOCATORI = 2;
	
	/** Costante indicante il numero massimo di giocatori consentito. */
	public static final int MAX_GIOCATORI = 4;
	
	/** Lista dei giocatori partecipanti alla partita. */
	private List<Giocatore> giocatori;
	
	/** Mucchio di tessere. */
	private final Mucchio mucchio;
	
	/** Mazzo di carte evento. */
	private final Mazzo mazzo;
	
	/** Livello di diffcioltà della partita. */
	private final Livello livello;
	
    /**
     * Costruisce una nuova istanza di {@code Partita} con i giocatori specificati e il livello selezionato.
     *
     * @param giocatori la lista dei giocatori partecipanti alla partita.
     * @param livello il livello di difficoltà della partita.
     * @throws NullPointerException se uno dei parametri è {@code null}.
     * @throws IllegalArgumentException se il numero di giocatori è fuori dai limiti definiti da {@link #MIN_GIOCATORI} e {@link #MAX_GIOCATORI}.
     */
	public Partita(List<Giocatore> giocatori, Livello livello) {
		
		if (giocatori == null) {
			String errore = "Il parametro 'giocatori' non può essere nullo!";
			LOGGER.error(errore);
			throw new NullPointerException(errore);
		}
		
		if (livello == null) {
			String errore = "Il livello non può essere null!";
			LOGGER.error(errore);
			throw new NullPointerException(errore);
		}
		
		if (giocatori.size() < MIN_GIOCATORI || giocatori.size() > MAX_GIOCATORI) {
			String errore = "Il numero di giocatori deve essere tra " + MIN_GIOCATORI + " e " + MAX_GIOCATORI + "!";
			LOGGER.error(errore);
			throw new IllegalArgumentException(errore);
		}
		
		this.giocatori = giocatori;
		this.livello = livello;
		
		// setup del mucchio di tessere e del mazzo di carte evento
		this.mucchio = new Mucchio();
		this.mazzo = new Mazzo(giocatori, livello);
	}
	
	/**
     * Avvia la partita facendo partire la fase di assemblaggio.
     */
	public void gioca() {
		assemblaggio();
	}
	
	/**
     * Gestisce la fase di assemblaggio delle astronavi.
     * Dopo la fase, avvia la preparazione al volo.
     */
	private void assemblaggio() {
		
		LOGGER.info("Inizio fase di assemblaggio delle astronavi...");
		FaseAssemblaggio fase = new FaseAssemblaggio(giocatori, livello, mucchio, mazzo.getCarteSbirciabili());
		
		new Thread(() -> {
			
		    fase.eseguiFase();
		    SwingUtilities.invokeLater(() -> {
	            
		    	LOGGER.info("Fase di assemblaggio della astronavi completata!");
		    	giocatori = fase.getListaOrdinata(); // lista nell'ordine di partenza dei giocatori
		    	preparazioneVolo();
	        });
		    
		}).start();	
		
	}
	
	/**
     * Gestisce tutte le operazioni di preparazione alla fase di volo:
     * <ul>
     *   <li>Assegnazione dei giorni di volo in base al livello.</li>
     *   <li>Validazione della configurazione delle navi.</li>
     *   <li>Posizionamento automatico dell'equipaggio e delle batterie.</li>
     *   <li>Dialog di scelta equipaggio per il posizionamento degli alieni (se il livello è diverso da {@code Livello.P}).</li>
     * </ul>
     * Una volta completata, avvia la fase di volo.
     */
	private void preparazioneVolo() {
		
		LOGGER.info("Preparazione alla fase di volo...");
		
		// impostazione giorni di volo di partenza, partendo in ordine inverso di rotta nell'assegnazione come previsto dal regolamento
		
		if (livello == Livello.P || livello == Livello.I) {
			if (giocatori.size() > 3) giocatori.get(3).perdiGiorniDiVolo(4, giocatori);	
			if (giocatori.size() > 2) giocatori.get(2).perdiGiorniDiVolo(3, giocatori);
			giocatori.get(1).perdiGiorniDiVolo(2, giocatori);
			giocatori.get(0).perdiGiorniDiVolo(0, giocatori);
			
		} else if (livello == Livello.II) {
			if (giocatori.size() > 3) giocatori.get(3).perdiGiorniDiVolo(6, giocatori);	
			if (giocatori.size() > 2) giocatori.get(2).perdiGiorniDiVolo(5, giocatori);
			giocatori.get(1).perdiGiorniDiVolo(3, giocatori);
			giocatori.get(0).perdiGiorniDiVolo(0, giocatori);			
			
		} else { // livello 3
			if (giocatori.size() > 3) giocatori.get(3).perdiGiorniDiVolo(9, giocatori);	
			if (giocatori.size() > 2) giocatori.get(2).perdiGiorniDiVolo(7, giocatori);
			giocatori.get(1).perdiGiorniDiVolo(4, giocatori);
			giocatori.get(0).perdiGiorniDiVolo(0, giocatori);
		}
		
		// validazione nave
				
		for (Giocatore g : giocatori) {
			
		    while (!g.isInVolo()) {
		        try {
		            g.getNave().verificaNave();
		            g.setInVolo(true); // in volo solo quando la nave è corretta
		        } catch (ConfigurazioneNaveNonValidaException e) {
		            new ValidazioneNaveDialog(null, g, e);
		        }
		    }
		}
		
		// rimozione dei debiti dovuti ad errori di costruzione per le partite in modalità di prova
		if (livello == Livello.P) for (Giocatore g : giocatori) g.azzeraDebiti();
		
		// scelta astronauti e posizionamento batterie
		
		for (Giocatore g : giocatori) {
			
			Griglia griglia = g.getNave().getGriglia();
			boolean proporreSceltaEquipaggio = false;
			
			for (int riga = 0; riga < griglia.getAltezza(); riga++) {
				for (int colonna = 0; colonna < griglia.getLarghezza(); colonna++) {
					
					Cella c = griglia.getCella(riga, colonna);
					if (c == null) continue;
					
					Tessera t = c.getTessera();				
					if (t == null) continue;
					
					if (t.fornisceBatterie()) {
						
						VanoBatteria vano = (VanoBatteria) t; // casting sicuro grazie al controllo in precedenza
						try {
							vano.setNumeroBatterie(vano.getMaxBatterie());
						} catch (RichiestaBatterieNonValidaException e) {
							// eccezione IMPOSSIBILE dato che setto il numero di batterie in modo che coincida con il massimo, è solo un controllo di sicurezza
							String errore = "Errore imprevisto nel posizionamento delle batterie!\n" + e.getMessage();
							LOGGER.error(errore);
							JOptionPane.showInternalMessageDialog(null, errore, "Errore imprevisto!", JOptionPane.ERROR_MESSAGE);
							continue;
						}
						
						continue;
					}
					
					if (t.accettaAstronauta() && !t.accettaAlieno(Abitante.ALIENO_MARRONE, griglia.getTessereAdiacenti(c)) && !t.accettaAlieno(Abitante.ALIENO_VIOLA, griglia.getTessereAdiacenti(c))) {
						// caso di una cabina senza alcun supporto vitale alieno connesso
						
						Cabina cabina = (Cabina) t; // casting sicuro grazie al controllo in precedenza
						
						try {
							cabina.setNumeroAstronauti(2);
						} catch (PiazzamentoEquipaggioNonValidoException e) {
							// eccezione IMPOSSIBILE dato che una cabina accetta sempre due astronauti, è solo un controllo di sicurezza
							String errore = "Errore imprevisto nel piazzamento dell'equipaggio!\n" + e.getMessage();
							LOGGER.error(errore);
							JOptionPane.showInternalMessageDialog(null, errore, "Errore imprevisto!", JOptionPane.ERROR_MESSAGE);
							continue;
						}
					} else if (t.accettaAstronauta()) {
						// può accettare alieni
						
						Cabina cabina = (Cabina) t; // casting sicuro grazie al controllo in precedenza
						
						if (cabina.isDiPartenza() || livello == Livello.P) { // NO alieni nella cabina di partenza e in modalità di prova
							try {
								cabina.setNumeroAstronauti(2);
							} catch (PiazzamentoEquipaggioNonValidoException e) {
								// eccezione IMPOSSIBILE dato che una cabina accetta sempre due astronauti, è solo un controllo di sicurezza
								String errore = "Errore imprevisto nel piazzamento dell'equipaggio!\n" + e.getMessage();
								LOGGER.error(errore);
								JOptionPane.showInternalMessageDialog(null, errore, "Errore imprevisto!", JOptionPane.ERROR_MESSAGE);
								continue;
							}
						} else { // propongo la scelta
							proporreSceltaEquipaggio = true;
						}
						
					}
				}
			}
			
			if (proporreSceltaEquipaggio) new SceltaEquipaggioDialog(null, g);
			
		}		
		
		LOGGER.info("Preparativi di volo completati!");
		LOGGER.info("Inizio fase di volo!");

		volo();
		    	    	
	}	
	
	/**
     * Avvia la fase di volo. Al termine, avvia l’assegnazione
     * dei punteggi e la generazione della classifica finale.
     */
	private void volo() {		
		
		FaseVolo fase = new FaseVolo(giocatori, livello, mazzo.getCodaEventi());
			
		new Thread(() -> {

		    fase.eseguiFase();
		    SwingUtilities.invokeLater(() -> {
		    	
		    	LOGGER.info("Fase di volo completata!");
		    	
		    	LOGGER.info("Assegnazione punteggi!");	    	
		    	assegnaPunteggio();
		    	
		    	LOGGER.info("Generazione della classifica finale!");
		    	generaClassifica();
		    	
		    	LOGGER.info("Partita conclusa!");
		    	new FineGui(giocatori);
		    	
	        });
		    
		}).start();
	}
	
	/**
     * Calcola e assegna il punteggio finale per ciascun giocatore.
     * Il punteggio tiene conto di:
     * <ul>
     *   <li>Crediti e debiti accumulati.</li>
     *   <li>Posizione all'arrivo.</li>
     *   <li>Premio per la nave più bella.</li>
     *   <li>Valore delle merci trasportate.</li>
     * </ul>
     * 
     * Le modalità di assegnazione variano in base al {@link Livello} di gioco.
     */
	private void assegnaPunteggio() {
		
		// punteggio di partenza
		
		for (Giocatore g : giocatori) {
			g.setPunteggioFinale(g.getCrediti() - g.getDebiti());
		}
		
		// assegnazione crediti posizione
		
		int multiplier = 4;
		
		for (Giocatore g: giocatori) {
			
			if (!g.isInVolo()) continue; // escludo i giocatori che hanno abbandonato il volo
			
			int punteggioCorrente = g.getPunteggioFinale();
			g.setPunteggioFinale(punteggioCorrente + multiplier * livello.getCreditiBaseDiVittoria());
			multiplier--;
		}
		
		// assegnazione crediti nave più bella

		int minConnettoriEsposti = Integer.MAX_VALUE;

		for (Giocatore g : giocatori) {
			if (!g.isInVolo()) continue;

			int connettoriEsposti = g.getNave().getConnettoriEsposti();

			if (connettoriEsposti < minConnettoriEsposti) {
				minConnettoriEsposti = connettoriEsposti;
			}
		}

		for (Giocatore g : giocatori) { // secondo ciclo poiché devo assegnare il premio anche in caso di parità
			if (!g.isInVolo()) continue;
			
			Nave nave = g.getNave();

			int connettoriEsposti = nave.getConnettoriEsposti();;

			if (connettoriEsposti == minConnettoriEsposti) {
				int punteggioCorrente = g.getPunteggioFinale();
				g.setPunteggioFinale(punteggioCorrente + livello.getCreditiNavePiuBella());
				nave.setNavePiuBella(true);
			}
		}
		
		// assegnazione crediti merce
		
		for (Giocatore g: giocatori) {
			int valoreMerce = 0;
			Nave nave = g.getNave();
			
			valoreMerce += nave.getMercePerTipo(Merce.ROSSA) * Merce.ROSSA.getValore();
			valoreMerce += nave.getMercePerTipo(Merce.BLU) * Merce.BLU.getValore();
			valoreMerce += nave.getMercePerTipo(Merce.GIALLA) * Merce.GIALLA.getValore();
			valoreMerce += nave.getMercePerTipo(Merce.VERDE) * Merce.VERDE.getValore();
			
			if (!g.isInVolo()) valoreMerce = Math.ceilDiv(valoreMerce, 2);
			// il regolamento prevede un arrotondamento per eccesso
			
			g.setPunteggioFinale(g.getPunteggioFinale() + valoreMerce);
			
		}
				
	}
	
	/**
     * Ordina i giocatori in base al punteggio finale e assegna loro un rank.
     * In caso di parità, assegna lo stesso rank ai giocatori con punteggio uguale.
     */
	private void generaClassifica() {	
		
		// criterio di ordinamento basato sul punteggio finale anziché sul numero di giorni di volo persi
		giocatori.sort(new PunteggioFinaleComparator());
		
		// assegnazione dei rank ai giocatori
		
		int rank = 1;
		int posizione = 1;
		int punteggioPrecedente = -1;

		for (Giocatore g : giocatori) {
		    int punteggio = g.getPunteggioFinale();

		    if (punteggio != punteggioPrecedente) {
		        rank = posizione;
		        punteggioPrecedente = punteggio;
		    }

		    g.setRankFinale(rank);
		    posizione++;
		}
		
	}
		
}