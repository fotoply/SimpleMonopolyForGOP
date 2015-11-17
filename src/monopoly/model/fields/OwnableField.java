package monopoly.model.fields;

import monopoly.model.Player;

/**
 * @author Niels Norberg
 */
public abstract class OwnableField implements FieldInterface {

    private String name;
    private int number;
    private int price;
    private Player owner = null;

    public OwnableField(String name, int number, int price) {
        this.name = name;
        this.number = number;
        this.price = price;
    }

    public boolean canBuy(Player player) {
        return player.canPay(price) && getOwner() == null;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public int getNumber() {
        return number;
    }

    /**
     * Is called when any player lands on this field.
     *
     * @param player which player has just landed on the field
     */
    public abstract void fieldEvent(Player player); // Is required but not declared by default

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public Player getOwner() {
        return owner;
    }

    public void setOwner(Player owner) {
        this.owner = owner;
    }

    @Override
    public String toString() {
        return "OwnableField{" +
                "number=" + number +
                ", name='" + name + '\'' +
                '}';
    }
}
