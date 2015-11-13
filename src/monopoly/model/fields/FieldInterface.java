package monopoly.model.fields;

import monopoly.model.Player;

/**
 * @author Niels Norberg
 */
public interface FieldInterface {
    public String getName();

    public int getNumber();

    /**
     * Triggers when a player lands on the field
     *
     * @param player which player has just landed on the field
     */
    public void fieldEvent(Player player);


}
