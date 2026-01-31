package galaxytrucker.src.logic.volo;

import galaxytrucker.src.logic.assemblaggio.Cella;
import galaxytrucker.src.logic.assemblaggio.Direzione;
import galaxytrucker.src.logic.assemblaggio.Griglia;
import galaxytrucker.src.logic.assemblaggio.Nave;
import galaxytrucker.src.logic.gioco.Giocatore;

/**
 * Classe astratta che rappresenta un colpo (meteorite o cannonata) che impatta la nave.
 * Ogni colpo ha una posizione di impatto, una {@link DimensioneColpo} (PICCOLO o GRANDE)
 * e una {@link Direzione} di provenienza.
 */
public abstract class Colpo {
			
		/** 
		 * Posizione di impatto, può essere una riga (se {@code direzioneProvenienza} è {@code EST} o {@code OVEST}),
		 * oppure una colonna (se {@code direzioneProvenienza} è {@code NORD} o {@code SUD}).
		 */
        private final int posizione;
        
        /** 
         * Dimensione del colpo (PICCOLO o GRANDE).
         * @see DimensioneColpo
         */
        private final DimensioneColpo dimensione;
        
        /** 
         * Direzione di provenienza del colpo.
         * @see Direzione
         */
        private final Direzione direzioneProvenienza;
        
        
        /**
         * Costruttore della classe.
         *
         * @param posizione la posizione di impatto.
         * @param dimensione la dimensione del colpo ({@code PICCOLO} o {@code GRANDE})
         * @param direzioneProvenienza la direzione da cui proviene il colpo.
         * @throws NullPointerException se dimensione o direzioneProvenienza sono {@code null}.
         */
        public Colpo(int posizione, DimensioneColpo dimensione, Direzione direzioneProvenienza) {
             
             if(dimensione == null) {
            	 String errore = "Il parametro 'dimensione' non può essere nullo!";
            	 Evento.getLogger().error(errore);
                 throw new NullPointerException(errore);
             }
             
             if(direzioneProvenienza == null) {           	 
            	 String errore = "Il parametro 'direzioneProvenienza' non può essere nullo!";
            	 Evento.getLogger().error(errore);
                 throw new NullPointerException(errore);
             }
             
             this.posizione=posizione;
             this.dimensione = dimensione;
             this.direzioneProvenienza = direzioneProvenienza;
         }
        
        /**
         * Metodo astratto che deve essere implementato per applicare gli effetti
         * del colpo (meteorite/cannonata) sulla griglia della nave.
         *
         * @param nave l'istanza di Nave su cui applicare l'impatto.
         * @return la stringa che descrive all'utente l'esito dell'eventuale impatto.
         */
        public abstract String gestisciImpatto(Giocatore giocatore);
        
        /**
         * Trova la prima cella colpita dal colpo nella nave.
         * La ricerca avviene lungo la direzione del colpo a partire dal bordo della nave.
         *
         * @param nave la nave da controllare
         * @return la prima cella non vuota trovata, o null se nessuna cella è occupata
         */
        protected Cella trovaCellaColpita(Nave nave) {
            Griglia griglia = nave.getGriglia();
            int larghezza = griglia.getLarghezza();
            int altezza = griglia.getAltezza();
            int posizioneInterna; // rappresenta la posizione di impatto secondo le coordinate interne della matrice, DIVERSE da quelle di gioco

            switch (getDirezioneProvenienza()) {
                case EST:          	
                	posizioneInterna = nave.getLivello().convertiDaGiocoAReali(posizione, 0).getRiga();
                	
                    if (posizioneInterna < 0 || posizioneInterna >= altezza) return null;
                    for (int colonna = larghezza - 1; colonna >= 0; colonna--) {
                        Cella c = griglia.getCella(posizioneInterna, colonna);
                        if (c != null && c.getTessera() != null) return c;
                    }
                    return null;

                case OVEST:
                	posizioneInterna = nave.getLivello().convertiDaGiocoAReali(posizione, 0).getRiga();
                	
                    if (posizioneInterna < 0 || posizioneInterna >= altezza) return null;
                    for (int colonna = 0; colonna < larghezza; colonna++) {
                        Cella c = griglia.getCella(posizioneInterna, colonna);
                        if (c != null && c.getTessera() != null) return c;
                    }
                    return null;

                case NORD:
                	posizioneInterna = nave.getLivello().convertiDaGiocoAReali(0, posizione).getColonna();
                	
                    if (posizioneInterna < 0 || posizioneInterna >= larghezza) return null;
                    for (int riga = 0; riga < altezza; riga++) {
                        Cella c = griglia.getCella(riga, posizioneInterna);
                        if (c != null && c.getTessera() != null) return c;
                    }
                    return null;

                case SUD:
                	posizioneInterna = nave.getLivello().convertiDaGiocoAReali(0, posizione).getColonna();
                	
                    if (posizioneInterna < 0 || posizioneInterna >= larghezza) return null;
                    for (int riga = altezza - 1; riga >= 0; riga--) {
                        Cella c = griglia.getCella(riga, posizioneInterna);
                        if (c != null && c.getTessera() != null) return c;
                    }
                    return null;

                default:
                    return null;
            }
        }
        
        // getters
        
        /**
         * Restituisce la posizione di impatto del colpo.
         * La posizione di impatto, può essere una riga (se {@code direzioneProvenienza} è {@code EST} o {@code OVEST}),
		 * oppure una colonna (se {@code direzioneProvenienza} è {@code NORD} o {@code SUD}).
         *
         * @return la posizione di impatto.
         */
        public int getPosizione() {
			return posizione;
		}

        /**
         * Restituisce la dimensione del colpo.
         * 
         * @return la dimensione del colpo (PICCOLO o GRANDE).
         * @see DimensioneColpo
         */
        public DimensioneColpo getDimensione() {
            return dimensione;
        }

        /**
         * Restituisce la direzione da cui proviene il colpo.
         *
         * @return la direzione di provenienza.
         * @see Direzione
         */
        public Direzione getDirezioneProvenienza() {
            return direzioneProvenienza;
        }
        
}