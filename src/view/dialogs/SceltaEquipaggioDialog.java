package galaxytrucker.src.view.dialogs;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.LineBorder;
import galaxytrucker.src.logic.assemblaggio.Abitante;
import galaxytrucker.src.logic.assemblaggio.Cabina;
import galaxytrucker.src.logic.assemblaggio.Cella;
import galaxytrucker.src.logic.assemblaggio.Griglia;
import galaxytrucker.src.logic.assemblaggio.Nave;
import galaxytrucker.src.logic.assemblaggio.Specie;
import galaxytrucker.src.logic.assemblaggio.Tessera;
import galaxytrucker.src.logic.eccezioni.PiazzamentoEquipaggioNonValidoException;
import galaxytrucker.src.logic.gioco.Giocatore;
import galaxytrucker.src.logic.volo.NaveAbbandonata;
import galaxytrucker.src.logic.volo.Schiavisti;
import galaxytrucker.src.view.base.AzioneClickCella;
import galaxytrucker.src.view.base.IconHandler;
import galaxytrucker.src.view.components.PannelloNave;

/**
 * La classe {@code SceltaEquipaggioDialog} rappresenta una finestra
 * di dialogo utilizzata per la gestione dell'equipaggio di una nave.
 * 
 * <p>Questa finestra consente due modalità operative distinte:
 * <ul>
 *   <li><b>Piazzamento dell'equipaggio:</b> consente al giocatore di aggiungere astronauti o alieni
 *       (marroni o viola) alle cabine idonee della propria nave.</li>
 *   <li><b>Eliminazione dell'equipaggio:</b> richiede al giocatore di selezionare ed eliminare 
 *       un numero specifico di abitanti tra astronauti e alieni presenti nella nave.</li>
 * </ul>
 * 
 * @see OperazioniNaveDialog
 */
public class SceltaEquipaggioDialog extends OperazioniNaveDialog {
	
	// costanti
	
	private static final String DIR_PATH = "/galaxytrucker/resources/images/utente/";
	private static final String ASTRONAUTA_ICON_PATH = DIR_PATH + "astronauta.png";
	private static final String HOV_ASTRONAUTA_ICON_PATH = DIR_PATH + "astronauta_hover.png";
	private static final String ASTRONAUTA_DISABLED_ICON_PATH = DIR_PATH + "astronauta_disabled.png";
	private static final String ALIENO_MARRONE_ICON_PATH = DIR_PATH + "alieno_marrone.png";
	private static final String HOV_ALIENO_MARRONE_ICON_PATH = DIR_PATH + "alieno_marrone_hover.png";
	private static final String ALIENO_MARRONE_DISABLED_ICON_PATH = DIR_PATH + "alieno_marrone_disabled.png";
	private static final String ALIENO_VIOLA_ICON_PATH = DIR_PATH + "alieno_viola.png";
	private static final String HOV_ALIENO_VIOLA_ICON_PATH = DIR_PATH + "alieno_viola_hover.png";
	private static final String ALIENO_VIOLA_DISABLED_ICON_PATH = DIR_PATH + "alieno_viola_disabled.png";
	
	// componenti
	
	private PannelloNave pannelloNave;
	
	private JLabel titoloLbl;
	private JLabel sottotitoloLbl;
	private JLabel contatoreAbitantiDaEliminareLbl;
	private JLabel contatoreAstronautiEliminatiLbl;
	private JLabel contatoreAlieniMarroniEliminatiLbl;
	private JLabel contatoreAlieniViolaEliminatiLbl;
	
	private JLabel astronautaLbl;
	private JLabel alienoMarroneLbl;
	private JLabel alienoViolaLbl;
	
	private ImageIcon astronautaIcon;
	private ImageIcon hovAstronautaIcon;
	private ImageIcon astronautaDisabledIcon;
	private ImageIcon alienoMarroneIcon;
	private ImageIcon hovAlienoMarroneIcon;
	private ImageIcon alienoMarroneDisabledIcon;
	private ImageIcon alienoViolaIcon;
	private ImageIcon hovAlienoViolaIcon;
	private ImageIcon alienoViolaDisabledIcon;
	
	// altri attributi
	
	private final int numeroAbitantiDaEliminare;
	private int abitantiDaEliminareRimasti;
	private int astronautiEliminati = 0;
	private int alieniMarroniEliminati = 0;
	private int alieniViolaEliminati = 0;
	private List<Cella> celleSelezionabili;
	private Cella cellaSelezionata = null;
	
	/**
	 * Costruisce una finestra di dialogo che consente all'utente di
	 * scegliere, quando possibile, la predisposizione degli abitanti
	 * nelle proprie cabine.
	 * <p>
	 * Utilizzata durante la fase di preparazione al volo
	 * se il livello è diverso dal livello di prova.
	 * 
	 * @param parent la finestra principale {@link JFrame} da cui viene mostrato il dialog.
	 * @param giocatore il giocatore che deve interagire con la finestra.
	 * 
	 * @see Abitante
	 */
	public SceltaEquipaggioDialog(JFrame parent, Giocatore giocatore) {
		super(parent, giocatore);
		
		this.numeroAbitantiDaEliminare = 0;		
		inizializzaDialog();
	}
	
	/**
	 * Costruisce una finestra di dialogo che consente all'utente di scegliere
	 * quali abitanti eliminare dalla propria nave.
	 * <p>
	 * N.B.: questa finestra andrebbe proposta solo se la {@link Nave} del giocatore
	 * presenti un numero sufficiente di abitanti totali, almeno pari al {@code numeroAbitantiDaEliminare}.
	 * <p>
	 * Eventi interessati: {@link NaveAbbandonata}, {@link Schiavisti}.
	 * 
	 * @see Abitante
     * @param parent la finestra principale {@link JFrame} da cui viene mostrato il dialog.
	 * @param giocatore giocatore che deve eliminare un certo numero di abitanti dalla propria nave
	 * @param numeroAbitantiDaEliminare il numero di abitanti, umani o alieni, da essere eliminato
	 * @throws NullPointerException se {@code giocatore} è {@code null}. 
	 * @throws IllegalArgumentException se {@code numeroAbitantiDaEliminare} è
	 * minore di 1 o maggiore del numero totale di abitanti presenti nella nave. 
	 */
	public SceltaEquipaggioDialog(JFrame parent, Giocatore giocatore, int numeroAbitantiDaEliminare) {
		super(parent, giocatore);
		
		if (numeroAbitantiDaEliminare < 1) {
			String errore = "Il parametro 'numeroAbitantiDaEliminare' deve valere almeno 1!";
			getLogger().error(errore);
			throw new IllegalArgumentException(errore);
		}
		
		if (numeroAbitantiDaEliminare > giocatore.getNave().getTotAbitanti()) {
			String errore = "Il parametro 'numeroAbitantiDaEliminare' non può superare "
					+ "il numero totale di abitanti della nave! Bisogna effettuare prima questo controllo!";
			getLogger().error(errore);
			throw new IllegalArgumentException(errore);
		}
		
		this.numeroAbitantiDaEliminare = numeroAbitantiDaEliminare;
		abitantiDaEliminareRimasti = numeroAbitantiDaEliminare;
		inizializzaDialog();
	}
	
	/**
     * Inizializza e visualizza la finestra di dialogo.
     * <p>
     * Popola le celle selezionabili in base all'operazione da eseguire (piazzamento o eliminazione).
     * Configura il pannello della nave e abilita l'interazione utente.
     */
	private void inizializzaDialog() {
		
		Griglia griglia = getGiocatore().getNave().getGriglia();
		celleSelezionabili = new ArrayList<>();
		
		for (int riga = 0; riga < griglia.getAltezza(); riga++) {
			for (int colonna = 0; colonna < griglia.getLarghezza(); colonna++) {
				
				Cella c = griglia.getCella(riga, colonna);				
				if (c.getTessera() == null) continue;
				
				if (numeroAbitantiDaEliminare == 0) { // caso piazzamento equipaggio
					
					if(c.getTessera().accettaAlieno(Abitante.ALIENO_MARRONE, griglia.getTessereAdiacenti(c)) || c.getTessera().accettaAlieno(Abitante.ALIENO_VIOLA, griglia.getTessereAdiacenti(c))) {
						celleSelezionabili.add(c);
					}
					
				} else { // caso eliminazione equipaggio
					if (c.getTessera().accettaAstronauta() && c.getTessera().getNumeroAbitantiTotale() > 0) {
						celleSelezionabili.add(c);
						
					}
					
				}
			}
		}
		
		pannelloNave = new PannelloNave(getGiocatore().getNave(), true, new AzioneClickCella() {
	    	@Override
            public void onClickCella(int riga, int colonna, MouseEvent evento) {
	    		
                if (SwingUtilities.isLeftMouseButton(evento) && !evento.isShiftDown()) {          	
                	logicaClickSinistro(riga, colonna);
 
                } else if (SwingUtilities.isLeftMouseButton(evento) && evento.isShiftDown()) {
                	Tessera t = getGiocatore().getNave().getGriglia().getCella(riga, colonna).getTessera();
                    String message = (t == null) ? "Questa cella non contiene alcuna tessera!" : t.toString();          
                    JOptionPane.showMessageDialog(getDialog(), message, "Informazioni sulla tessera", JOptionPane.INFORMATION_MESSAGE);
                }
               
            } 
        });
		
		for (Cella c : celleSelezionabili) {
			pannelloNave.getLabelCella(c.getRiga(), c.getColonna()).setBorder(new LineBorder(Color.GREEN, 2));
		}
		
		posizionaPannelloNave(pannelloNave);

		setupComponents();
		setupLayout();
		setupListeners();
		
		if (this.numeroAbitantiDaEliminare > 0) disabilitaChiusuraDialog();
        
        getDialog().setVisible(true);
        getDialog().revalidate();
	}

	/**
     * Inizializza e configura tutti i componenti della finestra di dialogo, inclusi testi, icone e bottoni.
     */
	@Override
	public void setupComponents() {
		
		astronautaIcon = new ImageIcon(getClass().getResource(ASTRONAUTA_ICON_PATH));
		hovAstronautaIcon = new ImageIcon(getClass().getResource(HOV_ASTRONAUTA_ICON_PATH));
		astronautaDisabledIcon = new ImageIcon(getClass().getResource(ASTRONAUTA_DISABLED_ICON_PATH));
		alienoMarroneIcon = new ImageIcon(getClass().getResource(ALIENO_MARRONE_ICON_PATH));
		hovAlienoMarroneIcon = new ImageIcon(getClass().getResource(HOV_ALIENO_MARRONE_ICON_PATH));
		alienoMarroneDisabledIcon = new ImageIcon(getClass().getResource(ALIENO_MARRONE_DISABLED_ICON_PATH));
		alienoViolaIcon = new ImageIcon(getClass().getResource(ALIENO_VIOLA_ICON_PATH));
		hovAlienoViolaIcon = new ImageIcon(getClass().getResource(HOV_ALIENO_VIOLA_ICON_PATH));
		alienoViolaDisabledIcon = new ImageIcon(getClass().getResource(ALIENO_VIOLA_DISABLED_ICON_PATH));

		astronautaLbl = new JLabel();
		alienoMarroneLbl = new JLabel();
		alienoViolaLbl = new JLabel();
		titoloLbl = new JLabel("Scelta equipaggio");
		sottotitoloLbl = new JLabel();
		contatoreAbitantiDaEliminareLbl = new JLabel("Abitanti da eliminare: " + abitantiDaEliminareRimasti);
		contatoreAstronautiEliminatiLbl = new JLabel("0");
		contatoreAlieniMarroniEliminatiLbl = new JLabel("0");
		contatoreAlieniViolaEliminatiLbl = new JLabel("0");

		
		String testoPiazzamentoEquipaggio = "<html><div style='text-align:justify;'>"
			    + "In questa sezione puoi scegliere quali abitanti <i>posizionare</i> "
			    + "nelle cabine evidenziate.<br>"
			    + "Una volta che hai selezionato una cabina (click <i>sinistro</i>), "
			    + "puoi usare i <i>bottoni</i> qui sotto per effettuare la tua scelta.<br>"
			    + "Nello specifico, cliccaci col <i>sinistro</i> per aggiungere un membro, "
			    + "con il <i>destro</i> invece per rimuoverlo.<br>"
			    + "Infine, fai <i>shift + click</i> se vuoi visualizzare i dettagli "
			    + "di una qualsiasi tessera.<br>"
			    + "</div></html>";
		
		String testoEliminazioneEquipaggio = "<html><div style='text-align:justify;'>"
			    + "In questa sezione puoi scegliere quali abitanti <i>eliminare</i>  "
			    + "dalle cabine evidenziate.<br>"
			    + "Una volta che hai selezionato una cabina (click <i>sinistro</i>), "
			    + "puoi usare i <i>bottoni</i> qui sotto per effettuare la tua scelta.<br>"
			    + "Nello specifico, cliccaci col <i>sinistro</i> per rimuovere un membro, "
			    + "con il <i>destro</i> invece per rimetterlo. "
			    + "Sotto ogni bottone trovi il numero di alieni eliminati per quel tipo.<br>"			    
			    + "Infine, fai <i>shift + click</i> se vuoi visualizzare i dettagli "
			    + "di una qualsiasi tessera.<br>"
			    + "</div></html>";
		
		sottotitoloLbl.setText((this.numeroAbitantiDaEliminare > 0) ? testoEliminazioneEquipaggio : testoPiazzamentoEquipaggio);
		
		titoloLbl.setForeground(Color.WHITE);
		titoloLbl.setFont(titoloLbl.getFont().deriveFont(Font.BOLD, 26));
		titoloLbl.setHorizontalAlignment(SwingConstants.CENTER);

		sottotitoloLbl.setForeground(Color.WHITE);
		sottotitoloLbl.setFont(sottotitoloLbl.getFont().deriveFont(Font.BOLD, (numeroAbitantiDaEliminare == 0) ? 14 : 13));
		sottotitoloLbl.setHorizontalAlignment(SwingConstants.CENTER);
		
		contatoreAbitantiDaEliminareLbl.setForeground(Color.WHITE);
		contatoreAbitantiDaEliminareLbl.setFont(contatoreAbitantiDaEliminareLbl.getFont().deriveFont(Font.BOLD, 15));
		contatoreAbitantiDaEliminareLbl.setHorizontalAlignment(SwingConstants.CENTER);
		contatoreAstronautiEliminatiLbl.setForeground(Color.WHITE);
		contatoreAstronautiEliminatiLbl.setFont(contatoreAstronautiEliminatiLbl.getFont().deriveFont(Font.BOLD, 16));
		contatoreAstronautiEliminatiLbl.setHorizontalAlignment(SwingConstants.CENTER);
		contatoreAlieniMarroniEliminatiLbl.setForeground(Color.WHITE);
		contatoreAlieniMarroniEliminatiLbl.setFont(contatoreAlieniMarroniEliminatiLbl.getFont().deriveFont(Font.BOLD, 16));
		contatoreAlieniMarroniEliminatiLbl.setHorizontalAlignment(SwingConstants.CENTER);
		contatoreAlieniViolaEliminatiLbl.setForeground(Color.WHITE);
		contatoreAlieniViolaEliminatiLbl.setFont(contatoreAlieniViolaEliminatiLbl.getFont().deriveFont(Font.BOLD, 16));
		contatoreAlieniViolaEliminatiLbl.setHorizontalAlignment(SwingConstants.CENTER);
		
		// disattivo i bottoni di default, saranno attivati solo quando una cella verrà selezionata
		astronautaLbl.putClientProperty("attivo", false);
		alienoMarroneLbl.putClientProperty("attivo", false);
		alienoViolaLbl.putClientProperty("attivo", false);
		astronautaLbl.setToolTipText("Devi selezionare una tessera prima!");
		alienoMarroneLbl.setToolTipText("Devi selezionare una tessera prima!");
		alienoViolaLbl.setToolTipText("Devi selezionare una tessera prima!");		
		IconHandler.setIconOnLabel(astronautaLbl, astronautaDisabledIcon, astronautaDisabledIcon);
		IconHandler.setIconOnLabel(alienoMarroneLbl, alienoMarroneDisabledIcon, alienoMarroneDisabledIcon);
		IconHandler.setIconOnLabel(alienoViolaLbl, alienoViolaDisabledIcon, alienoViolaDisabledIcon);
		
		gestisciBottoniAbilitati();
	
	}

	/**
     * Posiziona graficamente i componenti nella finestra usando layout assoluto.
     */
	@Override
	public void setupLayout() {
		
		if (numeroAbitantiDaEliminare == 0) {
			
			titoloLbl.setBounds(723, 0, 270, 60);
			sottotitoloLbl.setBounds(740, 40, 235, 300);
			astronautaLbl.setBounds(740, 347, 70, 70);
			alienoMarroneLbl.setBounds(820, 347, 70, 70);
			alienoViolaLbl.setBounds(900, 347, 70, 70);		
			
		} else {
			
			titoloLbl.setBounds(723, 0, 270, 50);
			sottotitoloLbl.setBounds(740, 40, 235, 280);
			astronautaLbl.setBounds(740, 357, 70, 70);
			alienoMarroneLbl.setBounds(820, 357, 70, 70);
			alienoViolaLbl.setBounds(900, 357, 70, 70);		
			contatoreAbitantiDaEliminareLbl.setBounds(723, 322, 270, 40);
			contatoreAstronautiEliminatiLbl.setBounds(755, 422, 40, 40);
			contatoreAlieniMarroniEliminatiLbl.setBounds(835, 422, 40, 40);
			contatoreAlieniViolaEliminatiLbl.setBounds(915, 422, 40, 40);
			
			getBackground().add(contatoreAbitantiDaEliminareLbl);
			getBackground().add(contatoreAstronautiEliminatiLbl);
			getBackground().add(contatoreAlieniMarroniEliminatiLbl);
			getBackground().add(contatoreAlieniViolaEliminatiLbl);
		}
		
		getBackground().add(titoloLbl);
		getBackground().add(sottotitoloLbl);
		getBackground().add(astronautaLbl);
		getBackground().add(alienoMarroneLbl);
		getBackground().add(alienoViolaLbl);

	}

	 /**
     * Associa i listener ai bottoni dell'interfaccia per gestire il click dell'utente.
     * <p>In base al tipo di click e al tipo di abitante selezionato, si effettua l'aggiunta o la rimozione.
     * 
     * <p>Supporta:
     * <ul>
     *   <li>Click sinistro: aggiunta o rimozione dell'abitante</li>
     *   <li>Click destro: operazione opposta al click sinistro</li>
     *   <li>Shift + click su una cella: mostra dettagli della tessera</li>
     * </ul>
     */
	@Override
	public void setupListeners() {

		astronautaLbl.addMouseListener(new MouseAdapter() {
		    @Override
		    public void mousePressed(MouseEvent e) {
		    	if (SwingUtilities.isLeftMouseButton(e) && Boolean.TRUE.equals(astronautaLbl.getClientProperty("attivo"))) {          	
                	if (numeroAbitantiDaEliminare == 0) aggiuntaAbitante(Abitante.ASTRONAUTA);
                	else eliminazioneAbitante(Abitante.ASTRONAUTA);
 
                } else if (SwingUtilities.isRightMouseButton(e) && Boolean.TRUE.equals(astronautaLbl.getClientProperty("attivo"))) {
                	if (numeroAbitantiDaEliminare == 0) eliminazioneAbitante(Abitante.ASTRONAUTA);
                	else aggiuntaAbitante(Abitante.ASTRONAUTA);
                }
		    }
		});
		
		alienoMarroneLbl.addMouseListener(new MouseAdapter() {
		    @Override
		    public void mousePressed(MouseEvent e) {
		    	if (SwingUtilities.isLeftMouseButton(e) && Boolean.TRUE.equals(alienoMarroneLbl.getClientProperty("attivo"))) {          	
                	if (numeroAbitantiDaEliminare == 0) aggiuntaAbitante(Abitante.ALIENO_MARRONE);
                	else eliminazioneAbitante(Abitante.ALIENO_MARRONE);
 
                } else if (SwingUtilities.isRightMouseButton(e) && Boolean.TRUE.equals(alienoMarroneLbl.getClientProperty("attivo"))) {
                	if (numeroAbitantiDaEliminare == 0) eliminazioneAbitante(Abitante.ALIENO_MARRONE);
                	else aggiuntaAbitante(Abitante.ALIENO_MARRONE);
                }
		    }
		});
		
		alienoViolaLbl.addMouseListener(new MouseAdapter() {
			
		    @Override
		    public void mousePressed(MouseEvent e) {
		    	if (SwingUtilities.isLeftMouseButton(e) && Boolean.TRUE.equals(alienoViolaLbl.getClientProperty("attivo"))) {          	
                	if (numeroAbitantiDaEliminare == 0) aggiuntaAbitante(Abitante.ALIENO_VIOLA);
                	else eliminazioneAbitante(Abitante.ALIENO_VIOLA);
 
                } else if (SwingUtilities.isRightMouseButton(e) && Boolean.TRUE.equals(alienoViolaLbl.getClientProperty("attivo"))) {
                	if (numeroAbitantiDaEliminare == 0) eliminazioneAbitante(Abitante.ALIENO_VIOLA);
                	else aggiuntaAbitante(Abitante.ALIENO_VIOLA);
                }
		    }
		});
		
	}
	
	/**
	 * Abilita o disabilita i bottoni per l'aggiunta/rimozione dell'equipaggio
	 * in base allo stato della cella selezionata e al tipo di abitante.
	 */
	private void gestisciBottoniAbilitati() {
		
		if (cellaSelezionata == null) { // disattivo i bottoni in mancanza di una cella selezionata
			astronautaLbl.putClientProperty("attivo", false);
			alienoMarroneLbl.putClientProperty("attivo", false);
			alienoViolaLbl.putClientProperty("attivo", false);
			astronautaLbl.setToolTipText("Devi selezionare una tessera prima!");
			alienoMarroneLbl.setToolTipText("Devi selezionare una tessera prima!");
			alienoViolaLbl.setToolTipText("Devi selezionare una tessera prima!");
			IconHandler.setIconOnLabel(astronautaLbl, astronautaDisabledIcon, astronautaDisabledIcon);
			IconHandler.setIconOnLabel(alienoMarroneLbl, alienoMarroneDisabledIcon, alienoMarroneDisabledIcon);
			IconHandler.setIconOnLabel(alienoViolaLbl, alienoViolaDisabledIcon, alienoViolaDisabledIcon);
			
			return;
		}
		
		if (abitantiDaEliminareRimasti == 0) {
			astronautaLbl.putClientProperty("attivo", false);
			alienoMarroneLbl.putClientProperty("attivo", false);
			alienoViolaLbl.putClientProperty("attivo", false);
			astronautaLbl.setToolTipText("Non hai altri abitanti da eliminare, puoi proseguire con il volo!");
			alienoMarroneLbl.setToolTipText("Non hai altri abitanti da eliminare, puoi proseguire con il volo!");
			alienoViolaLbl.setToolTipText("Non hai altri abitanti da eliminare, puoi proseguire con il volo!");
			IconHandler.setIconOnLabel(astronautaLbl, astronautaDisabledIcon, astronautaDisabledIcon);
			IconHandler.setIconOnLabel(alienoMarroneLbl, alienoMarroneDisabledIcon, alienoMarroneDisabledIcon);
			IconHandler.setIconOnLabel(alienoViolaLbl, alienoViolaDisabledIcon, alienoViolaDisabledIcon);
			
			abilitaChiusuraDialog();
			
		} else {
			
			astronautaLbl.putClientProperty("attivo", true);
			alienoMarroneLbl.putClientProperty("attivo", true);
			alienoViolaLbl.putClientProperty("attivo", true);
			astronautaLbl.setToolTipText(null);
			alienoMarroneLbl.setToolTipText(null);
			alienoViolaLbl.setToolTipText(null);
			IconHandler.setIconOnLabel(astronautaLbl, astronautaIcon, hovAstronautaIcon);
			IconHandler.setIconOnLabel(alienoMarroneLbl, alienoMarroneIcon, hovAlienoMarroneIcon);
			IconHandler.setIconOnLabel(alienoViolaLbl, alienoViolaIcon, hovAlienoViolaIcon);
			
			disabilitaChiusuraDialog();
		}
		
		if (numeroAbitantiDaEliminare == 0) {
			
			Tessera t = cellaSelezionata.getTessera();
			
			if (t.accettaAstronauta()) {
				astronautaLbl.putClientProperty("attivo", true);
				astronautaLbl.setToolTipText(null);
				IconHandler.setIconOnLabel(astronautaLbl, astronautaIcon, hovAstronautaIcon);
			} else {
				astronautaLbl.putClientProperty("attivo", false);
				astronautaLbl.setToolTipText("Non puoi posizionare un astronauta in questa tessera!");
				IconHandler.setIconOnLabel(astronautaLbl, astronautaDisabledIcon, astronautaDisabledIcon);
			}
			
			if (t.accettaAlieno(Abitante.ALIENO_MARRONE, getGiocatore().getNave().getGriglia().getTessereAdiacenti(cellaSelezionata))) {
				alienoMarroneLbl.putClientProperty("attivo", true);
				alienoMarroneLbl.setToolTipText(null);
				IconHandler.setIconOnLabel(alienoMarroneLbl, alienoMarroneIcon, hovAlienoMarroneIcon);
			} else {
				alienoMarroneLbl.putClientProperty("attivo", false);
				alienoMarroneLbl.setToolTipText("Non puoi posizionare un alieno marrone in questa cabina!");
				IconHandler.setIconOnLabel(alienoMarroneLbl, alienoMarroneDisabledIcon, alienoMarroneDisabledIcon);
			}
			
			if (t.accettaAlieno(Abitante.ALIENO_VIOLA, getGiocatore().getNave().getGriglia().getTessereAdiacenti(cellaSelezionata))) {
				alienoViolaLbl.putClientProperty("attivo", true);
				alienoViolaLbl.setToolTipText(null);
				IconHandler.setIconOnLabel(alienoViolaLbl, alienoViolaIcon, hovAlienoViolaIcon);
			} else {
				alienoViolaLbl.putClientProperty("attivo", false);
				alienoViolaLbl.setToolTipText("Non puoi posizionare un alieno viola in questa cabina!");
				IconHandler.setIconOnLabel(alienoViolaLbl, alienoViolaDisabledIcon, alienoViolaDisabledIcon);
			}
			
		}
		
	}
	
	/**
	 * Gestisce la logica associata al click sinistro su una cella della nave.
	 *
	 * @param riga riga della cella selezionata.
	 * @param colonna colonna della cella selezionata.
	 */
	private void logicaClickSinistro(int riga, int colonna) {
		
		if (!celleSelezionabili.contains(getGiocatore().getNave().getGriglia().getCella(riga, colonna))) {
			JOptionPane.showMessageDialog(getDialog(), "Questa cella non è tra quelle selezionabili!", "Errore nella scelta!", JOptionPane.ERROR_MESSAGE);
			return;
		}
		
		for (Cella c : celleSelezionabili) { // resetto il colore del bordo delle celle selezionabili a default, in caso un'altra fosse stata selezionata in precedenza
			pannelloNave.getLabelCella(c.getRiga(), c.getColonna()).setBorder(new LineBorder(Color.GREEN, 2));
		}
		
		pannelloNave.getLabelCella(riga, colonna).setBorder(new LineBorder(Color.RED, 3));
		cellaSelezionata = getGiocatore().getNave().getGriglia().getCella(riga, colonna);
		gestisciBottoniAbilitati();
	}
	
	/**
	 * Aggiunge un abitante (astronauta o alieno) alla tessera selezionata nella nave.
	 *
	 * @throws NullPointerException se {@code abitante} è {@code null}.
	 */
	private void aggiuntaAbitante(Abitante abitante) {
		
		if (abitante == null) {
			String errore = "Il parametro 'abitante' non può essere nullo";
			getLogger().error(errore);
			throw new NullPointerException(errore);
		}
		
		if (numeroAbitantiDaEliminare > 0) {
			int membriEliminati = 0;
			
			if (abitante == Abitante.ASTRONAUTA) membriEliminati = astronautiEliminati;
			if (abitante == Abitante.ALIENO_MARRONE) membriEliminati = alieniMarroniEliminati;
			if (abitante == Abitante.ALIENO_VIOLA) membriEliminati = alieniViolaEliminati;
			
			if (membriEliminati == 0) {
				JOptionPane.showMessageDialog(getDialog(), "Non hai alcun abitante di tipo '" + abitante + "' da piazzare!", "Errore nel piazzamento!", JOptionPane.ERROR_MESSAGE);
				return;
			}
		}
		
		Cabina c = (Cabina) cellaSelezionata.getTessera();
		
		if (abitante.getSpecie() == Specie.ALIENO && getGiocatore().getNave().getNumeroAbitantiPerTipo(abitante) > 0) { // impone che si possa avere al massimo un alieno
			JOptionPane.showMessageDialog(getDialog(), "La tua nave contiene già un " + abitante + ", non puoi averne altri!", "Errore nel piazzamento!", JOptionPane.ERROR_MESSAGE);
			return;
		}
				
		try {
			if (abitante == Abitante.ASTRONAUTA) c.setNumeroAstronauti(c.getNumeroAbitantiPerTipo(abitante) + 1);
			else
			c.setNumeroAlieni(abitante, c.getNumeroAbitantiPerTipo(abitante) + 1, getGiocatore().getNave().getGriglia().getTessereAdiacenti(cellaSelezionata));
		} catch (PiazzamentoEquipaggioNonValidoException e) {
			JOptionPane.showMessageDialog(getDialog(), e.getMessage(), "Errore nel piazzamento!", JOptionPane.ERROR_MESSAGE);
			return;
		}
			
		if (numeroAbitantiDaEliminare == 0) {
			JOptionPane.showMessageDialog(getDialog(), "Hai aggiunto un abitante di tipo '" + abitante + "'!", "Abitante aggiunto!", JOptionPane.INFORMATION_MESSAGE);
		} else {
			abitantiDaEliminareRimasti++;
			contatoreAbitantiDaEliminareLbl.setText("Abitanti da eliminare: " + abitantiDaEliminareRimasti);
			
			switch (abitante) {
			case ASTRONAUTA:
				astronautiEliminati--;
				contatoreAstronautiEliminatiLbl.setText(String.valueOf(astronautiEliminati));
				break;
			case ALIENO_MARRONE:
				alieniMarroniEliminati--;
				contatoreAlieniMarroniEliminatiLbl.setText(String.valueOf(alieniMarroniEliminati));
				break;
			case ALIENO_VIOLA:
				alieniViolaEliminati--;
				contatoreAlieniViolaEliminatiLbl.setText(String.valueOf(alieniViolaEliminati));
				break;
			default:
				break;
			
			}
		}
		
		gestisciBottoniAbilitati();
	}
	
	/**
	 * Elimina un abitante (astronauta o alieno) dalla tessera selezionata nella nave.
	 *
	 * @throws NullPointerException se {@code abitante} è {@code null}.
	 */
	private void eliminazioneAbitante(Abitante abitante) {
		
		if (abitante == null) {
			String errore = "Il parametro 'abitante' non può essere nullo";
			getLogger().error(errore);
			throw new NullPointerException(errore);
		}
		
		Cabina c = (Cabina) cellaSelezionata.getTessera();
		
		try {
			if (abitante == Abitante.ASTRONAUTA) c.setNumeroAstronauti(c.getNumeroAbitantiPerTipo(abitante) - 1);
			else
			c.setNumeroAlieni(abitante, c.getNumeroAbitantiPerTipo(abitante) - 1, getGiocatore().getNave().getGriglia().getTessereAdiacenti(cellaSelezionata));
		} catch (PiazzamentoEquipaggioNonValidoException e) {
			JOptionPane.showMessageDialog(getDialog(), e.getMessage(), "Errore nell'eliminazione!", JOptionPane.ERROR_MESSAGE);
			return;
		}
		
		if (numeroAbitantiDaEliminare == 0) {
			JOptionPane.showMessageDialog(getDialog(), "Hai eliminato un abitante di tipo '" + abitante + "'!", "Abitante eliminato!", JOptionPane.INFORMATION_MESSAGE);
			
		} else {
			
			abitantiDaEliminareRimasti--;
			contatoreAbitantiDaEliminareLbl.setText("Abitanti da eliminare: " + abitantiDaEliminareRimasti);
			
			switch (abitante) {
			case ASTRONAUTA:
				astronautiEliminati++;
				contatoreAstronautiEliminatiLbl.setText(String.valueOf(astronautiEliminati));
				break;
			case ALIENO_MARRONE:
				alieniMarroniEliminati++;
				contatoreAlieniMarroniEliminatiLbl.setText(String.valueOf(alieniMarroniEliminati));
				break;
			case ALIENO_VIOLA:
				alieniViolaEliminati++;
				contatoreAlieniViolaEliminatiLbl.setText(String.valueOf(alieniViolaEliminati));
				break;
			default:
				break;
			
			}
		}
		
		gestisciBottoniAbilitati();
	}

}