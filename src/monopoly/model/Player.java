package monopoly.model;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import monopoly.model.MonopolyConstants.Colors;
import monopoly.model.fields.FieldInterface;
import monopoly.model.fields.OwnableField;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Represents a player in the board game.
 *
 * @author Niels Norberg
 */
public class Player {
    // moneyProperty
    private final IntegerProperty moneyProperty = new SimpleIntegerProperty(this, "money");
    // posProperty
    private final IntegerProperty posProperty = new SimpleIntegerProperty(this, "pos");
    // roundsProperty
    private final IntegerProperty roundsProperty = new SimpleIntegerProperty(this, "rounds");
    // nameProperty
    private final StringProperty nameProperty = new SimpleStringProperty(this, "name");
    private Colors color; // The color of the player
    private boolean playerPassedStart; // If a player passed start in the current turn
    private ObservableList<OwnableField> ownedFields = FXCollections.observableArrayList();
    private ArrayList<PlayerListener> listeners = new ArrayList<>();
    private boolean jailed = false;

    /**
     * Initialises the player
     *
     * @param name  name of the player to be displayed. Does not have to be unique
     * @param pos   position of the board of the player. Pos 0 = Field1
     * @param color color of the player
     * @param money starting money of the player
     */
    public Player(String name, int pos, Colors color, int money) {
        setName(name);
        setPos(pos);
        this.color = color;
        setRounds(0);
        setMoney(money);
    }

    /**
     * Initialises the player
     *
     * @param name  name of the player to be displayed. Does not have to be unique
     * @param pos   position of the board of the player. Pos 0 = Field1
     * @param color color of the player
     */
    public Player(String name, int pos, Colors color) {
        this(name, pos, color, MonopolyConstants.START_MONEY);
    }
    /**
     * Initialises the player, can be overloaded with position and color
     *
     * @param name The name of the player to be displayed. Does not have to be unique
     */
    public Player(String name) {
        this(name, 0, Colors.values()[(int) (Math.random() * Colors.values().length)]);
    }

    public final StringProperty nameProperty() {
        return nameProperty;
    }

    public final String getName() {
        return nameProperty.get();
    }

    public final void setName(String value) {
        nameProperty.set(value);
    }

    public final IntegerProperty roundsProperty() {
        return roundsProperty;
    }

    public final int getRounds() {
        return roundsProperty.get();
    }

    public final void setRounds(int value) {
        roundsProperty.set(value);
    }

    public final IntegerProperty posProperty() {
        return posProperty;
    }

    public final int getPos() {
        return posProperty.get();
    }

    public final void setPos(int value) {
        posProperty.set(value);
        for (int i = 0; i < listeners.size() && listeners.get(i) != null; i++) {
            listeners.get(i).playerPositionChanged(this);
        }
    }

    public final IntegerProperty moneyProperty() {
        return moneyProperty;
    }

    public final int getMoney() {
        return moneyProperty.get();
    }

    public final void setMoney(int value) {
        moneyProperty.set(value);
    }

    /**
     * Adds a player listener to this player, which will be called when any of the relevant events occur
     *
     * @param listener a class implementing the PlayerListener interface
     */
    public void addListener(PlayerListener listener) {
        listeners.add(listener);
    }

    /**
     * Moves the player distance. Will wrap around when the player reaches the 40'th field.
     *
     * @param distance The distance to move the player
     */
    public void move(int distance) {
        for (PlayerListener listener : listeners) {
            int newDistance = listener.playerMoveStart(this, distance); // Check if any of the listeners have cancelled the movement
            if (newDistance != 0) {
                if (newDistance < 0) {
                    return;
                } else {
                    distance = newDistance;
                }
            }
        }
        playerPassedStart = false;
        int oldPos = getPos();
        setPos((getPos() + distance) % MonopolyConstants.BOARDSIZE);
        if (oldPos > getPos() && !jailed) {
            playerPassedStart = true;
            for (int i = 0; i < listeners.size() && listeners.get(i) != null; i++) {
                listeners.get(i).playerPassedStart(this);
            }
        }
    }

    /**
     * Sends the player to the jail field and marks them as jailed
     */
    public void sendToJail() {
        jailed = true;
        setPos(MonopolyConstants.JAIL_POS);
    }

    /**
     * Returns whether a player can buy a field.
     *
     * @param field the field to be tested
     * @return true if the player can buy it otherwise false
     */
    public boolean canBuyField(FieldInterface field) {
        if (!(field instanceof OwnableField)) {
            return false;
        }

        OwnableField ownableField = (OwnableField) field;

        return canPay(ownableField.getPrice()) && ownableField.getOwner() == null;
    }

    /**
     * Attempts to buy the field for player. Will allow an already owned field to be bought (transfered)
     *
     * @param field which field to buy
     * @return true if the field was bought, otherwise false
     */
    public boolean buyField(OwnableField field) {
        if (canPay(field.getPrice())) {
            pay(field.getPrice());
            if (field.getOwner() != null) {
                field.getOwner().getOwnedFields().remove(field);
                field.getOwner().reward(field.getPrice());
            }
            field.setOwner(this);
            getOwnedFields().add(field);
            System.out.println(getName() + " bought " + field.getName() + " for " + field.getPrice());
            return true;
        } else {
            System.out.println(getName() + " tried to buy " + field.getName() + " but was out of money..");
        }
        return false;
    }

    /**
     * Subtracts amount of money form the player, if possible. Will not make player money go to negative
     *
     * @param amount how much money to subtract from the player. Can be negative
     * @return true if the operation succeeded otherwise false
     */
    public boolean pay(int amount) {
        if (canPay(amount)) {
            setMoney(getMoney() - amount);
            return true;
        }
        return false;
    }

    /**
     * Rewards the player with a given amount of money. Cannot be negative!
     *
     * @param amount how much money to give the player
     */
    public void reward(int amount) {
        if (amount < 0) {
            return;
        }
        setMoney(getMoney() + amount);
    }

    /**
     * Transfers money from the player it is called on to the recieving player
     *
     * @param amount          how much money to transfer. Can be negative
     * @param receivingPlayer the player to transfer the money to
     * @return true if the operation succeeded otherwise false
     */
    public boolean pay(int amount, Player receivingPlayer) {
        if (canPay(amount)) {
            pay(amount);
            receivingPlayer.reward(amount);
            return true;
        }
        return false;
    }

    /**
     * Check if a player can pay a specific amount
     *
     * @param amount check if the player can pay this much
     * @return true if the player can pay that much otherwise false
     */
    public boolean canPay(int amount) {
        return getMoney() >= amount;
    }

    public Colors getColor() {
        return color;
    }

    public ObservableList<OwnableField> getOwnedFields() {
        return ownedFields;
    }

    public void addRound() {
        setRounds(getRounds() + 1);
    }

    /**
     * @return whether the player has just passed start in their current turn
     */
    public boolean hasPlayerPassedStart() {
        return playerPassedStart;
    }

    public boolean isJailed() {
        return jailed;
    }

    public void setJailed(boolean jailed) {
        this.jailed = jailed;
    }

    @Override
    public String toString() {
        return "Player{" +
                "name='" + getName() + '\'' +
                ", pos=" + getPos() +
                ", color=" + color +
                ", rounds=" + getRounds() +
                ", playerPassedStart=" + playerPassedStart +
                ", money=" + getMoney() +
                ", ownedFields=" + Arrays.toString(ownedFields.toArray()) +
                ", listeners=" + listeners +
                ", jailed=" + jailed +
                '}';
    }
}
