package galaxytrucker.src.view.dialogs;

import java.awt.Color;
import java.awt.Font;
import java.awt.Rectangle;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.util.EnumMap;
import java.util.Map;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import galaxytrucker.src.logic.assemblaggio.Stiva;
import galaxytrucker.src.logic.assemblaggio.Tessera;
import galaxytrucker.src.logic.eccezioni.CaricamentoMerceNonValidoException;
import galaxytrucker.src.logic.gioco.Giocatore;
import galaxytrucker.src.logic.volo.Contrabbandieri;
import galaxytrucker.src.logic.volo.Merce;
import galaxytrucker.src.logic.volo.Pianeti;
import galaxytrucker.src.logic.volo.StazioneAbbandonata;
import galaxytrucker.src.view.base.AzioneClickCella;
import galaxytrucker.src.view.base.IconHandler;
import galaxytrucker.src.view.components.PannelloNave;

/**
 * Finestra di dialogo che consente al giocatore di gestire
 * il carico e lo scarico delle merci dalla propria nave durante
 * eventi specifici del gioco.
 * <p>
 * Questa finestra è utilizzata per eventi come {@link Pianeti},
 * {@link StazioneAbbandonata} e {@link Contrabbandieri}, permettendo
 * di selezionare un tipo di merce e posizionarla nelle stive disponibili
 * o rimuoverla.
 *
 * @see OperazioniNaveDialog
 */
public class CaricoMerciDialog extends OperazioniNaveDialog {
	
	// costanti
	
	private static final String DIR_PATH = "/galaxytrucker/resources/images/utente/";
	private static final String MERCE_ROSSA_ICON_PATH = DIR_PATH + "merce_rossa.png";
	private static final String MERCE_BLU_ICON_PATH = DIR_PATH + "merce_blu.png";
	private static final String MERCE_GIALLA_ICON_PATH = DIR_PATH + "merce_gialla.png";
	private static final String MERCE_VERDE_ICON_PATH = DIR_PATH + "merce_verde.png";
	private static final String HOV_MERCE_ROSSA_ICON_PATH = DIR_PATH + "merce_rossa_hover.png";
	private static final String HOV_MERCE_BLU_ICON_PATH = DIR_PATH + "merce_blu_hover.png";
	private static final String HOV_MERCE_GIALLA_ICON_PATH = DIR_PATH + "merce_gialla_hover.png";
	private static final String HOV_MERCE_VERDE_ICON_PATH = DIR_PATH + "merce_verde_hover.png";
	
	private static final int MERCE_BTN_DEFAULT_SIZE = 45;
	private static final int MERCE_BTN_EXPANSION = 12;
	
	// componenti
	
	private PannelloNave pannelloNave;
	
	private ImageIcon merceRossaIcon;
	private ImageIcon hovMerceRossaIcon;
	private ImageIcon merceBluIcon;
	private ImageIcon hovMerceBluIcon;
	private ImageIcon merceGiallaIcon;
	private ImageIcon hovMerceGiallaIcon;
	private ImageIcon merceVerdeIcon;
	private ImageIcon hovMerceVerdeIcon;
	
	private JButton merceRossaBtn;
	private JButton merceBluBtn;
	private JButton merceGiallaBtn;
	private JButton merceVerdeBtn;
	
	private JLabel titoloLbl;
	private JLabel sottotitoloLbl;
	private JLabel contatoreMerceRossaLbl;
	private JLabel contatoreMerceBluLbl;
	private JLabel contatoreMerceGiallaLbl;
	private JLabel contatoreMerceVerdeLbl;
	
	// altri attributi
	
	private Map<Merce, Integer> merci;
	private Merce merceSelezionata;

	/**
	 * 
	 * Costruisce l'interfaccia grafica che consente al giocatore di
	 * gestire il carico e lo scarico delle merci dalla propria nave.
	 * 
	 * Eventi interessati: {@link Pianeti}, {@link StazioneAbbandonata}, {@link Contrabbandieri},
	 * 
	 * @param parent il {@code JFrame} padre della finestra di dialogo.
	 * @param giocatore il giocatore che deve effettuare operazioni con le merci.
	 * @param merci.
	 * @throws NullPointerException se almeno uno dei tipi di riferimento passati come parametro è {@code null}.
	 */
	public CaricoMerciDialog(JFrame parent, Giocatore giocatore, Map<Merce, Integer> merci) {
		super(parent, giocatore);
		
		if (merci == null) {
			String errore = "Il parametro 'merci' non può essere nullo!";
			getLogger().error(errore);
			throw new NullPointerException(errore);
		}
		
		this.merci = new EnumMap<>(merci); // evito modifiche al parametro dell'evento
		merceSelezionata = null;
		
		pannelloNave = new PannelloNave(giocatore.getNave(), true, new AzioneClickCella() {
	    	@Override
            public void onClickCella(int riga, int colonna, MouseEvent evento) {
	    		
                if (SwingUtilities.isLeftMouseButton(evento) && !evento.isShiftDown()) {          	
                	logicaClickSinistro(riga, colonna);
                    
                } else if (SwingUtilities.isRightMouseButton(evento)) {
                	logicaClickDestro(riga, colonna);
                    
                } else if (SwingUtilities.isLeftMouseButton(evento) && evento.isShiftDown()) {
                	Tessera t = giocatore.getNave().getGriglia().getCella(riga, colonna).getTessera();
                    String message = (t == null) ? "Questa cella non contiene alcuna tessera!" : t.toString();          
                    JOptionPane.showMessageDialog(getDialog(), message, "Informazioni sulla tessera", JOptionPane.INFORMATION_MESSAGE);
                }
               
            } 
        });
		
		posizionaPannelloNave(pannelloNave);

		setupComponents();
		setupLayout();
		setupListeners();
        
        getDialog().setVisible(true);
        getDialog().revalidate();
	}

	/**
     * Inizializza i componenti grafici dell'interfaccia,
     * inclusi bottoni, etichette e icone delle merci.
     */
	@Override
	public void setupComponents() {
		
		merceRossaIcon = new ImageIcon(getClass().getResource(MERCE_ROSSA_ICON_PATH));
		hovMerceRossaIcon = new ImageIcon(getClass().getResource(HOV_MERCE_ROSSA_ICON_PATH));
		merceBluIcon = new ImageIcon(getClass().getResource(MERCE_BLU_ICON_PATH));
		hovMerceBluIcon = new ImageIcon(getClass().getResource(HOV_MERCE_BLU_ICON_PATH));
		merceGiallaIcon = new ImageIcon(getClass().getResource(MERCE_GIALLA_ICON_PATH));
		hovMerceGiallaIcon = new ImageIcon(getClass().getResource(HOV_MERCE_GIALLA_ICON_PATH));
		merceVerdeIcon = new ImageIcon(getClass().getResource(MERCE_VERDE_ICON_PATH));
		hovMerceVerdeIcon = new ImageIcon(getClass().getResource(HOV_MERCE_VERDE_ICON_PATH));
		
		merceRossaBtn = new JButton();
		merceBluBtn = new JButton();
		merceGiallaBtn = new JButton();
		merceVerdeBtn = new JButton();

		titoloLbl = new JLabel("Carico della merce");
		sottotitoloLbl = new JLabel("<html><div style='text-align:justify;'>"
			    + "In questa sezione puoi gestire le operazioni di "
			    + "carico e scarico delle merci. La merce che non posizionerai sulla nave "
			    + "(per scelta o per spazio) andrà dispersa nella galassia. <br>"
			    + "Clicca su uno dei tipi di merce qui sotto per selezionarla. "
			    + "Usa il tasto <i>sinistro</i> per posizionare la merce in una stiva, "
			    + "il <i>destro</i> per prelevarla.<br>"
			    + "Infine, fai <i>shift + click</i> se vuoi visualizzare i dettagli "
			    + "di una qualsiasi tessera.<br>"
			    + "</div></html>");
		
		contatoreMerceRossaLbl = new JLabel(String.valueOf(merci.get(Merce.ROSSA)));
		contatoreMerceBluLbl = new JLabel(String.valueOf(merci.get(Merce.BLU)));
		contatoreMerceGiallaLbl = new JLabel(String.valueOf(merci.get(Merce.GIALLA)));
		contatoreMerceVerdeLbl = new JLabel(String.valueOf(merci.get(Merce.VERDE)));
		
		titoloLbl.setForeground(Color.WHITE);
		titoloLbl.setFont(titoloLbl.getFont().deriveFont(Font.BOLD, 26));
		sottotitoloLbl.setForeground(Color.WHITE);
		sottotitoloLbl.setFont(sottotitoloLbl.getFont().deriveFont(Font.BOLD, 14));

		contatoreMerceRossaLbl.setForeground(Color.WHITE);
		contatoreMerceRossaLbl.setFont(contatoreMerceRossaLbl.getFont().deriveFont(Font.BOLD, 24));
		contatoreMerceBluLbl.setForeground(Color.WHITE);
		contatoreMerceBluLbl.setFont(contatoreMerceBluLbl.getFont().deriveFont(Font.BOLD, 24));
		contatoreMerceGiallaLbl.setForeground(Color.WHITE);
		contatoreMerceGiallaLbl.setFont(contatoreMerceGiallaLbl.getFont().deriveFont(Font.BOLD, 24));
		contatoreMerceVerdeLbl.setForeground(Color.WHITE);
		contatoreMerceVerdeLbl.setFont(contatoreMerceVerdeLbl.getFont().deriveFont(Font.BOLD, 24));

		titoloLbl.setHorizontalAlignment(SwingConstants.CENTER);
		sottotitoloLbl.setHorizontalAlignment(SwingConstants.CENTER);
		contatoreMerceRossaLbl.setHorizontalAlignment(SwingConstants.CENTER);
		contatoreMerceBluLbl.setHorizontalAlignment(SwingConstants.CENTER);
		contatoreMerceGiallaLbl.setHorizontalAlignment(SwingConstants.CENTER);
		contatoreMerceVerdeLbl.setHorizontalAlignment(SwingConstants.CENTER);
		
		IconHandler.setIconOnButton(merceRossaBtn, merceRossaIcon, hovMerceRossaIcon);
		IconHandler.setIconOnButton(merceBluBtn, merceBluIcon, hovMerceBluIcon);
		IconHandler.setIconOnButton(merceGiallaBtn, merceGiallaIcon, hovMerceGiallaIcon);
		IconHandler.setIconOnButton(merceVerdeBtn, merceVerdeIcon, hovMerceVerdeIcon);
		
		impostaLookDefaultBottone(merceRossaBtn);
		impostaLookDefaultBottone(merceBluBtn);
		impostaLookDefaultBottone(merceGiallaBtn);
		impostaLookDefaultBottone(merceVerdeBtn);
		
		merceRossaBtn.putClientProperty("selezionato", false);
		merceBluBtn.putClientProperty("selezionato", false);
		merceGiallaBtn.putClientProperty("selezionato", false);
		merceVerdeBtn.putClientProperty("selezionato", false);
		
	}

	/**
     * Posiziona i componenti grafici all'interno del
     * pannello di sfondo utilizzando il layout assoluto.
     */
	@Override
	public void setupLayout() {
		
		titoloLbl.setBounds(723, 0, 270, 60);
		sottotitoloLbl.setBounds(740, 40, 235, 300);
		merceRossaBtn.setBounds(745, 350, MERCE_BTN_DEFAULT_SIZE, MERCE_BTN_DEFAULT_SIZE);
		merceBluBtn.setBounds(805, 350, MERCE_BTN_DEFAULT_SIZE, MERCE_BTN_DEFAULT_SIZE);
		merceGiallaBtn.setBounds(865, 350, MERCE_BTN_DEFAULT_SIZE, MERCE_BTN_DEFAULT_SIZE);
		merceVerdeBtn.setBounds(925, 350, MERCE_BTN_DEFAULT_SIZE, MERCE_BTN_DEFAULT_SIZE);
		contatoreMerceRossaLbl.setBounds(745, 400, 50, 50);
		contatoreMerceBluLbl.setBounds(805, 400, 50, 50);
		contatoreMerceGiallaLbl.setBounds(865, 400, 50, 50);
		contatoreMerceVerdeLbl.setBounds(925, 400, 50, 50);
		
		getBackground().add(titoloLbl);
		getBackground().add(sottotitoloLbl);
		getBackground().add(merceRossaBtn);
		getBackground().add(merceBluBtn);
		getBackground().add(merceGiallaBtn);
		getBackground().add(merceVerdeBtn);
		getBackground().add(contatoreMerceRossaLbl);
		getBackground().add(contatoreMerceBluLbl);
		getBackground().add(contatoreMerceGiallaLbl);
		getBackground().add(contatoreMerceVerdeLbl);
		
	}

	/**
     * Aggiunge i listener ai bottoni delle merci.
     * I bottoni gestiscono selezione/deselezione visiva
     * e attivano il tipo di merce selezionato.
     */
	@Override
	public void setupListeners() {

		ActionListener selezionaMerceListener = e -> {
		    JButton clickedBtn = (JButton) e.getSource();

		    if (Boolean.TRUE.equals(clickedBtn.getClientProperty("selezionato"))) {
		        
		    	/// deseleziona e ripristina la dimensione
		        clickedBtn.putClientProperty("selezionato", false);
		        merceSelezionata = null;

		        Rectangle bounds = clickedBtn.getBounds();
		        int centerX = bounds.x + bounds.width / 2;
		        int centerY = bounds.y + bounds.height / 2;

		        int newX = centerX - MERCE_BTN_DEFAULT_SIZE / 2;
		        int newY = centerY - MERCE_BTN_DEFAULT_SIZE / 2;

		        clickedBtn.setBounds(newX, newY, MERCE_BTN_DEFAULT_SIZE, MERCE_BTN_DEFAULT_SIZE);
		        return;
		    }

		    JButton[] bottoniMerce = {merceRossaBtn, merceBluBtn, merceGiallaBtn, merceVerdeBtn};

		    for (int i = 0; i < bottoniMerce.length; i++) {
		        if (bottoniMerce[i] == clickedBtn) {
		        	
		        	switch (i) {
		        	case 0:
		        		merceSelezionata = Merce.ROSSA;
		        		break;
		        	case 1:
		        		merceSelezionata = Merce.BLU;
		        		break;
		        	case 2:
		        		merceSelezionata = Merce.GIALLA;
		        		break;
		        	case 3:
		        		merceSelezionata = Merce.VERDE;
		        		break;
		        	default:
		        		String errore = "Errore in fase di selezione della merce!";
		        		getLogger().error(errore);
		        		JOptionPane.showInternalMessageDialog(getDialog(), errore, "Errore nel codice!", JOptionPane.ERROR_MESSAGE);
		        		return;
		        	}
		        	
		        	continue;
		        }

		        // deseleziona e ripristina la dimensione
		        bottoniMerce[i].putClientProperty("selezionato", false);
		        Rectangle bounds = bottoniMerce[i].getBounds();
		        int centerX = bounds.x + bounds.width / 2;
		        int centerY = bounds.y + bounds.height / 2;

		        int newX = centerX - MERCE_BTN_DEFAULT_SIZE / 2;
		        int newY = centerY - MERCE_BTN_DEFAULT_SIZE / 2;

		        bottoniMerce[i].setBounds(newX, newY, MERCE_BTN_DEFAULT_SIZE, MERCE_BTN_DEFAULT_SIZE);
		    }

		    // seleziona il bottone cliccato e lo ingrandisce
		    clickedBtn.putClientProperty("selezionato", true);

		    Rectangle bounds = clickedBtn.getBounds();
		    int centerX = bounds.x + bounds.width / 2;
		    int centerY = bounds.y + bounds.height / 2;

		    int newX = centerX - (MERCE_BTN_DEFAULT_SIZE + MERCE_BTN_EXPANSION) / 2;
		    int newY = centerY - (MERCE_BTN_DEFAULT_SIZE + MERCE_BTN_EXPANSION) / 2;

		    clickedBtn.setBounds(newX, newY, MERCE_BTN_DEFAULT_SIZE + MERCE_BTN_EXPANSION, MERCE_BTN_DEFAULT_SIZE + MERCE_BTN_EXPANSION);
		};
	
		merceRossaBtn.addActionListener(selezionaMerceListener);
		merceBluBtn.addActionListener(selezionaMerceListener);
		merceGiallaBtn.addActionListener(selezionaMerceListener);
		merceVerdeBtn.addActionListener(selezionaMerceListener);

	}
	
	/**
     * Verifica se la tessera passata è valida per il caricamento merce.
     * Mostra eventuali messaggi di errore all'utente in caso negativo.
     *
     * @param tessera la tessera selezionata.
     * @return {@code true} se la tessera può accettare della merce ed 
     * un tipo di merce è stato selezionato, altrimenti {@code false}.
     */
	private boolean isTesseraValida(Tessera tessera) {
		
		if (tessera == null) {
			JOptionPane.showMessageDialog(getDialog(), "Questa cella non contiene alcuna tessera!", "Errore nel caricamento della merce", JOptionPane.ERROR_MESSAGE);
			return false;
		}
		
		if (!tessera.accettaMerce()) {
			JOptionPane.showMessageDialog(getDialog(), "Questa tessera non è una stiva!", "Errore nel caricamento della merce", JOptionPane.ERROR_MESSAGE);
			return false;
		}
		
		if (merceSelezionata == null) {
			JOptionPane.showMessageDialog(getDialog(), "Prima devi selezionare un tipo di merce!", "Errore nel caricamento della merce", JOptionPane.ERROR_MESSAGE);
			return false;
		}
		
		return true;
	}
	
	/**
     * Logica eseguita quando l'utente clicca con il tasto sinistro
     * del mouse su una cella: carica una merce nella stiva selezionata.
     *
     * @param riga la riga della cella cliccata.
     * @param colonna la colonna della cella cliccata.
     */
	private void logicaClickSinistro(int riga, int colonna) {
		
		Tessera t = getGiocatore().getNave().getGriglia().getCella(riga, colonna).getTessera();	
		if (!isTesseraValida(t)) return;
		
		if (merci.get(merceSelezionata) == 0) {
			JOptionPane.showMessageDialog(getDialog(), "Non puoi caricare della merce " + merceSelezionata.toString().toLowerCase() + " perché non ne possiedi alcuna!", "Errore nel caricamento della merce", JOptionPane.ERROR_MESSAGE);
			return;
		}
		
		Stiva stiva = (Stiva) t; // casting sicuro grazie ai controlli in precedenza
		
		try {			
			stiva.setMerce(merceSelezionata, stiva.getMerce(merceSelezionata) + 1);		
			merci.put(merceSelezionata, merci.get(merceSelezionata) - 1);
			aggiornaContatori();
			
		} catch (CaricamentoMerceNonValidoException e) {
			
			JOptionPane.showMessageDialog(getDialog(), e.getMessage(), "Errore nel caricamento della merce", JOptionPane.ERROR_MESSAGE);
			return;
			
		}
	}
	
	/**
     * Logica eseguita quando l'utente clicca con il tasto destro
     * del mouse su una cella: scarica una merce dalla stiva selezionata.
     *
     * @param riga la riga della cella cliccata.
     * @param colonna la colonna della cella cliccata.
     */
	private void logicaClickDestro(int riga, int colonna) {
		
		Tessera t = getGiocatore().getNave().getGriglia().getCella(riga, colonna).getTessera();
		if (!isTesseraValida(t)) return;
		
		Stiva stiva = (Stiva) t; // casting sicuro grazie ai controlli in precedenza
		
		try {			
			stiva.setMerce(merceSelezionata, stiva.getMerce(merceSelezionata) - 1);		
			merci.put(merceSelezionata, merci.get(merceSelezionata) + 1);
			aggiornaContatori();
			
		} catch (CaricamentoMerceNonValidoException e) {
			
			JOptionPane.showMessageDialog(getDialog(), e.getMessage(), "Errore nel caricamento della merce", JOptionPane.ERROR_MESSAGE);
			return;
			
		}
		
	}

	/**
	 * Aggiorna le etichette che riportano visivamente i contatori delle merci.
	 */
	private void aggiornaContatori() {
		contatoreMerceRossaLbl.setText(String.valueOf(merci.get(Merce.ROSSA)));
		contatoreMerceBluLbl.setText(String.valueOf(merci.get(Merce.BLU)));
		contatoreMerceGiallaLbl.setText(String.valueOf(merci.get(Merce.GIALLA)));
		contatoreMerceVerdeLbl.setText(String.valueOf(merci.get(Merce.VERDE)));
	}

}