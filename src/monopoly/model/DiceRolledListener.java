package monopoly.model;

/**
 * @author Niels Norberg
 */
public interface DiceRolledListener {
    /**
     * Will be called when the dice is rolled in a dice class
     *
     * @param dice the dice that has been rolled
     */
    public void diceRolled(Dice dice);
}
