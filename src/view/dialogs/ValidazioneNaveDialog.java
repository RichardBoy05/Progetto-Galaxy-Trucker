package galaxytrucker.src.view.dialogs;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.HashMap;
import java.util.Map;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.TitledBorder;
import galaxytrucker.src.logic.assemblaggio.Cella;
import galaxytrucker.src.logic.assemblaggio.Coordinate;
import galaxytrucker.src.logic.assemblaggio.Nave;
import galaxytrucker.src.logic.assemblaggio.Tessera;
import galaxytrucker.src.logic.eccezioni.ConfigurazioneNaveNonValidaException;
import galaxytrucker.src.logic.gioco.Giocatore;
import galaxytrucker.src.view.base.AzioneClickCella;
import galaxytrucker.src.view.base.IconHandler;
import galaxytrucker.src.view.components.PannelloNave;

/**
 * Finestra di dialogo utilizzata per la validazione e la correzione della nave di un giocatore.
 * <p>
 * Questa classe consente al giocatore di rimuovere tessere dalla propria nave in caso di 
 * configurazione non valida e verificarne la correttezza. È utilizzata nei seguenti contesti:
 * <ul>
 *   <li>Preparazione al volo</li>
 *   <li>Evento {@link PioggiaDiMeteoriti}</li>
 *   <li>Evento {@link Pirati}</li>
 *   <li>Evento {@link Sabotaggio}</li>
 * </ul>
 * <p>
 * Le modifiche alla nave non sono applicate immediatamente ma solo al click del tasto "Fine!".
 * In caso di chiusura della finestra, invece, saranno ripristinate le modifiche non confermate.
 * 
 * @see OperazioniNaveDialog
 */
public class ValidazioneNaveDialog extends OperazioniNaveDialog {
	
	// costanti
	
	private static final String DIR_PATH = "/galaxytrucker/resources/images/utente/";
	private static final String VERIFICA_ICON_PATH = DIR_PATH + "verifica_nave.png";
	private static final String HOV_VERIFICA_ICON_PATH = DIR_PATH + "verifica_nave_hover.png";
	
	// componenti e altri attributi
	
	private Nave nave;
	private Map<Coordinate, Tessera> tessereRimosse;
	private ConfigurazioneNaveNonValidaException eccezioneNave;
	private PannelloNave pannelloNave;
	
	private ImageIcon verificaIcon;
	private ImageIcon hovVerificaIcon;
	
	private JLabel titoloLbl;
	private JLabel sottotitoloLbl;
	private JLabel erroreLbl;
	
	private JButton verificaNaveBtn;	
	
	/**
	 * 
	 * Costruisce una GUI che consente di correggere la nave di un giocatore e verificarne la validità.
	 * Da utilizzare nei seguenti contesti:
	 * <li>Preparazione di volo</li>
	 * <li>{@link PioggiaDiMeteoriti}</li>
	 * <li>{@link Pirati}</li>
	 * <li>{@link Sabotaggio}</li>
	 * 
	 * @param parent il {@code JFrame} padre della finestra di dialogo.
	 * @param giocatore il giocatore di cui si vuole validare la nave.
	 */
	public ValidazioneNaveDialog(JFrame parent, Giocatore giocatore, ConfigurazioneNaveNonValidaException eccezioneNave) {
		super(parent, giocatore);
		this.nave = getGiocatore().getNave();
		this.eccezioneNave = eccezioneNave;
		this.tessereRimosse = new HashMap<>();
		
		pannelloNave = new PannelloNave(giocatore.getNave(), true, new AzioneClickCella() {
	    	@Override
            public void onClickCella(int riga, int colonna, MouseEvent evento) {
                if (SwingUtilities.isLeftMouseButton(evento) && !evento.isShiftDown()) {
                	
                	Cella c = giocatore.getNave().getGriglia().getCella(riga, colonna);
                	if (c.getTessera() == null) return;
                	Tessera tesseraRimossa = c.rimuoviTessera();
                	tessereRimosse.put(new Coordinate(c.getRiga(), c.getColonna()), tesseraRimossa);
                	IconHandler.clearIconsFromLabel(pannelloNave.getLabelCella(riga, colonna));
                    
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
		
		getDialog().revalidate();
		getDialog().setVisible(true);
	}

	/**
	 * Inizializza i componenti dell'interfaccia grafica, come etichette, pulsanti e messaggi
	 * di errore, impostando anche icone e stili grafici.
	 */
	@Override
	public void setupComponents() {
		
		verificaIcon = new ImageIcon(getClass().getResource(VERIFICA_ICON_PATH));
		hovVerificaIcon = new ImageIcon(getClass().getResource(HOV_VERIFICA_ICON_PATH));
		titoloLbl = new JLabel("Validazione Nave");
		sottotitoloLbl = new JLabel("<html><div style='text-align:justify;'>"
			    + "La tua nave ha dei problemi da sistemare, perciò deve "
			    + "essere corretta.<br>"
			    + "Clicca su una cella per rimuoverne la tessera oppure "
			    + "fai <i>shift + click</i> per vederne le caratteristiche.<br>"
			    + "Per annullare le modifiche, chiudi con la <b>X</b>. "
			    + "Clicca su \"<i>Fine</i>\" per confermare, ma <b>ATTENZIONE</b>: "
			    + "non potrai più annullare le modifiche effettuate!"
			    + "</div></html>");



		erroreLbl = new JLabel(eccezioneNave.getMessage());
		verificaNaveBtn = new JButton();
		
		impostaLookDefaultBottone(verificaNaveBtn);
		IconHandler.setIconOnButton(verificaNaveBtn, verificaIcon, hovVerificaIcon);
		
		titoloLbl.setForeground(Color.WHITE);
		titoloLbl.setFont(titoloLbl.getFont().deriveFont(Font.BOLD, 28));
		titoloLbl.setHorizontalAlignment(SwingConstants.CENTER);
		sottotitoloLbl.setForeground(Color.WHITE);
		sottotitoloLbl.setFont(sottotitoloLbl.getFont().deriveFont(Font.BOLD, 12));
		sottotitoloLbl.setHorizontalAlignment(SwingConstants.CENTER);
		
		TitledBorder errorBorder = BorderFactory.createTitledBorder("Esito validazione");
		errorBorder.setTitleJustification(TitledBorder.LEFT);
		errorBorder.setTitlePosition(TitledBorder.ABOVE_TOP);
		errorBorder.setTitleColor(Color.WHITE);
		
		erroreLbl.setText("<html><div style='text-align:center;'>"
			    + eccezioneNave.getMessage()
			    + "</div></html>");
		
		erroreLbl.setBorder(errorBorder);
		erroreLbl.setForeground(Color.RED);
		erroreLbl.setFont(erroreLbl.getFont().deriveFont(Font.BOLD, 16));
		erroreLbl.setHorizontalAlignment(SwingConstants.CENTER);
		
	}

	/**
	 * Definisce la posizione assoluta dei componenti all'interno del pannello.
	 */
	@Override
	public void setupLayout() {
		
		titoloLbl.setBounds(723, 0, 270, 60);
		sottotitoloLbl.setBounds(740, 30, 235, 200);
		erroreLbl.setBounds(738, 230, 245, 100);
		verificaNaveBtn.setBounds(760, 370, 195, 60);
		
		getBackground().add(titoloLbl);
		getBackground().add(sottotitoloLbl);
		getBackground().add(erroreLbl);
		getBackground().add(verificaNaveBtn);
	
	}

	/**
	 * Registra i listener per l'interazione con i componenti della finestra,
	 * come la gestione dei click sulle celle e il comportamento del pulsante
	 * di verifica della nave. Gestisce inoltre il ripristino delle modifiche
	 * in caso di chiusura della finestra.
	 */
	@Override
	public void setupListeners() {
		
		verificaNaveBtn.addActionListener(e -> {
			
			try {
				
				nave.verificaNave();
				erroreLbl.setText("<html><div style='text-align:center;'>"
					    + "Nave corretta!<br> Puoi proseguire!"
					    + "</div></html>");
				erroreLbl.setForeground(Color.GREEN);
				eccezioneNave = null;
				
			} catch (ConfigurazioneNaveNonValidaException exception) {
				eccezioneNave = exception;
				erroreLbl.setForeground(Color.RED);
				erroreLbl.setText("<html><div style='text-align:center;'>"
					    + eccezioneNave.getMessage()
					    + "</div></html>");
			}
		});
		
		getDialog().addWindowListener(new WindowAdapter() { 
		    @Override
		    public void windowClosing(WindowEvent e) { // ripristino le modifiche apportate alla nave
		    	
		    	for (Coordinate c : tessereRimosse.keySet()) {
					nave.getGriglia().getCella(c.getRiga(), c.getColonna()).inserisciTessera(tessereRimosse.get(c));
				}
		    	
		    	if (Boolean.TRUE.equals(((JFrame) getDialog().getOwner()).getRootPane().getClientProperty("placeholder"))) {
		            ((JFrame) getDialog().getOwner()).dispose();
		        }
		    	
		    }});
	
	}
	
	/**
	 * Operazioni finali da eseguire alla chiusura della finestra (solo tramite il tasto "Fine!", 
	 * che funge da conferma per le modifiche effettuate).
	 * <p>
	 * In particolare vengono aggiunti debiti al giocatore per ogni tessera rimossa e vengono
	 * eliminati eventuali alieni non più supportati dalla nave a causa delle modifiche.
	 */
	@Override
	protected void operazioniFinali() {
		
		getGiocatore().aggiungiDebiti(tessereRimosse.keySet().size()); // ogni tessere rimossa costituisce 1 debito
		getGiocatore().getNave().rimuoviAlieniNonPiuSupportati(); // rimuove eventuali alieni che hanno perso il proprio supporto vitale
	}

}