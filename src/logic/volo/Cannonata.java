package galaxytrucker.src.logic.volo;

import galaxytrucker.src.logic.assemblaggio.Cella;
import galaxytrucker.src.logic.assemblaggio.Direzione;
import galaxytrucker.src.logic.assemblaggio.Nave;
import galaxytrucker.src.logic.gioco.Giocatore;

/**
 * Rappresenta un colpo di cannone che impatta una singola cella della nave.
 * Il comportamento del colpo dipende dalla sua dimensione:
 * <ul>
 *   <li>Se è {@code PICCOLO}, viene ignorato solo se la nave è protetta nella
 *   direzione di impatto, altrimenti colpisce e distrugge la tessera (se presente).</li>
 *   <li>Se è {@code GRANDE}, colpisce sempre e distrugge la tessera (se presente).</li>
 * </ul>
 * 
 * @see Colpo
 */
public final class Cannonata extends Colpo {
    
	 /**
     * Crea una nuova cannonata con una posizione, dimensione e direzione di provenienza specificate.
     *
     * @param posizione             la posizione orizzontale o verticale dell'impatto, a seconda della direzione.
     * @param dimensione            la dimensione della cannonata ({@code PICCOLO} o {@code GRANDE}).
     * @param direzioneProvenienza  la direzione da cui proviene la cannonata.
     */
	public Cannonata(int posizione, DimensioneColpo dimensione, Direzione direzioneProvenienza) {
		super(posizione, dimensione, direzioneProvenienza);
	}

    /**
     * Gestisce l'impatto della cannonata sulla nave del giocatore specificato.
     * <p>
     * Se la cella colpita è vuota o fuori dalla griglia, il colpo viene ignorato.
     * Se il colpo è {@code PICCOLO} e la nave è protetta nella direzione di impatto, il colpo è assorbito.
     * In tutti gli altri casi, la tessera viene distrutta.
     *
     * @param giocatore il giocatore la cui nave può essere colpita dal meteorite.
     * @return una stringa HTML che descrive l'esito dell'impatto.
     * @throws NullPointerException se {@code giocatore} o la sua nave sono {@code null}.
     */
    @Override
    public String gestisciImpatto(Giocatore giocatore) {
    	
    	if (giocatore == null) {
			String errore = "Il parametro 'giocatore' non può essere nullo!";
			Evento.getLogger().error(errore);
			throw new NullPointerException(errore);
		}
		
		Nave nave = giocatore.getNave();
		
		if (nave == null) {
			String errore = "L'attributo 'nave' del giocatore non può essere nullo!";
			Evento.getLogger().error(errore);
			throw new NullPointerException(errore);
		}
        
        Cella cella = trovaCellaColpita(nave);     

        if(cella == null) {
        	return "La cannonata ha mancato la nave del giocatore " + nave.getColore()+"!";       	
        }
        
        DimensioneColpo dimensione = getDimensione();
        Direzione direzione = getDirezioneProvenienza();
        
        // caso CANNONATA PICCOLA
        if (dimensione == DimensioneColpo.PICCOLO) {
        	
        	 if (nave.isProtetta(direzione)) {
                 return "<html><div style='text-align:justify;'>"
                      + "Una cannonata piccola ha colpito la nave alla riga " + cella.getCoordinateGioco(nave.getLivello()).getRiga()
                      + " e alla colonna " + cella.getCoordinateGioco(nave.getLivello()).getColonna() + ".<br>"
                      + "Fortunatamente, lo scudo attivo ha assorbito l'impatto e la nave non ha subito danni!<br>";
             }

             cella.rimuoviTessera();
             giocatore.aggiungiDebiti(1); // ogni tessera rimossa costituisce 1 debito
     		 giocatore.getNave().rimuoviAlieniNonPiuSupportati(); // rimuove eventuali alieni che hanno perso il proprio supporto vitale
     		 
     		 return "<html><div style='text-align:justify;'>"
             + "Una cannonata piccola ha colpito la nave e ha distrutto la tessera alla riga " + cella.getCoordinateGioco(nave.getLivello()).getRiga()
             + " e alla colonna " + cella.getCoordinateGioco(nave.getLivello()).getColonna() + "!<br>";
        	
        } else { // caso CANNONATA GRANDE
        	
        	cella.rimuoviTessera();
            giocatore.aggiungiDebiti(1); // ogni tessera rimossa costituisce 1 debito
    		giocatore.getNave().rimuoviAlieniNonPiuSupportati(); // rimuove eventuali alieni che hanno perso il proprio supporto vitale
    		 
    		return "<html><div style='text-align:justify;'>"
           + "Una cannonata grande ha colpito la nave e ha distrutto la tessera alla riga " + cella.getCoordinateGioco(nave.getLivello()).getRiga()
           + " e alla colonna " + cella.getCoordinateGioco(nave.getLivello()).getColonna() + "!<br>";
        }
    	
    }

}