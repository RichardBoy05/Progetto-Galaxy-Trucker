package galaxytrucker.src.view.dialogs;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Image;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.SwingConstants;
import galaxytrucker.src.logic.assemblaggio.Abitante;
import galaxytrucker.src.logic.assemblaggio.Cabina;
import galaxytrucker.src.logic.assemblaggio.Cella;
import galaxytrucker.src.logic.assemblaggio.Coordinate;
import galaxytrucker.src.logic.eccezioni.PiazzamentoEquipaggioNonValidoException;
import galaxytrucker.src.logic.gioco.GameLogger;
import galaxytrucker.src.logic.gioco.Giocatore;
import galaxytrucker.src.logic.volo.Epidemia;
import galaxytrucker.src.logic.volo.NaveAbbandonata;
import galaxytrucker.src.logic.volo.Pianeta;
import galaxytrucker.src.logic.volo.Pianeti;
import galaxytrucker.src.logic.volo.StazioneAbbandonata;
import galaxytrucker.src.view.base.IconHandler;


/**
 * Dialog di interazione utente durante gli eventi di gioco.
 * <p>
 * Questa classe permette di presentare diverse scelte grafiche all'utente a seconda dell'evento:
 * scelta Sì/No, scelta del pianeta o scelta dell'abitante da rimuovere.
 */
public class InterazioniUtenteDialog {
	
	// costanti
	
	private static final int WIDTH = 400;
    private static final int HEIGHT = 250;
    
    private static final String DIR_PATH = "/galaxytrucker/resources/images/utente/";
    private static final String BACKGROUND_IMG_PATH = DIR_PATH + "interazioni_utente_background.png";
    private static final String MOSTRA_NAVE_ICON_PATH = DIR_PATH + "mostra_nave.png";
    private static final String MOSTRA_NAVE_HOVER_ICON_PATH = DIR_PATH + "mostra_nave_hover.png";
    private static final String SI_ICON_PATH = DIR_PATH + "si_button.png";
    private static final String SI_HOVER_ICON_PATH = DIR_PATH + "si_button_hover.png";
    private static final String NO_ICON_PATH = DIR_PATH + "no_button.png";
    private static final String NO_HOVER_ICON_PATH = DIR_PATH + "no_button_hover.png";
    private static final String ASTRONAUTA_ICON_PATH = DIR_PATH + "astronauta.png";
    private static final String ASTRONAUTA_HOVER_ICON_PATH = DIR_PATH + "astronauta_hover.png";
    private static final String ASTRONAUTA_DISABLED_ICON_PATH = DIR_PATH + "astronauta_disabled.png";
    private static final String ALIENO_MARRONE_ICON_PATH = DIR_PATH + "alieno_marrone.png";
    private static final String ALIENO_MARRONE_HOVER_ICON_PATH = DIR_PATH + "alieno_marrone_hover.png";
    private static final String ALIENO_MARRONE_DISABLED_ICON_PATH = DIR_PATH + "alieno_marrone_disabled.png";
    private static final String ALIENO_VIOLA_ICON_PATH = DIR_PATH + "alieno_viola.png";
    private static final String ALIENO_VIOLA_HOVER_ICON_PATH = DIR_PATH + "alieno_viola_hover.png";
    private static final String ALIENO_VIOLA_DISABLED_ICON_PATH = DIR_PATH + "alieno_viola_disabled.png";
    private static final String X_128_ICON_PATH = "/galaxytrucker/resources/images/icons/icon128.png";
	private static final String X_64_ICON_PATH = "/galaxytrucker/resources/images/icons/icon64.png";
	private static final String X_32_ICON_PATH = "/galaxytrucker/resources/images/icons/icon32.png";
    
    private static final GameLogger LOGGER = GameLogger.getInstance();
    
    // componenti
    
    private JDialog dialog;
    
    private ImageIcon backgroundImg;
    private ImageIcon mostraNaveIcon;
    private ImageIcon hovMostraNaveIcon;
    private ImageIcon x128Icon;
	private ImageIcon x64Icon;
	private ImageIcon x32Icon;
	private Image x128Img;
	private Image x64Img;
	private Image x32Img;
	private Image[] images;
    
    private JLabel background;
    private JLabel titolo;
    private JLabel sottotitolo;
    
    private JButton mostraNaveBtn;
	
    // altri attributi
    
    private Giocatore giocatore;
	private boolean sceltaSiNo = false;
	private Pianeta sceltaPianeta = null;
	
	/**
     * Costruttore per eventi che richiedono una risposta Sì o No da parte dell'utente.
     * <p>
     * Eventi che lo richiedono: {@link NaveAbbandonata} e {@link StazioneAbbandonata}.
     *
     * @param parent    finestra principale {@link JFrame} da cui viene mostrato il dialog.
     * @param giocatore giocatore corrente che deve interagire.
     */
	public InterazioniUtenteDialog(JFrame parent, Giocatore giocatore) {
		
		inizializzaDialog(parent, giocatore);
        costruisciSceltaSiNo();
        dialog.setVisible(true);
	}
	
	
	/**
     * Costruttore per eventi che richiedono la scelta di un pianeta tra quelli disponibili.
     * <p>
     * Eventi che lo richiedono: {@link Pianeti}
     *
     * @param parent    finestra principale  {@link JFrame} da cui viene mostrato il dialog.
     * @param giocatore giocatore corrente che deve interagire.
     * @param pianeti   lista di pianeti da cui scegliere (almeno 2 non occupati).
     * @throws NullPointerException     se la lista dei pianeti è {@code null}
     * @throws IllegalArgumentException se non c'è nessun pianeta non occupato da scegliere.
     * La condizione in cui resta un solo pianeta non occupato è accettabile poiché il
     * giocatore può comunque scegliere SE atterrare.
     */
	public InterazioniUtenteDialog(JFrame parent, Giocatore giocatore, final List<Pianeta> pianeti) {
		
		inizializzaDialog(parent, giocatore);
		
		if (pianeti == null) {
			String errore = "La lista dei pianeti non può essere nulla!";
			LOGGER.error(errore);
			throw new NullPointerException(errore);
		}
		
		if (pianeti.size() == 0) {
			String errore = "La lista di pianeti non può essere vuota!";
			LOGGER.error(errore);
			throw new IllegalArgumentException(errore);
		}
		
		int pianetiLiberi = 0;
		for (Pianeta p : pianeti) {
			if (!p.isOccupato()) {
				pianetiLiberi++;
			}
		}
		
		if (pianetiLiberi == 0) {
			String errore = "Deve esserci almeno UN pianeta non occupato per presentare la scelta!";
			LOGGER.error(errore);
			throw new IllegalArgumentException(errore);
		}
		
		costruisciSceltaPianeta(pianeti);
        dialog.setVisible(true);
	}
	
	/**
     * Costruttore per eventi che richiedono la rimozione di un abitante da una cella specifica.
     * <p>
     * Eventi che lo richiedono: {@link Epidemia}
     *
     * @param parent    finestra principale  {@link JFrame} da cui viene mostrato il dialog.
     * @param giocatore giocatore corrente che deve interagire.
     * @param cella     cella da cui rimuovere l’abitante.
     * @throws NullPointerException se la {@code cella} o la sua tessera sono {@code null}.
     */
	public InterazioniUtenteDialog(JFrame parent, Giocatore giocatore, Cella cella) {
		
		inizializzaDialog(parent, giocatore);
		
		if (cella == null) {
			String errore = "La cella da cui eliminare un abitante non può essere nulla!";
			LOGGER.error(errore);
			throw new NullPointerException(errore);
		}
		
		if (cella.getTessera() == null) {
			String errore = "La tessera da cui eliminare un abitante non può essere nulla!";
			LOGGER.error(errore);
			throw new NullPointerException(errore);
		}
		
		costruisciSceltaAbitanteDaEliminare(cella);
        dialog.setVisible(true);
	}
	
	/**
	 * Inizializza il {@link JDialog} associato alla finestra. 
     * Questo metodo configura dimensioni, posizione, icone della finestra,
     * sfondo e listeners principali.
	 * 
	 * @param parent la finestra principale {@link JFrame}.
	 * @param giocatore il giocatore che deve interagire con la finestra di dialogo.
	 * @throws NullPointerException se {@code giocatore} è {@code null}.
	 */
	private void inizializzaDialog(JFrame parent, Giocatore giocatore) {
		
		if (giocatore == null) {
			String errore = "Il parametro 'giocatore' non può essere nullo!";
			LOGGER.error(errore);
			throw new NullPointerException(errore);
		}
		
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
        
        backgroundImg = new ImageIcon(getClass().getResource(BACKGROUND_IMG_PATH));
        mostraNaveIcon = new ImageIcon(getClass().getResource(MOSTRA_NAVE_ICON_PATH));
        hovMostraNaveIcon = new ImageIcon(getClass().getResource(MOSTRA_NAVE_HOVER_ICON_PATH));
        
        background = new JLabel();
        IconHandler.setIconOnLabel(background, backgroundImg, backgroundImg);
        background.setLayout(null);
        dialog.add(background);
        
        mostraNaveBtn = new JButton();
        
        impostaLookDefaultBottone(mostraNaveBtn);
		IconHandler.setIconOnButton(mostraNaveBtn, mostraNaveIcon, hovMostraNaveIcon);
		mostraNaveBtn.setToolTipText("Clicca se vuoi vedere la tua nave!");
		
		mostraNaveBtn.setBounds(5, 150, 35, 56);
		background.add(mostraNaveBtn);
		
		mostraNaveBtn.addActionListener(e -> {
			new NaveDialog(parent, giocatore);
		});
		
		dialog.revalidate();
        
	}
	
	/**
	 * Costruisce l'interfaccia grafica per un evento che richiede una decisione binaria (Sì/No).
	 * <p>
	 * Viene mostrato un dialogo con due pulsanti: uno per accettare l'evento (Sì),
	 * e uno per rifiutarlo (No).
	 * Il risultato della scelta viene salvato nella variabile 'sceltaSiNo'.
	 */
	private void costruisciSceltaSiNo() {
			
		ImageIcon siButtonIcon = new ImageIcon(getClass().getResource(SI_ICON_PATH));
		ImageIcon hovSiButtonIcon = new ImageIcon(getClass().getResource(SI_HOVER_ICON_PATH));
		ImageIcon noButtonIcon = new ImageIcon(getClass().getResource(NO_ICON_PATH));
		ImageIcon hovNoButtonIcon = new ImageIcon(getClass().getResource(NO_HOVER_ICON_PATH));
		
		titolo = new JLabel("Accetti l'opportunità?");
		sottotitolo = new JLabel("Se rinunci qualcun altro potrebbe farlo al posto tuo...");
		JButton siBtn = new JButton();
		JButton noBtn = new JButton();
		
		titolo.setFont(titolo.getFont().deriveFont(Font.BOLD, 34));
		titolo.setForeground(Color.WHITE);
		titolo.setHorizontalAlignment(SwingConstants.CENTER);
		sottotitolo.setFont(sottotitolo.getFont().deriveFont(Font.ITALIC, 15));
		sottotitolo.setForeground(Color.WHITE);
		sottotitolo.setHorizontalAlignment(SwingConstants.CENTER);
		
		impostaLookDefaultBottone(siBtn);
		IconHandler.setIconOnButton(siBtn, siButtonIcon, hovSiButtonIcon);
		
		impostaLookDefaultBottone(noBtn);
		IconHandler.setIconOnButton(noBtn, noButtonIcon, hovNoButtonIcon);
		
		titolo.setBounds(-5, 10, WIDTH, 50);		
		sottotitolo.setBounds(-5, 60, WIDTH, 40);
		siBtn.setBounds(70, 120, 110, 70);
		noBtn.setBounds(205, 120, 110, 70);
		
		background.add(titolo);
		background.add(sottotitolo);
		background.add(siBtn);
		background.add(noBtn);
		
		siBtn.addActionListener(e -> {
			this.sceltaSiNo = true;
			dialog.dispose();
		});
		
		noBtn.addActionListener(e -> {
			this.sceltaSiNo = false;
			dialog.dispose();
		});
		
	}
	
	/**
	 * Costruisce l'interfaccia grafica per permettere al giocatore di scegliere un pianeta non occupato.
	 * <p>
	 * Vengono creati pulsanti per ciascun pianeta disponibile. Quando il giocatore
	 * seleziona uno di essi, il pianeta viene salvato nella variabile 'pianetaScelto'.
	 *
	 * @param pianeti lista di pianeti tra cui il giocatore può scegliere.
	 *                Si presume che la validità della lista (almeno un pianeta non occupato)
	 *                sia già stata verificata prima della chiamata.
	 */
	private void costruisciSceltaPianeta(List<Pianeta> pianeti) {
		
		ImageIcon siButtonIcon = new ImageIcon(getClass().getResource(SI_ICON_PATH));
		ImageIcon hovSiButtonIcon = new ImageIcon(getClass().getResource(SI_HOVER_ICON_PATH));
		ImageIcon noButtonIcon = new ImageIcon(getClass().getResource(NO_ICON_PATH));
		ImageIcon hovNoButtonIcon = new ImageIcon(getClass().getResource(NO_HOVER_ICON_PATH));
		
		titolo = new JLabel("Vuoi atterrare?");
		sottotitolo = new JLabel("Se sì, scegli il pianeta (numerati dall'alto al basso)!");
		JButton noBtn = new JButton();
		JButton siBtn = new JButton();	
		
		List<String> stringhePianeti = new ArrayList<>();

		for (int i = 0; i < pianeti.size(); i++) {
		    if (pianeti.get(i).isOccupato()) continue;
		    stringhePianeti.add("Pianeta " + (i + 1));
		}
		
		JComboBox<String> box = new JComboBox<>(stringhePianeti.toArray(new String[0]));
		
		titolo.setFont(titolo.getFont().deriveFont(Font.BOLD, 36));
		titolo.setForeground(Color.WHITE);
		titolo.setHorizontalAlignment(SwingConstants.CENTER);
		sottotitolo.setFont(sottotitolo.getFont().deriveFont(Font.ITALIC, 16));
		sottotitolo.setForeground(Color.WHITE);
		sottotitolo.setHorizontalAlignment(SwingConstants.CENTER);
		
		impostaLookDefaultBottone(siBtn);  
		IconHandler.setIconOnButton(siBtn, siButtonIcon, hovSiButtonIcon); 
		
		impostaLookDefaultBottone(noBtn);
		IconHandler.setIconOnButton(noBtn, noButtonIcon, hovNoButtonIcon);
		
		box.setFocusable(false);
		box.setFont(box.getFont().deriveFont(18f));
		
		titolo.setBounds(-5, 10, WIDTH, 50);
		sottotitolo.setBounds(-5, 60, WIDTH, 40);
		siBtn.setBounds(250, 165, 55, 35);
		noBtn.setBounds(320, 165, 55, 35);
		box.setBounds(100, 107, 200, 40);

		background.add(titolo);
		background.add(sottotitolo);
		background.add(siBtn);
		background.add(noBtn);
		background.add(box);
		
		siBtn.addActionListener(e -> {
			this.sceltaPianeta = pianeti.get(Integer.parseInt(String.valueOf(box.getSelectedItem().toString().charAt(8))) - 1);
			dialog.dispose();
		});
		
		noBtn.addActionListener(e -> {
			this.sceltaPianeta = null;
			dialog.dispose();
		});
		
	}

	/**
	 * Costruisce l'interfaccia grafica per permettere al giocatore di eliminare uno degli abitanti
	 * presenti in una cella.
	 * <p>
	 * Viene mostrata una lista di tipi di abitanti (astronauta, alieno marrone, alieno viola), ciascuno associato a un pulsante.
	 * Quando il giocatore sceglie un abitante, questo viene eliminato dalla cabina.
	 *
	 *@see Abitante
	 * @param cella cella contenente la tessera Cabina con gli abitanti tra cui scegliere quello da eliminare.
	 * 
	 */
	private void costruisciSceltaAbitanteDaEliminare(Cella cella) {
		
		dialog.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE); // l'utente è obbligato ad eliminare un abitante
		
		ImageIcon astronautaIcon = new ImageIcon(getClass().getResource(ASTRONAUTA_ICON_PATH));
		ImageIcon hovAstronautaIcon = new ImageIcon(getClass().getResource(ASTRONAUTA_HOVER_ICON_PATH));
		ImageIcon astronautaDisabledIcon = new ImageIcon(getClass().getResource(ASTRONAUTA_DISABLED_ICON_PATH));
		ImageIcon alienoMarroneIcon = new ImageIcon(getClass().getResource(ALIENO_MARRONE_ICON_PATH));
		ImageIcon hovAlienoMarroneIcon = new ImageIcon(getClass().getResource(ALIENO_MARRONE_HOVER_ICON_PATH));
		ImageIcon alienoMarroneDisabledIcon = new ImageIcon(getClass().getResource(ALIENO_MARRONE_DISABLED_ICON_PATH));
		ImageIcon alienoViolaIcon = new ImageIcon(getClass().getResource(ALIENO_VIOLA_ICON_PATH));
		ImageIcon hovAlienoViolaIcon = new ImageIcon(getClass().getResource(ALIENO_VIOLA_HOVER_ICON_PATH));
		ImageIcon alienoViolaDisabledIcon = new ImageIcon(getClass().getResource(ALIENO_VIOLA_DISABLED_ICON_PATH));
		
		Coordinate coordinate = cella.getCoordinateGioco(giocatore.getNave().getLivello());
		titolo = new JLabel("Rimuovi un abitante!");
		sottotitolo = new JLabel("Scegli chi rimuovere dalla cabina alla riga " + coordinate.getRiga() + ", colonna " + coordinate.getColonna()+ "!");
		
		JButton astronautaBtn = new JButton();
		JButton alienoMarroneBtn = new JButton();
		JButton alienoViolaBtn = new JButton();
		
		titolo.setFont(titolo.getFont().deriveFont(Font.BOLD, 34));
		titolo.setForeground(Color.WHITE);
		titolo.setHorizontalAlignment(SwingConstants.CENTER);
		sottotitolo.setFont(sottotitolo.getFont().deriveFont(Font.ITALIC, 15));
		sottotitolo.setForeground(Color.WHITE);
		sottotitolo.setHorizontalAlignment(SwingConstants.CENTER);
		
		impostaLookDefaultBottone(astronautaBtn);
		IconHandler.setIconOnButton(astronautaBtn, astronautaIcon, hovAstronautaIcon);
		astronautaBtn.setToolTipText("Rimuovi un astronauta!");
		astronautaBtn.putClientProperty("enabled", true);
		
		impostaLookDefaultBottone(alienoMarroneBtn);
		IconHandler.setIconOnButton(alienoMarroneBtn, alienoMarroneIcon, hovAlienoMarroneIcon);
		alienoMarroneBtn.setToolTipText("Rimuovi un alieno marrone!");
		alienoMarroneBtn.putClientProperty("enabled", true);
		
		impostaLookDefaultBottone(alienoViolaBtn);    
		IconHandler.setIconOnButton(alienoViolaBtn, alienoViolaIcon, hovAlienoViolaIcon);
		alienoViolaBtn.setToolTipText("Rimuovi un alieno viola!");
		alienoViolaBtn.putClientProperty("enabled", true);
		
		if (cella.getTessera().getNumeroAbitantiPerTipo(Abitante.ASTRONAUTA) == 0) {
			astronautaBtn.putClientProperty("enabled", false);
			IconHandler.setIconOnButton(astronautaBtn, astronautaDisabledIcon, astronautaDisabledIcon);
			astronautaBtn.setToolTipText("Non è presente alcun astronauta viola in questa cabina!");
		}
		
		if (cella.getTessera().getNumeroAbitantiPerTipo(Abitante.ALIENO_MARRONE) == 0) {
			alienoMarroneBtn.putClientProperty("enabled", false);
			IconHandler.setIconOnButton(alienoMarroneBtn, alienoMarroneDisabledIcon, alienoMarroneDisabledIcon);
			alienoMarroneBtn.setToolTipText("Non è presente alcun alieno marrone in questa cabina!");
		}
		
		if (cella.getTessera().getNumeroAbitantiPerTipo(Abitante.ALIENO_VIOLA) == 0) {
			alienoViolaBtn.putClientProperty("enabled", false);
			IconHandler.setIconOnButton(alienoViolaBtn, alienoViolaDisabledIcon, alienoViolaDisabledIcon);
			alienoViolaBtn.setToolTipText("Non è presente alcun alieno viola in questa cabina!");
		}
		
		titolo.setBounds(-5, 10, WIDTH, 50);
		sottotitolo.setBounds(-5, 55, WIDTH, 40);	
		astronautaBtn.setBounds(62, 110, 60, 64);
		alienoMarroneBtn.setBounds(162, 110, 60, 64);
		alienoViolaBtn.setBounds(262, 110, 60, 64);
			
		background.add(titolo);
		background.add(sottotitolo);
		background.add(astronautaBtn);
		background.add(alienoMarroneBtn);
		background.add(alienoViolaBtn);
		
		astronautaBtn.addActionListener(e -> {	
			if (Boolean.FALSE.equals(astronautaBtn.getClientProperty("enabled"))) return;
			
			Cabina c = (Cabina) cella.getTessera(); // casting sicuro grazie ai controlli in precedenza
			try {
				c.setNumeroAstronauti(c.getNumeroAbitantiPerTipo(Abitante.ASTRONAUTA) - 1);
				dialog.dispose();
			} catch (PiazzamentoEquipaggioNonValidoException exception) {
				JOptionPane.showMessageDialog(dialog, exception.getMessage(), "Errore durante l'eliminazione!", JOptionPane.ERROR_MESSAGE);
			}
			
		});
		
		alienoMarroneBtn.addActionListener(e -> {
			if (Boolean.FALSE.equals(alienoMarroneBtn.getClientProperty("enabled"))) return;
			
			Cabina c = (Cabina) cella.getTessera(); // casting sicuro grazie ai controlli in precedenza
			try {
				c.setNumeroAstronauti(c.getNumeroAbitantiPerTipo(Abitante.ALIENO_MARRONE) - 1);
				dialog.dispose();
			} catch (PiazzamentoEquipaggioNonValidoException exception) {
				JOptionPane.showMessageDialog(dialog, exception.getMessage(), "Errore durante l'eliminazione!", JOptionPane.ERROR_MESSAGE);
			}
		});
		
		alienoViolaBtn.addActionListener(e -> {
			if (Boolean.FALSE.equals(alienoViolaBtn.getClientProperty("enabled"))) return;
			
			Cabina c = (Cabina) cella.getTessera(); // casting sicuro grazie ai controlli in precedenza
			try {
				c.setNumeroAstronauti(c.getNumeroAbitantiPerTipo(Abitante.ALIENO_VIOLA) - 1);
				dialog.dispose();
			} catch (PiazzamentoEquipaggioNonValidoException exception) {
				JOptionPane.showMessageDialog(dialog, exception.getMessage(), "Errore durante l'eliminazione!", JOptionPane.ERROR_MESSAGE);
			}
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
	
	// getters per ottenere l'eventuale risultato dell'interazione

	/**
	 * Indica se l'utente ha accettato o meno la proposta dell'evento.
	 * @return {@code true} se ha accettato, {@code false} se ha rifiutato.
	 */
	public boolean getSceltaSiNo() {
		return sceltaSiNo;
	}
	
	/**
	 * Indica il pianeta che è stato scelto dall'utente durante l'interazione.
	 * @return l'oggetto {@code Pianeta} scelto oppure {@code null} se l'utente ha scelto di non atterrare
	 */
	public Pianeta getSceltaPianeta() {
		return sceltaPianeta;
	}

}