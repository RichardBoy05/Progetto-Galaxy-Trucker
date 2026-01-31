package galaxytrucker.src.view.frames;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Deque;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import javax.swing.border.LineBorder;
import galaxytrucker.src.logic.assemblaggio.Cella;
import galaxytrucker.src.logic.assemblaggio.Coordinate;
import galaxytrucker.src.logic.assemblaggio.Mucchio;
import galaxytrucker.src.logic.assemblaggio.Tessera;
import galaxytrucker.src.logic.gioco.Colore;
import galaxytrucker.src.logic.gioco.GameLogger;
import galaxytrucker.src.logic.gioco.Giocatore;
import galaxytrucker.src.logic.gioco.Livello;
import galaxytrucker.src.logic.volo.Evento;
import galaxytrucker.src.view.base.AzioneClickCella;
import galaxytrucker.src.view.base.BlockingView;
import galaxytrucker.src.view.base.GuiBase;
import galaxytrucker.src.view.base.IconHandler;
import galaxytrucker.src.view.components.PannelloNave;
import galaxytrucker.src.view.dialogs.SbirciaCarteDialog;

/**
 * Interfaccia grafica per i turni di assemblaggio della nave.
 * Permette al giocatore di costruire la propria nave selezionando tessere
 * dal mucchio coperto o dalle pile visibili. Offre inoltre la possibilità
 * di prenotare tessere e di sbirciare alcune delle carte evento previste per la partita
 * (funzionalità disabilitate nella modalità di prova).
 * Include anche un timer che limita il tempo disponibile per il turno del giocatore,
 * anch'esso disattivato nella modalità di prova.
 *
 * @see GuiBase
 * @see BlockingView
 */

public class AssemblaggioGui extends GuiBase implements BlockingView {
	
	// costanti
	
	private static final String DIR_PATH = "/galaxytrucker/resources/images/assemblaggio/";
	private static final String BACKGROUND_IMG_PATH = DIR_PATH + "assemblaggio_background.png";
	private static final String TESSERE_NASCOSTE_ICON_PATH = DIR_PATH + "tessera_nascosta.png";
	private static final String HOV_TESSERE_NASCOSTE_ICON_PATH = DIR_PATH + "tessera_nascosta_hover.png";
	private static final String PILA_VUOTA_TESSERE_VISIBILI_ICON_PATH = DIR_PATH + "pila_vuota_tessere_visibili.png";
	private static final String HOV_PILA_VUOTA_TESSERE_VISIBILI_ICON_PATH = DIR_PATH + "pila_vuota_tessere_visibili_hover.png";
	private static final String PILA_VUOTA_TESSERE_VISIBILI_DISABLED_ICON_PATH = DIR_PATH + "pila_vuota_tessere_visibili_disabled.png";
	private static final String PROSSIMO_TURNO_ICON_PATH = DIR_PATH + "prossimo_turno.png";
	private static final String HOV_PROSSIMO_TURNO_ICON_PATH = DIR_PATH + "prossimo_turno_hover.png";
	private static final String SCARTA_TESSERA_ICON_PATH = DIR_PATH + "scarta.png";
	private static final String SCARTA_TESSERA_DISABLED_ICON_PATH = DIR_PATH + "scarta_disabled.png";
	private static final String HOV_SCARTA_TESSERA_ICON_PATH = DIR_PATH + "scarta_hover.png";
	private static final String SBIRCIA_CARTE_ICON_PATH = DIR_PATH + "sbircia_carte.png";
	private static final String SBIRCIA_CARTE_DISABLED_ICON_PATH = DIR_PATH + "sbircia_carte_disabled.png";
	private static final String HOV_SBIRCIA_CARTE_ICON_PATH = DIR_PATH + "sbircia_carte_hover.png";
	
	// rapporti fra le dimensioni di alcuni pannelli della GUI
	private static final double RATIO_PANNELLO_INFO_PANNELLO_NAVE = 0.2;
	private static final double RATIO_PANNELLO_SINISTRO_DESTRO = 0.75;
	
	private static final GameLogger LOGGER = GameLogger.getInstance();
	
	// componenti
	
	private PannelloNave pannelloNave;
	private JPanel pannelloInfo;
	private JPanel bottoniPanel;
	private JSplitPane splitOrizzontale;
	private JSplitPane splitVerticale;
	
	private ImageIcon backgroundIcon;
	private ImageIcon giocatoreIcon;
	private ImageIcon tessereNascosteIcon;
	private ImageIcon hovTessereNascosteIcon;
	private ImageIcon pilaVuotaTessereVisibiliIcon;
	private ImageIcon hovPilaVuotaTessereVisibiliIcon;
	private ImageIcon pilaVuotaTessereVisibiliDisabledIcon;
	private ImageIcon prossimoTurnoIcon;
	private ImageIcon hovProssimoTurnoIcon;
	private ImageIcon scartaTesseraIcon;
	private ImageIcon scartaTesseraDisabledIcon;
	private ImageIcon hovScartaTesseraIcon;
	private ImageIcon sbirciaCarteIcon;
	private ImageIcon sbirciaCarteDisabledIcon;
	private ImageIcon hovSbirciaCarteIcon;
	
	private JButton prossimoTurnoBtn;
	private JButton prenotaTesseraSinistraBtn;
	private JButton prenotaTesseraDestraBtn;	
	private JButton tessereNascosteBtn;
	private JButton scartaTesseraBtn;
	private JButton sbirciaCarteBtn;
	private JButton[] prenotaTesseraBtns = new JButton[2];
	private JButton[] bottoniGriglia = new JButton[10];
	private List<JButton> tuttiIBottoni;

	private List<JLabel> contatoriTessere;
	private JCheckBox assemblaggioCompletatoCb;
	
	private JLabel background;
	private JLabel nomeGiocatoreLbl;
	private JLabel infoAggiuntive1;
	private JLabel infoAggiuntive2;
	private JLabel iconaGiocatoreLbl;
	private JLabel timerLbl;
	private JLabel tesserePrenotateLbl;
	private JLabel[] contatoriGriglia = new JLabel[10];
	
	// altri attributi
	
	private CountDownLatch latch;
	
	private Timer timer;
	private int tempoRimanente = 60; // tempo rimanente in secondi
	
	private final Giocatore giocatore;
	private final Mucchio mucchio;
	private final Livello livello;
	private final List<Evento> carteSbirciabili;
	
	/** Tessera selezionata dal giocatore in un determinato momento. */
	private Tessera tesseraSelezionata = null;

	/** Indica se la tessera selezionata può essere rimessa nella pila di provenienza (tessera visibile) o meno (tessera nascosta). */
	private boolean selezionePermanente = true;

	/** Indica se la mossa (piazzamento) è già stata eseguita. */
	private boolean mossaFatta = false;

	/** {@code false} solo se è stata piazzata una tessera prenotata, non più scartabile. Altrimenti {@code true} */
	private boolean piazzamentoScartabile = true;

	/** Coordinate della tessera posizionata sulla nave. */
	private Coordinate coordinateTesseraPiazzata;

	/** Indice del bottone selezionato nella griglia delle tessere visibili. */
	private int bottoneGrigliaSelezionato = -1;

	/** Indice del bottone della tessera prenotata selezionata. */
	private int bottonePrenotato = -1;

	
	/**
	 * Costruisce un'interfaccia grafica di assemblaggio per
	 * il giocatore, inizializzandone i componenti principali.
	 *
	 * @param giocatore il giocatore corrente.
	 * @param mucchio il mucchio di tessere da cui pescare.
	 * @param livello il livello di difficoltà della partita.
	 * @param carteSbirciabili la lista di eventi che possono essere sbirciati.
	 * @throws NullPointerException se uno qualsiasi dei parametri è {@code null}.
	 */
	public AssemblaggioGui(Giocatore giocatore, Mucchio mucchio, Livello livello, List<Evento> carteSbirciabili) {
		super();
		
		if (giocatore == null) {
			String errore = "Il parametro 'giocatore' non può essere nullo!";
			LOGGER.error(errore);
			throw new NullPointerException(errore);
		}
    	
    	if (mucchio == null) {
			String errore = "Il parametro 'mucchio' non può essere nullo!";
			LOGGER.error(errore);
			throw new NullPointerException(errore);
		}
    	
    	if (livello == null) {
			String errore = "Il parametro 'livello' non può essere nullo!";
			LOGGER.error(errore);
			throw new NullPointerException(errore);
		}
    	
    	if (carteSbirciabili == null) {
			String errore = "Il parametro 'carteSbirciabili' non può essere nullo!";
			LOGGER.error(errore);
			throw new NullPointerException(errore);
		}
		
		this.giocatore = giocatore;
		this.mucchio = mucchio;
		this.livello = livello;
		this.carteSbirciabili = carteSbirciabili;
	}
	
	/**
	 * Mostra la finestra GUI associata a questo evento e attende finché l'utente non chiude l'interfaccia.
	 * <p>
	 * Questo metodo configura e visualizza il frame Swing in modo sincrono sul thread EDT 
	 * (Event Dispatch Thread), assicurandosi che tutte le componenti vengano inizializzate correttamente.
	 * Il metodo blocca il thread chiamante fino a che l'utente non esegue un'azione che rilascia il latch.
	 */
	@Override
	public void mostraEAttendi() {
		
		latch = new CountDownLatch(1);
		
        try {
            SwingUtilities.invokeAndWait(() -> {
            	
            	impostaDimensioniFinestra();
            	getFrame().setLayout(new BorderLayout());
        		
                setupComponents();
                setupLayout();
                setupListeners();          
                getFrame().revalidate();
                getFrame().setVisible(true);
                
            });
            
        } catch (Exception e) {
        	mostraErrore(e.getMessage());
        	LOGGER.error(e.getMessage());
        	e.printStackTrace();
        }

        try {
            latch.await();  // blocca solo il thread chiamante
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            LOGGER.error(e.getMessage());
        }
    }

	/** 
	 * Inizializza e carica tutte le risorse grafiche e i componenti dell'interfaccia utente. 
	 */
	@Override
	public void setupComponents() {
		
		impostaTimer();
		
		backgroundIcon = new ImageIcon(getClass().getResource(BACKGROUND_IMG_PATH));
		giocatoreIcon = new ImageIcon(getClass().getResource(DIR_PATH + giocatore.getColore().toString().toLowerCase() + ".png"));
		tessereNascosteIcon = new ImageIcon(getClass().getResource(TESSERE_NASCOSTE_ICON_PATH));
		hovTessereNascosteIcon = new ImageIcon(getClass().getResource(HOV_TESSERE_NASCOSTE_ICON_PATH));
		pilaVuotaTessereVisibiliIcon = new ImageIcon(getClass().getResource(PILA_VUOTA_TESSERE_VISIBILI_ICON_PATH));
		hovPilaVuotaTessereVisibiliIcon = new ImageIcon(getClass().getResource(HOV_PILA_VUOTA_TESSERE_VISIBILI_ICON_PATH));
		prossimoTurnoIcon = new ImageIcon(getClass().getResource(PROSSIMO_TURNO_ICON_PATH));
		pilaVuotaTessereVisibiliDisabledIcon = new ImageIcon(getClass().getResource(PILA_VUOTA_TESSERE_VISIBILI_DISABLED_ICON_PATH));
		hovProssimoTurnoIcon = new ImageIcon(getClass().getResource(HOV_PROSSIMO_TURNO_ICON_PATH));
		scartaTesseraIcon = new ImageIcon(getClass().getResource(SCARTA_TESSERA_ICON_PATH));
		scartaTesseraDisabledIcon = new ImageIcon(getClass().getResource(SCARTA_TESSERA_DISABLED_ICON_PATH));
		hovScartaTesseraIcon = new ImageIcon(getClass().getResource(HOV_SCARTA_TESSERA_ICON_PATH));
		sbirciaCarteIcon = new ImageIcon(getClass().getResource(SBIRCIA_CARTE_ICON_PATH));
		sbirciaCarteDisabledIcon = new ImageIcon(getClass().getResource(SBIRCIA_CARTE_DISABLED_ICON_PATH));
		hovSbirciaCarteIcon = new ImageIcon(getClass().getResource(HOV_SBIRCIA_CARTE_ICON_PATH));
		
		background = new JLabel(backgroundIcon);		
		iconaGiocatoreLbl = new JLabel();
		nomeGiocatoreLbl = new JLabel("<html><div style='white-space:nowrap; font-weight:normal;'>"
			    + "Giocatore <span style='font-weight:bold;'>" + giocatore.getColore() + "</span>: "
			    + "<i>" + giocatore.getNome() + "</i>"
			    + "</div></html>");
		
		prossimoTurnoBtn = new JButton();
	    prenotaTesseraSinistraBtn = new JButton();
	    prenotaTesseraDestraBtn = new JButton();
	    sbirciaCarteBtn = new JButton();
	}
	
	/** 
	 * Definisce il layout dell'interfaccia grafica, organizzando i pannelli e i componenti sullo schermo. 
	 */
	@Override
	public void setupLayout() {
		
	    background.setLayout(new BorderLayout());

	    pannelloInfo = creaPannelloInfo();
	    pannelloNave = new PannelloNave(giocatore.getNave(), false, new AzioneClickCella() {
	    	@Override
            public void onClickCella(int riga, int colonna, MouseEvent evento) {
                if (SwingUtilities.isLeftMouseButton(evento)) {
                    logicaClickSinistroSuCella(riga, colonna);
                } else if (SwingUtilities.isRightMouseButton(evento)) {
                	logicaClickDestroSuCella(riga, colonna);                
                }
            }
        });

	    pannelloNave.setOpaque(false);

	    splitVerticale = creaSplitPaneVerticale(pannelloInfo, pannelloNave);

	    JPanel pannelloSinistro = new JPanel(new BorderLayout());
	    pannelloSinistro.setOpaque(false);
	    pannelloSinistro.add(splitVerticale, BorderLayout.CENTER);
	    
	    JPanel pannelloDestroInterno = creaPannelloDestro();

	    JPanel contenitoreDestro = new JPanel(new BorderLayout());
	    contenitoreDestro.setOpaque(false);
	    contenitoreDestro.add(pannelloDestroInterno, BorderLayout.CENTER);

	    splitOrizzontale = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, pannelloSinistro, contenitoreDestro);
	    splitOrizzontale.setResizeWeight(RATIO_PANNELLO_SINISTRO_DESTRO);
	    splitOrizzontale.setDividerSize(5);
	    splitOrizzontale.setOpaque(false);
	    splitOrizzontale.setBorder(null);
	    splitOrizzontale.setEnabled(false);
	    
	    tuttiIBottoni = new ArrayList<>();
	    tuttiIBottoni.add(tessereNascosteBtn);
	    tuttiIBottoni.add(scartaTesseraBtn);
	    Collections.addAll(tuttiIBottoni, bottoniGriglia);
	    Collections.addAll(tuttiIBottoni, prenotaTesseraBtns);

	    gestisciSelezioneEsclusiva(tuttiIBottoni);

	    background.add(splitOrizzontale, BorderLayout.CENTER);
	    getFrame().setContentPane(background);
	}
	
	/** 
	 * Registra i listener per la gestione degli eventi dell'interfaccia grafica (click e resize). 
	 */
	@Override
	public void setupListeners() {
		
	    prossimoTurnoBtn.addActionListener(e -> {
	        fineTurno();			
	    });
	    
	    sbirciaCarteBtn.addActionListener(e -> {
	    	
	    	if (livello == Livello.P) return;
	    	new SbirciaCarteDialog(getFrame(), carteSbirciabili);
	    	
	    	if (mossaFatta) fineTurno(); // guardare una pila salda il componente in modo definitivo
	    });
	    
	    for (int i = 0; i < bottoniGriglia.length; i++) {
	    	final int indiceBottone = i;
	    	bottoniGriglia[i].addMouseListener(new MouseAdapter() {
	            @Override
	            public void mouseClicked(MouseEvent e) {
	                if (SwingUtilities.isRightMouseButton(e)) {
	                	
	                	if (tesseraSelezionata != null && bottoniGriglia[indiceBottone].getBorder() != null) {
	                		mostraErrore("Deseleziona la pila prima di poterne cambiare l'ordine!");
	                		return;
	                	}
	                	
	                	Map<Class<? extends Tessera>, Deque<Tessera>> map = mucchio.getCopiaTessereVisibili();               	
	                	if (map.keySet().size() == 0) return;
	                	
	                	List<Class<? extends Tessera>> keys = new ArrayList<>(map.keySet());
	                	giocatore.seppellisciTesseraVisibile(mucchio, keys.get(indiceBottone));
	                	
	                	aggiornaGrigliaTessereVisibili(true);     	
	                	
	                }
	                
	            }
	        });   
	    }
	    
	    pannelloInfo.addComponentListener(new ComponentAdapter() {
	        @Override
	        public void componentResized(ComponentEvent e) {
	            int height = pannelloInfo.getHeight() - 20; // lascia un po' di padding
	            Image scaled = giocatoreIcon.getImage().getScaledInstance(-1, height, Image.SCALE_SMOOTH);
	            iconaGiocatoreLbl.setIcon(new ImageIcon(scaled));
	        }
	    });
	    
	    getFrame().addComponentListener(new ComponentAdapter() {
	        @Override
	        public void componentResized(ComponentEvent e) {
	            int width = getFrame().getWidth();
	            int height = getFrame().getHeight();

	            splitOrizzontale.setDividerLocation((int) (width * RATIO_PANNELLO_SINISTRO_DESTRO));
	            splitVerticale.setDividerLocation((int) (height * RATIO_PANNELLO_INFO_PANNELLO_NAVE));
	        }
	    });
	    
	    nomeGiocatoreLbl.addComponentListener(new ComponentAdapter() {
	        @Override
	        public void componentResized(ComponentEvent e) {
	            adattaFont(nomeGiocatoreLbl, 40);
	        }
	    });
	    
	    infoAggiuntive2.addComponentListener(new ComponentAdapter() {
	        @Override
	        public void componentResized(ComponentEvent e) {
	            adattaFont(infoAggiuntive2, 30);
	            infoAggiuntive1.setFont(infoAggiuntive2.getFont());
	        }
	    });
	    
	    bottoniPanel.addComponentListener(new ComponentAdapter() {
	        @Override
	        public void componentResized(ComponentEvent e) {
	            int width = bottoniPanel.getWidth();

	            int buttonSize = (width / 2);

	            Dimension dim = new Dimension(buttonSize, buttonSize);

	            for (JButton btn : new JButton[]{prenotaTesseraSinistraBtn, prenotaTesseraDestraBtn}) {
	                btn.setPreferredSize(dim);
	                btn.setMaximumSize(dim);
	                btn.setMinimumSize(dim);
	            }

	            bottoniPanel.revalidate();
	        }
	    }); 


	}

	/**
	 * Crea e restituisce il pannello contenente le informazioni del giocatore, 
	 * tra cui icona, nome e tessere prenotate.
	 *
	 * @return il pannello informativo del giocatore.
	 */
	private JPanel creaPannelloInfo() {
	    JPanel pannelloInfo = new JPanel(new BorderLayout());
	    pannelloInfo.setOpaque(false);

	    pannelloInfo.add(creaIconaGiocatorePanel(), BorderLayout.WEST);
	    pannelloInfo.add(creaInfoCentralePanel(), BorderLayout.CENTER);
	    pannelloInfo.add(creaTesserePrenotatePanel(), BorderLayout.EAST);

	    return pannelloInfo;
	}

	/**
	 * Crea e restituisce il pannello con l'icona del giocatore, 
	 * scalata e centrata verticalmente.
	 *
	 * @return il pannello contenente l'icona del giocatore.
	 */
	private JPanel creaIconaGiocatorePanel() {
	    JPanel iconaGiocatorePanel = new JPanel();
	    iconaGiocatorePanel.setOpaque(false);
	    iconaGiocatorePanel.setLayout(new BoxLayout(iconaGiocatorePanel, BoxLayout.Y_AXIS));
	    iconaGiocatorePanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 0));
	    iconaGiocatorePanel.setMaximumSize(new Dimension(100, Integer.MAX_VALUE));
	    
	    iconaGiocatoreLbl.setIcon(giocatoreIcon);
	    Image img = giocatoreIcon.getImage();
	    Image scaledImg = img.getScaledInstance(50, 50, Image.SCALE_SMOOTH);
	    iconaGiocatoreLbl.setIcon(new ImageIcon(scaledImg));
	    iconaGiocatoreLbl.setAlignmentX(Component.CENTER_ALIGNMENT);
	    iconaGiocatorePanel.add(iconaGiocatoreLbl);
	    
	    return iconaGiocatorePanel;
	}

	/**
	 * Crea e restituisce il pannello centrale con nome del giocatore e istruzioni testuali.
	 *
	 * @return il pannello con informazioni testuali sul turno e sulle azioni.
	 */
	private JPanel creaInfoCentralePanel() {
	    JPanel panel = new JPanel();
	    panel.setOpaque(false);
	    panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
	    panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
	    panel.setMaximumSize(new Dimension(Short.MAX_VALUE, 120));

	    nomeGiocatoreLbl.setFont(nomeGiocatoreLbl.getFont().deriveFont(40f));
	    nomeGiocatoreLbl.setForeground(Color.WHITE);
	    nomeGiocatoreLbl.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));
	    nomeGiocatoreLbl.setHorizontalAlignment(SwingConstants.CENTER);
	    nomeGiocatoreLbl.setAlignmentX(Component.CENTER_ALIGNMENT);

	    infoAggiuntive1 = new JLabel("<html><div style='white-space:nowrap;'>"
	        + "<b>Costruisci la tua nave</b> pescando le tessere dalla sezione a destra!"
	        + "</div></html>");
	    infoAggiuntive1.setFont(new Font("Arial", Font.PLAIN, 16));
	    infoAggiuntive1.setForeground(Color.LIGHT_GRAY);
	    infoAggiuntive1.setAlignmentX(Component.CENTER_ALIGNMENT);
	    infoAggiuntive1.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
	    infoAggiuntive1.setHorizontalAlignment(SwingConstants.CENTER);

	    infoAggiuntive2 = new JLabel("<html><div style='white-space:nowrap;'>"
	        + "Puoi <b>piazzarle</b>, <b>ruotarle (click destro)</b>, <b>scartarle</b> o <b>prenotarle</b> sulla plancia!"
	        + "</div></html>");
	    infoAggiuntive2.setFont(new Font("Arial", Font.PLAIN, 16));
	    infoAggiuntive2.setForeground(Color.LIGHT_GRAY);
	    infoAggiuntive2.setAlignmentX(Component.CENTER_ALIGNMENT);
	    infoAggiuntive2.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
	    infoAggiuntive2.setHorizontalAlignment(SwingConstants.CENTER);

	    panel.add(Box.createVerticalStrut(5));
	    panel.add(nomeGiocatoreLbl);
	    panel.add(Box.createVerticalStrut(10));
	    panel.add(infoAggiuntive1);
	    panel.add(infoAggiuntive2);
	    panel.add(Box.createVerticalStrut(5));

	    return panel;
	}

	/**
	 * Crea e restituisce il pannello per la visualizzazione e gestione delle tessere prenotate.
	 *
	 * @return il pannello con i bottoni delle tessere prenotate.
	 */
	private JPanel creaTesserePrenotatePanel() {
	    JPanel panel = new JPanel(new GridBagLayout());
	    panel.setOpaque(false);
	    panel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 10));

	    GridBagConstraints gbc = new GridBagConstraints();
	    gbc.gridx = 0;
	    gbc.fill = GridBagConstraints.HORIZONTAL;
	    gbc.anchor = GridBagConstraints.CENTER;
	    gbc.weightx = 1.0;

	    // label
	    tesserePrenotateLbl = new JLabel("Tessere prenotate");
	    tesserePrenotateLbl.setToolTipText((livello != Livello.P) ? "Assicurati di piazzare le tessere prenotate prima di completare la nave, altrimenti pagherai una penalità!" : null);
	    tesserePrenotateLbl.setFont(tesserePrenotateLbl.getFont().deriveFont(Font.BOLD, 15));
	    tesserePrenotateLbl.setForeground(Color.WHITE);

	    gbc.gridy = 0;
	    gbc.insets = new Insets(0, 0, 5, 0); // spazio minimo sotto il testo
	    panel.add(tesserePrenotateLbl, gbc);

	    // pannello bottoni
	    bottoniPanel = new JPanel(new GridBagLayout());
	    bottoniPanel.setOpaque(false);

	    GridBagConstraints btnGbc = new GridBagConstraints();
	    btnGbc.gridy = 0;
	    btnGbc.weightx = 1.0;
	    btnGbc.fill = GridBagConstraints.BOTH;

	    prenotaTesseraBtns[0] = prenotaTesseraSinistraBtn;
	    prenotaTesseraBtns[1] = prenotaTesseraDestraBtn;
	    
	    List<Tessera> tesserePrenotate = giocatore.vediTesserePrenotate();

	    for (int i = 0; i < prenotaTesseraBtns.length; i++) {
	        JButton btn = prenotaTesseraBtns[i];
	        btn.setMinimumSize(new Dimension(20, 20));
	        btn.setPreferredSize(new Dimension(50, 50));
	        btn.setMaximumSize(new Dimension(100, 100));

	        if (livello == Livello.P) {
	            IconHandler.setIconOnButton(btn, pilaVuotaTessereVisibiliDisabledIcon, pilaVuotaTessereVisibiliDisabledIcon);
	            btn.setToolTipText("Prenotazione tessere disabilitata nella modalità di prova!");
	        } else {
	            Tessera tessera = (i < tesserePrenotate.size()) ? tesserePrenotate.get(i) : null;

	            if (tessera == null) {
	                IconHandler.setIconOnButton(btn, pilaVuotaTessereVisibiliIcon, hovPilaVuotaTessereVisibiliIcon);
	            } else {
	                IconHandler.setIconOnButton(btn, tessera.getImmagine(), tessera.getImmagineHovered());
	            }
	        }

	        btn.putClientProperty("selezionabile", true);
	        impostaLookDefaultBottone(btn);

	        btnGbc.gridx = i;
	        bottoniPanel.add(btn, btnGbc);
	    }

	    gbc.gridy = 1;
	    gbc.insets = new Insets(0, 0, 0, 0);
	    panel.add(bottoniPanel, gbc);

	    return panel;
	}

	/**
	 * Crea uno split pane verticale che divide due pannelli (superiore e inferiore)
	 * mantenendo un rapporto di dimensionamento predefinito.
	 *
	 * @param top il pannello superiore.
	 * @param bottom il pannello inferiore.
	 * @return lo split pane verticale configurato.
	 */
	private JSplitPane creaSplitPaneVerticale(JPanel top, JPanel bottom) {
	    JSplitPane split = new JSplitPane(JSplitPane.VERTICAL_SPLIT, top, bottom);
	    
	    split.setResizeWeight(RATIO_PANNELLO_INFO_PANNELLO_NAVE);
	    split.setDividerSize(4);
	    split.setOpaque(false);
	    split.setBorder(null);
	    split.setEnabled(false);
	    return split;
	}

	/**
	 * Crea il pannello destro principale.
	 *
	 * @return il pannello verticale sul lato destro dell’interfaccia.
	 */
	private JPanel creaPannelloDestro() {
	    JPanel destro = new JPanel();
	    destro.setLayout(new BoxLayout(destro, BoxLayout.Y_AXIS));
	    destro.setOpaque(false);

	    destro.add(creaPannelloDestroAlto());
	    destro.add(Box.createVerticalStrut(10));
	    destro.add(creaTessereNascostePanel());
	    destro.add(Box.createVerticalStrut(20));
	    destro.add(creaTessereVisibiliPanel());
	    destro.add(Box.createVerticalStrut(10));
	    destro.add(Box.createVerticalGlue());
	    destro.add(creaFineTurnoPanel());
	    destro.add(Box.createVerticalStrut(10));

	    return destro;
	}

	/**
	 * Crea la parte superiore del pannello destro con il timer e il pulsante
	 * per sbirciare le carte evento.
	 *
	 * @return il pannello contenente il timer e il pulsante "sbircia carte".
	 */
	private JPanel creaPannelloDestroAlto() {
	    JPanel panel = new JPanel(new BorderLayout());
	    panel.setOpaque(false);
	    panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));

	    timerLbl.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 10));
	    timerLbl.setHorizontalAlignment(SwingConstants.RIGHT);
	    timerLbl.setVerticalAlignment(SwingConstants.TOP);

        sbirciaCarteBtn.setContentAreaFilled(false);         
        sbirciaCarteBtn.setFocusPainted(false);
        sbirciaCarteBtn.setFocusable(false);
        sbirciaCarteBtn.setOpaque(false);   
	    sbirciaCarteBtn.setBorder(BorderFactory.createEmptyBorder(10, 10, 0, 0));
	    sbirciaCarteBtn.setHorizontalAlignment(SwingConstants.LEFT);
	    sbirciaCarteBtn.setMinimumSize(new Dimension(16, 8));
	    sbirciaCarteBtn.setPreferredSize(new Dimension(40, 20));
	    sbirciaCarteBtn.setMaximumSize(new Dimension(60, 30));

	    if (livello == Livello.P) {
	        IconHandler.setIconOnButton(sbirciaCarteBtn, sbirciaCarteDisabledIcon, sbirciaCarteDisabledIcon);
	        sbirciaCarteBtn.setToolTipText("Non puoi sbirciare le carte nella modalità di prova!");
	    } else {
	        IconHandler.setIconOnButton(sbirciaCarteBtn, sbirciaCarteIcon, hovSbirciaCarteIcon);
	        sbirciaCarteBtn.setToolTipText("Sbircia alcune carte evento! Se hai già piazzato una tessera, il turno finirà!");
	    }

	    JPanel topPanel = new JPanel(new BorderLayout());
	    topPanel.setOpaque(false);
	    topPanel.add(sbirciaCarteBtn, BorderLayout.WEST);
	    topPanel.add(timerLbl, BorderLayout.EAST);

	    panel.add(topPanel, BorderLayout.NORTH);

	    return panel;
	}

	/**
	 * Crea il pannello contenente il pulsante per pescare tessere nascoste e quello per scartarle.
	 *
	 * @return il pannello delle tessere nascoste.
	 */
	private JPanel creaTessereNascostePanel() {
	    JPanel panel = new JPanel(new GridBagLayout());
	    panel.setOpaque(false);

	    GridBagConstraints gbc = new GridBagConstraints();
	    gbc.gridx = 0;
	    gbc.gridy = 0;
	    gbc.insets = new Insets(0, 0, 10, 0); // gap inferiore di 5 pixel
	    gbc.anchor = GridBagConstraints.CENTER;

	    tessereNascosteBtn = new JButton();
	    tessereNascosteBtn.setMinimumSize(new Dimension(70, 70));
	    tessereNascosteBtn.setPreferredSize(new Dimension(120, 120));
	    tessereNascosteBtn.setMaximumSize(new Dimension(140, 140));
	    
	    impostaLookDefaultBottone(tessereNascosteBtn);    
	    tessereNascosteBtn.setToolTipText("Clicca per pescare una tessera dal mucchio!");

	    IconHandler.setIconOnButton(tessereNascosteBtn, tessereNascosteIcon, hovTessereNascosteIcon);
	    tessereNascosteBtn.putClientProperty("selezionabile", true);

	    panel.add(tessereNascosteBtn, gbc);

	    gbc.gridy = 1;
	    gbc.insets = new Insets(0, 0, 0, 0);

	    scartaTesseraBtn = new JButton();
	    
	    impostaLookDefaultBottone(scartaTesseraBtn);
        
	    scartaTesseraBtn.setMinimumSize(new Dimension(77, 30));
	    scartaTesseraBtn.setPreferredSize(new Dimension(103, 40));
	    scartaTesseraBtn.setMaximumSize(new Dimension(154, 60));
	    
	    IconHandler.setIconOnButton(scartaTesseraBtn, scartaTesseraDisabledIcon, scartaTesseraDisabledIcon);
	    scartaTesseraBtn.putClientProperty("selezionabile", false);
	    
	    panel.add(scartaTesseraBtn, gbc);

	    return panel;
	}

	/**
	 * Crea la griglia di pannelli con i pulsanti delle tessere visibili
	 * e i relativi contatori, adattando le dimensioni allo schermo.
	 *
	 * @return il pannello con le tessere visibili e i contatori associati.
	 */
	private JPanel creaTessereVisibiliPanel() {
	    JPanel panel = new JPanel(new GridLayout(5, 2, 2, 10));
	    panel.setOpaque(false);
	    panel.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 20));
	    contatoriTessere = new ArrayList<JLabel>();
	    
	    // aggiustamento dimensioni minime dei bottoni
	    Dimension min;
	    Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
	    
	    if (screenSize.height > 1000) {
	    	min = new Dimension(80, 80);
	    } else if (screenSize.height > 800 && screenSize.height <= 1000){
	    	min = new Dimension(65, 65);
	    } else if (screenSize.height > 700 && screenSize.height <= 800){
	    	min = new Dimension(50, 50);
	    } else {
	    	min = new Dimension(40, 40);
	    }
     
	    Dimension pref = new Dimension(80, 80);
	    Dimension max = new Dimension(120, 120);

	    for (int i = 0; i < 10; i++) {

	        JPanel cella = new JPanel(new GridBagLayout());
	        cella.setOpaque(false);
	        GridBagConstraints gbc = new GridBagConstraints();
	        gbc.insets = new Insets(0, 3, 0, 3);  // Spaziatura uniforme

	        JButton bottone = new JButton();
	        bottoniGriglia[i] = bottone;
	        bottone.setMinimumSize(min);
	        bottone.setPreferredSize(pref);
	        bottone.setMaximumSize(max);
	        bottone.setToolTipText("Click sinistro per selezionare, destro per vedere la tessera sotto!");
	        IconHandler.setIconOnButton(bottone, pilaVuotaTessereVisibiliIcon, hovPilaVuotaTessereVisibiliIcon);
	        bottone.putClientProperty("selezionabile", false); // selezionabile solo quando contengono almeno una tessera
	        
	        impostaLookDefaultBottone(bottone);

	        JLabel contatore = new JLabel("0");
	        contatoriGriglia[i] = contatore;
	        contatore.setForeground(Color.WHITE);
	        contatore.setFont(contatore.getFont().deriveFont(Font.BOLD, 12));
	        contatoriTessere.add(contatore);

	        if (i % 2 == 0) {
	            // contatore a sinistra
	            gbc.gridx = 0;
	            gbc.anchor = GridBagConstraints.CENTER;
	            cella.add(contatore, gbc);

	            gbc.gridx = 1;
	            cella.add(bottone, gbc);
	        } else {
	            // bottone a sinistra
	            gbc.gridx = 0;
	            cella.add(bottone, gbc);

	            gbc.gridx = 1;
	            cella.add(contatore, gbc);
	        }

	        panel.add(cella);
	    }
	    
	    aggiornaGrigliaTessereVisibili(true);

	    return panel;
	}

	/**
	 * Crea il pannello di fine turno con il pulsante "prossimo turno"
	 * e la checkbox per confermare l'assemblaggio completato.
	 *
	 * @return il pannello di controllo per terminare il turno.
	 */
	private JPanel creaFineTurnoPanel() {
	    JPanel panel = new JPanel(new GridBagLayout());
	    panel.setOpaque(false);

	    GridBagConstraints gbc = new GridBagConstraints();
	    gbc.gridx = 0;
	    gbc.gridy = 0;
	    gbc.insets = new Insets(0, 0, 10, 0);
	    gbc.anchor = GridBagConstraints.CENTER;

        impostaLookDefaultBottone(prossimoTurnoBtn);
	    prossimoTurnoBtn.setMinimumSize(new Dimension(147, 35));
	    prossimoTurnoBtn.setPreferredSize(new Dimension(252, 60));
	    prossimoTurnoBtn.setMaximumSize(new Dimension(252, 60));

	    IconHandler.setIconOnButton(prossimoTurnoBtn, prossimoTurnoIcon, hovProssimoTurnoIcon);
	    panel.add(prossimoTurnoBtn, gbc);

	    assemblaggioCompletatoCb = new JCheckBox("Assemblaggio finito");    
	    
	    int size = 18;
	    int frameWidth = getFrame().getWidth();    
	    
	    if (frameWidth > 1200) size = 17;
	    else if (frameWidth > 1000) size = 14;
	    else size = 12;
	    
	    assemblaggioCompletatoCb.setFont(assemblaggioCompletatoCb.getFont().deriveFont(Font.BOLD, size));
	    assemblaggioCompletatoCb.setOpaque(false);
	    assemblaggioCompletatoCb.setForeground(Color.WHITE);
	    assemblaggioCompletatoCb.setHorizontalAlignment(SwingConstants.CENTER);
	    assemblaggioCompletatoCb.setFocusable(false);

	    gbc.gridy = 1;
	    gbc.insets = new Insets(0, 0, 0, 0);
	    panel.add(assemblaggioCompletatoCb, gbc);

	    return panel;
	}
	
	/**
	 * Gestisce la selezione esclusiva tra i bottoni dell'interfaccia.
	 * <p>
	 * Permette di selezionare una sola azione alla volta tra i bottoni di pesca tessera nascosta,
	 * scarto tessera, selezione tessera visibile o prenotazione. Cambia dinamicamente il bordo
	 * del bottone attivo e aggiorna lo stato della tessera selezionata.
	 *
	 * @param tuttiIBottoni la lista completa dei bottoni interattivi nell'interfaccia.
	 * @throws NullPointerException se {@code tuttiIBottoni} è {@code null}.
	 */
	public void gestisciSelezioneEsclusiva(List<JButton> tuttiIBottoni) {
		
		if (tuttiIBottoni == null) {
			String errore = "Il parametro 'tuttiIBottoni' non può essere nullo!";
			LOGGER.error(errore);
			throw new NullPointerException(errore);
		}

	    final JButton[] bottoneAttivo = { null };
	    final int[] indiceAttivo = { -1 };

	    for (int i = 0; i < tuttiIBottoni.size(); i++) {
	        final int indiceBottone = i;
	        JButton bottone = tuttiIBottoni.get(i);

	        bottone.addActionListener(e -> {       	        	
	        	
	            if (Boolean.FALSE.equals(bottone.getClientProperty("selezionabile")) || mossaFatta) return;
	            if (livello == Livello.P && (indiceBottone == 12 || indiceBottone == 13)) return;
	            
	            if (bottonePrenotato != -1 && indiceBottone != bottonePrenotato) {
	        		
	            	giocatore.prenotaTessera(tesseraSelezionata, bottonePrenotato - 12);
	            	tesseraSelezionata = null;
	            	bottonePrenotato = -1;
            			
            	}
	            
	            if (bottoneAttivo[0] != null && bottoneAttivo[0] != bottone) {
	                bottoneAttivo[0].setBorder(null);
	            }

	            if (indiceBottone != 1) {
	                bottone.setBorderPainted(true);
	                bottone.setBorder(new LineBorder(Color.RED, 3));
	            }

	            bottoneAttivo[0] = bottone;
	            indiceAttivo[0] = indiceBottone;
	            
	            if (indiceBottone == 0) {
	            	 bottoneGrigliaSelezionato = -1;
	            	 piazzamentoScartabile = true;
	                gestisciPescaTesseraNascosta();
	            } else if (indiceBottone == 1) {           	
	            	 bottoneGrigliaSelezionato = -1;
	                gestisciScartoTessera();
	            } else if (indiceBottone >= 2 && indiceBottone <= 11){
	            	piazzamentoScartabile = true;
	            	bottoneGrigliaSelezionato = indiceBottone;
	            }
	            else if (indiceBottone >= 12){
	            	gestisciPrenotazioneTessere(indiceBottone);
	            }
	        });
	    }
	}
	
	/**
	 * Gestisce la logica per la pesca di una tessera nascosta dal mucchio.
	 * <p>
	 * Controlla che non ci sia una selezione permanente (altra tessera nascosta) non usata,
	 * aggiorna l'icona del bottone tessera nascosta e disabilita la griglia di tessere visibili.
	 * In caso di errore (es. mucchio vuoto), mostra un messaggio.
	 */
	private void gestisciPescaTesseraNascosta() {

	    if (tesseraSelezionata != null && selezionePermanente) {
	    	mostraErrore("Prima devi usare la tessera che hai pescato!");   	
	        return;
	    }

	    tesseraSelezionata = giocatore.pescaTesseraNascosta(mucchio);
	    
	    if (tesseraSelezionata == null) {
	    	mostraErrore("Tessere nascoste esaurite!");
	    	return;
	    }
	    
	    for (int i = 2; i < 12; i++) tuttiIBottoni.get(i).putClientProperty("selezionabile", false);
	    
	    IconHandler.setIconOnButton(tessereNascosteBtn, tesseraSelezionata.getImmagine(), tesseraSelezionata.getImmagineHovered());
	    selezionePermanente = true;
	    aggiornaPulsanteScarta(true);
	}

	/**
	 * Gestisce lo scarto della tessera selezionata da parte del giocatore.
	 * <p>
	 * Rimuove la tessera selezionata, aggiorna l’interfaccia, reimposta le icone,
	 * abilita i bottoni disabilitati e aggiorna lo stato della griglia visibile.
	 */
	private void gestisciScartoTessera() {
		
	    if (tesseraSelezionata == null) return;
	    giocatore.scartaTessera(mucchio, tesseraSelezionata);    
	    tesseraSelezionata = null;
	    IconHandler.setIconOnButton(tessereNascosteBtn, tessereNascosteIcon, hovTessereNascosteIcon);
	    
	    if (bottonePrenotato != -1) {
	    	IconHandler.setIconOnButton(tuttiIBottoni.get(bottonePrenotato), pilaVuotaTessereVisibiliIcon, hovPilaVuotaTessereVisibiliIcon);
	    	for (int index = 0; index < tuttiIBottoni.size(); index++) {
    			if (index != 1) tuttiIBottoni.get(index).putClientProperty("selezionabile", true);
    		}
	    	bottonePrenotato = -1;
	    }
	    
	    tessereNascosteBtn.setBorder(null);
	    aggiornaPulsanteScarta(false);
	    aggiornaGrigliaTessereVisibili(true);

	}

	/**
	 * Annulla tutte le selezioni grafiche attive. Rimuove i bordi da tutti i bottoni e disattiva il pulsante di scarto.
	 */
	private void annullaSelezioni() {
		for (JButton btn: tuttiIBottoni) {
			btn.setBorder(null);
		}
		aggiornaPulsanteScarta(false);
	}
	
	/**
	 * Abilita i bottoni dell’interfaccia per permettere nuove azioni al giocatore.
	 * <p>
	 * Imposta lo stato "selezionabile" per ogni bottone in base alle condizioni
	 * di gioco e aggiorna le icone e i tooltip relativi.
	 *
	 * @param tesseraSelezionata la tessera da mostrare nel bottone pesca tessera nascosta.
	 */
	private void abilitaBottoni(Tessera tesseraSelezionata) {
		
		for (int i = 0; i < tuttiIBottoni.size(); i++) {
			
			tuttiIBottoni.get(i).putClientProperty("selezionabile", !mossaFatta);
			if (i >= 2 && ((i - 2)  >= mucchio.getCopiaTessereVisibili().size()) && i <= 11) tuttiIBottoni.get(i).putClientProperty("selezionabile", false);
			
			if (i == 0) IconHandler.setIconOnButton(tessereNascosteBtn, tesseraSelezionata.getImmagine(), tesseraSelezionata.getImmagineHovered());
			
			if (i == 0) tuttiIBottoni.get(i).setToolTipText("Clicca per pescare una tessera dal mucchio!");
			if (i >= 2 && i <= 11) tuttiIBottoni.get(i).setToolTipText("Click sinistro per selezionare, destro per vedere la tessera sotto!");
			
		}

	}
	
	/**
	 * Disabilita tutti i bottoni nell'interfaccia utente.
	 * <p>
	 * Impedisce ulteriori azioni e imposta le icone corrette a seconda
	 * della disponibilità di tessere visibili nel mucchio.
	 *
	 * @param esistonoTessereVisibili {@code true} se ci sono delle tessere visibili, {@code false} altrimenti.
	 */
	private void disabilitaBottoni(boolean esistonoTessereVisibili) {
		annullaSelezioni();
		
		for (int i = 0; i < tuttiIBottoni.size(); i++) {
			tuttiIBottoni.get(i).putClientProperty("selezionabile", false);
			
			if (i == 0) IconHandler.setIconOnButton(tessereNascosteBtn, tessereNascosteIcon, tessereNascosteIcon);
			if (i >= 2 && i < 12 && !esistonoTessereVisibili) IconHandler.setIconOnButton(tuttiIBottoni.get(i), pilaVuotaTessereVisibiliIcon, pilaVuotaTessereVisibiliIcon);	
			
		}

	}

	/**
	 * Aggiorna lo stato visivo e la selezionabilità del pulsante di scarto.
	 *
	 * @param attivo {@code true} per attivare il pulsante, {@code false} per disattivarlo.
	 */
	private void aggiornaPulsanteScarta(boolean attivo) {
		
		scartaTesseraBtn.putClientProperty("selezionabile", attivo);
		
	    if (attivo) {
	        IconHandler.setIconOnButton(scartaTesseraBtn, scartaTesseraIcon, hovScartaTesseraIcon);
	    } else {
	        IconHandler.setIconOnButton(scartaTesseraBtn, scartaTesseraDisabledIcon, scartaTesseraDisabledIcon);
	    }
	}
	
	/**
	 * Aggiorna la griglia delle tessere visibili nell'interfaccia utente.
	 * <p>
	 * Imposta le icone e i contatori per ogni pila di tessere,
	 * abilita o disabilita la selezione in base allo stato attuale del gioco.
	 *
	 * @param isSelezionabile {@code true} se le tessere devono essere interattive, {@code false} altrimenti.
	 */
	private void aggiornaGrigliaTessereVisibili(boolean isSelezionabile) {
	    int index = 0;

	    for (Map.Entry<Class<? extends Tessera>, Deque<Tessera>> entry : mucchio.getCopiaTessereVisibili().entrySet()) {
	        if (index >= bottoniGriglia.length) break;

	        JButton bottone = bottoniGriglia[index];
	        JLabel contatore = contatoriGriglia[index];

	        Deque<Tessera> pila = entry.getValue();
	        Tessera primaTessera = pila.peekFirst();
	        
	        if (primaTessera != null) {
	            contatore.setText(String.valueOf(pila.size()));
	            if (isSelezionabile) {
		            IconHandler.setIconOnButton(bottone, primaTessera.getImmagine(), primaTessera.getImmagineHovered());
	            	bottone.putClientProperty("selezionabile", !mossaFatta);
	            }
	        } else {
	            
	        	if (isSelezionabile) IconHandler.setIconOnButton(bottone, pilaVuotaTessereVisibiliIcon, hovPilaVuotaTessereVisibiliIcon);
	            contatore.setText("0");
	            bottone.putClientProperty("selezionabile", false);
	        }

	        index++;
	    }

	    // pulizia dei bottoni restanti se meno di 10 tipi, riparte dall'index a cui ci si era fermati
	    for (; index < bottoniGriglia.length; index++) {
	    	if (isSelezionabile) IconHandler.setIconOnButton(bottoniGriglia[index], pilaVuotaTessereVisibiliIcon, hovPilaVuotaTessereVisibiliIcon);
	        contatoriGriglia[index].setText("0");
	    }
	}
	
	/**
	 * Gestisce la logica relativa alla prenotazione o al recupero di tessere da parte del giocatore.
	 * 
	 * <p>
	 * Il metodo verifica se il bottone cliccato è valido per l'interazione,
	 * quindi controlla se il relativo slot è vuoto o già occupato. Se vuoto, 
	 * prenota la tessera selezionata o una tessera visibile. Se occupato,
	 * consente al giocatore di selezionare la tessera precedentemente prenotata.
	 * 
	 * @param indiceBottone l'indice del bottone premuto corrispondente a uno slot di prenotazione.
	 */
	private void gestisciPrenotazioneTessere(int indiceBottone) {
		
		if (Math.abs(bottonePrenotato - indiceBottone) == 1) return;
		
	    List<Tessera> tesserePrenotate = giocatore.vediTesserePrenotate();
	    int indiceSlot = indiceBottone - 12;

	    if (tesserePrenotate.get(indiceSlot) == null) { // caso vuota

	    	if (tesseraSelezionata == null  && bottoneGrigliaSelezionato == -1) {
		        mostraErrore("Devi prima selezionare una tessera!");
		        tuttiIBottoni.get(indiceBottone).setBorder(null);
		        return;
		    }
	    	
	    	if (tesseraSelezionata == null && bottoneGrigliaSelezionato != -1) {
	    		
	    		Map<Class<? extends Tessera>, Deque<Tessera>> map = mucchio.getCopiaTessereVisibili();               		
		    	List<Class<? extends Tessera>> keys = new ArrayList<>(map.keySet());
		    	
			    tesseraSelezionata = giocatore.pescaTesseraVisibile(mucchio, keys.get(bottoneGrigliaSelezionato - 2));
			    
	    		giocatore.prenotaTessera(tesseraSelezionata, indiceSlot);
		    	IconHandler.setIconOnButton(tuttiIBottoni.get(indiceBottone), tesseraSelezionata.getImmagine(), tesseraSelezionata.getImmagineHovered());
		    	tuttiIBottoni.get(indiceBottone).setBorder(null);
	        	tesseraSelezionata = null;
	        	bottoneGrigliaSelezionato = -1;
	        	aggiornaGrigliaTessereVisibili(true);
	        	return;
	    	}
    	
	    	giocatore.prenotaTessera(tesseraSelezionata, indiceSlot);
	    	IconHandler.setIconOnButton(tuttiIBottoni.get(indiceBottone), tesseraSelezionata.getImmagine(), tesseraSelezionata.getImmagineHovered());
		    aggiornaPulsanteScarta(false);
		    tesseraSelezionata = null;
		    tuttiIBottoni.get(indiceBottone).setBorder(null);
		    IconHandler.setIconOnButton(tessereNascosteBtn, tessereNascosteIcon, hovTessereNascosteIcon);
		    tessereNascosteBtn.setBorder(null);
		    
		    Map<Class<? extends Tessera>, Deque<Tessera>> map = mucchio.getCopiaTessereVisibili();               		
	    	List<Class<? extends Tessera>> keys = new ArrayList<>(map.keySet());
		    
		    for (int i = 2; i < keys.size() + 2; i++) {
		    	if (map.get(keys.get(i - 2)).size() > 0) {
		    		tuttiIBottoni.get(i).putClientProperty("selezionabile", true);
		    	}
		    }
		    
		    
	    } else { // caso non vuota
	    	
	    	if (tesseraSelezionata != null && bottoneGrigliaSelezionato == -1) {
	    		tuttiIBottoni.get(indiceBottone).setBorder(null);
	    		tessereNascosteBtn.setBorder(new LineBorder(Color.RED, 3));
	    		return;
	    	}
	    	
	    	tesseraSelezionata = giocatore.prendiTesseraPrenotata(indiceSlot);
	    	bottonePrenotato = indiceBottone;
	    	piazzamentoScartabile = false;
	    	aggiornaPulsanteScarta(false);
	    	
	    }    

	}

	/**
	 * Gestisce la logica dell'interazione tramite click sinistro su una cella della nave del giocatore.
	 * <p>
	 * Il metodo si occupa di piazzare una tessera selezionata o di rimuovere una tessera già piazzata, 
	 * a seconda dello stato attuale della costruzione e delle regole imposte (es. una sola mossa per turno).
	 * L'interfaccia utente viene modificata per riflettere le modifiche.
	 * 
	 * @param riga la riga della cella cliccata.
	 * @param colonna la colonna della cella cliccata.
	 */
	private void logicaClickSinistroSuCella(int riga, int colonna) {
		
	    Cella cella = giocatore.getNave().getGriglia().getCella(riga, colonna);
	    Tessera tesseraPresente = cella.getTessera();
	    
	    if (tesseraSelezionata == null && bottoneGrigliaSelezionato != -1 && !mossaFatta) {

		    Map<Class<? extends Tessera>, Deque<Tessera>> map = mucchio.getCopiaTessereVisibili();               		
	    	List<Class<? extends Tessera>> keys = new ArrayList<>(map.keySet());
	    	
		    tesseraSelezionata = giocatore.pescaTesseraVisibile(mucchio, keys.get(bottoneGrigliaSelezionato - 2));
		    selezionePermanente = false;

	    }

	    if (tesseraSelezionata == null && tesseraPresente == null && !mossaFatta) {
	        mostraErrore("Devi prima selezionare una tessera!");
	        return;
	    }
	    
	    if (tesseraSelezionata == null && tesseraPresente == null && mossaFatta) {
	        mostraErrore("Puoi piazzare solo una tessera per turno, rimuovila se vuoi piazzarne un'altra!");
	        return;
	    }
	    
	    
	    if (coordinateTesseraPiazzata != null && !coordinateTesseraPiazzata.confrontaCoordinate(new Coordinate(riga, colonna))) {
	    	mostraErrore("Questa tessera è già stata saldata, non puoi più rimuoverla!");
	    	return;
	    }
	    
	    if (coordinateTesseraPiazzata == null && tesseraPresente != null) {
	    	mostraErrore("Questa tessera è già stata saldata, non puoi più rimuoverla!");
	    	return;
	    }

	    if (tesseraPresente != null && !coordinateTesseraPiazzata.confrontaCoordinate(new Coordinate(riga, colonna))) {
	        mostraErrore("Questa cella è già occupata!");
	        return;
	    }

	    if (tesseraPresente != null) {

	        tesseraSelezionata = giocatore.rimuoviTessera(cella);
	        mossaFatta = false;
	        IconHandler.clearIconsFromLabel(pannelloNave.getLabelCella(riga, colonna));
	        coordinateTesseraPiazzata = null;
	        abilitaBottoni(tesseraSelezionata);
	        
	        if (!piazzamentoScartabile) {
	        	
	        	if (giocatore.vediTesserePrenotate().get(0) == null) { // primo slot vuoto
	        		giocatore.prenotaTessera(tesseraSelezionata, 0);
	        		IconHandler.setIconOnButton(tuttiIBottoni.get(12), tesseraSelezionata.getImmagine(), tesseraSelezionata.getImmagineHovered());
	        		IconHandler.setIconOnButton(tuttiIBottoni.get(0), tessereNascosteIcon, hovTessereNascosteIcon);
	        		
	        	} else { // secondo slot vuoto        		
	        		giocatore.prenotaTessera(tesseraSelezionata, 1);
	        		IconHandler.setIconOnButton(tuttiIBottoni.get(13), tesseraSelezionata.getImmagine(), tesseraSelezionata.getImmagineHovered());
	        		IconHandler.setIconOnButton(tuttiIBottoni.get(0), tessereNascosteIcon, hovTessereNascosteIcon);
	        	}
	        	
	        	tesseraSelezionata = null;
	        	return;
	        }
	        
	        if (selezionePermanente) {
	        	tessereNascosteBtn.setBorderPainted(true);
	        	tessereNascosteBtn.setBorder(new LineBorder(Color.RED, 3));
	        	aggiornaPulsanteScarta(true);
	        	
	        } else {

	        	IconHandler.setIconOnButton(tessereNascosteBtn, tessereNascosteIcon, hovTessereNascosteIcon);
	        	giocatore.scartaTessera(mucchio, tesseraSelezionata);
	        	tesseraSelezionata = null;
	        	bottoneGrigliaSelezionato = -1;
	        	aggiornaGrigliaTessereVisibili(true);
	        }
	        
	        return;
	    }

	    // caso tessere non presente
	    
	    giocatore.piazzaTessera(cella, tesseraSelezionata);
	    IconHandler.setIconOnLabel(pannelloNave.getLabelCella(riga, colonna), tesseraSelezionata.getImmagine(), tesseraSelezionata.getImmagineHovered()); 
	    pannelloNave.getLabelCella(riga, colonna).repaint();    
	    disabilitaBottoni(true);
	    aggiornaGrigliaTessereVisibili(true);
	    
	    if (bottonePrenotato != -1) {
	    	IconHandler.setIconOnButton(tuttiIBottoni.get(bottonePrenotato), pilaVuotaTessereVisibiliIcon, hovPilaVuotaTessereVisibiliIcon);
	    	bottonePrenotato = -1;
	    }

	    if (!selezionePermanente) {
	        aggiornaGrigliaTessereVisibili(false);
	    }

	    tesseraSelezionata = null;
	    mossaFatta = true;
	    coordinateTesseraPiazzata = new Coordinate(riga, colonna);
	}

	/**
	 * Gestisce la logica dell'interazione tramite click destro su una cella della nave del giocatore.
	 * <p>
	 * Permette la rotazione di una tessera presente nella cella selezionata, solo se tale tessera
	 * è quella piazzata nel turno corrente e non ancora saldata. In caso contrario,
	 * viene mostrato un messaggio di errore.
	 * 
	 * @param riga la riga della cella cliccata.
	 * @param colonna la colonna della cella cliccata.
	 */
	private void logicaClickDestroSuCella(int riga, int colonna) {
		Tessera t = giocatore.getNave().getGriglia().getCella(riga, colonna).getTessera();
		
		if (t == null) {
			mostraErrore("Non puoi ruotare una cella senza tessera!");
			return;
		}
		
		if (coordinateTesseraPiazzata == null || !coordinateTesseraPiazzata.confrontaCoordinate(new Coordinate(riga, colonna))) {
			mostraErrore("Questa tessera è già stata saldata, non puoi più ruotarla!");
			return;
		}
		
		giocatore.ruotaTessera(t);
		IconHandler.setIconOnLabel(pannelloNave.getLabelCella(riga, colonna), t.getImmagine(), t.getImmagineHovered());
		pannelloNave.getLabelCella(riga, colonna).repaint(); 
	}
	
	/**
	 * Gestisce la chiusura del turno del giocatore.
	 * <p>
	 * Ferma il timer se attivo, gestisce eventuali tessere selezionate
	 * (prenotandole o scartandole se necessario), assegna eventuali debiti
	 * per tessere prenotate non utilizzate e chiude la finestra corrente.
	 */
	private void fineTurno() {
		
		if (timer != null && timer.isRunning()) {
            timer.stop();
        }
		
		if (tesseraSelezionata != null && bottonePrenotato == -1) giocatore.scartaTessera(mucchio, tesseraSelezionata); // evito la perdita di tessere
		if (tesseraSelezionata != null && bottonePrenotato != -1) {
			giocatore.prenotaTessera(tesseraSelezionata, bottonePrenotato - 12);
		}
		
		if (assemblaggioCompletatoCb.isSelected()) {
			giocatore.setAssemblaggioCompletato(true);
			
			// aggiunta debiti in caso di tessere prenotate non piazzate
			if (giocatore.prendiTesseraPrenotata(0) != null) giocatore.aggiungiDebiti(1);
			if (giocatore.prendiTesseraPrenotata(1) != null) giocatore.aggiungiDebiti(1);
		}
		
		latch.countDown();
        getFrame().dispose();
	}
	
	/**
	 * Imposta la dimensione della finestra principale in base alle dimensioni dello schermo. 
	 * <p>
	 * La finestra viene ridimensionata per occupare al massimo l'80% dello schermo,
	 * mantenendo un rapporto larghezza/altezza di 10:7. La finestra viene anche centrata
	 * sullo schermo.
	 */
	private void impostaDimensioniFinestra() {
	    
	    Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
	    // dimensioni massime della finestra (80% dello schermo)
	    int maxW = (int) (screenSize.getWidth() * 0.8);
	    int maxH = (int) (screenSize.getHeight() * 0.8);

	    int width = maxW;
	    int height = width * 7 / 10;

	    // se l'altezza supera il limite massimo, ridimensiona in base all'altezza massima
	    if (height > maxH) {
	        height = maxH;
	        width = height * 10 / 7;
	    }

	    getFrame().setSize(width, height);
	    getFrame().setLocation((screenSize.width - getFrame().getWidth()) / 2, (screenSize.height - getFrame().getHeight()) / 2);
	}
	
	/**
	 * Inizializza e avvia il timer per il turno del giocatore.
	 * <p>
	 * Se il livello è {@code Livello.P} il timer viene disattivato.
	 * Altrimenti, viene aggiornato ogni secondo e modificato
	 * il colore del testo in base al tempo rimanente. Allo scadere,
	 * il turno viene automaticamente chiuso.
	 */
	private void impostaTimer() {
		
		timerLbl = new JLabel();
		timerLbl.setFont(timerLbl.getFont().deriveFont(15f).deriveFont(Font.BOLD));
	    timerLbl.setForeground(Color.WHITE);
		
		if (livello == Livello.P) {
	        timerLbl.setText("Timer: OFF");
	        timerLbl.setToolTipText("Il timer è disabilitato nella modalità di prova!");
	        return;
	    }

		timer = new Timer(1000, e -> {
	        tempoRimanente--;
	        timerLbl.setText("Timer: " + formattaTimer(tempoRimanente));
	        
	        if (tempoRimanente >= 10 && tempoRimanente < 20) {
	        	timerLbl.setForeground(Color.ORANGE);
	        }
	        
	        if (tempoRimanente < 10) {
	        	timerLbl.setForeground(Color.RED);
	        }

	        if (tempoRimanente <= 0) {
	            fineTurno();
	        }
	    });
		
	    timer.start();
	}
	
	/**
	 * Restituisce una stringa formattata del tempo rimanente nel formato MM:SS.
	 * 
	 * @param totSecondi il tempo rimanente in secondi.
	 * @return una stringa nel formato "MM:SS".
	 */
	private String formattaTimer(int totSecondi) {
	    int minuti = totSecondi / 60;
	    int secondi = totSecondi % 60;
	    return String.format("%02d:%02d", minuti, secondi);
	}
	
	/**
	 * Adatta dinamicamente la dimensione del font per il testo contenuto in un'etichetta,
	 * in modo che si adatti allo spazio disponibile.
	 * <p>
	 * La dimensione del font viene ridotta fino a quando il testo rientra nei margini
	 * orizzontali e verticali della label. Supporta testo con markup HTML.
	 * 
	 * @param label l'etichetta il cui font va adattato.
	 * @param maxFontSize la dimensione massima del font.
	 */
	private void adattaFont(JLabel label, int maxFontSize) {
	    Font baseFont = label.getFont();
	    String text = label.getText();

	    if (text == null || text.isEmpty()) return;

	    int labelWidth = label.getWidth();
	    int labelHeight = label.getHeight();
	    if (labelWidth <= 0 || labelHeight <= 0) return;

	    Graphics g = label.getGraphics();
	    if (g == null) return;

	    int fontSize = maxFontSize;
	    FontMetrics metrics;

	    // pulisci testo da HTML per il calcolo grezzo
	    String plainText = text.replaceAll("<[^>]*>", "");

	    while (fontSize > 4) {
	        Font testFont = baseFont.deriveFont((float) fontSize);
	        metrics = g.getFontMetrics(testFont);
	        int textWidth = metrics.stringWidth(plainText);
	        int textHeight = metrics.getHeight();

	        int marginX = 35; // margine orizzontale
	        
	        if (giocatore.getColore() == Colore.VERDE) marginX = 50;
	        

	        if (textWidth <= labelWidth - marginX && textHeight <= labelHeight) {
	            break;
	        }

	        fontSize--;
	    }

	    label.setFont(baseFont.deriveFont((float) fontSize));
	}
	
	/**
	 * Mostra un messaggio di errore in un popup.
	 * 
	 * @param messaggio il messaggio di errore da visualizzare.
	 */
	private void mostraErrore(String messaggio) {
	    JOptionPane.showMessageDialog(getFrame(), messaggio, "Errore!", JOptionPane.ERROR_MESSAGE);
	}

}