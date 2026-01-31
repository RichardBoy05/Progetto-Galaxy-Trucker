package galaxytrucker.src.logic.volo;

import galaxytrucker.src.logic.assemblaggio.Cella;
import galaxytrucker.src.logic.assemblaggio.Connettore;
import galaxytrucker.src.logic.assemblaggio.Direzione;
import galaxytrucker.src.logic.assemblaggio.Griglia;
import galaxytrucker.src.logic.assemblaggio.Nave;
import galaxytrucker.src.logic.assemblaggio.Tessera;
import galaxytrucker.src.logic.gioco.Giocatore;
import galaxytrucker.src.logic.gioco.Livello;

/**
 * Rappresenta un meteorite che impatta una singola cella della nave.
 * <p>
 * A seconda delle caratteristiche del meteorite e della configurazione della nave, l'impatto può:
 * <ul>
 *   <li>mancare completamente la nave</li>
 *   <li>essere assorbito da uno scudo (solo meteorite {@code PICCOLO}) </li>
 *   <li>essere respinto da un connettore liscio (solo meteorite {@code PICCOLO}) </li>
 *   <li>essere intercettato da un cannone (solo meteorite {@code GRANDE}) </li>
 *   <li>distruggere una tessera colpita</li>
 * </ul>
 * 
 * @see Colpo
 */
public final class Meteorite extends Colpo {

	/**
     * Crea un nuovo meteorite con una posizione, dimensione e direzione di provenienza specificate.
     *
     * @param posizione             la posizione orizzontale o verticale dell'impatto, a seconda della direzione.
     * @param dimensione            la dimensione del meteorite ({@code PICCOLO} o {@code GRANDE}).
     * @param direzioneProvenienza  la direzione da cui proviene il meteorite.
     */
	public Meteorite(int posizione, DimensioneColpo dimensione, Direzione direzioneProvenienza) {
		super(posizione, dimensione, direzioneProvenienza);
	}

	/**
     * Gestisce l'impatto del meteorite sulla nave del giocatore specificato.
     * <p>
     * A seconda della posizione di impatto e dello stato della nave, il meteorite può causare effetti differenti
     * e generare un messaggio descrittivo dell'evento.
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
        	return "Il meteorite ha mancato la nave del giocatore " + nave.getColore()+"!";
        	
        }
        
        Tessera tessera = cella.getTessera(); // non nulla grazie al controllo effettuato in trovaCellaColpita()

        DimensioneColpo dimensione = getDimensione();
        Direzione direzione = getDirezioneProvenienza();

        // caso METEORITE PICCOLO
        if(dimensione == DimensioneColpo.PICCOLO) {
            Connettore connettore = tessera.getConnettore(direzione);
            if(connettore == Connettore.LISCIO){
            	return "<html><div style='text-align:justify;'>"
            		       + "Un meteorite piccolo ha colpito la nave alla riga " +cella.getCoordinateGioco(nave.getLivello()).getRiga()
            		       + " e alla colonna " +cella.getCoordinateGioco(nave.getLivello()).getColonna()
            		       + "! Tuttavia, essendo <br>di piccole dimensioni e non avendo colpito connettori esposti, ha semplicemente rimbalzato!<br>";
            }
            
            // caso in cui colpisce un CONNETTORE esposto
            if (nave.isProtetta(direzione)) {
                return "<html><div style='text-align:justify;'>"
                     + "Un meteorite piccolo ha colpito la nave alla riga " + cella.getCoordinateGioco(nave.getLivello()).getRiga()
                     + " e alla colonna " + cella.getCoordinateGioco(nave.getLivello()).getColonna() + ".<br>"
                     + "Fortunatamente, lo scudo attivo ha assorbito l'impatto e la nave non ha subito danni!<br>";
            }

            cella.rimuoviTessera();
            giocatore.aggiungiDebiti(1); // ogni tessera rimossa costituisce 1 debito
    		giocatore.getNave().rimuoviAlieniNonPiuSupportati(); // rimuove eventuali alieni che hanno perso il proprio supporto vitale
    		
            return "<html><div style='text-align:justify;'>"
                 + "Un meteorite piccolo ha colpito la nave e ha distrutto la tessera alla riga " + cella.getCoordinateGioco(nave.getLivello()).getRiga()
                 + " e alla colonna " + cella.getCoordinateGioco(nave.getLivello()).getColonna() + "!<br>";

        } else { // caso METEORITE GRANDE
        	
        	if (intercettaMeteorite(nave)) {
        	    return "<html><div style='text-align:justify;'>"
        	         + "Un meteorite grande è stato intercettato <br>da un cannone orientato verso " + direzione + "!</div></html>";
        	}

        	cella.rimuoviTessera();
        	giocatore.aggiungiDebiti(1); // ogni tessere rimossa costituisce 1 debito
    		giocatore.getNave().rimuoviAlieniNonPiuSupportati(); // rimuove eventuali alieni che hanno perso il proprio supporto vitale
    		
        	return "<html><div style='text-align:justify;'>"
        	     + "Un meteorite grande ha colpito la nave, distruggendo la tessera nella riga "
        	     + cella.getCoordinateGioco(nave.getLivello()).getRiga() + " e nella colonna "
        	     + cella.getCoordinateGioco(nave.getLivello()).getColonna() + ".</div></html>";
        	
        }
        
	}
	
	/**
     * Verifica se un meteorite grande viene intercettato da un cannone presente sulla nave.
     * <p>
     * L'intercettazione dipende dalla direzione del meteorite e dal livello della nave:
     * <ul>
     *   <li>Per colpi da Nord/Sud: si controllano le righe in una data colonna (estese nei livelli avanzati)</li>
     *   <li>Per colpi da Est/Ovest: si controllano le colonne in una data riga (estese nei livelli avanzati)</li>
     * </ul>
     *
     * @param nave la nave che potrebbe intercettare il meteorite.
     * @return {@code true} se un cannone intercetta il meteorite, {@code false} altrimenti.
     * @throws NullPointerException se {@code nave} è {@code null}.
     */
	private boolean intercettaMeteorite(Nave nave) {
		
		if (nave == null) {
			String errore = "Il parametro 'nave' non può essere nullo!";
			Evento.getLogger().error(errore);
			throw new NullPointerException(errore);
		}
		
	    Griglia griglia = nave.getGriglia();
	    int altezza = griglia.getAltezza();
	    int larghezza = griglia.getLarghezza();
	    int posizioneInterna; // rappresenta la posizione di impatto secondo le coordinate interne della matrice, diverse da quelle di gioco

	    // caso 1: meteorite da NORD o SUD, la "posizione" indica la colonna
	    if (getDirezioneProvenienza() == Direzione.NORD || getDirezioneProvenienza() == Direzione.SUD) {
	        int minRiga = 0;
	        int maxRiga = altezza - 1;
	        posizioneInterna = nave.getLivello().convertiDaGiocoAReali(0, getPosizione()).getColonna();

	        for (int i = minRiga; i <= maxRiga; i++) {
	        	
	            // livello III: se proviene da SUD, possiamo estendere a colonne adiacenti
	            int[] colonneDaControllare;
	            if (nave.getLivello() == Livello.III && getDirezioneProvenienza() == Direzione.SUD) {
	                colonneDaControllare = new int[] { posizioneInterna - 1, posizioneInterna, posizioneInterna + 1 };
	            } else {
	                colonneDaControllare = new int[] { posizioneInterna };
	            }

	            for (int col : colonneDaControllare) {
	                if (col < 0 || col >= larghezza) continue;

	                Cella cella = griglia.getCella(i, col);
	                if (cella == null || cella.getTessera() == null) continue;

	                if (cella.getTessera().colpisceVerso(getDirezioneProvenienza())) {
	                    return true;
	                }
	            }
	        }

	    // caso 2: meteorite da EST o OVEST, la "posizione" indica la riga
	    } else {
	    	
	        int minColonna = 0;
	        int maxColonna = larghezza - 1;
	        posizioneInterna = nave.getLivello().convertiDaGiocoAReali(getPosizione(), 0).getRiga();

	        int[] righeDaControllare;
	        if (nave.getLivello() == Livello.I) {
	            righeDaControllare = new int[] { posizioneInterna };
	        } else { // livelli II e III
	            righeDaControllare = new int[] { posizioneInterna - 1, posizioneInterna, posizioneInterna + 1 };
	        }

	        for (int riga : righeDaControllare) {
	            if (riga < 0 || riga >= altezza) continue;

	            for (int j = minColonna; j <= maxColonna; j++) {
	                Cella cella = griglia.getCella(riga, j);
	                if (cella == null || cella.getTessera() == null) continue;

	                if (cella.getTessera().colpisceVerso(getDirezioneProvenienza())) {
	                    return true;
	                }
	            }
	        }
	    }

	    return false;
	}

}