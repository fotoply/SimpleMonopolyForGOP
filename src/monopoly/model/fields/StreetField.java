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

    /**
     * Returns whether it is possible to buy upgrades for this street
     *
     * @return true if possible
     */
    public boolean canUpgrade() {
        return getOwner() != null && getOwner().canPay(rent / 2) && getUpgrades() < 5 && ownsAllInGroup();
    }

    /**
     * Checks if the player owns all fields in this color group.
     *
     * @return true if the player owns all fields in group otherwise false
     */
    public boolean ownsAllInGroup() {
        if (getOwner() == null) {
            return false;
        }

        int othersFound = 0; // IF this goes to 3 then all fields are owned, except if it is among the last 2 streets.
        for (OwnableField field : getOwner().getOwnedFields()) {
            if (field instanceof StreetField) {
                if (((StreetField) field).getGroupId() == getGroupId()) {
                    othersFound++;
                }
            }
        }

        return (othersFound == 2) || (othersFound == 1 && (getNumber() == 37 || getNumber() == 39));

    }

    /**
     * Adds 1 to the upgrade variable and makes the owner pay half the rent for the upgrade.
     */
    public void upgrade() {
        if (canUpgrade()) {
            getOwner().pay(rent / 2);
            setUpgrades(getUpgrades() + 1);
        }
    }

    @Override
    public void fieldEvent(Player player) {
        if (getOwner() == null) {
            System.out.println(player.getName() + " landed on " + getName() + " which is unowned");
            return;
        }

        if (player == getOwner()) {
            System.out.println(getOwner().getName() + " landed on " + getName() + " which is owned by him/her");
        } else {
            if (!player.canPay(rent * (upgrades + 1 + (ownsAllInGroup() ? 1:0)))) {
                System.out.println(player.getName() + " landed on " + getName() + " is unable to pay " + getOwner().getName());
                player.pay(player.getMoney(),getOwner());
            } else {
                player.pay(rent * (getUpgrades() + 1),getOwner());
                System.out.println(player.getName() + " paid " + rent + " to " + getOwner().getName());
            }
        }
    }

    public int getUpgrades() {
        return upgrades;
    }

    public void setUpgrades(int upgrades) {
        this.upgrades = upgrades;
    }

    public int getGroupId() {
        return groupId;
    }

    public void setGroupId(int groupId) {
        this.groupId = groupId;
    }
}
