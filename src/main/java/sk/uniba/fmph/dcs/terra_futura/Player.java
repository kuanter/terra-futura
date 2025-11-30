package sk.uniba.fmph.dcs.terra_futura;


public class Player {
    public final int id;

    public final Grid grid;

    // у игрока ровно 2 паттерна
    public final ActivationPattern[] activationPatterns = new ActivationPattern[2];

    // у игрока ровно 2 метода подсчёта очков
    public final ScoringMethod[] scoringMethods = new ScoringMethod[2];

    public Player(int id, Grid grid,
                  ActivationPattern firstPattern,
                  ActivationPattern secondPattern,
                  ScoringMethod firstScoring,
                  ScoringMethod secondScoring) {

        this.id = id;
        this.grid = grid;
        this.activationPatterns[0] = firstPattern;
        this.activationPatterns[1] = secondPattern;
        this.scoringMethods[0] = firstScoring;
        this.scoringMethods[1] = secondScoring;
    }
}
