package galaxytrucker.src.view.components;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.LineBorder;
import galaxytrucker.src.logic.assemblaggio.Nave;
import galaxytrucker.src.logic.assemblaggio.Tessera;
import galaxytrucker.src.logic.gioco.GameLogger;
import galaxytrucker.src.view.base.AzioneClickCella;
import galaxytrucker.src.view.base.IconHandler;

/**
 * Rappresenta un pannello grafico Swing ({@link JPanel}) per visualizzare la griglia della nave.
 * Mostra le celle piazzabili e le tessere eventualmente collocate, con supporto per interazioni tramite click.
 * <p>
 * Ogni cella è disegnata in base alla forma della nave e può essere cliccabile a seconda della configurazione.
 * I bordi includono etichette con le coordinate di gioco.
 */
public class PannelloNave extends JPanel {
    
    private static final long serialVersionUID = 1L;
    private static final GameLogger LOGGER = GameLogger.getInstance();
    
    private final Nave nave;
    private final JPanel gridContainer;
    private final AzioneClickCella azioneClick;
    private boolean tessereCliccabiliDiDefault;

    /**
     * Costruisce un nuovo pannello nave.
     *
     * @param nave l'oggetto {@link Nave} da visualizzare.
     * @param tessereCliccabiliDiDefault se {@code true} le tessere saranno considerate come cliccabili
     * e avranno effetto hover al passaggio del mouse. Se {@code false}, non avranno alcun effetto al
     * passaggio del mouse, rendendo chiaro all'utente che non è prevista alcuna interazione con loro.
     * @param azioneClick l'azione da eseguire al click su una cella.
     * 
     * @throws NullPointerException se {@code nave} o {@code azioneClick} è {@code null}.
     */
    public PannelloNave(Nave nave, boolean tessereCliccabiliDiDefault, AzioneClickCella azioneClick) {
    	
    	if (nave == null) {
    		String errore = "Il parametro 'nave' non può essere nullo!";
    		LOGGER.error(errore);
    		throw new NullPointerException(errore);
    	}
    	
    	if (azioneClick == null) {
    		String errore = "Il parametro 'azioneClick' non può essere nullo!";
    		LOGGER.error(errore);
    		throw new NullPointerException(errore);
    	}
    	
        this.nave = nave;
        this.tessereCliccabiliDiDefault = tessereCliccabiliDiDefault;
        this.azioneClick = azioneClick;
        setLayout(new BorderLayout());
        
        int righe = nave.getLivello().getFormaNave().length;
        int colonne = nave.getLivello().getFormaNave()[0].length;  
        gridContainer = new JPanel(new GridLayout(righe, colonne));
        add(creaGriglia(righe, colonne), BorderLayout.CENTER);
    }

    /**
     * Crea il contenitore della griglia con intestazioni e bordi numerati.
     *
     * @param righe numero di righe della griglia.
     * @param colonne numero di colonne della griglia.
     * @return il pannello contenente la griglia e le etichette.
     */
    private JPanel creaGriglia(int righe, int colonne) {
        JPanel wrapper = new JPanel(new BorderLayout());

        wrapper.add(creaHeaderColonne(colonne), BorderLayout.NORTH);
        wrapper.add(creaFooterColonne(colonne), BorderLayout.SOUTH);
        wrapper.add(creaEtichettaRighe(righe), BorderLayout.WEST);
        wrapper.add(creaEtichettaRighe(righe), BorderLayout.EAST);

        generaCelle(righe, colonne);
        wrapper.add(gridContainer, BorderLayout.CENTER);

        return wrapper;
    }

    /**
     * Genera le celle della griglia e le aggiunge al contenitore.
     *
     * @param righe numero di righe.
     * @param colonne numero di colonne.
     */
    private void generaCelle(int righe, int colonne) {
        boolean[][] formaNave = nave.getLivello().getFormaNave();
        for (int r = 0; r < righe; r++) {
            for (int c = 0; c < colonne; c++) {
                JLabel cella = creaCella(r, c, formaNave[r][c]);
                gridContainer.add(cella);
            }
        }
    }

    /**
     * Crea una singola cella della griglia come JLabel.
     * La cella può contenere una tessera e reagisce agli eventi del mouse.
     *
     * @param riga indice della riga.
     * @param colonna indice della colonna.
     * @param piazzabile {@code true} se la cella è potenzialmente valida per il piazzamento,
     * in base a quanto riportato nella matrice della nave, che dipende dal {@link Livello}.
     * Altrimenti {@code false}.
     * @return la JLabel che rappresenta la cella.
     */
    private JLabel creaCella(int riga, int colonna, boolean piazzabile) {
        JLabel label = new JLabel();
        label.setOpaque(true);
        label.setHorizontalAlignment(SwingConstants.CENTER);
        label.setPreferredSize(new Dimension(50, 50));
        label.setBorder(new LineBorder(Color.GRAY));
        
        // il colore del background varia in base al fatto che la cella sia piazzabile o meno.
        label.setBackground(piazzabile ? nave.getLivello().getColoreCelleValide() : nave.getLivello().getColoreCelleNonValide());
        
        Tessera t = nave.getGriglia().getCella(riga, colonna).getTessera();
        
        // di default si applica l'immagine hovered solo se le tessere sono cliccabili.
        if (t != null) IconHandler.setIconOnLabel(label, t.getImmagine(), (tessereCliccabiliDiDefault) ? t.getImmagineHovered() : t.getImmagine()); 

        label.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                if (piazzabile && label.getIcon() == null) {
                    label.setBackground(effettoHover(label.getBackground(), 0.2f));
                }
            }

            @Override
            public void mouseExited(MouseEvent e) {
                if (piazzabile && label.getIcon() == null) {
                    label.setBackground(nave.getLivello().getColoreCelleValide());
                }
            }

            @Override
            public void mousePressed(MouseEvent e) {
                if (!piazzabile) return; // click ammesso solo sulle celle in cui è possibile piazzare le tessere
                
                if (azioneClick != null) {
                    azioneClick.onClickCella(riga, colonna, e);
                }
                
            }
        });   

        return label;
    }

    /**
     * Crea il pannello con le etichette numeriche delle colonne (header).
     *
     * @param colonne numero di colonne.
     * @return il pannello contenente le etichette.
     */
    private JPanel creaHeaderColonne(int colonne) {
        JPanel panel = new JPanel(new GridLayout(1, colonne));
        for (int c = 0; c < colonne; c++) {
        	int id = nave.getGriglia().getCella(0, c).getCoordinateGioco(nave.getLivello()).getColonna();
        	panel.add(new JLabel("" + String.valueOf(id) + " ", SwingConstants.CENTER));
        }
        return panel;
    }

    /**
     * Crea il pannello con le etichette numeriche delle colonne (footer).
     *
     * @param colonne numero di colonne.
     * @return il pannello contenente le etichette.
     */
    private JPanel creaFooterColonne(int colonne) {
        JPanel panel = new JPanel(new GridLayout(1, colonne));
        for (int c = 0; c < colonne; c++) {
        	int id = nave.getGriglia().getCella(0, c).getCoordinateGioco(nave.getLivello()).getColonna();
        	panel.add(new JLabel(String.valueOf(id), SwingConstants.CENTER));
        }
        return panel;
    }

    /**
     * Crea il pannello con le etichette numeriche delle righe.
     *
     * @param righe numero di righe.
     * @return il pannello contenente le etichette.
     */
    private JPanel creaEtichettaRighe(int righe) {
        JPanel panel = new JPanel(new GridLayout(righe, 1));
        for (int r = 0; r < righe; r++) {
        	int id = nave.getGriglia().getCella(r, 0).getCoordinateGioco(nave.getLivello()).getRiga();
            panel.add(new JLabel(" " + String.valueOf(id) + " ", SwingConstants.CENTER));
        }
        return panel;
    }
    
    /**
     * Restituisce il componente JLabel corrispondente alla cella specificata nella griglia.
     * Utile per eseguirci operazioni e riportare visivamente quel che avviene internamente nella nave.
     *
     * @param riga indice della riga.
     * @param colonna indice della colonna.
     * @return la JLabel corrispondente.
     * @throws IllegalArgumentException se la riga o colonna è fuori dai limiti.
     */
    public JLabel getLabelCella(int riga, int colonna) {
    	
    	int numeroRighe = nave.getLivello().getFormaNave().length;
    	int numeroColonne = nave.getLivello().getFormaNave()[0].length;
    	
    	if (riga < 0 || riga >= numeroRighe) {
    		String errore = "La riga massima della griglia dev'essere compresa tra 0 e " + numeroRighe +"!";
    		LOGGER.error(errore);
    		throw new IllegalArgumentException(errore);
    	}
    	
    	if (colonna < 0 || colonna >= numeroColonne) {
    		String errore = "La colonna massima della griglia dev'essere compresa tra 0 e " + numeroColonne +"!";
    		LOGGER.error(errore);
    		throw new IllegalArgumentException(errore);
    	}
    	   	
    	return (JLabel) gridContainer.getComponent(riga * numeroColonne + colonna);
    }

    /**
     * Applica un effetto di schiarimento al colore di sfondo, simulando un hover event.
     * <p>
     * Risulta utile nel momento in cui la cella è ancora vuota (senza tessera) e si
     * vuole evidenziare il passaggio del mouse. In presenza di una tessera, invece,
     * l'effetto hover è caratteristico dell'immagine associata.
     *
     * @param colore il colore originale.
     * @param percentuale percentuale dell'effetto.
     * @return un nuovo colore schiarito.
     */
    private Color effettoHover(Color colore, float percentuale) {
        percentuale = Math.max(0f, Math.min(1f, percentuale));
        int r = colore.getRed() + Math.round((255 - colore.getRed()) * percentuale);
        int g = colore.getGreen() + Math.round((255 - colore.getGreen()) * percentuale);
        int b = colore.getBlue() + Math.round((255 - colore.getBlue()) * percentuale);
        return new Color(r, g, b);
    }
}