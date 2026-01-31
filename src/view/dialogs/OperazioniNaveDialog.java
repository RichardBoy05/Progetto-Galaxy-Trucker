package galaxytrucker.src.view.dialogs;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.Toolkit;
import java.util.Arrays;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import galaxytrucker.src.logic.assemblaggio.Nave;
import galaxytrucker.src.logic.gioco.GameLogger;
import galaxytrucker.src.logic.gioco.Giocatore;
import galaxytrucker.src.view.base.GuiConfigurable;
import galaxytrucker.src.view.base.IconHandler;
import galaxytrucker.src.view.components.PannelloNave;

/**
 * Classe astratta che rappresenta una finestra di dialogo modale
 * per effettuare operazioni sulla {@link Nave} di un {@link Giocatore}.
 * Fornisce un'interfaccia grafica personalizzata con sfondo, icone
 * e un pulsante "Fine" per la chiusura.
 * <p>
 * Le sottoclassi possono eventualmente ridefinire il metodo {@link #operazioniFinali()}
 * per aggiungere comportamento personalizzato alla chiusura.
 */
public abstract class OperazioniNaveDialog implements GuiConfigurable {

    // costanti
	
    private static final int WIDTH = 1000;
    private static final int HEIGHT = 550;
    
    private static final String DIR_PATH = "/galaxytrucker/resources/images/utente/";
    private static final String BACKGROUND_IMG_PATH = DIR_PATH + "operazione_nave_background.png";    
    private static final String FINE_ICON_PATH = DIR_PATH + "fine.png";
    private static final String FINE_HOVER_ICON_PATH = DIR_PATH + "fine_hover.png";
    private static final String FINE_DISABLED_ICON_PATH = DIR_PATH + "fine_disabled.png";
    private static final String X_128_ICON_PATH = "/galaxytrucker/resources/images/icons/icon128.png";
    private static final String X_64_ICON_PATH = "/galaxytrucker/resources/images/icons/icon64.png";
    private static final String X_32_ICON_PATH = "/galaxytrucker/resources/images/icons/icon32.png";

    private static final GameLogger LOGGER = GameLogger.getInstance();
    
    // componenti e altri attributi

    private JDialog dialog;
    private final JFrame parent;
    private final Giocatore giocatore;

    private JLabel background;
    private JButton fineBtn;

    private ImageIcon backgroundIcon;
    private ImageIcon fineIcon;
    private ImageIcon hovFineIcon;
    private ImageIcon fineDisabledIcon;

    private ImageIcon x128Icon;
    private ImageIcon x64Icon;
    private ImageIcon x32Icon;
    private Image x128Img;
    private Image x64Img;
    private Image x32Img;
    private Image[] images;

    /**
     * Costruisce il dialog per un giocatore specifico.
     * 
     * @param parent    la finestra principale {@link JFrame} (può essere null; in tal caso viene creata una finestra placeholder)
     * @param giocatore il giocatore a cui si riferisce la nave
     * @throws NullPointerException se {@code giocatore} o la sua nave sono {@code null}.
     */
    public OperazioniNaveDialog(JFrame parent, Giocatore giocatore) {
    	
        if (giocatore == null) {
            String errore = "Il parametro 'giocatore' non può essere nullo!";
            LOGGER.error(errore);
            throw new NullPointerException(errore);
        }

        if (giocatore.getNave() == null) {
            String errore = "L'attributo 'nave' del giocatore non può essere nullo!";
            LOGGER.error(errore);
            throw new NullPointerException(errore);
        }
        
        this.parent = parent;
        this.giocatore = giocatore;
        
        initDialog();
    }
    
    /**
     * Inizializza il {@link JDialog} associato alla finestra di operazioni sulla nave.
     * Questo metodo configura dimensioni, posizione, icone della finestra,
     * sfondo, pulsante "Fine" e listener di chiusura.
     */
    private void initDialog() {
    	
        x128Icon = new ImageIcon(getClass().getResource(X_128_ICON_PATH));        
        x64Icon = new ImageIcon(getClass().getResource(X_64_ICON_PATH));        
        x32Icon = new ImageIcon(getClass().getResource(X_32_ICON_PATH));
        x128Img = x128Icon.getImage();
        x64Img = x64Icon.getImage();
        x32Img = x32Icon.getImage();
        images = new Image[] {x128Img, x64Img, x32Img};

        dialog = new JDialog((parent != null) ? parent : creaParentComePlaceholder(images), true);
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        dialog.setResizable(false);
        dialog.setVisible(false);
        dialog.setTitle(giocatore.getColore().name() + " - " + giocatore.getNome());
        dialog.setSize(WIDTH, HEIGHT);
        dialog.setLayout(new BorderLayout());
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        dialog.setLocation((screenSize.width - dialog.getWidth()) / 2, (screenSize.height - dialog.getHeight()) / 2);
        dialog.setIconImages(Arrays.asList(images));

        backgroundIcon = new ImageIcon(getClass().getResource(BACKGROUND_IMG_PATH));
        fineIcon = new ImageIcon(getClass().getResource(FINE_ICON_PATH));
        hovFineIcon = new ImageIcon(getClass().getResource(FINE_HOVER_ICON_PATH));
        fineDisabledIcon = new ImageIcon(getClass().getResource(FINE_DISABLED_ICON_PATH));

        background = new JLabel();
        IconHandler.setIconOnLabel(background, backgroundIcon, backgroundIcon);

        fineBtn = new JButton();
        impostaLookDefaultBottone(fineBtn);
        IconHandler.setIconOnButton(fineBtn, fineIcon, hovFineIcon);
        fineBtn.putClientProperty("enabled", true);
        fineBtn.setBounds(900, 460, 73, 40);

        abilitaChiusuraDialog();

        fineBtn.addActionListener(e -> {
            operazioniFinali();
            if (Boolean.TRUE.equals(fineBtn.getClientProperty("enabled"))) dialog.dispose();
            if (Boolean.TRUE.equals(((JFrame) getDialog().getOwner()).getRootPane().getClientProperty("placeholder"))) {
                ((JFrame) getDialog().getOwner()).dispose();
            }
        });

        background.add(fineBtn);
        dialog.add(background);
        dialog.revalidate();
    }

    /**
     * Posiziona il pannello nave nella finestra di dialogo.
     *
     * @param pannelloNave il pannello della nave da aggiungere
     */
    protected void posizionaPannelloNave(PannelloNave pannelloNave) {
        pannelloNave.setBounds(0, 0, 730, 511);
        background.add(pannelloNave);
    }

    /**
     * Abilita la chiusura del dialog.
     */
    protected void abilitaChiusuraDialog() {
        IconHandler.setIconOnButton(fineBtn, fineIcon, hovFineIcon);
        fineBtn.putClientProperty("enabled", true);
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
    }

    /**
     * Disabilita la chiusura del dialog.
     */
    protected void disabilitaChiusuraDialog() {
        IconHandler.setIconOnButton(fineBtn, fineDisabledIcon, fineDisabledIcon);
        fineBtn.putClientProperty("enabled", false);
        dialog.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
    }

    /**
     * Metodo helper per creare un JFrame invisibile come parent temporaneo.
     *
     * @param icons le immagini da impostare come icona.
     * @return il frame placeholder.
     */
    private JFrame creaParentComePlaceholder(Image[] icons) {
        JFrame placeholderParent = new JFrame();
        placeholderParent.setUndecorated(true);
        placeholderParent.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        placeholderParent.setSize(0, 0);
        placeholderParent.setLocationRelativeTo(null);
        placeholderParent.setVisible(true);
        placeholderParent.setIconImages(Arrays.asList(icons));
        placeholderParent.getRootPane().putClientProperty("placeholder", true);
        return placeholderParent;
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
     * Metodo eseguibile alla pressione del pulsante "Fine".
     * Le sottoclassi possono ridefinirlo per aggiungere operazioni personalizzate.
     */
    protected void operazioniFinali() {
        return; // non fa nulla se non ridefinito
    }   

    /**
     * Restituisce il {@link JDialog} interno.
     *
     * @return il dialogo.
     */
    protected JDialog getDialog() {
        return dialog;
    }

    /**
     * Restituisce il background del dialog.
     *
     * @return JLabel usata come sfondo.
     */
    protected JLabel getBackground() {
        return background;
    }

    /**
     * Restituisce il {@link Giocatore} associato.
     *
     * @return il giocatore.
     */
    protected Giocatore getGiocatore() {
        return giocatore;
    }

    /**
     * Restituisce l'istanza del {@link GameLogger}.
     *
     * @return il logger
     */
    protected static GameLogger getLogger() {
        return LOGGER;
    }
    
}