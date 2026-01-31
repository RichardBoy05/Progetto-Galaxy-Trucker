package galaxytrucker.src.view.dialogs;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.MouseEvent;
import java.util.Arrays;
import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import galaxytrucker.src.logic.assemblaggio.Nave;
import galaxytrucker.src.logic.assemblaggio.Tessera;
import galaxytrucker.src.logic.gioco.GameLogger;
import galaxytrucker.src.logic.gioco.Giocatore;
import galaxytrucker.src.logic.gioco.Livello;
import galaxytrucker.src.view.base.AzioneClickCella;
import galaxytrucker.src.view.components.PannelloNave;

/**
 * Finestra di dialogo che mostra la {@link Nave} di un {@link Giocatore}.
 * Permette all'utente di cliccare sulle celle della nave per visualizzare le informazioni delle tessere.
 */
public class NaveDialog {
	
	// costanti
	
	private static final String DIR_PATH = "/galaxytrucker/resources/images/icons/";
	private static final String X_128_ICON_PATH = DIR_PATH + "icon128.png";
	private static final String X_64_ICON_PATH = DIR_PATH + "icon64.png";
	private static final String X_32_ICON_PATH = DIR_PATH + "icon32.png";
	
	private static final GameLogger LOGGER = GameLogger.getInstance();
	
	// componenti e altri attributi

	private Giocatore giocatore;
    private JFrame parent;
    private JDialog dialog;
    
    private ImageIcon x128Icon;
	private ImageIcon x64Icon;
	private ImageIcon x32Icon;
	private Image x128Img;
	private Image x64Img;
	private Image x32Img;
	private Image[] images;

    /**
     * Costruisce un oggetto NaveDialog per il giocatore specificato.
     * La finestra non viene visualizzata finché non viene chiamato {@link #costruisci()}.
     *
     * @param parent    la finestra principale {@link JFrame} da cui viene mostrato il dialog.
     * @param giocatore il giocatore di cui mostrare la nave.
     * @throws NullPointerException se {@code giocatore} o la sua nave sono {@code null}.
     */
    public NaveDialog(JFrame parent, Giocatore giocatore) {
    	
    	if (giocatore == null) {
        	String errore = "Il parametro 'giocatore' non può esssere nullo!";
        	LOGGER.error(errore);
        	throw new NullPointerException(errore);
        }
        
        if (giocatore.getNave() == null) {
        	String errore = "L'attributo 'nave' del giocatore non può esssere nullo!";
        	LOGGER.error(errore);
        	throw new NullPointerException(errore);
        }
    	
        this.parent = parent;
        this.giocatore = giocatore;
        
        dialog = new JDialog(parent, true);
        x128Icon = new ImageIcon(getClass().getResource(X_128_ICON_PATH));		
		x64Icon = new ImageIcon(getClass().getResource(X_64_ICON_PATH));		
		x32Icon = new ImageIcon(getClass().getResource(X_32_ICON_PATH));
		x128Img = x128Icon.getImage();
		x64Img = x64Icon.getImage();
		x32Img = x32Icon.getImage();
		images = new Image[] {x128Img, x64Img, x32Img};
		dialog.setIconImages(Arrays.asList(images));
        
        costruisci();
    }

    /**
     * Costruisce e mostra la finestra di dialogo contenente la nave del giocatore.
     */
    private void costruisci() {
    	
        dialog = new JDialog(parent, giocatore.getColore().name() + " - " + giocatore.getNome() + ": clicca su una tessera per vederne le caratteristiche specifiche!", true);
        dialog.setResizable(false);
        dialog.setLayout(new BorderLayout());

        impostaDimensioniFinestra();

        PannelloNave pannelloNave = new PannelloNave(giocatore.getNave(), true, new AzioneClickCella() {
            @Override
            public void onClickCella(int riga, int colonna, MouseEvent evento) {
                if (SwingUtilities.isLeftMouseButton(evento)) {
                    Tessera t = giocatore.getNave().getGriglia().getCella(riga, colonna).getTessera();
                    String message = (t == null) ? "Questa cella non contiene alcuna tessera!" : t.toString();
                    JOptionPane.showMessageDialog(dialog, message, "Informazioni sulla tessera", JOptionPane.INFORMATION_MESSAGE);
                }
            }
        });

        dialog.add(pannelloNave, BorderLayout.CENTER);
        dialog.setVisible(true);
        dialog.revalidate();
    }

    /**
     * Imposta le dimensioni della finestra in base alla dimensione dello schermo
     * e al livello della nave. Le dimensioni sono limitate al 70% dello schermo.
     */
    private void impostaDimensioniFinestra() {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int maxW = (int) (screenSize.getWidth() * 0.7);
        int maxH = (int) (screenSize.getHeight() * 0.7);

        int width = maxW;
        int height = width * 3 / 4;

        if (height > maxH) {
            height = maxH;
            width = height * 4 / 3;
        }

        if (giocatore.getNave().getLivello() == Livello.III) { // aggiustamento grafico per la nave di livello III
            width += 70;
        }

        dialog.setSize(width, height);
        dialog.setLocationRelativeTo(null);
    }
}