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
            return;
        }

        if (player != getOwner()) {
            int tempPrice = 0;
            for (int i = 0; i < getOwner().getOwnedFields().size(); i++) {
                if (getOwner().getOwnedFields().get(i) instanceof ShippingCompany) {
                    tempPrice += rent;
                }
            }
            player.pay(tempPrice, getOwner());
        }
    }
}
