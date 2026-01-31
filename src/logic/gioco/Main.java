package galaxytrucker.src.logic.gioco;

import galaxytrucker.src.view.frames.IntroGui;

/**
 * Classe principale del progetto Galaxy Trucker.
 * <p>
 * Questo è il punto di ingresso dell'applicazione.
 * All'avvio, viene inizializzata l'interfaccia grafica introduttiva {@link IntroGui}.
 * <p>
 * Il progetto è una riproduzione del gioco da tavolo "Galaxy Trucker",
 * progettato per essere utilizzato in locale in base ad un sistema a turni.
 * 
 * @author [Zineb Chakraoui, Eleonora Di Martino, Richard Meoli, Alessandro Matteo Moncayo Saltos]
 * @version 1.0
 */

public class Main {

    /**
     * Metodo principale che avvia l'applicazione.
     * Viene caricata l'interfaccia introduttiva del gioco {@link IntroGui}.
     *
     * @param args eventuali argomenti da linea di comando (attualmente non utilizzati)
     */
    public static void main(String[] args) {
        new IntroGui();
    }
}