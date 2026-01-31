package galaxytrucker.src.view.frames;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.LineBorder;
import javax.swing.border.MatteBorder;
import galaxytrucker.src.logic.eccezioni.ConfigurazioneNaveNonValidaException;
import galaxytrucker.src.logic.gioco.Colore;
import galaxytrucker.src.logic.gioco.GameLogger;
import galaxytrucker.src.logic.gioco.Giocatore;
import galaxytrucker.src.logic.gioco.Livello;
import galaxytrucker.src.logic.volo.Evento;
import galaxytrucker.src.view.base.BlockingView;
import galaxytrucker.src.view.base.GuiBase;
import galaxytrucker.src.view.base.IconHandler;
import galaxytrucker.src.view.dialogs.NaveDialog;
import galaxytrucker.src.view.dialogs.ValidazioneNaveDialog;

/**
 * Interfaccia grafica per i turni di volo.
 * Gestisce l'interazione del giocatore con gli eventi di volo, mostrando le carte evento,
 * le statistiche dei giocatori e delle rispettive navi, i pulsanti per controllare
 * il flusso dell'evento e tutte le informazioni rilevanti necessarie durante questa fase.
 *
 * @see GuiBase
 * @see BlockingView
 */
public class VoloGui extends GuiBase implements BlockingView {
	
	// costanti
	
	private static final int WIDTH = 900;
	private static final int HEIGHT = 580;
	
	private static final String DIR_PATH = "/galaxytrucker/resources/images/volo/";
	private static final String BACKGROUND_IMG_PATH = DIR_PATH + "volo_background.png";
	private static final String BANNER_ORO_ICON_PATH = DIR_PATH + "banner_oro.png";
	private static final String BANNER_ARGENTO_ICON_PATH = DIR_PATH + "banner_argento.png";
	private static final String BANNER_BRONZO_ICON_PATH = DIR_PATH + "banner_bronzo.png";
	private static final String BANNER_BIANCO_ICON_PATH = DIR_PATH + "banner_bianco.png";
	private static final String INFO_LIVELLO_PROVA_E_UNO_ICON_PATH = DIR_PATH + "info_livello_prova_e_uno.png";
	private static final String INFO_LIVELLO_DUE_ICON_PATH = DIR_PATH + "info_livello_due.png";
	private static final String INFO_LIVELLO_TRE_ICON_PATH = DIR_PATH + "info_livello_tre.png";
	private static final String ROSSO_ICON_PATH = DIR_PATH + "rosso.png";
	private static final String HOV_ROSSO_ICON_PATH = DIR_PATH + "rosso_hover.png";
	private static final String ROSSO_DISABLED_ICON_PATH = DIR_PATH + "rosso_disabled.png";
	private static final String BLU_ICON_PATH = DIR_PATH + "blu.png";
	private static final String HOV_BLU_ICON_PATH = DIR_PATH + "blu_hover.png";
	private static final String BLU_DISABLED_ICON_PATH = DIR_PATH + "blu_disabled.png";
	private static final String GIALLO_ICON_PATH = DIR_PATH + "giallo.png";
	private static final String HOV_GIALLO_ICON_PATH = DIR_PATH + "giallo_hover.png";
	private static final String GIALLO_DISABLED_ICON_PATH = DIR_PATH + "giallo_disabled.png";
	private static final String VERDE_ICON_PATH = DIR_PATH + "verde.png";
	private static final String HOV_VERDE_ICON_PATH = DIR_PATH + "verde_hover.png";
	private static final String VERDE_DISABLED_ICON_PATH = DIR_PATH + "verde_disabled.png";
	private static final String GIOCA_ICON_PATH = DIR_PATH + "gioca.png";
	private static final String HOV_GIOCA_ICON_PATH = DIR_PATH + "gioca_hover.png";
	private static final String GIOCA_DISABLED_ICON_PATH = DIR_PATH + "gioca_disabled.png";
	private static final String PROSSIMO_ICON_PATH = DIR_PATH + "prossimo.png";
	private static final String HOV_PROSSIMO_ICON_PATH = DIR_PATH + "prossimo_hover.png";
	private static final String PROSSIMO_DISABLED_ICON_PATH = DIR_PATH + "prossimo_disabled.png";
	private static final String INFO_ICON_PATH = DIR_PATH + "info.png";
	private static final String HOV_INFO_ICON_PATH = DIR_PATH + "info_hover.png";
	private static final String MOSTRA_NAVE_ICON_PATH = DIR_PATH + "mostra_nave.png";
	private static final String HOV_MOSTRA_NAVE_ICON_PATH = DIR_PATH + "mostra_nave_hover.png";
	private static final String MOSTRA_NAVE_DISABLED_ICON_PATH = DIR_PATH + "mostra_nave_disabled.png";
	private static final String PUNTO_ESCLAMATIVO_ICON_PATH = DIR_PATH + "punto_esclamativo.png";
	private static final String ABBANDONA_VOLO_ICON_PATH = DIR_PATH + "abbandona_volo.png";
	private static final String HOV_ABBANDONA_VOLO_ICON_PATH = DIR_PATH + "abbandona_volo_hover.png";
	private static final String ABBANDONA_VOLO__DISABLED_ICON_PATH = DIR_PATH + "abbandona_volo_disabled.png";
	
	private static final GameLogger LOGGER = GameLogger.getInstance();
	
	// componenti
	
	private ImageIcon backgroundIcon;
	private ImageIcon bannerOroIcon;
	private ImageIcon bannerArgentoIcon;
	private ImageIcon bannerBronzoIcon;
	private ImageIcon bannerBiancoIcon;
	private ImageIcon infoLivelloProvaEUnoIcon;
	private ImageIcon infoLivelloDueIcon;
	private ImageIcon infoLivelloTreIcon;
	private ImageIcon rossoIcon;
	private ImageIcon hovRossoIcon;	
	private ImageIcon rossoDisabledIcon;
	private ImageIcon bluIcon;
	private ImageIcon hovBluIcon;	
	private ImageIcon bluDisabledIcon;
	private ImageIcon gialloIcon;
	private ImageIcon hovGialloIcon;	
	private ImageIcon gialloDisabledIcon;
	private ImageIcon verdeIcon;
	private ImageIcon hovVerdeIcon;	
	private ImageIcon verdeDisabledIcon;
	private ImageIcon giocaIcon;
	private ImageIcon hovGiocaIcon;
	private ImageIcon giocaDisabledIcon;
	private ImageIcon prossimoIcon;
	private ImageIcon hovProssimoIcon;
	private ImageIcon prossimoDisabledIcon;
	private ImageIcon infoIcon;
	private ImageIcon hovInfoIcon;
	private ImageIcon mostraNaveIcon;
	private ImageIcon hovMostraNaveIcon;
	private ImageIcon mostraNaveDisabledIcon;
	private ImageIcon puntoEsclamativoIcon;
	private ImageIcon abbandonaVoloIcon;
	private ImageIcon hovAbbandonaVoloIcon;
	private ImageIcon abbandonaVoloDisabledIcon;
	
	private JLabel background;
	private JLabel cartaLbl;
	private JLabel titoloGiocatoriLbl;
	private JLabel sottotitoloGiocatoriLbl;
	private JLabel bannerOroLbl;
	private JLabel bannerArgentoLbl;
	private JLabel bannerBronzoLbl;
	private JLabel bannerBiancoLbl;
	private JLabel infoLivelloProvaEUnoLbl;
	private JLabel infoLivelloDueLbl;
	private JLabel infoLivelloTreLbl;
	private JLabel infoLivelloTxtLb;
	private JLabel giorniDiVoloPersiRossoLbl;
	private JLabel giorniDiVoloPersiBluLbl;
	private JLabel giorniDiVoloPersiGialloLbl;
	private JLabel giorniDiVoloPersiVerdeLbl;
	private JLabel puntoEsclamativoRossoLbl;
	private JLabel puntoEsclamativoBluLbl;
	private JLabel puntoEsclamativoGialloLbl;
	private JLabel puntoEsclamativoVerdeLbl;
	private JLabel titoloGiorniDiVoloPersiLblRosso;
	private JLabel titoloGiorniDiVoloPersiLblBlu;
	private JLabel titoloGiorniDiVoloPersiLblGiallo;
	private JLabel titoloGiorniDiVoloPersiLblVerde;
	
	private JButton rossoBtn;
	private JButton bluBtn;
	private JButton gialloBtn;
	private JButton verdeBtn;
	private JButton giocaBtn;
	private JButton prossimoBtn;
	private JButton mostraNaveRossoBtn;
	private JButton mostraNaveBluBtn;
	private JButton mostraNaveGialloBtn;
	private JButton mostraNaveVerdeBtn;
	private JButton infoEventoBtn;
	private JButton abbandonaVoloRossoBtn;
	private JButton abbandonaVoloBluBtn;
	private JButton abbandonaVoloGialloBtn;
	private JButton abbandonaVoloVerdeBtn;
	
	private List<JComponent> iconeGiocatoreBtns = new ArrayList<>();
	private List<JComponent> posizioniGiocatoreLbls = new ArrayList<>();
	private List<JComponent> puntiEsclamativiLbls = new ArrayList<>();
	private List<JComponent> mostraNaveBtns = new ArrayList<>();
	private List<JComponent> abbandonaVoloBtns = new ArrayList<>();
	private List<JComponent> componentiDisabilitabili = new ArrayList<>();
	
	// altri attributi
	
	private Evento evento;
	private CountDownLatch latch;
	private List<Giocatore> giocatori;
	private Giocatore giocatoreRosso;
	private Giocatore giocatoreBlu;
	private Giocatore giocatoreGiallo;
	private Giocatore giocatoreVerde;
	private Livello livelloPartita; // utilizzato per distinguere livello partita e livello evento, che non sempre coincidono
	
	// eccezioni che si possono verificare in presenza di eventi che eliminano delle tessere dalle navi
	private ConfigurazioneNaveNonValidaException erroreNaveRosso;
	private ConfigurazioneNaveNonValidaException erroreNaveBlu;
	private ConfigurazioneNaveNonValidaException erroreNaveGiallo;
	private ConfigurazioneNaveNonValidaException erroreNaveVerde;
	
	/**
	 * Costruisce una nuova istanza di {@code VoloGui} a partire
	 * da un oggetto {@link Evento} ed effettua un setup iniziale.
	 *
	 * @param evento l'evento da giocare.
	 * @throws NullPointerException se {@code evento} o la sua lista di giocatori è {@code null}.
	 */
	public VoloGui(Evento evento) {
		super();
		
		if (evento == null) {
			String errore = "Il parametro 'evento' non può essere nullo!";
			LOGGER.error(errore);
			throw new NullPointerException(errore);
		}
		
		if (evento.getGiocatori() == null) {
			String errore = "L'attributo 'giocatori' dell'evento non può essere nullo!";
			LOGGER.error(errore);
			throw new NullPointerException(errore);
		}
		
		this.evento = evento;
		this.giocatori = evento.getGiocatori();
		this.livelloPartita = giocatori.get(0).getNave().getLivello();
		
		Collections.sort(giocatori);
				
		for (Giocatore g: giocatori) { // i giocatori non in partita saranno null
			
			switch (g.getColore()) {
				case ROSSO:
					giocatoreRosso = g;
					break;
				case BLU:
					giocatoreBlu = g;
					break;
				case GIALLO:
					giocatoreGiallo = g;
					break;
				case VERDE:
					giocatoreVerde = g;
					break;		
			}
		}
	}
	
	/**
	 * Mostra la finestra GUI associata a questo evento e attende finché l'utente non chiude l'interfaccia.
	 * <p>
	 * Questo metodo configura e visualizza il frame Swing in modo sincrono sul thread EDT 
	 * (Event Dispatch Thread), assicurandosi che tutte le componenti vengano inizializzate correttamente.
	 * Il metodo blocca il thread chiamante fino a che l'utente non esegue un'azione che rilascia il latch.
	 */
	@Override
	public void mostraEAttendi() {
		
		latch = new CountDownLatch(1);
		
        try {
            SwingUtilities.invokeAndWait(() -> {
            	    	
            	getFrame().setLayout(new BorderLayout());
                getFrame().setSize(WIDTH, HEIGHT);
                Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
                getFrame().setLocation((screenSize.width - getFrame().getWidth()) / 2, (screenSize.height - getFrame().getHeight()) / 2);		
        		
                setupComponents();
                setupLayout();
                setupListeners();
                
                getFrame().revalidate();
            });
            
        } catch (Exception e) {
        	JOptionPane.showMessageDialog(getFrame(), e, "Errore!", JOptionPane.ERROR_MESSAGE);
        	e.printStackTrace();
        }

        try {
            latch.await();  // blocca solo il thread chiamante
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

	/**
	 * Inizializza e configura tutti i componenti grafici dell'interfaccia, inclusi label, bottoni e icone.
	 * Carica le risorse grafiche (icone), imposta il look dei pulsanti, applica bordi e stili testuali
	 * e definisce le proprietà necessarie per il corretto funzionamento e l’aspetto della GUI.
	 */
	@Override
	public void setupComponents() {
		
		backgroundIcon = new ImageIcon(getClass().getResource(BACKGROUND_IMG_PATH));
		bannerOroIcon = new ImageIcon(getClass().getResource(BANNER_ORO_ICON_PATH));
		bannerArgentoIcon = new ImageIcon(getClass().getResource(BANNER_ARGENTO_ICON_PATH));
		bannerBronzoIcon = new ImageIcon(getClass().getResource(BANNER_BRONZO_ICON_PATH));
		bannerBiancoIcon = new ImageIcon(getClass().getResource(BANNER_BIANCO_ICON_PATH));	
		infoLivelloProvaEUnoIcon = new ImageIcon(getClass().getResource(INFO_LIVELLO_PROVA_E_UNO_ICON_PATH));
		infoLivelloDueIcon = new ImageIcon(getClass().getResource(INFO_LIVELLO_DUE_ICON_PATH));
		infoLivelloTreIcon = new ImageIcon(getClass().getResource(INFO_LIVELLO_TRE_ICON_PATH));
		rossoIcon = new ImageIcon(getClass().getResource(ROSSO_ICON_PATH));
		hovRossoIcon = new ImageIcon(getClass().getResource(HOV_ROSSO_ICON_PATH));
		rossoDisabledIcon = new ImageIcon(getClass().getResource(ROSSO_DISABLED_ICON_PATH));
		bluIcon = new ImageIcon(getClass().getResource(BLU_ICON_PATH));
		hovBluIcon = new ImageIcon(getClass().getResource(HOV_BLU_ICON_PATH));
		bluDisabledIcon = new ImageIcon(getClass().getResource(BLU_DISABLED_ICON_PATH));
		gialloIcon = new ImageIcon(getClass().getResource(GIALLO_ICON_PATH));
		hovGialloIcon = new ImageIcon(getClass().getResource(HOV_GIALLO_ICON_PATH));
		gialloDisabledIcon = new ImageIcon(getClass().getResource(GIALLO_DISABLED_ICON_PATH));
		verdeIcon = new ImageIcon(getClass().getResource(VERDE_ICON_PATH));
		hovVerdeIcon = new ImageIcon(getClass().getResource(HOV_VERDE_ICON_PATH));
		verdeDisabledIcon = new ImageIcon(getClass().getResource(VERDE_DISABLED_ICON_PATH));
		giocaIcon = new ImageIcon(getClass().getResource(GIOCA_ICON_PATH));
		hovGiocaIcon = new ImageIcon(getClass().getResource(HOV_GIOCA_ICON_PATH));
		giocaDisabledIcon = new ImageIcon(getClass().getResource(GIOCA_DISABLED_ICON_PATH));
		prossimoIcon = new ImageIcon(getClass().getResource(PROSSIMO_ICON_PATH));
		hovProssimoIcon = new ImageIcon(getClass().getResource(HOV_PROSSIMO_ICON_PATH));
		prossimoDisabledIcon = new ImageIcon(getClass().getResource(PROSSIMO_DISABLED_ICON_PATH));
		infoIcon = new ImageIcon(getClass().getResource(INFO_ICON_PATH));
		hovInfoIcon = new ImageIcon(getClass().getResource(HOV_INFO_ICON_PATH));
		mostraNaveIcon = new ImageIcon(getClass().getResource(MOSTRA_NAVE_ICON_PATH));
		hovMostraNaveIcon = new ImageIcon(getClass().getResource(HOV_MOSTRA_NAVE_ICON_PATH));
		mostraNaveDisabledIcon = new ImageIcon(getClass().getResource(MOSTRA_NAVE_DISABLED_ICON_PATH));
		puntoEsclamativoIcon = new ImageIcon(getClass().getResource(PUNTO_ESCLAMATIVO_ICON_PATH));
		abbandonaVoloIcon = new ImageIcon(getClass().getResource(ABBANDONA_VOLO_ICON_PATH));
		hovAbbandonaVoloIcon = new ImageIcon(getClass().getResource(HOV_ABBANDONA_VOLO_ICON_PATH));
		abbandonaVoloDisabledIcon = new ImageIcon(getClass().getResource(ABBANDONA_VOLO__DISABLED_ICON_PATH));
		
		background = new JLabel(backgroundIcon);
		cartaLbl = new JLabel();
		titoloGiocatoriLbl = new JLabel("Giocatori"); 
		sottotitoloGiocatoriLbl = new JLabel("<html><div style='text-align:center;'>"
			    + "Qui trovi le statistiche dei<br>"
			    + " giocatori, elencati in ordine di rotta!"
			    + "</div></html>");
		bannerOroLbl = new JLabel(bannerOroIcon);
		bannerArgentoLbl = new JLabel(bannerArgentoIcon);
		bannerBronzoLbl = new JLabel(bannerBronzoIcon);
		bannerBiancoLbl = new JLabel(bannerBiancoIcon);
		infoLivelloProvaEUnoLbl = new JLabel(infoLivelloProvaEUnoIcon);
		infoLivelloDueLbl = new JLabel(infoLivelloDueIcon);
		infoLivelloTreLbl = new JLabel(infoLivelloTreIcon);
		infoLivelloTxtLb = new JLabel("Informazioni sul livello di gioco");
		giorniDiVoloPersiRossoLbl = new JLabel();
		giorniDiVoloPersiBluLbl = new JLabel();
		giorniDiVoloPersiGialloLbl = new JLabel();
		giorniDiVoloPersiVerdeLbl = new JLabel();
		puntoEsclamativoRossoLbl = new JLabel();
		puntoEsclamativoBluLbl = new JLabel();
		puntoEsclamativoGialloLbl = new JLabel();
		puntoEsclamativoVerdeLbl = new JLabel();
		titoloGiorniDiVoloPersiLblRosso = new JLabel("Giorni di volo persi");
		titoloGiorniDiVoloPersiLblBlu = new JLabel("Giorni di volo persi");
		titoloGiorniDiVoloPersiLblGiallo = new JLabel("Giorni di volo persi");
		titoloGiorniDiVoloPersiLblVerde = new JLabel("Giorni di volo persi");
		
		rossoBtn = new JButton();
		bluBtn = new JButton();
		gialloBtn = new JButton();
		verdeBtn = new JButton();
		giocaBtn = new JButton();
		prossimoBtn = new JButton();
		mostraNaveRossoBtn = new JButton();
		mostraNaveBluBtn = new JButton();
		mostraNaveGialloBtn = new JButton();
		mostraNaveVerdeBtn = new JButton();
		infoEventoBtn = new JButton();
		abbandonaVoloRossoBtn = new JButton();
		abbandonaVoloBluBtn = new JButton();
		abbandonaVoloGialloBtn = new JButton();
		abbandonaVoloVerdeBtn = new JButton();
		
		IconHandler.setIconOnLabel(cartaLbl, evento.getImmagine(), evento.getImmagine());
		IconHandler.setIconOnLabel(background, backgroundIcon, backgroundIcon);
		IconHandler.setIconOnLabel(bannerOroLbl, bannerOroIcon, bannerOroIcon);
		IconHandler.setIconOnLabel(bannerArgentoLbl, bannerArgentoIcon, bannerArgentoIcon);
		IconHandler.setIconOnLabel(bannerBronzoLbl, bannerBronzoIcon, bannerBronzoIcon);
		IconHandler.setIconOnLabel(bannerBiancoLbl, bannerBiancoIcon, bannerBiancoIcon);
		IconHandler.setIconOnLabel(infoLivelloProvaEUnoLbl, infoLivelloProvaEUnoIcon, infoLivelloProvaEUnoIcon);
		IconHandler.setIconOnLabel(infoLivelloDueLbl, infoLivelloDueIcon, infoLivelloDueIcon);
		IconHandler.setIconOnLabel(infoLivelloTreLbl, infoLivelloTreIcon, infoLivelloTreIcon);	
		IconHandler.setIconOnButton(rossoBtn, rossoIcon, hovRossoIcon);
		IconHandler.setIconOnButton(bluBtn, bluIcon, hovBluIcon);
		IconHandler.setIconOnButton(gialloBtn, gialloIcon, hovGialloIcon);
		IconHandler.setIconOnButton(verdeBtn, verdeIcon, hovVerdeIcon);
		IconHandler.setIconOnButton(giocaBtn, giocaIcon, hovGiocaIcon);
		IconHandler.setIconOnButton(prossimoBtn, prossimoDisabledIcon, prossimoDisabledIcon);
		IconHandler.setIconOnButton(mostraNaveRossoBtn, mostraNaveIcon, hovMostraNaveIcon);
		IconHandler.setIconOnButton(mostraNaveBluBtn, mostraNaveIcon, hovMostraNaveIcon);
		IconHandler.setIconOnButton(mostraNaveGialloBtn, mostraNaveIcon, hovMostraNaveIcon);
		IconHandler.setIconOnButton(mostraNaveVerdeBtn, mostraNaveIcon, hovMostraNaveIcon);
		IconHandler.setIconOnButton(infoEventoBtn, infoIcon, hovInfoIcon);
		IconHandler.setIconOnLabel(puntoEsclamativoRossoLbl, puntoEsclamativoIcon, puntoEsclamativoIcon);
		IconHandler.setIconOnLabel(puntoEsclamativoBluLbl, puntoEsclamativoIcon, puntoEsclamativoIcon);
		IconHandler.setIconOnLabel(puntoEsclamativoGialloLbl, puntoEsclamativoIcon, puntoEsclamativoIcon);
		IconHandler.setIconOnLabel(puntoEsclamativoVerdeLbl, puntoEsclamativoIcon, puntoEsclamativoIcon);
		IconHandler.setIconOnButton(abbandonaVoloRossoBtn, (livelloPartita != Livello.P) ? abbandonaVoloIcon : abbandonaVoloDisabledIcon, (livelloPartita != Livello.P) ? hovAbbandonaVoloIcon : abbandonaVoloDisabledIcon);
		IconHandler.setIconOnButton(abbandonaVoloBluBtn, (livelloPartita != Livello.P) ? abbandonaVoloIcon : abbandonaVoloDisabledIcon, (livelloPartita != Livello.P) ? hovAbbandonaVoloIcon : abbandonaVoloDisabledIcon);
		IconHandler.setIconOnButton(abbandonaVoloGialloBtn, (livelloPartita != Livello.P) ? abbandonaVoloIcon : abbandonaVoloDisabledIcon, (livelloPartita != Livello.P) ? hovAbbandonaVoloIcon : abbandonaVoloDisabledIcon);
		IconHandler.setIconOnButton(abbandonaVoloVerdeBtn, (livelloPartita != Livello.P) ? abbandonaVoloIcon : abbandonaVoloDisabledIcon, (livelloPartita != Livello.P) ? hovAbbandonaVoloIcon : abbandonaVoloDisabledIcon);
		
		impostaLookDefaultBottone(rossoBtn);
		impostaLookDefaultBottone(bluBtn);
		impostaLookDefaultBottone(gialloBtn);
		impostaLookDefaultBottone(verdeBtn);
		impostaLookDefaultBottone(giocaBtn);
		impostaLookDefaultBottone(prossimoBtn);
		impostaLookDefaultBottone(mostraNaveRossoBtn);
		impostaLookDefaultBottone(mostraNaveBluBtn);
		impostaLookDefaultBottone(mostraNaveGialloBtn);
		impostaLookDefaultBottone(mostraNaveVerdeBtn);
		impostaLookDefaultBottone(infoEventoBtn);
		impostaLookDefaultBottone(abbandonaVoloRossoBtn);
		impostaLookDefaultBottone(abbandonaVoloBluBtn);
		impostaLookDefaultBottone(abbandonaVoloGialloBtn);
		impostaLookDefaultBottone(abbandonaVoloVerdeBtn);
		
		giocaBtn.putClientProperty("selezionabile", true);
		prossimoBtn.putClientProperty("selezionabile", false);
		prossimoBtn.setToolTipText("Devi giocare questo turno prima di passare al prossimo!");
		
		infoLivelloProvaEUnoLbl.setBorder(new MatteBorder(2, 2, 0, 0, Color.WHITE));
		infoLivelloDueLbl.setBorder(new MatteBorder(2, 2, 0, 0, Color.WHITE));
		infoLivelloTreLbl.setBorder(new MatteBorder(2, 2, 0, 0, Color.WHITE));
		
		infoLivelloTxtLb.setForeground(Color.WHITE);
		infoLivelloTxtLb.setFont(infoLivelloTxtLb.getFont().deriveFont(Font.BOLD, 13));
		
		cartaLbl.setBorder(new LineBorder(Color.WHITE, 3));
		infoEventoBtn.setToolTipText("Clicca per vedere la descrizione dell'evento!");
		
		titoloGiocatoriLbl.setForeground(Color.WHITE);
		titoloGiocatoriLbl.setFont(titoloGiocatoriLbl.getFont().deriveFont(Font.BOLD, 30));
		titoloGiocatoriLbl.setHorizontalAlignment(SwingConstants.CENTER);
		sottotitoloGiocatoriLbl.setForeground(Color.WHITE);
		sottotitoloGiocatoriLbl.setFont(sottotitoloGiocatoriLbl.getFont().deriveFont(Font.BOLD, 13));
		sottotitoloGiocatoriLbl.setHorizontalAlignment(SwingConstants.CENTER);

		bannerOroLbl.setBorder(new MatteBorder(3, 0, 1, 3, Color.WHITE));
		bannerArgentoLbl.setBorder(new MatteBorder(1, 0, 1, 3, Color.WHITE));
		bannerBronzoLbl.setBorder(new MatteBorder(1, 0, 1, 3, Color.WHITE));
		bannerBiancoLbl.setBorder(new MatteBorder(1, 0, 0, 3, Color.WHITE));
			
		LineBorder bordoGiorniDiVoloPersiLbl = new LineBorder(Color.DARK_GRAY, 2);
		
		giorniDiVoloPersiRossoLbl.setForeground(Color.BLACK);
		giorniDiVoloPersiRossoLbl.setFont(giorniDiVoloPersiRossoLbl.getFont().deriveFont(Font.BOLD, 42));
		giorniDiVoloPersiRossoLbl.setHorizontalAlignment(SwingConstants.CENTER);
		giorniDiVoloPersiRossoLbl.setBorder(bordoGiorniDiVoloPersiLbl);
		giorniDiVoloPersiBluLbl.setForeground(Color.BLACK);
		giorniDiVoloPersiBluLbl.setFont(giorniDiVoloPersiBluLbl.getFont().deriveFont(Font.BOLD, 42));
		giorniDiVoloPersiBluLbl.setHorizontalAlignment(SwingConstants.CENTER);
		giorniDiVoloPersiBluLbl.setBorder(bordoGiorniDiVoloPersiLbl);
		giorniDiVoloPersiGialloLbl.setForeground(Color.BLACK);
		giorniDiVoloPersiGialloLbl.setFont(giorniDiVoloPersiGialloLbl.getFont().deriveFont(Font.BOLD, 42));
		giorniDiVoloPersiGialloLbl.setHorizontalAlignment(SwingConstants.CENTER);
		giorniDiVoloPersiGialloLbl.setBorder(bordoGiorniDiVoloPersiLbl);
		giorniDiVoloPersiVerdeLbl.setForeground(Color.BLACK);
		giorniDiVoloPersiVerdeLbl.setFont(giorniDiVoloPersiVerdeLbl.getFont().deriveFont(Font.BOLD, 42));
		giorniDiVoloPersiVerdeLbl.setHorizontalAlignment(SwingConstants.CENTER);
		giorniDiVoloPersiVerdeLbl.setBorder(bordoGiorniDiVoloPersiLbl);
		infoLivelloTxtLb.setFont(infoLivelloTxtLb.getFont().deriveFont(Font.BOLD, 13));
		
		titoloGiorniDiVoloPersiLblRosso.setFont(titoloGiorniDiVoloPersiLblRosso.getFont().deriveFont(Font.BOLD, 11));
		titoloGiorniDiVoloPersiLblBlu.setFont(titoloGiorniDiVoloPersiLblBlu.getFont().deriveFont(Font.BOLD, 11));
		titoloGiorniDiVoloPersiLblGiallo.setFont(titoloGiorniDiVoloPersiLblGiallo.getFont().deriveFont(Font.BOLD, 11));
		titoloGiorniDiVoloPersiLblVerde.setFont(titoloGiorniDiVoloPersiLblVerde.getFont().deriveFont(Font.BOLD, 11));
		titoloGiorniDiVoloPersiLblRosso.setBackground(Color.BLACK);
		titoloGiorniDiVoloPersiLblRosso.setHorizontalAlignment(SwingConstants.CENTER);
		titoloGiorniDiVoloPersiLblBlu.setHorizontalAlignment(SwingConstants.CENTER);
		titoloGiorniDiVoloPersiLblGiallo.setHorizontalAlignment(SwingConstants.CENTER);
		titoloGiorniDiVoloPersiLblVerde.setHorizontalAlignment(SwingConstants.CENTER);
		titoloGiorniDiVoloPersiLblRosso.setForeground(Color.BLACK);
		titoloGiorniDiVoloPersiLblBlu.setForeground(Color.BLACK);
		titoloGiorniDiVoloPersiLblGiallo.setForeground(Color.BLACK);
		titoloGiorniDiVoloPersiLblVerde.setForeground(Color.BLACK);

		giorniDiVoloPersiRossoLbl.setText(String.valueOf((giocatoreRosso != null) ? giocatoreRosso.getGiorniDiVoloPersi(): "/"));
		giorniDiVoloPersiBluLbl.setText(String.valueOf((giocatoreBlu != null) ? giocatoreBlu.getGiorniDiVoloPersi(): "/"));
		giorniDiVoloPersiGialloLbl.setText(String.valueOf((giocatoreGiallo != null) ? giocatoreGiallo.getGiorniDiVoloPersi(): "/"));
		giorniDiVoloPersiVerdeLbl.setText(String.valueOf((giocatoreVerde != null) ? giocatoreVerde.getGiorniDiVoloPersi(): "/"));
		
		rossoBtn.putClientProperty("colore", Colore.ROSSO);
		bluBtn.putClientProperty("colore", Colore.BLU);
		gialloBtn.putClientProperty("colore", Colore.GIALLO);
		verdeBtn.putClientProperty("colore", Colore.VERDE);
		mostraNaveRossoBtn.putClientProperty("colore", Colore.ROSSO);
		mostraNaveBluBtn.putClientProperty("colore", Colore.BLU);
		mostraNaveGialloBtn.putClientProperty("colore", Colore.GIALLO);
		mostraNaveVerdeBtn.putClientProperty("colore", Colore.VERDE);
		giorniDiVoloPersiRossoLbl.putClientProperty("colore", Colore.ROSSO);
		giorniDiVoloPersiBluLbl.putClientProperty("colore", Colore.BLU);
		giorniDiVoloPersiGialloLbl.putClientProperty("colore", Colore.GIALLO);
		giorniDiVoloPersiVerdeLbl.putClientProperty("colore", Colore.VERDE);
		puntoEsclamativoRossoLbl.putClientProperty("colore", Colore.ROSSO);
		puntoEsclamativoBluLbl.putClientProperty("colore", Colore.BLU);
		puntoEsclamativoGialloLbl.putClientProperty("colore", Colore.GIALLO);
		puntoEsclamativoVerdeLbl.putClientProperty("colore", Colore.VERDE);
		abbandonaVoloRossoBtn.putClientProperty("colore", Colore.ROSSO);
		abbandonaVoloBluBtn.putClientProperty("colore", Colore.BLU);
		abbandonaVoloGialloBtn.putClientProperty("colore", Colore.GIALLO);
		abbandonaVoloVerdeBtn.putClientProperty("colore", Colore.VERDE);
		
		rossoBtn.putClientProperty("iconaDisabled", rossoDisabledIcon);
		bluBtn.putClientProperty("iconaDisabled", bluDisabledIcon);
		gialloBtn.putClientProperty("iconaDisabled", gialloDisabledIcon);
		verdeBtn.putClientProperty("iconaDisabled", verdeDisabledIcon);
		mostraNaveRossoBtn.putClientProperty("iconaDisabled", mostraNaveDisabledIcon);
		mostraNaveBluBtn.putClientProperty("iconaDisabled", mostraNaveDisabledIcon);
		mostraNaveGialloBtn.putClientProperty("iconaDisabled", mostraNaveDisabledIcon);
		mostraNaveVerdeBtn.putClientProperty("iconaDisabled", mostraNaveDisabledIcon);
		abbandonaVoloRossoBtn.putClientProperty("iconaDisabled", abbandonaVoloDisabledIcon);
		abbandonaVoloBluBtn.putClientProperty("iconaDisabled", abbandonaVoloDisabledIcon);
		abbandonaVoloGialloBtn.putClientProperty("iconaDisabled", abbandonaVoloDisabledIcon);
		abbandonaVoloVerdeBtn.putClientProperty("iconaDisabled", abbandonaVoloDisabledIcon);
		
	}

	/**
	 * Inizializza e posiziona tutti i componenti grafici del pannello principale, 
	 * impostando le coordinate assolute e popolando le liste di riferimento ai componenti.
	 */
	@Override
	public void setupLayout() {
		
		background.setLayout(null);
		bannerOroLbl.setBounds(0, 102, 300, 110);
		bannerArgentoLbl.setBounds(0, 212, 300, 110);
		bannerBronzoLbl.setBounds(0, 322, 300, 110);
		bannerBiancoLbl.setBounds(0, 432, 300, 110);
		infoLivelloProvaEUnoLbl.setBounds(563, 391, 323, 150);
		infoLivelloDueLbl.setBounds(563, 391, 323, 150);
		infoLivelloTreLbl.setBounds(570, 391, 315, 150);
		infoLivelloTxtLb.setBounds(617, 350, 300, 50);
		titoloGiocatoriLbl.setBounds(5, 0, 300, 50);
		sottotitoloGiocatoriLbl.setBounds(5, 40, 300, 50);
		cartaLbl.setBounds(510, 30, 200, 310);
		giocaBtn.setBounds(344, 400, 180, 60);
		prossimoBtn.setBounds(344, 475, 180, 60);
		infoEventoBtn.setBounds(675, 40, 30, 30);
		titoloGiorniDiVoloPersiLblRosso.setBounds(15, 91, 200, 50);
		titoloGiorniDiVoloPersiLblBlu.setBounds(15, 200, 200, 50);
		titoloGiorniDiVoloPersiLblGiallo.setBounds(15, 309, 200, 50);
		titoloGiorniDiVoloPersiLblVerde.setBounds(15, 418, 200, 50);
		
		// posizionamento giocatori
		
		iconeGiocatoreBtns = new ArrayList<>();
		posizioniGiocatoreLbls = new ArrayList<>();
		puntiEsclamativiLbls = new ArrayList<>();
		mostraNaveBtns = new ArrayList<>();
		abbandonaVoloBtns = new ArrayList<>();
		componentiDisabilitabili = new ArrayList<>();
		
		iconeGiocatoreBtns.add(rossoBtn);
		iconeGiocatoreBtns.add(bluBtn);
		iconeGiocatoreBtns.add(gialloBtn);
		iconeGiocatoreBtns.add(verdeBtn);
		posizioniGiocatoreLbls.add(giorniDiVoloPersiRossoLbl);
		posizioniGiocatoreLbls.add(giorniDiVoloPersiBluLbl);
		posizioniGiocatoreLbls.add(giorniDiVoloPersiGialloLbl);
		posizioniGiocatoreLbls.add(giorniDiVoloPersiVerdeLbl);
		puntiEsclamativiLbls.add(puntoEsclamativoRossoLbl);
		puntiEsclamativiLbls.add(puntoEsclamativoBluLbl);
		puntiEsclamativiLbls.add(puntoEsclamativoGialloLbl);
		puntiEsclamativiLbls.add(puntoEsclamativoVerdeLbl);
		mostraNaveBtns.add(mostraNaveRossoBtn);
		mostraNaveBtns.add(mostraNaveBluBtn);
		mostraNaveBtns.add(mostraNaveGialloBtn);
		mostraNaveBtns.add(mostraNaveVerdeBtn);
		abbandonaVoloBtns.add(abbandonaVoloRossoBtn);
		abbandonaVoloBtns.add(abbandonaVoloBluBtn);
		abbandonaVoloBtns.add(abbandonaVoloGialloBtn);
		abbandonaVoloBtns.add(abbandonaVoloVerdeBtn);
		componentiDisabilitabili.addAll(iconeGiocatoreBtns);
		componentiDisabilitabili.addAll(mostraNaveBtns);
		componentiDisabilitabili.addAll(abbandonaVoloBtns);
		
		background.add(titoloGiorniDiVoloPersiLblRosso);
		background.add(titoloGiorniDiVoloPersiLblBlu);
		background.add(titoloGiorniDiVoloPersiLblGiallo);
		background.add(titoloGiorniDiVoloPersiLblVerde);
		
		aggiornaPannelloStatistiche(true);
		
		background.add(bannerOroLbl);
		background.add(bannerArgentoLbl);
		background.add(bannerBronzoLbl);
		background.add(bannerBiancoLbl);
		background.add(titoloGiocatoriLbl);
		background.add(sottotitoloGiocatoriLbl);
		background.add(infoEventoBtn);
		background.add(cartaLbl);
		background.add(giocaBtn);
		background.add(prossimoBtn);
		if (livelloPartita == Livello.P || livelloPartita == Livello.I) background.add(infoLivelloProvaEUnoLbl);
		if (livelloPartita == Livello.II) background.add(infoLivelloDueLbl);
		if (livelloPartita == Livello.III) background.add(infoLivelloTreLbl);
		background.add(infoLivelloTxtLb);
		
		getFrame().add(background);
	}

	/**
	 * Registra tutti i listener dei componenti dell’interfaccia grafica, 
	 * gestendo l’avvio dell’evento, il passaggio al turno successivo, 
	 * la visualizzazione delle informazioni su giocatori e navi, 
	 * e l’abbandono del volo da parte dei giocatori.
	 */
	@Override
	public void setupListeners() {
		
		prossimoBtn.addActionListener(e-> {

			if (Boolean.FALSE.equals(prossimoBtn.getClientProperty("selezionabile"))) return;
			
			if (erroreNaveRosso != null || erroreNaveBlu != null || erroreNaveGiallo != null || erroreNaveVerde != null) {
				JOptionPane.showMessageDialog(getFrame(), "Almeno una delle navi presenta errori da correggere!", "Errore nelle navi!", JOptionPane.ERROR_MESSAGE);
				return;
			}
			
			latch.countDown(); 
			getFrame().dispose();
			
		});
		
		giocaBtn.addActionListener(e-> {
			
			if (Boolean.FALSE.equals(giocaBtn.getClientProperty("selezionabile"))) return;
			
			giocaBtn.putClientProperty("selezionabile", false);
			IconHandler.setIconOnButton(giocaBtn, giocaDisabledIcon, giocaDisabledIcon);
			
			evento.avviaEvento(this);
			prossimoBtn.putClientProperty("selezionabile", true);
			IconHandler.setIconOnButton(prossimoBtn, prossimoIcon, hovProssimoIcon);
			prossimoBtn.setToolTipText(null);
			
			// gestione errori nella nave
			for (Giocatore g : giocatori) {
				
				if (!g.isInVolo()) continue;
				
				try {
					g.getNave().verificaNave();
					
				} catch (ConfigurazioneNaveNonValidaException exc) {
					
					switch (g.getColore()) {
					case ROSSO:
						erroreNaveRosso = exc;
						puntoEsclamativoRossoLbl.setVisible(true);
						break;
					case BLU:
						erroreNaveBlu = exc;
						puntoEsclamativoBluLbl.setVisible(true);
						break;
					case GIALLO:
						erroreNaveGiallo = exc;
						puntoEsclamativoGialloLbl.setVisible(true);
						break;
					case VERDE:
						erroreNaveVerde = exc;
						puntoEsclamativoVerdeLbl.setVisible(true);
						break;
					default:
						break;
					
					}
				}
			}
			
			aggiornaPannelloStatistiche(false);
						
		});
		
		infoEventoBtn.addActionListener(e-> {
			JOptionPane.showMessageDialog(getFrame(), evento.toString(), "Informazioni sull'evento", JOptionPane.INFORMATION_MESSAGE);
		});
		
		rossoBtn.addActionListener(e -> {		
			if (Boolean.FALSE.equals(rossoBtn.getClientProperty("enabled"))) return;			
			JOptionPane.showMessageDialog(getFrame(), giocatoreRosso.toString(), "Informazioni sul giocatore", JOptionPane.INFORMATION_MESSAGE);			
		});
		
		bluBtn.addActionListener(e -> {		
			if (Boolean.FALSE.equals(bluBtn.getClientProperty("enabled"))) return;
			JOptionPane.showMessageDialog(getFrame(), giocatoreBlu.toString(), "Informazioni sul giocatore", JOptionPane.INFORMATION_MESSAGE);			
		});
		
		gialloBtn.addActionListener(e -> {		
			if (Boolean.FALSE.equals(gialloBtn.getClientProperty("enabled"))) return;			
			JOptionPane.showMessageDialog(getFrame(), giocatoreGiallo.toString(), "Informazioni sul giocatore", JOptionPane.INFORMATION_MESSAGE);			
		});
		
		verdeBtn.addActionListener(e -> {		
			if (Boolean.FALSE.equals(verdeBtn.getClientProperty("enabled"))) return;			
			JOptionPane.showMessageDialog(getFrame(), giocatoreVerde.toString(), "Informazioni sul giocatore", JOptionPane.INFORMATION_MESSAGE);			
		});
		
		mostraNaveRossoBtn.addActionListener(e -> {
			if (Boolean.FALSE.equals(mostraNaveRossoBtn.getClientProperty("enabled"))) return;		
			
			if (erroreNaveRosso == null) {
				 new NaveDialog(getFrame(), giocatoreRosso);
			} else {
				new ValidazioneNaveDialog(getFrame(), giocatoreRosso, erroreNaveRosso);
				
				try {
					giocatoreRosso.getNave().verificaNave();
					puntoEsclamativoRossoLbl.setVisible(false);
					erroreNaveRosso = null;
				} catch (ConfigurazioneNaveNonValidaException exc) {
					erroreNaveRosso = exc;
				}
				
			}
		});

		mostraNaveBluBtn.addActionListener(e -> {
			if (Boolean.FALSE.equals(mostraNaveBluBtn.getClientProperty("enabled"))) return;	
			
			if (erroreNaveBlu == null) {
				 new NaveDialog(getFrame(), giocatoreBlu);
			} else {
				new ValidazioneNaveDialog(getFrame(), giocatoreBlu, erroreNaveBlu);
				
				try {
					giocatoreBlu.getNave().verificaNave();
					puntoEsclamativoBluLbl.setVisible(false);
					erroreNaveBlu = null;
				} catch (ConfigurazioneNaveNonValidaException exc) {
					erroreNaveBlu = exc;
				}
			}
		});

		mostraNaveGialloBtn.addActionListener(e -> {
			if (Boolean.FALSE.equals(mostraNaveGialloBtn.getClientProperty("enabled"))) return;	
			
			if (erroreNaveGiallo == null) {
				 new NaveDialog(getFrame(), giocatoreGiallo);
			} else {
				new ValidazioneNaveDialog(getFrame(), giocatoreGiallo, erroreNaveGiallo);
				
				try {
					giocatoreGiallo.getNave().verificaNave();
					puntoEsclamativoGialloLbl.setVisible(false);
					erroreNaveGiallo = null;
				} catch (ConfigurazioneNaveNonValidaException exc) {
					erroreNaveGiallo = exc;
				}
			}

		});

		mostraNaveVerdeBtn.addActionListener(e -> {
			if (Boolean.FALSE.equals(mostraNaveVerdeBtn.getClientProperty("enabled"))) return;	
			
			if (erroreNaveVerde == null) {
				 new NaveDialog(getFrame(), giocatoreVerde);
			} else {
				new ValidazioneNaveDialog(getFrame(), giocatoreVerde, erroreNaveVerde);
				
				try {
					giocatoreVerde.getNave().verificaNave();
					puntoEsclamativoVerdeLbl.setVisible(false);
					erroreNaveVerde = null;
				} catch (ConfigurazioneNaveNonValidaException exc) {
					erroreNaveVerde = exc;
				}
			}
		});
		
		abbandonaVoloRossoBtn.addActionListener(e -> {		
			if (Boolean.FALSE.equals(abbandonaVoloRossoBtn.getClientProperty("enabled"))) return;			
			
			int risposta = JOptionPane.showConfirmDialog(getFrame(), "Sei sicuro di voler abbandonare il volo?", "Conferma abbandono volo", JOptionPane.YES_NO_OPTION);

			if (risposta == JOptionPane.YES_OPTION) {
				giocatoreRosso.setInVolo(false);
				impostaGiocatoreNonInVolo(Colore.ROSSO);				    
			}		
		});
		
		abbandonaVoloBluBtn.addActionListener(e -> {		
			if (Boolean.FALSE.equals(abbandonaVoloBluBtn.getClientProperty("enabled"))) return;			
			
			int risposta = JOptionPane.showConfirmDialog(getFrame(), "Sei sicuro di voler abbandonare il volo?", "Conferma abbandono volo", JOptionPane.YES_NO_OPTION);

			if (risposta == JOptionPane.YES_OPTION) {
				giocatoreBlu.setInVolo(false);
				impostaGiocatoreNonInVolo(Colore.BLU);				    
			}		
		});
		
		abbandonaVoloGialloBtn.addActionListener(e -> {		
			if (Boolean.FALSE.equals(abbandonaVoloGialloBtn.getClientProperty("enabled"))) return;			
			
			int risposta = JOptionPane.showConfirmDialog(getFrame(), "Sei sicuro di voler abbandonare il volo?", "Conferma abbandono volo", JOptionPane.YES_NO_OPTION);

			if (risposta == JOptionPane.YES_OPTION) {
				giocatoreGiallo.setInVolo(false);
				impostaGiocatoreNonInVolo(Colore.GIALLO);				    
			}		
		});
		
		abbandonaVoloVerdeBtn.addActionListener(e -> {		
			if (Boolean.FALSE.equals(abbandonaVoloVerdeBtn.getClientProperty("enabled"))) return;			
			
			int risposta = JOptionPane.showConfirmDialog(getFrame(), "Sei sicuro di voler abbandonare il volo?", "Conferma abbandono volo", JOptionPane.YES_NO_OPTION);
			
			if (risposta == JOptionPane.YES_OPTION) {
				giocatoreVerde.setInVolo(false);
				impostaGiocatoreNonInVolo(Colore.VERDE);				    
			}		
		});

		
	}
	
	/**
	 * Aggiorna il pannello delle statistiche dei giocatori, posizionando i componenti
	 * grafici relativi a ciascun giocatore e aggiornando lo stato visivo e abilitato/disabilitato
	 * dei bottoni in base allo stato della partita.
	 *
	 * @param primoPiazzamento {@code true} se è il primo piazzamento dei componenti nel pannello; 
	 *                         solo in tal caso i componenti verranno aggiunti al background.
	 */
	private void aggiornaPannelloStatistiche(boolean primoPiazzamento) {
		posizionaComponentiPerGiocatori(iconeGiocatoreBtns, 10, 110, 50, 90, 20, primoPiazzamento);
		posizionaComponentiPerGiocatori(posizioniGiocatoreLbls, 80, 130, 60, 60, 50, primoPiazzamento);
		posizionaComponentiPerGiocatori(puntiEsclamativiLbls, 310, 125, 22, 60, 50, primoPiazzamento);
		posizionaComponentiPerGiocatori(mostraNaveBtns, 243, 120, 50, 80, 30, primoPiazzamento);	
		posizionaComponentiPerGiocatori(abbandonaVoloBtns, 181, 128, 55, 55, 55, primoPiazzamento);
		
		impostaGiocatoriNonInPartita(componentiDisabilitabili);
		if (primoPiazzamento) for (JComponent comp: puntiEsclamativiLbls) comp.setVisible(false);
		
		// aggiustamento delle proprietà dei seguenti bottoni in base alla modalità
		abbandonaVoloRossoBtn.putClientProperty("enabled", (giocatoreRosso != null && giocatoreRosso.isInVolo() && livelloPartita != Livello.P));
		abbandonaVoloBluBtn.putClientProperty("enabled", (giocatoreBlu != null && giocatoreBlu.isInVolo() && livelloPartita != Livello.P));
		abbandonaVoloGialloBtn.putClientProperty("enabled", (giocatoreGiallo != null && giocatoreGiallo.isInVolo() && livelloPartita != Livello.P));
		abbandonaVoloVerdeBtn.putClientProperty("enabled", (giocatoreVerde != null && giocatoreVerde.isInVolo() && livelloPartita != Livello.P));
		abbandonaVoloRossoBtn.setToolTipText((Boolean.TRUE.equals(abbandonaVoloRossoBtn.getClientProperty("enabled")) ? "Abbandona il volo?!" : null));
		abbandonaVoloBluBtn.setToolTipText((Boolean.TRUE.equals(abbandonaVoloBluBtn.getClientProperty("enabled")) ? "Abbandona il volo?!" : null));
		abbandonaVoloGialloBtn.setToolTipText((Boolean.TRUE.equals(abbandonaVoloGialloBtn.getClientProperty("enabled")) ? "Abbandona il volo?!" : null));
		abbandonaVoloVerdeBtn.setToolTipText((Boolean.TRUE.equals(abbandonaVoloVerdeBtn.getClientProperty("enabled")) ? "Abbandona il volo?!" : null));
		if (livelloPartita == Livello.P && giocatoreRosso != null) abbandonaVoloRossoBtn.setToolTipText("Non puoi abbandonare il volo nella modalità di prova!");
		if (livelloPartita == Livello.P && giocatoreBlu != null) abbandonaVoloBluBtn.setToolTipText("Non puoi abbandonare il volo nella modalità di prova!");
		if (livelloPartita == Livello.P && giocatoreGiallo != null) abbandonaVoloGialloBtn.setToolTipText("Non puoi abbandonare il volo nella modalità di prova!");
		if (livelloPartita == Livello.P && giocatoreRosso != null) abbandonaVoloVerdeBtn.setToolTipText("Non puoi abbandonare il volo nella modalità di prova!");
		rossoBtn.setToolTipText((Boolean.TRUE.equals(rossoBtn.getClientProperty("enabled")) ? "Statistiche del giocatore!" : null));
		bluBtn.setToolTipText((Boolean.TRUE.equals(bluBtn.getClientProperty("enabled")) ? "Statistiche del giocatore!" : null));
		gialloBtn.setToolTipText((Boolean.TRUE.equals(gialloBtn.getClientProperty("enabled")) ? "Statistiche del giocatore!" : null));
		verdeBtn.setToolTipText((Boolean.TRUE.equals(verdeBtn.getClientProperty("enabled")) ? "Statistiche del giocatore!" : null));
		mostraNaveRossoBtn.setToolTipText((Boolean.TRUE.equals(mostraNaveRossoBtn.getClientProperty("enabled")) ? "Apri pannello nave!" : null));
		mostraNaveBluBtn.setToolTipText((Boolean.TRUE.equals(mostraNaveBluBtn.getClientProperty("enabled")) ? "Apri pannello nave!" : null));
		mostraNaveGialloBtn.setToolTipText((Boolean.TRUE.equals(mostraNaveGialloBtn.getClientProperty("enabled")) ? "Apri pannello nave!" : null));
		mostraNaveVerdeBtn.setToolTipText((Boolean.TRUE.equals(mostraNaveVerdeBtn.getClientProperty("enabled")) ? "Apri pannello nave!" : null));	

		giorniDiVoloPersiRossoLbl.setText(String.valueOf((giocatoreRosso != null) ? giocatoreRosso.getGiorniDiVoloPersi(): "/"));
		giorniDiVoloPersiBluLbl.setText(String.valueOf((giocatoreBlu != null) ? giocatoreBlu.getGiorniDiVoloPersi(): "/"));
		giorniDiVoloPersiGialloLbl.setText(String.valueOf((giocatoreGiallo != null) ? giocatoreGiallo.getGiorniDiVoloPersi(): "/"));
		giorniDiVoloPersiVerdeLbl.setText(String.valueOf((giocatoreVerde != null) ? giocatoreVerde.getGiorniDiVoloPersi(): "/"));
	}
	
	/**
	 * Posiziona i componenti grafici relativi ai giocatori (es. bottoni o etichette)
	 * all'interno del pannello, ordinandoli per giocatore attivo e non attivo, e
	 * assegnando le coordinate corrette.
	 *
	 * @param componenti        la lista di componenti da posizionare.
	 * @param x                 la coordinata x dei componenti.
	 * @param yPartenza         la coordinata y da cui iniziare a posizionare i componenti.
	 * @param width             la larghezza da assegnare a ciascun componente.
	 * @param height            l'altezza da assegnare a ciascun componente.
	 * @param margine           lo spazio verticale tra i componenti.
	 * @param primoPiazzamento  {@code true} se è il primo piazzamento dei componenti nel pannello; 
	 *                          solo in tal caso i componenti verranno aggiunti al background.
	 * @throws NullPointerException se {@code componenti} è {@code null}.
	 */
	private void posizionaComponentiPerGiocatori(List<JComponent> componenti, int x, int yPartenza, int width, int height, int margine, boolean primoPiazzamento) {
		
		if (componenti == null) {
			String errore = "Il parametro 'componenti' non può essere nullo!";
			LOGGER.error(errore);
			throw new NullPointerException(errore);
		}
		
		List<Colore> tuttiColori = List.of(Colore.ROSSO, Colore.BLU, Colore.GIALLO, Colore.VERDE);
		List<Colore> coloriInGioco = new ArrayList<>();

		// trovo i colori dei giocatori attivi, per ordine corretto
		for (Giocatore g : giocatori) {
		    coloriInGioco.add(g.getColore());
		}

		// aggiungo prima i giocatori attivi (nel giusto ordine)
		int index = 0;
		for (Giocatore g : giocatori) {
		    Colore coloreGiocatore = g.getColore();
		    for (JComponent comp : componenti) {
		        Colore coloreComp = (Colore) comp.getClientProperty("colore");
		        if (coloreGiocatore.equals(coloreComp)) {
		            int y = yPartenza + index * (height + margine);
		            comp.setBounds(x, y, width, height);
		            comp.putClientProperty("enabled", true);
		            if (primoPiazzamento) background.add(comp);
		            index++;
		            break;
		        }
		    }
		    
		    if (!g.isInVolo()) impostaGiocatoreNonInVolo(coloreGiocatore);
		}

		// aggiungo i componenti per i colori non presenti, disabilitati
		for (Colore colore : tuttiColori) {
		    if (!coloriInGioco.contains(colore)) {
		        for (JComponent comp : componenti) {
		            Colore coloreComp = (Colore) comp.getClientProperty("colore");
		            if (colore.equals(coloreComp)) {
		                int y = yPartenza + index * (height + margine);
		                comp.setBounds(x, y, width, height);
		                comp.putClientProperty("outDallaPartita", true);
		                if (primoPiazzamento) background.add(comp);
		                index++;
		                break;
		            }
		        }
		    }
		}
		
		background.revalidate();
		background.repaint();

	}
	
	/**
	 * Disabilita i componenti associati ai giocatori che non partecipano alla partita,
	 * impostando le icone "disabled" e disattivando l'interazione.
	 *
	 * @param componentiDisabilitabili lista di componenti da controllare e potenzialmente disabilitare.
	 * @throws NullPointerException se {@code componentiDisabilitabili} è {@code null}.
	 */
	private void impostaGiocatoriNonInPartita(List<JComponent> componentiDisabilitabili) {
		
		if (componentiDisabilitabili == null) {
			String errore = "Il parametro 'componentiDisabilitabili' non può essere nullo!";
			LOGGER.error(errore);
			throw new NullPointerException(errore);
		}
		
		for (JComponent comp : componentiDisabilitabili) {
			if (Boolean.TRUE.equals(comp.getClientProperty("outDallaPartita"))) {
				JButton btn = (JButton)  comp; // casting sicuro poiché passo solo bottoni
				btn.putClientProperty("enabled", false);
				IconHandler.setIconOnButton(btn, (ImageIcon)btn.getClientProperty("iconaDisabled"), (ImageIcon)btn.getClientProperty("iconaDisabled"));
			}
		}
	}
	
	/**
	 * Imposta la grafica per un giocatore che non è più in volo, disabilitando
	 * i bottoni relativi alla nave e all'abbandono volo, aggiornando le icone/tooltip 
	 * e nascondendo l'etichetta del punto esclamativo associato.
	 *
	 * @param coloreGiocatore il colore del giocatore da aggiornare.
	 * @throws NullPointerException se {@code coloreGiocatore} è {@code null}.
	 */
	private void impostaGiocatoreNonInVolo(Colore coloreGiocatore) {
		
		if (coloreGiocatore == null) {
			String errore = "Il parametro 'coloreGiocatore' non può essere nullo!";
			LOGGER.error(errore);
			throw new NullPointerException(errore);
		}
		
		switch (coloreGiocatore) {
		
		case ROSSO:
			mostraNaveRossoBtn.putClientProperty("enabled", false);
			IconHandler.setIconOnButton(mostraNaveRossoBtn, mostraNaveDisabledIcon, mostraNaveDisabledIcon);
			abbandonaVoloRossoBtn.putClientProperty("enabled", false);
			IconHandler.setIconOnButton(abbandonaVoloRossoBtn, abbandonaVoloDisabledIcon, abbandonaVoloDisabledIcon);
			puntoEsclamativoRossoLbl.setVisible(false);
			mostraNaveRossoBtn.setToolTipText("Questo giocatore non è più in volo!");
			abbandonaVoloRossoBtn.setToolTipText("Questo giocatore non è più in volo!");
			break;
		case BLU:
			mostraNaveBluBtn.putClientProperty("enabled", false);
			IconHandler.setIconOnButton(mostraNaveBluBtn, mostraNaveDisabledIcon, mostraNaveDisabledIcon);
			abbandonaVoloBluBtn.putClientProperty("enabled", false);
			IconHandler.setIconOnButton(abbandonaVoloBluBtn, abbandonaVoloDisabledIcon, abbandonaVoloDisabledIcon);
			puntoEsclamativoBluLbl.setVisible(false);
			mostraNaveBluBtn.setToolTipText("Questo giocatore non è più in volo!");
			abbandonaVoloBluBtn.setToolTipText("Questo giocatore non è più in volo!");
			break;
		case GIALLO:
			mostraNaveGialloBtn.putClientProperty("enabled", false);
			IconHandler.setIconOnButton(mostraNaveGialloBtn, mostraNaveDisabledIcon, mostraNaveDisabledIcon);
			abbandonaVoloGialloBtn.putClientProperty("enabled", false);
			IconHandler.setIconOnButton(abbandonaVoloGialloBtn, abbandonaVoloDisabledIcon, abbandonaVoloDisabledIcon);
			puntoEsclamativoGialloLbl.setVisible(false);
			mostraNaveGialloBtn.setToolTipText("Questo giocatore non è più in volo!");
			abbandonaVoloGialloBtn.setToolTipText("Questo giocatore non è più in volo!");
			break;
		case VERDE:
			mostraNaveVerdeBtn.putClientProperty("enabled", false);
			IconHandler.setIconOnButton(mostraNaveVerdeBtn, mostraNaveDisabledIcon, mostraNaveDisabledIcon);
			abbandonaVoloVerdeBtn.putClientProperty("enabled", false);
			IconHandler.setIconOnButton(abbandonaVoloVerdeBtn, abbandonaVoloDisabledIcon, abbandonaVoloDisabledIcon);
			puntoEsclamativoVerdeLbl.setVisible(false);
			mostraNaveVerdeBtn.setToolTipText("Questo giocatore non è più in volo!");
			abbandonaVoloVerdeBtn.setToolTipText("Questo giocatore non è più in volo!");
			break;
		default:
			break;
		
		}
	}

}