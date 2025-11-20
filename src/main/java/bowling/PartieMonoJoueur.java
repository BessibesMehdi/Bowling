package bowling;

/**
 * Enregistrement des lancers et calcul du score via un tableau de lancers.
 */
public class PartieMonoJoueur {

    private final int[] rolls = new int[21 + 2]; // un peu de marge pour les strikes bonus
    private int currentRoll = 0;
    private int currentFrame = 1;
    private int ballInFrame = 1; // 1..3 (trois possible au 10e)
    private boolean finished = false;
    private int firstRollOf10 = 0;

    public PartieMonoJoueur() {
        // rien à construire via Tour ici
    }

    /**
     * Enregistrer un lancer.
     *
     * @param nombreDeQuillesAbattues
     * @return vrai si le joueur doit relancer pour finir le même tour, faux sinon
     */
    public boolean enregistreLancer(int nombreDeQuillesAbattues) {
        if (finished) {
            throw new IllegalStateException("La partie est finie.");
        }
        // mémoriser lancer
        rolls[currentRoll++] = nombreDeQuillesAbattues;

        int prevFrame = currentFrame;

        if (currentFrame <= 9) {
            if (ballInFrame == 1) {
                if (nombreDeQuillesAbattues == 10) {
                    // strike -> on passe au tour suivant
                    currentFrame++;
                    ballInFrame = 1;
                } else {
                    // on attend le second lancer du même tour
                    ballInFrame = 2;
                }
            } else { // ballInFrame == 2
                // fin du tour normal
                currentFrame++;
                ballInFrame = 1;
            }
        } else { // 10ème tour (special rules)
            if (ballInFrame == 1) {
                firstRollOf10 = nombreDeQuillesAbattues;
                ballInFrame = 2;
            } else if (ballInFrame == 2) {
                // si strike au 1er ou spare entre 1er et 2ème -> troisième lancer autorisé
                if (firstRollOf10 == 10 || (firstRollOf10 + nombreDeQuillesAbattues) == 10) {
                    ballInFrame = 3;
                } else {
                    // pas de troisième lancer -> fin du jeu
                    finished = true;
                    ballInFrame = 1;
                    currentFrame = 0; // jeu terminé -> numéroTourCourant doit retourner 0
                }
            } else { // ballInFrame == 3
                // après 3ème lancer fin du jeu
                finished = true;
                ballInFrame = 1;
                currentFrame = 0;
            }
        }

        // retourne vrai si on doit relancer pour finir LE MEME tour (même numéro de tour)
        boolean doitRelancerDansCeTour = (!finished) && (prevFrame == currentFrame);
        return doitRelancerDansCeTour;
    }

    /**
     * Calcule le score actuel. Si des lancers futurs manquent, ils valent 0.
     */
    public int score() {
        int score = 0;
        int rollIndex = 0;
        for (int frame = 1; frame <= 10; frame++) {
            if (isStrikeAt(rollIndex)) {
                score += 10 + rollsSafe(rollIndex + 1) + rollsSafe(rollIndex + 2);
                rollIndex += 1;
            } else if (isSpareAt(rollIndex)) {
                score += 10 + rollsSafe(rollIndex + 2);
                rollIndex += 2;
            } else {
                score += rollsSafe(rollIndex) + rollsSafe(rollIndex + 1);
                rollIndex += 2;
            }
        }
        return score;
    }

    private boolean isStrikeAt(int idx) {
        return rollsSafe(idx) == 10;
    }

    private boolean isSpareAt(int idx) {
        return rollsSafe(idx) + rollsSafe(idx + 1) == 10;
    }

    private int rollsSafe(int idx) {
        if (idx < 0) return 0;
        if (idx >= rolls.length) return 0;
        return rolls[idx];
    }

    public boolean estTerminee() {
        return finished;
    }

    public int numeroTourCourant() {
        return estTerminee() ? 0 : currentFrame;
    }

    public int numeroProchainLancer() {
        return estTerminee() ? 0 : ballInFrame;
    }

}
