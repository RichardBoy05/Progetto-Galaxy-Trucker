package galaxytrucker.src.view.dialogs;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Image;
import java.awt.event.WindowAdapter;
import java.util.Arrays;
import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import galaxytrucker.src.logic.gioco.Giocatore;
import galaxytrucker.src.view.base.GuiConfigurable;

/**
 * Classe responsabile della creazione e visualizzazione di una finestra di dialogo
 * contenente le statistiche di un {@link Giocatore} alla fine della partita.
 * 
 * <p>Mostra informazioni come: nome, posizione finale, distanza percorsa, 
 * crediti guadagnati, debiti subiti, stato di arrivo e premio per la nave più bella.</p>
 * 
 * <p>La grafica viene personalizzata in base al colore del giocatore.</p>
 * 
 * @see GuiConfigurable
 */
public class PlayerStatsDialog implements GuiConfigurable {
	
	// costanti
	
    private static final int WIDTH = 260;
    private static final int HEIGHT = 447;  
    
    private static final String DIR_PATH = "/galaxytrucker/resources/images/fine/";
    private static final String BACKGROUND_IMG_PATH = DIR_PATH + "stats_";
    private static final String BEST_NAVE_ICON_PATH = DIR_PATH + "best_nave.png";
    private static final String TICK_ICON_PATH = DIR_PATH + "tick.png";
    private static final String X_ICON_PATH = DIR_PATH + "x.png";
	private static final String X_128_ICON_PATH = "/galaxytrucker/resources/images/icons/icon128.png";
	private static final String X_64_ICON_PATH = "/galaxytrucker/resources/images/icons/icon64.png";
	private static final String X_32_ICON_PATH = "/galaxytrucker/resources/images/icons/icon32.png";
	
	// componenti e altri attributi
    
    private JDialog dialog;
    private Giocatore giocatore;

    private JLabel background;
    private JLabel nome;
    private JLabel ranking;
    private JLabel giorniDiVoloPersi;
    private JLabel crediti;
    private JLabel debiti;
    private JLabel arrivato;
    private JLabel premio;

    private ImageIcon x128Icon;
	private ImageIcon x64Icon;
	private ImageIcon x32Icon;
	private Image x128Img;
	private Image x64Img;
	private Image x32Img;
	private Image[] images;
    private ImageIcon backgroundIcon;
    private ImageIcon bestNaveIcon;
    private ImageIcon tickIcon;
    private ImageIcon xIcon;

    private Font valoriFt;

    /**
     * Costruttore della finestra delle statistiche del giocatore.
     * 
     * @param parent    la finestra principale {@link JFrame} da cui viene mostrato il dialog.
     * @param giocatore Giocatore di cui mostrare le statistiche.
     */
    public PlayerStatsDialog(JFrame parent, Giocatore giocatore) {
    	
        this.giocatore = giocatore;
        dialog = new JDialog(parent, giocatore.getColore().name() + " - " + giocatore.getNome(), true);
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
     * Inizializza e configura i componenti grafici
     * della finestra, incluse etichette, icone e font.
	 */
    @Override
    public void setupComponents() {
    	
        backgroundIcon = new ImageIcon(getClass().getResource(BACKGROUND_IMG_PATH + giocatore.getColore().name().toLowerCase() + ".png"));
        bestNaveIcon = new ImageIcon(getClass().getResource(BEST_NAVE_ICON_PATH));
        tickIcon = new ImageIcon(getClass().getResource(TICK_ICON_PATH));
        xIcon = new ImageIcon(getClass().getResource(X_ICON_PATH));

        background = new JLabel(backgroundIcon);
        nome = new JLabel(giocatore.getNome());    
        ranking = new JLabel("#" + giocatore.getRankFinale());
        giorniDiVoloPersi = new JLabel(String.valueOf(giocatore.getGiorniDiVoloPersi()));
        crediti = new JLabel(String.valueOf(giocatore.getCrediti()));
        debiti = new JLabel(String.valueOf(giocatore.getDebiti()));
        arrivato = new JLabel();
        premio = new JLabel();

        valoriFt = nome.getFont().deriveFont(37f);
        
        nome.setFont(valoriFt);
        nome.setForeground(Color.WHITE);    
        ranking.setFont(valoriFt);
        ranking.setForeground(Color.WHITE);
        giorniDiVoloPersi.setFont(valoriFt);
        giorniDiVoloPersi.setForeground(Color.WHITE);
        crediti.setFont(valoriFt);
        crediti.setForeground(Color.WHITE);
        debiti.setFont(valoriFt);
        debiti.setForeground(Color.WHITE);

        nome.setHorizontalAlignment(SwingConstants.CENTER);
        nome.setVerticalAlignment(SwingConstants.CENTER);

        ranking.setToolTipText("Posizione in classifica");
        giorniDiVoloPersi.setToolTipText("Giorni di volo persi");
        crediti.setToolTipText("Crediti cosmici guadagnati");
        debiti.setToolTipText("Debiti e perdite varie");

        arrivato.setIcon(giocatore.isInVolo() ? tickIcon : xIcon);
        arrivato.setToolTipText(giocatore.isInVolo() ? "Giunto sano e salvo a destinazione!" : "Disperso nella galassia...");

        premio.setIcon(giocatore.getNave().isNavePiuBella() ? bestNaveIcon : xIcon);
        premio.setToolTipText(giocatore.getNave().isNavePiuBella() ? "Premio nave più bella!" : "Nessun premio");
    }

    /**
	 * Posiziona i componenti sulla finestra utilizzando il layout assoluto.
	 */
    @Override
    public void setupLayout() {
        background.setLayout(null);
        nome.setBounds(0, 10, WIDTH, 75);
        ranking.setBounds(193, 107, 50, 30);
        giorniDiVoloPersi.setBounds(193, 160, 50, 30);
        crediti.setBounds(193, 209, 50, 30);
        debiti.setBounds(193, 259, 50, 30);
        arrivato.setBounds(193, 300, 50, 50);
        premio.setBounds(193, 350, 50, 50);

        if (giocatore.getNave().isNavePiuBella())
            premio.setBounds(190, 350, 50, 50); // aggiustamento grafico

        background.add(nome);
        background.add(ranking);
        background.add(giorniDiVoloPersi);
        background.add(crediti);
        background.add(debiti);
        background.add(arrivato);
        background.add(premio);
        dialog.add(background);
    }

    /**
     * Aggiunge listener agli eventi della finestra, come l'apertura,
     * per attivare l'adattamento nello spazio del nome del giocatore.
     */
    @Override
    public void setupListeners() {
        dialog.addWindowListener(new WindowAdapter() {
            @Override
            public void windowOpened(java.awt.event.WindowEvent e) {
                aggiustaNomeGiocatore(40, 10);
                dialog.revalidate();
                dialog.repaint();
            }
        });
    }

    /**
     * Adatta dinamicamente la dimensione del font
     * del nome del giocatore allo spazio disponibile.
     * 
     * @param maxSize dimensione massima del font.
     * @param minSize dimensione minima del font.
     */
    private void aggiustaNomeGiocatore(int maxSize, int minSize) {
        Container parent = nome.getParent();
        if (parent == null) return;

        int parentWidth = parent.getWidth();
        int labelHeight = nome.getHeight();

        if (parentWidth <= 0 || labelHeight <= 0) return;

        nome.setBounds(0, nome.getY(), parentWidth, labelHeight);

        String text = nome.getText();
        if (text == null || text.isEmpty()) return;

        Font baseFont = nome.getFont();
        int fontSize = maxSize;

        while (fontSize > minSize) {
            Font testFont = new Font(baseFont.getName(), Font.BOLD, fontSize);
            FontMetrics metrics = nome.getFontMetrics(testFont);
            int textWidth = metrics.stringWidth(text);

            if (textWidth <= parentWidth) break;
            fontSize--;
        }

        nome.setFont(new Font(baseFont.getName(), Font.BOLD, fontSize));
    }
}