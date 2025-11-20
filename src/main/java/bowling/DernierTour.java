package bowling;

public class DernierTour extends Tour {
    private int quillesEliminees3;

    public DernierTour() {
        super(10, null);
        this.quillesEliminees3 = 0;
    }

    public int getquillesEliminees3() {
        return quillesEliminees3;
    }

    @Override
    public void enregistreLancer(int nombreDeQuillesAbattues) {
        if (getBoulelancer() == 1) {
            super.enregistreLancer(nombreDeQuillesAbattues);
        } else if (getBoulelancer() == 2) {
            super.enregistreLancer(nombreDeQuillesAbattues);
        } else if (getBoulelancer() == 3) {
            if (estUnStrike() || estUnSpare()) {
                quillesEliminees3 = nombreDeQuillesAbattues;
                setBoulelancer(4); // Indique que tous les lancers sont faits
            } else {
                throw new IllegalStateException("Pas de troisième lancer autorisé sans strike ou spare au deuxième lancer.");
            }
        } else {
            throw new IllegalStateException("Tous les lancers pour ce tour ont déjà été effectués.");
        }
    }

    /**
     * Surcharge de rollAtOffset pour exposer jusqu'au 3ème lancer du dernier tour.
     * offset 0 -> 1er lancer, 1 -> 2ème lancer, 2 -> 3ème lancer (si existant).
     */
    @Override
    public int rollAtOffset(int offset) {
        if (offset == 0) return super.quilleseliminee1;
        if (offset == 1) return super.quilleseliminee2;
        if (offset == 2) return quillesEliminees3;
        return 0;
    }

    @Override
    public boolean estTermine() {
        return (estUnStrike() || estUnSpare()) ? (getBoulelancer() == 4) : (getBoulelancer() == 3);
    }

    @Override
    public int gainStrike() {
        // Pour le dernier tour, les "deux lancers suivants" sont simplement les deux premières boules du 10e
        return super.quilleseliminee1 + super.quilleseliminee2;
    }

    @Override
    public int scoreTotal() {
        // Score du dernier tour = somme des 2 ou 3 lancers
        return super.quilleseliminee1 + super.quilleseliminee2 + quillesEliminees3;
    }
}
