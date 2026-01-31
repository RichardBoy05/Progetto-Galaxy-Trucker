package galaxytrucker.src.view.dialogs;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.MouseEvent;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.LineBorder;
import galaxytrucker.src.logic.assemblaggio.Attivabile;
import galaxytrucker.src.logic.assemblaggio.Tessera;
import galaxytrucker.src.logic.assemblaggio.VanoBatteria;
import galaxytrucker.src.logic.eccezioni.RichiestaBatterieNonValidaException;
import galaxytrucker.src.logic.gioco.Giocatore;
import galaxytrucker.src.logic.volo.Contrabbandieri;
import galaxytrucker.src.logic.volo.PioggiaDiMeteoriti;
import galaxytrucker.src.logic.volo.Pirati;
import galaxytrucker.src.logic.volo.Schiavisti;
import galaxytrucker.src.logic.volo.SpazioAperto;
import galaxytrucker.src.view.base.AzioneClickCella;
import galaxytrucker.src.view.base.IconHandler;
import galaxytrucker.src.view.components.PannelloNave;

/**
 * Finestra di dialogo che consente al giocatore di gestire l'utilizzo delle batterie
 * a bordo della propria nave durante eventi che richiedono l'attivazione di componenti.
 * Viene mostrata solo se il giocatore possiede almeno una batteria e almeno un componente attivabile.
 * <p>
 * L'interfaccia permette di prelevare batterie dai vani appositi e usarle per attivare
 * cannoni doppi, motori doppi e scudi.
 * <p>
 * Eventi che utilizzano questa finestra:
 * {@link Contrabbandieri}, {@link PioggiaDiMeteoriti},
 * {@link SpazioAperto}, {@link Pirati}, {@link Schiavisti}.
 *
 * @see OperazioniNaveDialog
 */
public class UsoBatterieDialog extends OperazioniNaveDialog {
	
	// costanti
	
	private static final String DIR_PATH = "/galaxytrucker/resources/images/utente/";
	private static final String BATTERIA_ICON_PATH = DIR_PATH + "batteria.png";
	private static final String MOTORE_ICON_PATH = DIR_PATH + "motore.png";
	private static final String CANNONE_ICON_PATH = DIR_PATH + "cannone.png";
	private static final String SCUDO_ICON_PATH = DIR_PATH + "scudo.png";
	
	// componenti e altri attributi
	
	private PannelloNave pannelloNave;
	
	private ImageIcon batteriaIcon;
	private ImageIcon motoreIcon;
	private ImageIcon cannoneIcon;
	private ImageIcon scudoIcon;
	
	private JLabel titoloLbl;
	private JLabel sottotitoloLbl;
	private JLabel batteriaLbl;
	private JLabel motoreLbl;
	private JLabel cannoneLbl;
	private JLabel scudoLbl;
	private JLabel contaBatterieLbl;
	private JLabel tessereAttivabiliLbl;

	private int contatoreBatteriePrelevate;

	/**
	 * Costruisce l'interfaccia grafica per la scelta delle batterie per l'attivazione
	 * di determinati componenti della nave.
	 * 
	 * Prima di creare l'interfaccia, verifica che il giocatore in questione abbia almeno
	 * una batteria ed un componente attivabile.
	 * 
	 * Eventi interessati: {@link Contrabbandieri}, {@link PioggiaDiMeteoriti},
	 * {@link SpazioAperto}, {@link Pirati}, {@link Schiavisti}.
	 * @param parent il {@code JFrame} padre della finestra di dialogo.
	 * @param giocatore il giocatore che effettua la scelta di utilizzo batterie
	 * 
	 * @throws NullPointerException se {@code giocatore} è {@code null}.
	 * @throws IllegalArgumentException se {@code giocatore} è {@code null}.
	 */
	public UsoBatterieDialog(JFrame parent, Giocatore giocatore) {
		super(parent, giocatore);
		
        if (giocatore.getNave().getNumeroBatterie() == 0 || !giocatore.getNave().haComponentiAttivabili()) {
        	String errore = "Il giocatore deve possedere almeno una batteria ed un componente attivabile!";
        	getLogger().error(errore);
        	throw new IllegalArgumentException(errore);
        }
        
		contatoreBatteriePrelevate = 0;
        
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
	 * Inizializza i componenti grafici dell'interfaccia, come etichette, icone e testi.
	 * Imposta inoltre gli stili visivi (font, colore, allineamento) e i tooltip informativi.
	 */
	@Override
	public void setupComponents() {
		
		batteriaIcon = new ImageIcon(getClass().getResource(BATTERIA_ICON_PATH));
		motoreIcon = new ImageIcon(getClass().getResource(MOTORE_ICON_PATH));
		cannoneIcon = new ImageIcon(getClass().getResource(CANNONE_ICON_PATH));
		scudoIcon = new ImageIcon(getClass().getResource(SCUDO_ICON_PATH));

		titoloLbl = new JLabel("Utilizzo batterie");
		sottotitoloLbl = new JLabel("<html><div style='text-align:justify;'>"
			    + "In questa sezione puoi prelevare le batterie e scegliere "
			    + "se attivare le tessere che le richiedono.<br>"
			    + "Usa il tasto <i>sinistro</i> per prelevare delle batterie dai vani appositi, "
			    + "il <i>destro</i> per riposizionarle al loro posto.<br>"
			    + "Usa il tasto <i>sinistro</i> per attivare una delle tessere attivabili, "
			    + "il <i>destro</i> se scegli di disattivarle.<br>"
			    + "Infine, fai <i>shift + click</i> se vuoi visualizzare i dettagli "
			    + "di una qualsiasi tessera.<br>"
			    + "</div></html>");
		
		batteriaLbl = new JLabel();
		motoreLbl = new JLabel();
		cannoneLbl = new JLabel();
		scudoLbl = new JLabel();
		contaBatterieLbl = new JLabel(String.valueOf(contatoreBatteriePrelevate));
		tessereAttivabiliLbl = new JLabel("Tessere attivabili");

		titoloLbl.setForeground(Color.WHITE);
		titoloLbl.setFont(titoloLbl.getFont().deriveFont(Font.BOLD, 28));
		sottotitoloLbl.setForeground(Color.WHITE);
		sottotitoloLbl.setFont(sottotitoloLbl.getFont().deriveFont(Font.BOLD, 12));
		contaBatterieLbl.setForeground(Color.WHITE);
		contaBatterieLbl.setFont(contaBatterieLbl.getFont().deriveFont(Font.BOLD, 54));
		tessereAttivabiliLbl.setForeground(Color.WHITE);
		tessereAttivabiliLbl.setFont(tessereAttivabiliLbl.getFont().deriveFont(Font.BOLD, 16));
		
		IconHandler.setIconOnLabel(batteriaLbl, batteriaIcon, batteriaIcon);
		IconHandler.setIconOnLabel(motoreLbl, motoreIcon, motoreIcon);
		IconHandler.setIconOnLabel(cannoneLbl, cannoneIcon, cannoneIcon);
		IconHandler.setIconOnLabel(scudoLbl, scudoIcon, scudoIcon);

		titoloLbl.setHorizontalAlignment(SwingConstants.CENTER);
		sottotitoloLbl.setHorizontalAlignment(SwingConstants.CENTER);
		batteriaLbl.setHorizontalAlignment(SwingConstants.CENTER);
		motoreLbl.setHorizontalAlignment(SwingConstants.CENTER);
		cannoneLbl.setHorizontalAlignment(SwingConstants.CENTER);
		scudoLbl.setHorizontalAlignment(SwingConstants.CENTER);
		contaBatterieLbl.setHorizontalAlignment(SwingConstants.CENTER);
		tessereAttivabiliLbl.setHorizontalAlignment(SwingConstants.CENTER);
		
		batteriaLbl.setToolTipText("Batterie prelevate");
		contaBatterieLbl.setToolTipText("Batterie prelevate");
		cannoneLbl.setToolTipText("Cannone doppio");
		motoreLbl.setToolTipText("Motore doppio");
		scudoLbl.setToolTipText("Scudo");
		
	}

	/**
	 * Definisce il layout e le posizioni assolute dei componenti grafici
	 * all'interno della finestra di dialogo.
	 */
	@Override
	public void setupLayout() {
		
		titoloLbl.setBounds(723, 0, 270, 60);
		sottotitoloLbl.setBounds(740, 45, 235, 200);
		batteriaLbl.setBounds(765, 252, 105, 90);
		contaBatterieLbl.setBounds(870, 250, 70, 70);
		tessereAttivabiliLbl.setBounds(750, 340, 200, 50);
		motoreLbl.setBounds(740, 385, 70, 60);
		cannoneLbl.setBounds(817, 385, 87, 60);
		scudoLbl.setBounds(910, 385, 63, 60);
	
		getBackground().add(titoloLbl);
		getBackground().add(sottotitoloLbl);
		getBackground().add(batteriaLbl);
		getBackground().add(motoreLbl);
		getBackground().add(cannoneLbl);
		getBackground().add(scudoLbl);
		getBackground().add(contaBatterieLbl);
		getBackground().add(tessereAttivabiliLbl);

	}

	/**
	 * Registra eventuali listener. In questa implementazione non ne vengono aggiunti.
	 */
	@Override
	public void setupListeners() {
		// nessun listener da settare
	}
	
	/**
	 * Logica di gestione del click sinistro del mouse su una cella della nave.
	 * <p>
	 * Se la cella contiene un {@link VanoBatteria}, preleva una batteria (se disponibile).
	 * Se la cella contiene un componente {@link Attivabile}, lo attiva consumando una batteria.
	 * In caso di errori o condizioni non valide, viene mostrato un messaggio di errore.
	 * </p>
	 *
	 * @param riga    la riga della cella cliccata.
	 * @param colonna la colonna della cella cliccata.
	 */
	private void logicaClickSinistro(int riga, int colonna) {
		
		Tessera t = getGiocatore().getNave().getGriglia().getCella(riga, colonna).getTessera();
		
		if (t == null) {
			JOptionPane.showMessageDialog(getDialog(), "Questa cella non contiene alcuna tessera!", "Uso incorretto delle batterie", JOptionPane.ERROR_MESSAGE);
			return;
		}
		if (!t.fornisceBatterie() && !t.isAttivabile()) {
			JOptionPane.showMessageDialog(getDialog(), "Questa cella non contiene batterie e non è attivabile!", "Uso incorretto delle batterie", JOptionPane.ERROR_MESSAGE);
			return;
		}
		
		if (t.fornisceBatterie()) {
			
			VanoBatteria vano = (VanoBatteria)t; // casting sicuro grazie al controllo precedente
			
			int batteriePresenti = vano.getBatterie();
			
			try {
				vano.setNumeroBatterie(batteriePresenti - 1);
			} catch (RichiestaBatterieNonValidaException exception) {
				getLogger().error(exception.getMessage());
				JOptionPane.showMessageDialog(getDialog(), exception.getMessage(), "Uso incorretto delle batterie", JOptionPane.ERROR_MESSAGE);
				return;
			}
			
			contatoreBatteriePrelevate++;
			contaBatterieLbl.setText(String.valueOf(contatoreBatteriePrelevate));
			
		} else { // caso tessera attivabile
			
			if (contatoreBatteriePrelevate == 0) {
				JOptionPane.showMessageDialog(getDialog(), "Per attivare un componente devi prima prelevare una batteria!", "Uso incorretto delle batterie", JOptionPane.ERROR_MESSAGE);
				return;
			}
			
			Attivabile e = (Attivabile) t; // casting sicuro a causa del controllo precedente
			
			if (e.isAttivo()) {
				JOptionPane.showMessageDialog(getDialog(), "Questo componente è già attivo!", "Uso incorretto delle batterie", JOptionPane.ERROR_MESSAGE);
				return;
			}
			
			e.setAttivo(true);
			pannelloNave.getLabelCella(riga, colonna).setBorder(new LineBorder(Color.GREEN, 3));
			
			contatoreBatteriePrelevate--;
			contaBatterieLbl.setText(String.valueOf(contatoreBatteriePrelevate));
			
		}
		
	}
	
	/**
	 * Logica di gestione del click destro del mouse su una cella della nave.
	 * <p>
	 * Se la cella contiene un {@link VanoBatteria}, vi riposiziona una batteria (se il giocatore ne ha di prelevate).
	 * Se la cella contiene un componente {@link Attivabile}, lo disattiva restituendo una batteria al contatore.
	 * In caso di errori o condizioni non valide, viene mostrato un messaggio di errore.
	 * </p>
	 *
	 * @param riga    la riga della cella cliccata.
	 * @param colonna la colonna della cella cliccata.
	 */
	private void logicaClickDestro(int riga, int colonna) {
		
		Tessera t = getGiocatore().getNave().getGriglia().getCella(riga, colonna).getTessera();
		
		if (t == null) {
			JOptionPane.showMessageDialog(getDialog(), "Questa cella non contiene alcuna tessera!", "Uso incorretto delle batterie", JOptionPane.ERROR_MESSAGE);
			return;
		}
		if (!t.fornisceBatterie() && !t.isAttivabile()) {
			JOptionPane.showMessageDialog(getDialog(), "Questa cella non può contenere batterie e non è disattivabile!", "Uso incorretto delle batterie", JOptionPane.ERROR_MESSAGE);
			return;
		}
		
		if (t.fornisceBatterie()) {
			
			if (contatoreBatteriePrelevate == 0) {
				JOptionPane.showMessageDialog(getDialog(), "Non hai batterie da riposizionare!", "Uso incorretto delle batterie", JOptionPane.ERROR_MESSAGE);
				return;
			}
			
			VanoBatteria vano = (VanoBatteria)t; // casting sicuro grazie al controllo precedente		
			int batteriePresenti = vano.getBatterie();
			
			try {
				vano.setNumeroBatterie(batteriePresenti + 1);
			} catch (RichiestaBatterieNonValidaException exception) {
				getLogger().error(exception.getMessage());
				JOptionPane.showMessageDialog(getDialog(), exception.getMessage(), "Uso incorretto delle batterie", JOptionPane.ERROR_MESSAGE);
				return;
			}
			
			contatoreBatteriePrelevate--;
			contaBatterieLbl.setText(String.valueOf(contatoreBatteriePrelevate));
			
		} else { // caso tessera attivabile
			
			Attivabile e = (Attivabile) t; // casting sicuro a causa del controllo precedente
			
			if (!e.isAttivo()) {
				JOptionPane.showMessageDialog(getDialog(), "Questo componente è già disattivato!", "Uso incorretto delle batterie", JOptionPane.ERROR_MESSAGE);
				return;
			}
			
			e.setAttivo(false);
			pannelloNave.getLabelCella(riga, colonna).setBorder(new LineBorder(Color.GRAY));
			
			contatoreBatteriePrelevate++;
			contaBatterieLbl.setText(String.valueOf(contatoreBatteriePrelevate));
			contaBatterieLbl.repaint();
			
		}
	}

}