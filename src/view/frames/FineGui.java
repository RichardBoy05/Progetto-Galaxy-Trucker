package galaxytrucker.src.view.frames;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import java.util.List;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import galaxytrucker.src.logic.gioco.Colore;
import galaxytrucker.src.logic.gioco.Giocatore;
import galaxytrucker.src.view.base.GuiBase;
import galaxytrucker.src.view.dialogs.PlayerStatsDialog;

/**
 * Classe che rappresenta l'interfaccia grafica mostrata alla fine di una partita.
 * Visualizza i punteggi finali dei giocatori, i loro piazzamenti e consente di
 * accedere alle statistiche individuali o iniziare una nuova partita.
 * Estende la classe {@link GuiBase}.
 *
 * @see GuiBase
 */
public class FineGui extends GuiBase {
	
	// costanti
	
	private static final int WIDTH = 600;
	private static final int HEIGHT = 628;
	
	private static final String DIR_PATH = "/galaxytrucker/resources/images/fine/";
	private static final String BACKGROUND_IMG_PATH = DIR_PATH + "fine_background.png";
	private static final String PLAYAGAIN_ICON_PATH = DIR_PATH + "playagain.png";
	private static final String HOV_PLAYAGAIN_ICON_PATH = DIR_PATH + "hov_playagain.png";
	private static final String RED_ICON_PATH = DIR_PATH + "red.png";
	private static final String NOT_RED_ICON_PATH = DIR_PATH + "not_red.png";
	private static final String HOV_RED_ICON_PATH = DIR_PATH + "hov_red.png";
	private static final String BLUE_ICON_PATH = DIR_PATH + "blue.png";
	private static final String NOT_BLUE_ICON_PATH = DIR_PATH + "not_blue.png";
	private static final String HOV_BLUE_ICON_PATH = DIR_PATH + "hov_blue.png";
	private static final String YELLOW_ICON_PATH = DIR_PATH + "yellow.png";
	private static final String NOT_YELLOW_ICON_PATH = DIR_PATH + "not_yellow.png";
	private static final String HOV_YELLOW_ICON_PATH = DIR_PATH + "hov_yellow.png";
	private static final String GREEN_ICON_PATH = DIR_PATH + "green.png";
	private static final String NOT_GREEN_ICON_PATH = DIR_PATH + "not_green.png";
	private static final String HOV_GREEN_ICON_PATH = DIR_PATH + "hov_green.png";
	
	// componenti e altri attributi
	
	private List<Giocatore> giocatori;
	
	private JLabel background;
	private JLabel redPoints;
	private JLabel bluePoints;
	private JLabel yellowPoints;
	private JLabel greenPoints;
	
	private JButton playAgainBtn;
	private JButton red;
	private JButton blue;
	private JButton yellow;
	private JButton green;	
	
	private ImageIcon backgroundIcon;
	private ImageIcon playAgainIcon;
	private ImageIcon hovPlayAgainIcon;
	private ImageIcon redIcon;
	private ImageIcon notRedIcon;
	private ImageIcon hovRedIcon;
	private ImageIcon blueIcon;
	private ImageIcon notBlueIcon;
	private ImageIcon hovBlueIcon;
	private ImageIcon yellowIcon;
	private ImageIcon notYellowIcon;
	private ImageIcon hovYellowIcon;
	private ImageIcon greenIcon;
	private ImageIcon notGreenIcon;
	private ImageIcon hovGreenIcon;
	
	private Font pointsFont;
	
	private Color oro;
	private Color argento;
	private Color bronzo;
	private Color bianco;
	private Color grigio;

	/**
     * Costruttore della GUI di fine partita con la lista dei giocatori da visualizzare.
     *
     * @param giocatori lista dei giocatori che hanno partecipato alla partita
     */
	public FineGui(List<Giocatore> giocatori) {
		
		super();
		this.giocatori = giocatori;		
		getFrame().setLayout(new BorderLayout());
        getFrame().setSize(WIDTH, HEIGHT);
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		getFrame().setLocation((screenSize.width / 2) - (getFrame().getWidth() / 2), (screenSize.height / 2) - (getFrame().getHeight() / 2));
		
		setupComponents();
		setupLayout();
		setupListeners();
		getFrame().revalidate();

	}

	/**
	 * Inizializza e configura i componenti grafici della
	 * finestra, inclusi bottoni, label, font, colori e icone.
	 */
	@Override
	public void setupComponents() {
		
		backgroundIcon = new ImageIcon(getClass().getResource(BACKGROUND_IMG_PATH));
		playAgainIcon = new ImageIcon(getClass().getResource(PLAYAGAIN_ICON_PATH));
		hovPlayAgainIcon = new ImageIcon(getClass().getResource(HOV_PLAYAGAIN_ICON_PATH));
		redIcon = new ImageIcon(getClass().getResource(RED_ICON_PATH));
		notRedIcon = new ImageIcon(getClass().getResource(NOT_RED_ICON_PATH));
		hovRedIcon = new ImageIcon(getClass().getResource(HOV_RED_ICON_PATH));
		blueIcon = new ImageIcon(getClass().getResource(BLUE_ICON_PATH));
		notBlueIcon = new ImageIcon(getClass().getResource(NOT_BLUE_ICON_PATH));
		hovBlueIcon = new ImageIcon(getClass().getResource(HOV_BLUE_ICON_PATH));
		yellowIcon = new ImageIcon(getClass().getResource(YELLOW_ICON_PATH));
		notYellowIcon = new ImageIcon(getClass().getResource(NOT_YELLOW_ICON_PATH));
		hovYellowIcon = new ImageIcon(getClass().getResource(HOV_YELLOW_ICON_PATH));
		greenIcon = new ImageIcon(getClass().getResource(GREEN_ICON_PATH));
		notGreenIcon = new ImageIcon(getClass().getResource(NOT_GREEN_ICON_PATH));
		hovGreenIcon = new ImageIcon(getClass().getResource(HOV_GREEN_ICON_PATH));
		
		oro = new Color(212, 175, 55);
		argento = new Color(192, 192, 192);
		bronzo = new Color(205, 127, 50); 
		grigio = new Color(130, 130, 130);
		bianco = Color.WHITE;
		
		background = new JLabel(backgroundIcon);
		redPoints = new JLabel();
		bluePoints = new JLabel();
		yellowPoints = new JLabel();
		greenPoints = new JLabel();
		
		pointsFont = redPoints.getFont().deriveFont(Font.BOLD, 50f);
		redPoints.setFont(pointsFont);
		bluePoints.setFont(pointsFont);
		yellowPoints.setFont(pointsFont);
		greenPoints.setFont(pointsFont);
		redPoints.setHorizontalAlignment(SwingConstants.CENTER);
		bluePoints.setHorizontalAlignment(SwingConstants.CENTER);
		yellowPoints.setHorizontalAlignment(SwingConstants.CENTER);
		greenPoints.setHorizontalAlignment(SwingConstants.CENTER);
		
		playAgainBtn = new JButton();
		red = new JButton();
		blue = new JButton();
		yellow = new JButton();
		green = new JButton();	
			
		impostaValoriAstronauti();
		
		playAgainBtn.setIcon(playAgainIcon);
		playAgainBtn.setRolloverIcon(hovPlayAgainIcon);
		
		impostaLookDefaultBottone(playAgainBtn);
		impostaLookDefaultBottone(red);
		impostaLookDefaultBottone(blue);
		impostaLookDefaultBottone(yellow);
		impostaLookDefaultBottone(green);

	}

	/**
	 * Posiziona i componenti sulla finestra utilizzando il layout assoluto.
	 */
	@Override
	public void setupLayout() {
		
		playAgainBtn.setBounds(192, 495, 215, 55);
		red.setBounds(51, 190, 95, 184);
		blue.setBounds(181, 190, 100, 182);
		yellow.setBounds(301, 190, 101, 182);
		green.setBounds(441, 190, 108, 180);
		redPoints.setBounds(51, 370, 100, 100);
		bluePoints.setBounds(174, 370, 100, 100);
		yellowPoints.setBounds(306, 370, 100, 100);
		greenPoints.setBounds(431, 370, 100, 100);
		
		background.setLayout(null);
		background.add(playAgainBtn);
		background.add(red);
		background.add(blue);
		background.add(yellow);
		background.add(green);
		background.add(redPoints);
		background.add(bluePoints);
		background.add(yellowPoints);
		background.add(greenPoints);
		
		getFrame().add(background);
		
	}

	/**
     * Registra i listener per la gestione degli eventi dei pulsanti.
     * <ul>
     *   <li>Il pulsante "play again" avvia una nuova schermata di introduzione.</li>
     *   <li>I pulsanti dei giocatori aprono le relative statistiche.</li>
     * </ul>
     */
	@Override
	public void setupListeners() {
		
		playAgainBtn.addActionListener(e-> {			
			getFrame().dispose();
			new IntroGui();
			
		});
		
		red.addActionListener(e-> {
			apriStatistiche(Colore.ROSSO);					
		});
		
		blue.addActionListener(e-> {			
			apriStatistiche(Colore.BLU);					
		});
		
		yellow.addActionListener(e-> {			
			apriStatistiche(Colore.GIALLO);					
		});
		
		green.addActionListener(e-> {			
			apriStatistiche(Colore.VERDE);					
		});
		
	}
	
	/**
     * Imposta le icone, i testi e i colori dei punteggi per ciascun giocatore, in base alla loro partecipazione
     * e al piazzamento finale. I giocatori non partecipanti sono mostrati con icone grigie e punteggi disabilitati.
     */
	private void impostaValoriAstronauti() {
		
		// caso giocatori non partecipanti
		
		red.setIcon(notRedIcon);
		blue.setIcon(notBlueIcon);
		yellow.setIcon(notYellowIcon);
		green.setIcon(notGreenIcon);
		
		red.setToolTipText("Non partecipante!");
		blue.setToolTipText("Non partecipante!");
		yellow.setToolTipText("Non partecipante!");
		green.setToolTipText("Non partecipante!");
		
		redPoints.setText("/");
		bluePoints.setText("/");
		yellowPoints.setText("/");
		greenPoints.setText("/");
		
		redPoints.setForeground(grigio);
		bluePoints.setForeground(grigio);
		yellowPoints.setForeground(grigio);
		greenPoints.setForeground(grigio);
		
		// caso giocatori partecipanti
		
		for (Giocatore g : giocatori) {
			switch (g.getColore()) {
			
			case ROSSO:
				red.setIcon(redIcon);
				red.setRolloverIcon(hovRedIcon);
				red.setToolTipText("Il giocatore ROSSO si è classificato al " + g.getRankFinale() + "° posto!");
				redPoints.setText(String.valueOf(g.getPunteggioFinale()));
				impostaLookPunteggio(redPoints, g);
				break;
			case BLU:
				blue.setIcon(blueIcon);
				blue.setRolloverIcon(hovBlueIcon);
				blue.setToolTipText("Il giocatore BLU si è classificato al " + g.getRankFinale() + "° posto!");
				bluePoints.setText(String.valueOf(g.getPunteggioFinale()));
				impostaLookPunteggio(bluePoints, g);
				break;
			case GIALLO:
				yellow.setIcon(yellowIcon);
				yellow.setRolloverIcon(hovYellowIcon);
				yellow.setToolTipText("Il giocatore GIALLO si è classificato al " + g.getRankFinale() + "° posto!");
				yellowPoints.setText(String.valueOf(g.getPunteggioFinale()));
				impostaLookPunteggio(yellowPoints, g);
				break;
			case VERDE:
				green.setIcon(greenIcon);
				green.setRolloverIcon(hovGreenIcon);
				green.setToolTipText("Il giocatore VERDE si è classificato al " + g.getRankFinale() + "° posto!");
				greenPoints.setText(String.valueOf(g.getPunteggioFinale()));
				impostaLookPunteggio(greenPoints, g);
				break;
			default:
				break;
			
			}
			
		}
		
	}
	
	/**
     * Apre una finestra di dialogo contenente le statistiche
     * dettagliate del giocatore associato al colore specificato.
     *
     * @param colore il colore del giocatore di cui mostrare le statistiche
     */
	private void apriStatistiche(Colore colore) {
		
		for (Giocatore g : giocatori) {
			if (g.getColore() == colore) {
				new PlayerStatsDialog(getFrame(), g);
				break;
			}
		}
	}
	
	/**
     * Imposta il colore del punteggio mostrato accanto al giocatore, in base al suo piazzamento finale:
     * oro per il primo posto, argento per il secondo, bronzo per il terzo, bianco per gli altri.
     *
     * @param label etichetta su cui impostare il colore del punteggio
     * @param g il giocatore associato al punteggio
     */
	private void impostaLookPunteggio(JLabel label, Giocatore g) {	
		
		switch (g.getRankFinale()) {
		
			case 1:
				label.setForeground(oro);
				break;
			
			case 2:
				label.setForeground(argento);
				break;
				
			case 3:
				label.setForeground(bronzo);
				break;
				
			default:
				label.setForeground(bianco);
				break;
		
		}
		
	}		
	
}