package monopoly.model.fields;

import monopoly.model.Player;

/**
 * @author Niels Norberg
 */
public interface FieldInterface {
    String getName();

    int getNumber();

    /**
     * Triggers when a player lands on the field
     * Is triggered
     *
     * @param player which player has just landed on the field
     */
    void fieldEvent(Player player);


}
