package monopoly.model;

import java.util.Random;

/**
 * @author Niels Norberg
 */
public class Die {
    private static Random rnd = new Random();
    private int result;
    private int sides;

    /**
     * Creates a dice which can be rolled for a random number between 1 and sides, both inclusive.
     *
     * @param sides The amount of sides the dices will have
     * @throws IllegalArgumentException is thrown if the amount of sides is illegal for a die
     */
    public Die(int sides) {
        if (sides <= 1) {
            throw new IllegalArgumentException();
        }
        this.sides = sides;
    }

    /**
     * Rolls the die
     *
     * @return A random number between 1 and sides
     */
    public int rollDie() {
        result = rnd.nextInt(sides) + 1;
        rnd.nextInt(2);
        return result;
    }

    public int getResult() {
        return result;
    }

    public int getSides() {
        return sides;
    }

    @Override
    public String toString() {
        return "Die: Result = " + result + "\tSides=" + sides;
    }
}
