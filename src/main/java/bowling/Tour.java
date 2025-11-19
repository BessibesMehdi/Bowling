package bowling;

public class Tour {
    private int numeroTour;
    private int boulelancer;
    protected  int quilleseliminee1;
    protected  int quilleseliminee2;    

public Tour(int numeroTour) {
        this.numeroTour = numeroTour;
        this.boulelancer = 1;
        this.quilleseliminee1 = 0;
        this.quilleseliminee2 = 0;        
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

}
