package galaxytrucker.src.logic.assemblaggio;


/**
 * Enumerazione che rappresenta le quattro direzioni cardinali, che identificano i quattro lati di ogni tessera:
 * {@code NORD}, {@code EST}, {@code SUD}, {@code OVEST}.
 * <p>
 * Fornisce metodi per ottenere la direzione opposta e quella ruotata di 90° in senso orario.
 */
public enum Direzione {
	NORD, EST, SUD, OVEST;
	
	/**
     * Restituisce la direzione opposta a quella corrente.
     *
     * @return La direzione opposta, oppure {@code null} se non riconosciuta.
     */
	
	public Direzione opposta() {
		
        switch (this) {
            case NORD: return SUD;
            case SUD: return NORD;
            case EST: return OVEST;
            case OVEST: return EST;
            
            default: return null; 
           
        }
        
    }
	
	/**
     * Restituisce la direzione risultante dalla rotazione di 90° in senso orario rispetto a quella corrente.
     *
     * @return La direzione ruotata di 90° in senso orario, oppure {@code null} se non riconosciuta.
     */
	
	public Direzione orario90Gradi() {
		
	    switch (this) {
	        case NORD: return EST;
	        case EST: return SUD;
	        case SUD: return OVEST;
	        case OVEST: return NORD;
	        
	        default: return null;
	    }
	}

    /**
     * Restituisce la variazione sulla coordinata della Riga (X) per muoversi in questa direzione.
     *
     * @return -1 per NORD, +1 per SUD, 0 per EST ed OVEST.
     */

     public int deltaRiga() {
        switch (this) {
            case NORD: return -1; //NORD -> verso l'alto
            case SUD: return 1;  //SUD -> verso il basso
            default: return 0;    //altrimenti -> nessun movimento
        }
    }

    /**
     * Restituisce la variazione sulla coordinata della Colonna (Y) per muoversi in questa direzione.
     *
     * @return +1 per EST, -1 per OVEST, 0 per NORD e SUD.
     */

     public int deltaColonna() {
        switch (this) {
            case EST: return 1; //EST -> verso destra
            case OVEST: return -1;  //OVEST -> verso sinistra
            default: return 0;    //altrimenti -> nessun movimento
        }
    }
	
	 /**
     * Restituisce una rappresentazione testuale della direzione, con la prima lettera maiuscola e le altre minuscole.
     *
     * @return Stringa rappresentativa della direzione.
     */
	@Override
	public String toString() {
		return String.valueOf(name().charAt(0)) + name().toLowerCase().substring(1);
	}
}