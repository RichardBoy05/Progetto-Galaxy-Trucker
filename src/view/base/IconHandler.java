package galaxytrucker.src.view.base;

import java.awt.Image;
import java.awt.Insets;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import galaxytrucker.src.logic.gioco.GameLogger;

/**
 * Classe di utilità per impostare e aggiornare dinamicamente icone scalabili su componenti
 * {@link JButton} e {@link JLabel}, con supporto per effetto hover e ridimensionamento automatico.
 * <p>
 * La classe non è istanziabile e fornisce solo metodi statici.
 */
public final class IconHandler {
	
	/**
	 * Istanza del {@link GameLogger} utilizzata per registrare eventi di log.
	 */
	private static final GameLogger LOGGER = GameLogger.getInstance();
	
	/**
     * Costruttore privato per impedire l'istanza della classe.
     */
	private IconHandler() {}

	/**
     * Imposta un'icona standard e un'icona hover scalabili su un {@link JButton}.
     * Le icone vengono automaticamente ridimensionate in base alle dimensioni del bottone.
     *
     * @param button    il bottone su cui impostare le icone.
     * @param icon      l'icona predefinita da visualizzare.
     * @param hoverIcon l'icona da visualizzare al passaggio del mouse.
     * 
     * @throws NullPointerException se {@code button}, {@code icon} o {@code hoverIcon} sono {@code null}.
     */
    public static void setIconOnButton(JButton button, ImageIcon icon, ImageIcon hoverIcon) {
    	
    	if (button == null) {
    		String errore = "Il parametro 'button' non può essere nullo!";
    		LOGGER.error(errore);
    		throw new NullPointerException(errore);
    	}
    	
    	if (icon == null) {
    		String errore = "Il parametro 'icon' non può essere nullo!";
    		LOGGER.error(errore);
    		throw new NullPointerException(errore);
    	}
    	
    	if (hoverIcon == null) {
    		String errore = "Il parametro 'hoverIcon' non può essere nullo!";
    		LOGGER.error(errore);
    		throw new NullPointerException(errore);
    	}

        button.putClientProperty("originalIcon", icon.getImage());
        button.putClientProperty("hoverIcon", hoverIcon.getImage());

        if (button.getClientProperty("listenerAdded") == null) { // controllo per aggiungere il listener una sola volta
            button.addComponentListener(new ComponentAdapter() {
                @Override
                public void componentResized(ComponentEvent e) {
                    updateButtonIcon(button);
                }
            });
            button.putClientProperty("listenerAdded", true);
        }

        updateButtonIcon(button);
    }

    /**
     * Imposta un'icona standard e un'icona hover scalabili su un {@link JLabel}.
     * Le icone vengono ridimensionate automaticamente e cambia icona al passaggio del mouse.
     *
     * @param label     l'etichetta su cui impostare le icone.
     * @param icon      l'icona predefinita da visualizzare.
     * @param hoverIcon l'icona da visualizzare al passaggio del mouse.
     * 
     * @throws NullPointerException se {@code label}, {@code icon} o {@code hoverIcon} sono {@code null}.
     */
    public static void setIconOnLabel(JLabel label, ImageIcon icon, ImageIcon hoverIcon) {
        
    	if (label == null) {
    		String errore = "Il parametro 'label' non può essere nullo!";
    		LOGGER.error(errore);
    		throw new NullPointerException(errore);
    	}
    	
    	if (icon == null) {
    		String errore = "Il parametro 'icon' non può essere nullo!";
    		LOGGER.error(errore);
    		throw new NullPointerException(errore);
    	}
    	
    	if (hoverIcon == null) {
    		String errore = "Il parametro 'hoverIcon' non può essere nullo!";
    		LOGGER.error(errore);
    		throw new NullPointerException(errore);
    	}

        label.putClientProperty("originalIcon", icon.getImage());
        label.putClientProperty("hoverIcon", hoverIcon.getImage());

        if (label.getClientProperty("listenerAdded") == null) { // controllo per aggiungere il listener una sola volta
            label.addComponentListener(new ComponentAdapter() {
                @Override
                public void componentResized(ComponentEvent e) {
                    updateLabelIcon(label, false); // non in hover inizialmente
                }
            });

            label.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    updateLabelIcon(label, true); // hover
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    updateLabelIcon(label, false); // ritorno normale
                }
            });

            label.putClientProperty("listenerAdded", true);
        }

        updateLabelIcon(label, false); // icona normale iniziale
    }
    
    /**
     * Rimuove tutte le icone e le relative proprietà da un {@link JButton}.
     *
     * @param button il bottone da cui rimuovere le icone.
     * 
     * @throws NullPointerException se {@code button} è {@code null}.
     */
    public static void clearIconsFromButton(JButton button) {
    	
    	if (button == null) {
    		String errore = "Il parametro 'button' non può essere nullo!";
    		LOGGER.error(errore);
    		throw new NullPointerException(errore);
    	}
    	
        button.setIcon(null);
        button.setRolloverIcon(null);
        button.putClientProperty("originalIcon", null);
        button.putClientProperty("hoverIcon", null);
    }
    
    /**
     * Rimuove tutte le icone e le relative proprietà da un {@link JLabel}.
     *
     * @param label l'etichetta da cui rimuovere le icone.
     * 
     * @throws NullPointerException se {@code label} è {@code null}.
     */
    public static void clearIconsFromLabel(JLabel label) {
    	
    	if (label == null) {
    		String errore = "Il parametro 'label' non può essere nullo!";
    		LOGGER.error(errore);
    		throw new NullPointerException(errore);
    	}
    	
        label.setIcon(null);
        label.putClientProperty("originalIcon", null);
        label.putClientProperty("hoverIcon", null);
    }
    
    /**
     * Aggiorna l'icona e l'icona rollover del {@link JButton} in base alla sua dimensione attuale.
     * Utilizza le immagini memorizzate come proprietà del componente.
     *
     * @param button il bottone da aggiornare.
     * 
     * @throws NullPointerException se {@code button} è {@code null}.
     */
    private static void updateButtonIcon(JButton button) {
    	
    	if (button == null) {
    		String errore = "Il parametro 'button' non può essere nullo!";
    		LOGGER.error(errore);
    		throw new NullPointerException(errore);
    	}
    	
        int w = button.getWidth();
        int h = button.getHeight();
        if (w <= 0 || h <= 0) return; // caso in cui il bottone non ha ancora acquisito la propria dimensione
        
        // calcola gli spazi occupati dal bordo
        Insets insets = (button.getBorder() != null) ? button.getBorder().getBorderInsets(button) : new Insets(0, 0, 0, 0);
        int availableWidth = w - insets.left - insets.right;
        int availableHeight = h - insets.top - insets.bottom;
        if (availableWidth <= 0 || availableHeight <= 0) return;
        
        Image original = (Image) button.getClientProperty("originalIcon");
        Image hover = (Image) button.getClientProperty("hoverIcon");
        if (original != null && hover != null) {
        	
            button.setIcon(new ImageIcon(
                original.getScaledInstance(availableWidth, availableHeight, Image.SCALE_SMOOTH)
            ));
            button.setRolloverIcon(new ImageIcon(
                hover.getScaledInstance(availableWidth, availableHeight, Image.SCALE_SMOOTH)
            ));
        }
    }
    
    /**
     * Aggiorna l'icona del {@link JLabel} in base alla dimensione e allo stato di hover.
     *
     * @param label l'etichetta da aggiornare.
     * @param hover {@code true} per mostrare l'icona hover, {@code false} per quella normale.
     * 
     * @throws NullPointerException se {@code label} è {@code null}.
     */
    private static void updateLabelIcon(JLabel label, boolean hover) {
    	
    	if (label == null) {
    		String errore = "Il parametro 'label' non può essere nullo!";
    		LOGGER.error(errore);
    		throw new NullPointerException(errore);
    	}
    	
        int w = label.getWidth();
        int h = label.getHeight();
        if (w <= 0 || h <= 0) return; // caso in cui l'etichetta non ha ancora acquisito la propria dimensione

        Image image = (Image) label.getClientProperty(hover ? "hoverIcon" : "originalIcon");
        if (image != null) {
            label.setIcon(new ImageIcon(image.getScaledInstance(w, h, Image.SCALE_SMOOTH)));
        }
        
    }

}