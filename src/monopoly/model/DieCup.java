package monopoly.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

/**
 * Same as Dice class, except it allows any number of die in the cup
 *
 * @author Niels Norberg
 */
public class DieCup {
    private ArrayList<Die> dices = new ArrayList<>();

    /**
     * Initialises the cup with the specified amount of dices with the specified amount of sides
     *
     * @param dieCount how many dices are supposed to be in the cup
     * @param dieSides how many sides the dices are supposed to have
     */
    public DieCup(int dieCount, int dieSides) {
        this.dices = new ArrayList<>();
        for (int i = 0; i < dieCount; i++) {
            this.dices.add(new Die(dieSides));
        }
    }

    /**
     * Main method for testing
     *
     */
    public static void main(String[] args) {
        DieCup cup = new DieCup(0, 0);

        Scanner scanner = new Scanner(System.in);

        while (true) {
            String response = scanner.nextLine();

            if (!response.contains("d")) {
                System.out.println(response + " is not a valid die, try again");
            }

            ArrayList<String> dieInputs = new ArrayList<>();
            Collections.addAll(dieInputs, response.split(" "));

            for (String dieString : dieInputs) {
                String[] construct = dieString.split("d");
                if (construct.length > 2 || construct.length < 2) {
                    System.out.println(dieString + " is not a valid die, try again");
                    break;
                }
                try {
                    for (int i = 0; i < Integer.parseInt(construct[0]); i++) {
                        Die tempDie = new Die(Integer.parseInt(construct[1]));
                        cup.addDie(tempDie);
                    }
                } catch (Exception e) {
                    System.out.println(dieString + " is not a valid die, try again");
                    break;
                }
            }

            cup.throwDice();
            System.out.println("");
            System.out.println("Sum: " + cup.getSum());

            //System.out.println(cup);

            cup.dices.clear();
        }


        /*DieCup cup = new DieCup(3, 6);
        cup.addDie(new Die(5));
        cup.addDie(new Die(2));
        for (int i = 0; i < 10; i++) {
            System.out.println(cup.throwDice());
            System.out.println("Same: " + cup.sameFace());
            System.out.println(Arrays.toString(cup.dices.toArray()));
        }*/
    }

    /**
     * Adds a single die to the cup
     *
     * @param die the die to add
     */
    public void addDie(Die die) {
        dices.add(die);
    }

    /**
     * Removes a die from the cup. The die is removed based on the amount of sides it has
     *
     * @param sides which sided die to remove
     * @return True if the die was found and remove <br>
     * False if the die was not found and therefor not removed
     */
    public boolean removeDie(int sides) {
        for (int i = 0; i < dices.size(); i++) {
            if (dices.get(i).getSides() == sides) {
                dices.remove(i);
                return true;
            }
        }
        return false;
    }

    /**
     * Rolls all of the die in the cup
     *
     * @return The sum of the resulting faces in the cup
     */
    public int throwDice() {
        int sum = 0;
        for (Die dice : dices) {
            sum += dice.rollDie();
            System.out.print(dice.getResult() + " ");
        }
        return sum;
    }

    /**
     * Returns if all the die in the cup are the same
     *
     * @return Returns true if all die are on the same face otherwise returns false
     */
    public boolean sameFace() {
        for (int i = 0; i < dices.size() - 1; i++) {
            if (dices.get(i).getResult() != dices.get(i + 1).getResult()) {
                return false;
            }
        }
        return true;
    }

    /**
     * Calculate the sum of all the die in the cup, based on their current face
     *
     * @return the sum of all the die in the cup
     */
    public int getSum() {
        int sum = 0;
        for (Die dice : dices) {
            sum += dice.rollDie();
        }
        return sum;
    }

    @Override
    public String toString() {
        String result = "";
        for (Die die : dices) {
            result += die.getResult() + " ";
        }
        return result;
    }
}
