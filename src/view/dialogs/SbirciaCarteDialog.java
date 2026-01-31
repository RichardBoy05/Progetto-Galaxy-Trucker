package galaxytrucker.src.view.dialogs;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Image;
import java.util.Arrays;
import java.util.List;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.border.MatteBorder;
import galaxytrucker.src.logic.gioco.GameLogger;
import galaxytrucker.src.logic.volo.Evento;
import galaxytrucker.src.view.base.GuiConfigurable;
import galaxytrucker.src.view.base.IconHandler;

/**
 * Classe che rappresenta una finestra di dialogo modale che permette
 * al giocatore di sbirciare le carte {@link Evento} prima che vengano giocate.
 * Viene mostrata una carta alla volta, con pulsanti per navigare avanti e indietro.
 * 
 * @see GuiConfigurable
 */
public class SbirciaCarteDialog implements GuiConfigurable {
	
	// costanti
	
	private static final int WIDTH = 340;
	private static final int HEIGHT = 650;
	
	private static final String DIR_PATH = "/galaxytrucker/resources/images/assemblaggio/";
	private static final String BACKGROUND_IMG_PATH = DIR_PATH + "sbircia_carte_background.png";
	private static final String AVANTI_ICON_PATH = DIR_PATH + "avanti.png";
	private static final String HOV_AVANTI_ICON_PATH = DIR_PATH + "avanti_hover.png";
	private static final String INDIETRO_ICON_PATH = DIR_PATH + "indietro.png";
	private static final String HOV_INDIETRO_ICON_PATH = DIR_PATH + "indietro_hover.png";
	private static final String X_128_ICON_PATH = "/galaxytrucker/resources/images/icons/icon128.png";
	private static final String X_64_ICON_PATH = "/galaxytrucker/resources/images/icons/icon64.png";
	private static final String X_32_ICON_PATH = "/galaxytrucker/resources/images/icons/icon32.png";
	
	private static final GameLogger LOGGER = GameLogger.getInstance();
	
	// componenti
	
	private JDialog dialog;
	
	private JLabel background;
	private JLabel cartaLbl;
	private JButton avantiBtn;
	private JButton indietroBtn;
	
	private ImageIcon x128Icon;
	private ImageIcon x64Icon;
	private ImageIcon x32Icon;
	private Image x128Img;
	private Image x64Img;
	private Image x32Img;
	private Image[] images;
	private ImageIcon backgroundIcon;
	private ImageIcon avantiIcon;
	private ImageIcon hovAvantiIcon;
	private ImageIcon indietroIcon;
	public ImageIcon hovIndietroIcon;
	
	// altri attributi
	
	private List<Evento> carteSbirciabili;
	private int indiceCartaCorrente = 0;	

	/**
     * Crea e visualizza una nuova finestra di dialogo per sbirciare carte.
     *
     * @param parent la finestra principale {@link JFrame} da cui viene mostrato il dialog.
     * @param carteSbirciabili la lista di carte {@link Evento} da visualizzare.
     * @throws NullPointerException se {@code carteSbirciabili} è {@code null}.
     */
	public SbirciaCarteDialog(JFrame parent, List<Evento> carteSbirciabili) {
		
		if (carteSbirciabili == null) {
			String errore = "Il parametro 'carteSbirciabili' non può essere nullo!";
			LOGGER.error(errore);
			throw new NullPointerException(errore);
		}
    	
        this.carteSbirciabili = carteSbirciabili;
        
        dialog = new JDialog(parent, parent.getTitle(), true);
        dialog.setSize(WIDTH, HEIGHT);
        dialog.setResizable(false);
        dialog.setLocationRelativeTo(parent);
        dialog.setLayout(new BorderLayout());
        
        x128Icon = new ImageIcon(getClass().getResource(X_128_ICON_PATH));		
		x64Icon = new ImageIcon(getClass().getResource(X_64_ICON_PATH));		
		x32Icon = new ImageIcon(getClass().getResource(X_32_ICON_PATH));
		x128Img = x128Icon.getImage();
		x64Img = x64Icon.getImage();
		x32Img = x32Icon.getImage();
		images = new Image[] {x128Img, x64Img, x32Img};
		dialog.setIconImages(Arrays.asList(images));

        setupComponents();
        setupLayout();
        setupListeners();        
        dialog.setVisible(true);
    }
	
	/**
     * Inizializza i componenti grafici della finestra, come
     * sfondo, etichetta della carta e pulsanti avanti/indietro.
     */
	@Override
	public void setupComponents() {
		
		backgroundIcon = new ImageIcon(getClass().getResource(BACKGROUND_IMG_PATH));
		avantiIcon = new ImageIcon(getClass().getResource(AVANTI_ICON_PATH));
		hovAvantiIcon = new ImageIcon(getClass().getResource(HOV_AVANTI_ICON_PATH));
		indietroIcon = new ImageIcon(getClass().getResource(INDIETRO_ICON_PATH));
		hovIndietroIcon = new ImageIcon(getClass().getResource(HOV_INDIETRO_ICON_PATH));
		
		background = new JLabel();
		cartaLbl = new JLabel();
		avantiBtn = new JButton();
		indietroBtn = new JButton();
		
		IconHandler.setIconOnLabel(background, backgroundIcon, backgroundIcon);
		cartaLbl.setBorder(new MatteBorder(0, 0, 5, 0, Color.BLACK));
		IconHandler.setIconOnLabel(cartaLbl, carteSbirciabili.get(indiceCartaCorrente).getImmagine(), carteSbirciabili.get(indiceCartaCorrente).getImmagine());
		
		impostaLookDefaultBottone(avantiBtn);
		IconHandler.setIconOnButton(avantiBtn, avantiIcon, hovAvantiIcon);  	
		avantiBtn.setToolTipText("Vedi la prossima carta!");
		
		impostaLookDefaultBottone(indietroBtn);
		IconHandler.setIconOnButton(indietroBtn, indietroIcon, hovIndietroIcon);
		indietroBtn.setToolTipText("Vedi la carta precedente!");
		
	}

	/**
     * Posiziona i componenti all'interno della finestra tramite posizionamento assoluto.
     */
	@Override
	public void setupLayout() {
		
		background.setLayout(null);
		cartaLbl.setBounds(0, 0, WIDTH, 527);
		avantiBtn.setBounds(250, 538, 60, 60);
		indietroBtn.setBounds(14, 538, 60, 60);
		
		background.add(cartaLbl);
		background.add(avantiBtn);
		background.add(indietroBtn);
		dialog.add(background);
		
	}

	/**
     * Aggiunge i listener ai pulsanti per navigare tra le carte.
     */
	@Override
	public void setupListeners() {
		
		avantiBtn.addActionListener(e -> {
			indiceCartaCorrente = (indiceCartaCorrente + 1) % carteSbirciabili.size();
			IconHandler.setIconOnLabel(cartaLbl, carteSbirciabili.get(indiceCartaCorrente).getImmagine(), carteSbirciabili.get(indiceCartaCorrente).getImmagine());
		});
		
		indietroBtn.addActionListener(e -> {
			indiceCartaCorrente = (indiceCartaCorrente - 1 + carteSbirciabili.size()) % carteSbirciabili.size();
			IconHandler.setIconOnLabel(cartaLbl, carteSbirciabili.get(indiceCartaCorrente).getImmagine(), carteSbirciabili.get(indiceCartaCorrente).getImmagine());
		});
	}
	
	/**
     * Imposta uno stile trasparente e senza bordo su un pulsante, utile 
     * per consentire la corretta visualizzazione dell'immagine associata.
     *
     * @param btn il pulsante da configurare.
     */
    private void impostaLookDefaultBottone(JButton btn) {
        btn.setBorder(null);
        btn.setBorderPainted(false);
        btn.setContentAreaFilled(false);    
        btn.setFocusPainted(false);
        btn.setFocusable(false);
        btn.setOpaque(false); 
    }

}