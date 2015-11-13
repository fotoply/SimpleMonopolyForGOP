package monopoly.model.fields;

import monopoly.model.Player;

/**
 * @author Niels Norberg
 */
public class StreetField extends OwnableField {
    private int rent;
    private int upgrades = 0;

    public StreetField(String name, int number, int rent, int price) {
        super(name, number, price);
        this.rent = rent;
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
