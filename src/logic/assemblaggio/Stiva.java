package galaxytrucker.src.logic.assemblaggio;

import java.util.EnumMap;
import java.util.Map;
import galaxytrucker.src.logic.eccezioni.CaricamentoMerceNonValidoException;
import galaxytrucker.src.logic.volo.Merce;

/**
 * Classe che rappresenta una stiva, ossia una tessera che consente di caricare diverse quantità di merci,
 * rispettando i vincoli di capienza e di compatibilità tra il tipo di stiva e il tipo di merce.
 *
 * @see Tessera
 */
public class Stiva extends Tessera {

    /** Numero di scomparti disponibili nella stiva. */
    private final int numeroScomparti;

    /** Mappa che associa ogni tipo di {@link Merce} alla quantità presente nella stiva. */
    private final Map<Merce, Integer> merci;

    /** Indica se la stiva è speciale (può trasportare merce preziosa). */
    private final boolean speciale;

    /**
     * Costruttore più comune.
     *
     * @param lati             Mappa dei connettori della tessera per ogni direzione.
     * @param numeroScomparti  Numero di scomparti disponibili nella stiva.
     * @param speciale         {@code true} se la stiva è speciale, {@code false} altrimenti.
     * @param pathImmagine     Percorso dell'immagine associata alla tessera.
     * @throws IllegalArgumentException se il numero di scomparti non è valido rispetto al tipo di stiva.
     */
    public Stiva(EnumMap<Direzione, Connettore> lati, int numeroScomparti, boolean speciale, String pathImmagine) {
        this(lati, false, numeroScomparti, speciale, pathImmagine);
    }

    /**
     * Costruttore completo.
     *
     * @param lati             Mappa dei connettori della tessera per ogni direzione.
     * @param visibile         Specifica se la tessera è visibile.
     * @param numeroScomparti  Numero di scomparti disponibili nella stiva.
     * @param speciale         {@code true} se la stiva è speciale, {@code false} altrimenti.
     * @param pathImmagine     Percorso dell'immagine associata alla tessera.
     * @throws IllegalArgumentException se il numero di scomparti non è valido rispetto al tipo di stiva.
     */
    public Stiva(EnumMap<Direzione, Connettore> lati, boolean visibile, int numeroScomparti, boolean speciale, String pathImmagine) {
        super(lati, visibile, pathImmagine);

        if (speciale && (numeroScomparti < 1 || numeroScomparti > 2)) {
            String errore = "Le stive speciali possono avere solo 1 o 2 scomparti!";
            getLogger().error(errore);
            throw new IllegalArgumentException(errore);
        } else if (!speciale && (numeroScomparti < 2 || numeroScomparti > 3)) {
            String errore = "Le stive normali possono avere solo 2 o 3 scomparti!";
            getLogger().error(errore);
            throw new IllegalArgumentException(errore);
        }

        this.numeroScomparti = numeroScomparti;
        this.speciale = speciale;
        this.merci = new EnumMap<>(Merce.class);

        for (Merce a : Merce.values()) {
            merci.put(a, 0);
        }
    }

    /**
     * Verifica se la tessera può accettare merce, sempre vero in una stiva.
     *
     * @return {@code true}.
     */
    @Override
    public boolean accettaMerce() {
        return true;
    }

    /**
     * Restituisce la quantità di una specifica merce presente nella stiva.
     *
     * @param merce Tipo di merce.
     * @return Quantità presente.
     */
    @Override
    public int getMerce(Merce merce) {
        return merci.get(merce);
    }

    /**
     * Restituisce il numero totale di merci presenti nella stiva.
     *
     * @return Quantità totale di merce.
     */
    public int getMerceTotale() {
        int numero = 0;
        for (Merce m : Merce.values()) {
            numero += getMerce(m);
        }
        return numero;
    }

    /**
     * Imposta la quantità di una specifica merce nella stiva, verificando i vincoli.
     *
     * @param merce  Tipo di merce da caricare.
     * @param numero Quantità da impostare.
     * @throws NullPointerException                 se {@code merce} è {@code null}.
     * @throws CaricamentoMerceNonValidoException   se il caricamento viola i vincoli di stiva.
     */
    public void setMerce(Merce merce, int numero) throws CaricamentoMerceNonValidoException {
        if (merce == null) {
            String errore = "Il parametro 'merce' non può essere nullo!";
            getLogger().error(errore);
            throw new NullPointerException(errore);
        }

        if (numero < 0) {
            String errore = "Merce " + merce + " esaurita, non puoi rimuoverne altra!";
            getLogger().error(errore);
            throw new CaricamentoMerceNonValidoException(errore);
        }

        if (merce.isPreziosa() && !this.isSpeciale()) {
            String errore = "Non puoi inserire della merce preziosa su una stiva normale! Deve essere una stiva speciale!";
            getLogger().error(errore);
            throw new CaricamentoMerceNonValidoException(errore);
        }

        if (numero + getMerceTotale() - getMerce(merce) > numeroScomparti) {
            String errore = "Hai esaurito lo spazio nella stiva (numero di scomparti: " + numeroScomparti + ")!";
            getLogger().error(errore);
            throw new CaricamentoMerceNonValidoException(errore);
        }

        merci.put(merce, numero);
    }

    /**
     * Verifica se la tessera può essere collocata, sempre valida per le stive.
     *
     * @param tessereAdiacenti Mappa delle tessere adiacenti.
     * @return {@code true} sempre.
     */
    @Override
    public boolean verificaTessera(Map<Direzione, Tessera> tessereAdiacenti) {
        return true;
    }

    /**
     * Restituisce il numero di scomparti disponibili nella stiva.
     *
     * @return Numero di scomparti.
     */
    public int getNumeroScomparti() {
        return numeroScomparti;
    }

    /**
     * Verifica se la stiva è speciale.
     *
     * @return {@code true} se speciale, {@code false} altrimenti.
     */
    public boolean isSpeciale() {
        return speciale;
    }

    /**
     * Scarta la stiva, inserendola nel mucchio visibile associato alla sua classe.
     *
     * @param mucchio Il mucchio in cui scartare la tessera.
     */
    @Override
    public void scarta(Mucchio mucchio) {
        mucchio.accettaTesseraVisibile(this, Stiva.class);
    }

    
    /**
     * Restituisce una rappresentazione testuale HTML della tessera.
     *
     * @return Stringa HTML con le informazioni della stiva.
     */
    @Override
    public String toString() {
        return
            """
              <html>
                <div style='text-align: left; font-size: 16px; font-family: sans-serif;'>
                  <ul style='list-style-position: inside; padding: 0; margin: 0;'>
              """ +
            "<li><span style='color: #2E86C1;'>Tipo:</span> Stiva</li>" +
            "<li><span style='color: #2E86C1;'>Speciale:</span> " + (speciale ? "Sì" : "No") + "</li>" +
            "<li><span style='color: #2E86C1;'>Merce rossa:</span> " + merci.get(Merce.ROSSA) + "</li>" +
            "<li><span style='color: #2E86C1;'>Merce blu:</span> " + merci.get(Merce.BLU) + "</li>" +
            "<li><span style='color: #2E86C1;'>Merce gialla:</span> " + merci.get(Merce.GIALLA) + "</li>" +
            "<li><span style='color: #2E86C1;'>Merce verde:</span> " + merci.get(Merce.VERDE) + "</li>" +
            """
                  </ul>
                </div>
              </html>
            """;
    }
}