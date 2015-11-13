package monopoly.model.fields;

import monopoly.model.Player;

/**
 * @author Niels Norberg
 *         Represents a normal street which can be bought. No groupings are yet implemented.
 *         Houses and hotels (upgrades) are not implemented yet either.
 */
public class StreetField extends OwnableField {
    private int rent;
    private int upgrades = 0; // To be used for houses
    private int groupId; // To be used for coloring and prices and whether upgrade is possible

    public StreetField(String name, int number, int rent, int price, int groupId) {
        super(name, number, price);
        this.rent = rent;
        this.groupId = groupId;
    }

    @Override
    public void fieldEvent(Player player) {
        if (getOwner() == null) {
            player.buyField(this);
            return;
        }

        if (player == getOwner()) {
            System.out.println(getOwner().getName() + " landed on " + getName() + " which is owned by him/her");
        } else {
            if (!player.pay(rent * (upgrades + 1), getOwner())) {
                System.out.println(player.getName() + " landed on " + getName() + " is unable to pay " + getOwner().getName());
            } else {
                System.out.println(player.getName() + " paid " + rent + " to " + getOwner().getName());
            }
        }
    }


}
