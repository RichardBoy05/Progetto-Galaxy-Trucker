package galaxytrucker.src.view.frames;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import galaxytrucker.src.logic.assemblaggio.Nave;
import galaxytrucker.src.logic.gioco.Colore;
import galaxytrucker.src.logic.gioco.GameLogger;
import galaxytrucker.src.logic.gioco.Giocatore;
import galaxytrucker.src.logic.gioco.Livello;
import galaxytrucker.src.logic.gioco.Partita;
import galaxytrucker.src.view.base.GuiBase;
import galaxytrucker.src.view.components.PlaceholderTextField;

/**
 * Classe che rappresenta l'interfaccia grafica per la schermata di setup 
 * iniziale del gioco. Permette di configurare i giocatori, selezionare
 * il livello di difficoltà e avviare una nuova partita.
 *
 * <p>Gli utenti possono:
 * <ul>
 *   <li>Abilitare/disabilitare giocatori con checkbox personalizzate.</li>
 *   <li>Inserire i nomi dei giocatori tramite campi di testo.</li>
 *   <li>Selezionare il livello di difficoltà da un menu a tendina.</li>
 *   <li>Avviare la partita o tornare alla schermata introduttiva.</li>
 *   <li>Abilitare o disabilitare il logger di gioco.</li>
 * </ul>
 *
 * @see GuiBase
 */
public class SetupGui extends GuiBase {
	
	// costanti
	
	private static final int WIDTH = 430;
	private static final int HEIGHT = 677;
	
	private static final String DIR_PATH = "/galaxytrucker/resources/images/setup/";
	private static final String BACKGROUND_IMG_PATH = DIR_PATH + "setup_background.png";
	private static final String START_ICON_PATH = DIR_PATH + "start.png";
	private static final String BACK_ICON_PATH = DIR_PATH + "back.png";
	private static final String HOV_START_ICON_PATH = DIR_PATH + "start_hover.png";
	private static final String HOV_BACK_ICON_PATH = DIR_PATH + "back_hover.png";
	private static final String RED_ICON_PATH = DIR_PATH + "red.png";
	private static final String NOT_RED_ICON_PATH = DIR_PATH + "not_red.png";
	private static final String BLUE_ICON_PATH = DIR_PATH + "blue.png";
	private static final String NOT_BLUE_ICON_PATH = DIR_PATH + "not_blue.png";
	private static final String YELLOW_ICON_PATH = DIR_PATH + "yellow.png";
	private static final String NOT_YELLOW_ICON_PATH = DIR_PATH + "not_yellow.png";
	private static final String GREEN_ICON_PATH = DIR_PATH + "green.png";
	private static final String NOT_GREEN_ICON_PATH = DIR_PATH + "not_green.png";
	private static final String RED_TICK_PATH = DIR_PATH + "red_tick.png";
	private static final String RED_UNTICK_PATH = DIR_PATH + "red_untick.png";
	private static final String RED_TICK_HOVER_PATH = DIR_PATH + "red_tick_hover.png";
	private static final String RED_UNTICK_HOVER_PATH = DIR_PATH + "red_untick_hover.png";
	private static final String BLUE_TICK_PATH = DIR_PATH + "blue_tick.png";
	private static final String BLUE_UNTICK_PATH = DIR_PATH + "blue_untick.png";
	private static final String BLUE_TICK_HOVER_PATH = DIR_PATH + "blue_tick_hover.png";
	private static final String BLUE_UNTICK_HOVER_PATH = DIR_PATH + "blue_untick_hover.png";
	private static final String YELLOW_TICK_PATH = DIR_PATH + "yellow_tick.png";
	private static final String YELLOW_UNTICK_PATH = DIR_PATH + "yellow_untick.png";
	private static final String YELLOW_TICK_HOVER_PATH = DIR_PATH + "yellow_tick_hover.png";
	private static final String YELLOW_UNTICK_HOVER_PATH = DIR_PATH + "yellow_untick_hover.png";
	private static final String GREEN_TICK_PATH = DIR_PATH + "green_tick.png";
	private static final String GREEN_UNTICK_PATH = DIR_PATH + "green_untick.png";
	private static final String GREEN_TICK_HOVER_PATH = DIR_PATH + "green_tick_hover.png";
	private static final String GREEN_UNTICK_HOVER_PATH = DIR_PATH + "green_untick_hover.png";
	
	private static final String PLACEHOLDER_TEXT = "Inserisci giocatore...";
	private static final int PLAYER_NAME_MAX_LEN = 20;
	private static final int PLAYER_NAME_MIN_LEN = 3;
	
	private static final GameLogger LOGGER = GameLogger.getInstance();
	
	// componenti
	
	private JLabel background;
	private JLabel red;
	private JLabel blue;
	private JLabel yellow;
	private JLabel green;
	
	private JButton startBtn;
	private JButton backBtn;
	
	private PlaceholderTextField giocatoreRossoTf;
	private PlaceholderTextField giocatoreBluTf;
	private PlaceholderTextField giocatoreGialloTf;
	private PlaceholderTextField giocatoreVerdeTf;
	
	private JCheckBox giocatoreRossoCb;
	private JCheckBox giocatoreBluCb;
	private JCheckBox giocatoreGialloCb;
	private JCheckBox giocatoreVerdeCb;
	private JCheckBox loggerCb;
	
	private JComboBox<Livello> selezionaLivello;
	
	private ImageIcon backgroundIcon;
	private ImageIcon startIcon;
	private ImageIcon hovStartIcon;
	private ImageIcon backIcon;
	private ImageIcon hovBackIcon;
	private ImageIcon redIcon;
	private ImageIcon notRedIcon;
	private ImageIcon blueIcon;
	private ImageIcon notBlueIcon;
	private ImageIcon yellowIcon;
	private ImageIcon notYellowIcon;
	private ImageIcon greenIcon;
	private ImageIcon notGreenIcon;
	private ImageIcon redTickIcon;
	private ImageIcon redUntickIcon;
	private ImageIcon redTickHoverIcon;
	private ImageIcon redUntickHoverIcon;
	private ImageIcon blueTickIcon;
	private ImageIcon blueUntickIcon;
	private ImageIcon blueTickHoverIcon;
	private ImageIcon blueUntickHoverIcon;
	private ImageIcon yellowTickIcon;
	private ImageIcon yellowUntickIcon;
	private ImageIcon yellowTickHoverIcon;
	private ImageIcon yellowUntickHoverIcon;
	private ImageIcon greenTickIcon;
	private ImageIcon greenUntickIcon;
	private ImageIcon greenTickHoverIcon;
	private ImageIcon greenUntickHoverIcon;
		
	
	/**
	 * Costruttore della GUI di setup.
	 * Inizializza la finestra, posiziona i componenti, registra i listener.
	 */
	public SetupGui() {
		
		super();
		getFrame().setLayout(new BorderLayout());
        getFrame().setSize(WIDTH, HEIGHT);
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        getFrame().setLocation((screenSize.width - getFrame().getWidth()) / 2, (screenSize.height - getFrame().getHeight()) / 2);		
		setupComponents();
		setupLayout();
		setupListeners();
		getFrame().revalidate();

	}
	
	/**
	 * Inizializza e configura i componenti grafici della
	 * finestra, inclusi bottoni, checkbox, label e icone.
	 */
	@Override
	public void setupComponents() {
		
		startBtn = new JButton();
		backBtn = new JButton();
		giocatoreRossoTf = new PlaceholderTextField(PLACEHOLDER_TEXT);
		giocatoreBluTf = new PlaceholderTextField(PLACEHOLDER_TEXT);
		giocatoreGialloTf = new PlaceholderTextField(PLACEHOLDER_TEXT);
		giocatoreVerdeTf = new PlaceholderTextField(PLACEHOLDER_TEXT);
		giocatoreRossoCb = new JCheckBox();
		giocatoreBluCb = new JCheckBox();
		giocatoreGialloCb = new JCheckBox();
		giocatoreVerdeCb = new JCheckBox();
		loggerCb = new JCheckBox("Logger abilitato");
		selezionaLivello = new JComboBox<>(Livello.values());
		
		backgroundIcon = new ImageIcon(getClass().getResource(BACKGROUND_IMG_PATH));
		redIcon = new ImageIcon(getClass().getResource(RED_ICON_PATH));
		notRedIcon = new ImageIcon(getClass().getResource(NOT_RED_ICON_PATH));
		blueIcon = new ImageIcon(getClass().getResource(BLUE_ICON_PATH));
		notBlueIcon = new ImageIcon(getClass().getResource(NOT_BLUE_ICON_PATH));
		yellowIcon = new ImageIcon(getClass().getResource(YELLOW_ICON_PATH));
		notYellowIcon = new ImageIcon(getClass().getResource(NOT_YELLOW_ICON_PATH));
		greenIcon = new ImageIcon(getClass().getResource(GREEN_ICON_PATH));
		notGreenIcon = new ImageIcon(getClass().getResource(NOT_GREEN_ICON_PATH));
		backIcon = new ImageIcon(getClass().getResource(BACK_ICON_PATH));
		hovBackIcon = new ImageIcon(getClass().getResource(HOV_BACK_ICON_PATH));
		startIcon = new ImageIcon(getClass().getResource(START_ICON_PATH));
		hovStartIcon = new ImageIcon(getClass().getResource(HOV_START_ICON_PATH));
		redTickIcon = new ImageIcon(getClass().getResource(RED_TICK_PATH));
		redUntickIcon = new ImageIcon(getClass().getResource(RED_UNTICK_PATH));
		redTickHoverIcon = new ImageIcon(getClass().getResource(RED_TICK_HOVER_PATH));
		redUntickHoverIcon = new ImageIcon(getClass().getResource(RED_UNTICK_HOVER_PATH));
		blueTickIcon = new ImageIcon(getClass().getResource(BLUE_TICK_PATH));
		blueUntickIcon = new ImageIcon(getClass().getResource(BLUE_UNTICK_PATH));
		blueTickHoverIcon = new ImageIcon(getClass().getResource(BLUE_TICK_HOVER_PATH));
		blueUntickHoverIcon = new ImageIcon(getClass().getResource(BLUE_UNTICK_HOVER_PATH));
		yellowTickIcon = new ImageIcon(getClass().getResource(YELLOW_TICK_PATH));
		yellowUntickIcon = new ImageIcon(getClass().getResource(YELLOW_UNTICK_PATH));
		yellowTickHoverIcon = new ImageIcon(getClass().getResource(YELLOW_TICK_HOVER_PATH));
		yellowUntickHoverIcon = new ImageIcon(getClass().getResource(YELLOW_UNTICK_HOVER_PATH));
		greenTickIcon = new ImageIcon(getClass().getResource(GREEN_TICK_PATH));
		greenUntickIcon = new ImageIcon(getClass().getResource(GREEN_UNTICK_PATH));
		greenTickHoverIcon = new ImageIcon(getClass().getResource(GREEN_TICK_HOVER_PATH));
		greenUntickHoverIcon = new ImageIcon(getClass().getResource(GREEN_UNTICK_HOVER_PATH));
		
		background = new JLabel(backgroundIcon);
		red = new JLabel(redIcon);
		blue = new JLabel(blueIcon);
		yellow = new JLabel(yellowIcon);
		green = new JLabel(greenIcon);
		
		selezionaLivello.setFocusable(false);
		selezionaLivello.setFont(selezionaLivello.getFont().deriveFont(18f));
		selezionaLivello.setSelectedItem(Livello.I);
		
		giocatoreRossoTf.setFont(giocatoreRossoTf.getFont().deriveFont(20f));
		giocatoreBluTf.setFont(giocatoreBluTf.getFont().deriveFont(20f));
		giocatoreGialloTf.setFont(giocatoreGialloTf.getFont().deriveFont(20f));
		giocatoreVerdeTf.setFont(giocatoreVerdeTf.getFont().deriveFont(20f));
		
		giocatoreRossoCb.setOpaque(false);
		giocatoreRossoCb.setContentAreaFilled(false);
		giocatoreRossoCb.setBorderPainted(false);
		giocatoreBluCb.setOpaque(false);
		giocatoreBluCb.setContentAreaFilled(false);
		giocatoreBluCb.setBorderPainted(false);	
		giocatoreGialloCb.setOpaque(false);
		giocatoreGialloCb.setContentAreaFilled(false);
		giocatoreGialloCb.setBorderPainted(false);	
		giocatoreVerdeCb.setOpaque(false);
		giocatoreVerdeCb.setContentAreaFilled(false);
		giocatoreVerdeCb.setBorderPainted(false);
		loggerCb.setOpaque(false);
		loggerCb.setContentAreaFilled(false);
		loggerCb.setBorderPainted(false);
		loggerCb.setFocusable(false);	
		loggerCb.setForeground(Color.WHITE);
		loggerCb.setFont(loggerCb.getFont().deriveFont(14f));
		
		giocatoreRossoCb.setIcon(redUntickIcon);
		giocatoreRossoCb.setSelectedIcon(redTickIcon);
		giocatoreRossoCb.setRolloverIcon(redUntickHoverIcon);
		giocatoreRossoCb.setRolloverSelectedIcon(redTickHoverIcon);
		giocatoreBluCb.setIcon(blueUntickIcon);
		giocatoreBluCb.setSelectedIcon(blueTickIcon);
		giocatoreBluCb.setRolloverIcon(blueUntickHoverIcon);
		giocatoreBluCb.setRolloverSelectedIcon(blueTickHoverIcon);
		giocatoreGialloCb.setIcon(yellowUntickIcon);
		giocatoreGialloCb.setSelectedIcon(yellowTickIcon);
		giocatoreGialloCb.setRolloverIcon(yellowUntickHoverIcon);
		giocatoreGialloCb.setRolloverSelectedIcon(yellowTickHoverIcon);
		giocatoreVerdeCb.setIcon(greenUntickIcon);
		giocatoreVerdeCb.setSelectedIcon(greenTickIcon);
		giocatoreVerdeCb.setRolloverIcon(greenUntickHoverIcon);
		giocatoreVerdeCb.setRolloverSelectedIcon(greenTickHoverIcon);
		
		giocatoreRossoCb.setSelected(true);
		giocatoreBluCb.setSelected(true);
		giocatoreGialloCb.setSelected(true);
		giocatoreVerdeCb.setSelected(true);
		loggerCb.setSelected(true);
		
		Border initborder = giocatoreRossoTf.getBorder();
		Border emptyborder = BorderFactory.createEmptyBorder(4, 4, 4, 4);
		CompoundBorder border = new CompoundBorder(initborder, emptyborder);
		
		giocatoreRossoTf.setBorder(border);
		giocatoreBluTf.setBorder(border);
		giocatoreGialloTf.setBorder(border);
		giocatoreVerdeTf.setBorder(border);
		
		impostaLookDefaultBottone(startBtn);
		startBtn.setIcon(startIcon);
		startBtn.setRolloverIcon(hovStartIcon);

		impostaLookDefaultBottone(backBtn);
		backBtn.setIcon(backIcon);
		backBtn.setRolloverIcon(hovBackIcon);
					
	}
	
	/**
	 * Posiziona i componenti sulla finestra utilizzando il layout assoluto.
	 */
	@Override
	public void setupLayout() {
		
		startBtn.setBounds(79, 520, 264, 74);
		backBtn.setBounds(365, 590, 44, 44);
		red.setBounds(358, 180, 40, 70);
		blue.setBounds(363, 260, 40, 70);
		yellow.setBounds(360, 340, 40, 70);
		green.setBounds(366, 420, 40, 70);
		selezionaLivello.setBounds(177, 120, 220, 35);
		giocatoreRossoTf.setBounds(83,196, 250, 40);
		giocatoreBluTf.setBounds(83, 276, 250, 40);
		giocatoreGialloTf.setBounds(83, 356,250, 40);
		giocatoreVerdeTf.setBounds(83, 436, 250, 40);
		selezionaLivello.setBounds(176, 120, 220, 35);
		giocatoreRossoCb.setBounds(17, 198, 35, 35);
		giocatoreBluCb.setBounds(17, 277, 35, 35);
		giocatoreGialloCb.setBounds(17, 357, 35, 35);
		giocatoreVerdeCb.setBounds(17, 437, 35, 35);
		loggerCb.setBounds(5, 612, 160, 20);
		
		background.setLayout(null);
		background.add(startBtn);
		background.add(backBtn);
		background.add(giocatoreRossoTf);
		background.add(giocatoreBluTf);
		background.add(giocatoreGialloTf);
		background.add(giocatoreVerdeTf);
		background.add(giocatoreRossoCb);
		background.add(giocatoreBluCb);
		background.add(giocatoreGialloCb);
		background.add(giocatoreVerdeCb);
		background.add(loggerCb);
		background.add(red);
		background.add(blue);
		background.add(yellow);
		background.add(green);
		background.add(selezionaLivello);
		getFrame().add(background);
	}
	
	/**
	 * Registra tutti i listener necessari, inclusi:
	 * <ul>
	 *   <li>Avvio della partita tramite bottone Start.</li>
	 *   <li>Ritorno alla schermata iniziale tramite bottone Back.</li>
	 *   <li>Gestione attivazione/disattivazione dei giocatori.</li>
	 * </ul>
	 */
	@Override
	public void setupListeners() {
		
		startBtn.addActionListener(e-> {
			
			if (!verificaSufficienzaGiocatori()) {
				mostraErrore("Il numero dei giocatori deve essere compreso fra " + Partita.MIN_GIOCATORI + " e " + Partita.MAX_GIOCATORI + "!");
				return;
			}
			
			Livello livello = (Livello) selezionaLivello.getSelectedItem();
			
			String nomeRosso = ottieniNome(giocatoreRossoTf, Colore.ROSSO);		
			if (nomeRosso == null && giocatoreRossoTf.isEnabled()) return;
			
			String nomeBlu = ottieniNome(giocatoreBluTf, Colore.BLU);
			if (nomeBlu == null && giocatoreBluTf.isEnabled()) return;
			
			String nomeGiallo = ottieniNome(giocatoreGialloTf, Colore.GIALLO);
			if (nomeGiallo == null && giocatoreGialloTf.isEnabled()) return;
			
			String nomeVerde = ottieniNome(giocatoreVerdeTf, Colore.VERDE);
			if (nomeVerde == null && giocatoreVerdeTf.isEnabled()) return;
			
			/* non effettuo controlli su duplicazione dei nomi dato che ogni
			   giocatore è identificato univocamente dal proprio colore */
			
			List<Giocatore> giocatori = new ArrayList<Giocatore>();
			
			if (nomeRosso != null) giocatori.add(new Giocatore(nomeRosso, Colore.ROSSO, new Nave(livello, Colore.ROSSO)));
			if (nomeBlu != null) giocatori.add(new Giocatore(nomeBlu, Colore.BLU, new Nave(livello, Colore.BLU)));
			if (nomeGiallo != null) giocatori.add(new Giocatore(nomeGiallo, Colore.GIALLO, new Nave(livello, Colore.GIALLO)));
			if (nomeVerde != null) giocatori.add(new Giocatore(nomeVerde, Colore.VERDE, new Nave(livello, Colore.VERDE)));
			
			getFrame().dispose();
			
			
			// testing
			/*new Thread(() -> {
				
				Evento ee = new PolvereStellare();
				ee.giocatori = giocatori;
				new VoloGui(ee).mostraEAttendi();
			    SwingUtilities.invokeLater(() -> {
		            
			    	//
		        });
			    
			}).start();	*/
			
				
			if (loggerCb.isSelected()) LOGGER.enable();
			else LOGGER.disable();
			LOGGER.logInizioPartita(giocatori);
			Partita partita = new Partita(giocatori, livello);
			
			
			partita.gioca();
			
			
		});
		
		backBtn.addActionListener(e-> {			
			getFrame().dispose();
			new IntroGui();
			
		});
		
		
		giocatoreRossoCb.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                	giocatoreRossoTf.setEnabled(true);
                	red.setIcon(redIcon);
                } else if (e.getStateChange() == ItemEvent.DESELECTED) {
                	giocatoreRossoTf.setEnabled(false);
                	red.setIcon(notRedIcon);
                }
            }
        });
		
		giocatoreBluCb.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                	giocatoreBluTf.setEnabled(true);
                	blue.setIcon(blueIcon);
                } else if (e.getStateChange() == ItemEvent.DESELECTED) {
                	giocatoreBluTf.setEnabled(false);
                	blue.setIcon(notBlueIcon);
                }
            }
        });
		
		giocatoreGialloCb.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                	giocatoreGialloTf.setEnabled(true);
                	yellow.setIcon(yellowIcon);
                } else if (e.getStateChange() == ItemEvent.DESELECTED) {
                	giocatoreGialloTf.setEnabled(false);
                	yellow.setIcon(notYellowIcon);
                }
            }
        });
		
		giocatoreVerdeCb.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                	giocatoreVerdeTf.setEnabled(true);
                	green.setIcon(greenIcon);
                } else if (e.getStateChange() == ItemEvent.DESELECTED) {
                	giocatoreVerdeTf.setEnabled(false);
                	green.setIcon(notGreenIcon);
                }
            }
        });
		
		loggerCb.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                	loggerCb.setText("Logger abilitato");
                } else if (e.getStateChange() == ItemEvent.DESELECTED) {
                	loggerCb.setText("Logger disabilitato");
                }
            }
        });
		
		
	}
	
	/**
	 * Verifica che il numero di giocatori attivi sia valido.
	 *
	 * @return {@code true} se il numero di giocatori è compreso tra i limiti minimi e massimi.
	 * 		   {@code false} altrimenti
	 * @see Partita
	 */
	private boolean verificaSufficienzaGiocatori() {
		int enabled = 0;
		
		if (giocatoreRossoTf.isEnabled()) enabled ++;
		if (giocatoreBluTf.isEnabled()) enabled ++;
		if (giocatoreGialloTf.isEnabled()) enabled ++;
		if (giocatoreVerdeTf.isEnabled()) enabled ++;
		
		return (enabled >= Partita.MIN_GIOCATORI && enabled <= Partita.MAX_GIOCATORI);
		
	}
	
	/**
	 * Ottiene e valida il nome di un giocatore da un campo di testo.
	 *
	 * @param textfield Campo di testo da cui estrarre il nome.
	 * @param colore Colore del giocatore, usato per i messaggi di errore.
	 * @return Nome valido del giocatore, oppure {@code null} se non valido.
	 */
	private String ottieniNome(PlaceholderTextField textfield, Colore colore) {
		
		if (!textfield.isEnabled()) return null;
		
	    String nome = textfield.getRealText().trim();

	    if (nome.length() < PLAYER_NAME_MIN_LEN || nome.length() > PLAYER_NAME_MAX_LEN) {
	        mostraErrore("Il nome dei giocatori deve essere compreso fra " + PLAYER_NAME_MIN_LEN + " e " + PLAYER_NAME_MAX_LEN + " caratteri!", colore);
	        return null;
	    }

	    if (!nome.matches("^[a-zA-Z0-9àèéìòùÀÈÉÌÒÙ ]+$")) {
	        mostraErrore("Il nome dei giocatori può contenere solo lettere, cifre e spazi!", colore);
	        return null;
	    }

	    return nome;
	}
	
	/**
	 * Mostra una finestra di dialogo con un messaggio di errore generico.
	 *
	 * @param messaggio Il testo del messaggio da visualizzare nella finestra di dialogo.
	 */
	private void mostraErrore(String messaggio) {
	    JOptionPane.showMessageDialog(
	        getFrame(),
	        messaggio,
	        "Errore!",
	        JOptionPane.ERROR_MESSAGE
	    );
	}
	
	/**
	 * Mostra una finestra di dialogo con un messaggio di errore specifico per un giocatore.
	 *
	 * @param messaggio Il testo del messaggio da visualizzare.
	 * @param colore Il colore associato al giocatore per cui è avvenuto l'errore.
	 */
	private void mostraErrore(String messaggio, Colore colore) {
	    JOptionPane.showMessageDialog(
	        getFrame(),
	        messaggio,
	        "Errore nel GIOCATORE " + colore + "!",
	        JOptionPane.ERROR_MESSAGE
	    );
	}

}