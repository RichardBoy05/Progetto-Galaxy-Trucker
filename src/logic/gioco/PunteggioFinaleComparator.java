package galaxytrucker.src.logic.gioco;

import java.util.Comparator;

/**
 * {@code PunteggioFinaleComparator} è un comparatore che ordina oggetti {@link Giocatore}
 * in base al loro punteggio finale in ordine decrescente (dal punteggio maggiore al minore).
 * È utilizzato per generare la classifica finale della partita.
 *
 * @see Giocatore
 */
public class PunteggioFinaleComparator implements Comparator<Giocatore> {

	/**
	 * Confronta due oggetti {@code Giocatore} in base al loro punteggio finale.
	 *
	 * @param g1 il primo giocatore da confrontare
	 * @param g2 il secondo giocatore da confrontare
	 * @return un valore negativo se {@code g1} ha un punteggio maggiore di {@code g2},
	 *         zero se hanno lo stesso punteggio,
	 *         un valore positivo se {@code g1} ha un punteggio minore di {@code g2}.
	 */
    @Override
    public int compare(Giocatore g1, Giocatore g2) {
        return Integer.compare(g2.getPunteggioFinale(), g1.getPunteggioFinale()); // ordinamento DECRESCENTE
    }
}