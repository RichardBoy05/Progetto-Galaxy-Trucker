package galaxytrucker.src.logic.assemblaggio;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.util.EnumMap;
import java.util.Map;
import javax.swing.ImageIcon;
import galaxytrucker.src.logic.gioco.GameLogger;
import galaxytrucker.src.logic.volo.Merce;

/**
 * Classe astratta che rappresenta una tessera del gioco.
 * Ogni tessera ha dei connettori ai lati, una visibilità, e immagini associate (per l'interfaccia grafica).
 * Fornisce metodi comuni per la gestione dei connettori, la rotazione e la verifica delle connessioni,
 * oltre che dei metodi di default da sovrascrivere nelle sottoclassi laddove necessario.
 */
public abstract class Tessera {

    /** Logger centralizzato. */
    private static final GameLogger LOGGER = GameLogger.getInstance();

    /** Mappa dei connettori ai lati della tessera. */
    private final Map<Direzione, Connettore> lati;

    /** Specifica se la tessera è visibile. */
    private boolean visibile;

    /** Immagine della tessera. */
    private ImageIcon immagine;

    /** Immagine della tessera in stato hovered (al passaggio del mouse). */
    private ImageIcon immagineHovered;

    /**
     * Costruttore base per la tessera, che imposta i lati e il path dell'immagine con visibilità predefinita a {@code false}.
     *
     * @param lati         Mappa dei connettori ai lati della tessera.
     * @param pathImmagine Percorso dell'immagine associata.
     * @throws NullPointerException se {@code lati} o {@code pathImmagine} sono null.
     */
    public Tessera(EnumMap<Direzione, Connettore> lati, String pathImmagine) {
        this(lati, false, pathImmagine);
    }

    /**
     * Costruttore completo per la tessera.
     *
     * @param lati         Mappa dei connettori ai lati della tessera.
     * @param visibile     Specifica se la tessera è visibile.
     * @param pathImmagine Percorso dell'immagine associata.
     * @throws NullPointerException se {@code lati} o {@code pathImmagine} sono null.
     */
    public Tessera(EnumMap<Direzione, Connettore> lati, boolean visibile, String pathImmagine) {

        if (lati == null) {
            String errore = "Il parametro lati non può essere null!";
            LOGGER.error(errore);
            throw new NullPointerException(errore);
        }

        if (pathImmagine == null) {
            String errore = "Il parametro pathImmagine non può essere null!";
            LOGGER.error(errore);
            throw new NullPointerException(errore);
        }

        this.lati = lati;
        this.visibile = visibile;
        this.immagine = new ImageIcon(getClass().getResource(pathImmagine));
        this.immagineHovered = new ImageIcon(getClass().getResource(pathImmagine.split("\\.")[0] + "_hover.png"));
    }

    /**
     * Imposta un connettore in una determinata direzione.
     *
     * @param direzione  Direzione del connettore.
     * @param connettore Connettore da impostare.
     */
    public void setConnettore(Direzione direzione, Connettore connettore) {
        lati.put(direzione, connettore);
    }

    /**
     * Restituisce il connettore in una determinata direzione.
     *
     * @param direzione Direzione del connettore.
     * @return Connettore presente in quella direzione.
     */
    public Connettore getConnettore(Direzione direzione) {
        return lati.get(direzione);
    }

    /**
     * Ruota la tessera di 90° in senso orario, aggiornando connettori, componenti e immagine.
     */
    public void ruotaTesseraOrario() {
        ruotaConnettoriOrario();
        ruotaComponentiOrario();
        ruotaImmagineOrario();
    }

    /**
     * Metodo per ruotare eventuali componenti interni della tessera.
     * Implementazione di default vuota.
     */
    protected void ruotaComponentiOrario() {
        return;
    }

    /**
     * Ruota i connettori della tessera di 90° in senso orario.
     */
    private void ruotaConnettoriOrario() {
        Connettore nord = getConnettore(Direzione.NORD);
        Connettore est = getConnettore(Direzione.EST);
        Connettore sud = getConnettore(Direzione.SUD);
        Connettore ovest = getConnettore(Direzione.OVEST);

        setConnettore(Direzione.NORD, ovest);
        setConnettore(Direzione.EST, nord);
        setConnettore(Direzione.SUD, est);
        setConnettore(Direzione.OVEST, sud);
    }

    /**
     * Applica la rotazione dell'immagine agli attributi corrispondenti della tessera.
     *
     * @throws NullPointerException se le immagini non sono state caricate correttamente.
     */
    private void ruotaImmagineOrario() {
        if (immagine == null || immagineHovered == null) {
            throw new NullPointerException("L'immagine associata alla tessera non può essere nulla!");
        }

        immagine = getImmagineRuotata(immagine.getImage());
        immagineHovered = getImmagineRuotata(immagineHovered.getImage());
    }

    /**
     * Ruota un'immagine di 90° in senso orario.
     *
     * @param img Immagine da ruotare.
     * @return Immagine ruotata.
     */
    private ImageIcon getImmagineRuotata(Image img) {
        int w = img.getWidth(null);
        int h = img.getHeight(null);
        BufferedImage src = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2dSrc = src.createGraphics();
        g2dSrc.drawImage(img, 0, 0, null);
        g2dSrc.dispose();

        BufferedImage rotated = new BufferedImage(h, w, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2dRot = rotated.createGraphics();
        g2dRot.translate(h, 0);
        g2dRot.rotate(Math.toRadians(90));
        g2dRot.drawImage(src, 0, 0, null);
        g2dRot.dispose();

        return new ImageIcon(rotated);
    }

    /**
     * Verifica se le connessioni tra questa tessera e quelle adiacenti sono valide.
     *
     * @param tessereAdiacenti Mappa delle tessere adiacenti.
     * @return {@code true} se tutte le connessioni sono compatibili, {@code false} altrimenti.
     */
    public boolean verificaConnessioni(Map<Direzione, Tessera> tessereAdiacenti) {
        for (Direzione direzione : tessereAdiacenti.keySet()) {
            Tessera adiacente = tessereAdiacenti.get(direzione);
            if (adiacente == null) continue;

            Connettore mioConnettore = getConnettore(direzione);
            Connettore connettoreAdiacente = adiacente.getConnettore(direzione.opposta());

            if (!mioConnettore.isCompatibile(connettoreAdiacente)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Verifica se la tessera rispetta le regole specifiche di validità.
     *
     * @param tessereAdiacenti Mappa delle tessere adiacenti.
     * @return {@code true} se valida, {@code false} altrimenti.
     */
    public abstract boolean verificaTessera(Map<Direzione, Tessera> tessereAdiacenti);

    /**
     * Scarta la tessera, inserendola nel mucchio specificato.
     *
     * @param mucchio Il mucchio di destinazione.
     */
    public abstract void scarta(Mucchio mucchio);

    // Metodi di default per caratteristiche specifiche delle tessere, da ridefinire nelle sottoclassi

    public boolean isProtetta(Direzione direzione) {
    	return false;
    }
    
    public int getPotenzaMotrice() {
    	return 0;
    }
    
    public double getPotenzaDiFuoco() {
    	return 0;
    }
    
    public boolean colpisceVerso(Direzione direzione) {
    	return false;
    }
    
    public Abitante getAlienoSupportato() {
		return null;
	}
	
	public boolean accettaAlieno(Abitante tipoAlieno, Map<Direzione, Tessera> tessereAdiacenti) {
    	return false;
    }
    
    public boolean accettaAstronauta() {
    	return false;
    }
    
    public int getNumeroAbitantiPerTipo(Abitante abitante) {
		return 0;
	}
	
	public int getNumeroAbitantiTotale() {
		return 0;
	}
    
	public boolean pericoloEpidemia(Map<Direzione, Tessera> tessereAdiacenti) {
		return false;
	}
	
	public boolean isAttivabile() {
		return false;
	}
	
	public boolean accettaMerce() {
		return false;
	}
	
	public int getMerce(Merce merce) {
		return 0;
	}
	
	public boolean fornisceBatterie() {
		return false;
	}
	
	public int getBatterie() {
		return 0;
	}

    // Getters e setters

    public boolean isVisibile() {
        return visibile;
    }

    public void setVisibile(boolean visibile) {
        this.visibile = visibile;
    }

    public ImageIcon getImmagine() {
        return immagine;
    }

    public ImageIcon getImmagineHovered() {
        return immagineHovered;
    }

    /**
     * Restituisce il logger di gioco.
     *
     * @return Istanza del {@code GameLogger}.
     */
    protected static GameLogger getLogger() {
        return LOGGER;
    }
}