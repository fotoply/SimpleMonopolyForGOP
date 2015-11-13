package monopoly.model.fields;

import monopoly.model.MonopolyConstants;

/**
 * Represents a single field on the board.
 *
 * @author Niels Norberg
 */
public class Field {
    private String name;
    private int number;

    /**
     * Initialises the field. Will set it's name and ID number.
     *
     * @param name   name of the field. Can be anything and does not have to be unique
     * @param number ID number for the field. Should always be unique
     * @throws IllegalArgumentException is thrown if the ID passed is higher than the static <code>boardMaxSize</code>
     */
    public Field(String name, int number) {
        this.name = name;
        if (number < 0 || number > MonopolyConstants.BOARDSIZE - 1) { // If the number is not valid then throw an exception
            throw new IllegalArgumentException("Field number too high or too low compared to board size: " + (MonopolyConstants.BOARDSIZE - 1) + " max");
        }
        this.number = number;
    }

    public String getName() {
        return name;
    }

    public int getNumber() {
        return number;
    }

    @Override
    public String toString() {
        return "Name: " + name + "\tPosition: " + number;
    }
}
