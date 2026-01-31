package galaxytrucker.src.view.base;

import java.awt.event.MouseEvent;

/**
 * Interfaccia che definisce una callback da eseguire al clic su una cella di una griglia.
 * Viene utilizzata nel contesto di questo progetto al fine di gestire i click sulla plancia della nave.
 *<p>
 * È possibile implementare questa interfaccia per eseguire operazioni specifiche
 * in base alla posizione cliccata o alle caratteristiche del {@link MouseEvent}.
 * </p>
 */
public interface AzioneClickCella {

    /**
     * Metodo invocato quando una cella viene cliccata.
     *
     * @param riga   la riga della cella cliccata.
     * @param colonna la colonna della cella cliccata.
     * @param evento l'evento del mouse associato al clic (click sinistro, destro,...).
     */
    void onClickCella(int riga, int colonna, MouseEvent evento);
}