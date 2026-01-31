package galaxytrucker.src.logic.assemblaggio;

import java.util.EnumMap;
import java.util.Map;
import galaxytrucker.src.logic.eccezioni.PiazzamentoEquipaggioNonValidoException;
import galaxytrucker.src.logic.volo.Epidemia;

/**
 * Rappresenta una tessera di tipo cabina all'interno della nave.
 * Una cabina può contenere astronauti e alieni, rispettando specifiche regole di piazzamento.
 * 
 * @see Tessera
 */
public class Cabina extends Tessera {
	
    /** Mappa che associa ogni tipo di {@link Abitante} al numero di presenti nella cabina. */
    private final Map<Abitante, Integer> abitanti;

    /** Indica se questa cabina è quella di partenza. */
    private final boolean diPartenza;

    /** Numero massimo di abitanti che possono occupare una cabina. */
    private static final int MAX_ABITANTI = 2;

    /** Numero massimo di alieni per tipo consentiti in una cabina. */
    private static final int MAX_ALIENI_PER_TIPO = 1;

    /**
     * Costruttore più comune.
     *
     * @param lati         Mappa dei connettori della tessera per ogni direzione.
     * @param pathImmagine Percorso dell'immagine associata alla tessera.
     */
    public Cabina(EnumMap<Direzione, Connettore> lati, String pathImmagine) {
        this(lati, false, false, pathImmagine);
    }

    /**
     * Costruttore completo.
     *
     * @param lati         Mappa dei connettori della tessera per ogni direzione.
     * @param visibile     Specifica se la tessera è visibile.
     * @param diPartenza   Specifica se questa cabina è quella di partenza.
     * @param pathImmagine Percorso dell'immagine associata alla tessera.
     */
    public Cabina(EnumMap<Direzione, Connettore> lati, boolean visibile, boolean diPartenza, String pathImmagine) {
        super(lati, visibile, pathImmagine);
        this.abitanti = new EnumMap<>(Abitante.class);
        for (Abitante a : Abitante.values()) {
            abitanti.put(a, 0);
        }
        this.diPartenza = diPartenza;
    }
    
    /**
     * Verifica se è possibile ospitare un alieno di uno specifico tipo
     * in questa cabina, in base alle tessere adiacenti e alla presenza di
     * supporti vitali che risultino CONNESSI (tramite connettori NON lisci).
     * Inoltre, non è consentito il posizionamento di alieni nella cabina di partenza.
     *
     * @param tipoAlieno         Tipo di alieno da posizionare.
     * @param tessereAdiacenti   Mappa delle tessere adiacenti per direzione.
     * @return {@code true} se presente almeno una tessera adiacente con supporto vitale compatibile, altrimenti {@code false}.
     * @throws NullPointerException     Se uno dei parametri è {@code null}.
     * @throws IllegalArgumentException Se il tipo specificato non è di specie aliena.
      */
     @Override
     public boolean accettaAlieno(Abitante tipoAlieno, Map<Direzione, Tessera> tessereAdiacenti) {
     	
     	if (tipoAlieno == null) {
     		String errore = "Il parametro 'tipoAlieno' non può essere nullo!";
     		getLogger().error(errore);
     		throw new NullPointerException(errore);
     	}
     	
     	if (tessereAdiacenti == null) {
     		String errore = "Il parametro 'tessereAdiacenti' non può essere nullo!";
     		getLogger().error(errore);
     		throw new NullPointerException(errore);
     	}
     	
     	if(tipoAlieno.getSpecie() != Specie.ALIENO) {
     		String errore = "Il parametro 'tipoAlieno' deve essere di specie aliena!";
     		getLogger().error(errore);
     		throw new IllegalArgumentException(errore);
     	}
     	
     	if (diPartenza) return false;// non è possibile piazzare alieni nella cabina di partenza
     	
     	for (Direzione dir : tessereAdiacenti.keySet()) {
     		Tessera t = tessereAdiacenti.get(dir);
     		if (t == null) continue;
     		if (t.getAlienoSupportato() == tipoAlieno && this.getConnettore(dir) != Connettore.LISCIO) return true;
     	}
     	
     	return false;
     }

    
    /**
     * Ridefinisce l'omonimo metodo della classe {@link Tessera}.
     *
     * @return {@code true}, in quanto cabina.
     */
    @Override
    public boolean accettaAstronauta() {
    	return true;
    }
    
    /**
     * Imposta il numero di astronauti presenti nella cabina.
     *
     * @param numero Numero di astronauti da impostare (maggiore o uguale di 0).
     * @throws PiazzamentoEquipaggioNonValidoException Se il numero è negativo o supera la capienza massima.
     */
    public void setNumeroAstronauti(int numero) throws PiazzamentoEquipaggioNonValidoException {
    	
    	if (numero < 0) {
    		String errore = "Non ci sono più astronauti da rimuovere!";
    		getLogger().error(errore);
    		throw new PiazzamentoEquipaggioNonValidoException(errore);
    	}
    	
    	if(numero + getNumeroAbitantiPerTipo(Abitante.ALIENO_MARRONE) + getNumeroAbitantiPerTipo(Abitante.ALIENO_VIOLA) > MAX_ABITANTI) {
    		String errore = "Il numero massimo di abitanti in una cabina è " + MAX_ABITANTI + "!";
    		getLogger().error(errore);
    		throw new PiazzamentoEquipaggioNonValidoException(errore);
    	}
    	
    	abitanti.put(Abitante.ASTRONAUTA, numero);
    	
	}
	
    /**
     * Imposta il numero di alieni di un certo tipo presenti nella cabina.
     * 
     * <p>
     * N.B.: i controlli globali (un solo alieno per tipo su tutta la nave),
	 * devono essere effettuati a monte, ossia nella classe Nave.
     * </p>
     *
     * @param tipoAlieno         Tipo di alieno da posizionare.
     * @param numero             Numero di alieni da impostare (maggiore o uguale di 0).
     * @param tessereAdiacenti   Mappa delle tessere adiacenti per verificare i supporti vitali.
     * @throws PiazzamentoEquipaggioNonValidoException Se violano le regole di piazzamento o di capienza.
     */
	public void setNumeroAlieni(Abitante tipoAlieno, int numero, Map<Direzione, Tessera> tessereAdiacenti) throws PiazzamentoEquipaggioNonValidoException {
		
		if (numero < 0) {
			String errore = "Non ci sono più " + ((tipoAlieno == Abitante.ALIENO_MARRONE) ? "alieni marroni" : "alieni viola") + " da rimuovere!";
    		getLogger().error(errore);
    		throw new PiazzamentoEquipaggioNonValidoException(errore);
    	}
		
		if (!accettaAlieno(tipoAlieno, tessereAdiacenti)) {
			String errore = "Non esiste un supporto vitale adiacente a questa tessera per l'alieno specificato!";
    		getLogger().error(errore);
    		throw new PiazzamentoEquipaggioNonValidoException(errore);
		}
		
		if (numero > 0 && diPartenza) {
			String errore = "Non puoi piazzare degli alieni nella cabina di partenza!";
    		getLogger().error(errore);
    		throw new PiazzamentoEquipaggioNonValidoException(errore);
		}
		
		if (numero + getNumeroAbitantiPerTipo(tipoAlieno) > MAX_ALIENI_PER_TIPO) {
			String errore = "Puoi avere al massimo " + MAX_ALIENI_PER_TIPO + " alieno per colore in ogni cabina!";
    		getLogger().error(errore);
    		throw new PiazzamentoEquipaggioNonValidoException(errore);
		}
		
		if (numero + getNumeroAbitantiTotale() - getNumeroAbitantiPerTipo(tipoAlieno) > MAX_ABITANTI) {
			String errore = "Puoi avere al massimo " + MAX_ABITANTI + " abitanti in ogni cabina!";
    		getLogger().error(errore);
    		throw new PiazzamentoEquipaggioNonValidoException(errore);
		}
		
		abitanti.put(tipoAlieno, numero);
			
	}	
	
	/**
	 * Rimuove gli alieni (abitanti di tipo {@code ALIENO_MARRONE} e {@code ALIENO_VIOLA})
	 * che non sono più supportati in base alle tessere adiacenti fornite.
	 * <p>
	 * Se un tipo di alieno è presente ma non può più essere supportato secondo la logica
	 * del metodo {@code accettaAlieno}, allora viene rimosso (impostato a 0).
	 * </p>
	 *
	 * @param tessereAdiacenti Mappa delle tessere adiacenti per direzione.
	 */
	public void rimuoviAlieniNonPiuSupportati(Map<Direzione, Tessera> tessereAdiacenti) {
		
		if (getNumeroAbitantiPerTipo(Abitante.ALIENO_MARRONE) > 0 && !accettaAlieno(Abitante.ALIENO_MARRONE, tessereAdiacenti)) {
			abitanti.put(Abitante.ALIENO_MARRONE, 0);
		}
		
		if (getNumeroAbitantiPerTipo(Abitante.ALIENO_VIOLA) > 0 && !accettaAlieno(Abitante.ALIENO_VIOLA, tessereAdiacenti)) {
			abitanti.put(Abitante.ALIENO_VIOLA, 0);
		}
		
	}

	/**
     * Determina se esiste un pericolo di epidemia in questa cabina,
     * controllando la presenza di abitanti in tessere adiacenti connesse fra di loro.
     * Da utilizzare nella classe evento {@link Epidemia}.
     *
     * @param tessereAdiacenti Mappa delle tessere adiacenti.
     * @return {@code true} se è possibile un'epidemia, {@code false} altrimenti.
     */
	@Override
	public boolean pericoloEpidemia(Map<Direzione, Tessera> tessereAdiacenti) {

		if (getNumeroAbitantiTotale() == 0) return false;
		
		for (Direzione key : tessereAdiacenti.keySet()) {		
			Tessera t = tessereAdiacenti.get(key);	
			if (t != null && t.getNumeroAbitantiTotale() > 0 && t.getConnettore(key) != Connettore.LISCIO) return true;
		}
		
		return false;
	}
	
	/**
     * Restituisce il numero di abitanti di un certo tipo presenti nella cabina.
     *
     * @param abitante Tipo di abitante da contare.
     * @return Numero di abitanti di quel tipo.
     */
	@Override
	public int getNumeroAbitantiPerTipo(Abitante abitante) {
		return abitanti.get(abitante);
	}
	
	/**
    * Restituisce il numero totale di abitanti presenti nella cabina.
    *
    * @return Numero totale di abitanti.
    */
	@Override
	public int getNumeroAbitantiTotale() {
		
		int numero = 0;
		
		for(Abitante a : Abitante.values()) {
			numero += getNumeroAbitantiPerTipo(a);
		}
		
		return numero;
	}
    
	 /**
     * Ridefinisce l'omonimo metodo della classe {@link Tessera}.
     * Per le cabine restituisce sempre {@code true}, perché non seguono regole di posizionamento specifiche.
     *
     * @param tessereAdiacenti Mappa delle tessere adiacenti.
     * @return {@code true}
     */
	@Override
	public boolean verificaTessera(Map<Direzione, Tessera> tessereAdiacenti) {
		return true;
	}
	
	/**
     * Scarta la cabina nel mucchio delle tessere visibili di tipo {@link Cabina}.
     *
     * @param mucchio Mucchio di tessere.
     */
	@Override
	public void scarta(Mucchio mucchio) {
		mucchio.accettaTesseraVisibile(this, Cabina.class);		
	}
	
	/**
     * Indica se questa cabina è quella di partenza.
     *
     * @return {@code true} se è cabina di partenza, {@code false} altrimenti.
     */
	public boolean isDiPartenza() {
		return diPartenza;
	}

	/**
     * Restituisce una rappresentazione HTML della cabina per interfacce grafiche.
     *
     * @return Stringa HTML contenente il riepilogo della cabina.
     */
	@Override
	public String toString() {
		return 
				"""
		          <html>
		            <div style='text-align: left; font-size: 16px; font-family: sans-serif;'>
		              <ul style='list-style-position: inside; padding: 0; margin: 0;'>
		          """ +
		          "<li><span style='color: #2E86C1;'>Tipo:</span> Cabina</li>" +
		          "<li><span style='color: #2E86C1;'>Astronauti:</span> " + getNumeroAbitantiPerTipo(Abitante.ASTRONAUTA) + "</li>" +
		          "<li><span style='color: #2E86C1;'>Alieni marroni:</span> " + getNumeroAbitantiPerTipo(Abitante.ALIENO_MARRONE) + "</li>" +
		          "<li><span style='color: #2E86C1;'>Alieni viola:</span> " + getNumeroAbitantiPerTipo(Abitante.ALIENO_VIOLA) + "</li>" +
		          "<li><span style='color: #2E86C1;'>Cabina centrale:</span> " + ((diPartenza) ? "Sì" : "No") + "</li>" +
		          """
		              </ul>
		            </div>
		          </html>
		          """;
	}

}