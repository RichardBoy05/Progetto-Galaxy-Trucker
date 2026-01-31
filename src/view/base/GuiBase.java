package galaxytrucker.src.view.base;

import java.awt.Image;
import java.util.Arrays;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;

/**
 * Classe astratta di base per tutte le GUI del gioco basate su {@link JFrame}.
 * <p>
 * Inizializza una finestra principale non ridimensionabile con icone personalizzate
 * e un titolo predefinito. Le icone vengono caricate da risorse interne e impostate
 * in vari formati per supportare più risoluzioni.
 * </p>
 * <p>
 * Le sottoclassi devono estendere {@code GuiBase} e possono utilizzare
 * {@link #getFrame()} per accedere alla finestra principale.
 * </p>
 * 
 * @see GuiConfigurable 
 */
public abstract class GuiBase implements GuiConfigurable {
		
	// percorsi relativi alle risorse grafiche
	
	private static final String DIR_PATH = "/galaxytrucker/resources/images/icons/";
	private static final String X_128_ICON_PATH = DIR_PATH + "icon128.png";	
	private static final String X_64_ICON_PATH = DIR_PATH + "icon64.png";	
	private static final String X_32_ICON_PATH = DIR_PATH + "icon32.png";
	
	// componenti
	
	private final JFrame frame;
	
	private ImageIcon x128Icon;
	private ImageIcon x64Icon;
	private ImageIcon x32Icon;
	private Image x128Img;
	private Image x64Img;
	private Image x32Img;
	private Image[] images;

	/**
	 * Costruttore base che inizializza la finestra principale del gioco,
	 * imposta il titolo, carica le icone e le applica al frame.
	 */
	public GuiBase() {
        frame = new JFrame("Galaxy Trucker");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(false);
		frame.setVisible(true);

		x128Icon = new ImageIcon(getClass().getResource(X_128_ICON_PATH));		
		x64Icon = new ImageIcon(getClass().getResource(X_64_ICON_PATH));		
		x32Icon = new ImageIcon(getClass().getResource(X_32_ICON_PATH));

		x128Img = x128Icon.getImage();
		x64Img = x64Icon.getImage();
		x32Img = x32Icon.getImage();

		images = new Image[] {x128Img, x64Img, x32Img};
		frame.setIconImages(Arrays.asList(images));
    }
	
    /**
     * Imposta uno stile trasparente e senza bordo su un pulsante, utile 
     * per consentire la corretta visualizzazione dell'immagine associata.
     *
     * @param btn il pulsante da configurare.
     */
    protected void impostaLookDefaultBottone(JButton btn) {
        btn.setBorder(null);
        btn.setBorderPainted(false);
        btn.setContentAreaFilled(false);    
        btn.setFocusPainted(false);
        btn.setFocusable(false);
        btn.setOpaque(false); 
    }

	/**
	 * Restituisce il frame principale associato a questa GUI.
	 * 
	 * @return il {@link JFrame} principale.
	 */
	public JFrame getFrame() {
		return frame;
	}
}