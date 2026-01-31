package galaxytrucker.src.view.components;

import java.awt.Color;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.JTextField;

/**
 * La classe estende {@link JTextField} per fornire
 * un campo di testo con funzionalità di placeholder.
 * <p>
 * Quando il campo è vuoto e non ha il focus, viene mostrato un testo grigio
 * indicante il testo del placeholder. Al focus, il placeholder viene
 * rimosso se non è stato modificato.
 * <p>
 * Viene utilizzata nella classe {@code SetupGui} per semplificare
 * l'inserimento di input utente con indicazioni visive.
 * 
 */
public class PlaceholderTextField extends JTextField {

    private static final long serialVersionUID = 1L;
    
    /** Testo da visualizzare quando il campo è vuoto e non ha il focus. */
    private String placeholder;

    /**
     * Crea un nuovo {@code PlaceholderTextField} con il testo di placeholder specificato.
     *
     * @param placeholder il testo da visualizzare come suggerimento quando il campo è vuoto
     */
    public PlaceholderTextField(String placeholder) {
        this.placeholder = placeholder;

        setText(placeholder);
        setForeground(Color.GRAY);

        this.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (getText().equals(placeholder)) {
                    setText("");
                    setForeground(Color.BLACK);
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                resetPlaceholderIfNeeded();
            }
        });

        this.addPropertyChangeListener("enabled", new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                if (isEnabled()) {
                    resetPlaceholderIfNeeded();
                }
            }
        });
    }

    /**
     * Reimposta il placeholder nel campo di testo se è vuoto.
     * Viene utilizzato quando il focus viene perso o lo stato abilitato cambia.
     */
    private void resetPlaceholderIfNeeded() {
        if (getText().isEmpty()) {
            setText(placeholder);
            setForeground(Color.GRAY);
        }
    }

    /**
     * Restituisce il testo effettivamente inserito dall'utente, escludendo il placeholder.
     *
     * @return il contenuto del campo di testo se differente dal placeholder, altrimenti stringa vuota
     */
    public String getRealText() {
        return getText().equals(placeholder) ? "" : getText();
    }
}