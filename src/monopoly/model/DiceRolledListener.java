package monopoly.model;

/**
 * @author Niels Norberg
 */
public interface DiceRolledListener {
    /**
     * Will be called when the dice is rolled in a dice class which is being listened to.
     * Is called after the dice roll is done
     *
     * @param dice the dice that has been rolled
     */
    void diceRolled(Dice dice);
}
