package bowling;

public class Tour {
    private int numeroTour;
    private int boulelancer;
    protected Tour tourSuivant;
    protected int quilleseliminee1;
    protected int quilleseliminee2;

    public Tour(int numeroTour, Tour tourSuivant) {
        this.numeroTour = numeroTour;
        this.boulelancer = 1;
        this.quilleseliminee1 = 0;
        this.quilleseliminee2 = 0;
        this.tourSuivant = tourSuivant;
    }

    public void TouraFaireSuivant() {
        this.tourSuivant = new Tour(this.numeroTour + 1, null);
    }

    public int getNumeroTour() {
        return numeroTour;
    }

    public int getBoulelancer() {
        return boulelancer;
    }

    public void setBoulelancer(int boulelancer) {
        this.boulelancer = boulelancer;
    }

    public void enregistreLancer(int nombreDeQuillesAbattues) {
        if (boulelancer == 1) {
            quilleseliminee1 = nombreDeQuillesAbattues;
            boulelancer = 2;
        } else if (boulelancer == 2) {
            quilleseliminee2 = nombreDeQuillesAbattues;
            boulelancer = 3;
        } else {
            throw new IllegalStateException("Tous les lancers pour ce tour ont déjà été effectués.");
        }
    }

    public boolean estTermine() {
        // Un tour normal est terminé si strike (fin immédiate) ou si on a effectué 2 lancers
        return estUnStrike() || (boulelancer == 3);
    }

    public boolean estUnStrike() {
        return quilleseliminee1 == 10;
    }

    boolean estUnSpare() {
        return (quilleseliminee1 + quilleseliminee2) == 10;
    }

   
    public int rollAtOffset(int offset) {
        if (offset < 0) return 0;
        if (offset == 0) {
            return quilleseliminee1;
        }
        if (offset == 1) {
            // Si ce tour est un strike, le deuxième lancer demandé est le premier du suivant
            if (estUnStrike()) {
                return (tourSuivant != null) ? tourSuivant.rollAtOffset(0) : 0;
            } else {
                return quilleseliminee2;
            }
        }
        // offset >= 2 : avancer dans la chaîne en soustrayant le nombre de lancers de ce tour
        int lancersCeTour = estUnStrike() ? 1 : 2;
        if (tourSuivant == null) return 0;
        return tourSuivant.rollAtOffset(offset - lancersCeTour);
    }

    public int gainSpare() {
        // Bonus d'un spare = premier lancer du tour suivant (0 si absent)
        if (tourSuivant == null) return 0;
        return tourSuivant.rollAtOffset(0);
    }

    public int gainStrike() {
        // Bonus d'un strike = somme des deux lancers suivants (sécurisé via rollAtOffset)
        if (tourSuivant == null) return 0;
        return tourSuivant.rollAtOffset(0) + tourSuivant.rollAtOffset(1);
    }

    public int scoreTotal() {
        int scoreDeCeTour;
        if (estUnStrike()) {
            scoreDeCeTour = 10 + (tourSuivant != null ? tourSuivant.gainStrike() : 0);
        } else if (estUnSpare()) {
            scoreDeCeTour = 10 + (tourSuivant != null ? tourSuivant.gainSpare() : 0);
        } else {
            scoreDeCeTour = quilleseliminee1 + quilleseliminee2;
        }
        if (tourSuivant == null) {
            return scoreDeCeTour;
        }
        return scoreDeCeTour + tourSuivant.scoreTotal();
    }

    public Tour getSuivant() {
        return tourSuivant;
    }

    @Override
    public String toString() {
        return "Tour# " + numeroTour + ", premier Lancer: " + quilleseliminee1 + ", secondLancer: " + quilleseliminee2;
    }
}
