package galaxytrucker.src.view.frames;

import java.awt.BorderLayout;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.ToolTipManager;
import galaxytrucker.src.view.base.GuiBase;

/**
 * Classe che rappresenta l'interfaccia grafica introduttiva del gioco.
 * Mostra una schermata con sfondo, un pulsante per iniziare una nuova partita e due pulsanti
 * icona per accedere a risorse esterne: regolamento e sito ufficiale del gioco.
 *
 * @see GuiBase
 */
public class IntroGui extends GuiBase{
	
	// costanti
	
	private static final int WIDTH = 592;
	private static final int HEIGHT = 462;
	
	private static final String REGOLE_URL = "https://drive.google.com/file/d/1QB80JkDJSRWcFHzO_NxaxQL8BHtRW2-E/view?usp=drive_link";
	private static final String SITO_UFFICIALE_URL = "https://www.craniocreations.it/prodotto/galaxy-trucker";
	private static final String DIR_PATH = "/galaxytrucker/resources/images/intro/";
	private static final String BACKGROUND_IMG_PATH = DIR_PATH + "intro_background.png";
	private static final String NUOVA_PARTITA_ICON_PATH = DIR_PATH + "nuova_partita.png";
	private static final String REGOLE_ICON_PATH = DIR_PATH + "regole.png";
	private static final String SITO_UFFICIALE_ICON_PATH = DIR_PATH + "sito.png";
	private static final String HOV_NUOVA_PARTITA_ICON_PATH = DIR_PATH + "nuova_partita_hover.png";
	private static final String HOV_REGOLE_ICON_PATH = DIR_PATH + "regole_hover.png";
	private static final String HOV_SITO_UFFICIALE_ICON_PATH = DIR_PATH + "sito_hover.png";
	
	// componenti
	
	private JLabel background;
	
	private JButton nuovaPartitaBtn;
	private JButton regoleBtn;
	private JButton sitoUfficialeBtn;
	
	private ImageIcon backgroundIcon;
	private ImageIcon nuovaPartitaIcon;
	private ImageIcon regoleIcon;
	private ImageIcon sitoUfficialeIcon;
	private ImageIcon hovNuovaPartitaIcon;
	private ImageIcon hovRegoleIcon;
	private ImageIcon hovSitoUfficialeIcon;

	/**
     * Costruttore della GUI introduttiva.
     * <p>
     * Inizializza la finestra, posiziona i componenti, registra i listener.
     * </p>
     */
	public IntroGui() {	
		
		super();	
		getFrame().setLayout(new BorderLayout());
        getFrame().setSize(WIDTH, HEIGHT);
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        
        // setup iniziale del ToolTipManager
        ToolTipManager.sharedInstance().setInitialDelay(850);
        ToolTipManager.sharedInstance().setReshowDelay(0);
        ToolTipManager.sharedInstance().setDismissDelay(10000);
        
        getFrame().setLocation((screenSize.width - getFrame().getWidth()) / 2, (screenSize.height - getFrame().getHeight()) / 2);
        setupComponents();
		setupLayout();
		setupListeners();
		getFrame().revalidate();
		
	}
	
	/**
	 * Inizializza e configura i componenti grafici
	 * della finestra, inclusi bottoni, label e icone.
	 */
	@Override
	public void setupComponents() {	
		
		backgroundIcon  = new ImageIcon(getClass().getResource(BACKGROUND_IMG_PATH));			
		nuovaPartitaIcon = new ImageIcon(getClass().getResource(NUOVA_PARTITA_ICON_PATH));		
		regoleIcon = new ImageIcon(getClass().getResource(REGOLE_ICON_PATH));
		sitoUfficialeIcon = new ImageIcon(getClass().getResource(SITO_UFFICIALE_ICON_PATH));
		hovNuovaPartitaIcon = new ImageIcon(getClass().getResource(HOV_NUOVA_PARTITA_ICON_PATH));		
		hovRegoleIcon = new ImageIcon(getClass().getResource(HOV_REGOLE_ICON_PATH));
		hovSitoUfficialeIcon = new ImageIcon(getClass().getResource(HOV_SITO_UFFICIALE_ICON_PATH));
		
		background = new JLabel(backgroundIcon);
		
		nuovaPartitaBtn = new JButton();
		regoleBtn = new JButton();
		sitoUfficialeBtn = new JButton();
		
		impostaLookDefaultBottone(nuovaPartitaBtn);
		nuovaPartitaBtn.setIcon(nuovaPartitaIcon);
		nuovaPartitaBtn.setRolloverIcon(hovNuovaPartitaIcon);
		
		impostaLookDefaultBottone(regoleBtn);
		regoleBtn.setIcon(regoleIcon);
		regoleBtn.setRolloverIcon(hovRegoleIcon);
		regoleBtn.setToolTipText("Regolamento");
		
		impostaLookDefaultBottone(sitoUfficialeBtn);
		sitoUfficialeBtn.setIcon(sitoUfficialeIcon);
		sitoUfficialeBtn.setRolloverIcon(hovSitoUfficialeIcon);
		sitoUfficialeBtn.setToolTipText("Sito ufficiale");
		
	}
	
	/**
	 * Posiziona i componenti sulla finestra utilizzando il layout assoluto.
	 */
	@Override
	public void setupLayout() {
		
		nuovaPartitaBtn.setBounds(146, 266, 300, 90);
		regoleBtn.setBounds(519, 402, 20, 20);
		sitoUfficialeBtn.setBounds(549, 401, 20, 20);
		
		// piccolo aggiustamento grafico per MacOS
		if (System.getProperty("os.name").toLowerCase().contains("mac")) {
			regoleBtn.setBounds(526, 409, 20, 20);
			sitoUfficialeBtn.setBounds(556, 408, 20, 20);
		}
		
		background.setLayout(null);
		background.add(nuovaPartitaBtn);
		background.add(regoleBtn);
		background.add(sitoUfficialeBtn);
		getFrame().add(background);
		
	}
	
	/**
     * Associa gli event listener ai pulsanti della GUI.
     * Ogni pulsante apre una nuova finestra o un link esterno.
     */
	@Override
	public void setupListeners() {	
		
		nuovaPartitaBtn.addActionListener(e -> {
			getFrame().dispose();
			new SetupGui();
		});
		
		regoleBtn.addActionListener(e -> {
			
			try{
				Desktop.getDesktop().browse(new URI(REGOLE_URL));
		    }
		    catch(IOException | URISyntaxException exc){
		    	
				JOptionPane.showMessageDialog(null, exc);
			}
			
		});
		
		sitoUfficialeBtn.addActionListener(e -> {
			
			try{
				Desktop.getDesktop().browse(new URI(SITO_UFFICIALE_URL));
		    }
		    catch(IOException | URISyntaxException exc){

				JOptionPane.showMessageDialog(null, exc);
			}
			
		});
		
	}

}