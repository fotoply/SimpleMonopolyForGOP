package monopoly.model.fields;

import monopoly.model.Dice;
import monopoly.model.Player;

/**
 * @author Niels Norberg
 */
public class Brewery extends OwnableField {
    private Dice dices;

    public Brewery(String name, int number, Dice dice, int price) {
        super(name, number, price);
        dices = dice;
    }

    @Override
    public void fieldEvent(Player player) {
        if (getOwner() == null) {
            player.buyField(this);
            return;
        }

        if (player != getOwner()) {
            int tempPrice = dices.getSum();
            int brewsOwned = 0;
            for (int i = 0; i < getOwner().getOwnedFields().size(); i++) {
                if (getOwner().getOwnedFields().get(i) instanceof Brewery) {
                    brewsOwned += 1;
                }
            }
            player.pay(tempPrice * brewsOwned * 10, getOwner());
        }
    }
}
