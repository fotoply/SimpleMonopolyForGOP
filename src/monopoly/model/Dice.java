package monopoly.model;

import java.util.ArrayList;

/**
 * @author Niels Norberg
 */
public class Dice {
    private Die die1 = new Die(6);
    private Die die2 = new Die(6);
    private ArrayList<DiceRolledListener> listeners = new ArrayList<>();

    /**
     * Rolls both of the dices in the cup
     *
     * @return The sum of the two dices in the cup
     */
    public int throwDice() {
        int sum = die1.rollDie() + die2.rollDie();
        for (int i = 0; i < listeners.size() && listeners.get(i) != null; i++) {
            listeners.get(i).diceRolled(this);
        }
        return sum;
    }

    /**
     * Returns if the two die in the cup are the same
     *
     * @return Returns true if both die are on the same face otherwise returns false
     */
    public boolean isPair() {
        return die1.getResult() == die2.getResult();
    }

    /**
     * Returns the sum of the die in the cup
     *
     * @return the sum of the dices
     */
    public int getSum() {
        return die1.getResult() + die2.getResult();
    }

    /**
     * Adds a new listener for when the dice is rolled
     *
     * @param newListener the class which is listening
     */
    public void addListener(DiceRolledListener newListener) {
        listeners.add(newListener);
    }
}
