package monopoly.model.fields;

import monopoly.model.Player;

/**
 * @author Niels Norberg
 */
public class ShippingCompany extends OwnableField {
    private int rent;

    public ShippingCompany(String name, int number, int rent, int price) {
        super(name, number, price);
        this.rent = rent;
    }

    @Override
    public void fieldEvent(Player player) {
        if (getOwner() == null) {
            System.out.println(player.getName() + " landed on " + getName() + " which is unowned");
            return;
        }

        if (player != getOwner()) {
            int tempPrice = 0;
            for (int i = 0; i < getOwner().getOwnedFields().size(); i++) {
                if (getOwner().getOwnedFields().get(i) instanceof ShippingCompany) {
                    tempPrice += rent;
                }
            }
            if (!player.canPay(tempPrice * (tempPrice == 4 * rent ? 2 : 1))) {
                System.out.println(player.getName() + " landed on " + getName() + " and is unable to pay " + getOwner().getName());
                player.pay(player.getMoney(), getOwner());
            } else {
                System.out.println(player.getName() + " landed on " + getName() + " which is owned by " + getOwner().getName() + " and paid rent of " + tempPrice);
                player.pay(tempPrice, getOwner());
            }
        }
    }
}
